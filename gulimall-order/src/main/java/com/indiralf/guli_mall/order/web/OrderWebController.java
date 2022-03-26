package com.indiralf.guli_mall.order.web;

import com.indiralf.guli_mall.order.service.OrderService;
import com.indiralf.guli_mall.order.vo.OrderConfirmVo;
import com.indiralf.guli_mall.order.vo.OrderSubmitVo;
import com.indiralf.guli_mall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

/**
 * @author
 * @time 2022/3/15 20:39
 * @Description- TODO
 */
@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {

        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData",confirmVo);

        //展示订单确认的数据
        return "confirm";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes redirectAttributes){

        SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);

        if (responseVo.getCode() == 0){
            model.addAttribute("submitOrderResp",responseVo);
            return "pay";
        }else {
            String msg = "下单失败";
            switch (responseVo.getCode()){
                case 1: msg = "订单信息过期，请刷新再次提交"; break;
                case 2: msg = "订单商品价格发生变化，请确认后再次提交"; break;
                case 3: msg = "库存锁定失败，商品库存不足";break;
            }
            redirectAttributes.addFlashAttribute("msg","");
            return "redirect:http://order.gulimall.com/toTrade";
        }

    }
}
