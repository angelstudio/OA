package com.jdoa.basic.login.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;

public class MainAction {
	/**
	 * @author Action
	 * @describe 根据用户名获取用户主页
	 * @date2014-11-7
	 * 
	 */
	public void getMainUrl() {
		HttpSession session = ActionUtil.getRequest().getSession();
		JSONObject json = new JSONObject();
		String userId = session.getAttribute("fuserId").toString();
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		String sql = "select fmainUrl from t_sys_user where fid='" + userId
				+ "'";
		try {
			ResultSet rs = jdbc.executeQuery(sql);
			String fmainUrl = "";
			while (rs.next()) {
				fmainUrl = rs.getString("fmainUrl");
			}
			json.put("fmainUrl", fmainUrl);
			ActionUtil.getResponse().getWriter().write(json.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
