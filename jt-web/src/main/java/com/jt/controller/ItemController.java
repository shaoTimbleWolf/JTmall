package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;


@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	/**
	 * 	根据商品的id查询后台服务器的数据
	 * 	业务步骤：1.在前台的service中实现HttpClient的调用
	 * 		2.后台根据itemId返回对象的json串
	 * 		3.将json转化为item对象
	 * 		4.将item的对象保存草request域中
	 * 		5.返回页面的逻辑名称item
	 * @param itemId 商品id
	 * @return 详细列表页面
	 */
	@RequestMapping("/items/{itemId}")
	public String findItenById(@PathVariable Long itemId,Model model) {
		Item item = itemService.findItemById(itemId);
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
