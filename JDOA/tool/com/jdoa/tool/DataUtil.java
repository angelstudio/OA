/**
 * Project Name:SIP
 * File Name:JBPMDBUtil.java
 * Package Name:com.hnbp.cloud.jbpm.base
 * Date:2014-5-26上午11:49:18
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.jdoa.tool;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * ClassName:JBPMDBUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: 数据库操作类 Date: 2014-5-26 上午11:49:18 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public class DataUtil {
    private static JDBCUtil jdbcUtil;
	/**
	 * 
	 * getJdbcUtil:(这里用一句话描述这个方法的作用). <br/>
	 * 获取数据库连接执行 TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author Administrator
	 * @return
	 * @since JDK 1.6
	 */
	public static JDBCUtil getJdbcUtil() {
		 JDBCUtil jdbc=null;
		if(jdbcUtil==null){
		   jdbc = (JDBCUtil)getSpringBean("jdbcUtil");
		   jdbcUtil=jdbc;
		}
		return jdbcUtil;
	}
	/**
	 * @author Action
	 * @param beanid
	 * @return
	 * @date 2017-04-03
	 * @describe 容器中获取bean
	 */
	public static  Object getSpringBean(String beanid) {
		ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(ActionUtil.getRequest()
						.getSession().getServletContext());
		Object objBean = context.getBean(beanid);
		return objBean;
	}
}
