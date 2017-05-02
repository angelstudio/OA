package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.basic.person.model.Person;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.TableUtil;

public class AllotAction{

	/**
	 * 资产调拨列表
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  allotList() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		String ftitle = request.getParameter("ftitle");
		String fuseunit =request.getParameter("fuseunit");
		
		String sql = "select * from T_ASSETALLOT WHERE　1=1 ";
		if(ftitle!=null && ftitle.length()>0){
			sql+=" and ftitle like'%"+ftitle+"%' ";
		}
		
		if(fuseunit!=null && fuseunit.length()>0){
			sql+=" and fuseunit like'%"+fuseunit+"%' ";
		}
		
		String datastr = null;
		try {
		  datastr = TableUtil.getTabStr(sql, start, limit);
		} catch (SQLException e) {
		  e.printStackTrace();
		}
		  ActionUtil.getResponse().getWriter().write(datastr);
	}
	
	/**
	 * 调拨详情
	 * @throws IOException 
	 */
	public void AllotDetails() throws IOException{
		
		String fid = ActionUtil.getRequest().getParameter("fid");
		int start = Integer.parseInt(ActionUtil.getRequest().getParameter("start"));
		int limit= Integer.parseInt(ActionUtil.getRequest().getParameter("limit"));
		String sql = "select * from T_ASSETALLOT_DETAIL WHERE FASSETALLOTID =  '"+fid+"' ";
		String datastr = null;
		try {
		  datastr = TableUtil.getTabStr(sql, start, limit);
		} catch (SQLException e) {
		  e.printStackTrace();
		}
		  ActionUtil.getResponse().getWriter().write(datastr);
		
	}
	
	/**
	 * 申购调拨添加或者修改
	 * @throws IOException 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void addOrEditAllot() throws IOException{
		 JDBCUtil jdbc =DataUtil.getJdbcUtil();
		 HttpServletRequest request = ActionUtil.getRequest();
		 String operation =request.getParameter("operation");
		 HttpSession session = request.getSession();
		 TSysUser user = (TSysUser) session.getAttribute("user");
		 Person person =user.getPerson();
		 Map<String, Object> map =new  HashMap<String, Object>();
		 map.put("fid", request.getParameter("fid"));
	     map.put("ftitle", request.getParameter("ftitle"));
		 map.put("findepartment", request.getParameter("findepartment")); 
		 map.put("foutdepartment", request.getParameter("foutdepartment")); 
		 map.put("fassetmanagement", request.getParameter("fassetmanagement")); 
		 map.put("foutpeople", request.getParameter("foutpeople")); 
		 map.put("finpeople", request.getParameter("finpeople")); 
		 map.put("fdbtype", request.getParameter("fdbtype")); 
		 map.put("ftype", request.getParameter("ftype"));
		 map.put("fuseunit", request.getParameter("fuseunit"));
		 map.put("fapprover", request.getParameter("fapprover"));
		 map.put("fapproveldate", request.getParameter("fapproveldate"));
		 map.put("fstatus", request.getParameter("fstatus"));
		 
		 String formData = ActionUtil.getRequest().getParameter("formData"); 

	     Map<String,Object> datamap = (Map<String,Object>) JSONObject.fromObject(formData);
	     
		 //更新数据  
	     List<Map<String,Object>> updateList = (List<Map<String,Object>>)datamap.get("update"); 
	     
	     //添加数据
	     List<Map<String,Object>> addList = (List<Map<String,Object>>)datamap.get("insert"); 
	     
	     //删除数据
	     List<Map<String,Object>> delList = (List<Map<String,Object>>)datamap.get("delete"); 
	     
		if(operation.equals("modify")){
			 
		 String upsql = MapUtil.getUpdateSql((HashMap) map, "T_ASSETALLOT");
		 //修改资产
		 jdbc.execute(upsql);
		 
		 if(addList.size()>0){
			 for (Map<String, Object> map2 : addList) {
				   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
				   jsonObject.remove("operation");
				   jsonObject.remove("rn");
				   jsonObject.element("fcreateman", "");
				   jsonObject.element("fid", JDUuid.createID("efb453t7"));
				   jsonObject.element("fassetallotid", map.get("fid"));
				   jsonObject.element("fcreatedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()) );//创建时间
				   jsonObject.element("fmodifier", "");
				   jsonObject.element("fmodifieddate", "");
				   String  addsql = MapUtil.getSQLByJson(jsonObject, "T_ASSETALLOT_DETAIL");
				   jdbc.execute(addsql);
			  } 
		 }
		 
		 if(updateList.size()>0){
		     for (Map<String, Object> map2 : updateList) {
			   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
			   jsonObject.remove("operation");
			   jsonObject.remove("rn");
			   String  updatasql = MapUtil.getUpdateByJson(jsonObject, "T_ASSETALLOT_DETAIL");
			   jdbc.execute(updatasql);
		     }
		}
		 
		 if(delList.size()>0){
		     for (Map<String, Object> map2 : delList) {
			   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
			   String fid = (String) jsonObject.get("fid");
			   String sql = "delete from T_ASSETALLOT_DETAIL where fid=  '"+fid+"' ";
			   jdbc.execute(sql);
		     }
		} 
		 
	 }else if(operation.equals("add")){
		 map.put("fid", JDUuid.createID("asdbuytr"));
		 map.put("faudit", "0");
		 map.put("fapplicant", person.getFname());
		 map.put("fapplydate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		 String sql = MapUtil.getSQL((HashMap) map, "T_ASSETALLOT");
		 //添加资产
		 jdbc.execute(sql);
		 
	     for (Map<String, Object> map2 : addList) {
		   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
		   
		   jsonObject.remove("operation");
		   jsonObject.remove("rn");
		   jsonObject.element("fcreateman", "");
		   jsonObject.element("fid", JDUuid.createID("efb453t7"));
		   jsonObject.element("fassetallotid", map.get("fid"));
		   jsonObject.element("fcreatedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()) );//创建时间
		   jsonObject.element("fmodifier", "");
		   jsonObject.element("fmodifieddate", "");
		   String  addsql = MapUtil.getSQLByJson(jsonObject, "T_ASSETALLOT_DETAIL");
		   jdbc.execute(addsql);
	  } 
		 
	 }
	      JSONObject json =new JSONObject();
	      json.accumulate("success", "success");
	     //提示
	      ActionUtil.getResponse().getWriter().write(json.toString());
	   
	}
	
	
	/**
	 * 调拨删除
	 * @throws IOException 
	 */
	public void deleteAllot() throws IOException{
		
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		
		HttpServletRequest request = ActionUtil.getRequest();
		
		String fid = request.getParameter("fid");
		 
		String sql ="delete  from  T_ASSETALLOT WHERE fid= '"+fid+"' ";	 
			
		String  logsql ="delete  from  T_ASSETALLOT_DETAIL WHERE fassetallotid= '"+fid+"' ";
		//先删除子类记录
		jdbc.execute(logsql);
		jdbc.execute(sql);
		
		JSONObject json =new JSONObject();
	    
	    json.accumulate("success", "success");
	     
	      //提示
	    ActionUtil.getResponse().getWriter().write(json.toString());
	}
	
	
}
