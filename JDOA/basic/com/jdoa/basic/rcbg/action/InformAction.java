package com.jdoa.basic.rcbg.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;

/**
 * 通知公告模块
 * @author ningjianguo
 *
 */
public class InformAction {
	
	/**
	 * 添加通知公告
	 */
	public void addInform(){
		HttpServletRequest request = ActionUtil.getRequest();
	    JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
	    HttpSession session = request.getSession();
	    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		TSysUser user = (TSysUser) session.getAttribute("user");
	    String ftitle = request.getParameter("ftitle");
	    String fbody = request.getParameter("fbody");
	    String fid = JDUuid.createID("qwe123ui");
	    HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("fid", fid);
		map.put("ftitle", ftitle);
		map.put("fbody", fbody);
		map.put("fcreate_person", user.getPerson().getFname());
		map.put("fcreate_date", sf.format(new Date()));
		jdbcUtil.executeUpdateSql(MapUtil.getSQL(map, "t_inform"));
		try {
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除通知公告
	 */
	public void delInform(){
		HttpServletRequest request = ActionUtil.getRequest();
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fidItems = request.getParameter("fidItems");
		String[] fids = fidItems.split(",");
		ArrayList<String> delSqls = new ArrayList<String>();
		for (String fid : fids) {
			String delSql = "delete from t_inform where fid='"+fid+"'";
			delSqls.add(delSql);
		}
		jdbcUtil.executeBatch(delSqls);
		try {
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *更新通知公告 
	 */
	public void updInform(){
		HttpServletRequest request = ActionUtil.getRequest();
	    JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
	    String ftitle = request.getParameter("ftitle");
	    String fbody = request.getParameter("fbody");
	    String fid = request.getParameter("fid");
	    HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("fid", fid);
		map.put("ftitle", ftitle);
		map.put("fbody", fbody);
		jdbcUtil.executeUpdateSql(MapUtil.getUpdateSql(map, "t_inform"));
		try {
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载所有通知公告
	 */
	public void findAllInformToGrid(){
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		String findSql = "select * from t_inform where fcreate_person='"+user.getPerson().getFname()+"'";
		int start = Integer.parseInt(request.getParameter("start"));
		int limit= Integer.parseInt(request.getParameter("limit"));//每页展示行数
		String jsonString = null;
		try {
			jsonString = TableUtil.getTabStr(findSql, start,limit);
			ActionUtil.getResponse().getWriter().write(jsonString);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据条件加载通知公告
	 */
	public void findInformByArgs(){
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		String ftitle = request.getParameter("ftitle");
		String date1 = request.getParameter("date1");
		String date2 = request.getParameter("date2");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit= Integer.parseInt(request.getParameter("limit"));//每页展示行数
		String findSql = "select * from t_inform where fcreate_person='"+user.getPerson().getFname()+"'";
		if(!StringUtil.isEmpty(ftitle)){
			findSql +=" and ftitle like '%"+ftitle+"%'";
		}
		if(!StringUtil.isEmpty(date1)&&!StringUtil.isEmpty(date2)){
			findSql +=" and to_date('"+date1+"','yyyy-MM-dd')<=to_date(fcreate_date,'yyyy-MM-dd') and to_date(fcreate_date,'yyyy-MM-dd')<=to_date('"+date2+"','yyyy-MM-dd')";
		}
		try {
			String jsonString = TableUtil.getTabStr(findSql, start,limit);
			ActionUtil.getResponse().getWriter().write(jsonString);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取通知公告详情
	 */
	public void findOneInformDetail(){
		HttpServletRequest request = ActionUtil.getRequest();
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fid = request.getParameter("fid");
		String findSql = "select * from t_inform where fid='"+fid+"'";
		ResultSet rs = jdbcUtil.executeQuery(findSql);
		List<Object> lists = null;
		try {
			while(rs.next()){
				lists = new ArrayList<Object>();
				for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
					Object temp = rs.getObject(i) == null ? "": rs.getObject(i);
					lists.add(temp);
				}
			}
			ActionUtil.getResponse().getWriter().write(lists.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
