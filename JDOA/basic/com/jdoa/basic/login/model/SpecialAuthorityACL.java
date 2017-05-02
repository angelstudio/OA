package com.jdoa.basic.login.model;


public class SpecialAuthorityACL {
    private String actionSn;	
	private String actionName;
    private String actionAliasname;	
	private String specialName;
    private String specialaliasname;	
	private String authority;	
	private String aclid;
	private String userid;
	private String roleid;
	private String orgid;
	
	private String[] s2;
	
	public String[] getS2() {
		return s2;
	}
	public void setS2(String[] s2) {
		this.s2 = s2;
	}
	public String getActionSn() {
		return actionSn;
	}
	public void setActionSn(String actionSn) {
		this.actionSn = actionSn;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionAliasname() {
		return actionAliasname;
	}
	public void setActionAliasname(String actionAliasname) {
		this.actionAliasname = actionAliasname;
	}

	public String getSpecialName() {
		return specialName;
	}
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	public String getSpecialaliasname() {
		return specialaliasname;
	}
	public void setSpecialaliasname(String specialaliasname) {
		this.specialaliasname = specialaliasname;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getAclid() {
		return aclid;
	}
	public void setAclid(String aclid) {
		this.aclid = aclid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
    
	
	
}