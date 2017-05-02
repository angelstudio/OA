package com.jdoa.basic.login.service;

import java.sql.SQLException;
import java.util.List;

public interface UserRoleService {

	public void addUserRoles(List userRoleList) throws SQLException;
	
	public void delUserRolesByIds(List idList) throws SQLException;
	
	public List findRolesByUseId(String userId) throws SQLException;
	
}
