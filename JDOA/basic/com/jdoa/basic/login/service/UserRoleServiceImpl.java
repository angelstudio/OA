package com.jdoa.basic.login.service;

import java.sql.SQLException;
import java.util.List;

import com.jdoa.basic.login.dao.UserRoleDAO;

public class UserRoleServiceImpl implements UserRoleService {

	private UserRoleDAO userRoleDAO;
	
	


	public UserRoleDAO getUserRoleDAO() {
		return userRoleDAO;
	}


	public void setUserRoleDAO(UserRoleDAO userRoleDAO) {
		this.userRoleDAO = userRoleDAO;
	}


	public void addUserRoles(List userRoleList) throws SQLException {
		// TODO Auto-generated method stub
		
	}


	public void delUserRolesByIds(List idList) throws SQLException {
		// TODO Auto-generated method stub
		
	}


	public List findRolesByUseId(String userId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
