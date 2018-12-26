package com.yz.util;


/**
 * 验证工具类
 * @author zl
 *
 */
public class ValidationUtil {
	
	private static ValidationUtil instance;
	
	private ValidationUtil(){}
	
	public static ValidationUtil getInstance() {
		if(ValidationUtil.instance == null) {
			ValidationUtil.instance = new ValidationUtil();
		}
		return ValidationUtil.instance;
	}
    
	 /** 
     * 判断是否为正确的邮件格式 
     * @return boolean 
     */  
    public  ValidationUtil isEmail(String arg0){
    	isRegExp(arg0,"^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$","请输入有效邮箱格式");
    	 return this ;
    }  
      
    /** 
     * 判断字符串是否为合法手机号 11位 13 14 15 18开头 
     * @return boolean 
     */  
    public  ValidationUtil isMobile(String arg0){
    	isRegExp(arg0,"^(13|14|15|18)\\d{9}$","请输入有效电话号码");
    	return this;
    }  
      
    /** 
     * 判断是否为数字 
     * @return 
     */  
    public ValidationUtil isNumber(String arg0) {  
        try{  
            Integer.parseInt(arg0);  
            return this;  
        }catch(Exception ex){  
        	throw new IllegalArgumentException("不为数字");  
        }  
    }  
      
          
    /** 
     * 判断字符串是否为非空(包含null与"") 
     * @return 
     */  
    public ValidationUtil isNotEmpty(String arg0){  
        if(arg0 == null || "".equals(arg0))  
        	throw new IllegalArgumentException("不能为null或");  
        return this;  
    }  
      
    /** 
     * 判断字符串是否为非空(包含null与"","    ") 
     * @return 
     */  
    public ValidationUtil isNotEmptyIgnoreBlank(String arg0){  
        if(arg0 == null || "".equals(arg0) || "".equals(arg0.trim()))  
        	throw new IllegalArgumentException("不能为null或 或    ");  
        return this;  
    }  
      
    /** 
     * 判断字符串是否为空(包含null与"") 
     * @param str 
     * @return 
     */  
    public ValidationUtil isEmpty(String arg0){  
        if(arg0 == null || "".equals(arg0))  
        	throw new IllegalArgumentException("不能为null或 ");  
            return this;  
    }  
    
    /** 
     * 判断字符串是否超过指定长度
     * @return 
     */  
    public ValidationUtil isExceed(String arg0,int arg1){  
        if(arg1<arg0.length())
        	throw new IllegalArgumentException("超过指定长度");
        	return this;
    }  
    
    /** 
     * 判断是否大于
     * @return 
     */  
    public ValidationUtil isLgt(int arg0,int arg1){  
        if(arg1<arg0)
        	return this;
        	throw new IllegalArgumentException("不大于");
    }  
    
    /** 
     * 判断是否小于
     * @return 
     */  
    public ValidationUtil isLt(int arg0,int arg1){  
        if(arg1>arg0)
        	return this;
        	throw new IllegalArgumentException("不小于");
    }  
    /** 
     * 判断是否大于小于
     * @return 
     */  
    public ValidationUtil isLgtLt(int arg0,int arg1,int arg2){  
        if( arg1<arg0 && arg2>arg0)
        	return this;
        	throw new IllegalArgumentException("不大于小于");
    }  
    
    /** 
     * 判断是否符合指定表达式
     * @return 
     */  
    public ValidationUtil isRegExp(String arg0,String regx,String info){  
    	if(arg0.matches(regx))
     	   return this;
        throw new IllegalArgumentException(info); 
    }  
}
