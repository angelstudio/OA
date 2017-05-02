package com.jdoa.jbpm.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jbpm.api.ExecutionService;

import com.jdoa.basic.jbpm.model.JBPMKey;
import com.jdoa.jbpm.model.MessageInfo;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.Constant;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.GridDataModel;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;

public class QueryAction {
  /**
   * @author Action
   * @date 2017-04-19
   * @describe 查询走流程的表单
   */
  public void queryBill(){
	  StringBuffer sql=new StringBuffer();
	  sql.append("select fid,fname,fbz,fbm from t_jbpm_zlb");
	  JDBCUtil jdbc=DataUtil.getJdbcUtil();
	  ResultSet rs=jdbc.executeQuery(sql.toString());
	  JSONArray arr=new JSONArray();
	  try {
		  while(rs.next()){
			    JSONObject json_obj = new JSONObject();
			    String fid=rs.getString("fid");
			    String fname=rs.getString("fname");
			    String fbm=rs.getString("fbm");
			    String fbz=rs.getString("fbz");
				json_obj.put("fid", fid);
				json_obj.put("fname",fname);
				json_obj.put("fbm", fbm);
				json_obj.put("fbz", fbz);
				arr.add(json_obj);
		  }
		  ActionUtil.getResponse().getWriter().write(arr.toString());
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  /**
   * @author Action
   * @date 2017-04-19
   * @describe 查询表单action
   */
  public void queryAction(){
	  HttpServletRequest request=ActionUtil.getRequest();
	  String fname=request.getParameter("fname");
	  JDBCUtil jdbc=DataUtil.getJdbcUtil();
	  String sql="select fid,factionname,fbm from t_jbpm_actionzlb where fname='"+fname+"'";
	  ResultSet rs=jdbc.executeQuery(sql);
	  JSONArray arr=new JSONArray();
	  try {
		  int total = 0;
		while(rs.next()){
			 total++;
			  JSONObject json=new JSONObject();
			  String fid=rs.getString("fid");
			  String factionname=rs.getString("factionname");
			  String fbm=rs.getString("fbm");
			  json.put("value", factionname);
			  json.put("text", fbm);
			  arr.add(json);
		  }
		JSONObject resultObj = new JSONObject();
		resultObj.put("results", total);
		resultObj.put("rows", arr);
		ActionUtil.getResponse().getWriter().write(resultObj.toString());
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  }
  /**
   * @author Action
   * @date 2017-04-22
   * @describe 查询表单字段信息
   */
  public void queryBillField(){
	  HttpServletRequest request=ActionUtil.getRequest();
	  JDBCUtil jdbc=DataUtil.getJdbcUtil();
	  String formName=request.getParameter("formName");
	  String sql="select fid,fname,fbm,flx from t_jbpm_zdxx where fbdmc='"+formName+"'";
	  ResultSet rs=jdbc.executeQuery(sql);
	  JSONArray arr=new JSONArray();
		try {
			while(rs.next()){
				  JSONObject json=new JSONObject();
				  String fid=rs.getString("fid");
				  String fname=rs.getString("fname");
				  String fbm=rs.getString("fbm");
				  String flx=rs.getString("flx");
				  json.put("fid", fid);
				  json.put("fname", fname);
				  json.put("faliasname", fbm);
				  json.put("ftype", flx);
				  arr.add(json);
			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ActionUtil.getResponse().getWriter().write(arr.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  /**
   * @author Action
   * @dsecribe 查询用户信息
   * @date 2017-04-22
   */
  public void queryUser(){
	  JDBCUtil jdbc=DataUtil.getJdbcUtil();
	  String sql="select a.fid,a.fusername,b.fname from t_sys_user a inner join T_PERSON b on a.fpersonid=b.fid where a.fstatus=0 ";
	  ResultSet rs=jdbc.executeQuery(sql);
	  JSONArray arr=new JSONArray();
		try {
			while(rs.next()){
				  JSONObject json=new JSONObject();
				  String fid=rs.getString("fid");
				  String fusername=rs.getString("fusername");
				  String fname=rs.getString("fname");
				  json.put("fid", fid);
				  json.put("fusername", fusername);
				  json.put("frealname", fname);
				  arr.add(json);
			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ActionUtil.getResponse().getWriter().write(arr.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  /**
   * @author Action
   * @dsecribe 查询角色信息
   * @date 2017-04-22
   */
  public void jbpm_queryRoleData(){
	  JDBCUtil jdbc=DataUtil.getJdbcUtil();
	  String sql="select fid,fname,fbz from t_role ";
	  ResultSet rs=jdbc.executeQuery(sql);
	  JSONArray arr=new JSONArray();
		try {
			while(rs.next()){
				  JSONObject json=new JSONObject();
				  String fid=rs.getString("fid");
				  String fname=rs.getString("fname");
				  String fbz=rs.getString("fbz");
				  json.put("fid", fid);
				  json.put("frolename", fname);
				  json.put("fdescription", fbz);
				  arr.add(json);
			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ActionUtil.getResponse().getWriter().write(arr.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  /**
   * @author Action
   * @dsecribe 查询部门信息
   * @date 2017-04-22
   */
  public void jbpm_queryDeptData(){
	    JDBCUtil jdbc=DataUtil.getJdbcUtil();
	    String sql="select fid,fname from t_org ";
	    ResultSet rs=jdbc.executeQuery(sql);
	    JSONArray arr=new JSONArray();
		try {
			while(rs.next()){
				  JSONObject json=new JSONObject();
				  String fid=rs.getString("fid");
				  String faliasname=rs.getString("fname");
				  json.put("fid", fid);
				  json.put("faliasname", faliasname);
				  arr.add(json);
			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ActionUtil.getResponse().getWriter().write(arr.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  /**
   * @author Action
   * @dsecribe 查询流程信息
   * @date 2017-04-22
   */
  public void jbpm_getProcessList(){
		String sql="select fid,fnumber, fname, faliasName,fcreatetime,fcreator,flastUpdateTime,flastUpdateUser,fstatus,fbillTypeID," +
				"case when fdeploymentID is null then '未部署' else '部署' end as fdeploymentID ,fstarttype,fstarttask,fparams,fassigner " +
				" from T_BP_PROCESS order by fcreatetime desc";	
		try {
			String tab=TableUtil.getNoLimitTabStr(sql);
			ActionUtil.getResponse().getWriter().write(tab);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

  }
  /**
   * @author Action
   * @dsecribe 删除流程信息
   * @date 2017-04-22
   */
  public void deleteProcess(){
	   HttpServletRequest request = ActionUtil.getRequest();
	   String fprocessId = request.getParameter("fprocessId");
	   String sql="delete from T_BP_PROCESS where fid='"+fprocessId+"'";
		try {
		  DataUtil.getJdbcUtil().execute(sql);
		  ActionUtil.getResponse().getWriter().write("ok");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
  /**
	 * 启动流程
	 * 
	 * @param fprocessId
	 * @author taotao 2014-10-11
	 */
	public void startProcess() {
		HttpServletRequest request = ActionUtil.getRequest();
		String fprocessId = request.getParameter("fprocessId");
		String returnVal = Constant.SUCCESS_RETURN_VALUE;
		try {
			String sql=" update t_bp_process set fstatus='2' where fid='"+fprocessId+"'";
			DataUtil.getJdbcUtil().execute(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnVal = Constant.FAILED_RETURN_VALUE;
		}
		try {
			ActionUtil.getResponse().getWriter().write(returnVal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 /**
	 * 启动流程
	 * 
	 * @param fprocessId
	 * @author taotao 2014-10-11
	 */
	public void stopProcess() {
		HttpServletRequest request = ActionUtil.getRequest();
		String fprocessId = request.getParameter("fprocessId");
		String returnVal = Constant.SUCCESS_RETURN_VALUE;
		try {
			String sql=" update t_bp_process set fstatus='1' where fid='"+fprocessId+"'";
			DataUtil.getJdbcUtil().execute(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnVal = Constant.FAILED_RETURN_VALUE;
		}
		try {
			ActionUtil.getResponse().getWriter().write(returnVal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @author Action
	 * @date 2017-04-23
	 * @describe 获取消息数量
	 */
	public void getMessage(){
		JDBCUtil jdbc= DataUtil.getJdbcUtil();
    	String userId=(String) ActionUtil.getRequest().getSession().getAttribute("fuserId");
    	String sql="  select to_char(task.DBID_)DBID_,task.PRIORITY_,task.CREATE_,task.EXECUTION_ID_, " +
    			"to_char(task.U_ID) U_ID,task.USERID ,'1' as fmessagetype,ar.fcurrentinstanceprocessid, " +
    			"st.fman,st.fprocessid,st.fname,st.fbusinessid   from v_task_user task left join m_t_auditrecord ar on  " +
    			"task.EXECUTION_ID_=ar.fcurrentinstanceprocessid and task.U_ID=ar.ftaskid and task.USERID=ar.userid  " +
    			"left join m_t_jbpmStartRecord st on task.EXECUTION_ID_=st.fcurrentinstanceprocessid  " +
    			" where ar.fcurrentinstanceprocessid is null and task.userid='"+userId+"' " +
    			"union all " +
    			" select pm.fdbid DBID_,0 as PRIORITY_ ,pm.ftime CREATE_,pm.fexecutionid EXECUTION_ID_, " +
    			"pm.ftaskid as U_ID,pm.fuserid as USERID,pm.fmessagetype,ar.fcurrentinstanceprocessid,  " +
    			" st.fman,st.fprocessid,st.fname,st.fbusinessid from t_jbpm_vmessage pm left join m_t_auditrecord ar on  " +
    			"pm.fexecutionid=ar.fcurrentinstanceprocessid and pm.ftaskid=ar.ftaskid and pm.fuserid=ar.userid   " +
    			"left join m_t_jbpmStartRecord st on pm.fexecutionid=st.fcurrentinstanceprocessid  " +
    			" where ar.fcurrentinstanceprocessid is null and pm.fuserid='"+userId+"' and pm.fstatus=0";
    	ResultSet rs=jdbc.executeQuery(sql);
    	int total=0;
    	try {
			while(rs.next()){
				total++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ActionUtil.getResponse().getWriter().write(String.valueOf(total));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 /**
	  * @author Action
	  * @date 2017-04-23
	  * @describe 获取消息
	  */
    public void getJBPMMessage(){
    	JDBCUtil jdbc= DataUtil.getJdbcUtil();
    	String userId=(String) ActionUtil.getRequest().getSession().getAttribute("fuserId");
    	String sql="  select to_char(task.DBID_)DBID_,task.PRIORITY_,task.CREATE_,task.EXECUTION_ID_, " +
    			"to_char(task.U_ID) U_ID,task.USERID ,'1' as fmessagetype,ar.fcurrentinstanceprocessid, " +
    			"st.fman,st.fprocessid,st.fname,st.fbusinessid   from v_task_user task left join m_t_auditrecord ar on  " +
    			"task.EXECUTION_ID_=ar.fcurrentinstanceprocessid and task.U_ID=ar.ftaskid and task.USERID=ar.userid  " +
    			"left join m_t_jbpmStartRecord st on task.EXECUTION_ID_=st.fcurrentinstanceprocessid  " +
    			" where ar.fcurrentinstanceprocessid is null and task.userid='"+userId+"' " +
    			"union all " +
    			" select pm.fdbid DBID_,0 as PRIORITY_ ,pm.ftime CREATE_,pm.fexecutionid EXECUTION_ID_, " +
    			"pm.ftaskid as U_ID,pm.fuserid as USERID,pm.fmessagetype,ar.fcurrentinstanceprocessid,  " +
    			" st.fman,st.fprocessid,st.fname,st.fbusinessid from t_jbpm_vmessage pm left join m_t_auditrecord ar on  " +
    			"pm.fexecutionid=ar.fcurrentinstanceprocessid and pm.ftaskid=ar.ftaskid and pm.fuserid=ar.userid   " +
    			"left join m_t_jbpmStartRecord st on pm.fexecutionid=st.fcurrentinstanceprocessid  " +
    			" where ar.fcurrentinstanceprocessid is null and pm.fuserid='"+userId+"' and pm.fstatus=0";
    	List list = new ArrayList();
    	ResultSet rs=jdbc.executeQuery(sql);
    	try {
    	ResultSetMetaData rsmd = rs.getMetaData();
    	int size = rsmd.getColumnCount();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while(rs.next()){
				Map map = new HashMap();
				for (int i = 0; i < size; i++) {
					if (rsmd.getColumnName(i + 1).toUpperCase()
							.equals("CREATE_")) {
						oracle.sql.TIMESTAMP ts = (oracle.sql.TIMESTAMP) rs.getObject("CREATE_");
						Date date=new Date(ts.dateValue().getTime());
						map.put(rsmd.getColumnName(i + 1).toUpperCase(),
								rs.getObject(i + 1) == null ? "" : sdf
										.format(date));
					} else {
						map.put(rsmd.getColumnName(i + 1).toUpperCase(),
								rs.getObject(i + 1) == null ? "" : rs
										.getObject(i + 1));
					}
				}
				String fid = rs.getString("fbusinessid");
				String fname = rs.getString("fname");
				String fprocessid = rs.getString("fprocessid");
				String taskid = rs.getString("u_id");
				String mainInfo = AnalyseNotice(fid, fname, fprocessid, taskid);
				map.put("title", mainInfo);
				list.add(map);
			}
			List<MessageInfo> message_list = combinationMessageByUnTask(list);
			GridDataModel model = new GridDataModel();
			model.setTotal(message_list.size());
			model.setRows(message_list);
			ActionUtil.getResponse().getWriter()
					.write(JSONObject.fromObject(model).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
	 * 获得消息并解析 fid为流程实例ID,fname为业务表单中的任务，fprocessid为流程id，taskid为当前节点id
	 */
	private String AnalyseNotice(String fid, String fname, String fprocessid,
			String taskid) {
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		// 获得原始消息
		String firstNoticeSql = "select n.f_noticecontext,f.factiontype,n.f_noticedesc from m_t_noticeData n inner join y_form f on n.f_process_id=f.fprocessid where n.f_process_id='"
				+ fprocessid + "' and n.f_node_id='" + taskid + "'";
		String noticeContext = "";
		String formType = null;
		List<String> parameterList = new ArrayList<String>();
		List<String> parameterList_ys = new ArrayList<String>();
		try {
			ResultSet rs = jdbcUtil.executeQuery(firstNoticeSql);
			if (rs.next()) {
				noticeContext = rs.getString(1);
				formType = rs.getString(2);
			}

			if (noticeContext != null && !("".equals(noticeContext))) {
				// 对noticeContext进行解析，需要哪些字段信息
				if (noticeContext.contains("#") && noticeContext.contains("[")
						&& noticeContext.contains("]")) {
					// 得到需要解析的参数
					String[] fnoteArray = noticeContext.split("#");
					for (int j = 0; j < fnoteArray.length; j++) {
						String parameter = fnoteArray[j];
						if (!(parameter.contains("[") || parameter
								.contains("]"))) {
							if (!parameterList.contains(parameter)) {
								parameterList.add(parameter.toLowerCase());
								parameterList_ys.add(parameter);
							}
						}
					}
				}

				// 根据单据或基础资料和实例ID获得信息
				// 第一步获得fname属于单据还是基础资料
				String ftablename = null;
				String	tableNameSql = "select ftable from t_jbpm_zlb where fname='"+ fname + "'";
				ResultSet tableRs = jdbcUtil.executeQuery(tableNameSql);
				if (tableRs.next()) {
					ftablename = tableRs.getString("ftable");
				}

				if (ftablename != null && parameterList.size() > 0) {// 如果查不到表名和拼接的字段数组小于等于0，那么没必要替换数据
					StringBuffer selectSql = new StringBuffer();
					selectSql.append("select * from " + ftablename
							+ " where fid='" + fid + "'");
					ResultSet paramrs = jdbcUtil.executeQuery(selectSql
							.toString());
					ResultSetMetaData rsmd = paramrs.getMetaData();
					int size = rsmd.getColumnCount();
					if (paramrs.next()) {
						Map map = new HashMap();
						for (int i = 0; i < size; i++) {
							String filedValue = rsmd.getColumnName(i + 1)
									.toLowerCase();
							if (parameterList.contains(filedValue)) {
								map.put(filedValue,
										paramrs.getObject(i + 1) == null ? ""
												: paramrs.getObject(i + 1)
														.toString());
							}
						}

						for (int j = 0; j < parameterList.size(); j++) {
							String key = parameterList.get(j);
							String key_ys = parameterList_ys.get(j);
							if (map.containsKey(key)) {
								String val = (String) map.get(key);
								String parame = "[#" + key_ys + "#]";
								noticeContext = noticeContext.replace(parame,
										val);
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		noticeContext = noticeContext == null ? "" : noticeContext;// 判断是否为空，为空就置为""
		return noticeContext;
	}
   
	/**
	 * 
	 * combinationMessageByUnTask:(这里用一句话描述这个方法的作用). <br/>
	 * 代办任务重新组装成消息中心格式。
	 * 
	 * @author Administrator
	 * @return
	 * @since JDK 1.6
	 */
	private List combinationMessageByUnTask(List list) {
		List<MessageInfo> message_list = new ArrayList<MessageInfo>();
		MessageInfo messageInfo = null;
		String executionId = null;
		String billid = null;
		ProcessListen processListen=(ProcessListen) DataUtil.getSpringBean("processListen");
		for (int i = 0, n = list.size(); i < n; i++) {
			Map map = new HashMap();
			map = (Map) list.get(i);
			messageInfo = new MessageInfo();
			messageInfo.setId(map.get("DBID_").toString());
			messageInfo.setType(JBPMKey.taskMessage);
			messageInfo.setSender(map.get("FMAN").toString());
			messageInfo.setSendTime(map.get("CREATE_").toString());
			messageInfo.setPrority(map.get("PRIORITY_").toString());
			messageInfo.setTitle(map.get("title").toString());
			messageInfo.setReceiveTime(map.get("CREATE_").toString());
			executionId = map.get("EXECUTION_ID_").toString();
			ExecutionService executionService = processListen
					.getProcessEngine().getExecutionService();
			messageInfo.setBizid((String) executionService.getVariable(
					executionId, JBPMKey.BIZID));
			messageInfo.setBiztype((String) executionService.getVariable(
					executionId, JBPMKey.BIZTYPE));
			messageInfo.setFromtype((String) executionService.getVariable(
					executionId, JBPMKey.FORMTYPE));
			messageInfo.setFtaskId(map.get("U_ID").toString());
			messageInfo.setFexecutionId(executionId);
			messageInfo.setFmessageType(map.get("FMESSAGETYPE").toString());
			message_list.add(messageInfo);
		}
		return message_list;

	}
	/**
	 * @author Action
	 * @date 2017-05-01
	 * @describe 查询流程表单信息
	 * 
	 */
     public void jbpm_Form_List(){
    	 String sql="select fid,fname,fbm,fbz,ftable,furl from t_jbpm_zlb";
    	 String tab=null;
		try {
			tab = TableUtil.getNoLimitTabStr(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	 try {
			ActionUtil.getResponse().getWriter().write(tab);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     /**
 	 * @author Action
 	 * @date 2017-05-01
 	 * @describe 查询流程表单信息
 	 * 
 	 */
     public void jbpm_ZDXX_List(){
    	 String fname=ActionUtil.getRequest().getParameter("fname");
    	 String sql="select fid,fname,fbm,flx,fbdmc from t_jbpm_zdxx where fbdmc='"+fname+"'";
    	 String tab=null;
		try {
			tab = TableUtil.getNoLimitTabStr(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	 try {
			ActionUtil.getResponse().getWriter().write(tab);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     /**
  	 * @author Action
  	 * @date 2017-05-01
  	 * @describe 查询流程表单信息
  	 * 
  	 */
      public void jbpm_ANXX_List(){
     	 String fname=ActionUtil.getRequest().getParameter("fname");
     	 String sql="select fid,fname,fanid,fanclass,fanff,fbdmc,fpx,fsfkj from t_jbpm_anxx where fbdmc='"+fname+"' order by fpx";
     	 String tab=null;
 		try {
 			tab = TableUtil.getNoLimitTabStr(sql);
 		} catch (SQLException e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
     	 try {
 			ActionUtil.getResponse().getWriter().write(tab);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
      }
      /**
    	 * @author Action
    	 * @date 2017-05-01
    	 * @describe 查询流程表单信息
    	 * 
    	 */
      public void jbpm_QQPZXX_List(){
    	  String fname=ActionUtil.getRequest().getParameter("fname");
      	 String sql="select fid,fname,FactionName,fbm from t_jbpm_actionzlb where fname='"+fname+"' ";
      	 String tab=null;
  		try {
  			tab = TableUtil.getNoLimitTabStr(sql);
  		} catch (SQLException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
      	 try {
  			ActionUtil.getResponse().getWriter().write(tab);
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
    	  
      }
      /**
  	 * @author Action
  	 * @date 2017-05-01
  	 * @describe 查询URL信息
  	 * 
  	 */
      public void jbpm_Form_URL(){
    	  String fname=ActionUtil.getRequest().getParameter("fname");
    	  JDBCUtil jdbc= DataUtil.getJdbcUtil();
    	  String sql="select fid,fname,fbm,furl from t_jbpm_zlb where fname='"+fname+"'";
    	  ResultSet rs=jdbc.executeQuery(sql);
          JSONObject json=new JSONObject();    	  
    	  try {
			while(rs.next()){
				json.put("fname",rs.getString("fname"));
				json.put("fbm",rs.getString("fbm"));
				json.put("furl",rs.getString("furl"));
			}
			ActionUtil.getResponse().getWriter().write(json.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      /**
    	 * @author Action
    	 * @date 2017-05-01
    	 * @describe 查询按钮信息
    	 * 
    	 */
      public void getAllControlButInfo(){
    	  String ftablename = ActionUtil.getRequest().getParameter("ftablename");
          JSONArray json_arr = new JSONArray();
          String selectSql = "select fname fbuttonname,fsfkj fbuttonview,fid from t_jbpm_anxx where fbdmc='"
                  + ftablename + "'";
          try
          {
        	  ResultSet  rs = DataUtil.getJdbcUtil().executeQuery(selectSql);
              List list = null;
              while (rs.next())
              {
                  list = new ArrayList();
                  list.add(rs.getString(1));
                  list.add("1".equals(rs.getString(2)) ? 0 : 1);// 针对是否可见取反
                  list.add(rs.getString(3));
                  json_arr.add(list);
              }
              ActionUtil.getResponse().getWriter().write(json_arr.toString());
          }
          catch (SQLException e)
          {
              e.printStackTrace();
          }
          catch (IOException e)
          {
              e.printStackTrace();
          }
      }
      /**
  	 * @author Action
  	 * @date 2017-05-01
  	 * @describe 查询字段信息
  	 * 
  	 */
      public void getFormTableInfo(){
    	  String ftablename = ActionUtil.getRequest().getParameter("ftablename");
          JSONArray json_arr = new JSONArray();
          String   selectSql = "select fname,fbm faliasname,fsfzd fonlyread,fsfkj fviewvisable from t_jbpm_zdxx where fbdmc='" + ftablename + "' ";
          try
          {
        	  ResultSet   rs = DataUtil.getJdbcUtil().executeQuery(selectSql);
              List list = null;
              while (rs.next())
              {
                  list = new ArrayList();
                  list.add(rs.getString(1));
                  list.add(rs.getString(2));
                  list.add(rs.getString(3));
                  list.add(rs.getString(4));
                  json_arr.add(list);
              }
              ActionUtil.getResponse().getWriter().write(json_arr.toString());
          }
          catch (SQLException e)
          {
              e.printStackTrace();
          }
          catch (IOException e)
          {
              e.printStackTrace();
          }
      }
      /**
    	 * @author Action
    	 * @date 2017-05-01
    	 * @describe 追加按钮信息
    	 * 
    	 */
      public void jbpm_Add_FORM(){
    	  HttpServletRequest request=ActionUtil.getRequest();
    	  JDBCUtil jdbc= DataUtil.getJdbcUtil();
    	  String taskId=request.getParameter("taskid");
    	  String billType=request.getParameter("billType");
    	  String id=request.getParameter("id");
    	  String buttonSql = "select friskId from m_t_controldata where ftaskId='"+ taskId + "' and fstart=1 ";
    	  ResultSet butRs = jdbc.executeQuery(buttonSql);
    	  StringBuffer workButInfo=new StringBuffer();
			String strId = "";
			try {
				while (butRs.next()) {
					strId = strId + "'" + butRs.getString("friskId") + "',";
				}
			String fid = "";
			if (!StringUtil.isEmpty(strId)) {
				fid = strId.substring(0, strId.lastIndexOf(","));
			} else {
				fid = "''";
			}
			String butSqlInfo = "select fid,fname,fanid,fanclass,fanff,fbdmc,fpx,fsfkj from t_jbpm_anxx where fid in("
					+ fid + ") order by fpx";
			ResultSet butInfoRs = jdbc.executeQuery(butSqlInfo);
			while (butInfoRs.next()) {
				workButInfo.append("<button id='").append(
						butInfoRs.getString("fanid")).append("' ")
						.append("class='").append(
								butInfoRs.getString("fanclass"))
						.append("' ").append("onclick='").append(
								butInfoRs.getString("fanff"))
						.append("()' ").append(">").append(
								butInfoRs.getString("fname")).append(
								"</button>");
			}
			//字段赋值
			String formSql="select ftable from t_jbpm_zlb where fname='"+billType+"'";
			ResultSet formRs=jdbc.executeQuery(formSql);
			String ftable=null;
			while(formRs.next()){
				ftable=formRs.getString("ftable");
			}
			String fieldSql="select fname,flx  from t_jbpm_zdxx where fbdmc='"+billType+"'";
			ResultSet fieldRs=jdbc.executeQuery(fieldSql);
			StringBuffer val=new StringBuffer();
			HashMap map=new HashMap();
			while(fieldRs.next()){
				String fname=fieldRs.getString("fname");
				String flx=fieldRs.getString("flx");
				val.append(fname).append(",");
				map.put(fname, flx);
			};
			val.delete(val.length()-1, val.length());
			if(val.length()<1){
				val.append("1");
			}
			String valSql="select "+val.toString()+" from "+ftable+" where fid='"+id+"'";
			ResultSet rs=jdbc.executeQuery(valSql.toString());
			JSONObject json=new JSONObject();
			if(rs.next()){
				for(Iterator iterator=map.keySet().iterator();iterator.hasNext();){
					String key=(String) iterator.next();
					String flx=(String) map.get(key);
					String fieldVal=rs.getString(key);
					json.put(key, fieldVal);
				}
			}
			json.put("workButInfo", workButInfo.toString());
			ActionUtil.getResponse().getWriter().write(json.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      };
}
