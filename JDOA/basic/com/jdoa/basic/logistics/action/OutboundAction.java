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

public class OutboundAction {

	public void queryAllOutbound() {

		HttpServletRequest request = ActionUtil.getRequest();

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));

		String sql = "select * from t_outbound order by fcreation_date,fmodification_date";
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

	public void fuzzyQueryOutbound() {
		HttpServletRequest request = ActionUtil.getRequest();
		try {
			request.setCharacterEncoding("utf-8");

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String conditions = request.getParameter("conditions");

		String sql = "select * from t_outbound where foutbound_goods_name like '%"
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

	public void addOutbound() {

		HttpServletRequest request = ActionUtil.getRequest();

		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();

		String operation = request.getParameter("operation");
		// 出库用品名称
		String foutbound_goods_name = request
				.getParameter("foutbound_goods_name");
		// 数量
		String famount = request.getParameter("famount");
		// 备注
		String fcomment = request.getParameter("fcomment");
		// 审核人
		String fauditor = request.getParameter("fapprover");
		// 办公用品型号
		String ftype = request.getParameter("ftype");
		// Id
		String foutbound_id = request.getParameter("editpid");

		Map<String, Object> map = new HashMap<String, Object>();

		String formData = ActionUtil.getRequest().getParameter("formData");

		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) JSONObject
				.fromObject(formData);

		if ("add".equals(operation)) {
			// 创建者
			String fcreator = (String) dataMap.get("freceiver");
			// 创建日期
			String fcreation_date = (String) dataMap.get("frecipientsdate");
			// 创建部门
			String fdepartment = (String) dataMap.get("frecipientsdepartment");
			// 新增
			map.clear();
			foutbound_id = JDUuid.createID("22222222");

			map.put("foutbound_id", foutbound_id);
			map.put("foutbound_goods_name", foutbound_goods_name);
			map.put("famount", famount);
			map.put("fcomment", fcomment);
			map.put("fcreator", fcreator);
			map.put("fcreation_date", fcreation_date);
			map.put("ftype", ftype);
			map.put("fauditor", fauditor);
			map.put("fdepartment", fdepartment);

			String sql = MapUtil.getSQL((HashMap) map, "t_outbound");

			// System.out.println(map);

			jdbcUtil.execute(sql);

		} else if ("modify".equals(operation)) {
			// 创建者
			String fcreator = (String) dataMap.get("freceiver");
			// 创建日期
			String fcreation_date = (String) dataMap.get("frecipientsdate");

			// 修改
			String sql = "update t_outbound set foutbound_goods_name='"
					+ foutbound_goods_name + "',famount='" + famount
					+ "',fcomment='" + fcomment + "',fmodifier='" + fcreator
					+ "',fmodification_date='" + fcreation_date + "',ftype='"
					+ ftype + "' where foutbound_id ='" + foutbound_id + "'";

			// System.out.println(sql);
			jdbcUtil.execute(sql);
		} else if ("delete".equals(operation)) {
			JDBCUtil jdbc = DataUtil.getJdbcUtil();

			foutbound_id = request.getParameter("fid");
			// 删除
			String sql = "delete t_outbound where foutbound_id = '"
					+ foutbound_id + "'";

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
