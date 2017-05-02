package com.jdoa.basic.person.model;

import com.jdoa.baisc.org.model.Organization;

public class Person {
	private String fid;
	private String fname;
	private String fsfzhm;
	private String fssbm;
	private String fbmid;
	private Organization org;
	

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getFsfzhm() {
		return fsfzhm;
	}

	public void setFsfzhm(String fsfzhm) {
		this.fsfzhm = fsfzhm;
	}

	public String getFssbm() {
		return fssbm;
	}

	public void setFssbm(String fssbm) {
		this.fssbm = fssbm;
	}

	public String getFbmid() {
		return fbmid;
	}

	public void setFbmid(String fbmid) {
		this.fbmid = fbmid;
	}

}
