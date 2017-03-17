package com.taogu.scan.ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;

public class IpFind {

	private final static Logger log = Logger.getLogger(IpFind.class);
	private static String maxDate = "00000000";
	private static List<IpGroup> ipGroups = Collections.synchronizedList(new LinkedList<IpGroup>());

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		new Thread(new worker()).start();
	}

	public static IpGroup popIpGroup() {
		if (ipGroups.isEmpty()) {
			return null;
		}
		return ipGroups.remove(0);
	}

	static class worker implements Runnable {

		@Override
		public void run() {
			try {
				getIpGroupInChina();
				log.info("加载中国ip地址结束!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void getIpGroupInChina() throws IOException {
		InputStream openStream = IpFind.class.getClassLoader().getResourceAsStream("ipGroup.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(openStream))) {
			String line = null;
			String temp_max_date = maxDate;
			while ((line = br.readLine()) != null) {
				if (StringUtil.isBlank(line) || line.startsWith("#")) {
					continue;
				}
				if (line.contains("CN|ipv4")) {
					String[] ss = line.split("\\|");
					if (maxDate.compareToIgnoreCase(ss[5]) > 0) {
						continue;
					}
					temp_max_date = temp_max_date.compareToIgnoreCase(ss[5]) > 0 ? temp_max_date : ss[5];
					String ip = ss[3];
					int ipNum = Integer.parseInt(ss[4]);
					IpGroup ig = new IpGroup();
					ig.setNum(ipNum);
					ig.setIp(ip);
					ipGroups.add(ig);
				}
			}
			maxDate = temp_max_date;
		} finally {
			if (openStream != null) {
				try {
					openStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
