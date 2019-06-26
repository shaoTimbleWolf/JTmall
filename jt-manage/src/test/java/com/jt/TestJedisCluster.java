package com.jt;

import java.util.HashSet;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 测试redis集群
 */
public class TestJedisCluster {
	@Test
	public void test01() {
		HashSet<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.161.129", 7000));
		nodes.add(new HostAndPort("192.168.161.129", 7001));
		nodes.add(new HostAndPort("192.168.161.129", 7002));
		nodes.add(new HostAndPort("192.168.161.129", 7003));
		nodes.add(new HostAndPort("192.168.161.129", 7004));
		nodes.add(new HostAndPort("192.168.161.129", 7005));
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("redis","redis集群测试");
		System.out.println(cluster.get("redis"));
	}
}
