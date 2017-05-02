package com.jdoa.jbpm.service;

import com.jdoa.jbpm.dao.JBPMDao;
import com.jdoa.jbpm.model.ProcessInfo;
import com.jdoa.jbpm.model.TempXml;

public class JBPMServiceImpl implements JBPMService {
    private JBPMDao jbpmDaoImpl;
    
	public JBPMDao getJbpmDaoImpl() {
		return jbpmDaoImpl;
	}

	public void setJbpmDaoImpl(JBPMDao jbpmDaoImpl) {
		this.jbpmDaoImpl = jbpmDaoImpl;
	}

	@Override
	public void deleteTempXml(String flcid) {
		// TODO Auto-generated method stub
		jbpmDaoImpl.deleteTempXml(flcid);
	}

	@Override
	public void saveTempXml(TempXml temXml) {
		// TODO Auto-generated method stub
		jbpmDaoImpl.saveTempXml(temXml);
	}

	@Override
	public ProcessInfo findDataByID(String processId) {
		// TODO Auto-generated method stub
		ProcessInfo processInfo=jbpmDaoImpl.findDataByID(processId);
		return processInfo;
	}

	@Override
	public void updateProcess(ProcessInfo processInfo) {
		// TODO Auto-generated method stub
		jbpmDaoImpl.updateProcess(processInfo);
	}

	@Override
	public void insertProcess(ProcessInfo processInfo) {
		// TODO Auto-generated method stub
		jbpmDaoImpl.insertProcess(processInfo);
	}

}
