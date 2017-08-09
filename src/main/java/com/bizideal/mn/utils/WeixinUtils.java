package com.bizideal.mn.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONObject;

import com.bizideal.mn.config.WeChatConfig;

/**
 * @author 作者 liulq:
 * @data 创建时间：2017年5月10日 下午3:52:22
 * @version 1.0
 * @description 描述
 */
public class WeixinUtils {

	public static String getJsapiTicket(String accessToken) {
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
		JSONObject httpRequest = HttpRequestInterface.httpRequest(url, "GET", "");
		return httpRequest.getString("ticket");
	}

	public static String getAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WeChatConfig.appId + "&secret="
				+ WeChatConfig.getAppsecret();
		url.replace("APPID", WeChatConfig.getAppid()).replace("APPSECRET", WeChatConfig.getAppsecret());
		JSONObject httpRequest = HttpRequestInterface.httpRequest(url, "GET", "");
		String ticket = httpRequest.getString("access_token");
		return ticket;
	}

	/**
	 * 方法名：getWxConfig</br> 详述：获取微信的配置信息 </br> 开发人员：souvc </br> 创建时间：2016-1-5
	 * </br>
	 * 
	 * @param request
	 * @return 说明返回值含义
	 * @throws 说明发生此异常的条件
	 */
	public static Map<String, Object> getWxConfig(HttpServletRequest request, String access_token, String jsapi_ticket) {
		Map<String, Object> ret = new HashMap<String, Object>();

		String appId = WeChatConfig.getAppid(); // 必填，公众号的唯一标识

		String requestUrl = request.getRequestURL().toString() + createParams(request);
		String timestamp = Long.toString(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳
		String nonceStr = UUID.randomUUID().toString(); // 必填，生成签名的随机串
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		String sign = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + requestUrl;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(sign.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("appId", appId);
		ret.put("timestamp", timestamp);
		ret.put("nonceStr", nonceStr);
		ret.put("signature", signature);
		return ret;
	}

	/**
	 * 方法名：byteToHex</br> 详述：字符串加密辅助方法 </br> 开发人员：souvc </br> 创建时间：2016-1-5
	 * </br>
	 * 
	 * @param hash
	 * @return 说明返回值含义
	 * @throws 说明发生此异常的条件
	 */
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;

	}

	@SuppressWarnings("unchecked")
	public static String createParams(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(name);
			sb.append("=");
			sb.append(request.getParameter(name));
		}
		String params = sb.toString();
		if (StringUtils.isNotEmpty(params)) {
			params = "?" + params;
		}
		return params;
	}

	public static void main(String[] args) {
	}
}
