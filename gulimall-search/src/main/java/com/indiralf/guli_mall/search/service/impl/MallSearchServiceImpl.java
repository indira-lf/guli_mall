package com.indiralf.guli_mall.search.service.impl;

import com.indiralf.guli_mall.search.config.GulimallElasticSearchConfig;
import com.indiralf.guli_mall.search.constant.EsConstant;
import com.indiralf.guli_mall.search.service.MallSearchService;
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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author
 * @time 2022/2/13 13:25
 * @Description- TODO
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public SearchResult search(SearchParam param) {
        SearchResult result = null;
        //准备检索请求
        SearchRequest searchRequest = buildSearchRequest(param);

        try {
            //执行检索请求
            SearchResponse response = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
            //分析响应数据封装成我们需要的格式
            result = buildSearchResult(response);
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
        boolQuery.filter(QueryBuilders.termQuery("hasStock",param.getHasStock()));

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

        /**
         * 聚合分析
         */

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);

        return searchRequest;
    }

    /**
     * 构建结果数据
     * @param response
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response) {
        return null;
    }
}
