package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.interceptor.UserInterceptor;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@RequestMapping("/cart/")
@Controller//因为要跳转页面不需要@RestController,后期需要返回json添加@Responsbody注解
public class CartController {
	@Reference(timeout=3000,check=false)
	private DubboCartService cartService;

	/**
	 * 	实现购物车页面的展现
	 * @param model
	 * @return
	 */
	@RequestMapping("show")
	public String findCartList(Model model) {
		User user = (User)ThreadLocalUtil.get("JT_USER");
		List<Cart> cartList = cartService.findCartListByUserId(user.getId());
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	/**
	 * 	实现购物车数量的修改
	 * 	在restFul风格中获取数据时参数和对象属性匹配时可以用对象来接收 
	 * @param cart
	 * @return
	 */
	@RequestMapping("update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		try {
			User user = (User)ThreadLocalUtil.get("JT_USER");
			cart.setUserId(user.getId());
			cartService.updateCarNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	/**
	 * 	面后台请求成功后跳转页面，重定向到购物车页
	 * @param Cart
	 * @return
	 */
	@RequestMapping("delete/{itemId}")
	public String deleteCart(Cart cart) {
		User user = (User)ThreadLocalUtil.get("JT_USER");
		cart.setUserId(user.getId());
		cartService.deleteCart(cart);
		//	重定向到购物车页面
		return "redirect:/cart/show.html";

	}
	/**
	 * 	页面表单发起post请求，携带购物车参数
	 * @param cart
	 * @return
	 */
	@RequestMapping("add/{itemId}")
	public String insertCart(Cart cart) {
		User user = (User)ThreadLocalUtil.get("JT_USER");
		cart.setUserId(user.getId());
		cartService.insertCart(cart);
		//	新增数据后跳转到购物车页面
		return "redirect:/cart/show.html";
	}
}
