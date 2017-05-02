package com.jdoa.tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

/**
 * @describe MAP工具类
 * @author Action
 * 
 */
public class MapUtil {

	/**
	 * 
	 * @Title: MapUtil.java
	 * @Description: 根据map获取sql
	 * @author Action
	 * @date 2016-12-23
	 */
	public static String getSQL(HashMap hashMap, String tableName) {
		StringBuffer feildBuf = new StringBuffer();
		StringBuffer valueBuf = new StringBuffer();
		Set set = hashMap.entrySet();
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
		return sql.toString();
	};

	/**
	 * 
	 * @Title: MapUtil.java
	 * @Description: 根据JSON获取sql
	 * @author Action
	 * @date 2016-12-23
	 */
	public static String getSQLByJson(JSONObject json, String tableName) {
		StringBuffer feildBuf = new StringBuffer();
		StringBuffer valueBuf = new StringBuffer();
		Set set = json.entrySet();
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
		return sql.toString();
	};
	/**
	 * 
	 * @Title: MapUtil.java
	 * @Description: TODO
	 * @author Action
	 * @date 2016-12-26
	 */
	public static String getUpdateSql(HashMap hashMap, String tableName){
		Set set = hashMap.entrySet();
		Iterator it = set.iterator();
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(tableName).append(" set ");
		Entry elem = null;
		while (it.hasNext()) {
			elem = (Entry) it.next();
			sql.append(elem.getKey()).append("='").append(elem.getValue()).append("',");
		}
		sql.delete(sql.length() - 1, sql.length());
		sql.append(" where fid='").append(hashMap.get("fid")).append("'");
		return sql.toString();
	}

	/**
	 * 
	 * @Title: MapUtil.java
	 * @Description: TODO
	 * @author Action
	 * @date 2016-12-26
	 */
	public static String getUpdateByJson(JSONObject json, String tableName){
		Set set = json.entrySet();
		Iterator it = set.iterator();
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(tableName).append(" set ");
		Entry elem = null;
		while (it.hasNext()) {
			elem = (Entry) it.next();
			sql.append(elem.getKey()).append("='").append(elem.getValue()).append("',");
		}
		sql.delete(sql.length() - 1, sql.length());
		sql.append(" where fid='").append(json.getString("fid")).append("'");
		return sql.toString();
	}
}
