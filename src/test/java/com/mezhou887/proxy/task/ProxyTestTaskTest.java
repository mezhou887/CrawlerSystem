package com.mezhou887.proxy.task;

import org.junit.Test;

import com.mezhou887.proxy.ProxyPool;
import com.mezhou887.proxy.entity.Direct;
import com.mezhou887.proxy.entity.Proxy;

public class ProxyTestTaskTest {

	@Test
	public void testStart() {
		Proxy proxy = new Direct(1000);
		ProxyTestTask task = new ProxyTestTask(proxy);
		new Thread(task).run();
		System.out.println(ProxyPool.proxyQueue.size());
	}

}
