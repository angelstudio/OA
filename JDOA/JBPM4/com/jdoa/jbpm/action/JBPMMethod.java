package com.jdoa.jbpm.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessEngine;

import net.sf.json.JSONObject;

import com.jdoa.basic.jbpm.model.JBPMKey;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.ReflexUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TimeUtil;

public class JBPMMethod {
   /**
    * @author Action
    * @date 2017-04-23
    * @describe 提交启动流程
    */
   public void jbpmSubmit(){
	   JDBCUtil jdbc=DataUtil.getJdbcUtil();
	   HttpServletRequest request=ActionUtil.getRequest();
	   String formName=request.getParameter("type");
	   String fid=request.getParameter("id");
	   String tabSql="select ftable from t_jbpm_zlb where fname='"+formName+"'";
	   ResultSet rs=jdbc.executeQuery(tabSql);
	   String tab=null;
	   try {
		while(rs.next()){
			   tab=rs.getString("ftable");
		   }
		String jdbpmFlag=null;
		if(!StringUtil.isEmpty(tab)&&!StringUtil.isEmpty(fid)){
			String updateSql="update "+tab+" set fstatus=2  where fid='"+fid+"'";
			jdbc.execute(updateSql);
			jdbpmFlag = JBPMKey.JBPM_SUCCESS;
		}else{
			jdbpmFlag=JBPMKey.JBPM_FAILD;
		}
		request.setAttribute(fid, jdbpmFlag);
		ActionUtil.getResponse().getWriter().write(jdbpmFlag);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   
   /**
	 * jbpmAudit:(这里用一句话描述这个方法的作用). <br/>
	 * 工作流审批
	 * @author Administrator
	 * @throws Exception 
	 * @since JDK 1.6
	 */
	public void jbpmAudit() throws Exception {
		HttpServletRequest request=ActionUtil.getRequest();
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String spjg = request.getParameter("spjg");
		String spyj = request.getParameter("spyj");
		String DBID_ = request.getParameter("currentTaskId");
		String spsj = TimeUtil.formatDateToString(new Date(), "yy-MM-dd HH:mm:ss");
		HttpSession session = ActionUtil.getRequest().getSession();
		String userid=session.getAttribute("fuserId").toString();
		String username =session.getAttribute("fusername").toString() ;
		String fid = JDUuid.createID("jbpmspjg");
		Date d1 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(d1);
		try {			
			String taskSql = "select to_char(task.U_ID) as U_ID,to_char(task.EXECUTION_ID_) as EXECUTION_ID_,bp.fprocessid from v_task_user task inner join t_bp_processdelegate bp on task.U_ID=bp.ftaskid where task.DBID_="
					+ DBID_
					+ " union all select  to_char(task.U_ID) as U_ID,to_char(task.EXECUTION_ID_) as EXECUTION_ID_,bp.fprocessid  from v_task_vuser task inner join  t_bp_processdelegate bp on task.U_ID=bp.ftaskid where task.DBid_="
					+ DBID_;
			ResultSet taskRs = jdbcUtil.executeQuery(taskSql);
			// 节点ID
			String ftaskId = null;
			// 当前实例ID
			String fcurrentInstanceProcessId = null;
			// 流程ID
			String fprocessId = null;
			if (taskRs.next()) {
				ftaskId = taskRs.getString("U_ID");
				fcurrentInstanceProcessId = taskRs.getString("EXECUTION_ID_");
				fprocessId = taskRs.getString("fprocessid");
			}
			StringBuffer sql = new StringBuffer();
			sql.append(
					"insert into m_t_auditRecord(fid,fauditResult,fauditIdea,fauditMan,fauditTime,")
					.append("fbusinessId,fname,ftaskId,fprocessId,fcurrentInstanceTaskId,fcurrentInstanceProcessId,userid)")
					.append("values('").append(fid).append("','").append(spjg)
					.append("','").append(spyj).append("','").append(username)
					.append("','").append(date).append("','").append(id)
					.append("','").append(type).append("','").append(ftaskId)
					.append("','").append(fprocessId).append("','")
					.append(DBID_).append("','")
					.append(fcurrentInstanceProcessId).append("','")
					.append(userid).append("')");
			jdbcUtil.execute(sql.toString());
			ProcessEngine processEngine = ((ProcessEngine) DataUtil
					.getSpringBean("processEngine"));
			// 获取流程参数的map集合
			System.out.println(fcurrentInstanceProcessId);
			Set set = processEngine.getExecutionService().getVariableNames(
					fcurrentInstanceProcessId);
			HashMap map = (HashMap) processEngine.getExecutionService()
					.getVariables(fcurrentInstanceProcessId, set);
			map.put(JBPMKey.JBPM_SPJG, spjg);
			map.put(JBPMKey.JBPM_SL_ID, fcurrentInstanceProcessId);
			if (spjg.equals("agree")) {
				String methodSql = "select md.methodcompleteradio_value,md.methodnumbox_value,md.methodpecentbox_value from m_t_methoddata md where md.node_id='"
						+ ftaskId + "'";
				ResultSet methodRs = jdbcUtil.executeQuery(methodSql);
				// 1.完成策略值
				String methodcompleteradio_value = "";
				// 2.当策略值为3时的完成个数
				int methodnumbox_value = 0;
				// 3.当策略值为4时完成百分比
				Double methodpecentbox_value = 0.0;
				if (methodRs.next()) {
					methodcompleteradio_value = methodRs
							.getString("methodcompleteradio_value");
					methodnumbox_value = methodRs.getInt("methodnumbox_value");
					methodpecentbox_value = methodRs
							.getDouble("methodpecentbox_value");
				}
				// 1表是这个节点只需任意一人则completeTask
				// 2表示这个节点需要所有人完成审批则completeTask
				// 3表示这个节点需要明确个数人完成审批则completeTask
				// 4表示这个节点需要完成一定的比例则completeTask
				if (methodcompleteradio_value.equals("1")) {
					processEngine.getTaskService().completeTask(DBID_);
					changeMessageStatus(userid, ftaskId,
							fcurrentInstanceProcessId);
					taskPostpositionMethod(map, ftaskId);
				} else if (methodcompleteradio_value.equals("2")) {
					List<String> listUserId = getUserId(fprocessId, ftaskId,
							fcurrentInstanceProcessId);
					String numberSql = "select count(ar.userid) fnumber from m_t_auditRecord ar where ar.fcurrentInstanceProcessId='"
							+ fcurrentInstanceProcessId
							+ "' and ar.ftaskid='"
							+ ftaskId + "'";
					ResultSet numberRs = jdbcUtil.executeQuery(numberSql);
					int fnumber = 0;
					if (numberRs.next()) {
						fnumber = numberRs.getInt(1);
					}
					// 当该流程实例在该节点拥有的记录和listUserId.size相等完成该节点
					if (fnumber == listUserId.size()) {
						processEngine.getTaskService().completeTask(DBID_);
						changeMessageStatus(userid, ftaskId,
								fcurrentInstanceProcessId);
						taskPostpositionMethod(map, ftaskId);
					}
					
					//记录审批意见
					//终结参与人
				} else if (methodcompleteradio_value.equals("3")) {
					String numberSql = "select count(ar.userid) fnumber from m_t_auditRecord ar where ar.fcurrentInstanceProcessId='"
							+ fcurrentInstanceProcessId
							+ "' and ar.ftaskid='"
							+ ftaskId + "'";
					ResultSet numberRs = jdbcUtil.executeQuery(numberSql);
					int fnumber = 0;
					if (numberRs.next()) {
						fnumber = numberRs.getInt(1);
					}
					// 当完成个数相等时则完成该节点
					if (methodnumbox_value == fnumber) {
						processEngine.getTaskService().completeTask(DBID_);
						changeMessageStatus(userid, ftaskId,
								fcurrentInstanceProcessId);
						taskPostpositionMethod(map, ftaskId);
					}
				} else if (methodcompleteradio_value.equals("4")) {
					List<String> listUserId = getUserId(fprocessId, ftaskId,
							fcurrentInstanceProcessId);
					String numberSql = "select count(ar.userid) fnumber from m_t_auditRecord ar where ar.fcurrentInstanceProcessId='"
							+ fcurrentInstanceProcessId
							+ "' and ar.ftaskid='"
							+ ftaskId + "'";
					ResultSet numberRs = jdbcUtil.executeQuery(numberSql);
					int fnumber = 0;
					if (numberRs.next()) {
						fnumber = numberRs.getInt(1);
					}
					BigDecimal b1 = new BigDecimal(fnumber);
					BigDecimal b2 = new BigDecimal(listUserId.size());
					double dou = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP)
							.doubleValue() * 100;
					if (dou >= methodpecentbox_value) {
						processEngine.getTaskService().completeTask(DBID_);
						changeMessageStatus(userid, ftaskId,
								fcurrentInstanceProcessId);
						taskPostpositionMethod(map, ftaskId);
					}
				} else {
					processEngine.getTaskService().completeTask(DBID_);
					changeMessageStatus(userid, ftaskId,
							fcurrentInstanceProcessId);
					taskPostpositionMethod(map, ftaskId);
				}
			} else {
				ExecutionService executionService = processEngine
						.getExecutionService();
				executionService.endProcessInstance(fcurrentInstanceProcessId,
						"cancle");
				invalidBillWhenCancel(map);// 当此流程作废时，对应的当前单据也作废
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
    
	/**
	 * @author Action
	 * @param userId
	 * @param ftaskId
	 * @param fexecutionId
	 * @date 2014-12-29
	 * @describe 更新变量参与人消息状态
	 */
	public void changeMessageStatus(String userId, String ftaskId,
			String fexecutionId) {
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		String sql = "update t_jbpm_vmessage set fstatus='1' where ftaskId='"
				+ ftaskId + "' and fuserId='" + userId + "' and fexecutionId='"
				+ fexecutionId + "'";
			jdbc.execute(sql);
	}
	
	/**
	 * taotao 2014-09-10 节点后置方法
	 * 
	 * @param ftaskId
	 */
	private void taskPostpositionMethod(HashMap map, String ftaskId) {
		try {
			JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
			String sql = "select fclassname,fmethodname,fsqlparamname from m_t_sqldata where ftaskid='"
					+ ftaskId + "'";
			ResultSet rs = jdbcUtil.executeQuery(sql);
			String fclassname = null;
			String fmethodname = null;
			while (rs.next()) {
				fclassname = rs.getString("fclassname");
				fmethodname = rs.getString("fmethodname");
			}
			if (!StringUtil.isEmpty(fclassname)) {
				Object obj = ReflexUtil.getClassObj(fclassname);
				if (obj != null) {
					ReflexUtil.invokeMethod(obj, fmethodname,
							new Object[] { map });
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 根据流程Id和节点ID获取该节点的userId集合 taotao 2014-08-29
	 * 
	 * @param fprocessId
	 * @param ftaskId
	 * @oaram fcurrentInstanceProcessId; 变量参与人
	 * @return
	 */
	public List<String> getUserId(String fprocessId, String ftaskId,
			String fcurrentInstanceProcessId) {
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String userSql = "select bp.delegateuserid,bp.delegateroleid,bp.delegatedeptid from t_bp_processdelegate bp where bp.fprocessid='"
				+ fprocessId + "' and bp.ftaskid='" + ftaskId + "'";
		List<String> listUserId = null;
		try {
			ResultSet userRs = jdbcUtil.executeQuery(userSql);
			// 每条记录只有一个id不为空
			// 声明三个集合存储ID
			listUserId = new ArrayList<String>();
			List<String> listRoleId = new ArrayList<String>();
			List<String> listDeptId = new ArrayList<String>();
			List<String> listvUserId = new ArrayList<String>();
			while (userRs.next()) {
				if (!StringUtil.isEmpty(userRs.getString("delegateuserid"))) {
					listUserId.add(userRs.getString("delegateuserid"));
				} else if (!StringUtil.isEmpty(userRs
						.getString("delegateroleid"))) {
					listRoleId.add(userRs.getString("delegateroleid"));
				} else if (!StringUtil.isEmpty(userRs
						.getString("delegatedeptid"))) {
					listDeptId.add(userRs.getString("delegatedeptid"));
				}
			}
			if (fcurrentInstanceProcessId != null) {
				// 变量参与人列表
				String sql = "select fuserid from t_jbpm_vmessage where fexecutionid='"
						+ fcurrentInstanceProcessId
						+ "' and ftaskid='"
						+ ftaskId + "' ";
				ResultSet rs = jdbcUtil.executeQuery(sql);

				while (rs.next()) {
					listvUserId.add(rs.getString("fuserid"));
				}
			}
			// 无论是角色id还部门ID最后只能化为最小单元userid 来比较
			// 角色化最小单元userid
			List<String> listRoleId_UserId = new ArrayList<String>();
			if (listRoleId.size() > 0) {
				// 拼装角色条件
				String roleStr = "'',";
				for (int i = 0; i < listRoleId.size(); i++) {
					String roleId = listRoleId.get(i).toString();
					roleStr = roleStr + "'" + roleId + "',";
				}
				// 从条件中取出不重复的userid
				roleStr = roleStr.substring(0, roleStr.lastIndexOf(","));
				String roleUserSql = "select userid from t_user_role where roleid in("
						+ roleStr + ")";
				ResultSet roleUserRs = jdbcUtil.executeQuery(roleUserSql);
				while (roleUserRs.next()) {
					if (!listRoleId_UserId.contains(roleUserRs
							.getString("userid"))
							&& !listUserId.contains(roleUserRs
									.getString("userid"))) {
						listRoleId_UserId.add(roleUserRs.getString("userid"));
					}
				}
			}
			// 部门最小单元userid
			List<String> listDeptId_UserId = new ArrayList<String>();
			if (listDeptId.size() > 0) {
				// 拼装部门条件
				String deptStr = "'',";
				for (int i = 0; i < listDeptId.size(); i++) {
					String deptId = listDeptId.get(i).toString();
					deptStr = deptStr + "'" + deptId + "',";
				}
				deptStr = deptStr.substring(0, deptStr.lastIndexOf(","));
				// 从条件中取出不重复的userid;
				String deptUserSql = "select a.fid from t_sys_user a inner join t_sys_person b  on a.fpersonid=b.fid  where  b.fdeptid in("
						+ deptStr + ")";
				ResultSet deptUserRs = jdbcUtil.executeQuery(deptUserSql);
				while (deptUserRs.next()) {
					if (!listDeptId_UserId.contains(deptUserRs
							.getString("userid"))
							&& !listUserId.contains(deptUserRs
									.getString("userid"))) {
						listDeptId_UserId.add(deptUserRs.getString("userid"));
					}
				}
			}
			// 合并list
			listUserId.addAll(listRoleId_UserId);
			listUserId.addAll(listDeptId_UserId);
			if (listvUserId.size() > 0) {
				listUserId.addAll(listvUserId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listUserId;
	}
	
	/**
	 * 当流程作废时当前单据也作废
	 * 
	 * @param map
	 */
	public void invalidBillWhenCancel(HashMap map) {
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fbizId = map.get("BIZID").toString();
		String fbizName = map.get("BIZTYPE").toString();
		// b_end 为true表示当前节点为最后节点 则返写单据状态
		String tableSql = null;
		String ftablename = null;
		String bizSql = null;
		// fformType=1表示单据，否则基础资料
		try {
			tableSql = "select ftable  from t_jbpm_zlb where fname='"
						+ fbizName + "'";
            ResultSet rs=jdbcUtil.executeQuery(tableSql);
            if(rs.next()){
            	ftablename=rs.getString("ftable");
            }
			bizSql = "update " + ftablename + " set fstatus='-1' where fid='"
					+ fbizId + "'";
			jdbcUtil.execute(bizSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 流程处理前的判断 taotao 2014-09-02
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void beforeDeal() throws SQLException, IOException {
		String returnVal = "ok";
		String id = ActionUtil.getRequest().getParameter("taskid");
		String userId = ActionUtil.getRequest().getSession()
				.getAttribute("fuserId").toString();
		String sql = "select task.EXECUTION_ID_,task.U_ID from v_task_user task where task.DBID_="
				+ id;
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		ResultSet rs = jdbcUtil.executeQuery(sql);
		String EXECUTION_ID_ = null;
		String U_ID = null;
		if (rs.next()) {
			EXECUTION_ID_ = rs.getString("EXECUTION_ID_");
			U_ID = rs.getString("U_ID");
		}
		String auditSql = "select 1 from  m_t_auditrecord  where userid='"
				+ userId + "' and ftaskid='" + U_ID
				+ "' and fcurrentinstanceprocessid='" + EXECUTION_ID_ + "'";
		ResultSet auditRs = jdbcUtil.executeQuery(auditSql);
		if (auditRs.next()) {
			returnVal = "error";
		}
		PrintWriter writer = ActionUtil.getResponse().getWriter();
		writer.write(returnVal);
	}
}
