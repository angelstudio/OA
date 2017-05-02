package com.jdoa.basic.org.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jdoa.baisc.org.model.Organization;
import com.jdoa.basic.org.service.OrgService;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.StringUtil;

public class OrgAction {
	private OrgService orgService;

	public OrgService getOrgService() {
		return orgService;
	}

	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @author Action
	 * @date 2017-04-09
	 * @describe 查询组织架构信息
	 */
	public void queryAllOrg() {
		List<Organization> orgList = orgService.queryOrg();
		JSONArray jsonArr = new JSONArray();
		for (int i = 0; i < orgList.size(); i++) {
			Organization org = orgList.get(i);
			JSONObject json = new JSONObject();
			String fid = org.getFid();
			String fname = org.getFname();
			String fparentId = org.getFparentId();
			int fseq = org.getFseq();
			int flevel = org.getFlevel();
			String flongNumber = org.getFlongNumber();
			json.put("id", fid);
			json.put("pid", fparentId);
			json.put("text", fname);
			json.put("flevel", flevel);
			json.put("fseq", fseq);
			json.put("flongNumber", flongNumber);
			jsonArr.add(json);
		}
		try {
			ActionUtil.getResponse().getWriter().write(jsonArr.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author Action
	 * @descrbie 组织新增
	 * @date 2017-04-09
	 */
	public void addOrg() {
		HttpServletRequest request = ActionUtil.getRequest();
		String fid = request.getParameter("fid");
		String fname = request.getParameter("fname");
		String strSeq = request.getParameter("fseq");
		String pflongNumber = request.getParameter("pflongNumber");
		String fparentId = request.getParameter("fparentId");
		String flevelStr = request.getParameter("flevel");
		if (StringUtil.isEmpty(strSeq)) {
			strSeq = "999";
		}
		int fseq = Integer.valueOf(strSeq);
		if (StringUtil.isEmpty(flevelStr)) {
			flevelStr = "1";
		}
		int flevel = Integer.valueOf(flevelStr);
		fid = JDUuid.createID("2222_org");
		Organization org= new Organization();
		org.setFid(fid);
		org.setFname(fname);
		org.setFparentId(fparentId);
		org.setFlevel(flevel);
		org.setFlongNumber( pflongNumber + "_" + fid);
		org.setFseq(fseq);
		try {
			orgService.addOrg(org);
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * @author Action
	 * @date 2017-04-09
	 * @describe 组织修改
	 */
	public void editOrg(){
		HttpServletRequest request = ActionUtil.getRequest();
		String fid = request.getParameter("fid");
		String fname = request.getParameter("fname");
		String strSeq = request.getParameter("fseq");
		if (StringUtil.isEmpty(strSeq)) {
			strSeq = "999";
		}
		int fseq = Integer.valueOf(strSeq);
		Organization org= new Organization();
		org.setFid(fid);
		org.setFname(fname);
		org.setFseq(fseq);
		try {
			orgService.updateOrg(org);
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	/**
	 * @author Action
	 * @date 2017-04-09
	 * @describe 删除组织信息
	 */
	public void delOrg(){
		HttpServletRequest request = ActionUtil.getRequest();
		String fid = request.getParameter("fid");
		try {
			orgService.deleteOrg(fid);
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
