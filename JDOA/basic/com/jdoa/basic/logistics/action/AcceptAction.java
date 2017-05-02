	package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * 资产验收
 * @author Administrator
 *
 */
public class AcceptAction {

	
	/**
	 *验收列表
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  acceptList() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		String ftitle =request.getParameter("ftitle");
		String fapplicant =request.getParameter("fapplicant");
		
		String sql = "select * from T_ASSETSACCEPTANCE WHERE 1=1 ";
		
		if(ftitle != null && ftitle.length()>0){
			sql+=" and ftitle like '%"+ftitle+"%' ";
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
	 *验收添加或者修改
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("null")
	public  void  addOrEditAccept() throws IOException, SQLException{
		HttpServletRequest request = ActionUtil.getRequest();
		String operation =request.getParameter("operation");
		String fpurchaseid =request.getParameter("fpurchaseid");
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		Person person =user.getPerson();
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		Map<String, Object> map =new  HashMap<String, Object>();
		map.put("fid", request.getParameter("fid"));
		map.put("fpurchaseid",fpurchaseid);
	    map.put("ftitle", request.getParameter("ftitle"));
		map.put("fuseunit", request.getParameter("fuseunit")); 
		map.put("ftype", request.getParameter("ftype"));
		map.put("fapprover", request.getParameter("fapprover"));
		map.put("fapproveldate", request.getParameter("fapproveldate")); 
		
		if(operation.endsWith("add")){
			//添加
			map.put("fid", JDUuid.createID("asdfghj4"));
			map.put("faudit", "0");
			map.put("fapplicant", person.getFname());
			map.put("fdepartment", person.getFssbm());
			map.put("fapplydate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		    String sql = MapUtil.getSQL((HashMap) map, "T_ASSETSACCEPTANCE");
		    jdbc.execute(sql);
		   
		    //暂时往仓库添加数据
		    String querysql = "select  *  from t_purchase_detail  where fpurchaseid = '"+fpurchaseid+"'";
		    ResultSet resultSet = jdbc.executeQuery(querysql);
		    
		    List<Map<String,Object>> datas =new ArrayList<Map<String,Object>>();
		    
		    while (resultSet.next()) {
		    	Map<String,Object> datamap = new HashMap<String, Object>(); 
		    	datamap.put("fid", JDUuid.createID("asdfgh84"));
		    	datamap.put("fassetsn", resultSet.getString("fassetsn"));
		    	datamap.put("fassetname", resultSet.getString("fassetname"));
		    	datamap.put("fsepcification", resultSet.getString("fsepcification"));
		    	datamap.put("fmaker", resultSet.getString("fmaker"));
		    	datamap.put("funitprice", resultSet.getString("funitprice"));
		    	datamap.put("fnumber", resultSet.getString("fnumber"));
		    	datamap.put("fnotes", resultSet.getString("fnotes"));
		    	datamap.put("fapplicant", resultSet.getString("fcreateman")); 
		    	datamap.put("ftype", map.get("ftype"));
		    	datamap.put("faudit", "0");
		    	datamap.put("fcreatedate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		    	datamap.put("fdepartment",  person.getFssbm());
		    	datamap.put("fapplicant",  person.getFname());
		    	datas.add(datamap);
			}
		    
		     for (Map<String, Object> map2 : datas) {
		    	String addsql =  MapUtil.getSQL((HashMap) map2, "T_ASSETWAREHOUSE");
		    	jdbc.execute(addsql);
			}
		   
		}else{
			//修改
		   String sql = MapUtil.getUpdateSql((HashMap) map, "T_ASSETSACCEPTANCE");
		   jdbc.execute(sql);
		}
		
		 ActionUtil.getResponse().getWriter().write("success");
	}
	

	/**
	 *验收删除
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  deleteAccept() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String fid =request.getParameter("fid");
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		
        String sql = " delete T_ASSETSACCEPTANCE  WHERE　fid = '"+fid+"' ";
        jdbc.execute(sql);
        
		ActionUtil.getResponse().getWriter().write("success");
	}
	
	/**
	 *查询已审核的验收单
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  qureypurchase() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String fid =request.getParameter("fid");
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		
        String sql = "select * from T_PURCHASE WHERE faudit=2 ";
		
        
        List<Map<String, String>> listdata =new ArrayList<Map<String,String>>();
		try {
			
			ResultSet rs = jdbc.executeQuery(sql);
			while (rs.next()) {
				Map<String, String> datamap = new HashMap<String, String>(); 
				datamap.put("fpurchaseid", rs.getString("fid"));
				datamap.put("ftitle", rs.getString("ftitle"));
				listdata.add(datamap);
			}
			
		} catch (SQLException e) {
		  e.printStackTrace();
		}
		
		 JSONObject json =new JSONObject();
	     json.accumulate("success", "success");
	     json.accumulate("listdata", listdata);
	     //提示
	     ActionUtil.getResponse().getWriter().write(json.toString());
	}
	
	/**
	 *查询单个验收信息 
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  qureyonepurchase() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		
		String fid =request.getParameter("fid");
		String sql = "select * from T_PURCHASE WHERE fid= '"+fid+"' ";
		
		String datastr = null;
		try {
		  datastr = TableUtil.getTabStr(sql, start, limit);
		} catch (SQLException e) {
		  e.printStackTrace();
		}
		  ActionUtil.getResponse().getWriter().write(datastr);
	}
}
