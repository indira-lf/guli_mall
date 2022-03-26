package com.feng.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.feng.gulimall.cart.feign.ProductFeignService;
import com.feng.gulimall.cart.interceptor.CartInterceptor;
import com.feng.gulimall.cart.service.CartService;
import com.feng.gulimall.cart.vo.Cart;
import com.feng.gulimall.cart.vo.CartItem;
import com.feng.gulimall.cart.vo.SkuInfoVo;
import com.feng.gulimall.cart.vo.UserInfoTo;
import com.indiralf.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author
 * @time 2022/3/13 9:04
 * @Description- TODO
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService feignService;

    @Autowired
    ThreadPoolExecutor executor;

    private final String CART_PREFIX = "gulimall:cart:";

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {

        BoundHashOperations<String, Object, Object> operations = extracted();

        String res = (String) operations.get(skuId.toString());
        if (StringUtils.isEmpty(res)){
            CartItem cartItem = new CartItem();

            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                //1、远程查询当前要添加的商品信息
                R skuInfo = feignService.getSkuInfo(skuId);
                SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                //2、商品添加到购物车
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(data.getPrice());
            },executor);

            //3、远程查询sku组合信息
            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                List<String> values = feignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(values);
            }, executor);

            CompletableFuture.allOf(getSkuInfoTask,getSkuSaleAttrValues).get();

            String s = JSON.toJSONString(cartItem);
            operations.put(skuId.toString(),s);

            return cartItem;
        }else {
            //购物车有此商品,修改数量
            CartItem cartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(cartItem.getCount()+num);
            operations.put(skuId.toString(),JSON.toJSONString(cartItem));
            return cartItem;
        }

    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = extracted();

        String str = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(str, CartItem.class);
        return cartItem;
    }

    @Override
    public Cart getCar() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() != null){
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);

            //如果临时购物车的数据还没有合并
            String tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> cartItems = getCartItems(tempCartKey);
            if (cartItems != null){
                for (CartItem cartItem : cartItems) {
                    addToCart(cartItem.getSkuId(),cartItem.getCount());
                }

                //清空购物车
                clearCart(tempCartKey);
            }

            //获取登录后的购物车
            List<CartItem> cartItems1 = getCartItems(cartKey);
            cart.setItems(cartItems1);

        }else {
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        }
        return cart;
    }

    @Override
    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> operations = extracted();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check == 1?true:false);
        String s = JSON.toJSONString(cartItem);
        operations.put(skuId.toString(),s);
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        BoundHashOperations<String, Object, Object> operations = extracted();
        operations.put(skuId,JSON.toJSONString(cartItem));
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> operations = extracted();
        operations.delete(skuId);
    }

    @Override
    public List<CartItem> getUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() == null){
            return null;
        }else {
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);
            List<CartItem> collect = cartItems.stream().filter(item -> item.getCheck())
                    .map(cartItem -> {
                        R price = feignService.getPrice(cartItem.getSkuId());
                        String data = (String) price.get("data");
                        cartItem.setPrice(new BigDecimal(data));
                        return cartItem;
                    })
                    .collect(Collectors.toList());
            return collect;
        }
    }

    /**
     * 获取到我们要操作的购物车
     * @return
     */
    private BoundHashOperations<String, Object, Object> extracted() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();

        String cartKey = "";
        if (userInfoTo.getUserId() != null){
            cartKey = CART_PREFIX+userInfoTo.getUserId();
        }else {
            cartKey =CART_PREFIX+userInfoTo.getUserKey();
        }

        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);

        return operations;
    }

    private List<CartItem> getCartItems(String cartKey){
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if (values != null && values.size() >0){
            List<CartItem> collect = values.stream().map(obj -> {
                String str = (String) obj;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

}
