package com.yz.pay.cert;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 证书信息
 * @author cyf
 *
 */
public class Cert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3064703601296950213L;
	
	/**
	 * 公钥
	 */
	private PublicKey publicKey;
	/**
	 * 私钥
	 */
	private PrivateKey privateKey;
	/**
	 * 证书编号
	 */
	private String certId;
	/**
	 * 商户名称
	 */
	private String mertName;
	/**
	 * 商户号
	 */
	private String merchantId;
	/**
	 * appid
	 */
	private String appId;
	
	/**
	 * 用户名
	 * */
	private String userName;
	/**
	 * 用户密码
	 */
	private String userPwd;

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public String getMertName() {
		return mertName;
	}

	public void setMertName(String mertName) {
		this.mertName = mertName;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
}
