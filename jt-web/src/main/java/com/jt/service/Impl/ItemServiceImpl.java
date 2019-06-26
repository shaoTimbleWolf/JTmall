package com.jt.service.Impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
@Service
public class ItemServiceImpl implements ItemService{
	@Autowired
	private HttpClientService httpClientService;

	@Override
	public Item findItemById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemById";
		//	为了满足get请求需求  定义 id=xxx
		HashMap<String, String> params = new HashMap<>();
		params.put("id", ""+itemId);
		String result = httpClientService.doGet(url,params);
		Item item = ObjectMapperUtil.toObject(result, Item.class);
		return item;
	}
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemDescById";
		//	为了满足get请求需求  定义 id=xxx
		HashMap<String, String> params = new HashMap<>();
		params.put("id", ""+itemId);
		String result = httpClientService.doGet(url,params);
		ItemDesc itemDesc = ObjectMapperUtil.toObject(result, ItemDesc.class);
		return itemDesc;
	}

}
