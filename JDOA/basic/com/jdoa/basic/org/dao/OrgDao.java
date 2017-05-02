package com.jdoa.basic.org.dao;

import java.util.List;

import com.jdoa.baisc.org.model.Organization;

public interface OrgDao {
	 public int addOrg(Organization org);
	  public int deleteOrg(String fid);
	  public int updateOrg(Organization org);
	  public List<Organization> queryOrg(Organization org);
	  public List<Organization> queryOrg();
	  public List<Organization> queryOrg(List list);
}
