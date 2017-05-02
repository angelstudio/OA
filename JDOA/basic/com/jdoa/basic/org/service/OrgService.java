package com.jdoa.basic.org.service;

import java.util.List;

import com.jdoa.baisc.org.model.Organization;

public interface OrgService {
	  public int addOrg(Organization org);
	  public int deleteOrg(String fid);
	  public int updateOrg(Organization org);
	  public List<Organization> queryOrg(Organization org);
	  public List<Organization> queryOrg();
	  public List<Organization> queryOrg(List list);
}
