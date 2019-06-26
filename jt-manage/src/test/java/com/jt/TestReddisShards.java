package com.jt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
/**
 *	测试redis分片 
 */
public class TestReddisShards {
	/**
	 * 操作是需要将多台redis当做一台
	 */
	@Test
	public void testShard() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo info1 = new JedisShardInfo("192.168.161.129",6379);
		JedisShardInfo info2 = new JedisShardInfo("192.168.161.129",6380);
		JedisShardInfo info3 = new JedisShardInfo("192.168.161.129",6381);
		shards.add(info1);
		shards.add(info2);
		shards.add(info3);
		
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		for (int i = 0; i < 10000; i++) {
			shardedJedis.set("aa"+i,"cc"+i);
		}
		
//		System.out.println(shardedJedis.get("aa"));
	}
}
