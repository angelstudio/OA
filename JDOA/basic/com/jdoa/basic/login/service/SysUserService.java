package com.jdoa.basic.login.service;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jdoa.basic.login.dao.TSysUserDAOImpl;
import com.jdoa.basic.login.model.TSysUser;

public class SysUserService {

	public TSysUserDAOImpl tSysUserDAO;
    
	//增加用户
	public void addSysUser(TSysUser tSysUser) throws SQLException {
		tSysUserDAO.insert(tSysUser);
	}


  
	//删除用户
	public int delUser(String fId) throws SQLException {
		int del = tSysUserDAO.deleteByExample(fId);
		return del;
	}


	//更新用户状态
	public int updateUserDataStatus(TSysUser tSysUser) throws SQLException {
		return tSysUserDAO.updateUserDataStatus(tSysUser);
	}
	//更新用户
	public int updateUser(TSysUser tSysUser) throws SQLException {
		return tSysUserDAO.updateByExample(tSysUser);
	}
	
	//更新用户密码
	public int updateUserPassword(TSysUser tSysUser) throws SQLException {
		return tSysUserDAO.updateUserPassword(tSysUser);
	}
	
	public int updateUserByUserName(TSysUser tSysUser) throws SQLException {
		return tSysUserDAO.updateUserByUserName(tSysUser);
	}
	
	//根据条件查找用户
	public List searchUser(TSysUser tSysUser) throws SQLException {
		return tSysUserDAO.searchUser(tSysUser);
	}
	
	//查找所有用户
	public List findAllUser(int start,int limit) throws SQLException {
		return tSysUserDAO.selectByExample(start,limit);
	}
	//
	public List findAllUserData() throws SQLException {
		return tSysUserDAO.findAllUserData();
	}
	
	
	public List findAllUser() throws SQLException {
		return tSysUserDAO.selectByExample(null);
	}
   

	//
	//查找所有的用户分类
	public List findSysUserClassification(int start,int limit) throws SQLException {
		List selectList = tSysUserDAO.findSysUserClassification(start,limit);
		return selectList;
	}
	
	
	//查找所有的用户角色
	public List findAllUserRole(int start,int limit) throws SQLException {
		List selectList = tSysUserDAO.selectUserRoleByExample(start,limit);
		return selectList;
	}

	//根据id查找用户
	public TSysUser findByUserId(String fId) throws SQLException {
		TSysUser sSysUser = tSysUserDAO.selectUserById(fId);
		return sSysUser;
	}
    
	//根据用户名查找用户
	public TSysUser findUserByUserName(String username) throws SQLException {
		TSysUser sSysUser = tSysUserDAO.selectUserByName(username);
		return sSysUser;
	}
	
	//查找用户总数
	public int findCountSearchUser(TSysUser sysUser) throws SQLException {
		int count = tSysUserDAO.countSearchUser(sysUser);
		System.out.println(count + "findCountPerson");
		return count;
	}
	

    


	// IsUserValid
	public TSysUser IsUserValid(TSysUser u) throws SQLException {
		TSysUser tSysUer = tSysUserDAO.findUser(u);
		return tSysUer;
	}
	  public List findClassificationByuserid(String fid) throws SQLException {
			List selectList = tSysUserDAO.findClassificationByuserid(fid);
			return selectList;
		  
	  }
	// 设置
	public List<String> findloginAllMenuByuserId(String fuserId) throws SQLException {
		List<String> allMenu = tSysUserDAO.findLoginAllmenu(fuserId);
		return allMenu;
	}
    
	// 设置
	public Set findloginAllBillByuserId(String userid) throws SQLException {
		Set allMenu = tSysUserDAO.findLoginAllBill(userid);
		return allMenu;
	}
	// 设置
	public Set findloginAllMaterialByuserId(String userid) throws SQLException {
		Set allMenu = tSysUserDAO.findLoginAllMaterial(userid);
		return allMenu;
	}
	// 设置权限项
	public Set findloginAllAuthorityByuserId(String userid) throws SQLException {
		Set allMenu = tSysUserDAO.findLoginAllAuthority(userid);
		return allMenu;
	}

	public TSysUserDAOImpl getTSysUserDAO() {
		return tSysUserDAO;
	}

	public void setTSysUserDAO(TSysUserDAOImpl sysUserDAO) {
		tSysUserDAO = sysUserDAO;
	}

}
