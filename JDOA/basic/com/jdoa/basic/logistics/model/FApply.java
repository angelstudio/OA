package com.jdoa.basic.logistics.model;

import java.util.Date;

public class FApply {
	private String fnumber;
	private String fclaims_department; 
	private Date ftime ;
	private String fclaimant ;
	private String fcomment ;
	private String fstate ;
	private String frecipients_state ;
	private int fterms ;
	private String fauditor ;
	private Date faudit_data ;
	private String fgoods_name ;
	private int famount; 
	private String ftype ;
	private String fcreator ;
	private Date fcreation_date ;
	private String fmodifier ;
	private Date fmodification_date ;
	private String frecipients_who ;
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getFclaims_department() {
		return fclaims_department;
	}
	public void setFclaims_department(String fclaims_department) {
		this.fclaims_department = fclaims_department;
	}
	public Date getFtime() {
		return ftime;
	}
	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}
	public String getFclaimant() {
		return fclaimant;
	}
	public void setFclaimant(String fclaimant) {
		this.fclaimant = fclaimant;
	}
	public String getFcomment() {
		return fcomment;
	}
	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}
	public String getFstate() {
		return fstate;
	}
	public void setFstate(String fstate) {
		this.fstate = fstate;
	}
	public String getFrecipients_state() {
		return frecipients_state;
	}
	public void setFrecipients_state(String frecipients_state) {
		this.frecipients_state = frecipients_state;
	}
	public int getFterms() {
		return fterms;
	}
	public void setFterms(int fterms) {
		this.fterms = fterms;
	}
	public String getFauditor() {
		return fauditor;
	}
	public void setFauditor(String fauditor) {
		this.fauditor = fauditor;
	}
	public Date getFaudit_data() {
		return faudit_data;
	}
	public void setFaudit_data(Date faudit_data) {
		this.faudit_data = faudit_data;
	}
	public String getFgoods_name() {
		return fgoods_name;
	}
	public void setFgoods_name(String fgoods_name) {
		this.fgoods_name = fgoods_name;
	}
	public int getFamount() {
		return famount;
	}
	public void setFamount(int famount) {
		this.famount = famount;
	}
	public String getFtype() {
		return ftype;
	}
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}
	public String getFcreator() {
		return fcreator;
	}
	public void setFcreator(String fcreator) {
		this.fcreator = fcreator;
	}
	public Date getFcreation_date() {
		return fcreation_date;
	}
	public void setFcreation_date(Date fcreation_date) {
		this.fcreation_date = fcreation_date;
	}
	public String getFmodifier() {
		return fmodifier;
	}
	public void setFmodifier(String fmodifier) {
		this.fmodifier = fmodifier;
	}
	public Date getFmodification_date() {
		return fmodification_date;
	}
	public void setFmodification_date(Date fmodification_date) {
		this.fmodification_date = fmodification_date;
	}
	public String getFrecipients_who() {
		return frecipients_who;
	}
	public void setFrecipients_who(String frecipients_who) {
		this.frecipients_who = frecipients_who;
	}
	public FApply() {
		super();
	}
	public FApply(String fnumber, String fclaims_department, Date ftime,
			String fclaimant, String fcomment, String fstate,
			String frecipients_state, int fterms, String fauditor,
			Date faudit_data, String fgoods_name, int famount, String ftype,
			String fcreator, Date fcreation_date, String fmodifier,
			Date fmodification_date, String frecipients_who) {
		super();
		this.fnumber = fnumber;
		this.fclaims_department = fclaims_department;
		this.ftime = ftime;
		this.fclaimant = fclaimant;
		this.fcomment = fcomment;
		this.fstate = fstate;
		this.frecipients_state = frecipients_state;
		this.fterms = fterms;
		this.fauditor = fauditor;
		this.faudit_data = faudit_data;
		this.fgoods_name = fgoods_name;
		this.famount = famount;
		this.ftype = ftype;
		this.fcreator = fcreator;
		this.fcreation_date = fcreation_date;
		this.fmodifier = fmodifier;
		this.fmodification_date = fmodification_date;
		this.frecipients_who = frecipients_who;
	}
	@Override
	public String toString() {
		return "FApply [fnumber=" + fnumber + ", fclaims_department="
				+ fclaims_department + ", ftime=" + ftime + ", fclaimant="
				+ fclaimant + ", fcomment=" + fcomment + ", fstate=" + fstate
				+ ", frecipients_state=" + frecipients_state + ", fterms="
				+ fterms + ", fauditor=" + fauditor + ", faudit_data="
				+ faudit_data + ", fgoods_name=" + fgoods_name + ", famount="
				+ famount + ", ftype=" + ftype + ", fcreator=" + fcreator
				+ ", fcreation_date=" + fcreation_date + ", fmodifier="
				+ fmodifier + ", fmodification_date=" + fmodification_date
				+ ", frecipients_who=" + frecipients_who + "]";
	}
	
}
