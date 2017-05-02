package com.jdoa.jbpm.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;



import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jbpm.api.Configuration;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;

import com.jdoa.jbpm.model.ProcessInfo;
import com.jdoa.jbpm.model.TempXml;
import com.jdoa.jbpm.service.JBPMService;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.GridDataModel;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;

public class JBPMAction {
	private JBPMService jbpmServiceImpl;
	
	public JBPMService getJbpmServiceImpl() {
		return jbpmServiceImpl;
	}

	public void setJbpmServiceImpl(JBPMService jbpmServiceImpl) {
		this.jbpmServiceImpl = jbpmServiceImpl;
	}
	

	/**
	 * @author Action
	 * @date 2017-04-22
	 * @descrbie 保存流程
	 */
	public void jbpm_saveProcess() {
		String xml = null;
		String name = null;
		boolean isAddnew = false;
		String process_id = null;
		String[] arr_startAction = null;
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader
					.read(ActionUtil.getRequest().getInputStream());
			xml = doc.asXML();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		name = (String) ActionUtil.getRequest().getParameter("name");
		String number = (String) ActionUtil.getRequest().getParameter("number");
		process_id = (String) ActionUtil.getRequest()
				.getParameter("process_id");
		ProcessInfo processInfo = null;
		if (process_id != null && process_id.trim().length() > 0) {
			try {
				processInfo = jbpmServiceImpl.findDataByID(process_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (processInfo == null) {
			isAddnew = true;
			processInfo = new ProcessInfo();
			processInfo.setId(process_id);
		}
		try {

			name = java.net.URLDecoder.decode(java.net.URLDecoder.decode(name,
					"GBK"), "utf-8");

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();

		}
		processInfo.setName(name);
		processInfo.setNumber(number);
		processInfo.setXml(xml);
		processInfo.setStatus("1");
		try {
			if (!isAddnew) {
				jbpmServiceImpl.updateProcess(processInfo);
			} else {
				jbpmServiceImpl.insertProcess(processInfo);
			}
			String id = processInfo.getId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @author Action
	 * @date 2017-04-22
	 * @descrbie 保存临时XML流程
	 */
  public void jbpm_saveProcessTempDataXml(){
	   String xml = null;
		boolean delegate_flag = false;// 定义一个标识，默认值为false
		boolean form_flag = false;
		boolean case_flag = false;
		boolean method_flag = false;
		boolean config_flag = false;
		boolean sql_flag = false;
		boolean notice_flag = false;
		boolean control_flag = false;
		boolean change_flag = false;
		SAXReader reader = new SAXReader();
		Document doc;
		try {
			// 读取流文件
			doc = reader.read(ActionUtil.getRequest().getInputStream());
			xml = doc.asXML();
			// 得到xml文件的根节点
			Element root = doc.getRootElement();
			// 流程id
			String process_id = (String) ActionUtil.getRequest().getParameter(
					"process_id");
			// 流程编码
			String number = (String) ActionUtil.getRequest().getParameter(
					"number");
			// 流程名称
			String name = (String) ActionUtil.getRequest().getParameter("name");

			name = java.net.URLDecoder.decode(java.net.URLDecoder.decode(name,
					"GBK"), "utf-8");
			// 获取创建者ID
			String userId = (String) ActionUtil.getRequest().getSession()
					.getAttribute("userID");
			// 生成ID
			String fid = JDUuid.createID("jbpm0005");
			Date d1 = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fcreateTime = sdf.format(d1);
			TempXml temp = new TempXml();
			temp.setFid(fid);
			temp.setFprocessId(process_id);
			temp.setFname(name);
			temp.setFnumber(number);
			temp.setFcreateTime(fcreateTime);
			temp.setFcreator(userId);
			temp.setFtempXml(xml);
			// 定义sql语句
			StringBuffer deleteSql = new StringBuffer();
			// 删除已存在的数据
			// 执行sql语句
			jbpmServiceImpl.deleteTempXml(process_id);
			jbpmServiceImpl.saveTempXml(temp);
			// 得到根节点下的子节点，返回list
			List<Element> list = root.elements();

			// 遍历list
			for (int i = 0; i < list.size(); i++) {
				Element taskElement = list.get(i);
				if (!notice_flag) {
					if (taskElement.element("notices") != null) {
						if (taskElement.element("notices").elements().size() > 0) {
							notice_flag = true;
						}
					}
				}

				if (!form_flag) {
					if (taskElement.element("forms") != null) {
						if (taskElement.element("forms").elements().size() > 0) {
							// forms节点存在子节点form，flag设置为true
							form_flag = true;
						}
					}
				}

				// 判断是否存在delegates节点，不等于null说明存在
				if (!delegate_flag) {
					if (taskElement.element("delegates") != null) {
						// 判断delegates节点是否存在子节点，大于0说明delegates节点下存在子节点
						if (taskElement.element("delegates").elements().size() > 0) {
							// delegates节点存在子节点delegate，flag设置为true
							delegate_flag = true;
						}
					}
				}

				if (!case_flag) {
					if (taskElement.element("cases") != null) {
						if (taskElement.element("cases").elements().size() > 0) {
							// cases节点存在子节点case，flag设置为true
							case_flag = true;
						}
					}
				}

				if (!method_flag) {
					if (taskElement.element("methods") != null) {
						if (taskElement.element("methods").elements().size() > 0) {
							// methods节点存在子节点method，flag设置为true
							method_flag = true;
						}
					}
				}

				if (!config_flag) {
					if (taskElement.element("configs") != null) {
						if (taskElement.element("configs").elements().size() > 0) {
							// configs节点存在子节点config，flag设置为true
							config_flag = true;
						}
					}
				}

				if (!sql_flag) {
					if (taskElement.element("sqls") != null) {
						if (taskElement.element("sqls").elements().size() > 0) {
							// configs节点存在子节点config，flag设置为true
							sql_flag = true;
						}
					}
				}

				if (!control_flag) {
					if (taskElement.element("controls") != null) {
						if (taskElement.element("controls").elements().size() > 0) {
							control_flag = true;
						}
					}
				}

				if (!change_flag) {
					if (taskElement.element("changes") != null) {
						if (taskElement.element("changes").elements().size() > 0) {
							change_flag = true;
						}
					}
				}
			}

			if (delegate_flag) {// 当flag的值为true时，说明前台请求时传递的xml数据中存在delegates节点，并且delegates节点下存在子节点
				// 调用保存delegates节点的相关数据的方法
				saveDelegateData(root, process_id);
			}

			if (config_flag) {
				saveFormConfigData(root, process_id);
			}

			if (case_flag) {// 当flag的值为true时，说明前台请求时传递的xml数据中存在cases节点，并且cases节点下存在子节点
				// 调用保存case节点的相关数据的方法
				saveCaseData(root, process_id);
			}

			if (notice_flag) {
				saveNoticeData(root, process_id);
			}

			if (method_flag) {
				saveMethodData(root, process_id);
			}

			if (sql_flag) {
				saveSqlData(root, process_id);
			}

			if (form_flag) {// 当flag的值为true时，说明前台请求时传递的xml数据中存在forms节点，并且forms节点下存在子节点
				// 调用保存form节点的相关数据的方法
				saveFormData(root, process_id, null);
			}

			if (control_flag) {
				saveControlData(root, process_id);
			}

			if (change_flag) {
				saveChangeData(root, process_id);
			}

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
  /**
	 * 解析xml，并将解析的数据存储到数据库
	 * 
	 * @author shengbo_zhang
	 * @date 2014-8-1
	 * @param root
	 * @param process_id
	 * @throws SQLException
	 */
	private void saveDelegateData(Element root, String process_id)
			throws SQLException {
		StringBuffer excuteSQL = new StringBuffer();
		excuteSQL.append(
				"begin delete from t_bp_processdelegate where fprocessid = '")
				.append(process_id).append("' ;");
		// 得到根节点下的子节点，返回list
		List<Element> list = root.elements();
		// 遍历list
		for (int i = 0; i < list.size(); i++) {
			Element taskElement = list.get(i);
			if (taskElement.element("delegates") != null) {// 判断是否存在delegates节点
				// 得到delegates节点下的子节点
				List<Element> delegate_list = taskElement.element("delegates")
						.elements();
				if (delegate_list.size() > 0) {// 判断是否存在子节点
					// 遍历delegates节点的子节点delegate的集合
					for (int j = 0; j < delegate_list.size(); j++) {
						// 得到delegate节点的子节点
						List<Element> child = delegate_list.get(j).elements();
						// 关系参与者
						List<Element> relation_list = child.get(1).elements();
						// 定义插入sql语句
						StringBuffer insertSQL = new StringBuffer();
						insertSQL
								.append(
										"insert into t_bp_processdelegate(fid,fprocessid,ftaskid,fdelegateid,variable_value,relation_value,")
								.append(
										"relation_type,relation_taskname,delegateuser,delegaterole,delegatedept,delegateswimlane,delegateuserid,delegateroleid,delegatedeptid,variable_taskid)")
								.append("values('").append(
										JDUuid.createID("jbpm0006")).append(
										"','").append(process_id).append("','")
								.append(taskElement.getName()).append("','")
								.append(
										delegate_list.get(j).attributeValue(
												"id")).append("','")
								.append(child.get(0).attributeValue("value"))
								// 变量参与者
								// 关系参与人
								.append("','").append(
										relation_list.get(0).attributeValue(
												"value"))
								// 关系参与人-关系
								.append("','").append(
										relation_list.get(1).attributeValue(
												"value"))
								// 关系参与人-参与者对应的名称
								.append("','").append(
										relation_list.get(2).attributeValue(
												"value"))
								// 用户
								.append("','").append(
										child.get(2).attributeValue("value"))
								// 角色
								.append("','").append(
										child.get(3).attributeValue("value"))
								// 部门
								.append("','").append(
										child.get(4).attributeValue("value"))
								// 泳道
								.append("','").append(
										child.get(5).attributeValue("id"))
								// 用户ID
								.append("','").append(
										child.get(2).attributeValue("id"))
								// 角色ID
								.append("','").append(
										child.get(3).attributeValue("id"))
								// 部门ID
								.append("','").append(
										child.get(4).attributeValue("id"))
								// 变量参与者-对应的节点ID
								.append("','").append(
										child.get(6).attributeValue("value"))
								.append("') ;");
						excuteSQL.append(insertSQL);

					}
				}
			}
		}
		excuteSQL.append("end; ");
		JDBCUtil jdbc=DataUtil.getJdbcUtil();
		jdbc.execute(excuteSQL.toString());

	}

	/**
	 * 保存form配置的数据 创建日期 2014-9-1 作者 yong_chen 修改时间 下午02:58:35 void
	 * 
	 * @param root
	 * @param process_id
	 */
	private void saveFormConfigData(Element root, String process_id) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DataUtil.getJdbcUtil().getConnection();;
			stmt = conn.createStatement();
			conn.setAutoCommit(false);// 取消自动提交
			stmt.addBatch("delete from m_t_formconfig where fprocessid = '"
					+ process_id + "'");// 根据流程编号删除form配置
			// 得到根节点下的子节点，返回list
			List<Element> list = root.elements();
			// 遍历list
			for (int i = 0; i < list.size(); i++) {
				Element transitionElement = list.get(i);
				String ftaskid = transitionElement.getName();
				if (transitionElement.element("configs") != null) {// 判断是否存在cases节点
					// 得到cases节点下的子节点
					List<Element> config_list = transitionElement.element(
							"configs").elements();
					String fparentid = transitionElement.element("configs")
							.attributeValue("id");
					String fromtable = transitionElement.element("configs")
							.attributeValue("value");
					if (config_list.size() > 0) {// 判断是否存在子节点
						for (int j = 0; j < config_list.size(); j++) {
							String config_id = config_list.get(j)
									.attributeValue("id");// configs的编号
							List<Element> child = config_list.get(j).elements();
							String insert = "insert into m_t_formconfig(fid,fname,faliasname,fonlyread,fviewvisable,"
									+ "fromtable,ftaskid,fnodeid,fprocessid) values('"
									+ JDUuid.createID("99999999")
									+ "','"
									+ child.get(0).attributeValue("value")
									+ "','"
									+ child.get(1).attributeValue("value")
									+ "','"
									+ child.get(2).attributeValue("value")
									+ "','"
									+ child.get(3).attributeValue("value")
									+ "','"
									+ fromtable
									+ "','"
									+ ftaskid
									+ "','"
									+ fparentid
									+ "','"
									+ process_id
									+ "')";
							stmt.addBatch(insert);
						}
					}
				}
			}
			stmt.executeBatch();
			// 如果没有异常，则执行此段代码
			// 提交事务，真正向数据库中提交数据
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();// 将数据回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 保存转换条件中的数据 创建日期 2014-8-11 作者 yong_chen 修改时间 下午04:40:47 void
	 * 
	 * @param root
	 * @param process_id
	 */
	private void saveCaseData(Element root, String process_id) {
		StringBuffer excuteSql = new StringBuffer();
		StringBuffer deleteSql = new StringBuffer();
		StringBuffer insertSql = new StringBuffer();
		deleteSql.append(" delete from t_bp_process_case where fprocessid = '")
				.append(process_id).append("' ;");
		// 得到根节点下的子节点，返回list
		List<Element> list = root.elements();
		// 遍历list
		for (int i = 0; i < list.size(); i++) {
			Element transitionElement = list.get(i);
			if (transitionElement.element("cases") != null) {// 判断是否存在cases节点
				// 得到cases节点下的子节点
				List<Element> case_list = transitionElement.element("cases")
						.elements();
				String fparentid = transitionElement.element("cases")
						.attributeValue("id");
				if (case_list.size() > 0) {// 判断是否存在子节点
					// 遍历forms节点的子节点form的集合
					for (int j = 0; j < case_list.size(); j++) {
						// 得到form节点的子节点
						String case_id = case_list.get(j).attributeValue("id");
						String case_name = case_list.get(j).attributeValue(
								"name");
						List<Element> child = case_list.get(j).elements();
						insertSql
								.append(
										"insert into t_bp_process_case(fid,caseLeftValueBox1,caseCompareCombo1,"
												+ "caseRightValueBox1,caseRelationCombo,caseExpressionBox,fcaseorder,fcasesid,"
												+ "fcasename,fparentid,ftransitionid,fprocessid) values")
								.append("(").append("'").append(
										JDUuid.createID("99999999")).append(
										"',").append("'").append(
										child.get(0).attributeValue("value"))
								.append("',").append("'").append(
										child.get(1).attributeValue("value"))
								.append("',").append("'").append(
										child.get(2).attributeValue("value"))
								.append("',").append("'").append(
										child.get(3).attributeValue("value"))
								.append("',").append("'").append(
										child.get(4).attributeValue("value"))
								.append("',").append("'").append(
										child.get(5).attributeValue("value"))
								.append("',").append("'").append(case_id)
								.append("',").append("'").append(case_name)
								.append("',").append("'").append(fparentid)
								.append("',").append("'").append(
										transitionElement.getName()).append(
										"',").append("'").append(process_id)
								.append("'").append(");");
					}
				}
			}
		}
		excuteSql.append(" begin ").append(deleteSql).append(insertSql).append(
				" end ;");
			DataUtil.getJdbcUtil().execute(excuteSql.toString());
	}

	/**
	 * 保存消息提醒数据
	 * 
	 * @projectName SIP
	 * @packageName com.hnbp.cloud.jbpm.action
	 * @className BPProcessAction.java
	 * @author shengbo_zhang
	 * @date 2014-8-29 上午9:53:05
	 * @return void
	 * @param root
	 * @param process_id
	 * @throws SQLException
	 */
	private void saveNoticeData(Element root, String process_id)
			throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer excuteSQL = new StringBuffer();
		excuteSQL.append(
				"begin delete from m_t_noticeData where f_process_id = '")
				.append(process_id).append("'  ;");
		// 得到根节点下的子节点，返回list
		List<Element> list = root.elements();
		// 遍历list
		for (int i = 0; i < list.size(); i++) {
			Element taskElement = list.get(i);
			if (taskElement.element("notices") != null) {
				List<Element> notice_list = taskElement.element("notices")
						.elements();
				if (notice_list.size() > 0) {
					for (int j = 0; j < notice_list.size(); j++) {
						StringBuffer insertSQL = new StringBuffer();
						List<Element> child = notice_list.get(j).elements();
						List<Element> recipient = child.get(2).elements();
						List<Element> executor = recipient.get(1).elements();
						List<Element> partner = recipient.get(2).elements();
						insertSQL
								.append(
										"insert into m_t_noticeData(fid,f_process_id,f_node_id,f_noticeDesc,f_eventType,f_sponsor,f_executor_value,")
								.append(
										"f_executor_type,f_executor_node,f_partner_value,f_partner_type,f_partner_node,f_noticeUser,")
								.append(
										"f_noticeUserId,f_noticeRole,f_noticeRoleId,f_noticeDept,f_noticeDeptId,f_noticeContext) values('")
								.append(JDUuid.createID("jbpm0012"))
								.append("','")
								.append(process_id)
								.append("','")
								.append(taskElement.getName())
								.append("','")
								.append(child.get(0).attributeValue("value"))
								.append("','")
								.append(child.get(1).attributeValue("value"))
								.append("','")
								.append(
										recipient.get(0)
												.attributeValue("value"))
								.append("','")
								.append(executor.get(0).attributeValue("value"))
								.append("','")
								.append(executor.get(1).attributeValue("value"))
								.append("','")
								.append(executor.get(2).attributeValue("value"))
								.append("','").append(
										partner.get(0).attributeValue("value"))
								.append("','").append(
										partner.get(1).attributeValue("value"))
								.append("','").append(
										partner.get(2).attributeValue("value"))
								.append("','").append(
										recipient.get(3)
												.attributeValue("value"))
								.append("','").append(
										recipient.get(3).attributeValue("id"))
								.append("','").append(
										recipient.get(4)
												.attributeValue("value"))
								.append("','").append(
										recipient.get(4).attributeValue("id"))
								.append("','").append(
										recipient.get(5)
												.attributeValue("value"))
								.append("','").append(
										recipient.get(5).attributeValue("id"))
								.append("','").append(
										child.get(3).attributeValue("value"))
								.append("') ;");
						excuteSQL.append(insertSQL);
					}
				}
			}
		}
		excuteSQL.append("end; ");
		DataUtil.getJdbcUtil().execute(excuteSQL.toString());
	}

	/**
	 * 保存任务策略中的数据 创建日期 2014-8-26 作者 yong_chen 修改时间 下午03:36:21 void
	 * 
	 * @param root
	 * @param process_id
	 */
	private void saveMethodData(Element root, String process_id) {
		StringBuffer excuteSql = new StringBuffer();
		StringBuffer deleteSql = new StringBuffer();
		StringBuffer insertSql = new StringBuffer();

		deleteSql.append(" delete from m_t_methoddata where process_id = '")
				.append(process_id).append("' ;");
		// 得到根节点下的子节点，返回list
		List<Element> list = root.elements();
		// 遍历list
		for (int i = 0; i < list.size(); i++) {
			Element transitionElement = list.get(i);
			if (transitionElement.element("methods") != null) {// 判断是否存在cases节点
				// 得到cases节点下的子节点
				List<Element> method_list = transitionElement
						.element("methods").elements();
				String fparentid = transitionElement.element("methods")
						.attributeValue("id");
				if (method_list.size() > 0) {// 判断是否存在子节点
					String method_id = method_list.get(0).attributeValue("id");
					String method_name = method_list.get(0).attributeValue(
							"name");
					List<Element> child = method_list.get(0).elements();
					insertSql
							.append(
									"insert into m_t_methoddata(fid,methoddistributionradio_value,"
											+ "methodcompleteradio_value,methodnumbox_value,methodpecentbox_value,process_id,node_id) values")
							.append("('").append(JDUuid.createID("99999999"))
							.append("','").append(
									child.get(0).attributeValue("value"))
							.append("','").append(
									child.get(1).attributeValue("value"))
							.append("','");
					if (child.get(1).elements().size() > 0) {// 对是否含有子节点进行判断并添加相应的值
						List<Element> grandSon = child.get(1).elements();
						if ("3".equals(child.get(1).attributeValue("value"))) {
							insertSql.append(
									grandSon.get(0).attributeValue("value"))
									.append("','','");
						} else {
							insertSql.append("','").append(
									grandSon.get(0).attributeValue("value"))
									.append("','");
						}
					} else {
						insertSql.append("','','");
					}
					insertSql.append(process_id).append("','")
							.append(method_id).append("');");
				}
			}
		}
		excuteSql.append(" begin ").append(deleteSql).append(insertSql).append(
				" end ;");
			DataUtil.getJdbcUtil().execute(excuteSql.toString());
	}
	/**
	 * 保存sql脚本的数据 创建日期 2014-9-2 作者 yong_chen 修改时间 上午11:42:55 void
	 * 
	 * @param root
	 * @param process_id
	 */
	private void saveSqlData(Element root, String process_id) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DataUtil.getJdbcUtil().getConnection();
			stmt = conn.createStatement();
			conn.setAutoCommit(false);// 取消自动提交
			stmt.addBatch("delete from m_t_sqldata where fprocessid = '"
					+ process_id + "'");// 根据流程编号删除form配置
			// 得到根节点下的子节点，返回list
			List<Element> list = root.elements();
			// 遍历list
			for (int i = 0; i < list.size(); i++) {
				Element transitionElement = list.get(i);
				String ftaskid = transitionElement.getName();
				if (transitionElement.element("sqls") != null) {// 判断是否存在cases节点
					// 得到cases节点下的子节点
					List<Element> sql_list = transitionElement.element("sqls")
							.elements();
					String fparentid = transitionElement.element("sqls")
							.attributeValue("id");
					String classname = transitionElement.element("classname")
							.attributeValue("value");
					String methodname = transitionElement.element("methodname")
							.attributeValue("value");
					if (sql_list.size() > 0) {// 判断是否存在子节点
						for (int j = 0; j < sql_list.size(); j++) {
							String sql_id = sql_list.get(j)
									.attributeValue("id");// sql节点的id
							List<Element> child = sql_list.get(j).elements();
							String insert = "insert into m_t_sqldata(fid,fclassname,fmethodname,fsqlparamname,"
									+ "ftaskid,fnodeid,fprocessid) values('"
									+ JDUuid.createID("99999999")
									+ "','"
									+ classname
									+ "','"
									+ methodname
									+ "','"
									+ child.get(0).attributeValue("value")
									+ "','"
									+ ftaskid
									+ "','"
									+ fparentid
									+ "','" + process_id + "')";
							stmt.addBatch(insert);
						}
					}
				}
			}
			stmt.executeBatch();
			// 如果没有异常，则执行此段代码
			// 提交事务，真正向数据库中提交数据
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();// 将数据回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 给临时xml中业务表单的信息保存 创建日期 2014-8-1 作者 yong_chen 修改时间 下午02:28:03 void
	 * 
	 * @param root
	 * @param process_id
	 * @throws SQLException
	 */

	private void saveFormData(Element root, String process_id, String fstatus) {

		StringBuffer excuteSql = new StringBuffer();
		StringBuffer deleteSql = new StringBuffer();
		StringBuffer insertSql = new StringBuffer();

		deleteSql.append(" delete from y_form where fprocessid = '").append(
				process_id).append("' ;");

		// 得到根节点下的子节点，返回list
		List<Element> list = root.elements();
		// 遍历list
		for (int i = 0; i < list.size(); i++) {
			Element taskElement = list.get(i);
			String ftaskid = taskElement.getName();
			if (taskElement.element("forms") != null) {// 判断是否存在forms节点
				// 得到forms节点下的子节点
				List<Element> form_list = taskElement.element("forms")
						.elements();
				if (form_list.size() > 0) {// 判断是否存在子节点
					// 遍历forms节点的子节点form的集合
					for (int j = 0; j < form_list.size(); j++) {
						// 得到form节点的子节点
						String form_id = form_list.get(j).attributeValue("id");
						List<Element> child = form_list.get(j).elements();

						insertSql
								.append(
										"insert into y_form(fid,fformid,fbillFormBox,ffacadeCombo,"
												+ "fsqlParamNameBox1,fsqlParamTypeCombo1,fsqlParamValueBox1,factionname,factiontype,ftaskid,fprocessid) values")
								.append("(").append("'").append(
										JDUuid.createID("99999999")).append(
										"',").append("'").append(form_id)
								.append("',").append("'").append(
										child.get(0).attributeValue("value"))
								.append("',").append("'").append(
										child.get(1).attributeValue("value"))
								.append("',").append("'").append(
										child.get(2).attributeValue("value"))
								.append("',").append("'").append(
										child.get(3).attributeValue("value"))
								.append("',").append("'").append(
										child.get(4).attributeValue("value"))
								.append("',").append("'").append(
										child.get(5).attributeValue("value"))
								.append("',").append("'").append(
										child.get(6).attributeValue("value"))
								.append("','").append(ftaskid).append("',")
								.append("'").append(process_id).append("'")

								.append(");");
					}
				}
			}

		}
		excuteSql.append(" begin ").append(deleteSql).append(insertSql).append(
				" end ;");
			DataUtil.getJdbcUtil().execute(excuteSql.toString());
	}
	/**
	 * 保存动态设置的数据
	 * 
	 * @param root
	 * @param process_id
	 */
	private void saveControlData(Element root, String process_id) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DataUtil.getJdbcUtil().getConnection();
			stmt = conn.createStatement();
			conn.setAutoCommit(false);// 取消自动提交
			stmt.addBatch("delete from m_t_controldata where fprocessid = '"
					+ process_id + "'");// 根据流程编号删除form配置
			// 得到根节点下的子节点，返回list
			List<Element> list = root.elements();
			// 遍历list
			for (int i = 0; i < list.size(); i++) {
				Element transitionElement = list.get(i);
				String ftaskid = transitionElement.getName();
				if (transitionElement.element("controls") != null) {// 判断是否存在cases节点
					// 得到cases节点下的子节点
					List<Element> control_list = transitionElement.element(
							"controls").elements();
					if (control_list.size() > 0) {// 判断是否存在子节点
						for (int j = 0; j < control_list.size(); j++) {
							String control_id = control_list.get(j)
									.attributeValue("id");
							List<Element> child = control_list.get(j)
									.elements();
							String insert = "insert into m_t_controldata(fid,fcontrol,fstart,friskid,"
									+ "ftaskid,fprocessid) values('"
									+ JDUuid.createID("99999999")
									+ "','"
									+ child.get(0).attributeValue("value")
									+ "','"
									+ child.get(1).attributeValue("value")
									+ "','"
									+ child.get(2).attributeValue("value")
									+ "','"
									+ ftaskid
									+ "','"
									+ process_id
									+ "')";
							;
							stmt.addBatch(insert);
						}
					}
				}
			}
			stmt.executeBatch();
			// 如果没有异常，则执行此段代码
			// 提交事务，真正向数据库中提交数据
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();// 将数据回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * 保存转换模式的数据
	 * 
	 * @param root
	 * @param process_id
	 */
	private void saveChangeData(Element root, String process_id) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DataUtil.getJdbcUtil().getConnection();
			stmt = conn.createStatement();
			conn.setAutoCommit(false);// 取消自动提交
			stmt.addBatch("delete from m_t_changedata where fprocessid = '"
					+ process_id + "'");// 根据流程编号删除form配置
			// 得到根节点下的子节点，返回list
			List<Element> list = root.elements();
			// 遍历list
			for (int i = 0; i < list.size(); i++) {
				Element transitionElement = list.get(i);
				String ftaskid = transitionElement.getName();
				if (transitionElement.element("changes") != null) {// 判断是否存在changes节点
					// 得到changes节点下的子节点,每个任务节点对应的只有两个值节点
					List<Element> child = transitionElement.element("changes")
							.elements();
					String insert = "insert into m_t_changedata(fid,changeMethodRadio,startMethodRadio,"
							+ "ftaskid,fprocessid) values('"
							+ JDUuid.createID("99999999")
							+ "','"
							+ child.get(0).attributeValue("value")
							+ "','"
							+ child.get(1).attributeValue("value")
							+ "','"
							+ ftaskid + "','" + process_id + "')";
					stmt.addBatch(insert);
				}
			}
			stmt.executeBatch();
			// 如果没有异常，则执行此段代码
			// 提交事务，真正向数据库中提交数据
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();// 将数据回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	/**
	 * @author Action
	 * @date 2017-04-22
	 * @describe 流程部署
	 */
	public void jbpm_deployProcess(){
		String fprocessId = ActionUtil.getRequest().getParameter("processid");
		ProcessInfo processInfo=jbpmServiceImpl.findDataByID(fprocessId);
		String xml = processInfo.getXml();
		String name = processInfo.getName();
		String deploymentId = processInfo.getDeploymentID();
		ProcessListen processListen = (ProcessListen) DataUtil
				.getSpringBean("processListen");
		ProcessEngine processEngine = processListen.getProcessEngine();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		ExecutionService executionService = processEngine.getExecutionService();
		TaskService taskService = processEngine.getTaskService();
		deploymentId = repositoryService.createDeployment()
				.addResourceFromString(name + ".jpdl.xml", xml).deploy();
		processInfo.setDeploymentID(deploymentId);
		processInfo.setStatus("1");
		try {
			jbpmServiceImpl.updateProcess(processInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ProcessDefinition> list = repositoryService
				.createProcessDefinitionQuery().deploymentId(deploymentId)
				.list();
		for (ProcessDefinition pd : list) {
			System.out.println(pd.getDeploymentId());
			// repositoryService.deleteDeploymentCascade(pd.getDeploymentId());
			System.out.println(pd.getId());
		}
		// bpProcessService.
		try {
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @author Action
	 * @date 2017-04-22
	 * @describe 流程反部署
	 */
	public void jbpm_unDeployProcess(){
		String id = ActionUtil.getRequest().getParameter("processid");
		ProcessInfo processInfo = (ProcessInfo) jbpmServiceImpl.findDataByID(id);
		ProcessEngine processEngine = Configuration.getProcessEngine();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		ExecutionService executionService = processEngine.getExecutionService();
		TaskService taskService = processEngine.getTaskService();
		repositoryService.deleteDeployment(processInfo.getDeploymentID());
		processInfo.setStatus("1");
		processInfo.setDeploymentID(null);
		try {
			jbpmServiceImpl.updateProcess(processInfo);
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 /**
     * 通过流程id获得相应的xml数据 创建日期 2014-8-27 作者 yong_chen 修改时间 下午04:11:56 void
     */
    public void selectXmlFromProcess()
    {
        String fid = ActionUtil.getRequest().getParameter("fid");
        String fexecutionId = ActionUtil.getRequest().getParameter(
                "fexecutionId");
        JDBCUtil jdbc=DataUtil.getJdbcUtil();
        String selectSql = "";
        ResultSet rs = null;
        ResultSet xmlrs = null;
        String fcurrentProcessid = "";
        try
        {
            // 流程走完，直接查看整个流程
            if (fid == null || "null".equals(fid) || "".equals(fid)
                    || "undefined".equals(fid))
            {
                // 字段分别是起始节点id、流程id
                selectSql = "select j.fprocessid,j.ftaskid from m_t_taskcomplete t inner join M_T_JBPMSTARTRECORD j on t.fexecutionid=j.fcurrentinstanceprocessid where t.fexecutionid='"
                        + fexecutionId + "'";
                rs = jdbc.executeQuery(selectSql);
                if (rs.next())
                {
                    fcurrentProcessid = rs.getString(1);// 流程id
                }
            }
            else
            {
                // 字段分别是当前任务id、流程实例id、流程id
                selectSql = "select bp.fprocessid from v_task_user task inner join t_bp_processdelegate bp on task.U_ID=bp.ftaskid where task.dbid_="
                        + fid
                        + " and task.EXECUTION_ID_='"
                        + fexecutionId
                        + "' union all "
                        + "select bp.fprocessid from v_task_vuser task inner join t_bp_processdelegate bp on task.U_ID=bp.ftaskid where task.dbid_="
                        + fid
                        + " and task.EXECUTION_ID_='"
                        + fexecutionId
                        + "'";

                rs = jdbc.executeQuery(selectSql);
                if (rs.next())
                {
                    fcurrentProcessid = rs.getString(1);
                }
            }
            // 获得流程的xml字符
            String querySql = "select fxml from T_BP_PROCESS where fid='"
                    + fcurrentProcessid + "'";
            xmlrs = jdbc.executeQuery(querySql);
            String xmlStr = "";
            if (xmlrs.next())
            {
                Clob clob = xmlrs.getClob(1);
                int k = (int) clob.length();
                xmlStr = clob.getSubString(1, k);
            }
            ActionUtil.getResponse().getWriter().write(xmlStr);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (xmlrs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void selectCompletedNodeFromProcess()
    {
    	JDBCUtil jdbc=DataUtil.getJdbcUtil();
        String fid = ActionUtil.getRequest().getParameter("fid");
        String fexecutionId = ActionUtil.getRequest().getParameter(
                "fexecutionId");
        Map map = new HashMap();
        ResultSet rs = null;
        ResultSet endrs = null;
        try
        {
            // 流程走完，直接查看整个流程
            if (fid == null || "null".equals(fid) || "".equals(fid)
                    || "undefined".equals(fid))
            {
                // 根据流程实例ID获得已完成的节点
                String selectSql = "select ftaskid from m_t_jbpmStartRecord  where "
                        + " fcurrentinstanceprocessid='"
                        + fexecutionId
                        + "' union all "
                        + " select a.ftaskid from m_t_auditrecord a left join "
                        + " m_t_taskcomplete t on a.fcurrentinstancetaskid=t.ftaskid where "
                        + " fcurrentinstanceprocessid='" + fexecutionId + "'";

                rs =jdbc.executeQuery(selectSql);
                String ftaskid = null;
                List<String> list = new ArrayList<String>();
                while (rs.next())
                {
                    ftaskid = rs.getString(1);
                    list.add(ftaskid);
                }
                map.put("changeFlag", false);
                map.put("currentNode", "");
                map.put("completedNode", list.toArray());
            }
            else
            {
                String currentNodeId = "";
                String fcurrentinstanceprocessid = "";
                // 字段分别是当前任务id、流程实例id、流程id
                String selectSql = "select to_char(task.U_ID) as U_ID, to_char(task.EXECUTION_ID_) as EXECUTION_ID_, bp.fprocessid from v_task_user task inner join t_bp_processdelegate bp on task.U_ID=bp.ftaskid where task.dbid_="
                        + fid
                        + " and task.EXECUTION_ID_='"
                        + fexecutionId
                        + "'"
                        + " union all "
                        + " select to_char(task.U_ID) as U_ID, to_char(task.EXECUTION_ID_) as EXECUTION_ID_, bp.fprocessid from v_task_vuser task inner join t_bp_processdelegate bp on task.U_ID=bp.ftaskid where task.dbid_="
                        + fid
                        + " and task.EXECUTION_ID_='"
                        + fexecutionId
                        + "'";
                rs = jdbc.executeQuery(selectSql);
                if (rs.next())
                {
                    currentNodeId = rs.getString(1);
                    fcurrentinstanceprocessid = rs.getString(2);
                }
                // 根据流程id获得已完成节点信息
                String querySql = "select ftaskid from m_t_auditrecord where fcurrentinstanceprocessid='"
                        + fcurrentinstanceprocessid + "'";
                // 查询获得起草节点信息
                String startSql = "select ftaskid from m_t_jbpmStartRecord where fcurrentinstanceprocessid='"
                        + fcurrentinstanceprocessid + "'";

                rs = jdbc.executeQuery(querySql);
                endrs = jdbc.executeQuery(startSql);
                Set<String> set = new HashSet<String>();
                while (rs.next())
                {
                    set.add(rs.getString(1));
                }
                if (endrs.next())
                {
                    set.add(endrs.getString(1));
                }
                set.remove(currentNodeId);

                map.put("currentNode", currentNodeId);
                if (set.size() == 0)
                {
                    map.put("completedNode", new String[]
                    { "" });
                }
                else
                {
                    map.put("completedNode", set.toArray());
                }
                map.put("changeFlag", true);
            }
            JSONObject obj = JSONObject.fromObject(map);
            ActionUtil.getResponse().getWriter().write(obj.toString());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (endrs != null)
            {
                try
                {
                    endrs.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    // 火狐浏览器中查看流程的审核信息
    public void selectProcessStatusData()
    {
        HttpServletRequest request = ActionUtil.getRequest();
        String fid = request.getParameter("fid");
        String fexecutionId = request.getParameter("fexecutionId");
        JDBCUtil jdbc=DataUtil.getJdbcUtil();
        // 分页处理
        int starta = Integer.valueOf(request.getParameter("start"));
        int limitb = Integer.valueOf(request.getParameter("limit"));
        limitb = starta + limitb;

        String selectSql = "";
        // 流程走完，直接查看整个流程
        if (fid == null || "null".equals(fid) || "".equals(fid)
                || "undefined".equals(fid))
        {
            // 字段分别是流程实例id、流程id
            selectSql = "select t.fexecutionid fcurrentinstanceprocessid,j.fprocessid fprocessid from m_t_taskcomplete t inner join M_T_JBPMSTARTRECORD j on t.fexecutionid=j.fcurrentinstanceprocessid where t.fexecutionid='"
                    + fexecutionId + "'";
        }
        else
        {
            // 字段分别是当前任务id、流程实例id、流程id
            selectSql = "select to_char(task.EXECUTION_ID_) fcurrentinstanceprocessid,bp.fprocessid fprocessid from v_task_user task inner join t_bp_processdelegate bp on task.U_ID=bp.ftaskid where task.dbid_='"
                    + fid
                    + "' and task.EXECUTION_ID_='"
                    + fexecutionId
                    + "'"
                    + " union all "
                    + "select to_char(task.EXECUTION_ID_) fcurrentinstanceprocessid,bp.fprocessid fprocessid from v_task_vuser task inner join t_bp_processdelegate bp on task.U_ID=bp.ftaskid where task.dbid_='"
                    + fid + "' and task.EXECUTION_ID_='" + fexecutionId + "'";
        }
        ResultSet firstrs = null;
        ResultSet res = null;
        List datalist = new ArrayList();
        int total = 0;
        try
        {
            firstrs = jdbc.executeQuery(selectSql);
            String fprocessid = "";
            String fcurrentinstanceprocessid = "";
            JSONObject json_obj = null;
            if (firstrs.next())
            {
                fprocessid = firstrs.getString("fprocessid");
                fcurrentinstanceprocessid = firstrs
                        .getString("fcurrentinstanceprocessid");
            }

            String querySql = "select fid,fauditman,faudittime,fname,fauditresult,fauditidea from v_start_audit where fcurrentinstanceprocessid='"
                    + fcurrentinstanceprocessid + "'";
            String sql1 = querySql.toString();
            String sqltotal = "select count(*) from (" + sql1 + ")";
            String sql = "select * from(select A.* ,ROWNUM RN FROM (" + sql1
                    + ")A )where RN<=" + limitb + " and RN>" + starta;
            ResultSet totalrs = jdbc.executeQuery(sqltotal);
            res = jdbc.executeQuery(sql);

            if (totalrs.next())
            {
                total = totalrs.getInt(1);
            }

            while (res.next())
            {
                Map rowData = new HashMap();
                rowData.put("fid", res.getString("fid"));
                rowData.put("operator", res.getString("fauditman"));
                rowData.put("opertime", res.getString("faudittime"));
                rowData.put("nodename", res.getString("fname"));
                rowData.put("result", res.getString("fauditresult"));
                rowData.put("fauditidea", res.getString("fauditidea"));
                datalist.add(rowData);
            }

            GridDataModel model = new GridDataModel();
            model.setRows(datalist);
            model.setTotal(total);

            ActionUtil.getResponse().getWriter()
                    .write(JSONObject.fromObject(model).toString());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (res != null)
            {
                try
                {
                    res.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (firstrs != null)
            {
                try
                {
                    firstrs.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
