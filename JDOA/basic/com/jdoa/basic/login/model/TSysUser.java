package com.jdoa.basic.login.model;

import com.jdoa.basic.person.model.Person;

public class TSysUser {

	private String fid;
	private String fusername;
	private String fpassword;
	private int fstatus;
	private int fusertype;
	private String fpersonid;
	private String fmainUrl;
    private Person person;
    
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFusername() {
		return fusername;
	}

	public void setFusername(String fusername) {
		this.fusername = fusername;
	}

	public String getFpassword() {
		return fpassword;
	}

	public void setFpassword(String fpassword) {
		this.fpassword = fpassword;
	}

	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	public int getFusertype() {
		return fusertype;
	}

	public void setFusertype(int fusertype) {
		this.fusertype = fusertype;
	}

	public String getFpersonid() {
		return fpersonid;
	}

	public void setFpersonid(String fpersonid) {
		this.fpersonid = fpersonid;
	}

	public String getFmainUrl() {
		return fmainUrl;
	}

	public void setFmainUrl(String fmainUrl) {
		this.fmainUrl = fmainUrl;
	}

}