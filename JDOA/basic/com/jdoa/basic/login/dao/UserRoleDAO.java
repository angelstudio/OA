package com.jdoa.basic.login.dao;

import java.sql.SQLException;
import java.util.List;


public interface UserRoleDAO {

	void addUserRoles(List userRoleList) throws SQLException ;

	void delUserRolesByIds(List idList)throws SQLException ;

	List findRolesByUseId(String userId)throws SQLException ;
	
}