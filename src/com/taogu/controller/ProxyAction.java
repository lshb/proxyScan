package com.taogu.controller;

import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.util.List;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.alibaba.fastjson.JSONObject;
import com.taogu.scan.proxy.FindProxy;
import com.taogu.scan.proxy.ProxyIp;

@IocBean
@At("/proxy")
public class ProxyAction {

	@At("/borrow")
	@Ok("raw")
	public Object borrowProxy() {
		List<String> proxy = FindProxy.getProxies();
		JSONObject json = new JSONObject();
		json.put("proxy", proxy);
		return json;
	}

	@At("/remove")
	@Ok("raw")
	public Object removeProxy(@Param("ip") String ip, @Param("port") int port) {
		ProxyIp p = new ProxyIp(Type.HTTP, new InetSocketAddress(ip, port));
		return FindProxy.removeProxy(p);
	}

	@At("/test")
	@Ok("raw")
	public Object testProxy(@Param("ip") String ip, @Param("port") int port) {
		ProxyIp p = new ProxyIp(Type.HTTP, new InetSocketAddress(ip, port));
		return p.testProxy();
	}

	@At("/ping")
	@Ok("raw")
	public Object ping() {
		return "ok";
	}
}
