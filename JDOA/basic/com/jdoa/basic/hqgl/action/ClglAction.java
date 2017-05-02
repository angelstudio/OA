package com.jdoa.basic.hqgl.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.TableUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ClglAction{
	
	public void getClglList(){
		HttpServletRequest request = ActionUtil.getRequest();
		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));
		String sql="select * from T_HQGL_YCGL ";
		String tab=null;
		try {
			tab=TableUtil.getTabStr(sql,start,limit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ActionUtil.getResponse().getWriter().write(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveClglxx(){
		HttpServletRequest request = ActionUtil.getRequest();
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		String oper=request.getParameter("oper");
		String licence=request.getParameter("licence");
		String brand=request.getParameter("brand");
		String specification=request.getParameter("specification");
		String photo=request.getParameter("photo");
		String arctic=request.getParameter("arctic");
		String info=request.getParameter("info");
		String person=request.getParameter("person");
		String notes=request.getParameter("notes");
		String serialnumber = request.getParameter("serialnumber");
		String fid = request.getParameter("fid");
		if("1".equals(oper)){//新增
			String id = JDUuid.createID("22222222");
			Date nowDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sdate = sdf.format(nowDate);
			HashMap map=new HashMap();
			map.put("fid", id);
			map.put("licence", licence);
			map.put("brand", brand);
			map.put("specification", specification);
			map.put("photo", photo);
			map.put("arctic", arctic);
			map.put("info", info);
			map.put("person", person);
			map.put("serialnumber", serialnumber);
			map.put("info", info);
			map.put("notes", notes);
			map.put("createdate", sdate);
			String sql=MapUtil.getSQL(map, "T_HQGL_YCGL");
			jdbc.execute(sql);
		}else{//修改
			HashMap map=new HashMap();
			map.put("fid", fid);
			map.put("licence", licence);
			map.put("brand", brand);
			map.put("specification", specification);
			map.put("photo", photo);
			map.put("arctic", arctic);
			map.put("info", info);
			map.put("person", person);
			map.put("serialnumber", serialnumber);
			map.put("info", info);
			map.put("notes", notes);
			String sql = MapUtil.getUpdateSql(map, "T_HQGL_YCGL");
			jdbc.execute(sql);
		}
	}
	
	public void deleteClglxx(){
		HttpServletRequest request = ActionUtil.getRequest();
		String id = request.getParameter("id");
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		String sql="delete from T_HQGL_YCGL where id = '"+id+"'";
	    jdbc.execute(sql);
	}
}
