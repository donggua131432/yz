package com.yz.pay.factory;

 
import java.security.Security; 
 

import com.auth0.jwt.internal.org.bouncycastle.jce.provider.BouncyCastleProvider;
 

public class AllinPayFactory  {

	
	private static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();
	
	static {
		Security.addProvider(PROVIDER);
	}
 

	public static BouncyCastleProvider getProvider() {return PROVIDER;}
	
}
