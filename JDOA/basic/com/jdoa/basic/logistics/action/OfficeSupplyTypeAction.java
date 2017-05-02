package com.jdoa.basic.logistics.action;

import javax.servlet.http.HttpServletRequest;

import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.TableUtil;

/**
 * 办公用品类型Action
 * 
 * @author Administrator
 *
 */
public class OfficeSupplyTypeAction {

	public void queryAllOfficeSupplyType(){
		HttpServletRequest request = ActionUtil.getRequest();

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));

		String sql = "select * from t_office_supply_type order by fcreation_date desc";

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
}
