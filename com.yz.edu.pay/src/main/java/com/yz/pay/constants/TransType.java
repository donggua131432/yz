package com.yz.pay.constants;

public enum TransType {
	
	WECHAT("wechat"),
	UNIONPAY("unionpay"),
	ALIPAY("alipay"),
	ALLINPAY("allinpay");
	
	private String type;
	
	private TransType(String type) {
		this.type= type;
	}
	
	public String type() {
		return type;
	}
	
	public static TransType get(String name){   
        //所有的枚举值   
		TransType[] types = values();   
       //遍历查找   
       for(TransType t : types){   
           if(t.type().equals(name)){   
             	return t;  
           }   
       }   
       return null;   
    }

}
