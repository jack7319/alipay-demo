package com.bizideal.mn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizideal.mn.utils.WeixinUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author 作者 liulq:
 * @data 创建时间：2017年5月10日 下午4:00:34
 * @version 1.0
 * @description 描述
 */
@Controller
@RequestMapping("/weixin")
public class WeixinController {

	@RequestMapping("/index")
	public String index(HttpServletRequest request,Model model) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		// 获得ticket
		String access_token = WeixinUtils.getAccessToken();
		String jsapi_ticket = WeixinUtils.getJsapiTicket(access_token);
		Map<String, Object> wxConfig = WeixinUtils.getWxConfig(request, access_token, jsapi_ticket);
		model.addAttribute("config", wxConfig);
		return "index";
	}

	@RequestMapping("/getTicket")
	@ResponseBody
	public ObjectNode getTicket() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String ticket = WeixinUtils.getJsapiTicket(WeixinUtils.getAccessToken());
		node.put("ticket", ticket);
		return node;

	}

}
