package com.indiralf.guli_mall.search.controller;

import com.indiralf.guli_mall.search.service.MallSearchService;
import com.indiralf.guli_mall.search.vo.SearchParam;
import com.indiralf.guli_mall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @time 2022/2/13 12:45
 * @Description- TODO
 */
@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request){
        String queryString = request.getQueryString();
        param.set_queryString(queryString);
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result",result);
        return "list";
    }
}
