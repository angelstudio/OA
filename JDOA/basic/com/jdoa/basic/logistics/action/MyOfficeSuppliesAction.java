package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;

public class MyOfficeSuppliesAction {
	
	/**
	 * 
	 * 查询所有
	 * 
	 */
	public void queryAllMyOfficeSupplies() {
		HttpServletRequest request = ActionUtil.getRequest();

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));

		String sql = "select * from t_my_office_supplies order by frecipients_date,faudit_date desc";
		String tab = null;
		try {
			tab = TableUtil.getTabStr(sql, start, limit);
			if (tab != null) {
				ActionUtil.getResponse().getWriter().write(tab);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void fuzzyQueryMyOfficeSupplies() {
		HttpServletRequest request = ActionUtil.getRequest();
		try {
			request.setCharacterEncoding("utf-8");

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 办公物品名称
		String conditions = request.getParameter("conditions");

		String sql = null;
		if (!StringUtil.isEmpty(conditions)) {

			sql = "select * from t_my_office_supplies where fname like '%"
					+ conditions + "%'";
		}
		String tab = null;

		try {
			tab = TableUtil.getNoLimitTabStr(sql);

			if (tab != null) {
				ActionUtil.getResponse().getWriter().write(tab);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
