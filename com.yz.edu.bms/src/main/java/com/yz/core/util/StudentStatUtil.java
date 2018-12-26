package com.yz.core.util;

/**
 * @描述: ${DESCRIPTION}
 * @作者: DuKai
 * @创建时间: 2017/10/12 11:42
 * @版本号: V1.0
 */
public class StudentStatUtil {


    public static boolean isFieldString(String str1, String str2){
        String[] array = str1.split(",");
        for(int i=0; i<array.length; i++){
            if(array[i].equals(str2)){
                return true;
            }
        }
        return false;
    }
}
