package com.yz.core.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.yz.constants.UsConstants;
import com.yz.core.constants.UsRelationConstants;
import com.yz.model.UsConsults;

/**
 * 
 * @author Administrator
 *
 */
public class UsRelationUtil {

	private static Logger logger = LoggerFactory.getLogger(UsRelationUtil.class);
	
	/**
	 * 
	 * @param relation
	 * @return
	 */
	public static boolean isEmp(int relation )
	{
		return calc(relation, UsRelationConstants.N_USER_TYPE_EMPLOYEE);
	}
	
	/**
	 * 
	 * @param relation
	 * @return
	 */
	public static List<String> makeUserTypes(int relation )
	{
		 List<String>userTypes = Lists.newArrayList();
		 if((relation&UsRelationConstants.N_USER_TYPE_EMPLOYEE)>0){userTypes.add(UsConstants.I_USER_TYPE_EMPLOYEE);}
		 if((relation&UsRelationConstants.N_USER_TYPE_STUDENT)>0){userTypes.add(UsConstants.I_USER_TYPE_STUDENT);}
		 return userTypes;
	}
	
	
	
	
	/**
	 * 
	 * @param relation
	 * @return
	 */
	public static boolean isStd(int relation )
	{
		return calc(relation, UsRelationConstants.N_USER_TYPE_STUDENT);
	}
	
	/**
	 * 
	 * @param relation
	 * @param targetType
	 * @return
	 */
	private static boolean calc(int relation,int targetType)
	{
		logger.info("relation:{},targetType:{}",relation,targetType);
		return (relation&targetType)>0;
	}
}
