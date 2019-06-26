package com.jt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.config.HttpClientConfig;
import com.jt.util.HttpClientService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpClient {
	@Autowired
	private CloseableHttpClient client;
	@Autowired
	private HttpClientService httpClientService;
	/**
	 * 	测试HttpClient 
	 *  1.实例化httpClient对象
	 *  2.定义http请求路径 url/uri
	 *  3.定义请求方式 get/post
	 *  4.利用API发起http请求
	 *  5.获取返回值以后判断状态信息 200
	 *  6.获取响应数据.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void testGet() throws ClientProtocolException, IOException {

//		CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://www.baidu.com";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = client.execute(httpGet);

		if (response.getStatusLine().getStatusCode()==200) {
			System.out.println("请求成功");
			String result = EntityUtils.toString(response.getEntity());
			System.out.println(result);
		}else {
			throw new RuntimeException();
		}
	}
	@Test
	public void testUtil() {
		String url = "https://spring.io/search";
		Map<String, String> params = new HashMap<>();
		params.put("q","spring");
		String result = httpClientService.doGet(url,params);
		System.out.println(result);
	}
}
