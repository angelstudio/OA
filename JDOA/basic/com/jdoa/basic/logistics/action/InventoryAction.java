package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

/**
 * 库存Action
 * 
 * @author Administrator
 *
 */
public class InventoryAction {
	
	/**
	 * 库存模糊查询
	 * 
	 */
	public void fuzzyQueryInventory() {
		HttpServletRequest request = ActionUtil.getRequest();
		try {
			request.setCharacterEncoding("utf-8");

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 办公物品名称
		String conditions = request.getParameter("conditions");
		// 创建者
		String requisitioner = request.getParameter("requisitioner");

		String sql = null;
		if (!StringUtil.isEmpty(conditions)
				&& StringUtil.isEmpty(requisitioner)) {
			
			sql = "select * from t_inventory where fname like '%"
					+ conditions + "%'";

		} else if (StringUtil.isEmpty(conditions)
				&& !StringUtil.isEmpty(requisitioner)) {
			
			sql = "select * from t_inventory where fcreator like '%"
					+ requisitioner + "%'";

		} else if (!StringUtil.isEmpty(conditions)
				&& !StringUtil.isEmpty(requisitioner)) {

			sql = "select * from t_inventory where fcreator like '%"
					+ requisitioner
					+ "%' and fname like '%"
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
	 * 查询所有库存
	 * 
	 * 
	 */
	public void queryAllInventory() {
		HttpServletRequest request = ActionUtil.getRequest();

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));

		String sql = "select * from t_inventory order by fcreation_time desc";
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

	public void addInventory() throws IOException {

		JDBCUtil jdbc = DataUtil.getJdbcUtil();

		HttpServletRequest request = ActionUtil.getRequest();

		String operation = request.getParameter("operation");
		
		String editpid = request.getParameter("editpid");
		// 办公用品名称
		String fname = request.getParameter("fname");
		// 数量
		String famount = request.getParameter("famount");
		// 备注
		String fcomment = request.getParameter("fcomment");
		// 描述
		String fdescription = request.getParameter("fdescription");
		// 单价
		String fprice = request.getParameter("fprice");
		// 类别
		String fcategory = request.getParameter("fcategory");
		// 供应商名称
		String fsupplier = request.getParameter("fsupplier");
		// 审核人
		String fapprover = request.getParameter("fapprover");
		// 类型
		String ftype = request.getParameter("ftype");

		Map<String, Object> map = new HashMap<String, Object>();

		String formData = ActionUtil.getRequest().getParameter("formData");

		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) JSONObject
				.fromObject(formData);

		
		// 更新数据
		/*@SuppressWarnings("unchecked")
		List<Map<String, Object>> updateList = (List<Map<String, Object>>) dataMap
				.get("update");*/

		// 更新添加数据
		/*@SuppressWarnings("unchecked")
		List<Map<String, Object>> addList = (List<Map<String, Object>>) dataMap
				.get("insert");*/

		if (operation.equals("modify")) {
			
			map.clear();
			map.put("fid", editpid);
			map.put("famount", famount);
			map.put("ftype", ftype);
			map.put("fsupplier", fsupplier);
			map.put("fprice", fprice);
			map.put("fdescription", fdescription);
			map.put("fcomment", fcomment);
			
			String upsql = MapUtil.getUpdateSql((HashMap) map, "t_inventory");
			
//			System.out.println(map);
			
			jdbc.execute(upsql);

			
		} else if (operation.equals("add")) {
			// 创建人
			String fcreator = (String) dataMap.get("freceiver");
			// 创建部门
			String fdepartment = (String) dataMap.get("frecipientsdepartment");
			// 创建日期
			String fcreation_time = (String) dataMap.get("frecipientsdate");
			
			String fid = JDUuid.createID("22222222");
			map.clear();
			map.put("fid", fid);
			map.put("fname", fname);
			map.put("famount", famount);
			map.put("fcomment", fcomment);
			map.put("fcreation_time", fcreation_time);
			map.put("fdescription", fdescription);
			map.put("fprice", fprice);
			map.put("fcategory", fcategory);
			map.put("fsupplier", fsupplier);
			map.put("fcreator", fcreator);
			map.put("fapprover", fapprover);
			map.put("ftype", ftype);
			map.put("fdepartment", fdepartment);

			String sql = MapUtil.getSQL((HashMap) map, "t_inventory");
			//System.out.println(map);
			// 添加办公用品库存
			jdbc.execute(sql);

		}
		JSONObject json = new JSONObject();

		json.accumulate("success", "success");
		// 提示
		ActionUtil.getResponse().getWriter().write(json.toString());
	}
	
	public void deleteInventory() throws IOException {

		JDBCUtil jdbc = DataUtil.getJdbcUtil();

		HttpServletRequest request = ActionUtil.getRequest();

		String fid = request.getParameter("fid");
		
		String logsql = "delete from t_inventory where fid= '"
				+ fid + "' ";
		//删除
		jdbc.execute(logsql);
		
		JSONObject json = new JSONObject();

		json.accumulate("success", "success");
		// 提示
		ActionUtil.getResponse().getWriter().write(json.toString());
	}
}
