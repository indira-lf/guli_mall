package com.indiralf.guli_mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.indiralf.common.to.es.SkuEsModel;
import com.indiralf.common.utils.R;
import com.indiralf.guli_mall.search.config.GulimallElasticSearchConfig;
import com.indiralf.guli_mall.search.constant.EsConstant;
import com.indiralf.guli_mall.search.feign.ProductFeignService;
import com.indiralf.guli_mall.search.service.MallSearchService;
import com.indiralf.guli_mall.search.vo.AttrResponseVo;
import com.indiralf.guli_mall.search.vo.BrandVo;
import com.indiralf.guli_mall.search.vo.SearchParam;
import com.indiralf.guli_mall.search.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 * @time 2022/2/13 13:25
 * @Description- TODO
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam param) {
        SearchResult result = null;
        //准备检索请求
        SearchRequest searchRequest = buildSearchRequest(param);

        try {
            //执行检索请求
            SearchResponse response = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
            //分析响应数据封装成我们需要的格式
            result = buildSearchResult(response,param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 准备检索请求
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam param) {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /**
         * 模糊匹配，过滤(按照属性，分类，品牌，价格区间，库存)
         */
        //1、构建bool - query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //1.1 must - 模糊匹配
        if (!StringUtils.isEmpty(param.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",param.getKeyword()));
        }
        //1.2 bool - filter 按三级分类id查询
        if (!StringUtils.isEmpty(param.getCatalog3Id())){
            boolQuery.filter(QueryBuilders.termQuery("catalogId",param.getCatalog3Id()));
        }
        //1.2 bool - filter 按品牌id查询
        if (param.getBrandId() != null && param.getBrandId().size()>0){
            boolQuery.filter(QueryBuilders.termQuery("brandId",param.getBrandId()));
        }
        //1.2 bool - filter 按所有指定的属性进行查询
        if (param.getAttrs() != null && param.getAttrs().size() > 0){
            for (String attrStr : param.getAttrs()){
                BoolQueryBuilder nestBoolQuery = QueryBuilders.boolQuery();

                String[] s = attrStr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                nestBoolQuery.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nestBoolQuery.must(QueryBuilders.termQuery("attrs.attrValue",attrValues));

                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", null, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }

        //1.2 bool - filter 按照是否有库存进行查询
        if (param.getHasStock() != null){
            boolQuery.filter(QueryBuilders.termQuery("hasStock",param.getHasStock()));
        }

        //1.2 bool - filter 按照价格区间
        if (!StringUtils.isEmpty(param.getSkuPrice())){
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_");
            if (param.getSkuPrice().startsWith("_")){
                rangeQuery.lte(s[1]);
            }else if (param.getSkuPrice().endsWith("_")){
                rangeQuery.gte(s[0]);
            }else {
                rangeQuery.gte(s[0]);
                rangeQuery.lte(s[1]);
            }
            boolQuery.filter(rangeQuery);
        }
        sourceBuilder.query(boolQuery);

        /**
         * 排序，分页，高亮
         */
        //2.1 排序
        if (!StringUtils.isEmpty(param.getSort())){
            String sort = param.getSort();
            //sort=hotScore_asc/desc
            String[] s = sort.split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc")?SortOrder.ASC:SortOrder.DESC;
            sourceBuilder.sort(s[0],order);
        }

        //2.2 分页
        sourceBuilder.from((param.getPageNum()-1) * EsConstant.PRODUCT_PAGE_SIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE);

        //2.3 高亮
        if (!StringUtils.isEmpty(param.getKeyword())){
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");
            sourceBuilder.highlighter(builder);
        }
        /**
         * 聚合分析
         */
        //品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        //品牌聚合子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        //分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        sourceBuilder.aggregation(brand_agg);

        //属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        //聚合分析出当前attr_id对应的名字
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        //聚合分析出当前attr_id对应的所有可能的属性值
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);

        return searchRequest;
    }

    /**
     * 构建结果数据
     * @param response
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response,SearchParam param) {
        SearchResult result = new SearchResult();
        //1、返回的所有查询到的商品
        SearchHits hits = response.getHits();

        List<SkuEsModel> esModels = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0){
            for (SearchHit hit : hits.getHits()){
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(param.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    esModel.setSkuTitle(string);
                }
                esModels.add(esModel);
            }
        }
        result.setProducts(esModels);

        //2、属性信息
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            long attrId = bucket.getKeyAsNumber().longValue();
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(item -> {
                String keyAsString = ((Terms.Bucket) item).getKeyAsString();
                return keyAsString;
            }).collect(Collectors.toList());
            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);
            attrVos.add(attrVo);
        }

        //3、品牌信息
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()){
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            //品牌id
            long brandId = bucket.getKeyAsNumber().longValue();
            //品牌name
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            //品牌img
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandName(brandName);
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);
        }

        //4、分类信息
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets){
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));
            ParsedLongTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }

        //5、分页信息
        result.setPageNum(param.getPageNum());
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        int totalPages = (int) total % EsConstant.PRODUCT_PAGE_SIZE == 0 ? (int)total/EsConstant.PRODUCT_PAGE_SIZE : (int)(total/EsConstant.PRODUCT_PAGE_SIZE + 1);
        result.setTotalPages(totalPages);

        List<Integer> pageNavs = new ArrayList<>();
        for (int i=1; i<= totalPages; i++){
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);

        //6、构建面包屑导航功能
        if (param.getAttrs() != null && param.getAttrs().size() > 0){
            List<SearchResult.NavVo> navVos = param.getAttrs().stream().map(attr -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.attrsInfo(Long.parseLong(s[0]));
                result.getAttrIds().add(Long.parseLong(s[0]));
                if (r.getCode() == 0){
                    AttrResponseVo data = r.getData("attr", new TypeReference<AttrResponseVo>() {
                    });
                    navVo.setNavName(data.getAttrName());
                }else {
                    navVo.setNavName(s[0]);
                }
                String replace = replaceQueryString(param, attr,"attrs");
                navVo.setLink("http://search.gulimall.com/list.html?"+replace);
                return navVo;
            }).collect(Collectors.toList());

            result.setNavs(navVos);
        }

        //品牌、分类
        if (param.getBrandId() != null && param.getBrandId().size() > 0 ){
            List<SearchResult.NavVo> navs = result.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();
            navVo.setNavName("品牌");
            R r = productFeignService.brandsInfo(param.getBrandId());
            if (r.getCode() == 0){
                List<BrandVo> brand = r.getData("brand", new TypeReference<List<BrandVo>>() {
                });
                StringBuffer buffer = new StringBuffer();
                String replace = "";
                for (BrandVo brandVo : brand) {
                    buffer.append(brandVo.getName()+";");
                    replace = replaceQueryString(param, brandVo.getBrandId()+"","attrs");
                }
                navVo.setNavValue(buffer.toString());
                navVo.setLink("http://search.gulimall.com/list.html?"+replace);
            }

            navs.add(navVo);
        }

        return result;
    }

    public String replaceQueryString(SearchParam param,String attr,String key){
        String encode = null;
        try {
            encode = URLEncoder.encode(attr, "UTF-8");
            encode.replace("+","%20");//浏览器对空格编码和java不一样
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String replace = param.get_queryString().replace("&"+key+"=" + encode, "");

        return replace;
    }
}
