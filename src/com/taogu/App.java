package com.taogu;

import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.taogu.config.Config;
import com.taogu.server.Bootstrap;
import com.taogu.server.SiteSetup;

@Modules(packages = { "com.taogu.controller" }, scanPackage = true)
@IocBy(type = ComboIocProvider.class, args = { "*org.nutz.ioc.loader.annotation.AnnotationIocLoader", "com.taogu" })
@Encoding(input = "UTF-8", output = "UTF-8")
@SetupBy(SiteSetup.class)
public class App {

	public static void main(String[] args) {
		String webPath = "web/";
		Config.init();
		int port = Config.getInt("server.port", 8080);
		Bootstrap.start(webPath, port, "/welcome.jsp");
	}
}
