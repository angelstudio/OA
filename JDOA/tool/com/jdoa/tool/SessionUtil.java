package com.jdoa.tool;

import org.apache.struts2.ServletActionContext;

import com.jdoa.basic.login.model.TSysUser;


/**
 * session工具类
 * @author lyle
 *
 */
public class SessionUtil {

	public static String getUserId(){
		return ServletActionContext.getRequest().getSession().getAttribute("userID").toString();
	}
	
	
	public static String getUserName(){
		return ServletActionContext.getRequest().getSession().getAttribute("username").toString();
	}
	
	public static TSysUser getUser(){
		return  (TSysUser) ServletActionContext.getRequest().getSession().getAttribute("user");
	}
	
	
	
}
