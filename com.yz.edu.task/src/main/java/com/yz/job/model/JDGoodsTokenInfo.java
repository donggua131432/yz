package com.yz.job.model;

import java.io.Serializable;
/**
 * 京东实物对接 
 * @author lx
 * @date 2018年3月26日 上午11:28:20
 */
public class JDGoodsTokenInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4463796022409060981L;
	
	private String jdId;
	private String accessToken;
	private String refreshToken;
	private String tokenExpire;
	private String refreshTokenExpires;
	public String getJdId()
	{
		return jdId;
	}
	public void setJdId(String jdId)
	{
		this.jdId = jdId;
	}
	public String getAccessToken()
	{
		return accessToken;
	}
	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}
	public String getRefreshToken()
	{
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken)
	{
		this.refreshToken = refreshToken;
	}
	public String getTokenExpire()
	{
		return tokenExpire;
	}
	public void setTokenExpire(String tokenExpire)
	{
		this.tokenExpire = tokenExpire;
	}
	public String getRefreshTokenExpires()
	{
		return refreshTokenExpires;
	}
	public void setRefreshTokenExpires(String refreshTokenExpires)
	{
		this.refreshTokenExpires = refreshTokenExpires;
	}
}
