package com.jdoa.jbpm.action;

import java.sql.SQLException;
import java.util.HashMap;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.TaskService;

import com.jdoa.basic.jbpm.model.JBPMKey;
import com.jdoa.jbpm.model.ProcessInfo;

public class ProcessListen {

	private ExecutionService executionService;
	private TaskService taskService;
	private HistoryService historyService;
	private ProcessEngine processEngine;

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public ExecutionService getExecutionService() {
		return executionService;
	}

	public void setExecutionService(ExecutionService executionService) {
		this.executionService = executionService;
	}

	public ProcessListen() {
		// init();
	}

	public void init() {
		// context = new ClassPathXmlApplicationContext(
		// "classpath:applicationContext.xml");
		// setProcessServiceImpl((ProcessServiceImpl) context
		// .getBean("processServiceImpl"));
		// setTbillCreateServiceImpl(getSQLExecuteService());
		setExecutionService(processEngine.getExecutionService());
		setTaskService(processEngine.getTaskService());
		setHistoryService(processEngine.getHistoryService());
	}

	/**
	 * 
	 */
	public ProcessInstance startProcess(HashMap processParam,
			ProcessInfo processInfo) {

		// 根据单据ID 启动工作流引擎。
		ProcessInstance processInstance = executionService
				.startProcessInstanceByKey("Custom", processParam.get("dataID")
						.toString());
		return processInstance;
		// List<ProcessDefinition> pdList = repositoryService
		// .createProcessDefinitionQuery().list();
		// List<ProcessInstance> piList = executionService
		// .createProcessInstanceQuery().list();
	}

	/**
	 * 较色类型的判断是否属于范围内。
	 * 
	 * @param userid
	 * @param assigner
	 * @return
	 */
	public boolean checkRoleForAssign(String userid, String assigner) {
		return false;
	}

	public void startProcess(ProcessInfo processInfo) {
		String deployid = processInfo.getDeploymentID();

	}

	public void completeTask(HashMap param) throws SQLException {
		String taskid = (String) param.get(JBPMKey.JBPM_SPRW);
		taskService = this.getProcessEngine().getTaskService();

		HashMap<String, Object> auditRecordInfo = new HashMap<String, Object>();
		auditRecordInfo.put("ftaskid", param.get(JBPMKey.JBPM_SPRW));
		auditRecordInfo.put("fspjg", param.get(JBPMKey.JBPM_SPJG));
		auditRecordInfo.put("fspyj", param.get(JBPMKey.JBPM_SPYJ));
		auditRecordInfo.put("fspr", param.get(JBPMKey.JBPM_SPR));
		auditRecordInfo.put("fsprid", param.get(JBPMKey.JBPM_SPRID));
		auditRecordInfo.put("fspsj", param.get(JBPMKey.JBPM_SPSJ));
		auditRecordInfo.put("fdataid", param.get(JBPMKey.JBPM_DATAID));
		auditRecordInfo.put("tasktype", param.get(JBPMKey.JBPM_DATAID));
		taskService.completeTask(taskid, auditRecordInfo);
		// 插入工作流审批记录。

		// BaseDataUtil.saveData(auditRecordInfo,
		// "9fd499a297c74555b23b9035308ff9cc10000018", "BASEDATA");
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}
}
