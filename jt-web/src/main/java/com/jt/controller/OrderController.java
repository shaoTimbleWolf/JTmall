package com.jt.controller;

import java.util.List;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order/")
public class OrderController {
	@Reference(timeout=3000,check=false)
	private DubboOrderService orderService;
	@Reference(timeout=3000,check=false)
	private DubboCartService cartService;
	/**
	 * url:http://www.jt.com/order/create.html
	 * 	页面取值carts=${carts}
	 * @return 返回到页面订单确认页面order-cart.jsp
	 */
	@RequestMapping("create")
	public String orderCart(Model model) {
		Long userId = ((User)ThreadLocalUtil.get("JT_USER")).getId();
		List<Cart> carts = cartService.findCartListByUserId(userId);
		model.addAttribute("carts", carts);
		return "order-cart";
	}
	//	实现订单存储
	@RequestMapping("submit")
	@ResponseBody
	public SysResult insertOrder(Order order) {
		try {
			Long userId = ((User)ThreadLocalUtil.get("JT_USER")).getId();
			order.setUserId(userId);
			String orderId = orderService.insertOrder(order);
			if (!StringUtils.isEmpty(orderId)) {
				return SysResult.ok(orderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}
	//	根据orderId查询订单数据，跳转页面
	@RequestMapping("success")
	public String findOrderById(String id,Model model) {
		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
}
