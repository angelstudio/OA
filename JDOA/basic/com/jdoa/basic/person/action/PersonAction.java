package com.jdoa.basic.person.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;
import com.jdoa.tool.TimeUtil;

public class PersonAction {
	/**
	 * @author Action
	 * @date 2017-04-09
	 * @descbrie 获取部门信息
	 */
   public void getOrg(){
	   JDBCUtil jdbc=DataUtil.getJdbcUtil();
	   JSONObject json=new JSONObject();
	   String sql="select fid,fname from T_ORG where fid not in(select fparentid from T_ORG) ";
	   try {
	      ResultSet rs=jdbc.executeQuery(sql);
			while(rs.next()){
				String fid=rs.getString("fid");
				String fname=rs.getString("fname");
				json.put(fid, fname);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ActionUtil.getResponse().getWriter().write(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   };
   /**
    * @author Action
    * @date 2017-04-09
    * @describe 保存人员信息
    */
   public void savePerson(){
	   HttpServletRequest request=ActionUtil.getRequest();
	   String jsonstr=request.getParameter("json");
	   JSONObject json=JSONObject.fromObject(jsonstr);
	   String fid=JDUuid.createID("2222_psn");
	   json.put("fid", fid);
	   String fusername=(String) request.getSession().getAttribute("fusername");
	   json.put("fcjz", fusername);
	   String fcjrq=TimeUtil.formatDateToString(new Date(),"yyyy-MM-dd");
	   json.put("fcjrq", fcjrq);
	   String sql=MapUtil.getSQLByJson(json, "T_PERSON");
	   JDBCUtil jdbc=DataUtil.getJdbcUtil();
	   jdbc.execute(sql);
	   try {
		ActionUtil.getResponse().getWriter().write(fid);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   /**
    * @author Action
    * @date 2017-04-09
    * @describe 修改人员信息
    */
   public void editPerson(){
	   HttpServletRequest request=ActionUtil.getRequest();
	   String jsonstr=request.getParameter("json");
	   JSONObject json=JSONObject.fromObject(jsonstr);
	   String fusername=(String) request.getSession().getAttribute("fusername");
	   json.put("fxgz", fusername);
	   String fxgrq=TimeUtil.formatDateToString(new Date(),"yyyy-MM-dd");
	   json.put("fxgrq", fxgrq);
	   String sql=MapUtil.getUpdateByJson(json, "T_PERSON");
	   JDBCUtil jdbc=DataUtil.getJdbcUtil();
	   jdbc.execute(sql);
	   try {
		ActionUtil.getResponse().getWriter().write("ok");
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
   };
   /**
    * @author Action
    * @date 2017-04-09
    * @describe 删除人员信息
    */
   public void delPerson(){
	   HttpServletRequest request=ActionUtil.getRequest();
	   String fid=request.getParameter("fid");
	   JDBCUtil jdbc=DataUtil.getJdbcUtil();
	   String sql="delete from T_PERSON where fid='"+fid+"'";
	   jdbc.execute(sql);
	   try {
		ActionUtil.getResponse().getWriter().write("ok");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   /**
    * @author Action
    * @describe 查询人员信息
    * @date 2017-04-09
    */
   public void queryPerson(){
	   HttpServletRequest request=ActionUtil.getRequest();
	   String fbmid=request.getParameter("fbmid");
	   String sql=null;
	   if(StringUtil.isEmpty(fbmid)){
		   sql="select * from T_PERSON";
	   }else{
		   sql="select * from T_PERSON where fbmid='"+fbmid+"'";
	   }
	   int starta=Integer.valueOf(request.getParameter("start"));
	   int limitb=Integer.valueOf(request.getParameter("limit"));
	   try {
		String tab=TableUtil.getTabStr(sql, starta, limitb);
		ActionUtil.getResponse().getWriter().write(tab);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   
   
   /**
    * @author Action
    * @describe 查询员工通讯录
    * @date 2017-04-09
    */
   public void queryEmployee(){
	   HttpServletRequest request=ActionUtil.getRequest();
	   String fname=request.getParameter("fname");
	   String sql=null;
	   if(fname==null || fname.length()<=0){
		   sql="select * from T_PERSON";
	   }else{
		   sql="select * from T_PERSON where fname='"+fname+"'";
	   }
	   int starta=Integer.valueOf(request.getParameter("start"));
	   int limitb=Integer.valueOf(request.getParameter("limit"));
	   try {
		String tab=TableUtil.getTabStr(sql, starta, limitb);
		ActionUtil.getResponse().getWriter().write(tab);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
}
