package com.jdoa.basic.login.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jdoa.basic.login.model.AuthorityRoleACL;
import com.jdoa.basic.login.model.SpecialAuthorityACL;
import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.basic.login.model.TSysUserRole;
import com.jdoa.basic.login.model.UserRole;

public class TSysUserDAOImpl implements TSysUserDAO {
	private SqlMapClient sqlMapClient;

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public Set findLoginAllBill(String fId) throws SQLException {
		Map temp = new HashMap();

		// 得到用户菜单编号

		// 取得角色菜单编号
		List rolelist = sqlMapClient.queryForList("findAllspecialByrole", fId);
		String roleId;
		String[] s2 = { "abc", "", "", "", "", "", "", "", "", "" };

		int i = 0;
		for (Iterator it = rolelist.iterator(); it.hasNext();) {
			SpecialAuthorityACL specialroleACL = (SpecialAuthorityACL) it
					.next();
			roleId = specialroleACL.getRoleid();
			s2[i] = roleId;
			i++;
		}
		String s = s2.toString();

		System.out.println(s);
		SpecialAuthorityACL specialAuthorityACL = new SpecialAuthorityACL();
		specialAuthorityACL.setUserid(fId);
		specialAuthorityACL.setS2(s2);

		// String[] s2=str.split(",");
		// System.out.println(str);
		List urolist = null;
		try {
			urolist = sqlMapClient.queryForList("findAllSpecialURACLListLogin",
					specialAuthorityACL);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (urolist != null) {

			for (Iterator it = urolist.iterator(); it.hasNext();) {
				SpecialAuthorityACL specialACL = (SpecialAuthorityACL) it
						.next();
				String str = specialACL.getActionSn() + "_"
						+ specialACL.getSpecialName();
				temp.put(str, str);

			}
		}
		Set set = temp.keySet();
		return set;
	}

	public Set findLoginAllMaterial(String fId) throws SQLException {
		Map temp = new HashMap();

		// 得到用户菜单编号

		// 取得角色菜单编号
		List rolelist = sqlMapClient.queryForList("findAllspecialByrole", fId);
		String roleId;
		String[] s2 = new String[10];

		int i = 0;
		for (Iterator it = rolelist.iterator(); it.hasNext();) {
			SpecialAuthorityACL specialroleACL = (SpecialAuthorityACL) it
					.next();
			roleId = specialroleACL.getRoleid();
			s2[i] = roleId;
			i++;
		}
		String s = s2.toString();

		System.out.println(s);
		SpecialAuthorityACL specialAuthorityACL = new SpecialAuthorityACL();
		specialAuthorityACL.setUserid(fId);
		specialAuthorityACL.setS2(s2);

		List urolist = null;
		try {
			urolist = sqlMapClient
					.queryForList("findAllSpecialMaterialURACLListLogin",
							specialAuthorityACL);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		for (Iterator it = urolist.iterator(); it.hasNext();) {
			SpecialAuthorityACL specialACL = (SpecialAuthorityACL) it.next();
			String str = specialACL.getActionSn() + "_"
					+ specialACL.getSpecialName();
			temp.put(str, str);

		}

		Set set = temp.keySet();
		return set;
	}

	public List findClassificationByuserid(String fid) throws SQLException {
		List list = null;
		try {
			list = sqlMapClient.queryForList("findUserClassificationByuserid",
					fid);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}

		return list;
	}

	public List<String> findLoginAllmenu(String fuserId) throws SQLException {
		List<String> idList = new ArrayList<String>();
		List<String> menuList = new ArrayList();
		try {
			menuList = sqlMapClient
					.queryForList(
							"ERPCLOUD_T_MENU.findAllEnableLongNumbersByUserId",
							fuserId);// 已有权限的菜单长编码
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		// 添加父菜单
		for (int j = 0; j < menuList.size(); j++) {
			String fmenuId = (String) menuList.get(j);
			if(!idList.contains(fmenuId)){
				idList.add(fmenuId);
			}
		}
		return idList;
	}

	// 查找所有的权限项actionname的集合
	public Set findLoginAllAuthority(String fId) throws SQLException {
		Map temp = new HashMap();
		// 得到用户菜单编号
		List list = sqlMapClient.queryForList("findAllAuthorityUserACLList",
				fId);
		for (Iterator it = list.iterator(); it.hasNext();) {
			AuthorityRoleACL authorityRoleACL = (AuthorityRoleACL) it.next();
			temp.put(authorityRoleACL.getActionName(), authorityRoleACL);

		}
		// 取得角色菜单编号
		List rolelist = sqlMapClient
				.queryForList("findAllAuthorityByrole", fId);
		String roleId;
		for (Iterator it = rolelist.iterator(); it.hasNext();) {
			AuthorityRoleACL authorityRoleACL = (AuthorityRoleACL) it.next();
			roleId = authorityRoleACL.getRoleId();
			List roleById = sqlMapClient.queryForList(
					"findAllAuthorityByroleId", roleId);
			for (Iterator roleit = roleById.iterator(); roleit.hasNext();) {
				AuthorityRoleACL authorityRoleACL2 = (AuthorityRoleACL) roleit
						.next();
				temp.put(authorityRoleACL2.getActionName(), authorityRoleACL2);

			}

		}
		// 取得组织菜单编号
		List orglist = sqlMapClient.queryForList("findAllAuthorityByorg", fId);
		String orgId;
		for (Iterator it = orglist.iterator(); it.hasNext();) {
			AuthorityRoleACL authorityRoleACL = (AuthorityRoleACL) it.next();
			orgId = authorityRoleACL.getOrgId();
			List orgById = sqlMapClient.queryForList("findAllAuthorityByorgId",
					orgId);
			for (Iterator roleit = orgById.iterator(); roleit.hasNext();) {
				AuthorityRoleACL authorityRoleACL2 = (AuthorityRoleACL) roleit
						.next();
				temp.put(authorityRoleACL2.getActionName(), authorityRoleACL2);
			}
		}
		System.out.println(temp.keySet());
		Set set = temp.keySet();
		return set;
	}

	public TSysUser findUser(TSysUser u) throws SQLException {
		// TODO Auto-generated method stub

		TSysUser record = (TSysUser) sqlMapClient.queryForObject(
				"sysUserValid", u);
		return record;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds
	 * to the database table ERPCLOUD.T_SYS_USER
	 * 
	 * @abatorgenerated Tue Sep 11 14:53:26 CST 2012
	 */
	public void insert(TSysUser sysUser) throws SQLException {
		sqlMapClient.insert("insertSysUser", sysUser);
	}

	public void insertUserRole(TSysUserRole record) throws SQLException {
		System.out.println("insertUserRole");
		try {
			sqlMapClient.insert("insertSysUserRole", record);
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		System.out.println("insertUserRole2");
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds
	 * to the database table ERPCLOUD.T_SYS_USER
	 * 
	 * @abatorgenerated Tue Sep 11 14:53:26 CST 2012
	 */
	public int updateByPrimaryKey(TSysUser record) throws SQLException {
		int rows = sqlMapClient.update(
				"ERPCLOUD_T_SYS_USER.abatorgenerated_updateByPrimaryKey",
				record);
		return rows;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds
	 * to the database table ERPCLOUD.T_SYS_USER
	 * 
	 * @abatorgenerated Tue Sep 11 14:53:26 CST 2012
	 */
	public int updateByPrimaryKeySelective(TSysUser record) throws SQLException {
		int rows = sqlMapClient
				.update(
						"ERPCLOUD_T_SYS_USER.abatorgenerated_updateByPrimaryKeySelective",
						record);
		return rows;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds
	 * to the database table ERPCLOUD.T_SYS_USER
	 * 
	 * @abatorgenerated Tue Sep 11 14:53:26 CST 2012
	 */
	public List searchUser(TSysUser sysUser) throws SQLException {
		List list = null;
		try {
			list = sqlMapClient.queryForList("searchSysUser", sysUser);
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}

		return list;
	}

	public List selectByExample(int start, int limit) throws SQLException {
		List list = null;
		try {
			list = sqlMapClient.queryForList("findListSysUser", start, limit);
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}

		return list;
	}

	public List findAllUserData() throws SQLException {

		List list = sqlMapClient.queryForList("findListSysUser");

		return list;
	}

	public List selectByExample(TSysUser record) throws SQLException {
		List list = sqlMapClient.queryForList("findAllSystemUsers");
		return list;
	}

	public List searchUserRole(UserRole userRole) throws SQLException {

		List list = sqlMapClient.queryForList("searchSysUserRole", userRole);
		return list;
	}

	public List findSysUserClassification(int start, int limit)
			throws SQLException {
		List list = null;
		try {
			list = sqlMapClient.queryForList("findListSysUserClassification",
					start, limit);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}

		return list;
	}

	public List selectUserRoleByExample(int start, int limit)
			throws SQLException {
		List list = null;
		try {
			list = sqlMapClient.queryForList("findListSysUserRole", start,
					limit);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}

		return list;
	}

	public TSysUser selectUserById(String fId) throws SQLException {

		System.out.println("selectUserById");
		TSysUser tSysUser = (TSysUser) sqlMapClient.queryForObject(
				"findSystemUsersByID", fId);
		return tSysUser;
	}

	public TSysUser selectUserByName(String username) throws SQLException {

		System.out.println("selectUserById");
		TSysUser tSysUser = (TSysUser) sqlMapClient.queryForObject(
				"findUserByUserName", username);
		return tSysUser;
	}

	// try{
	// List<TSysUser> roleList = new ArrayList<TSysUser>();
	//	
	// roleList.add(User);
	// // TSysUser User = (TSysUser)
	// sqlMapClient.queryForObject("findUserRoleList",fid);
	//	
	// }catch(Exception e){
	//			
	// e.printStackTrace();
	// System.out.println(e.getStackTrace());
	// }
	// ;
	/**
	 * This method was generated by Abator for iBATIS. This method corresponds
	 * to the database table ERPCLOUD.T_SYS_USER
	 * 
	 * @abatorgenerated Tue Sep 11 14:53:26 CST 2012
	 */
	public TSysUser selectByPrimaryKey(String fId) throws SQLException {
		TSysUser key = new TSysUser();
		key.setFid(fId);
		TSysUser record = (TSysUser) sqlMapClient.queryForObject(
				"ERPCLOUD_T_SYS_USER.abatorgenerated_selectByPrimaryKey", key);
		return record;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds
	 * to the database table ERPCLOUD.T_SYS_USER
	 * 
	 * @abatorgenerated Tue Sep 11 14:53:26 CST 2012
	 */
	public int deleteByExample(String fId) throws SQLException {
		int rows = sqlMapClient.delete("deleteSysUser", fId);
		return rows;
	}

	public int deleteByUserRoleId(TSysUserRole userRole) throws SQLException {
		int rows = sqlMapClient.delete("deleteUserRoleId", userRole);
		return rows;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds
	 * to the database table ERPCLOUD.T_SYS_USER
	 * 
	 * @abatorgenerated Tue Sep 11 14:53:26 CST 2012
	 */
	public int deleteByPrimaryKey(String fId) throws SQLException {
		TSysUser key = new TSysUser();
		key.setFid(fId);
		int rows = sqlMapClient.delete(
				"ERPCLOUD_T_SYS_USER.abatorgenerated_deleteByPrimaryKey", key);
		return rows;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds
	 * to the database table ERPCLOUD.T_SYS_USER
	 * 
	 * @abatorgenerated Tue Sep 11 14:53:26 CST 2012
	 */
	public int countSearchUser(TSysUser sysUser) throws SQLException {
		Integer count = (Integer) sqlMapClient.queryForObject(
				"countSearchTSysUser", sysUser);
		return count.intValue();
	}

	public int updateUserDataStatus(TSysUser tSysUser) throws SQLException {
		int rows = sqlMapClient.update("updateSysUserDataStatus", tSysUser);
		return rows;
	}

	public int updateByExample(TSysUser tSysUser) throws SQLException {
		int rows = sqlMapClient.update("updateSysUser", tSysUser);
		return rows;
	}

	public int updateUserPassword(TSysUser tSysUser) throws SQLException {
		int rows = sqlMapClient.update("updateSysUserPassword", tSysUser);
		return rows;
	}

	public int updateUserByUserName(TSysUser tSysUser) throws SQLException {
		int rows = sqlMapClient.update("updateSysUserByUserName", tSysUser);
		return rows;
	}

	public List findAllUserToJson() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}