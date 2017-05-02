package com.jdoa.jbpm.model;

public class TempXml {
	private String fid;// 主键
	private String fprocessId;// 流程ID
	private String fname;// 流程名称
	private String fnumber;// 流程编号
	private String fcreateTime;// 创建时间
	private String fcreator;// 创建者
	private String ftempXml;// 临时xml

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFprocessId() {
		return fprocessId;
	}

	public void setFprocessId(String fprocessId) {
		this.fprocessId = fprocessId;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public String getFcreateTime() {
		return fcreateTime;
	}

	public void setFcreateTime(String fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	public String getFcreator() {
		return fcreator;
	}

	public void setFcreator(String fcreator) {
		this.fcreator = fcreator;
	}

	public String getFtempXml() {
		return ftempXml;
	}

	public void setFtempXml(String ftempXml) {
		this.ftempXml = ftempXml;
	}

}
