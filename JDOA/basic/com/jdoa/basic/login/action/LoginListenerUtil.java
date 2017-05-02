package com.jdoa.basic.login.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.jdoa.basic.login.model.TSysUser;



public class LoginListenerUtil implements HttpSessionListener,
		HttpSessionBindingListener {
	private static Map<String, Map<TSysUser, String>> userMap = new HashMap<String, Map<TSysUser, String>>();

	/**
	 * @author Action
	 * @describe 增加登陆监听器 创建session
	 * @date 2014-12-12
	 */
	public void sessionCreated(HttpSessionEvent event) {

	}

	/**
	 * @author Action
	 * @describe 增加登陆监听器 销毁session
	 * @date 2014-12-12
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		String sessionId = session.getId();
		userMap.remove(sessionId);
	}

	/**
	 * @author Action
	 * @describe 调用setAttrbute触发
	 * @date 2014-12-12
	 */
	public void valueBound(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		String sessionId = session.getId();
		TSysUser user = (TSysUser) session.getAttribute("user");
		//String ftime = JBPMUtil.formatDateToString(new Date(), Constant.YMD_HMS);
		Map<TSysUser, String> userTime = new HashMap<TSysUser, String>();
		userTime.put(user, "");
		userMap.put(sessionId, userTime);
	}

	/**
	 * @author Action
	 * @describe 调用removeAttrbute触发
	 * @date 2014-12-12
	 */
	public void valueUnbound(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		String sessionId = session.getId();
		userMap.remove(sessionId);
	}

	public static Map<String, Map<TSysUser, String>> getUserMap() {
		return userMap;
	}

	public static void setUserMap(Map<String, Map<TSysUser, String>> userMap) {
		LoginListenerUtil.userMap = userMap;
	}

}
