package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.basic.person.model.Person;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.TableUtil;

/**
 * 固定资产仓库
 * @author Administrator
 *
 */
public class WarehouseAction {

	
	/**
	 *仓库列表
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  warehouseList() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		String fassetname = request.getParameter("fassetname");
		String fuseunit =request.getParameter("fuseunit");
		
		String sql = "select * from T_ASSETWAREHOUSE WHERE 1=1  ";
		if(fuseunit!=null && fuseunit.length()>0){
			sql+=" and　fuseunit like '%"+fuseunit+"%' ";
		}
		if(fassetname!=null && fassetname.length()>0){
			
			sql+=" and  fassetname like '%"+fassetname+"%' ";
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
	 *仓库添加或者修改
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  addOrEditwarehouse() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String operation =request.getParameter("operation");
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		Person person =user.getPerson();
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		Map<String, Object> map =new  HashMap<String, Object>();
		map.put("fid", request.getParameter("fid"));
	    map.put("fassetsn", request.getParameter("fassetsn"));
		map.put("fassetname", request.getParameter("fassetname")); 
		map.put("fsepcification", request.getParameter("fsepcification"));
		map.put("fmaker", request.getParameter("fmaker"));
		map.put("fuseunit", request.getParameter("fuseunit"));
		map.put("fnotes", request.getParameter("fnotes"));
		map.put("ftype", request.getParameter("ftype"));
		map.put("fassetstate", request.getParameter("fassetstate"));
		map.put("funitprice", request.getParameter("funitprice")); 
		map.put("fnumber", request.getParameter("fnumber")); 
		
		if(operation.endsWith("add")){
			//添加
			map.put("fid", JDUuid.createID("asdfghjk"));
			map.put("faudit", "0");
			map.put("fcreatedate", new SimpleDateFormat("yyyy-MM-dd").format(new Date())  );
			map.put("fdepartment", person.getFssbm());
			map.put("fapplicant", person.getFname());
			
		   String sql = MapUtil.getSQL((HashMap) map, "T_ASSETWAREHOUSE");
		   jdbc.execute(sql);
		}else{
			//修改
		   String sql = MapUtil.getUpdateSql((HashMap) map, "T_ASSETWAREHOUSE");
		   jdbc.execute(sql);
		}
		
		 ActionUtil.getResponse().getWriter().write("success");
	}
	

	/**
	 *仓库删除
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  deleteWarehouse() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String fid =request.getParameter("fid");
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		
        String sql = " delete T_ASSETWAREHOUSE  WHERE　fid = '"+fid+"' ";
        jdbc.execute(sql);
        
		ActionUtil.getResponse().getWriter().write("success");
	}
}
