package com.jdoa.basic.menu.dao;

import java.sql.SQLException;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jdoa.basic.menu.model.TMenu;

public class MenuDaoImpl implements MenuDao{
    private SqlMapClient sqlMapClient;
    
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public int addMenu(TMenu menu) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int deleteMenu(String fid) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<TMenu> queryMenu(TMenu menu) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TMenu> queryMenu() {
		List<TMenu> menuList=null;
		try {
			menuList=sqlMapClient.queryForList("queryAllMenu");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return menuList;
	}

	public int updateMenu(TMenu menu) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<TMenu> queryMenu(List list) {
		List<TMenu> menuList=null;
		try {
			menuList=sqlMapClient.queryForList("ERPCLOUD_T_MENU_findCacheMenu", list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return menuList;
	}	

}
