package com.jdoa.baisc.org.model;

public class Organization {
	private String fid;
	private String fname;
	private String fparentId;
	private int flevel;
	private String flongNumber;
	private int fseq;

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

	public String getFparentId() {
		return fparentId;
	}

	public void setFparentId(String fparentId) {
		this.fparentId = fparentId;
	}

	public int getFlevel() {
		return flevel;
	}

	public void setFlevel(int flevel) {
		this.flevel = flevel;
	}

	public String getFlongNumber() {
		return flongNumber;
	}

	public void setFlongNumber(String flongNumber) {
		this.flongNumber = flongNumber;
	}

	public int getFseq() {
		return fseq;
	}

	public void setFseq(int fseq) {
		this.fseq = fseq;
	}

}
