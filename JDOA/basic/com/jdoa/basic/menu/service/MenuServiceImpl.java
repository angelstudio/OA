package com.jdoa.basic.menu.service;

import java.util.List;

import com.jdoa.basic.menu.dao.MenuDao;
import com.jdoa.basic.menu.model.TMenu;

public class MenuServiceImpl implements MenuService{
    private MenuDao menuDaoImpl;
    
	public MenuDao getMenuDaoImpl() {
		return menuDaoImpl;
	}

	public void setMenuDaoImpl(MenuDao menuDaoImpl) {
		this.menuDaoImpl = menuDaoImpl;
	}

	public int addMenu(TMenu menu) {
		return 0;
	}

	public int deleteMenu(String fid) {
		return 0;
	}

	public List<TMenu> queryMenu(TMenu menu) {
		return null;
	}

	public List<TMenu> queryMenu() {
		List<TMenu> menuList=menuDaoImpl.queryMenu();
		return menuList;
	}

	public int updateMenu(TMenu menu) {
		return 0;
	}

	public List<TMenu> queryMenu(List list) {
		List<TMenu> menuList=menuDaoImpl.queryMenu(list);
		return menuList;
	}



}
