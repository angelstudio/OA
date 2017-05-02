package com.jdoa.basic.rcbg.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.EmailManager;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;


public class SendEmailAction {
	private File  file;
	private String fileFileName;
	private InputStream fileInputStream;
	
	/**
	 *邮件列表
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("null")
	public  void  EmailList() throws IOException{
		HttpServletRequest request = ActionUtil.getRequest();
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit= Integer.parseInt(request.getParameter("limit"));
		String ftitle = request.getParameter("ftitle");
		String fsender =request.getParameter("fsender");
		
		String sql = "select * from T_SEND_EMAIL WHERE 1=1  ";
		
		if(ftitle!=null && ftitle.length()>0){
			sql+=" and　ftitle like '%"+ftitle+"%' ";
		}
		if(fsender!=null && fsender.length()>0){
			
			sql+=" and  fsender like '%"+fsender+"%' ";
		}
		
		String datastr = null;
		try {
		  datastr = TableUtil.getTabStr(sql, start, limit);
		} catch (SQLException e) {
		  e.printStackTrace();
		}
		  ActionUtil.getResponse().getWriter().write(datastr);
	}
	
	
	/**
	 * 发送邮件
	 * @throws IOException 
	 */
	@SuppressWarnings("static-access")
	public void sendEmail() throws IOException{
		JSONObject json =new JSONObject();
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		Map<String, Object> map =new HashMap<String, Object>();
		
		map.put("fid",JDUuid.createID("poiuytre"));
		map.put("ftitle", request.getParameter("ftitle"));
		map.put("fcontent", request.getParameter("fcontent"));
		map.put("fusername", request.getParameter("fusername"));
		map.put("fuserid", request.getParameter("fuserid"));
		map.put("faccessory", fileFileName);
		map.put("fcreatedate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		map.put("fsenddepartment", user.getPerson().getFssbm());
		map.put("fsender", user.getPerson().getFname());
		map.put("fsenderid", user.getPerson().getFid());
		map.put("femailnum", request.getParameter("femailnum"));
		
		//判断文件格式是否正确
		String[] contentTypes = {".doc",".docx",".xls","xlsx",".ppt",".pptx",".pdf",".txt",".zip",".rar"};
		boolean flag = false;
		  
        String[] fileList = null;
		if(fileFileName!=null && fileFileName.length()>0){
		for (String contentType : contentTypes) {
			if(fileFileName.endsWith(contentType)){
			flag = true;
			}
	     }
		 if(!flag){
			//格式不正确
			try {
		 	ActionUtil.getResponse().getWriter().write("error");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			}
		}
		//保存到根目录下的upload文件夹下  
        String realPath = ServletActionContext.getServletContext().getRealPath("/emailfile");    //取得真实路径
        fileList = new String[1];
        fileList[0] = realPath+"/"+fileFileName;
        
        //创建父文件夹  
        if(file!=null){  
        	
        File saveFile = new File(new File(realPath), fileFileName);  
        if(!saveFile.getParentFile().exists()){     //如果Images文件夹不存在  
            saveFile.getParentFile().mkdirs();  //则创建新的多级文件夹  
        } 
        try {
        	//保存文件  
			FileUtils.copyFile(file, saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		}
        
    }
		
		 String femailnum = (String) map.get("femailnum");
	        String[] to=femailnum.split(",");
	        for (int i = 0; i < to.length; i++) {
				String string = to[i];
				if(string.equals("null")){
					ActionUtil.getResponse().getWriter().write("fail");
					return;
				}
			}
		    String from = EmailManager.username;
	        String[] copyto = {""};
	        String subject = (String) map.get("ftitle");
	        String content = (String) map.get("fcontent");
	        EmailManager.senderNick=(String) map.get("fsender");
	        EmailManager.getInstance().sendMail(from, to, copyto, subject, content, fileList);
	        
	        //添加
	        String sql = MapUtil.getSQL((HashMap) map, "T_SEND_EMAIL");
	        jdbc.execute(sql);
            ActionUtil.getResponse().getWriter().write("success");
	}


	/**
	 * 删除记录
	 * @throws IOException 
	 */
	public void delEmail() throws IOException{
		
		JDBCUtil jdbc = DataUtil.getJdbcUtil();
		HttpServletRequest request = ActionUtil.getRequest();
		String fid = request.getParameter("fid");
		String sql ="delete  from  T_SEND_EMAIL WHERE fid= '"+fid+"' ";	 
	
		jdbc.execute(sql);
		
		JSONObject json =new JSONObject();
	    json.accumulate("success", "success");
	      //提示
	    ActionUtil.getResponse().getWriter().write(json.toString());
	}
	
	
	
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	
	
	
}
