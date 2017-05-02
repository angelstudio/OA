package com.jdoa.basic.logistics.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.TableUtil;

/**
 * 
 * 购置入库Action
 * @author Administrator
 *
 */
public class PurchaseInventoryAction {
	public void queryAll() {
		//T_PURCHASE_INVENTORY
		String sql = "select * from t_purchase_inventory";
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
	
	public void fuzzyQueryPurchaseInventory() {
		HttpServletRequest request = ActionUtil.getRequest();
		try {
			request.setCharacterEncoding("utf-8");

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String conditions = request.getParameter("conditions");

		String sql = "select * from t_purchase_inventory where fname like '%"
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

	
	/**
	 * 新增、修改、删除
	 * 
	 */
	public void addPurchaseInventory() {

		HttpServletRequest request = ActionUtil.getRequest();

		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();

		String operation = request.getParameter("operation");

		String fname = request.getParameter("fname");

		String famount = request.getParameter("famount");
		String fcomment = request.getParameter("fcomment");
		String fcreator = request.getParameter("fcreator");
		String fcreation_date = request.getParameter("fcreation_date");
		String fmodifier = request.getParameter("fmodifier");
		String fmodification_date = request.getParameter("fmodification_date");
		String ftype = request.getParameter("ftype");
		String fpurchase_inventory_id = request
				.getParameter("fpurchase_inventory_id");

		if ("add".equals(operation)) {
			// 新增
			HashMap map = new HashMap();

			fpurchase_inventory_id = JDUuid.createID("22222222");

			map.put("fpurchase_inventory_id", fpurchase_inventory_id);
			map.put("fname", fname);
			map.put("famount", famount);
			map.put("fcomment", fcomment);
			map.put("fcreator", fcreator);
			map.put("fcreation_date", fcreation_date);
			map.put("fmodifier", fmodifier);
			map.put("fmodification_date", fmodification_date);
			map.put("ftype", ftype);

			String sql = MapUtil.getSQL(map, "t_purchase_inventory");

			jdbcUtil.execute(sql);

		} else if ("modify".equals(operation)) {
			// 修改
			String sql = "update t_purchase_inventory set fname='" + fname
					+ "',famount='" + famount + "',fcomment='" + fcomment
					+ "',fcreator='" + fcreator + "',fcreation_date='"
					+ fcreation_date + "',fmodifier='" + fmodifier
					+ "',fmodification_date='" + fmodification_date
					+ "',ftype='" + ftype + "' where fpurchase_inventory_id ='"
					+ fpurchase_inventory_id + "'";

			jdbcUtil.execute(sql);
		} else if ("delete".equals(operation)) {
			// 删除
			String sql = "delete t_purchase_inventory where fpurchase_inventory_id = '"
					+ fpurchase_inventory_id + "'";
			jdbcUtil.execute(sql);

		}

	}
}
