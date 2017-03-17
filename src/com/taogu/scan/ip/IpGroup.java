package com.taogu.scan.ip;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpGroup {
	private String ip;
	private String address;
	private String operator;// 运营商
	private int num;// ip地址数目

	/**
	 * 判断是否为合法IP
	 * 
	 * @return the ip
	 */
	public boolean isboolIp(String ipAddress) {
		String ip = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}

	public static void main(String[] args) {
		IpGroup ig = new IpGroup();
		ig.setIp("27.8.0.0");
		ig.setNum(524288);
		System.err.println(ig.contains("27.20.255.255"));
	}

	public boolean contains(String ip1) {
		if (!isboolIp(ip1)) {
			return false;
		}
		String[] ips1 = ip1.split("\\.");
		String[] ips = ip.split("\\.");
		int n = 0;
		for (int i = 0; i < ips.length; i++) {
			int m = Integer.parseInt(ips[i]);
			int m1 = Integer.parseInt(ips1[i]);
			n = n * 255 + (m - m1);
		}
		if (Math.abs(n) < num) {
			return true;
		}
		return false;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "IpGroup [ip=" + ip + ", address=" + address + ", operator=" + operator + ", num=" + num + "]";
	}
}
