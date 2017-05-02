package com.jdoa.tool;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

public class TableUtil {
 /**
  * 	
  * @Title: TableString.java
  * @Description: 获取无分页的tabstring
  * @author Action
  * @date 2016-6-7
  */
 public static String getNoLimitTabStr(String sql) throws SQLException {
		JDBCUtil jdbcUtil=DataUtil.getJdbcUtil();
		ResultSet rs = jdbcUtil.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
					rowData.put(rsmd.getColumnName(i).toLowerCase(),rs
							.getObject(i));
			}
			list.add(rowData);
		}
		GridDataModel<Map<String, Object>> model = new GridDataModel<Map<String, Object>>();
		model.setTotal(list.size());
		model.setRows(list);
		return JSONObject.fromObject(model).toString();
	}
 
 /**
  * 
  * @Title: TableString.java
  * @Description: 获取无分页List
  * @author Action
  * @date 2016-6-7
  */
 public static List getNoLimitTabList(String sql) throws SQLException {
		JDBCUtil jdbcUtil=DataUtil.getJdbcUtil();
		ResultSet rs = jdbcUtil.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
					rowData.put(rsmd.getColumnName(i).toLowerCase(),rs
							.getObject(i));
			}
			list.add(rowData);
		}

		return list;
	}
 
 /**
  * 	
  * @Title: TableString.java
  * @Description: 获取tabstring
  * @author Action
  * @date 2016-6-7
  */
 public static String getTabStr(String sql, int starta,int limitb) throws SQLException {
		JDBCUtil jdbcUtil=DataUtil.getJdbcUtil();
		limitb = starta + limitb;
		String exesql = "select * from(select A.* ,ROWNUM RN FROM (" + sql
				+ ")A )where RN<=" + limitb + " and RN>" + starta;
		String totalSql="select count(*) from("+sql+")";
		ResultSet totalrs = jdbcUtil.executeQuery(totalSql);
		int total = 0;
		while (totalrs.next()) {
			total = totalrs.getInt(1);
		}
		ResultSet rs = jdbcUtil.executeQuery(exesql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
					rowData.put(rsmd.getColumnName(i).toLowerCase(),rs
							.getObject(i));
			}
			list.add(rowData);
		}
		GridDataModel<Map<String, Object>> model = new GridDataModel<Map<String, Object>>();
		model.setTotal(total);
		model.setRows(list);
		return JSONObject.fromObject(model).toString();
	}
 /**
  * 
  * @Title: TableString.java
  * @Description: 获取list
  * @author Action
  * @date 2016-6-7
  */
 public static List getTabList(String sql, int starta,int limitb) throws SQLException {
		JDBCUtil jdbcUtil=DataUtil.getJdbcUtil();
		limitb = starta + limitb;
		String exesql = "select * from(select A.* ,ROWNUM RN FROM (" + sql
				+ ")A )where RN<=" + limitb + " and RN>" + starta;
		String totalSql="select count(*) from("+sql+")";
		ResultSet totalrs = jdbcUtil.executeQuery(totalSql);
		int total = 0;
		while (totalrs.next()) {
			total = totalrs.getInt(1);
		}
		ResultSet rs = jdbcUtil.executeQuery(exesql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
					rowData.put(rsmd.getColumnName(i).toLowerCase(),rs
							.getObject(i));
			}
			list.add(rowData);
		}

		return list;
	}
 /**
	 * @author Action
	 *@param sql
	 *@param starta
	 *@param limitb
	 *@param mapKey 需要进行计算的字段Map集合和初始值
	 *@param decimalFormat 小数位格式  # 一个数字，不包括 0,like:##.## ;
	 *@return
	 *@throws SQLException
	 *@date    2016-06-07
	 *@describe 产生有合计的列表
	 */
	public static String getTabStrHaveSum(String sql, int starta,int limitb, Set<String> sumSet, String decimalFormat)
			throws SQLException {
		//sumSet初始化，得到合计的初始HashMap
		HashMap<String,Double> sumMap=new HashMap<String,Double>();
		for(Iterator<String> it = sumSet.iterator();it.hasNext();){
			String key=it.next();
			sumMap.put(key, 0.0);
		}
		JDBCUtil jdbcUtil=DataUtil.getJdbcUtil();
		limitb = starta + limitb;
		String exesql = "select * from(select A.* ,ROWNUM RN FROM (" + sql
				+ ")A )where RN<=" + limitb + " and RN>" + starta;
		ResultSet rs = jdbcUtil.executeQuery(exesql);
		String totalSql="select count(*) from("+sql+")";
		ResultSet totalrs = jdbcUtil.executeQuery(totalSql);
		int total = 0;
		while (totalrs.next()) {
			total = totalrs.getInt(1);
		}
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		while (rs.next()) {
			Map<String, String> rowData = new HashMap<String, String>();
			for (int i = 1; i <= columnCount; i++) {
				if (!(rsmd.getColumnName(i).toLowerCase().equals("rn"))) {
					rowData.put(rsmd.getColumnName(i).toLowerCase(), rs
							.getObject(i) == null ? "" : rs.getObject(i)
							.toString());
				}
			}
			list.add(rowData);
		}
		if(StringUtil.isEmpty(decimalFormat)){
			decimalFormat="##.##";
		}
		DecimalFormat df = new DecimalFormat(decimalFormat);
		for (int j = 0, len = list.size(); j < len; j++) {
			Map<String, String> mapList = (Map<String,String>) list.get(j);
			for(Iterator<String> it=sumMap.keySet().iterator();it.hasNext();){
				String sumKey=it.next();
				String dbStr=mapList.get(sumKey);
				//如果为空则什么不做，否则进行转换累计;
				if(StringUtil.isEmpty(dbStr)){
					
				}else{
					Double db=Double.valueOf(dbStr);
					sumMap.put(sumKey,(sumMap.get(sumKey)+db));
				}
			}
			//最后一次进行数据格式化
			if(len==j+1){
				HashMap lastMap=new HashMap();
				for(Iterator<String> it=mapList.keySet().iterator();it.hasNext();){
					String key=it.next();
					if(sumMap.containsKey(key)){
						Double value=sumMap.get(key);
						lastMap.put(key, value);
					}else{
						lastMap.put(key, "");
					}
				}
				list.add(lastMap);
			}
		}
		GridDataModel<Map<String, ?>> model = new GridDataModel<Map<String, ?>>();
		model.setTotal(total);
		model.setRows(list);
		return JSONObject.fromObject(model).toString();
	}
	 /**
		 * @author Action
		 *@param sql
		 *@param starta
		 *@param limitb
		 *@param mapKey 需要进行计算的字段Map集合和初始值
		 *@param decimalFormat 小数位格式  # 一个数字，不包括 0,like:##.## ;
		 *@return
		 *@throws SQLException
		 *@date    2016-06-07
		 *@describe 产生有合计的List
		 */
	public static GridDataModel getTabListHaveSum(String sql, int starta,int limitb, Set<String> sumSet, String decimalFormat)
			throws SQLException {
		//sumSet初始化，得到合计的初始HashMap
		HashMap<String,Double> sumMap=new HashMap<String,Double>();
		for(Iterator<String> it = sumSet.iterator();it.hasNext();){
			String key=it.next();
			sumMap.put(key, 0.0);
		}
		JDBCUtil jdbcUtil=DataUtil.getJdbcUtil();
		limitb = starta + limitb;
		String exesql = "select * from(select A.* ,ROWNUM RN FROM (" + sql
				+ ")A )where RN<=" + limitb + " and RN>" + starta;
		ResultSet rs = jdbcUtil.executeQuery(exesql);
		String totalSql="select count(*) from("+sql+")";
		ResultSet totalrs = jdbcUtil.executeQuery(totalSql);
		int total = 0;
		while (totalrs.next()) {
			total = totalrs.getInt(1);
		}
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		while (rs.next()) {
			Map<String, String> rowData = new HashMap<String, String>();
			for (int i = 1; i <= columnCount; i++) {
				if (!(rsmd.getColumnName(i).toLowerCase().equals("rn"))) {
					rowData.put(rsmd.getColumnName(i).toLowerCase(), rs
							.getObject(i) == null ? "" : rs.getObject(i)
							.toString());
				}
			}
			list.add(rowData);
		}
		if(StringUtil.isEmpty(decimalFormat)){
			decimalFormat="##.##";
		}
		DecimalFormat df = new DecimalFormat(decimalFormat);
		for (int j = 0, len = list.size(); j < len; j++) {
			Map<String, String> mapList = (Map<String,String>) list.get(j);
			for(Iterator<String> it=sumMap.keySet().iterator();it.hasNext();){
				String sumKey=it.next();
				String dbStr=mapList.get(sumKey);
				//如果为空则什么不做，否则进行转换累计;
				if(StringUtil.isEmpty(dbStr)){
					
				}else{
					Double db=Double.valueOf(dbStr);
					sumMap.put(sumKey,(sumMap.get(sumKey)+db));
				}
			}
			//最后一次进行数据格式化
			if(len==j+1){
				HashMap lastMap=new HashMap();
				for(Iterator<String> it=mapList.keySet().iterator();it.hasNext();){
					String key=it.next();
					if(sumMap.containsKey(key)){
						Double value=sumMap.get(key);
						lastMap.put(key, df.format(value));
					}else{
						lastMap.put(key, "");
					}
				}
				list.add(lastMap);
			}
		}
		GridDataModel model = new GridDataModel();
		model.setTotal(total);
		model.setRows(list);
		return model;
	}
	
	 /**
		 * @author Action
		 *@param sql
		 *@param starta
		 *@param limitb
		 *@param Set 需要计算的key
		 *@param decimalFormat 小数位格式  # 一个数字，不包括 0,like:##.## ;
		 *@return
		 *@throws SQLException
		 *@date    2014-11-8
		 *@describe 产生无分页有合计的列表
		 */
		public static String getTabStrHaveSumNoLimit(String sql ,Set<String> sumSet, String decimalFormat)
				throws SQLException {
			//sumSet初始化，得到合计的初始HashMap
			HashMap<String,Double> sumMap=new HashMap<String,Double>();
			for(Iterator<String> it = sumSet.iterator();it.hasNext();){
				String key=it.next();
				sumMap.put(key, 0.0);
			}
			JDBCUtil jdbcUtil=DataUtil.getJdbcUtil();
			ResultSet rs = jdbcUtil.executeQuery(sql);
			String totalSql="select count(*) from("+sql+")";
			ResultSet totalrs = jdbcUtil.executeQuery(totalSql);
			int total = 0;
			while (totalrs.next()) {
				total = totalrs.getInt(1);
			}
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
			while (rs.next()) {
				Map<String, String> rowData = new HashMap<String, String>();
				for (int i = 1; i <= columnCount; i++) {
					if (!(rsmd.getColumnName(i).toLowerCase().equals("rn"))) {
						rowData.put(rsmd.getColumnName(i).toLowerCase(), rs
								.getObject(i) == null ? "" : rs.getObject(i)
								.toString());
					}
				}
				list.add(rowData);
			}
			if(StringUtil.isEmpty(decimalFormat)){
				decimalFormat="##.##";
			}
			DecimalFormat df = new DecimalFormat(decimalFormat);
			for (int j = 0, len = list.size(); j < len; j++) {
				Map<String, String> mapList = (Map<String,String>) list.get(j);
				for(Iterator<String> it=sumMap.keySet().iterator();it.hasNext();){
					String sumKey=it.next();
					String dbStr=mapList.get(sumKey);
					//如果为空则什么不做，否则进行转换累计;
					if(StringUtil.isEmpty(dbStr)){
						
					}else{
						Double db=Double.valueOf(dbStr);
						sumMap.put(sumKey,(sumMap.get(sumKey)+db));
					}
				}
				//最后一次进行数据格式化
				if(len==j+1){
					HashMap lastMap=new HashMap();
					for(Iterator<String> it=mapList.keySet().iterator();it.hasNext();){
						String key=it.next();
						if(sumMap.containsKey(key)){
							Double value=sumMap.get(key);
							lastMap.put(key, df.format(value));
						}else{
							lastMap.put(key, "");
						}
					}
					list.add(lastMap);
				}
			}
			GridDataModel<Map<String, ?>> model = new GridDataModel<Map<String, ?>>();
			model.setTotal(total);
			model.setRows(list);
			return JSONObject.fromObject(model).toString();
		}
		/**
		 * 
		 * @Title: TableString.java
		 * @Description: 获取合计无分页list
		 * @author Action
		 * @date 2016-6-7
		 */
		public static List getTabListHaveSumNoLimit(String sql ,Set<String> sumSet, String decimalFormat)
				throws SQLException {
			//sumSet初始化，得到合计的初始HashMap
			HashMap<String,Double> sumMap=new HashMap<String,Double>();
			for(Iterator<String> it = sumSet.iterator();it.hasNext();){
				String key=it.next();
				sumMap.put(key, 0.0);
			}
			JDBCUtil jdbcUtil=DataUtil.getJdbcUtil();
			ResultSet rs = jdbcUtil.executeQuery(sql);
			String totalSql="select count(*) from("+sql+")";
			ResultSet totalrs = jdbcUtil.executeQuery(totalSql);
			int total = 0;
			while (totalrs.next()) {
				total = totalrs.getInt(1);
			}
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
			while (rs.next()) {
				Map<String, String> rowData = new HashMap<String, String>();
				for (int i = 1; i <= columnCount; i++) {
					if (!(rsmd.getColumnName(i).toLowerCase().equals("rn"))) {
						rowData.put(rsmd.getColumnName(i).toLowerCase(), rs
								.getObject(i) == null ? "" : rs.getObject(i)
								.toString());
					}
				}
				list.add(rowData);
			}
			if(StringUtil.isEmpty(decimalFormat)){
				decimalFormat="##.##";
			}
			DecimalFormat df = new DecimalFormat(decimalFormat);
			for (int j = 0, len = list.size(); j < len; j++) {
				Map<String, String> mapList = (Map<String,String>) list.get(j);
				for(Iterator<String> it=sumMap.keySet().iterator();it.hasNext();){
					String sumKey=it.next();
					String dbStr=mapList.get(sumKey);
					//如果为空则什么不做，否则进行转换累计;
					if(StringUtil.isEmpty(dbStr)){
						
					}else{
						Double db=Double.valueOf(dbStr);
						sumMap.put(sumKey,(sumMap.get(sumKey)+db));
					}
				}
				//最后一次进行数据格式化
				if(len==j+1){
					HashMap lastMap=new HashMap();
					for(Iterator<String> it=mapList.keySet().iterator();it.hasNext();){
						String key=it.next();
						if(sumMap.containsKey(key)){
							Double value=sumMap.get(key);
							lastMap.put(key, df.format(value));
						}else{
							lastMap.put(key, "");
						}
					}
					list.add(lastMap);
				}
			}
			return list;
		}
}
