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
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;

public class SummaryOfRecipientsAction {
	/**
	 * 查询个人汇总
	 * 
	 * 
	 */
	public void queryAllSummaryOfRecipients() {
		HttpServletRequest request = ActionUtil.getRequest();

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));

		String sql = "select * from t_summary_of_recipients order by ftotal_quantity desc";
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

	// 个人领用汇总模糊查询
	public void fuzzyQuerySummaryOfRecipients() {
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

			sql = "select * from t_summary_of_recipients where fname like '%"
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

	/**
	 * 新增个人领用汇总
	 * 
	 * @throws IOException
	 */
	public void addSummaryOfRecipients() throws IOException {

		JDBCUtil jdbc = DataUtil.getJdbcUtil();

		HttpServletRequest request = ActionUtil.getRequest();

		String operation = request.getParameter("operation");

		String editpid = request.getParameter("editpid");
		// 办公用品名称
		String fname = request.getParameter("fname");
		// 领用总数量
		String ftotal_quantity = request.getParameter("ftotal_quantity");
		// 备注
		String fcomment = request.getParameter("fcomment");
		// 型号
		String ftype = request.getParameter("ftype");

		Map<String, Object> map = new HashMap<String, Object>();

		String formData = ActionUtil.getRequest().getParameter("formData");

		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) JSONObject
				.fromObject(formData);

		// 更新数据
		/*
		 * @SuppressWarnings("unchecked") List<Map<String, Object>> updateList =
		 * (List<Map<String, Object>>) dataMap .get("update");
		 */

		// 更新添加数据
		/*
		 * @SuppressWarnings("unchecked") List<Map<String, Object>> addList =
		 * (List<Map<String, Object>>) dataMap .get("insert");
		 */

		if (operation.equals("modify")) {

			map.clear();
			map.put("fid", editpid);

			map.put("fcomment", fcomment);

			String upsql = MapUtil.getUpdateSql((HashMap) map,
					"t_for_the_detail");

			// System.out.println(map);

			jdbc.execute(upsql);

		} else if (operation.equals("add")) {

			String fid = JDUuid.createID("22222222");
			map.clear();
			map.put("fid", fid);
			map.put("fname", fname);
			map.put("ftotal_quantity", ftotal_quantity);
			map.put("fcomment", fcomment);
			map.put("ftype", ftype);

			String sql = MapUtil.getSQL((HashMap) map,
					"t_summary_of_recipients");

			// System.out.println(map);
			// 添加
			jdbc.execute(sql);

		}
		JSONObject json = new JSONObject();

		json.accumulate("success", "success");
		// 提示
		ActionUtil.getResponse().getWriter().write(json.toString());
	}

	// 删除
	public void deleteSummaryOfRecipients() throws IOException {

		JDBCUtil jdbc = DataUtil.getJdbcUtil();

		HttpServletRequest request = ActionUtil.getRequest();

		String fid = request.getParameter("fid");

		String logsql = "delete from t_summary_of_recipients where fid= '"
				+ fid + "' ";
		// 删除
		jdbc.execute(logsql);

		JSONObject json = new JSONObject();

		json.accumulate("success", "success");
		// 提示
		ActionUtil.getResponse().getWriter().write(json.toString());
	}
}
