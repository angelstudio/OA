package com.jdoa.basic.login.model;


public class AuthorityRoleACL {
    private String actionaliasname;	
	private String aclId;
	private String authority;	
    private String roleId;
    private String userId;
    private String orgId;
    private String actionSn;
    private String actionName;
    
	public String getAclId() {
		return aclId;
	}
	public void setAclId(String aclId) {
		this.aclId = aclId;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
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
	public String getActionaliasname() {
		return actionaliasname;
	}
	public void setActionaliasname(String actionaliasname) {
		this.actionaliasname = actionaliasname;
	}
	
}