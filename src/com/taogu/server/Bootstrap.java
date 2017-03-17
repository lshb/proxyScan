package com.taogu.server;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.webapp.WebAppContext;

public class Bootstrap {
	private static Server server;

	public static void start(String webPath, int port, String... welcomePage) {
		server = new Server();

		ServerConnector httpsConnector = new ServerConnector(server);
		httpsConnector.setPort(port);
		httpsConnector.setIdleTimeout(3600000);

		server.addConnector(httpsConnector);

		WebAppContext context = new WebAppContext(webPath, "/");
		context.setWelcomeFiles(welcomePage);
		context.setMaxFormContentSize(-1);

		context.addFilter(GzipFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

		server.setHandler(context);
		server.setStopAtShutdown(true);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void shutdown() {
		try {
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
