package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
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
import com.opensymphony.xwork2.ActionSupport;

/**
 * 办公用品申购Action
 * 
 * @author Administrator
 * 
 */
public class SubscribeAction extends ActionSupport {

	/**
	 * 查询所有
	 */
	public void queryAllSubscribe() {
		HttpServletRequest request = ActionUtil.getRequest();

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));

		String sql = "select t_officestuffpurchasedetail.fname,t_officestuffpurchase.ftitle,t_officestuffpurchase.fdepartment,t_officestuffpurchase.fstatus,t_officestuffpurchase.fapplicant,t_officestuffpurchase.fapplydate,t_officestuffpurchase.fapprover,t_officestuffpurchase.fid,t_officestuffpurchase.fusingfor  from t_officestuffpurchase,t_officestuffpurchasedetail where t_officestuffpurchase.fid = t_officestuffpurchasedetail.fofficestuffpurchaseid order by t_officestuffpurchase.fapplydate desc";
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

	/**
	 * 申购物品详情查询 queryOfficesTuffPurchaseDetail
	 */
	public void queryOfficesTuffPurchaseDetail() {
		HttpServletRequest request = ActionUtil.getRequest();

		String fofficestuffpurchaseid = request.getParameter("fofficestuffpurchaseid");
		
		//System.out.println(fofficestuffpurchaseid);
		// officestuffpurchase
		String sql = "select * from t_officestuffpurchasedetail where fofficestuffpurchaseid = '"
				+ fofficestuffpurchaseid + "'";

		String tab = null;

		try {
			tab = TableUtil.getNoLimitTabStr(sql);

			ActionUtil.getResponse().getWriter().write(tab);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void fuzzyQuerySubscribe() {
		HttpServletRequest request = ActionUtil.getRequest();
		try {
			request.setCharacterEncoding("utf-8");

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 申购物品
		String conditions = request.getParameter("conditions");
		// 申购人
		String requisitioner = request.getParameter("requisitioner");

		String sql = null;
		if (!StringUtil.isEmpty(conditions)
				&& StringUtil.isEmpty(requisitioner)) {

			sql = "select t_officestuffpurchasedetail.fname,t_officestuffpurchase.ftitle,t_officestuffpurchase.fdepartment,t_officestuffpurchase.fstatus,t_officestuffpurchase.fapplicant,t_officestuffpurchase.fapplydate,t_officestuffpurchase.fapprover,t_officestuffpurchase.fid from t_officestuffpurchase,t_officestuffpurchasedetail where t_officestuffpurchase.fid = t_officestuffpurchasedetail.fofficestuffpurchaseid and t_officestuffpurchasedetail.fname like '%"
					+ conditions + "%'";

		} else if (StringUtil.isEmpty(conditions)
				&& !StringUtil.isEmpty(requisitioner)) {

			sql = "select t_officestuffpurchasedetail.fname,t_officestuffpurchase.ftitle,t_officestuffpurchase.fdepartment,t_officestuffpurchase.fstatus,t_officestuffpurchase.fapplicant,t_officestuffpurchase.fapplydate,t_officestuffpurchase.fapprover,t_officestuffpurchase.fid from t_officestuffpurchase,t_officestuffpurchasedetail where t_officestuffpurchase.fid = t_officestuffpurchasedetail.fofficestuffpurchaseid and t_officestuffpurchase.fapplicant like '%"
					+ requisitioner + "%'";

		} else if (!StringUtil.isEmpty(conditions)
				&& !StringUtil.isEmpty(requisitioner)) {

			sql = "select t_officestuffpurchasedetail.fname,t_officestuffpurchase.ftitle,t_officestuffpurchase.fdepartment,t_officestuffpurchase.fstatus,t_officestuffpurchase.fapplicant,t_officestuffpurchase.fapplydate,t_officestuffpurchase.fapprover,t_officestuffpurchase.fid from t_officestuffpurchase,t_officestuffpurchasedetail where t_officestuffpurchase.fid = t_officestuffpurchasedetail.fofficestuffpurchaseid and t_officestuffpurchase.fapplicant like '%"
					+ requisitioner
					+ "%' and t_officestuffpurchasedetail.fname like '%"
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

	public void addOfficeSuppliesPurchase() throws IOException {

		JDBCUtil jdbc = DataUtil.getJdbcUtil();

		HttpServletRequest request = ActionUtil.getRequest();

		String operation = request.getParameter("operation");
		// 申购物品名
		String fname = request.getParameter("fname");
		// 数量
		String fnumber = request.getParameter("fnumber");
		// 类型
		String fmodel = request.getParameter("fmodel");
		//id
		String fid = request.getParameter("editpid");
		//修改者
		String fmodifier = request.getParameter("fmodifier");
		//修改日期
		String fmodifydate = request.getParameter("fmodifydate");
		
		String fusingfor = request.getParameter("fusingfor");
		
		String ftitle = request.getParameter("ftitle");
		

		Map<String, Object> map = new HashMap<String, Object>();

		String formData = ActionUtil.getRequest().getParameter("formData");
		
		String detailData = ActionUtil.getRequest().getParameter("detailData");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) JSONObject
				.fromObject(formData);

		@SuppressWarnings("unchecked")
		Map<String, Object> detailMap = (Map<String, Object>) JSONObject
		.fromObject(detailData);
		
		// 申购人
		String fapplicant = (String) dataMap.get("freceiver");

		// 申购部门
		String fdepartment = (String) dataMap.get("frecipientsdepartment");
		// 申购日期
		String fapplydate = (String) dataMap.get("frecipientsdate");
		// 审核人
		String fapprover = request.getParameter("fapprover");

		map.put("fapplicant", fapplicant);
		map.put("fdepartment", fdepartment);
		map.put("fapplydate", fapplydate);
		map.put("fapprover", fapprover);

		// 更新数据
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> updateList = (List<Map<String, Object>>) dataMap
				.get("update");

		// 更新添加数据
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> addList = (List<Map<String, Object>>) dataMap
				.get("insert");

		if (operation.equals("modify")) {
			map.clear();
			
			fname = (String) detailMap.get("fname");
			fmodel = (String) detailMap.get("fmodel");
			Object fnumber1 = detailMap.get("fnumber");
			
			map.put("fid", fid);
			map.put("ftitle", ftitle);
			map.put("fusingfor", fusingfor);
			map.put("fmodifier", fapplicant);
			map.put("fmodifydate", fapplydate);
			
			String upsql = MapUtil.getUpdateSql((HashMap) map, "t_officestuffpurchase");
			// 修改办公用品申购
			
			//System.out.println(map);
			jdbc.execute(upsql);
			String sql = "update t_officestuffpurchasedetail set fnumber='"+fnumber1+"',fname='"+fname+"',fmodel='"+fmodel+"' where fofficestuffpurchaseid='"+fid+"'";
			
			jdbc.execute(sql);
			
			
		} else if (operation.equals("add")) {

			fid = JDUuid.createID("22222222");
			// 标题
			map.put("ftitle", ftitle);
			// 用途FUSINGFOR
			map.put("fusingfor", fusingfor);
			// 流程状态
			map.put("fstatus", "草稿");
			map.put("fid", fid);
			String sql = MapUtil.getSQL((HashMap) map, "t_officestuffpurchase");
			// 添加办公用品申购
			jdbc.execute(sql);
			map.clear();
			
			map.put("fofficestuffpurchaseid", fid);
			map.put("fid", JDUuid.createID("22222222"));
			map.put("fname", fname);
			map.put("fnumber", fnumber);
			map.put("fcreator", fapplicant);
			map.put("fcreationdate", fapplydate);
			map.put("fmodel", fmodel);

			String detailSql = MapUtil.getSQL((HashMap) map,
					"t_officestuffpurchasedetail");
			jdbc.execute(detailSql);

		}
		JSONObject json = new JSONObject();

		json.accumulate("success", "success");
		// 提示
		ActionUtil.getResponse().getWriter().write(json.toString());

	}

	/**
	 * 
	 * 删除办公用品申购
	 * 
	 * @throws IOException
	 */
	public void deleteOfficeSuppliesPurchase() throws IOException {

		JDBCUtil jdbc = DataUtil.getJdbcUtil();

		HttpServletRequest request = ActionUtil.getRequest();

		String fid = request.getParameter("fid");

		//System.out.println(fid);

		String sql = "delete from  t_officestuffpurchase where fid = '"
				+ fid + "'";

		String logsql = "delete from  t_officestuffpurchasedetail where fofficestuffpurchaseid = '"
				+ fid + "' ";
		// 先删除子类记录
		jdbc.execute(logsql);

		jdbc.execute(sql);

		JSONObject json = new JSONObject();

		json.accumulate("success", "success");

		// 提示
		ActionUtil.getResponse().getWriter().write(json.toString());
	}

}
