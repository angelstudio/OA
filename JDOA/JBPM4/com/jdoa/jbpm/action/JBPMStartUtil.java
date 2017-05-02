package com.jdoa.jbpm.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;

import com.jdoa.basic.jbpm.model.JBPMKey;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TimeUtil;



public class JBPMStartUtil {

/**
 * 
 * @param userid 用户id
 * @param actionName 
 * @param objType 单据、基础资料 类型 
 * @param fid 单据ID
 * @param flag 基础资料或者单据
 * @param p_taskId 节点ID 
 */
	public void getForInfo(String userid, String actionName, String objType,
			String fid, String flag, String p_taskId) {
		String[] formTypes = objType.split(",");
		String[] formIds = fid.split(",");
		//String[] formFlags = flag.split(",");
		String[] formFlags = null;
		String formTaskId = p_taskId;
		for (int i = 0; i < formTypes.length; i++) {
			String formActionName = actionName;
			String formType = formTypes[i];
			String formId = formIds[i];
			//String formFlag = formFlags[i];
			String formFlag=null;
			startJBPM(userid, formActionName, formType, formId, formFlag,
					formTaskId);
		}

	}

	/**
	 * 根据传入的actionName,从y_form,t_bp_process表查找相应信息，确定流程是否启动
	 * 
	 * @param userid
	 * @param actionName
	 * @param objType
	 *            2014-08-19 taotao
	 */
	public void startJBPM(String userid, String formActionName,
			String formType, String formId, String formFlag, String formTaskId) {
		JDBCUtil jdbcUtil=DataUtil.getJdbcUtil();
		String username = (String) ActionUtil.getRequest().getSession().getAttribute("fusername");
		ResultSet rs = null, rs1 = null, rs3 = null, rs4 = null, rs5 = null;
		try {
			String JBPMActionName = formType+"_"+formActionName;
			// 根据formTaskId判断是否启动节点还是人工任务
			String actionSql = null;
			if (StringUtil.isEmpty(formTaskId)) {
				actionSql = "select b.fprocessid,c.ftaskid,c.startmethodradio from t_bp_process a inner join y_form b on a.fid=b.fprocessid "
						+ "inner join m_t_changedata c on b.ftaskid=c.ftaskid    where "
						+ "b.factionname='"
						+ JBPMActionName
						+ "' and c.changemethodradio='0' and a.fstatus ='2' and c.startmethodradio='1' ";
			} else {
				actionSql = "select b.fprocessid,c.ftaskid,c.startmethodradio from t_bp_process a inner join y_form b on a.fid=b.fprocessid "
						+ "inner join m_t_changedata c on b.ftaskid=c.ftaskid    where "
						+ "b.factionname='"
						+ JBPMActionName
						+ "' and c.changemethodradio='0' and a.fstatus ='2' and c.ftaskid='"
						+ formTaskId + "'";
			}
			rs = jdbcUtil.executeQuery(actionSql);
			ProcessEngine processEngine = ((ProcessEngine) DataUtil
					.getSpringBean("processEngine"));
			while (rs.next()) {
				if ("1".equals(rs.getString("startmethodradio"))) {
					// 流程id
					String fprocessid = rs.getString("fprocessid");
					// 流程节点id
					String ftaskid = rs.getString("ftaskid");
					// 先从部门ID，其次角色ID,最后userid进行判断
					if (byUserid(userid, ftaskid)) {
						// 获取流程部署ID
						String sql = "select STRINGVAL_ from t_bp_process a inner join JBPM4_DEPLOYPROP b on a.fdeploymentid=b.DEPLOYMENT_ where  key_='pdid' and a.fid='"
								+ fprocessid + "'";
						rs1 = jdbcUtil.executeQuery(sql);
						if (rs1.next()) {
							String processDeployid = rs1
									.getString("STRINGVAL_");
							// 传递系统必要变量
							HashMap<String, Object> var = new HashMap<String, Object>();
							var.put(JBPMKey.BIZID, formId);
							var.put(JBPMKey.BIZTYPE, formType);
							var.put(JBPMKey.FORMTYPE, formFlag);
							// 添加系统配置的变量
							addConfigVar(var, fprocessid, formType, formFlag,
									formId, ftaskid);
							// 1.发起流程
							ProcessInstance processInstance = processEngine
									.getExecutionService()
									.startProcessInstanceById(processDeployid,
											var, formId);
							String fcurrentInstanceProcessId = processInstance
									.getId();
							String currentTasksql = "select dbid_ from jbpm4_task where u_id='"
									+ ftaskid
									+ "' and execution_id_='"
									+ fcurrentInstanceProcessId + "'";
							rs3 = jdbcUtil.executeQuery(currentTasksql);

							if (rs3.next()) {
								String currenttaskid = rs3.getString("dbid_");
								processEngine.getTaskService().completeTask(
										currenttaskid);
							}
							String exeSql = "select DBID_ from jbpm4_execution where id_='"
									+ fcurrentInstanceProcessId + "'";
							rs4 = jdbcUtil.executeQuery(exeSql);
							String exedbid = "";
							while (rs4.next()) {
								exedbid = rs4.getString("DBID_");
							}
							// 新增一个map存起草信息
							String date = TimeUtil.formatDateToString(
									new Date(), "yyyy-MM-dd HH:mm:ss");
							Map<String, String> map = new HashMap<String, String>();
							map.put("fman", username);
							map.put("fbusinessId", formId);
							map.put("fname", formType);
							map.put("ftaskId", ftaskid);
							map.put("fprocessId", fprocessid);
							map.put("fcurrentInstanceProcessId",
									fcurrentInstanceProcessId);
							map.put("userid", userid);
							map.put("ftime", date);
							map.put("fdbid", exedbid);
							BaseDataUtil.saveData(map,
									"m_t_jbpmStartRecord");
						}
					}
				} else {
					// 人工非启动节点。
					// 流程节点id
					String fprocessid = rs.getString("fprocessid");
					String ftaskid = rs.getString("ftaskid");
					// 先从部门ID，其次角色ID,最后userid进行判断
					if (byUserid(userid, ftaskid)) {
						// 流程节点id
						String sql = "select * from jbpm4_task where execution_id_ in ( "
								+ " select id_ from jbpm4_execution  where key_='"
								+ formId + "') and u_id='" + ftaskid + "'";
						String taskBizid = null;
						String execution_id_ = null;
						String ftaskName = null;
						rs5 = jdbcUtil.executeQuery(sql);
						// 添加系统配置的变量
						if (rs5.next()) {
							// 流程id
							taskBizid = rs5.getString("DBID_");
							execution_id_ = rs5.getString("execution_id_");
							ftaskName = rs5.getString("taskdefname_");
							Set<String> var_set = processEngine
									.getExecutionService().getVariableNames(
											execution_id_);
							HashMap var_map = null;
							if (var_set != null && var_set.size() > 0) {
								var_map = (HashMap) processEngine
										.getExecutionService().getVariables(
												execution_id_, var_set);
							} else {
								var_map = new HashMap();
							}
							addConfigVar(var_map, fprocessid, formType,
									formFlag, formId, ftaskid);
							processEngine.getExecutionService().setVariables(
									execution_id_, var_map);
							processEngine.getTaskService().completeTask(
									taskBizid);
							changeMessageStatus(userid, ftaskid, execution_id_);
							// 新增一个map存人工信息
							String date = TimeUtil.formatDateToString(
									new Date(), "yyyy-MM-dd HH:mm:ss");
							Map<String, String> map = new HashMap<String, String>();
							map.put("fman", username);
							map.put("fbusinessId", formId);
							map.put("fname", formType);
							map.put("ftaskId", ftaskid);
							map.put("fprocessId", fprocessid);
							map.put("fcurrentInstanceProcessId", execution_id_);
							map.put("userid", userid);
							map.put("ftime", date);
							map.put("fdbid", taskBizid);
							map.put("ftaskName", ftaskName);
							BaseDataUtil.saveData(map,
									"c02bbb50f5074e3082989426921d250210000018");
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * addConfigVar:(这里用一句话描述这个方法的作用). <br/>
	 * 添加配置变量
	 * 
	 * @author Administrator
	 * @param var
	 * @throws SQLException
	 * @since JDK 1.6
	 */
	public void addConfigVar(HashMap var, String fprocessid, String billtype,
			String flag, String id, String taskid) throws SQLException {
         JDBCUtil jdbcUtil= DataUtil.getJdbcUtil();
		String sql = " select fsqlparamnamebox1,fsqlparamvaluebox1,fsqlparamtypecombo1 from y_form where  fprocessid='"
				+ fprocessid + "' and ftaskid='" + taskid + "'";
		ResultSet rs = jdbcUtil.executeQuery(sql);
		String tableName = null;
		String	tableNameSql = "select ftable from t_jbpm_zlb where fname='"+ billtype + "'";
		ResultSet rs1 = jdbcUtil.executeQuery(tableNameSql);
		if (rs1.next()) {
			tableName = rs1.getString("ftable");
		}

		String valueSql = "select * from " + tableName + " where fid='" + id
				+ "'";
		ResultSet rs2 = jdbcUtil.executeQuery(valueSql);
		if (rs2.next()) {
			while (rs.next()) {
				var.put(rs.getString("fsqlparamnamebox1"), rs2.getString(rs
						.getString("fsqlparamvaluebox1").trim()));
			}
		}
	}

	public boolean byUserid(String userid, String ftaskid) {
		JDBCUtil jdbcUtil= DataUtil.getJdbcUtil();
		boolean returnVal = false;
		String sql = "select a.fprocessid,a.ftaskid,a.delegateuserid,a.delegateroleid,a.delegatedeptid,a.delegatesponsor from t_bp_processdelegate a where a.ftaskid='"
				+ ftaskid + "'";
		try {
			ResultSet rs = jdbcUtil.executeQuery(sql);
			if (rs.next()) {
				// 用户ID
				String delegateuserid = rs.getString("delegateuserid");
				// 角色ID
				String delegateroleid = rs.getString("delegateroleid");
				// 部门ID
				String delegatedeptid = rs.getString("delegatedeptid");
				// 从部门ID查找是否包含userid;
				String departmentSql = "select a.fid,b.fbmid from t_sys_user a inner join t_person b  on a.fpersonid=b.fid  where b.fbmid='"
						+ delegatedeptid + "' and a.fid='" + userid + "'";
				// 从角色ID查找是否包含userid
				String roleSql = "select a.fid,b.froleid from t_sys_user a inner join  T_USER_ROLE b on a.fid=b.fuserid where b.froleid='"
						+ delegateroleid + "' and a.fid='" + userid + "'";
				// 从用户ID查找是否包含userid;
				String userSql = "select fid from t_sys_user where fid='"
						+ delegateuserid + "' and fid='" + userid + "'";
				// 变量参与人
				String vSql = " select 1 from t_jbpm_vmessage where ftaskid='"
						+ ftaskid + "' and fuserid='" + userid + "'";
				ResultSet departmentRs = jdbcUtil.executeQuery(departmentSql);
				if (departmentRs.next()) {
					returnVal = true;
				} else {
					ResultSet roleRs = jdbcUtil.executeQuery(roleSql);
					if (roleRs.next()) {
						returnVal = true;
					} else {
						ResultSet userRs = jdbcUtil.executeQuery(userSql);
						if (userRs.next()) {
							returnVal = true;
						} else {
							ResultSet vRs = jdbcUtil.executeQuery(vSql);
							if (vRs.next()) {
								returnVal = true;
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVal;
	}

	/**
	 *@author Action
	 *@param userId
	 *@param ftaskId
	 *@param fexecutionId
	 *@date 2014-12-29
	 *@describe 更新变量参与人消息状态
	 */
	public void changeMessageStatus(String userId, String ftaskId,
			String fexecutionId) {
		JDBCUtil jdbc= DataUtil.getJdbcUtil();
		String sql = "update t_jbpm_vmessage set fstatus='1' where ftaskId='"
				+ ftaskId + "' and fuserId='" + userId + "' and fexecutionId='"
				+ fexecutionId + "'";
		jdbc.execute(sql);
	}
}
