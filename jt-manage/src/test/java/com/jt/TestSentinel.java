package com.jt;

import java.util.HashSet;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestSentinel {
	//测试哨兵get、set
	@Test
	public void test01() {
		//masterName 代表主机的变量名称
		HashSet<String> sentinels = new HashSet<String>();
		sentinels.add("192.168.161.129:26379");
		JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = jedisSentinelPool.getResource();
		jedis.set("asd","asd");
		
		System.out.println(jedis.get("asd"));
		jedis.close();
	}
}
