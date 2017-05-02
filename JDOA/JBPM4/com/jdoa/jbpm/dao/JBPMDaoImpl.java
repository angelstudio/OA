package com.jdoa.jbpm.dao;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jdoa.jbpm.model.ProcessInfo;
import com.jdoa.jbpm.model.TempXml;

public class JBPMDaoImpl implements JBPMDao{
    private SqlMapClient sqlMapClient;
    
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	@Override
	public void deleteTempXml(String flcid) {
		// TODO Auto-generated method stub
		try {
			sqlMapClient.delete("delete_tempXMl", flcid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void saveTempXml(TempXml temXml) {
		// TODO Auto-generated method stub
		try {
			sqlMapClient.insert("bp_tempxml_insert", temXml);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ProcessInfo findDataByID(String processId) {
		// TODO Auto-generated method stub
		ProcessInfo processInfo=null;
		try {
			processInfo=(ProcessInfo) sqlMapClient.queryForObject("T_BP_Process_Select", processId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processInfo;
	}

	@Override
	public void updateProcess(ProcessInfo processInfo) {
		// TODO Auto-generated method stub
		try {
			sqlMapClient.update("T_BP_Process_update_1", processInfo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void insertProcess(ProcessInfo processInfo) {
		// TODO Auto-generated method stub
		try {
			sqlMapClient.update("T_BP_Process_insert", processInfo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
