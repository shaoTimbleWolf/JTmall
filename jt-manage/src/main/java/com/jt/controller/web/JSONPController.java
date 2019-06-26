package com.jt.controller.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;

//在后台编辑方法
@RestController
public class JSONPController {
	//返回值是回调函数
//	@RequestMapping("/web/testJSONP")
//	public String testJSONP(String callback) {
//		ItemDesc itemDesc = new ItemDesc();
//		itemDesc.setItemId(1000L);
//		itemDesc.setItemDesc("dasdasdasdasd");
//		String json = ObjectMapperUtil.toJSON(itemDesc);
//		return callback + "("+json + ")";
//	}
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonpObject(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1000L);
		itemDesc.setItemDesc("dasdasdasdasd");

		return new JSONPObject(callback, itemDesc);
	}
}
