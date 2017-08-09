package com.bizideal.mn.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.bizideal.mn.config.AlipayConfig;
import com.google.common.collect.Maps;
import com.sun.tools.classfile.StackMapTable_attribute.same_frame;

/**
 * @author 作者 liulq:
 * @data 创建时间：2017年6月6日 上午10:49:11
 * @version 1.0
 * @description 描述
 */
@Controller
@RequestMapping("/")
public class IndexController {
	
	@RequestMapping("/")
	public String index() throws IOException {
		return "pay";
	}

	@RequestMapping("/returnUrl")
	public String returnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		System.out.println("returnUrl");
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			params.put(name, valueStr);
			System.out.println(name + ":" + valueStr);
		}
		System.out.println("returnUrl");

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号

		String out_trade_no = request.getParameter("out_trade_no");

		// 支付宝交易号

		String trade_no = request.getParameter("trade_no");

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		// 计算得出通知验证结果
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");

		System.out.println("======进入return url====");
		// 解决响应乱码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");

		if (verify_result) {// 验证成功
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码
			// 该页面可做页面美工编辑
			// out.clear();
			// out.println("验证成功<br />");
			response.getWriter().write("验证成功<br />");
			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			// ////////////////////////////////////////////////////////////////////////////////////////
		} else {
			// 该页面可做页面美工编辑
			// out.clear();
			// out.println("验证失败");
			response.getWriter().write("验证失败");
		}
		response.getWriter().flush();
		response.getWriter().close();
		return "payResult";
	}

	@RequestMapping("/notifyUrl")
	public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		System.out.println("notifyUrl");
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
			System.out.println(name + ":" + valueStr);
		}
		System.out.println("notifyUrl");
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号

		String out_trade_no = request.getParameter("out_trade_no");
		// 支付宝交易号

		String trade_no = request.getParameter("trade_no");

		// 交易状态
		String trade_status = request.getParameter("trade_status");

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		// 计算得出通知验证结果
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");

		if (verify_result) {// 验证成功
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			if (trade_status.equals("TRADE_FINISHED")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				// 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
			} else if (trade_status.equals("TRADE_SUCCESS")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
			}

			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			response.getWriter().write("success");
			// out.clear();
			// out.println("success"); // 请不要修改或删除

			// ////////////////////////////////////////////////////////////////////////////////////////
		} else {// 验证失败
			// out.println("fail");
			response.getWriter().write("fail");
		}
		response.getWriter().flush();
		response.getWriter().close();
	}

	/* 构造支付表单，跳转支付宝app准备支付 */
	@RequestMapping("/toPay")
	public void toPay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("WIDout_trade_no") != null) {
			// 商户订单号，商户网站订单系统中唯一订单号，必填
			String out_trade_no = request.getParameter("WIDout_trade_no");
			// 订单名称，必填
			String subject = request.getParameter("WIDsubject");
			// 付款金额，必填
			String total_amount = request.getParameter("WIDtotal_amount");
			// 商品描述，可空
			String body = request.getParameter("WIDbody");
			// 超时时间 可空
			String timeout_express = "2m";
			// 销售产品码 必填
			String product_code = "QUICK_WAP_PAY";
			/**********************/
			// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
			// 调用RSA签名方式
			AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT,
					AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
			AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

			// 封装请求支付信息
			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			model.setOutTradeNo(out_trade_no);
			model.setSubject(subject);
			model.setTotalAmount(total_amount);
			model.setBody(body);
			model.setTimeoutExpress(timeout_express);
			model.setProductCode(product_code);
			alipay_request.setBizModel(model);
			// 设置异步通知地址
			alipay_request.setNotifyUrl(AlipayConfig.notify_url);
			// 设置同步地址
			alipay_request.setReturnUrl(AlipayConfig.return_url);

			// form表单生产
			String form = "";
			try {
				// 调用SDK生成表单
				form = client.pageExecute(alipay_request).getBody();
				response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
				response.getWriter().write(form);// 直接将完整的表单html输出到页面
				response.getWriter().flush();
				response.getWriter().close();
			} catch (AlipayApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* 发起退款操作(默认付款成功后的3个月内可进行退款) */
	@RequestMapping("/back")
	public String back() throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT,
				AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		JSONObject json = new JSONObject();
		// 文档地址
		// https://doc.open.alipay.com/doc2/apiDetail.htm?apiId=759&docType=4
		json.put("out_trade_no", "201766134642446"); // 特殊可选 64
														// 订单支付时传入的商户订单号,不能和trade_no同时为空。
		json.put("trade_no", "2017060621001004950257186197"); // 特殊可选 64
																// 支付宝交易号，和商户订单号不能同时为空
		json.put("refund_amount", "0.01"); // 必须 9
											// 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
		json.put("refund_reason", "正常退款"); // 可选 256 退款的原因说明
		// json.put("out_request_no", "HZ01RF001"); // 可选 64
		// // 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
		// json.put("operator_id", "OP001"); // 可选 30 商户的操作员编号
		// json.put("store_id", "NJ_S_001"); // 可选 32 商户的门店编号
		// json.put("terminal_id", "NJ_T_001"); // 可选 32 商户的终端编号
		request.setBizContent(json.toJSONString());
		// request.setBizContent("{" + "\"out_trade_no\":\"20150320010101001\","
		// + "\"trade_no\":\"2014112611001004680073956707\","
		// + "\"refund_amount\":0.01," + "\"refund_reason\":\"正常退款\"," +
		// "\"out_request_no\":\"HZ01RF001\"," + "\"operator_id\":\"OP001\","
		// + "\"store_id\":\"NJ_S_001\"," + "\"terminal_id\":\"NJ_T_001\"" +
		// "  }");
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		String code = response.getCode(); // code = 10000 表示成功
		if (response.isSuccess()) {
			System.out.println("调用成功");
		} else {
			System.out.println("调用失败");
		}
		return "pay";
	}

}
