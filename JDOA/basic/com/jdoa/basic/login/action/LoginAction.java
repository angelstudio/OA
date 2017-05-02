package com.jdoa.basic.login.action;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.basic.login.service.SysUserService;
import com.jdoa.basic.menu.model.TMenu;
import com.jdoa.basic.menu.service.MenuService;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.MD5;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {
	private SysUserService sysUserService;
	private MenuService menuServiceImpl;

	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}
	public MenuService getMenuServiceImpl() {
		return menuServiceImpl;
	}

	public void setMenuServiceImpl(MenuService menuServiceImpl) {
		this.menuServiceImpl = menuServiceImpl;
	}

	/**
	 * @author Action
	 * @describe 登陆请求类
	 * @return
	 * @date 2017-04-03
	 */
	public String userLogin() {
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		String fusername = request.getParameter("fusername");
		String fpassword = request.getParameter("fpassword");
		// 1.判断用户状态，2.验证用户账号密码，3.根据用户类型查询菜单
		// 当前电脑分辨率
		Toolkit tl = Toolkit.getDefaultToolkit();
		Dimension screenSize = tl.getScreenSize();
		session.setAttribute("screen_X", (int) screenSize.getWidth());
		session.setAttribute("screen_Y", (int) screenSize.getHeight());
		try {
			TSysUser user = sysUserService.findUserByUserName(fusername);
			if (user != null) {
				int fstatus = user.getFstatus();
				if (fstatus == 0) {
					// 正常状态
					List<String> idList = new ArrayList<String>();
					MD5 m = new MD5();
					String MD5_fpassword = m.getMD5ofStr(fpassword);
					if (MD5_fpassword.equals(user.getFpassword())) {
						// 匹配成功
						session.setAttribute("fuserId", user.getFid());
						session.setAttribute("fusername", fusername);
						session.setAttribute("user", user);
						session.setAttribute("LoginListenerUtil",
								new LoginListenerUtil());
						// 查询菜单权限1.用户类型，2用户菜单权限表，3.用户角色表
						int fuserType = user.getFusertype();
						if (0 == fuserType) {
							// 系统用户，所有权限
							List<TMenu> menuList = menuServiceImpl.queryMenu();
							for (int i = 0; i < menuList.size(); i++) {
								TMenu menu = menuList.get(i);
								String fmenuId=menu.getFid();
								if(!idList.contains(fmenuId)){
									idList.add(fmenuId);
								}
							}
						} else {
							// 普通用户 2用户菜单权限表，3.用户角色表
							String fuserId = user.getFid();
							idList = sysUserService
									.findloginAllMenuByuserId(fuserId);
						}
						session.setAttribute("idList", idList.toString());
					} else {
						request.setAttribute("error", "账号密码不正确!");
						return "loginerror";
					}
				} else {
					request.setAttribute("error", "用户被禁用!");
					return "loginerror";
				}
			} else {
				request.setAttribute("error", "用户不存在!");
				return "loginerror";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";

	}

	/**
	 * 根据cookie名称获取cookie
	 * 
	 * @author Action
	 * @param request
	 * @param name
	 * @return Jul 10, 2013
	 */
	public Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies && name != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * @author Action
	 * @return
	 * @describe 用户退出，销毁session
	 * @date 2017-04-03
	 */
	public String userLogout() {
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		session.invalidate();
		return "success";
	}
}
