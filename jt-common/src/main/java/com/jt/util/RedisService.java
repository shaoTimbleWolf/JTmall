package com.jt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@Service
public class RedisService {
	@Autowired(required=false)//调用时注入
	private JedisSentinelPool sentinelPool;
	//封装方法 get
	public String get(String key) {
		Jedis jedis = sentinelPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		return result;
	}
	//set
	public void set(String key,String json) {
		Jedis jedis = sentinelPool.getResource();
		String result = jedis.set(key,json);
		jedis.close();
	}
	//set
	public void setex(String key,int seconds,String json) {
		Jedis jedis = sentinelPool.getResource();
		String result = jedis.setex(key,seconds,json);
		jedis.close();
	}
}
