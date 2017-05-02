/**
 * Project Name:SIP
 * File Name:BaseDataUtil.java
 * Package Name:com.hnbp.cloud.jbpm.base
 * Date:2014-6-9下午3:42:24
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.jdoa.jbpm.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.struts2.ServletActionContext;

import com.jdoa.basic.jbpm.model.JBPMKey;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;



/**
 * ClassName:BaseDataUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-6-9 下午3:42:24 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public class BaseDataUtil {

	/**
	 * 
	 * saveData:(这里用一句话描述这个方法的作用). <br/>
	 * 保存数据。
	 * 
	 * @author Administrator
	 * @param map
	 * @param id
	 * @param type
	 * @throws SQLException
	 * @since JDK 1.6
	 */
	public static String saveData(Map map,String tableName) throws SQLException {
		String userid =null;
		if (ServletActionContext.getRequest().getSession()
				.getAttribute("userID") == null) {
			userid = "b00ad7e49dea406c9c6336654d5e94fb10000008";
		} else {
			userid= ServletActionContext.getRequest().getSession()
					.getAttribute("userID").toString();
		}
		StringBuffer feildBuf = new StringBuffer();
		StringBuffer valueBuf = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dataid = JDUuid.createID("jbpm0001");
		feildBuf.append(" fid,");
		feildBuf.append(" fcreator,");
		feildBuf.append(" fcreatetime,");
		valueBuf.append("'");
		valueBuf.append(dataid);
		valueBuf.append("',");
		valueBuf.append("'");
		valueBuf.append(userid);
		valueBuf.append("',");
		valueBuf.append("'");
		valueBuf.append(sdf.format(new Date()));
		valueBuf.append("',");
		Set set = map.entrySet();
		Iterator it = set.iterator();
		Entry elem = null;
		while (it.hasNext()) {
			elem = (Entry) it.next();
			feildBuf.append(elem.getKey());
			feildBuf.append(",");
			if (elem.getValue() == "sysdate") {
				valueBuf.append(elem.getValue() + ",");
			} else {
				valueBuf.append("'");
				valueBuf.append(elem.getValue());
				valueBuf.append("',");
			}
		}
		valueBuf.deleteCharAt(valueBuf.length() - 1);
		feildBuf.deleteCharAt(feildBuf.length() - 1);
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ");
		sql.append(tableName);
		sql.append("(");
		sql.append(feildBuf);
		sql.append(") values (");
		sql.append(valueBuf);
		sql.append(")");

		DataUtil.getJdbcUtil().execute(sql.toString());
		return dataid;
	}

	public static String saveData(Map map, String id, Map mapOnly)
			throws SQLException {
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		ResultSet rs = null;
		String tableName =null;
		String userid = ServletActionContext.getRequest().getSession()
				.getAttribute("userID").toString();
		StringBuffer feildBuf = new StringBuffer();
		StringBuffer valueBuf = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dataid = JDUuid.createID("JBPM0001");
		Iterator itOnly = mapOnly.keySet().iterator();
		boolean only = true;
		while (itOnly.hasNext()) {
			String key = itOnly.next().toString();
			String value = mapOnly.get(key).toString();
			String formValue = map.get(key).toString();
			if ("1".equals(value)) {
				String sql = "select " + key + " from " + tableName + " where "
						+ key + "='" + formValue + "'";
				rs = jdbcUtil.executeQuery(sql);
				while (rs.next()) {
					only = false;
					break;
				}
				if (only == false) {
					break;
				}
			}
		}
		if (only == true) {
			feildBuf.append(" fid,");
			feildBuf.append(" fcreator,");
			feildBuf.append(" fcreatetime,");
			valueBuf.append("'");
			valueBuf.append(dataid);
			valueBuf.append("',");
			valueBuf.append("'");
			valueBuf.append(userid);
			valueBuf.append("',");
			valueBuf.append("'");
			valueBuf.append(sdf.format(new Date()));
			valueBuf.append("',");

			Set set = map.entrySet();
			Iterator it = set.iterator();
			Entry elem = null;
			while (it.hasNext()) {
				elem = (Entry) it.next();
				feildBuf.append(elem.getKey());
				feildBuf.append(",");
				valueBuf.append("'");
				valueBuf.append(elem.getValue());
				valueBuf.append("',");
			}
			valueBuf.deleteCharAt(valueBuf.length() - 1);
			feildBuf.deleteCharAt(feildBuf.length() - 1);

			StringBuffer sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append("(");
			sql.append(feildBuf);
			sql.append(") values (");
			sql.append(valueBuf);
			sql.append(")");

			jdbcUtil.execute(sql.toString());
		} else {

		}
		return "SUCCESS";
	}
}
