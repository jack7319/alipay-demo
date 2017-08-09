package com.bizideal.mn.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.bizideal.mn.config.AlipayConfig;

/**
 * @author 作者 liulq:
 * @data 创建时间：2017年5月10日 下午12:27:53
 * @version 1.0
 * @description 描述
 */
@Controller
@RequestMapping("/sign")
public class SignController {

	@RequestMapping("/checkSign")
	public void t(HttpServletRequest request) {
		// 获取异步请求参数
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String paramName = (String) iter.next();
			String paramValue = ((String[]) requestParams.get(paramName))[0];
			System.out.println("收到参数：" + paramName + " = " + paramValue);
			/* 若你在notify_url后添加了自定义参数如http://www.alipay.com?a=a,请不要加入params */
			params.put(paramName, paramValue);
		}
		// 调用SDK验签
		boolean verifyResult = false;
		try {
			verifyResult = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		System.out.println("验证结果：" + verifyResult);
	}
}
