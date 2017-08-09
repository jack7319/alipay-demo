package com.bizideal.mn.config;

import java.io.IOException;
import java.util.Properties;

public class AlipayConfig {

	private static Properties properties = new Properties();

	static {
		try {
			properties.load(AlipayConfig.class.getClassLoader().getResourceAsStream("pay.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 商户appid
	public static final String APPID = properties.getProperty("APPID");
	// 私钥 pkcs8格式的
	public static final String RSA_PRIVATE_KEY = properties.getProperty("RSA_PRIVATE_KEY");
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static final String notify_url = properties.getProperty("notify_url");
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	// 商户可以自定义同步跳转地址
	public static final String return_url = properties.getProperty("return_url");
	// 请求网关地址
	public static final String URL = properties.getProperty("URL");
	// 编码
	public static final String CHARSET = properties.getProperty("CHARSET");
	// 返回格式
	public static final String FORMAT = properties.getProperty("FORMAT");
	// 支付宝公钥
	public static final String ALIPAY_PUBLIC_KEY = properties.getProperty("ALIPAY_PUBLIC_KEY");
	// 日志记录目录
	public static final String log_path = properties.getProperty("log_path");
	// RSA2
	public static final String SIGNTYPE = properties.getProperty("SIGNTYPE");
}
