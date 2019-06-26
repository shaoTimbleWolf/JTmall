package com.jt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

import redis.clients.jedis.Jedis;

public class TestRedis {

	//string 类型的测试
	//端口号：
	//IP地址
	@Test
	public void testString() {
		Jedis jedis = new Jedis("192.168.161.129", 6379);
		jedis.set("1902", "1902班");
		jedis.expire("1902", 20);
		System.out.println(jedis.get("1902"));
	}

	//设定数据的超时方法
	//第一种,上面的方法中的设置
	//第二种:分布式锁
	@Test
	public void setExpire() throws InterruptedException {
		Jedis jedis = new Jedis("192.168.161.129", 6379);
		//set和expire同时操作
		jedis.setex("aa", 2, "12");
		System.out.println(jedis.get("aa"));
		Thread.sleep(3000);
		//当key不存在时，操作正常，可以不存在时，操作失败
		//赋值
		jedis.setnx("aa", "bb");
		System.out.println("获取输出数据"+jedis.get("aa"));
	}
	/**
	 * 	实现对象转换为JSON
	 * @throws JsonProcessingException 
	 */
	@Test
	public void objectToJson() throws JsonProcessingException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(10010L).setItemDesc("ceshi---hahaha");
		ObjectMapper objectMapper = new ObjectMapper();
		//转换时必须有set和get方法
		String json = objectMapper.writeValueAsString(itemDesc);
		System.out.println(json);
		Jedis jedis = new Jedis("192.168.161.129", 6379);
		jedis.set("itemDesc",json);
	}
	
	/**
	 * 	实现JSON转换为对象
	 * @throws IOException 
	 */
	@Test
	public void jsonToObject() throws IOException {
		Jedis jedis = new Jedis("192.168.161.129", 6379);
		ObjectMapper objectMapper = new ObjectMapper();
		ItemDesc readValue = objectMapper.readValue(jedis.get("itemDesc"), ItemDesc.class);
		System.out.println(readValue);
	}
	
	/**
	 * 	利用redis 保存业务数据
	 * 	数据库对象：Object
	 * 
	 * 	string类型只能储存字符串类型
	 * 	需要将item转换成JSON字符串
	 */
	@Test
	public void testObject() {
		Item item = new Item();
		item.setId(10001L).setTitle("1902哈哈");
		
		Jedis jedis = new Jedis("192.168.161.129", 6379);
		
	}
	/**
	 * 	读写数据库时list 和json 之间的互相转化
	 * @throws IOException
	 */
	@Test
	public void listToJSON() throws IOException{
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(10011L).setItemDesc("ceshi111---hahaha");
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(10022L).setItemDesc("ceshi222---hahaha");
		List<ItemDesc> list = new ArrayList<>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String itemDescList = objectMapper.writeValueAsString(list);
		//存入数据库
		Jedis jedis = new Jedis("192.168.161.129", 6379);
		jedis.set("itemDescList",itemDescList);
		//从数据库获取
		String result = jedis.get("itemDescList");
		System.out.println("输出为JSON:"+result);
		@SuppressWarnings("unchecked")
		List<ItemDesc> resultList = objectMapper.readValue(result,list.getClass());
		System.out.println("输出为list集合:"+resultList);
	}

	
}
