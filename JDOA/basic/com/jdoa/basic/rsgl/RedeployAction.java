package com.jdoa.basic.rsgl;

import java.io.IOException;
import java.sql.ResultSet;
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
 * 人员调动
 * @author Administrator
 *
 */
public class RedeployAction {

	
	/**
	 *人员调动列表
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  RedeployList() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		String fusername = request.getParameter("fusername");
		String fbmid =request.getParameter("fbmid");
		
		String sql = "select * from T_REDEPLOY WHERE 1=1  ";
		if(fusername!=null && fusername.length()>0){
			sql+=" and　fusername like '%"+fusername+"%' ";
		}
		if(!fbmid.equals("请选择")){
			
			sql+=" and  fbmid like '%"+fbmid+"%' ";
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
	 *人员调动添加或者修改
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("null")
	public  void  addOrEditRedeploy() throws IOException, SQLException{
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		Person person =user.getPerson();
		String operation =request.getParameter("operation");
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		Map<String, Object> map =new  HashMap<String, Object>();
		map.put("fid", request.getParameter("fid"));
	    map.put("fsex", request.getParameter("fsex"));
		map.put("fusername", request.getParameter("fusername")); 
		map.put("fnowposition", request.getParameter("fnowposition"));
		map.put("fexpression", request.getParameter("fexpression"));
		map.put("ftransfer", request.getParameter("ftransfer"));
		map.put("ftransfercause", request.getParameter("ftransfercause"));
		map.put("fbmid", request.getParameter("fbmid"));
		
		
		if(operation.endsWith("add")){
			//添加
			map.put("fid", JDUuid.createID("79safdwt"));
			map.put("faudit", "0");
			map.put("fapplicant", person.getFname());
			map.put("fdepartment",person.getFssbm()); 
			//部门ID
			String bmsql="select fid,fname from T_ORG where fid not in(select fparentid from T_ORG) ";
			
			String bmid = (String) map.get("fbmid");
			
			ResultSet rs=jdbc.executeQuery(bmsql);
			while(rs.next()){
				String fid=rs.getString("fid");
				String fname=rs.getString("fname");
				if(bmid.equals(fid)){
				 map.put("fbmmc", fname);	
				}
			}
			map.put("fapplydate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		   String sql = MapUtil.getSQL((HashMap) map, "T_REDEPLOY");
		   jdbc.execute(sql);
		}else{
			//修改
		   String sql = MapUtil.getUpdateSql((HashMap) map, "T_REDEPLOY");
		   jdbc.execute(sql);
		}
		
		 ActionUtil.getResponse().getWriter().write("success");
	}
	

	/**
	 *人员调动删除
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  deleteRedeploy() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		String fid =request.getParameter("fid");
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		
        String sql = " delete T_REDEPLOY  WHERE　fid = '"+fid+"' ";
        jdbc.execute(sql);
        
		ActionUtil.getResponse().getWriter().write("success");
	}
}
