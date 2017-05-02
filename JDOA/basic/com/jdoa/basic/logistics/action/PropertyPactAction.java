package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.TableUtil;

/**
 * 资产采购合同
 * @author Administrator
 *
 */
public class PropertyPactAction {

	
	/**
	 *采购合同列表
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  propertyPactList() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		String fpactname = request.getParameter("fpactname");
		String fpactnum =request.getParameter("fpactnum");
		String fsuppliername =request.getParameter("fsuppliername");
		
		String sql = "select * from T_PROPERTYPACT WHERE 1=1  ";
		if(fpactname!=null && fpactname.length()>0){
			sql+=" and　fpactname like '%"+fpactname+"%' ";
		}
		if(fpactnum!=null && fpactnum.length()>0){
			
			sql+=" and  fpactnum like '%"+fpactnum+"%' ";
		}
		if(fsuppliername!=null && fsuppliername.length()>0){
			sql+=" and  fsuppliername like '%"+fsuppliername+"%' ";
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
	 *采购合同添加或者修改
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  addOrEditpact() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String operation =request.getParameter("operation");
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		Map<String, Object> map =new  HashMap<String, Object>();
		map.put("fid", request.getParameter("fid"));
	    map.put("fdemandername", request.getParameter("fdemandername"));
		map.put("fsuppliername", request.getParameter("fsuppliername")); 
		map.put("fdemandersite", request.getParameter("fdemandersite"));
		map.put("fdemanderfaxes", request.getParameter("fdemanderfaxes"));
		map.put("fdemanderphone", request.getParameter("fdemanderphone"));
		map.put("fdemanderagent", request.getParameter("fdemanderagent"));
		map.put("fdemanderbankdoor", request.getParameter("fdemanderbankdoor"));
		map.put("fdemanderaccount", request.getParameter("fdemanderaccount"));
		map.put("fdemanderdutynum", request.getParameter("fdemanderdutynum"));
		map.put("fdemandersign", request.getParameter("fdemandersign"));
		map.put("fsuppliersite", request.getParameter("fsuppliersite")); 
		map.put("fsupplierfaxes", request.getParameter("fsupplierfaxes"));
		map.put("fsupplierphone", request.getParameter("fsupplierphone"));
		map.put("fsupplieragent", request.getParameter("fsupplieragent"));
		map.put("fsupplierbankdoor", request.getParameter("fsupplierbankdoor"));
		map.put("fsupplieraccount", request.getParameter("fsupplieraccount"));
		map.put("fsupplierdutynum", request.getParameter("fsupplierdutynum"));
		map.put("fsuppliersign", request.getParameter("fsuppliersign"));
		map.put("fcontracttime", request.getParameter("fcontracttime"));
		map.put("fcontractsite", request.getParameter("fcontractsite"));
		map.put("fpactnum", request.getParameter("fpactnum"));
		map.put("fpactname", request.getParameter("fpactname"));
		
		if(operation.endsWith("add")){
			//添加
			map.put("fid", JDUuid.createID("asdfghjk"));
			map.put("faudit", "0");
		   String sql = MapUtil.getSQL((HashMap) map, "T_PROPERTYPACT");
		   jdbc.execute(sql);
		}else{
			//修改
		   String sql = MapUtil.getUpdateSql((HashMap) map, "T_PROPERTYPACT");
		   jdbc.execute(sql);
		}
		
		 ActionUtil.getResponse().getWriter().write("success");
	}
	

	/**
	 *采购合同删除
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  deletePact() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String fid =request.getParameter("fid");
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		
        String sql = " delete T_PROPERTYPACT  WHERE　fid = '"+fid+"' ";
        jdbc.execute(sql);
        
		ActionUtil.getResponse().getWriter().write("success");
	}
}
