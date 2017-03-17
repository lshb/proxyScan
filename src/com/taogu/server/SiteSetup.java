package com.taogu.server;

import org.apache.log4j.Logger;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import com.taogu.config.Config;
import com.taogu.scan.ip.IpFind;
import com.taogu.scan.proxy.FindProxy;

public class SiteSetup implements Setup {

	private final static Logger log = Logger.getLogger(SiteSetup.class);

	@Override
	public void destroy(NutConfig nc) {
		log.info("网站销毁！");
	}

	@Override
	public void init(NutConfig nc) {
		log.info("开启ip生成线程");
		IpFind.start();
		log.info("开启代理ip扫描线程！");
		String default_ports = "80,8080,8081,8082,8083,8084,8085,8086,8087,8088,8089,8090";
		String ports = Config.getString("proxy.ports", default_ports);
		String[] ss = ports.split(",");
		for (String p : ss) {
			FindProxy.addPort(Integer.valueOf(p.trim()));
		}
		FindProxy.start();
	}

}
