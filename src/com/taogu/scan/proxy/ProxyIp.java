package com.taogu.scan.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

import com.taogu.config.Config;

public class ProxyIp extends Proxy {

	private long time = 0L;
	// 代理测试目标地址
	private String desTestUrl = Config.getString("proxy.target.url");

	public ProxyIp(Type type, SocketAddress sa) {
		super(type, sa);
		this.time = System.currentTimeMillis();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public boolean testProxy() {
		InputStream in = null;
		byte[] b = new byte[1024];
		try {
			URL url = new URL(desTestUrl);
			URLConnection con = url.openConnection(this);
			in = con.getInputStream();
			in.read(b);
			System.err.println(new String(b));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}