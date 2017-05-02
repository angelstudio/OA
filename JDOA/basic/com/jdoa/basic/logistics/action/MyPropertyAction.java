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

public class MyPropertyAction{

	/**
	 * 我的资产
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  myProperty() throws IOException{
		
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		Person person =user.getPerson();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		String fassetsn = request.getParameter("fassetsn");
		String fassetname =request.getParameter("fassetname");
		
		String sql = "select * from T_ASSETRECEIVE_DETAIL WHERE　fuserid= '" +person.getFid()+"' ";
		if(fassetsn != null && fassetsn.length()>0){
			sql+=" and fassetsn like '%"+fassetsn+"%' ";
		}
		
		if(fassetname != null && fassetname.length()>0){
			sql+=" and fassetname like '%"+fassetname+"%' ";
		}
		
		String datastr = null;
		try {
		  datastr = TableUtil.getTabStr(sql, start, limit);
		} catch (SQLException e) {
		  e.printStackTrace();
		}
		  ActionUtil.getResponse().getWriter().write(datastr);
	}
	
	
	
}
