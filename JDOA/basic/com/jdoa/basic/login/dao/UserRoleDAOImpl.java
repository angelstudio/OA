package com.jdoa.basic.login.dao;

import java.sql.SQLException;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;


public class UserRoleDAOImpl implements UserRoleDAO {
	
	private SqlMapClient sqlMapClient;
	
	
	public UserRoleDAOImpl(SqlMapClient sqlMapClient) {
        super();
        this.sqlMapClient = sqlMapClient;
    }

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
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
