package com.taogu.scan.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.taogu.config.Config;
import com.taogu.scan.file.FilePersis;
import com.taogu.scan.ip.IpFind;
import com.taogu.scan.ip.IpGroup;

//发现互联网中的代理
public class FindProxy {

	private final static Logger log = Logger.getLogger(FindProxy.class);
	private static int threadNum = Config.getInt("proxy.thread.num");
	private static int subThreadNum = Config.getInt("proxy.subThread.num");
	private static ExecutorService es = Executors.newFixedThreadPool(threadNum);
	private static Set<Integer> ports = new HashSet<Integer>();
	private static ConcurrentLinkedQueue<ProxyIp> queue = new ConcurrentLinkedQueue<ProxyIp>();
	private static boolean flag = true;

	public static void addPort(int port) {
		ports.add(port);
	}

	public static void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int num = 0;
				while (flag) {
					es = Executors.newFixedThreadPool(threadNum);
					for (int i = 0; i < threadNum; i++) {
						es.submit(new ProxyFind());
					}
					es.shutdown();
					try {
						es.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 将代理持久化到文件中
					FilePersis.persis(getProxies());
					num++;
					log.info("代理扫描" + num + "遍完毕！");
				}
			}
		}).start();
	}

	static class ProxyFind implements Runnable {
		private int n = 6;

		@Override
		public void run() {
			while (n != 0) {
				IpGroup ipGroup = IpFind.popIpGroup();
				if (ipGroup == null) {
					try {
						Thread.sleep(10 * 1000l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					n--;
					continue;
				}
				n = 6;
				String iGroup = ipGroup.getIp();
				int num = ipGroup.getNum();
				selectThread(iGroup, num);
			}
		}
	}

	public static String host = Config.getString("proxy.target.url");
	private static AtomicInteger atomicThreadNum = new AtomicInteger(0);

	public static void selectThread(String iGroup, int num) {
		for (int i = 0; i < num; i++) {
			String ip = generageIp(iGroup, i);
			if (ip.endsWith(".0")) {
				continue;
			}
			for (int port : ports) {
				int nn = atomicThreadNum.incrementAndGet();
				if (nn > subThreadNum) {
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				ProxyIp proxy = new ProxyIp(Type.HTTP, new InetSocketAddress(ip, port));
				new Thread(new ProxyRun(proxy)).start();
			}
		}
	}

	public static class ProxyRun implements Runnable {

		private ProxyIp proxy = null;

		public ProxyRun(ProxyIp proxy) {
			this.proxy = proxy;
		}

		@Override
		public void run() {
			InputStream in = null;
			byte[] b = new byte[1024];
			try {
				URL url = new URL(host);
				URLConnection con = url.openConnection(proxy);
				in = con.getInputStream();
				in.read(b);
				log.info(atomicThreadNum.get() + proxy.address().toString());
				queue.add(proxy);
			} catch (IOException e) {
			} finally {
				if (in != null) {
					try {
						in.close();
						in = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				atomicThreadNum.decrementAndGet();
			}
		}
	}

	public static String generageIp(String iGroup, int i) {
		String[] ips = iGroup.split("\\.");
		long num = 0;
		for (int j = 0; j < ips.length; j++) {
			int ii = Integer.parseInt(ips[j]);
			num = num * 256 + ii;
		}
		num += i;
		long n4 = num % 256;
		long n = num / 256;
		long n3 = n % 256;
		n = n / 256;
		long n2 = n % 256;
		n = n / 256;
		long n1 = n % 256;
		String ip = n1 + "." + n2 + "." + n3 + "." + n4;
		return ip;
	}

	public static Set<Integer> getPorts() {
		return ports;
	}

	public static void setPorts(Set<Integer> ports) {
		FindProxy.ports = ports;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		FindProxy.host = host;
	}

	public static List<String> getProxies() {
		List<String> list = new ArrayList<>();
		for (ProxyIp p : queue) {
			list.add(p.toString());
		}
		return list;
	}

	public static boolean removeProxy(ProxyIp p) {
		return queue.remove(p);
	}

	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		FindProxy.flag = flag;
	}
}