package com.indiralf.guli_mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.indiralf.guli_mall.product.dao.CategoryDao;
import com.indiralf.guli_mall.product.entity.CategoryEntity;
import com.indiralf.guli_mall.product.service.CategoryBrandRelationService;
import com.indiralf.guli_mall.product.service.CategoryService;
import com.indiralf.guli_mall.product.vo.Catelog2Vo;
import com.mysql.cj.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.common.utils.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 2021-11-28
     * 查询所有分类以及子分类，以树形结构组装起来
     * @return
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2.组成父子的树形结构
        //2.1找到所有的一级分类
        List<CategoryEntity> levelMenus = entities.stream().filter(categoryEntity ->
             categoryEntity.getParentCid() == 0
        ).map(menu -> {
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2) -> {
            return (menu1.getSort() == null ? 0:menu1.getSort()) - (menu2.getSort() == null ? 0: menu2.getSort());
        }).collect(Collectors.toList());

        return levelMenus;
    }

    /**
     * 2021-12-03
     * 删除
     * @param asList
     */
    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前删除的菜单，是否被别的地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);

        return  parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     * @CacheEvict 失效模式
     * @param category
     */
    @CacheEvict(value = "category",key = "'getLevelOneCategorys'")
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());

    }

    /**
     * 查询一级菜单
     * @return
     */
    @Cacheable(value = {"category"},key = "#root.method.name") //代表当前方法的结果需要缓存，如果缓存中有，方法不用调用，如果缓存中没有，调方法并写入缓存
    @Override
    public List<CategoryEntity> getLevelOneCategorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    /**
     * 查询三级菜单
     * //TODO 产生堆外内存溢出 OutOfDirectMemoryError
     * @return
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        /**
         * 1、空结果缓存: 解决缓存穿透
         * 2、设置过期时间(加随机值): 解决缓存雪崩
         * 3、加锁: 解决缓存击穿
         */
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String catalogJSON = operations.get("catalogJSON");
            //判断是否需要加入缓存
            catalogJSON = addRedisCache(catalogJSON,operations);
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON,new TypeReference<Map<String, List<Catelog2Vo>>>(){});

            return result;
    }

    public String addRedisCache(String catalogJSON,ValueOperations<String, String> operations){
        if (StringUtils.isEmpty(catalogJSON)){
            String uuid = UUID.randomUUID().toString();
            Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
            if (lock) {
//                redisTemplate.expire("lock",30, TimeUnit.SECONDS);//设置过期时间
                try {
                    Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDb();
                    catalogJSON = JSON.toJSONString(catalogJsonFromDb);
                    if (!StringUtils.isEmpty(catalogJSON)) {
                        operations.set("catalogJSON", catalogJSON, 1, TimeUnit.DAYS);
                    }
                }finally {
                    String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                    redisTemplate.execute(new DefaultRedisScript<>(script,Long.class),Arrays.asList("lock"),uuid);
                }
                //解锁
//                String lock1 = operations.get("lock");
//                if (lock1.equals(uuid)){
//                    redisTemplate.delete("lock");
//                }
                //使用lua脚本
            }else{
                addRedisCache(catalogJSON,operations);
            }
        }
        return catalogJSON;
    }

    //从数据库查询并封装分类数据
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {

        //查询所有
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        //1、查出所有一级分类
        List<CategoryEntity> levelOneCategorys = getParentCid(selectList,0L);

        //2、封装数据
        Map<String, List<Catelog2Vo>> parent_cid = levelOneCategorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //查询每个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParentCid(selectList,v.getCatId());
            //封装二级菜单数据
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //查找三级分类
                    List<CategoryEntity> level3Catelog = getParentCid(selectList,l2.getCatId());
                    if (level3Catelog != null){
                        List<Catelog2Vo.Catalog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            //封装成指定格式
                            Catelog2Vo.Catalog3Vo catelog3Vo = new Catelog2Vo.Catalog3Vo(l2.getCatId().toString(),l3.getCatId().toString(),l3.getName());

                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));
        return parent_cid;
    }

    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList,Long parentCid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid() == parentCid).collect(Collectors.toList());
        return collect;
    }

    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    /**
     * 2021-11-28
     * 递归查找所有菜单的子菜单
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //找子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity,all));
            return  categoryEntity;
        }).sorted((menu1,menu2) -> {
            //菜单排序
            return (menu1.getSort() == null ? 0:menu1.getSort()) - (menu2.getSort() == null ? 0: menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

}