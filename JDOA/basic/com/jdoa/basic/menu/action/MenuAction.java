package com.jdoa.basic.menu.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jdoa.basic.menu.model.TMenu;
import com.jdoa.basic.menu.service.MenuService;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;

public class MenuAction {
	private MenuService menuServiceImpl;
	
	public MenuService getMenuServiceImpl() {
		return menuServiceImpl;
	}

	public void setMenuServiceImpl(MenuService menuServiceImpl) {
		this.menuServiceImpl = menuServiceImpl;
	}

	/**
	 * @author Action
	 * @date 2017-04-03
	 * @describe 根据session中缓存的菜单id查询菜单信息
	 */
	public void findCacheMenu() {
	   HttpServletRequest request=  ActionUtil.getRequest();
	   HttpSession session=request.getSession();
	   String idList= (String) session.getAttribute("idList");
	   //根据菜单ID查询菜单信息
		String str=idList.substring(1);
		str=str.substring(0,str.length()-1);	  
		str=str.replaceAll(" ","");
		String[] strArr=str.split(",");	
		ArrayList<String> arrList=new ArrayList<String>();
		for(int i=0;i<strArr.length;i++){
			String fmenuId=strArr[i];
		  if(!arrList.contains(fmenuId)){
			  arrList.add(fmenuId);
		  }
		}
	    List<TMenu> menuList=menuServiceImpl.queryMenu(arrList);
	    JSONArray jsonArr=new JSONArray();
	    for(int i=0;i<menuList.size();i++){
	    	TMenu tmenu=menuList.get(i);
	    	JSONObject json=new JSONObject();
	    	String fmenuId=tmenu.getFid();
	    	String fname=tmenu.getFname();
	    	String fparentid=tmenu.getFparentid();
	    	int fseq=tmenu.getFseq();
	    	int flevel=tmenu.getFlevel();
	    	String furl=tmenu.getFurl();
	    	String flongNumber=tmenu.getFlongNumber();
	    	json.put("id", fmenuId);
	    	json.put("pid", fparentid);
	    	json.put("text", fname);
	    	json.put("flevel", flevel);
	    	json.put("furl", furl);
	    	json.put("fseq", fseq);
	    	json.put("flongNumber", flongNumber);
	    	jsonArr.add(json);
	    }
	    try {
			ActionUtil.getResponse().getWriter().write(jsonArr.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @author Action
	 * @describe 菜单操作
	 * @date 2017-04-04
	 */
	public void operMenu(){
		 HttpServletRequest request=  ActionUtil.getRequest();
		 String oper=request.getParameter("oper");
		 String fid=request.getParameter("fid");
		 String fname=request.getParameter("fname");
		 String furl=request.getParameter("furl");
		 String strSeq=request.getParameter("fseq");
		 if(StringUtil.isEmpty(strSeq)){
			 strSeq="999";
		 }
		 int fseq=Integer.valueOf(strSeq);
		 JDBCUtil jdbc = DataUtil.getJdbcUtil();
		 if("1".equals(oper)){
			 //新增操作
			 String pflongNumber=request.getParameter("pflongNumber");
			 String fparentId=request.getParameter("fparentId");
			 String flevelStr=request.getParameter("flevel");
			 if(StringUtil.isEmpty(flevelStr)){
				 flevelStr="1";
			 }
			 int flevel=Integer.valueOf(flevelStr);
			 fid= JDUuid.createID("22222222");
			 HashMap map=new HashMap();
			 map.put("fid", fid);
			 map.put("fname", fname);
			 map.put("fparentId", fparentId);
			 map.put("flevel", flevel);
			 map.put("furl", furl);
			 map.put("flongNumber", pflongNumber+"_"+fid);
			 map.put("fseq", fseq);
			 String sql=MapUtil.getSQL(map, "t_menu");
			 jdbc.execute(sql);
		 }else if("2".equals(oper)){
			 //修改操作
			 HashMap map=new HashMap();
			 map.put("fid", fid);
			 map.put("fname", fname);
			 map.put("furl", furl);
			 map.put("fseq", fseq);
			 String sql=MapUtil.getUpdateSql(map, "t_menu");
			 jdbc.execute(sql);
		 }else if("3".equals(oper)){
			 String sql="delete from t_menu where fid='"+fid+"'";
			 jdbc.execute(sql);
		 }
		 try {
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @author Action
	 * @date 2017-04-04
	 * @describe 查询所有菜单信息
	 */
	public void queryAllMenu(){
		    List<TMenu> menuList=menuServiceImpl.queryMenu();
		    JSONArray jsonArr=new JSONArray();
		    for(int i=0;i<menuList.size();i++){
		    	TMenu tmenu=menuList.get(i);
		    	JSONObject json=new JSONObject();
		    	String fmenuId=tmenu.getFid();
		    	String fname=tmenu.getFname();
		    	String fparentid=tmenu.getFparentid();
		    	int fseq=tmenu.getFseq();
		    	int flevel=tmenu.getFlevel();
		    	String furl=tmenu.getFurl();
		    	String flongNumber=tmenu.getFlongNumber();
		    	json.put("id", fmenuId);
		    	json.put("pid", fparentid);
		    	json.put("text", fname);
		    	json.put("flevel", flevel);
		    	json.put("furl", furl);
		    	json.put("fseq", fseq);
		    	json.put("flongNumber", flongNumber);
		    	jsonArr.add(json);
		    }
		    try {
				ActionUtil.getResponse().getWriter().write(jsonArr.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void getTable(){
		String sql="select fname,flevel,furl from t_menu";
		String tab=null;
		try {
			tab=TableUtil.getNoLimitTabStr(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ActionUtil.getResponse().getWriter().write(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
