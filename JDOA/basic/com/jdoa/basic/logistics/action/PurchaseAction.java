package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

/**
 * 资产采购
 * @author Administrator
 *
 */
public class PurchaseAction {

	
	/**
	 *采购列表
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  purchaseList() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		String fsn = request.getParameter("fsn");
		String ftitle =request.getParameter("ftitle");
		String fapplicant =request.getParameter("fapplicant");
		
		String sql = "select * from T_PURCHASE WHERE 1=1 ";
		
		if(ftitle!=null && ftitle.length()>0){
			sql+=" and ftitle like '%"+ftitle+"%' ";
		}
		if(fsn!=null && fsn.length()>0){
			sql+=" and fsn like '%"+fsn+"%' ";
		}
		if(fapplicant!=null && fapplicant.length()>0){
			sql+=" and fapplicant like '%"+fapplicant+"%' ";
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
	 *采购添加或者修改
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  addOrEditPurchase() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String operation =request.getParameter("operation");
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		Person person =user.getPerson();
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		Map<String, Object> map =new  HashMap<String, Object>();
		map.put("fid", request.getParameter("fid"));
	    map.put("ftitle", request.getParameter("ftitle"));
		map.put("funit", request.getParameter("funit")); 
		map.put("fsn", request.getParameter("fsn"));
		map.put("fspend", request.getParameter("fspend"));
		
		String formData = ActionUtil.getRequest().getParameter("formData"); 

	    Map<String,Object> datamap = (Map<String,Object>) JSONObject.fromObject(formData);
	     
		 //更新数据  
	    List<Map<String,Object>> updateList = (List<Map<String,Object>>)datamap.get("update"); 
	     
	     //更新添加数据
	    List<Map<String,Object>> addList = (List<Map<String,Object>>)datamap.get("insert");
	    
	    //删除数据
	    List<Map<String,Object>> delList = (List<Map<String,Object>>)datamap.get("delete"); 
	    
		if(operation.equals("add")){
			//添加
			map.put("fapplydate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			map.put("fid", JDUuid.createID("asdfghj4"));
			map.put("faudit", "0");
			map.put("fapplicant", person.getFname());
			map.put("fdepartment", person.getFssbm());
		    String sql = MapUtil.getSQL((HashMap) map, "T_PURCHASE");
		    jdbc.execute(sql);
		    
		    for (Map<String, Object> map2 : addList) {
				   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
				   jsonObject.remove("operation");
				   jsonObject.remove("rn");
				   jsonObject.element("fid", JDUuid.createID("efb453t7"));
				   jsonObject.element("fcreateman", person.getFname());
				   jsonObject.element("FPURCHASEID", map.get("fid"));
				   jsonObject.element("fcreatedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()) );//创建时间
				   jsonObject.element("fmodifier", "");
				   jsonObject.element("fmodifieddate", "");
				   String  updatasql = MapUtil.getSQLByJson(jsonObject, "T_PURCHASE_DETAIL");
				   jdbc.execute(updatasql);
			  }
		}else{
			//修改
		   String sql = MapUtil.getUpdateSql((HashMap) map, "T_PURCHASE");
		   jdbc.execute(sql);
		   
		   if(updateList.size()>0){
			     for (Map<String, Object> map2 : updateList) {
				   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
				   jsonObject.remove("operation");
				   jsonObject.remove("rn");
				   String  updatasql = MapUtil.getUpdateByJson(jsonObject, "T_PURCHASE_DETAIL");
				   jdbc.execute(updatasql);
			     }
			}
		   
		   if(delList.size()>0){
			     for (Map<String, Object> map2 : delList) {
				   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
				   String fid = (String) jsonObject.get("fid");
				   String delsql = "delete from T_PURCHASE_DETAIL where fid=  '"+fid+"' ";
				   jdbc.execute(delsql);
			     }
			} 
		   
		   if(addList.size()>0){
				 for (Map<String, Object> map2 : addList) {
					   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
					   jsonObject.remove("operation");
					   jsonObject.remove("rn");
					   jsonObject.element("fid", JDUuid.createID("efb453t7"));
					   jsonObject.element("fpurchaseid", map.get("fid"));
					   jsonObject.element("fcreatedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()) );//创建时间
					   jsonObject.element("fmodifier", "");
					   jsonObject.element("fmodifieddate", "");
					   String  addsql = MapUtil.getSQLByJson(jsonObject, "T_PURCHASE_DETAIL");
					   jdbc.execute(addsql);
				  } 
			 } 
		   
		}
		
		  JSONObject json =new JSONObject();
	      json.accumulate("success", "success");
	     //提示
	      ActionUtil.getResponse().getWriter().write(json.toString());
	}
	
	/**
	 * 采购详情
	 * @throws IOException 
	 */
	public void purchaseDetails() throws IOException{
		
		String fid = ActionUtil.getRequest().getParameter("fid");
		int start = Integer.parseInt(ActionUtil.getRequest().getParameter("start"));
		int limit= Integer.parseInt(ActionUtil.getRequest().getParameter("limit"));
		String sql = "select * from T_PURCHASE_DETAIL WHERE FPURCHASEID =  '"+fid+"' ";
		String datastr = null;
		try {
		  datastr = TableUtil.getTabStr(sql, start, limit);
		} catch (SQLException e) {
		  e.printStackTrace();
		}
		  ActionUtil.getResponse().getWriter().write(datastr);
		
	}
	/**
	 *采购删除
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  deletePurchase() throws IOException{
        JDBCUtil jdbc = DataUtil.getJdbcUtil();
		
		HttpServletRequest request = ActionUtil.getRequest();
		
		String fid = request.getParameter("fid");
		 
		String sql ="delete  from  T_PURCHASE WHERE fid= '"+fid+"' ";	 
			
		String  logsql ="delete  from  T_PURCHASE_DETAIL WHERE FPURCHASEID= '"+fid+"' ";
		//先删除子类记录
		jdbc.execute(logsql);
		jdbc.execute(sql);
		
		JSONObject json =new JSONObject();
	    
	    json.accumulate("success", "success");
	     
	      //提示
	    ActionUtil.getResponse().getWriter().write(json.toString());
	}
}
