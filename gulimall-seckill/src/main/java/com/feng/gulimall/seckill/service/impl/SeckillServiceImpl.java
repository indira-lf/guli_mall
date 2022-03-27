package com.feng.gulimall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.feng.gulimall.seckill.To.SeckillSKuRedisTo;
import com.feng.gulimall.seckill.feign.CouponFeignService;
import com.feng.gulimall.seckill.service.SeckillService;
import com.feng.gulimall.seckill.vo.SeckillSessionWithSkus;
import com.indiralf.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 * @time 2022/3/27 12:58
 * @Description- TODO
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    private final String SESSIONS_CACHE_PREFIX = "seckill:session:";
    private final String SKUKILL_CACHE_PREFIX = "seckill:skus:";

    @Override
    public void uploadSeckillSkuLatest3Days() {
        //扫描需要参与描述的服务
        R session = couponFeignService.getLate3DaysSession();
        if (session.getCode() == 0){
            //上架商品
            List<SeckillSessionWithSkus> sessionData = session.getData(new TypeReference<List<SeckillSessionWithSkus>>() {
            });
            //缓存到redis
            saveSessionInfos(sessionData);
            saveSessionSkuInfos(sessionData);
        }

    }

    private void saveSessionInfos(List<SeckillSessionWithSkus> sessions){

        sessions.stream().forEach(session -> {
            Long startTime = session.getStartTime().getTime();
            Long endTime = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + startTime +"_"+ endTime;
            List<String> collect = session.getRelationSkus().stream().map(item -> item.getId().toString()).collect(Collectors.toList());
            redisTemplate.opsForList().leftPushAll(key,collect);
        });
    }
    private void saveSessionSkuInfos(List<SeckillSessionWithSkus> sessions){
        sessions.stream().forEach(session -> {
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            session.getRelationSkus().forEach(seckillSkuVo -> {

                SeckillSKuRedisTo redisTo = new SeckillSKuRedisTo();
                //sku基本信息

                //sku秒杀信息
                BeanUtils.copyProperties(seckillSkuVo,redisTo);
                String s = JSON.toJSONString(redisTo);
                ops.put(seckillSkuVo.getId(),s);
            });
        });
    }
}
