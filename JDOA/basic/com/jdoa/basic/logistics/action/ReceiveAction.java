package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.sql.ResultSet;
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

public class ReceiveAction{

	/**
	 * 资产领用列表
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  receiveList() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		String ftitle = request.getParameter("ftitle");
		String fsponsor =request.getParameter("fsponsor");
		
		String sql = "select * from T_ASSETRECEIVE  WHERE　1=1 ";
		if(ftitle!=null && ftitle.length()>0){
			sql+=" and ftitle like '%"+ftitle+"%' ";
		}
		
		if(fsponsor!=null && fsponsor.length()>0){
			sql+=" and fsponsor like '%"+fsponsor+"%' ";
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
	 * 领用详情
	 * @throws IOException 
	 */
	public void ReceiveDetails() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String fid = request.getParameter("fid");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit= Integer.parseInt(request.getParameter("limit"));
		String sql = "select * from T_ASSETRECEIVE_DETAIL WHERE freceiveid =  '"+fid+"' ";
		String datastr = null;
		try {
		  datastr = TableUtil.getTabStr(sql, start, limit);
		} catch (SQLException e) {
		  e.printStackTrace();
		}
		  ActionUtil.getResponse().getWriter().write(datastr);
		
	}
	
	/**
	 * 领用资产查询
	 * @throws IOException 
	 *//*
	public void queryReceive() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String fid = request.getParameter("fid");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit= Integer.parseInt(request.getParameter("limit"));
		String fassetname = request.getParameter("fassetname");
		String fsepcification =request.getParameter("fsepcification");
		
		String sql = "select * from T_ASSETWAREHOUSE WHERE 1=1 ";
		
		if(!fassetname.equals("")){
			sql+=" and fassetname like '%"+fassetname+"%' ";
		}
		if(!fsepcification.equals("")){
			sql+=" and fsepcification like '%"+fsepcification+"%' ";
		}
		
		List<Map<String, Object>> list =null;
		try {
			list= TableUtil.getTabList(sql, start, limit);
			
			for (int i = 0; i < list.size()-1; i++) {
				
				Integer inum = Integer.valueOf((String) list.get(i).get("fnumber"));
				for (int j = list.size()-1; j > i; j--) {
					if(list.get(j).get("fassetname").equals(list.get(i).get("fassetname"))){
					Integer jnum = Integer.valueOf((String) list.get(j).get("fnumber"));
					inum+=jnum;
					list.get(i).put("fnumber",inum);
					list.remove(j);
					}
				}
			}
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		
		JSONObject jsonObject =new JSONObject();
		jsonObject.accumulate("total", list.size());
		jsonObject.accumulate("rows", list);
		
		ActionUtil.getResponse().getWriter().write(jsonObject.toString());
		
	}*/
	/**
	 * 领用资产添加或者修改
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void addOrEditReceive() throws IOException, SQLException{
		 JSONObject json =new JSONObject();
		 JDBCUtil jdbc =DataUtil.getJdbcUtil();
		 HttpServletRequest request = ActionUtil.getRequest();
		 HttpSession session = request.getSession();
		 TSysUser user = (TSysUser) session.getAttribute("user");
		 Person person =user.getPerson();
		 String operation =request.getParameter("operation");
		 Map<String, Object> map =new  HashMap<String, Object>();
		 map.put("fid", request.getParameter("fid"));
	     map.put("ftitle", request.getParameter("ftitle"));
	     map.put("fsponsor", request.getParameter("fsponsor"));
		 map.put("fdepartmentapprove", request.getParameter("fdepartmentapprove")); 
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
			 
		 String upsql = MapUtil.getUpdateSql((HashMap) map, "T_ASSETRECEIVE");
		 
		 //修改资产
		 jdbc.execute(upsql);
		 
		 if(addList.size()>0){
			 
			 for (Map<String, Object> map2 : addList) {
				 
				   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
				   
				   String fnumber = (String) jsonObject.get("fnumber");
				   if(fnumber.equals("")){
					   fnumber="1"; 
				   }
				   //资产库对应的资产
				   String selsql = "select * from  T_ASSETWAREHOUSE  where fassetsn = '"+jsonObject.get("fassetsn") +"' "; 
				   ResultSet set = jdbc.executeQuery(selsql);
				   String zcnum="";
				   while (set.next()) {
					   zcnum= set.getString("fnumber");
				   }
				   if(zcnum.equals("")){
					   json.accumulate("fail", "请输入正确的资产序号");
					   ActionUtil.getResponse().getWriter().write(json.toString());
					   return;
				   }
				   Integer synum = Integer.valueOf(zcnum)-Integer.valueOf(fnumber);
				   
				   if(synum<0){
					   json.accumulate("fail", "领用数量超过资产库存啦");
					   ActionUtil.getResponse().getWriter().write(json.toString());
					   return;
				   }
				   jsonObject.remove("operation");
				   jsonObject.remove("rn");
				   jsonObject.element("fcreateman", person.getFname());
				   jsonObject.element("fid", JDUuid.createID("efb453t7"));
				   jsonObject.element("freceiveid", map.get("fid"));
				   jsonObject.element("fuserid", person.getFid());
				   jsonObject.element("fcreatedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()) );//创建时间
				   jsonObject.element("fmodifier", "");
				   jsonObject.element("fmodifieddate", "");
				   
				   String  addsql = MapUtil.getSQLByJson(jsonObject, "T_ASSETRECEIVE_DETAIL");
				   //更新仓库 T_ASSETWAREHOUSE
				   String  updatasql =" UPDATE T_ASSETWAREHOUSE SET fnumber = '"+synum+"' "+" WHERE fassetsn = '"+jsonObject.get("fassetsn") +"' ";
				   
				   jdbc.execute(addsql);

				   jdbc.execute(updatasql);
			  } 
		 }
		 
		 if(updateList.size()>0){
		     for (Map<String, Object> map2 : updateList) {
			   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
			   jsonObject.remove("operation");
			   jsonObject.remove("rn");
			   //传过来的值
			   String fnumber = (String) jsonObject.get("fnumber");
			   if(fnumber.equals("")){
				   fnumber="1"; 
			   }
			   //查询的值
			   String quersql = "select * from  T_ASSETRECEIVE_DETAIL  where fassetsn = '"+jsonObject.get("fassetsn") +"' "; 
			   ResultSet er = jdbc.executeQuery(quersql);
			   String quernum="";
			   while (er.next()) {
				   quernum= er.getString("fnumber");
			   }
			   //仓库的值
			   String selsql = "select * from  T_ASSETWAREHOUSE  where fassetsn = '"+jsonObject.get("fassetsn") +"' "; 
			   ResultSet set = jdbc.executeQuery(selsql);
			   Integer zcnum=0;
			   while (set.next()) {
				   zcnum= Integer.valueOf(set.getString("fnumber"));
			   }
			   if(Integer.valueOf(fnumber)>zcnum){
				   json.accumulate("fail", "领用数量超过资产库存啦");
				   ActionUtil.getResponse().getWriter().write(json.toString());
				   return;
			   }
			   
			   Integer differnum = 0; 
			   //传过来的值 大于查询的值 仓库减
			   if( Integer.valueOf(fnumber)>Integer.valueOf(quernum)){
				   
				   differnum =Integer.valueOf(fnumber)-Integer.valueOf(quernum);
				   zcnum = Integer.valueOf(zcnum)-differnum;
			   }
			   //反之 仓库加
			   if(Integer.valueOf(fnumber)<Integer.valueOf(quernum)){
				   
				   differnum =Integer.valueOf(quernum)-Integer.valueOf(fnumber);
				   zcnum = Integer.valueOf(zcnum)+differnum;
			   }
			   
			   
			   //更新仓库 T_ASSETWAREHOUSE
			   String  updatacksql =" UPDATE T_ASSETWAREHOUSE SET fnumber = '"+zcnum+"' "+" WHERE fassetsn = '"+jsonObject.get("fassetsn") +"' ";
			   
			   String  updatasql = MapUtil.getUpdateByJson(jsonObject, "T_ASSETRECEIVE_DETAIL");
			   jdbc.execute(updatasql);
			   jdbc.execute(updatacksql);
		     }
		}
		 
		 if(delList.size()>0){
		     for (Map<String, Object> map2 : delList) {
			   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
			   String fid = (String) jsonObject.get("fid");
			   
			   String fnumber = (String) jsonObject.get("fnumber");
			   //仓库的值
			   String selsql = "select * from  T_ASSETWAREHOUSE  where fassetsn = '"+jsonObject.get("fassetsn") +"' "; 
			   ResultSet set = jdbc.executeQuery(selsql);
			   Integer zcnum=0;
			   while (set.next()) {
				   zcnum= Integer.valueOf(set.getString("fnumber"));
			   }
			   zcnum+=Integer.valueOf(fnumber);
			   
			   //更新仓库 T_ASSETWAREHOUSE
			   String  updatacksql =" UPDATE T_ASSETWAREHOUSE SET fnumber = '"+zcnum+"' "+" WHERE fassetsn = '"+jsonObject.get("fassetsn") +"' ";
			   String sql = "delete from T_ASSETRECEIVE_DETAIL where fid=  '"+fid+"' ";
			   
			   jdbc.execute(sql);
			   jdbc.execute(updatacksql);
			   
		     }
		} 
		 
	 }else if(operation.equals("add")){
		
		 map.put("fid", JDUuid.createID("dsaweqvf"));
		 map.put("faudit", "0");
		 map.put("fapplicant", person.getFname());
		 map.put("fdepartment", person.getFssbm());
		 map.put("fapplydate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		 String sql = MapUtil.getSQL((HashMap) map, "T_ASSETRECEIVE");
		 //添加资产
		 jdbc.execute(sql);
		   
		 if(addList.size()>0){
	     for (Map<String, Object> map2 : addList) {
		   JSONObject jsonObject = JSONObject.fromObject(map2.toString()); 
		   String fnumber = (String) jsonObject.get("fnumber");
		   if(fnumber.equals("")){
			   fnumber="1"; 
		   }
		   //资产库对应的资产
		   String selsql = "select * from  T_ASSETWAREHOUSE  where fassetsn = '"+jsonObject.get("fassetsn") +"' "; 
		   ResultSet set = jdbc.executeQuery(selsql);
		   String zcnum="";
		   while (set.next()) {
			   zcnum= set.getString("fnumber");
		   }
		   if(zcnum.equals("")){
			   json.accumulate("fail", "请输入正确的资产序号");
			   ActionUtil.getResponse().getWriter().write(json.toString());
			   return;
		   }
		   Integer synum = Integer.valueOf(zcnum)-Integer.valueOf(fnumber);
		   
		   if(synum<0){
			   json.accumulate("fail", "领用数量超过资产库存啦");
			   ActionUtil.getResponse().getWriter().write(json.toString());
			   return;
		   }
		   
		   jsonObject.remove("operation");
		   jsonObject.remove("rn");
		   jsonObject.element("fcreateman", person.getFname());
		   jsonObject.element("fid", JDUuid.createID("efb453t7"));
		   jsonObject.element("freceiveid", map.get("fid"));
		   jsonObject.element("fuserid", person.getFid());
		   jsonObject.element("fcreatedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()) );//创建时间
		   jsonObject.element("fmodifier", "");
		   jsonObject.element("fmodifieddate", "");
		   
		   String  addsql = MapUtil.getSQLByJson(jsonObject, "T_ASSETRECEIVE_DETAIL");
		   
		   //更新仓库 T_ASSETWAREHOUSE
		   String  updatasql =" UPDATE T_ASSETWAREHOUSE SET fnumber = '"+synum+"' "+" WHERE fassetsn = '"+jsonObject.get("fassetsn") +"' ";
		   
		   
		   jdbc.execute(addsql);
		   
		   jdbc.execute(updatasql);
		  
	   } 
      }
		
	 }
	     //提示
	      json.accumulate("success", "success");
		  ActionUtil.getResponse().getWriter().write(json.toString());
	   
	}
	
	
	/**
	 * 删除领用
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void deleteReceive() throws IOException, SQLException{
		
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		
		HttpServletRequest request = ActionUtil.getRequest();
		
		String fid = request.getParameter("fid");
		 
		String sql ="delete  from  T_ASSETRECEIVE WHERE fid= '"+fid+"' ";	 
		
		String  selquerysql = "select * from  T_ASSETRECEIVE_DETAIL WHERE freceiveid= '"+fid+"' ";
		
		ResultSet set = jdbc.executeQuery(selquerysql);
		String num="";
		String fassetsn="";
		   while (set.next()) {
			   
			   fassetsn=set.getString("fassetsn");
			   num= set.getString("fnumber");
			   
			   //仓库的值
			   String selsql = "select * from  T_ASSETWAREHOUSE  where fassetsn = '"+fassetsn +"' "; 
			   ResultSet r = jdbc.executeQuery(selsql);
			   Integer zcnum=0;
			   while (r.next()) {
				   zcnum= Integer.valueOf(r.getString("fnumber"));
			   }
			   zcnum+=Integer.valueOf(num);
			   //更新仓库 T_ASSETWAREHOUSE
			   String  updatacksql =" UPDATE T_ASSETWAREHOUSE SET fnumber = '"+zcnum+"' "+" WHERE fassetsn = '"+fassetsn +"' ";
			  
			   //删除
			   String  delsql ="delete  from  T_ASSETRECEIVE_DETAIL WHERE fassetsn= '"+fassetsn+"' ";
			  
			   jdbc.execute(delsql);
			   jdbc.execute(updatacksql);
		   }
		
		//先删除子类记录
		   
		jdbc.execute(sql);
		JSONObject json =new JSONObject();
	    json.accumulate("success", "success");
	      //提示
	    ActionUtil.getResponse().getWriter().write(json.toString());
	}
	
	
}
