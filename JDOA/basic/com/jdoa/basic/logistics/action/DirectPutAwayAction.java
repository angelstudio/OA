package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.TableUtil;

/**
 * 直接入库Action
 * 
 * @author Administrator
 *
 */
public class DirectPutAwayAction {

	public void queryAllDirectPutAway() {
		HttpServletRequest request = ActionUtil.getRequest();

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));

		String sql = "select * from t_direct_put_away order by fcreation_date desc";

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

	public void fuzzyQueryDirectPutAway() {
		HttpServletRequest request = ActionUtil.getRequest();
		try {
			request.setCharacterEncoding("utf-8");

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String conditions = request.getParameter("conditions");

		String sql = "select * from t_direct_put_away where fincoming_goods_name like '%"
				+ conditions + "%'";

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
	//DIRECTPUTAWAY
	public void addDirectPutAway() {

		HttpServletRequest request = ActionUtil.getRequest();

		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();

		String operation = request.getParameter("operation");
		// 入库物品名称
		String fincoming_goods_name = request
				.getParameter("fincoming_goods_name");
		// 数量
		String famount = request.getParameter("famount");
		// 备注
		String fcomment = request.getParameter("fcomment");
		// 型号
		String ftype = request.getParameter("ftype");
		// 审核人
		String fauditor = request.getParameter("fapprover");

		// id  office_supply_type.jsp
		String fdirect_put_away_id = null;

		Map<String, Object> map = new HashMap<String, Object>();

		String formData = ActionUtil.getRequest().getParameter("formData");

		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) JSONObject
				.fromObject(formData);

		if ("add".equals(operation)) {
			// 新增
			// 入库人
			String fcreator = (String) dataMap.get("freceiver");
			// 入库日期
			String fcreation_date = (String) dataMap.get("frecipientsdate");
			// 入库部门
			String fdepartment = (String) dataMap.get("frecipientsdepartment");

			fdirect_put_away_id = JDUuid.createID("22222222");

			map.clear();
			map.put("fdirect_put_away_id", fdirect_put_away_id);

			map.put("fincoming_goods_name", fincoming_goods_name);

			map.put("famount", famount);

			map.put("fcomment", fcomment);

			map.put("fcreator", fcreator);

			map.put("fcreation_date", fcreation_date);

			map.put("ftype", ftype);

			map.put("fdepartment", fdepartment);

			map.put("fauditor", fauditor);

			String sql = MapUtil.getSQL((HashMap) map, "t_direct_put_away");
			
			
			
			
			
			// System.out.println(map);

			jdbcUtil.execute(sql);

		} else if ("modify".equals(operation)) {
			// 修改
			
		    String fmodifier = (String) dataMap.get("freceiver");
		    String fmodification_date = (String) dataMap.get("frecipientsdate");
			String fid = request.getParameter("editpid");
		    
			String sql = "update t_direct_put_away set famount = '" + famount
					+ "', fcomment = '" + fcomment + "', ftype = '" + ftype
					+ "', fmodifier = '" + fmodifier
					+ "', fmodification_date = '"
					+ fmodification_date
					+ "' where fdirect_put_away_id='" + fid+"'";

			jdbcUtil.execute(sql);

		} else if ("delete".equals(operation)) {

			JDBCUtil jdbc = DataUtil.getJdbcUtil();

			String fid = request.getParameter("fid");

			// 删除
			String sql = "delete t_direct_put_away where FDIRECT_PUT_AWAY_ID = '"
					+ fid + "'";

			jdbcUtil.execute(sql);

		}
		try {
			JSONObject json = new JSONObject();

			json.accumulate("success", "success");
			// 提示
			ActionUtil.getResponse().getWriter().write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
