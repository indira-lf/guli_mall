package com.feng.gulimall.cart.controller;

import com.feng.gulimall.cart.interceptor.CartInterceptor;
import com.feng.gulimall.cart.service.CartService;
import com.feng.gulimall.cart.vo.Cart;
import com.feng.gulimall.cart.vo.CartItem;
import com.feng.gulimall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author
 * @time 2022/3/13 9:25
 * @Description- TODO
 */
@Controller
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/currentUserCartItems")
    @ResponseBody
    public List<CartItem> getCurrentUserCartItems(){

        return cartService.getUserCartItems();
    }

    @GetMapping("/cartItem")
    public String checkItem(@RequestParam("skuId")Long skuId,
                            @RequestParam("check") Integer check){
        cartService.checkItem(skuId,check);

        return "redirect:http://cart.gulimall.com/cart.html";
    }

    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId")Long skuId,
                            @RequestParam("num") Integer num){
        cartService.changeItemCount(skuId,num);

        return "redirect:http://cart.gulimall.com/cart.html";
    }
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId")Long skuId){
        cartService.deleteItem(skuId);

        return "redirect:http://cart.gulimall.com/cart.html";
    }
    /**
     * 浏览器有一个cookie:user-key标识用户身份，一个月后过期
     * 如果第一次使用jd的购物车功能,都会给一个临时的用户身份
     * 浏览器以后保存，每次访问都会带上这个cookie
     *
     * 登录:session有
     * 没登录:按照cookie里面带来user-key来做(第一次如果没有临时用户,创建一个)
     * @return
     */
    @GetMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        Cart cart = cartService.getCar();

        model.addAttribute("cart",cart);
        return "cartList";
    }

    /**
     * 添加商品到购物车
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes attributes) throws ExecutionException, InterruptedException {

        cartService.addToCart(skuId,num);

        attributes.addAttribute("skuId",skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }

    /**
     * 跳转到成功页
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,Model model){
        //重定向到成功页面
        CartItem item = cartService.getCartItem(skuId);
        model.addAttribute("item",item);
        return "success";
    }

}
