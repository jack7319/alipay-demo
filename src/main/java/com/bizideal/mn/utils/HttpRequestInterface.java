package com.bizideal.mn.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestInterface {
	private static Logger log = LoggerFactory
			.getLogger(HttpRequestInterface.class);

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}

	/**
	 * 获取票据
	 * 
	 * @param requestUrl
	 *            http://10.1.3.2:8080/CasServer/ticket_login
	 * @param username
	 * @param password
	 * @return
	 */
	public static String httpRequestPostForm(String requestUrl,
			String username, String password) {
		String repstr = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 连接

			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setInstanceFollowRedirects(true);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod("POST");

			httpUrlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			httpUrlConn.connect();
			DataOutputStream out = new DataOutputStream(
					httpUrlConn.getOutputStream());
			String content = "username=" + URLEncoder.encode(username, "UTF-8");
			content += "&password=" + URLEncoder.encode(password, "UTF-8");
			out.writeBytes(content);

			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {

				buffer.append(line);

			}
			reader.close();
			httpUrlConn.disconnect();
			repstr = buffer.toString();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return repstr;
	}

	/**
	 * 通用表单提交
	 * 
	 * @param requestUrl
	 * @param password
	 * @return
	 */
	public static String httpRequest(String requestUrl, String method,
			Map<String, String> map) {
		String repstr = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 连接

			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setInstanceFollowRedirects(true);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(method);

			httpUrlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			httpUrlConn.connect();
			DataOutputStream out = new DataOutputStream(
					httpUrlConn.getOutputStream());
			String content = "";
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if (content.length() > 0) {
					content += "&";
				}
				content += entry.getKey() + "="
						+ URLEncoder.encode(entry.getValue(), "UTF-8");
			}
			out.writeBytes(content);

			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {

				buffer.append(line);

			}
			reader.close();
			httpUrlConn.disconnect();
			repstr = buffer.toString();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return repstr;
	}

	/**
	 * 
	 * @param requestUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public static String httpRequestPut(String requestUrl, String password) {
		String repstr = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 连接

			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setInstanceFollowRedirects(true);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod("PUT");

			httpUrlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			httpUrlConn.connect();
			DataOutputStream out = new DataOutputStream(
					httpUrlConn.getOutputStream());
			String content = "newpassword="
					+ URLEncoder.encode(password, "UTF-8");
			out.writeBytes(content);

			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {

				buffer.append(line);

			}
			reader.close();
			httpUrlConn.disconnect();
			repstr = buffer.toString();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return repstr;
	}

	/**
	 * put请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	public static String put(String requestUrl, Map<String, String> map) {
		String repstr = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 连接

			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setInstanceFollowRedirects(true);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod("PUT");

			httpUrlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			httpUrlConn.connect();
			DataOutputStream out = new DataOutputStream(
					httpUrlConn.getOutputStream());
			String content = "";
			for (Entry<String, String> e : map.entrySet()) {
				content += e.getKey() + "="
						+ URLEncoder.encode(e.getValue(), "UTF-8") + "&";
			}
			content = content.substring(0, content.length() - 1);
			out.writeBytes(content);

			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {

				buffer.append(line);

			}
			reader.close();
			httpUrlConn.disconnect();
			repstr = buffer.toString();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return repstr;
	}

	/**
	 * delete方法
	 * 
	 * @param requestUrl
	 * @param method
	 * @param map
	 * @return
	 */
	public static String doDelete(String requestUrl) {
		String repstr = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 连接

			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setInstanceFollowRedirects(true);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod("DELETE");

			httpUrlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			httpUrlConn.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {

				buffer.append(line);

			}
			reader.close();
			httpUrlConn.disconnect();
			repstr = buffer.toString();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return repstr;
	}

	/**
	 * 
	 * @param requestUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public static String httpRequestDelete(String requestUrl) {
		String repstr = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 连接

			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setInstanceFollowRedirects(true);

			// 设置请求方式（DELETE）
			httpUrlConn.setRequestMethod("DELETE");

			httpUrlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			httpUrlConn.connect();
			DataOutputStream out = new DataOutputStream(
					httpUrlConn.getOutputStream());

			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {

				buffer.append(line);

			}
			reader.close();
			httpUrlConn.disconnect();
			repstr = buffer.toString();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return repstr;
	}

	/*
	 * public static void main(String[] args) { Map<String, String> map = new
	 * HashMap<String, String>(); map.put("a", "1"); map.put("b", "2");
	 * map.put("c", "3"); String content = ""; for (Map.Entry<String, String>
	 * entry : map.entrySet()) { if (content.length() > 0) { content += "&"; }
	 * content += entry.getKey() + "=" + entry.getValue(); }
	 * System.out.println(content); System.out.println("".length()); }
	 */

	/**
	 * post请求发送json数据
	 * 
	 * @param url
	 *            请求路径
	 * @param json
	 *            发送的json数据
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String post(String url, String json) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String jsonString = "";
		try {
			HttpPost method = new HttpPost(url);
			StringEntity se = new StringEntity(json, "application/json",
					"UTF-8");
			method.setEntity(se);

			CloseableHttpResponse response = httpclient.execute(method);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 返回的json数据
				jsonString = new String(EntityUtils.toString(entity, "UTF-8"));
			}
			response.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonString;
	}

	/**
	 * post请求发送json数据
	 * 
	 * @param url
	 *            请求路径
	 * @param json
	 *            发送的json数据
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String postReturnStatus(String url, String json) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String jsonString = "";
		try {
			HttpPost method = new HttpPost(url);
			StringEntity se = new StringEntity(json, "application/json",
					"UTF-8");
			method.setEntity(se);

			CloseableHttpResponse response = httpclient.execute(method);
			int statusCode = response.getStatusLine().getStatusCode();
			response.close();
			return statusCode + "";
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonString;
	}

	/**
	 * post请求发送json数据
	 * 
	 * @param url
	 *            请求路径
	 * @param json
	 *            发送的json数据
	 * @return
	 */
	public static String postMap(String url, Map<String, String> map) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String jsonString = "";
		try {
			HttpPost method = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);
			method.setEntity(formEntity);

			CloseableHttpResponse response = httpclient.execute(method);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 返回的json数据
				jsonString = new String(EntityUtils.toString(entity, "UTF-8"));
			}
			response.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonString;
	}

	public static String delete(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String jsonString = "";
		try {
			HttpDelete method = new HttpDelete(url);

			CloseableHttpResponse response = httpclient.execute(method);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 返回的json数据
				jsonString = new String(EntityUtils.toString(entity, "UTF-8"));
			}
			response.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonString;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		/*
		 * String url = "http://127.0.0.1:8080/userManager/unit/get.do";
		 * CloseableHttpClient httpClient = HttpClients.createDefault();
		 * HttpPost httpPost = new HttpPost(url); try { JSONObject jsonObject =
		 * new JSONObject(); jsonObject.put("unitId", 73);
		 * jsonObject.put("meeId", 1); StringEntity stringEntity = new
		 * StringEntity(jsonObject.toString(), "application/json", "utf-8");
		 * httpPost.setEntity(stringEntity); CloseableHttpResponse response =
		 * httpClient.execute(httpPost); int statusCode =
		 * response.getStatusLine().getStatusCode(); if (statusCode == 200) {
		 * HttpEntity entity = response.getEntity(); if (null != entity) {
		 * String json = new String(EntityUtils.toString(entity, "utf-8"));
		 * System.out.println(json); } } } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		// String url =
		// "http://114.55.41.24:8081/MeetingManage/meeMeeting_queryMeetByPage.shtml";
		String url = "http://127.0.0.1:8082/MeetingManager/meeMeeting_queryMeetByPage.shtml";
		Map<String, String> map = new HashMap<String, String>();
		map.put("meeName", "全国");
		map.put("pageIndex", "1");
		map.put("pageSize", "5");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		try {
			// post.setHeader("Content-Type","text/html;charset=UTF-8");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,
					"utf-8");
			post.setEntity(formEntity);
			CloseableHttpResponse response = httpClient.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println(statusCode);
			if (200 == statusCode) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					String json = new String(EntityUtils.toString(entity,
							"utf-8"));
					System.out.println(json);
				} else {
					System.out.println("entity==null");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
