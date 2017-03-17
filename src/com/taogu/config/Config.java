package com.taogu.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;

public class Config {

	private final static Logger log = Logger.getLogger(Config.class);
	private static Properties p = new Properties();

	public static void init() {
		try {
			p.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	public static String getString(String key) {
		return p.getProperty(key);
	}

	public static String getString(String key, String defaul) {
		return p.getProperty(key, defaul);
	}

	public static int getInt(String key) {
		String v = getString(key);
		if (StringUtil.isBlank(v)) {
			return -1;
		}
		return Integer.parseInt(v);
	}

	public static int getInt(String key, int defaul) {
		String v = getString(key);
		if (StringUtil.isBlank(v)) {
			return defaul;
		}
		return Integer.parseInt(v);
	}
}
