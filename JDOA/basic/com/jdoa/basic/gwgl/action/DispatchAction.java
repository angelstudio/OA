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

import net.sf.json.JSONArray;

import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;
import com.jdoa.tool.PrintWordUtil;
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;
import com.lowagie.text.DocumentException;

/**
 * 发文管理模块
 * 
 * @author ningjianguo
 * 
 */
public class DispatchAction {
	private File file;
	private String fileFileName;
	private InputStream fileInputStream;

	/**
	 * 添加一条记录
	 */
	public void addOneDispatch() {
		if (file != null) {
			// 判断文件格式是否正确
			String[] contentTypes = { ".doc", ".docx", ".xls", "xlsx", ".ppt",
					".pptx", ".pdf", ".txt", ".zip", ".rar" };
			boolean flag = false;
			for (String contentType : contentTypes) {
				if (fileFileName.endsWith(contentType)) {
					flag = true;
				}
			}
			if (!flag) {
				// 格式不正确
				try {
					ActionUtil.getResponse().getWriter().write("error");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String fid = JDUuid.createID("qwertyui");
		String ftype = request.getParameter("ftype");
		String funit = request.getParameter("funit");
		String ftemplate = request.getParameter("ftemplate");
		String fpriority = request.getParameter("fpriority");
		String fmain_send = request.getParameter("fmain_send");
		String fcopy_send = request.getParameter("fcopy_send");
		String fdraftunit = user.getPerson().getFssbm();
		String fediter = user.getPerson().getFname();
		String ftitle = request.getParameter("ftitle");
		String fbody = request.getParameter("fbody");
		String fbody_ = request.getParameter("fbody_");// 不带格式的文本
		String fassist = request.getParameter("fassist");
		String fstatu = request.getParameter("fstatu");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("fid", fid);
		map.put("fbody", fbody.replace("nbsp", "&nbsp"));
		map.put("ftype", ftype);
		map.put("funit", funit);
		map.put("ftitle", ftitle);
		map.put("fmain_send", fmain_send);
		map.put("fcopy_send", fcopy_send);
		map.put("ftemplate", ftemplate);
		map.put("fpriority", fpriority);
		map.put("fdraftunit", fdraftunit);
		map.put("fediter", fediter);
		map.put("fassist", fassist);
		map.put("fstatu", fstatu);
		map.put("fcreate_date", sf.format(new Date()));
		String fileName = funit + "文件.doc";
		// 保存文件到数据库
		String fileids = "";
		String fileFolderStr = basePath + "//发文文件";
		File fileFolder = new File(fileFolderStr);
		if (!fileFolder.exists()) {
			fileFolder.mkdir();
		}
		if (file != null) {
			String fileid1 = JDUuid.createID("qwert2ui");
			String physicalFileName = fileid1
					+ fileFileName.substring(fileFileName.lastIndexOf("."));
			HashMap<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("fid", fileid1);
			fileMap.put("ftitle", fileFileName);
			fileMap.put("ffilepath", physicalFileName);
			fileMap.put("fremark", "1");// 1:用户上传的附件;0:自动生成的附件
			fileMap.put("funit", funit);
			fileMap.put("fcreate_person", fediter);
			fileMap.put("fcreate_date", sf.format(new Date()));
			jdbcUtil.executeUpdateSql(MapUtil.getSQL(fileMap, "t_file"));
			// 保存用户上传的附件
			try {
				FileInputStream inputStream = new FileInputStream(
						this.getFile());
				FileOutputStream outputStream = new FileOutputStream(fileFolderStr +"//"+ physicalFileName);
				byte[] buf = new byte[1024];
				int length = 0;
				while ((length = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, length);
				}
				inputStream.close();
				outputStream.flush();
				outputStream.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			fileids += fileid1 + ";";
		}
		String fileid2 = JDUuid.createID("qwert1ui");
		HashMap<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("fid", fileid2);
		fileMap.put("ftitle", fileName);
		fileMap.put("ffilepath", fileid2 + ".doc");
		fileMap.put("fremark", "0");
		fileMap.put("funit", funit);
		fileMap.put("fcreate_person", fediter);
		fileMap.put("fcreate_date", sf.format(new Date()));
		jdbcUtil.executeUpdateSql(MapUtil.getSQL(fileMap, "t_file"));
		fileids += fileid2;
		map.put("ffilepath", fileids);
		jdbcUtil.executeUpdateSql(MapUtil.getSQL(map, "t_dispatch"));
		// 保存自动生成的附件
		String[] paragraphs = fbody_.split("   ");
		List<String> pgs = new ArrayList<String>();
		for (String paragraph : paragraphs) {
			if(!StringUtil.isEmpty(paragraph)){
				pgs.add(paragraph.trim());
			}
		}
		String filePath = null;
		try {
			filePath = fileFolderStr + "//" + fileid2 + ".doc";
			File descFile = new File(filePath);
			if (!descFile.exists()) {
				descFile.createNewFile();
			}
			PrintWordUtil.printWord(funit + "文件", "中海利群置业[2017]3 号", ftitle,
					pgs, filePath);
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除记录
	 */
	public void delOneDispatch() {
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fidItems = request.getParameter("fidItems");
		String[] fids = fidItems.split(",");
		ArrayList<String> delSqls = new ArrayList<String>();
		for (String fid : fids) {
			// 删除物理磁盘文件
			String findSql = "select ffilepath from t_dispatch where fid='"
					+ fid + "'";
			ResultSet rs = jdbcUtil.executeQuery(findSql);
			try {
				rs.next();
				String tempItem = rs.getString("ffilepath");
				String[] fileItems = tempItem.split(";");
				for (String fileItem : fileItems) {
					String fsql = "select ffilepath from t_file where fid='"
							+ fileItem + "'";
					ResultSet rs_ = jdbcUtil.executeQuery(fsql);
					while (rs_.next()) {
						String temp = rs_.getString("ffilepath");
						File delFile = new File(basePath + "//发文文件//" + temp);
						delFile.delete();
					}
					String delSql = "delete from t_file where fid='" + fileItem
							+ "'";
					jdbcUtil.executeUpdateSql(delSql);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String delSql = "delete from t_dispatch where fid='" + fid + "'";
			delSqls.add(delSql);
		}
		jdbcUtil.executeBatch(delSqls);
		try {
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除一个附件
	 */
	public void delOneDispatchFile(){
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fid = request.getParameter("fid");
		String ffileid = request.getParameter("ffileid");
		String ffileName = request.getParameter("ffileName");
		//物理删除
		File delFile = new File(basePath + "//发文文件//" + ffileid + ffileName.substring(ffileName.lastIndexOf(".")));
		delFile.delete();
		//数据库删除
		String delSql = "delete from t_file where fid='"+ffileid+"'";
		jdbcUtil.executeUpdateSql(delSql);
		String findSql = "select ffilepath from t_dispatch where fid='"+fid+"'";
		ResultSet rs = jdbcUtil.executeQuery(findSql);
		try {
			while(rs.next()){
				String temp = rs.getString("ffilepath");
				StringBuffer filepath = new StringBuffer();
				String[] filepaths = temp.split(";");
				for (String filepath_ : filepaths) {
					if(!filepath_.equals(ffileid)){
						filepath.append(filepath_+";");
					}
				}
				filepath.deleteCharAt(filepath.length()-1);
				HashMap<String,Object> map = new HashMap<String,Object>();
				map.put("fid", fid);
				map.put("ffilepath", filepath.toString());
				jdbcUtil.executeUpdateSql(MapUtil.getUpdateSql(map, "t_dispatch"));
				String[] fps = filepath.toString().split(";");
				ArrayList<HashMap<String,String>> lists = new ArrayList<HashMap<String,String>>();
				for(String fp : fps){
					HashMap<String,String> maps = new HashMap<String, String>();
					String sql = "select ftitle,fremark from t_file where fid='"+fp+"'";
					ResultSet rs_ = jdbcUtil.executeQuery(sql);
					while(rs_.next()){
						maps.put("fid", fid);
						maps.put("ffileid", fp);
						maps.put("ffiletitle", rs_.getString("ftitle"));
						maps.put("fremark", rs_.getString("fremark"));
					}
					lists.add(maps);
				}
				ActionUtil.getResponse().getWriter().write(JSONArray.fromObject(lists).toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 更新一条记录
	 */
	public void updOneDispatch() {
		if (file != null) {
			// 判断文件格式是否正确
			String[] contentTypes = { ".doc", ".docx", ".xls", "xlsx", ".ppt",
					".pptx", ".pdf", ".txt", ".zip", ".rar" };
			boolean flag = false;
			for (String contentType : contentTypes) {
				if (fileFileName.endsWith(contentType)) {
					flag = true;
				}
			}
			if (!flag) {
				// 格式不正确
				try {
					ActionUtil.getResponse().getWriter().write("error");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String fid = request.getParameter("fid");
		String ftype = request.getParameter("ftype");
		String funit = request.getParameter("funit");
		String ftemplate = request.getParameter("ftemplate");
		String fpriority = request.getParameter("fpriority");
		String fmain_send = request.getParameter("fmain_send");
		String fcopy_send = request.getParameter("fcopy_send");
		String ftitle = request.getParameter("ftitle");
		String fbody = request.getParameter("fbody");
		String fbody_ = request.getParameter("fbody_");
		String fassist = request.getParameter("fassist");
		String fstatu = request.getParameter("fstatu");
		String autoFilepath = request.getParameter("autoFilepath");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("fid", fid);
		map.put("ftype", ftype);
		map.put("funit", funit);
		map.put("ftitle", ftitle);
		map.put("fbody", fbody.replace("nbsp", "&nbsp"));
		map.put("fmain_send", fmain_send);
		map.put("fcopy_send", fcopy_send);
		map.put("ftemplate", ftemplate);
		map.put("fpriority", fpriority);
		map.put("fassist", fassist);
		map.put("fstatu", fstatu);
		// 保存自动生成的附件
		String[] paragraphs = fbody_.split("   ");
		List<String> pgs = new ArrayList<String>();
		for (String paragraph : paragraphs) {
			if(!StringUtil.isEmpty(paragraph)){
				pgs.add(paragraph.trim());
			}
		}
		String filePath = null;
		try {
			filePath = basePath + "//发文文件//" + autoFilepath + ".doc";
			HashMap<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("fid", autoFilepath);
			fileMap.put("ftitle", funit + "文件.doc");
			jdbcUtil.executeUpdateSql(MapUtil.getUpdateSql(fileMap, "t_file"));
			PrintWordUtil.printWord(funit + "文件", "中海利群置业[2017]3 号", ftitle,
					pgs, filePath);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// 保存用户上传的附件
			if (file != null) {
				String fileid1 = JDUuid.createID("qwert1ui");
				String physicalFileName = fileid1
						+ fileFileName.substring(fileFileName.lastIndexOf("."));
				HashMap<String, Object> fileMap = new HashMap<String, Object>();
				fileMap.put("fid", fileid1);
				fileMap.put("ftitle", fileFileName);
				fileMap.put("ffilepath", physicalFileName);
				fileMap.put("fremark", "1");// 1:用户上传的附件;0:自动生成的附件
				fileMap.put("funit", funit);
				fileMap.put("fcreate_person", user.getPerson().getFname());
				fileMap.put("fcreate_date", sf.format(new Date()));
				jdbcUtil.executeUpdateSql(MapUtil.getSQL(fileMap, "t_file"));
				FileInputStream inputStream = new FileInputStream(
						this.getFile());
				FileOutputStream outputStream = new FileOutputStream(basePath
						+ "//发文文件//" + physicalFileName);
				byte[] buf = new byte[1024];
				int length = 0;
				while ((length = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, length);
				}
				inputStream.close();
				outputStream.flush();
				outputStream.close();
				String sql = "select ffilepath from t_dispatch where fid='"+fid+"'";
				ResultSet rs = jdbcUtil.executeQuery(sql);
				while(rs.next()){
					String ffilepath = rs.getString("ffilepath");
					map.put("ffilepath", ffilepath+";"+fileid1);
				}
			}
			jdbcUtil.executeUpdateSql(MapUtil.getUpdateSql(map, "t_dispatch"));
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有的记录
	 */
	public void findAllDispatchesToGrid() {
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		String fediter = user.getPerson().getFname();
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));// 每页展示行数
		String findSql = "select * from t_dispatch where fediter='" + fediter
				+ "'";
		String jsonString = null;
		try {
			jsonString = TableUtil.getTabStr(findSql, start, limit);
			ActionUtil.getResponse().getWriter().write(jsonString);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取记录详情
	 */
	public void queryDispatchDetail() {
		HttpServletRequest request = ActionUtil.getRequest();
		String fid = request.getParameter("fid");
		String findSql = "select * from t_dispatch where fid='" + fid + "'";
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		ResultSet rs = jdbcUtil.executeQuery(findSql);
		List<Object> lists = null;
		try {
			while (rs.next()) {
				lists = new ArrayList<Object>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					Object temp = rs.getObject(i) == null ? "" : rs
							.getObject(i);
					lists.add(temp);
				}
			}
			HashMap<String, String> files = null;
			List<HashMap<String, String>> fileItems = new ArrayList<HashMap<String, String>>();
			String tempArgs = lists.get(15).toString();
			String[] filepaths = tempArgs.split(";");
			for (String filepath : filepaths) {
				files = new HashMap<String, String>();
				String findSql_ = "select ftitle,fremark from t_file where fid='"
						+ filepath + "'";
				ResultSet rs_ = jdbcUtil.executeQuery(findSql_);
				rs_.next();
				String ffiletitle = rs_.getString("ftitle");
				String fremark = rs_.getString("fremark");
				files.put("ffileid", filepath);
				files.put("ffiletitle", ffiletitle);
				files.put("fremark", fremark);
				fileItems.add(files);
			}
			lists.add(fileItems);
			ActionUtil.getResponse().getWriter()
					.write(JSONArray.fromObject(lists).toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 附件下载
	 */
	public String downloadFile() {
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fid = request.getParameter("fid");
		ResultSet rs = jdbcUtil
				.executeQuery("select ftitle,ffilepath from t_file where fid='"
						+ fid + "'");
		try {
			while (rs.next()) {
				fileFileName = rs.getString("ftitle");
				String physicalFileName = rs.getString("ffilepath");
				File file = new File(basePath + "//发文文件//" + physicalFileName);
				fileInputStream = new FileInputStream(file);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 根据检索条件和查询关键字查询
	 */
	public void queryDispatchesToGrid() {
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		String fediter = user.getPerson().getFname();
		String ftitle = request.getParameter("ftitle");
		String fdispatch_no = request.getParameter("fdispatch_no");
		String funit = request.getParameter("funit");
		String ftype = request.getParameter("ftype");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));// 每页展示行数
		String findSql = "select * from t_dispatch where fediter='" + fediter
				+ "'";
		if (!StringUtil.isEmpty(ftitle)) {
			findSql += " and ftitle like '%" + ftitle + "%'";
		}
		if (!StringUtil.isEmpty(fdispatch_no)) {
			findSql += " and fdispatch_no like '%" + fdispatch_no + "%'";
		}
		if (!StringUtil.isEmpty(funit)) {
			findSql += " and funit like '%" + funit + "%'";
		}
		if (!StringUtil.isEmpty(ftype)) {
			findSql += " and ftype like '%" + ftype + "%'";
		}
		try {
			String jsonString = TableUtil.getTabStr(findSql, start, limit);
			ActionUtil.getResponse().getWriter().write(jsonString);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
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
