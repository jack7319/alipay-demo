package com.bizideal.mn.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.bizideal.mn.config.AlipayConfig;
import com.bizideal.mn.utils.HttpClientUtils;

/**
 * @author 作者 liulq:
 * @data 创建时间：2017年6月9日 下午2:23:38
 * @version 1.0
 * @description 获取支付宝用户个人信息，需要授权
 * @description 文档地址
 *              https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0
 *              .0.aCaRO6 &treeId=284&articleId=106001&docType=1
 */
@Controller
@RequestMapping("/user")
public class UserInfoController {

	/*
	 * 第二步，获取auth_code,在请求之前，需要在开发者中心配置回调地址，并且必须配置为这个地址
	 * http://weixindeve.tunnel.qydev.com/msdx1/user/getAuthcode
	 */
	@RequestMapping("/getAuthcode")
	public String success(String auth_code, String app_id, String scope, Model model) {

		// auth_code 44f530519cd447b6b9a83d492e2eXX95
		// scope auth_user

		/* 第三步，用auth_code获取access_token及用户userId */
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPID,
				AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(auth_code);
		request.setGrantType("authorization_code");
		String accessToken = "";
		try {
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
			accessToken = oauthTokenResponse.getAccessToken(); // authusrB9bd17b0e0ef54c9ab2c95bc151fc0C95,过期时间600秒
			System.out.println(accessToken);
			String refreshToken = oauthTokenResponse.getRefreshToken(); // 过期时间660秒
			String userId = oauthTokenResponse.getUserId();
		} catch (AlipayApiException e) {
			// 处理异常
			e.printStackTrace();
		}

		// 第四步，获取用户的详细信息
		AlipayUserInfoShareRequest userInfoRequest = new AlipayUserInfoShareRequest();
		try {
			AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(userInfoRequest, accessToken);
			System.out.println(userinfoShareResponse.getBody());
			String avatar = userinfoShareResponse.getAvatar(); //头像
			String userId = userinfoShareResponse.getUserId(); // 支付宝用户id
			String nickName = userinfoShareResponse.getNickName(); // 昵称
			String province = userinfoShareResponse.getProvince(); // 省份
			String city = userinfoShareResponse.getCity(); // 城市
			String gender = userinfoShareResponse.getGender(); // 性别 M为男性，F为女
			String userType = userinfoShareResponse.getUserType(); // 账户类型 1代表公司账户2代表个人账户 
			String userStatus = userinfoShareResponse.getUserStatus(); // 用户状态 Q代表快速注册用户 T代表已认证用户 B代表被冻结账户 W代表已注册，未激活的账户 
			String isCertified = userinfoShareResponse.getIsCertified(); // 是否通过实名认证 T是通过 F是没有实名认证 
			String isStudentCertified = userinfoShareResponse.getIsStudentCertified(); // 是否是学生  T是学生 F不是学生 
			model.addAttribute("user", userinfoShareResponse.getBody());
		} catch (AlipayApiException e) {
			// 处理异常
			e.printStackTrace();
		}
		return "success";
	}

	/* 第一步，跳转支付宝授权页面 */
	@RequestMapping("/zhifubaoLogin")
	public String zhifubaoLogin(HttpServletRequest request) {
		String httpAddress = HttpClientUtils.getHttpAddress(request);
		// httpAddress样例 http://weixindeve.tunnel.qydev.com/msdx1
		String url = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=APPID&scope=SCOPE&redirect_uri=ENCODED_URL";
		url = url.replace("APPID", AlipayConfig.APPID).replace("SCOPE", "auth_user").replace("ENCODED_URL", httpAddress + "/user/getAuthcode");
		return "redirect:" + url;
	}
}
