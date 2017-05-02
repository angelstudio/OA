package com.jdoa.jbpm.model;



public class ProcessInfo extends BaseModel {
	// 状态 1.保存、 2 启用。
	private String status;
	// 单据类型ID
	private String billTypeID;
	// 流程xml
	private String xml;
	private String deploymentID;
	// starttype t_action ,t_bill,t_base   任务类型
	private String starttype;
	//开始任务
	private String starttask;
	//参数
	private String params;
	//参与者
	private String assigner;

	public String getBosType() {
		// 获取单据Type方法。以后添加。
		return "JBPM0002";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBillTypeID() {
		return billTypeID;
	}

	public void setBillTypeID(String billTypeID) {
		this.billTypeID = billTypeID;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getDeploymentID() {
		return deploymentID;
	}

	public void setDeploymentID(String deploymentID) {
		this.deploymentID = deploymentID;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getStarttask() {
		return starttask;
	}

	public void setStarttask(String starttask) {
		this.starttask = starttask;
	}

	public String getStarttype() {
		return starttype;
	}

	public void setStarttype(String starttype) {
		this.starttype = starttype;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}
}
