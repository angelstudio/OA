package com.jdoa.jbpm.dao;

import com.jdoa.jbpm.model.ProcessInfo;
import com.jdoa.jbpm.model.TempXml;

public interface JBPMDao {
	  public void saveTempXml(TempXml temXml);
	  public void deleteTempXml(String flcid);
	  public ProcessInfo findDataByID(String process_id);
	  public void updateProcess(ProcessInfo processInfo);
	  public void insertProcess(ProcessInfo processInfo);
}
