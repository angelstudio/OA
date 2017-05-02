package com.jdoa.basic.org.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jdoa.baisc.org.model.Organization;

public class OrgDaoImpl implements OrgDao{
    private SqlMapClient sqlMapClient;
    
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	@Override
	public int addOrg(Organization org) {
		// TODO Auto-generated method stub
		try {
			sqlMapClient.insert("OrgAction_addOrg", org);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int deleteOrg(String fid) {
		// TODO Auto-generated method stub
		int i=0;
		try {
			i=sqlMapClient.delete("OrgAction_delOrg", fid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public List<Organization> queryOrg(Organization org) {
		// TODO Auto-generated method stub
		List<Organization>  orgList=new ArrayList<Organization>();
		try {
			orgList=sqlMapClient.queryForList("queryAllOrg");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orgList;
	}

	@Override
	public List<Organization> queryOrg() {
		// TODO Auto-generated method stub
		List<Organization>  orgList=new ArrayList<Organization>();
		try {
			orgList=sqlMapClient.queryForList("queryAllOrg");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orgList;
	}

	@Override
	public List<Organization> queryOrg(List list) {
		// TODO Auto-generated method stub
	
		return null;
	}

	@Override
	public int updateOrg(Organization org) {
		// TODO Auto-generated method stub
		int i=0;
		try {
			 i=sqlMapClient.update("OrgAction_editOrg", org);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

}
