package com.jdoa.basic.logistics.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.basic.person.model.Person;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.GridDataModel;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 办公用品申领Action
 * 
 * @author Administrator
 * 
 */
public class ApplyAction extends ActionSupport {

	/**
	 * 查询所有
	 */
	public void queryAllApply() {

		HttpServletRequest request = ActionUtil.getRequest();

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));

		String sql = "select t_officedepotdetail.fname,t_officedepot.fapplicant,t_officedepot.fapplydate,t_officedepot.fdepartment,t_officedepot.fstatus,t_officedepot.fapprover,t_officedepot.freceiver,t_officedepot.frecipientsdepartment,t_officedepot.frecipientsdate,t_officedepot.fid from t_officedepot,t_officedepotdetail where t_officedepot.fid = t_officedepotdetail.fofficedepotid ";
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

	public void queryPersonalInformation() {
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		// 登录者基本资料
		Person person = user.getPerson();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> rowData = new HashMap<String, Object>();
		rowData.put("freceiver", person.getFname());

		rowData.put("frecipientsdepartment", person.getFssbm());
		// 领用日期
		rowData.put("frecipientsdate",
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		list.add(rowData);

		GridDataModel<Map<String, Object>> model = new GridDataModel<Map<String, Object>>();
		model.setTotal(1);
		model.setRows(list);

		try {
			ActionUtil.getResponse().getWriter()
					.write(JSONObject.fromObject(model).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 申领详情查询
	 * 
	 */
	public void queryApplyDetail() {
		HttpServletRequest request = ActionUtil.getRequest();

		String fofficedepotid = request.getParameter("fofficedepotid");

		String sql = "select * from t_officedepotdetail where fofficedepotid = '"
				+ fofficedepotid + "'";

		String tab = null;

		try {
			tab = TableUtil.getNoLimitTabStr(sql);

			ActionUtil.getResponse().getWriter().write(tab);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchByName() {
		HttpServletRequest request = ActionUtil.getRequest();
		String fname = request.getParameter("fname");

		String sql = "select fname,FAMOUNT,FTYPE from T_INVENTORY where fname='"
				+ fname + "'";
		String tab = null;
		try {
			tab = TableUtil.getNoLimitTabStr(sql);

			ActionUtil.getResponse().getWriter().write(tab);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fuzzyQuery() {
		HttpServletRequest request = ActionUtil.getRequest();
		try {
			request.setCharacterEncoding("utf-8");

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 领用物品
		String fname = request.getParameter("conditions");
		// 领用人
		String freceiver = request.getParameter("recipient");
		// 部门
		String frecipientsdepartment = request.getParameter("querySelect");

		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));

		String sql = null;

		String tab = null;
		if (StringUtil.isEmpty(freceiver)
				&& StringUtil.isEmpty(frecipientsdepartment)
				&& !StringUtil.isEmpty(fname)) {
			sql = "select t_officedepotdetail.fname,t_officedepot.fapplicant,t_officedepot.fapplydate,t_officedepot.fdepartment,t_officedepot.fstatus,t_officedepot.fapprover,t_officedepot.freceiver,t_officedepot.frecipientsdepartment,t_officedepot.frecipientsdate,t_officedepot.fid from t_officedepot,t_officedepotdetail where t_officedepot.fid = t_officedepotdetail.fofficedepotid and t_officedepotdetail.fname like '%"
					+ fname + "%'";

		} else if (StringUtil.isEmpty(fname)
				&& StringUtil.isEmpty(frecipientsdepartment)
				&& !StringUtil.isEmpty(freceiver)) {

			sql = "select t_officedepotdetail.fname,t_officedepot.fapplicant,t_officedepot.fapplydate,t_officedepot.fdepartment,t_officedepot.fstatus,t_officedepot.fapprover,t_officedepot.freceiver,t_officedepot.frecipientsdepartment,t_officedepot.frecipientsdate,t_officedepot.fid from t_officedepot,t_officedepotdetail where t_officedepot.fid = t_officedepotdetail.fofficedepotid and t_officedepot.freceiver like '%"
					+ freceiver + "%'";
		} else if (StringUtil.isEmpty(fname) && StringUtil.isEmpty(freceiver)
				&& !StringUtil.isEmpty(frecipientsdepartment)) {

			sql = "select t_officedepotdetail.fname,t_officedepot.fapplicant,t_officedepot.fapplydate,t_officedepot.fdepartment,t_officedepot.fstatus,t_officedepot.fapprover,t_officedepot.freceiver,t_officedepot.frecipientsdepartment,t_officedepot.frecipientsdate,t_officedepot.fid from t_officedepot,t_officedepotdetail where t_officedepot.fid = t_officedepotdetail.fofficedepotid and t_officedepot.frecipientsdepartment like '%"
					+ frecipientsdepartment + "%'";

		} else if (!StringUtil.isEmpty(fname) && !StringUtil.isEmpty(freceiver)
				&& StringUtil.isEmpty(frecipientsdepartment)) {
			sql = "select t_officedepotdetail.fname,t_officedepot.fapplicant,t_officedepot.fapplydate,t_officedepot.fdepartment,t_officedepot.fstatus,t_officedepot.fapprover,t_officedepot.freceiver,t_officedepot.frecipientsdepartment,t_officedepot.frecipientsdate,t_officedepot.fid from t_officedepot,t_officedepotdetail where t_officedepot.fid = t_officedepotdetail.fofficedepotid and t_officedepotdetail.fname like '%"
					+ fname
					+ "%' and t_officedepot.freceiver like '%"
					+ freceiver + "%'";
		} else if (!StringUtil.isEmpty(fname)
				&& !StringUtil.isEmpty(frecipientsdepartment)
				&& StringUtil.isEmpty(freceiver)) {

			sql = "select t_officedepotdetail.fname,t_officedepot.fapplicant,t_officedepot.fapplydate,t_officedepot.fdepartment,t_officedepot.fstatus,t_officedepot.fapprover,t_officedepot.freceiver,t_officedepot.frecipientsdepartment,t_officedepot.frecipientsdate,t_officedepot.fid from t_officedepot,t_officedepotdetail where t_officedepot.fid = t_officedepotdetail.fofficedepotid and t_officedepotdetail.fname like '%"
					+ fname
					+ "%' and t_officedepot.frecipientsdepartment like '%"
					+ frecipientsdepartment + "%'";
		} else if (StringUtil.isEmpty(fname) && !StringUtil.isEmpty(freceiver)
				&& !StringUtil.isEmpty(frecipientsdepartment)) {
			sql = "select t_officedepotdetail.fname,t_officedepot.fapplicant,t_officedepot.fapplydate,t_officedepot.fdepartment,t_officedepot.fstatus,t_officedepot.fapprover,t_officedepot.freceiver,t_officedepot.frecipientsdepartment,t_officedepot.frecipientsdate,t_officedepot.fid from t_officedepot,t_officedepotdetail where t_officedepot.fid = t_officedepotdetail.fofficedepotid and t_officedepot.freceiver like '%"
					+ freceiver
					+ "%' and t_officedepot.frecipientsdepartment like '%"
					+ frecipientsdepartment + "%'";
		} else if (!StringUtil.isEmpty(fname) && !StringUtil.isEmpty(freceiver)
				&& !StringUtil.isEmpty(frecipientsdepartment)) {
			sql = "select t_officedepotdetail.fname,t_officedepot.fapplicant,t_officedepot.fapplydate,t_officedepot.fdepartment,t_officedepot.fstatus,t_officedepot.fapprover,t_officedepot.freceiver,t_officedepot.frecipientsdepartment,t_officedepot.frecipientsdate,t_officedepot.fid from t_officedepot,t_officedepotdetail where t_officedepot.fid = t_officedepotdetail.fofficedepotid and t_officedepot.freceiver like '%"
					+ freceiver
					+ "%' and t_officedepot.frecipientsdepartment like '%"
					+ frecipientsdepartment
					+ "%' and t_officedepotdetail.fname like '%" + fname + "%'";

		}
		try {
			tab = TableUtil.getTabStr(sql, start, limit);
			if (tab != null) {
				ActionUtil.getResponse().getWriter().write(tab);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addOfficeSuppliesFor() throws IOException {

		JDBCUtil jdbc = DataUtil.getJdbcUtil();

		HttpServletRequest request = ActionUtil.getRequest();
		// 是增加还是修改
		String operation = request.getParameter("operation");
		// 审核人
		String fapprover = request.getParameter("fapprover");

		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> detailMap = new HashMap<String, Object>();
		// 当前申领人信息
		String personData = ActionUtil.getRequest().getParameter(
				"personDataStr");
		// 物品信息
		String itemsData = ActionUtil.getRequest().getParameter("itemsDataStr");
		// 修改后的物品信息
		String detailData = ActionUtil.getRequest().getParameter("formData");

		@SuppressWarnings("unchecked")
		Map<String, Object> personDataMap = (Map<String, Object>) JSONObject
				.fromObject(personData);

		@SuppressWarnings("unchecked")
		Map<String, Object> itemsDataMap = (Map<String, Object>) JSONObject
				.fromObject(itemsData);

		@SuppressWarnings("unchecked")
		Map<String, Object> detailDataMap = (Map<String, Object>) JSONObject
				.fromObject(detailData);

		String frecipientsdate = null;
		String freceiver = null;
		String frecipientsdepartment = null;

		if (operation.equals("add")) {
			// 领用日期
			frecipientsdate = (String) personDataMap.get("frecipientsdate");
			// 领用部门
			frecipientsdepartment = (String) personDataMap
					.get("frecipientsdepartment");
			// 领用人
			freceiver = (String) personDataMap.get("freceiver");
			// 办公用品名称
			String fname = (String) itemsDataMap.get("fname");
			// 型号
			String ftype = (String) itemsDataMap.get("ftype");
			// 库存
			Object famount = itemsDataMap.get("famount");

			// 申请数量
			Object fnumber = itemsDataMap.get("fnumber");

			String fid = JDUuid.createID("22222222");

			map.put("fid", fid);
			// 申领部门
			map.put("fdepartment", frecipientsdepartment);
			// 领用人
			map.put("freceiver", freceiver);
			// 申请人
			map.put("fapplicant", freceiver);
			// 审核人
			map.put("fapprover", fapprover);
			// 审核日期
			map.put("fapproveldate",
					new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			// 流程状态
			map.put("fstatus", "草稿");
			// 领用部门
			map.put("frecipientsdepartment", frecipientsdepartment);
			// 领用日期
			map.put("frecipientsdate", frecipientsdate);
			// 申请日期 fapplydate
			map.put("fapplydate",
					new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

			String sql = MapUtil.getSQL((HashMap<String, Object>) map,
					"t_officedepot");
			// 添加办公用品申领
			jdbc.execute(sql);

			// 添加办公用品领用详情
			String detailId = JDUuid.createID("22222222");
			detailMap.put("fid", detailId);
			detailMap.put("fofficedepotid", fid);
			detailMap.put("fnumber", fnumber);
			detailMap.put("fcreateman", freceiver);
			detailMap.put("fcreatedate", frecipientsdate);
			detailMap.put("fmodel", ftype);
			detailMap.put("fname", fname);

			String detailSql = MapUtil.getSQL(
					(HashMap<String, Object>) detailMap, "t_officedepotdetail");

			jdbc.execute(detailSql);

		} else if ("modify".equals(operation)) {
			map.clear();

			String fid = request.getParameter("editpid");
			// 领用人
			freceiver = request.getParameter("freceiver");
			// 领用部门
			frecipientsdepartment = request
					.getParameter("frecipientsdepartment");
			// 领用日期
			frecipientsdate = request.getParameter("frecipientsdate");
			// 修改人
			String fmodifier = (String) personDataMap.get("freceiver");
			// 修改日期
			String fmodifydate = (String) personDataMap.get("frecipientsdate");

			map.put("fid", fid);
			map.put("freceiver", freceiver);
			map.put("frecipientsdepartment", frecipientsdepartment);
			map.put("frecipientsdate", frecipientsdate);
			map.put("fmodifier", fmodifier);
			map.put("fmodifydate", fmodifydate);

			String sql = MapUtil.getUpdateSql((HashMap) map, "t_officedepot");

			// System.out.println(map);

			jdbc.execute(sql);
			map.clear();

			String fnumber = (String) detailDataMap.get("fnumber");

			String detailSql = "update t_officedepotdetail set fnumber = '"
					+ fnumber + "' where fofficedepotid = '" + fid + "'";

			jdbc.execute(detailSql);

		}
		JSONObject json = new JSONObject();

		json.accumulate("success", "success");
		// 提示
		ActionUtil.getResponse().getWriter().write(json.toString());

	}

	/**
	 * 办公用品详情删除
	 * 
	 * @throws IOException
	 */
	public void delOfficeSuppliesForDetail() throws IOException {

		JDBCUtil jdbc = DataUtil.getJdbcUtil();

		String fid = ActionUtil.getRequest().getParameter("fid");

		String sql = "delete  from  t_officedepotdetail where fid= '" + fid
				+ "' ";

		jdbc.execute(sql);

		JSONObject json = new JSONObject();

		json.accumulate("success", "success");

		// 提示
		ActionUtil.getResponse().getWriter().write(json.toString());

	}

	/**
	 * 
	 * 删除办公用品申领
	 * 
	 * @throws IOException
	 */
	public void deleteOfficeSuppliesFor() throws IOException {

		JDBCUtil jdbc = DataUtil.getJdbcUtil();

		HttpServletRequest request = ActionUtil.getRequest();

		String fid = request.getParameter("fid");

		String operation = request.getParameter("operation");
		String sql = null;
		String logsql = null;
		if ("delete".equals(operation)) {
			
			sql = "delete from t_officedepot where fid ='"+fid+"'";
			
			logsql = "delete from t_officedepotdetail where fofficedepotid = '"+fid+"'";
		} else {
			
			sql = "delete from  t_officedepot where fid in (select fofficedepotid from t_officedepotdetail where fid = '"
					+ fid + "' )";

			logsql = "delete from  t_officedepotdetail where fid= '" + fid
					+ "' ";

		}

		// 先删除子类记录
		jdbc.execute(logsql);
		jdbc.execute(sql);

		JSONObject json = new JSONObject();

		json.accumulate("success", "success");

		// 提示
		ActionUtil.getResponse().getWriter().write(json.toString());
	}

}
