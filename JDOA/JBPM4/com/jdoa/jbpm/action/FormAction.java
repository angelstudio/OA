package com.jdoa.jbpm.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;

public class FormAction {
	
	/**
	 * @author Action
	 * @date 2017-05-01
	 * @describe 数据保存前检测是否重复
	 */
	public void jbpm_Form_SJJC(){
		HttpServletRequest request = ActionUtil.getRequest();
		String json = request.getParameter("json");
		// 将字符串转换成json
		StringBuffer sb=new StringBuffer();
		JSONArray arr=JSONArray.fromObject(json);
		for (int i=0;i<arr.size();i++){
			JSONObject obj=arr.getJSONObject(i);
			String fname=obj.getString("fname");
			sb.append("'").append(fname).append("',");
		}
		JSONObject returnJson=new JSONObject();
		String returnVal="ok";
		String sql=null;
		if(sb.length()<1){
			sql="select fname from t_jbpm_zlb where 1=0 ";
		}else{
			sb.delete(sb.length()-1, sb.length());
			sql="select fname from t_jbpm_zlb where fname in("+sb.toString()+")";
		}
		ResultSet rs=DataUtil.getJdbcUtil().executeQuery(sql);
		
		try {
			if(rs.next()){
				String fname=rs.getString("fname");
				returnJson.put("text", fname);
				returnVal="error";
			}
			returnJson.put("flag", returnVal);
			ActionUtil.getResponse().getWriter().write(returnJson.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @author Action
	 * @date 2017-05-01
	 * @describe 流程表单处理
	 */
	public void jbpm_Form_Deal()
	{
		String returnValue="ok";
		HttpServletRequest request = ActionUtil.getRequest();
		String json = request.getParameter("json");
		// 将字符串转换成json
		JSONObject jsonobject = JSONObject.fromObject(json);
		// 提取出json里面的信息，增删改信息
		JSONArray arrayadd = jsonobject.getJSONArray("insert");
		JSONArray arrayupdate = jsonobject.getJSONArray("update");
		JSONArray arraydel = jsonobject.getJSONArray("delete");
		//判断增加数据是否为空。并且增加
		List sqlList=new ArrayList();
		try {
		if (!arrayadd.isEmpty()) {
			for (int i = 0; i < arrayadd.size(); i++) {
				JSONObject tempJson = JSONObject.fromObject(arrayadd.get(i));
				tempJson.put("fid", JDUuid.createID("jbpm2222"));
				String sql=MapUtil.getSQLByJson(tempJson, "t_jbpm_zlb");
				sqlList.add(sql);
			}
		}
		if(!arrayupdate.isEmpty())
		{
			for(int i = 0; i < arrayupdate.size(); i++)
			{
			JSONObject tempJson = JSONObject.fromObject(arrayupdate.get(i));
			String sql=MapUtil.getUpdateByJson(tempJson,  "t_jbpm_zlb");
			sqlList.add(sql);
			}
		}
		if(!arraydel.isEmpty())
		{
			for (int i = 0; i < arraydel.size(); i++) {
				JSONObject tempJson = JSONObject.fromObject(arraydel.get(i));
				String fid=tempJson.getString("fid");
				String fname=tempJson.getString("fname");
                String sql="delete from t_jbpm_zlb where fid='"+fid+"'";
                String zdsql="delete from t_jbpm_zdxx where fbdmc='"+fname+"'";
                String ansql="delete from  t_jbpm_anxx where fbdmc='"+fname+"'";
                String qqsql="delete from  t_jbpm_actionzlb where fname='"+fname+"'";
                sqlList.add(sql);
                sqlList.add(zdsql);
                sqlList.add(ansql);
                sqlList.add(qqsql);
			}
		}
		DataUtil.getJdbcUtil().executeBatch(sqlList);
		} catch (Exception e) {
			returnValue="error";
			e.printStackTrace();
		}
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("returnValue", returnValue);
		try {
			ActionUtil.getResponse().getWriter().write(returnJson.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @author Action
	 * @date 2017-05-01
	 * @describe 流程字段处理
	 */
	public void jbpm_ZDXX_Deal(){
		String returnValue="ok";
		HttpServletRequest request = ActionUtil.getRequest();
		String json = request.getParameter("json");
		// 将字符串转换成json
		JSONObject jsonobject = JSONObject.fromObject(json);
		// 提取出json里面的信息，增删改信息
		JSONArray arrayadd = jsonobject.getJSONArray("insert");
		JSONArray arrayupdate = jsonobject.getJSONArray("update");
		JSONArray arraydel = jsonobject.getJSONArray("delete");
		//判断增加数据是否为空。并且增加
		List sqlList=new ArrayList();
		try {
		if (!arrayadd.isEmpty()) {
			for (int i = 0; i < arrayadd.size(); i++) {
				JSONObject tempJson = JSONObject.fromObject(arrayadd.get(i));
				tempJson.put("fid", JDUuid.createID("jbpm2222"));
				String sql=MapUtil.getSQLByJson(tempJson, "t_jbpm_zdxx");
				sqlList.add(sql);
			}
		}
		if(!arrayupdate.isEmpty())
		{
			for(int i = 0; i < arrayupdate.size(); i++)
			{
			JSONObject tempJson = JSONObject.fromObject(arrayupdate.get(i));
			String sql=MapUtil.getUpdateByJson(tempJson,  "t_jbpm_zdxx");
			sqlList.add(sql);
			}
		}
		if(!arraydel.isEmpty())
		{
			for (int i = 0; i < arraydel.size(); i++) {
				JSONObject tempJson = JSONObject.fromObject(arraydel.get(i));
				String fid=tempJson.getString("fid");
                String zdsql="delete from t_jbpm_zdxx where fid='"+fid+"'";
                sqlList.add(zdsql);
			}
		}
		DataUtil.getJdbcUtil().executeBatch(sqlList);
		} catch (Exception e) {
			returnValue="error";
			e.printStackTrace();
		}
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("returnValue", returnValue);
		try {
			ActionUtil.getResponse().getWriter().write(returnJson.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Action
	 * @date 2017-05-01
	 * @describe 流程按钮处理
	 */
	public void jbpm_ANXX_Deal(){
		String returnValue="ok";
		HttpServletRequest request = ActionUtil.getRequest();
		String json = request.getParameter("json");
		// 将字符串转换成json
		JSONObject jsonobject = JSONObject.fromObject(json);
		// 提取出json里面的信息，增删改信息
		JSONArray arrayadd = jsonobject.getJSONArray("insert");
		JSONArray arrayupdate = jsonobject.getJSONArray("update");
		JSONArray arraydel = jsonobject.getJSONArray("delete");
		//判断增加数据是否为空。并且增加
		List sqlList=new ArrayList();
		try {
		if (!arrayadd.isEmpty()) {
			for (int i = 0; i < arrayadd.size(); i++) {
				JSONObject tempJson = JSONObject.fromObject(arrayadd.get(i));
				tempJson.put("fid", JDUuid.createID("jbpm2222"));
				String sql=MapUtil.getSQLByJson(tempJson, "t_jbpm_anxx");
				sqlList.add(sql);
			}
		}
		if(!arrayupdate.isEmpty())
		{
			for(int i = 0; i < arrayupdate.size(); i++)
			{
			JSONObject tempJson = JSONObject.fromObject(arrayupdate.get(i));
			String sql=MapUtil.getUpdateByJson(tempJson,  "t_jbpm_anxx");
			sqlList.add(sql);
			}
		}
		if(!arraydel.isEmpty())
		{
			for (int i = 0; i < arraydel.size(); i++) {
				JSONObject tempJson = JSONObject.fromObject(arraydel.get(i));
				String fid=tempJson.getString("fid");
                String zdsql="delete from t_jbpm_anxx where fid='"+fid+"'";
                sqlList.add(zdsql);
			}
		}
		DataUtil.getJdbcUtil().executeBatch(sqlList);
		} catch (Exception e) {
			returnValue="error";
			e.printStackTrace();
		}
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("returnValue", returnValue);
		try {
			ActionUtil.getResponse().getWriter().write(returnJson.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Action
	 * @date 2017-05-01
	 * @describe 流程请求处理
	 */
	public void jbpm_QQPZXX_Deal(){
		String returnValue="ok";
		HttpServletRequest request = ActionUtil.getRequest();
		String json = request.getParameter("json");
		// 将字符串转换成json
		JSONObject jsonobject = JSONObject.fromObject(json);
		// 提取出json里面的信息，增删改信息
		JSONArray arrayadd = jsonobject.getJSONArray("insert");
		JSONArray arrayupdate = jsonobject.getJSONArray("update");
		JSONArray arraydel = jsonobject.getJSONArray("delete");
		//判断增加数据是否为空。并且增加
		List sqlList=new ArrayList();
		try {
		if (!arrayadd.isEmpty()) {
			for (int i = 0; i < arrayadd.size(); i++) {
				JSONObject tempJson = JSONObject.fromObject(arrayadd.get(i));
				tempJson.put("fid", JDUuid.createID("jbpm2222"));
				String sql=MapUtil.getSQLByJson(tempJson, "t_jbpm_actionzlb");
				sqlList.add(sql);
			}
		}
		if(!arrayupdate.isEmpty())
		{
			for(int i = 0; i < arrayupdate.size(); i++)
			{
			JSONObject tempJson = JSONObject.fromObject(arrayupdate.get(i));
			String sql=MapUtil.getUpdateByJson(tempJson,  "t_jbpm_actionzlb");
			sqlList.add(sql);
			}
		}
		if(!arraydel.isEmpty())
		{
			for (int i = 0; i < arraydel.size(); i++) {
				JSONObject tempJson = JSONObject.fromObject(arraydel.get(i));
				String fid=tempJson.getString("fid");
                String zdsql="delete from t_jbpm_actionzlb where fid='"+fid+"'";
                sqlList.add(zdsql);
			}
		}
		DataUtil.getJdbcUtil().executeBatch(sqlList);
		} catch (Exception e) {
			returnValue="error";
			e.printStackTrace();
		}
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("returnValue", returnValue);
		try {
			ActionUtil.getResponse().getWriter().write(returnJson.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
