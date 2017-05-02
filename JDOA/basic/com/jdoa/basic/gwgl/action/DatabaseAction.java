package com.jdoa.basic.gwgl.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;
/**
 * 文件模块
 * @author ningjianguo
 *
 */
public class DatabaseAction {
	private File  file;
	private String fileFileName;
	private InputStream fileInputStream;
	/**
	 * 文件上传
	 */
	public void uploadFile(){
		//判断文件格式是否正确
		String[] contentTypes = {".doc",".docx",".xls","xlsx",".ppt",".pptx",".pdf",".txt",".zip",".rar"};
		boolean flag = false;
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
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String fid = JDUuid.createID("qwer12ui");
		String basePath = request.getRealPath("/uploadfile");
		String fparentid = request.getParameter("fid");
		String ftitle = request.getParameter("ftitle");
		String fkeyword = request.getParameter("fkeyword");
		//把文件存入指定文件夹
		List<String> lists = new DatabaseFolderAction().getAllParentName(new ArrayList<String>(), fparentid);
		String filePath = "";
		for(int i = lists.size()-1; i >= 0; i--){
			filePath+=lists.get(i)+"//";
		}
		 filePath = basePath+"//"+filePath;
		 fileFileName = ftitle+fileFileName.substring(fileFileName.lastIndexOf("."));
		 String physicalFileName = fid+fileFileName.substring(fileFileName.lastIndexOf("."));
		 try {
			 FileInputStream inputStream = new FileInputStream(this.getFile());
			 FileOutputStream outputStream = new FileOutputStream(filePath+ physicalFileName);
			 byte[] buf = new byte[1024];
			 int length = 0;
			 while ((length = inputStream.read(buf)) != -1) {
			     outputStream.write(buf, 0, length);
			 }
			 inputStream.close();
			 outputStream.flush();
			 outputStream.close();
			 //保存到数据库
			 HashMap<String, String> maps = new HashMap<String, String>();
				maps.put("fid", fid);
				maps.put("fparentfolder", fparentid);
				maps.put("ftitle", fileFileName);
				maps.put("ffilepath", physicalFileName);
				maps.put("fkeyword", fkeyword);
				maps.put("fcreate_person", user.getPerson().getFname());
				maps.put("fcreate_date", sf.format(new Date()));
				jdbcUtil.executeUpdateSql(MapUtil.getSQL(maps, "t_database"));
				try {
					ActionUtil.getResponse().getWriter().write("ok");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 文件下载
	 */
	public String downloadFile(){
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fid = request.getParameter("fid");
		ResultSet rs = jdbcUtil.executeQuery("select ftitle,ffilepath,fparentfolder from t_database where fid='"+fid+"'");
		try {
			while(rs.next()){
				fileFileName = rs.getString("ftitle");
				String ffilepath = rs.getString("ffilepath");
				String fparentfolder = rs.getString("fparentfolder");
				String filePath = "";
				List<String> lists = new DatabaseFolderAction().getAllParentName(new ArrayList<String>(), fparentfolder);
				for(int i = lists.size()-1; i >= 0; i--){
					filePath += lists.get(i)+"//";
				}
				File file = new File(basePath+"//"+filePath+ffilepath);
				fileInputStream = new FileInputStream(file);
			}
			}catch (SQLException e) {
				e.printStackTrace();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		return "success";
	}
	
	/**
	 * 查询文件详情
	 */
	public void findFileInfo(){
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		String fparentfolder = request.getParameter("fid");
		String fediter = user.getPerson().getFname();
		int start = Integer.parseInt(request.getParameter("start"));
		int limit= Integer.parseInt(request.getParameter("limit"));//每页展示行数
		String findSql = "select * from t_database where fcreate_person='"+fediter+"'"+(fparentfolder == null ? "" : " and fparentfolder='"+fparentfolder+"'");
		String jsonString = null;
		try {
			jsonString = TableUtil.getTabStr(findSql, start,limit);
			ActionUtil.getResponse().getWriter().write(jsonString);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询一个文件
	 */
	public void findOneFile(){
		HttpServletRequest request = ActionUtil.getRequest();
		String fid = request.getParameter("fid");
		String findSql = "select * from t_database where fid='"+fid+"'";
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		ResultSet rs = jdbcUtil.executeQuery(findSql);
		ArrayList<Object> lists = new ArrayList<Object>();
		try {
			while(rs.next()){
				for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
					Object temp = rs.getObject(i) == null ? "": rs.getObject(i);
					lists.add(temp);
				}
			}
			ActionUtil.getResponse().getWriter().write(lists.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新一个文件
	 */
	public void updOneFile(){
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fid = request.getParameter("fid");
		String ftitle = request.getParameter("ftitle");
		String fkeyword = request.getParameter("fkeyword");
		HashMap<String, String> maps = new HashMap<String, String>();
		maps.put("fid", fid);
		maps.put("ftitle", ftitle);
		maps.put("fkeyword", fkeyword);
		jdbcUtil.executeUpdateSql(MapUtil.getUpdateSql(maps, "t_database"));
		try {
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	
	/**
	 * 删除文件
	 */
	public void delOneFile(){
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fidItems = request.getParameter("fidItems");
		String[] fids = fidItems.split(",");
		ArrayList<String> delSqls = new ArrayList<String>();
		//物理删除文件
		for(String fid : fids){
			ResultSet rs = jdbcUtil.executeQuery("select ffilepath, fparentfolder from t_database where fid='"+fid+"'");
			try {
				while(rs.next()){
					String fparentfolder = rs.getString("fparentfolder");
					String ffilepath = rs.getString("ffilepath");
					String filePath = "";
					List<String> lists = new DatabaseFolderAction().getAllParentName(new ArrayList<String>(), fparentfolder);
					for(int i = lists.size()-1; i >= 0; i--){
						filePath += lists.get(i)+"//";
					}
					File delFile = new File(basePath+"//"+filePath+ffilepath);
					if(delFile.delete()){
						//数据库删除
						for (String fid_ : fids) {
							String delSql = "delete from t_database where fid='"+fid_+"'";
							delSqls.add(delSql);
						}
						jdbcUtil.executeBatch(delSqls);
						ActionUtil.getResponse().getWriter().write("ok");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//根据条件查询出文件
	public void queryFileInfoToGrid(){
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		String fcreate_person = user.getPerson().getFname();
		String funit = request.getParameter("funit");
		String fdispatch_no = request.getParameter("fdispatch_no");
		String ftitle = request.getParameter("ftitle");
		String fkeyword = request.getParameter("fkeyword");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit= Integer.parseInt(request.getParameter("limit"));//每页展示行数
		String findSql = "select * from t_database where fcreate_person='"+fcreate_person+"'";
		if(!StringUtil.isEmpty(ftitle)){
			findSql +=" and ftitle like '%"+ftitle+"%'";
		}
		if(!StringUtil.isEmpty(fdispatch_no)){
			findSql +=" and fdispatch_no like '%"+fdispatch_no+"%'";
		}
		if(!StringUtil.isEmpty(funit)){
			findSql +=" and funit like '%"+funit+"%'";
		}
		if(!StringUtil.isEmpty(fkeyword)){
			findSql +=" and fkeyword like '%"+fkeyword+"%'";
		}
		try {
			String jsonString = TableUtil.getTabStr(findSql, start,limit);
			ActionUtil.getResponse().getWriter().write(jsonString);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		try {
			return new String(fileFileName.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
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
