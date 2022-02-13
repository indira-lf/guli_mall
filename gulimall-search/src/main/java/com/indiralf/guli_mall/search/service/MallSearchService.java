package com.indiralf.guli_mall.search.service;


import com.indiralf.guli_mall.search.vo.SearchParam;
import com.indiralf.guli_mall.search.vo.SearchResult;

/**
 * @author
 * @time 2022/2/13 13:26
 * @Description- TODO
 */
public interface MallSearchService {
    SearchResult search(SearchParam param);
}
