package com.jdoa.basic.org.service;

import java.util.List;

import com.jdoa.baisc.org.model.Organization;
import com.jdoa.basic.org.dao.OrgDao;

public class OrgServiceImpl implements OrgService {
	private OrgDao orgDao;
    
	public OrgDao getOrgDao() {
		return orgDao;
	}

	public void setOrgDao(OrgDao orgDao) {
		this.orgDao = orgDao;
	}

	@Override
	public int addOrg(Organization org) {
		// TODO Auto-generated method stub
		orgDao.addOrg(org);
		return 0;
	}

	@Override
	public int deleteOrg(String fid) {
		// TODO Auto-generated method stub
		orgDao.deleteOrg(fid);
		return 0;
	}

	@Override
	public List<Organization> queryOrg(Organization org) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Organization> queryOrg() {
		// TODO Auto-generated method stub
		List<Organization> list=orgDao.queryOrg();
		return list;
	}

	@Override
	public List<Organization> queryOrg(List list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateOrg(Organization org) {
		// TODO Auto-generated method stub
		orgDao.updateOrg(org);
		return 0;
	}

}
