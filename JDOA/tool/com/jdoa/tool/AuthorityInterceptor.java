package com.jdoa.tool;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.jdoa.basic.jbpm.model.JBPMKey;
import com.jdoa.jbpm.action.JBPMStartUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.jdoa.tool.StringUtil;

public class AuthorityInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1358600090729208361L;
	private static Log log = LogFactory.getLog(AbstractInterceptor.class);

	// 拦截Action处理的拦截方法
	public String intercept(ActionInvocation invocation) throws Exception {
		// 取得请求相关的ActionContext实例
		System.out.println("自定义权限拦截器");
		String actionName = invocation.getInvocationContext().getName();
		System.out.println("获取请求的action名字" + actionName);

		// 多数据中心同时访问时，数据问题 add by qingfeng_li 2013-7-8
		String datacenterName = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("datacenterName");
		if (datacenterName != null) {
			DataSourceContextHolder.setDataSourceType(datacenterName);
		}

		PrintWriter writer = ActionUtil.getResponse().getWriter();
		ActionContext ctx = invocation.getInvocationContext();
		String userid = (String) ctx.getSession().get("fuserId");

		String authoritytype = "0";// 默认设置为零 解决为空问题authoritytype
		if ((String) ctx.getParameters().get("authoritytype") != null) {

			authoritytype = (String) ctx.getParameters().get("authoritytype");
			// 若为-1则为系统内置可用功能权限
		}
		// 2013-12-19
		String[] objTypes = (String[]) ctx.getParameters().get("type");
		String objType = null;
		if (objTypes != null) {
			objType = objTypes[0];
		}
		String[] fids = (String[]) ctx.getParameters().get("id");
		String fid = null;
		if (fids != null) {
			fid = fids[0];
		}

		String[] flags = (String[]) ctx.getParameters().get("flag");
		String flag = null;
		if (flags != null) {
			flag = flags[0];
		}

		// 处理自定义action 没有FId flag objecttype 的情况重新获取
		String[] p_bizids = null;
		String p_bizid = null;
		String p_flag = null;
		String p_objectType = null;
		String[] taskIds = null;
		String p_taskId = null;

		if (fid == null && ctx.getParameters().get("p_bizid") != null) {
			p_bizids = (String[]) ctx.getParameters().get("p_bizid");
			p_bizid = p_bizids[0];
		} else {
			p_bizid = fid;
		}
		if (p_flag == null && ctx.getParameters().get("p_type") != null) {
			p_flag = ctx.getParameters().get("p_type").toString();
		} else {
			p_flag = flag;
		}
		if (p_objectType == null && ctx.getParameters().get("p_form") != null) {
			p_objectType = (String) ctx.getParameters().get("p_form");
		} else {
			p_objectType = objType;
		}
		if (p_taskId == null && ctx.getParameters().get("p_taskId") != null) {
			taskIds = (String[]) ctx.getParameters().get("p_taskId");
			if (taskIds != null) {
				p_taskId = taskIds[0];
			}
		}

		if (actionName != null && ("-1".equals(authoritytype))) {
			return invocation.invoke();
		}
		JDBCUtil jdbcutil =DataUtil.getJdbcUtil();
		if (userid == null) {
			return null;
		} else {
			//boolean bl=hasAuthority(userid, actionName, objType);
			if (true) {
				invocation.invoke();
				if (p_bizid == null) {
					p_bizid = "";
				}
				HttpServletRequest request = ServletActionContext.getRequest();
				String again = (String) request.getAttribute(JBPMKey.JBPM_AGAIN);
				if (!StringUtil.isEmpty(again) && ("OK").equals(again)) {
					p_bizid = (String) request.getAttribute("p_bizid");
				}
				String result = (String) request.getAttribute(p_bizid);
				if (JBPMKey.JBPM_SUCCESS.equals(result)) {
					JBPMStartUtil jBPMStartUtil = new JBPMStartUtil();
					jBPMStartUtil.getForInfo(userid, actionName, p_objectType,
							p_bizid, p_flag, p_taskId);
  				   return "success";
				} else {
					return "success";
			}
			} else {
				try {
					writer.write("has-no-authority");
					return null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return invocation.invoke();

	}

	public void destroy() {

		super.destroy();

	}

	/**
	 * 字符串是否包含在数组中
	 * 
	 * @author lyle
	 * @param actionName
	 * @return Nov 7, 2013
	 */
	private boolean isIncluded(String string, String[] args) {
		if (args == null || string == null) {
			return false;
		}
		for (int i = 0; i < args.length; i++) {
			if (string.equals(args[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否有权限
	 * 
	 * @author lyle
	 * @param userid
	 * @param actionName
	 * @param objType
	 * @return Nov 7, 2013
	 */
	private boolean hasAuthority(String userid, String actionName,
			String objType) {
		String isSysUserSql = "select 1 from t_sys_user where fid = '" + userid
				+ "'  and fusertype='-1' "; // 是否是系统用户
		StringBuffer sql = new StringBuffer();
		sql
				.append("select 1 from t_authority_acl where actionname = '")
				.append(actionName)
				.append("' and ( userid= '")
				.append(userid)
				.append("' ")
				.append(
						" or userroleid in (select roleid from t_user_role where userid  = '")
				.append(userid).append("')) ");
		if (isIncluded(actionName, InitUtil.getNeedInterceptCommonActions())) {
			sql.append(" and  ftype  = '").append(objType).append("' ");
		}
		sql.append(" union ").append(isSysUserSql);
		JDBCUtil jdbcutil = DataUtil.getJdbcUtil();
		ResultSet rs = null;
		try {
			rs = jdbcutil.executeQuery(sql.toString());
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return false;
	}
}
