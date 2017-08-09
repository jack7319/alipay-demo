package com.bizideal.mn.config;

/**
 * @author 作者 liulq:
 * @data 创建时间：2017年3月3日 下午6:13:58
 * @version 1.0
 */
public class WeChatConfig {

	public static final String appId = "wx54799dab8009a3de";
	public static final String appSecret = "53de5f94ef0d783847482764de655327";
	private String partnerId;
	private String partnerKey;
	private String token;
	private String aesKey;
	private String profile;

	public WeChatConfig() {
		super();
	}

	public WeChatConfig(String partnerId, String partnerKey, String token,
			String aesKey, String profile) {
		super();
		this.partnerId = partnerId;
		this.partnerKey = partnerKey;
		this.token = token;
		this.aesKey = aesKey;
		this.profile = profile;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerKey() {
		return partnerKey;
	}

	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public static String getAppid() {
		return appId;
	}

	public static String getAppsecret() {
		return appSecret;
	}

	@Override
	public String toString() {
		return "WeChatConfig [partnerId=" + partnerId + ", partnerKey="
				+ partnerKey + ", token=" + token + ", aesKey=" + aesKey
				+ ", profile=" + profile + "]";
	}

}
