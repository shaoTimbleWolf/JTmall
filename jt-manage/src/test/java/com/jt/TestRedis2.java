package com.jt;



import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestRedis2 {
	//测试hset hget
	@Test
	public void testHsah() {
		Jedis jedis = new Jedis("192.168.161.129", 6379);
		jedis.hset("student", "id", "12");
		jedis.hset("student", "name", "小米");
		jedis.hset("student", "age", "18");
		System.out.println(jedis.hget("student", "name"));
		System.out.println(jedis.hgetAll("student"));
	}
	//编辑list集合
	@Test
	public void testList() {
		Jedis jedis = new Jedis("192.168.161.129", 6379);
		jedis.lpush("com33", "1","2","3","4");//4321
		System.out.println(jedis.lpop("com33"));
		System.out.println(jedis.rpop("com33"));
		
		jedis.rpush("com12", "1","2","3","4");//1234
		System.out.println(jedis.lpop("com12"));
		System.out.println(jedis.rpop("com12"));
		
	}
	//redis事务控制
	@Test
	public void testTX() {
		Jedis jedis = new Jedis("192.168.161.129", 6379);
		Transaction transaction = jedis.multi();//开启事务
		try {
			transaction.set("zz","zz");//成功
			transaction.set("vvv",null);//回滚
			transaction.exec();//事务提交
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();//事务回滚
		}
	}
}
