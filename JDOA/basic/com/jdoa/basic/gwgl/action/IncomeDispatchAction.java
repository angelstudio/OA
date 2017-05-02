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
import com.jdoa.tool.StringUtil;
import com.jdoa.tool.TableUtil;

/**
 * 收文管理模块
 * @author ningjianguo
 *
 */
public class IncomeDispatchAction {
	private File file;
	private String fileFileName;
	private InputStream fileInputStream;
	/**
	 * 删除记录
	 */
	public void delOneIncomeDispatch(){
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fidItems = request.getParameter("fidItems");
		String[] fids = fidItems.split(",");
		ArrayList<String> delSqls = new ArrayList<String>();
		for (String fid : fids) {
			// 删除物理磁盘文件
			String findSql = "select ffilepath from t_incom_dispatch where fid='"
					+ fid + "'";
			ResultSet rs = jdbcUtil.executeQuery(findSql);
			try {
				rs.next();
				String tempItem = rs.getString("ffilepath");
				if(!StringUtil.isEmpty(tempItem)){
					String[] fileItems = tempItem.split(";");
					for (String fileItem : fileItems) {
						String fsql = "select ffilepath from t_file where fid='"
								+ fileItem + "'";
						ResultSet rs_ = jdbcUtil.executeQuery(fsql);
						while (rs_.next()) {
							String temp = rs_.getString("ffilepath");
							File delFile = new File(basePath + "//收文文件//" + temp);
							delFile.delete();
						}
						String delSql = "delete from t_file where fid='" + fileItem
								+ "'";
						jdbcUtil.executeUpdateSql(delSql);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String delSql = "delete from t_incom_dispatch where fid='" + fid + "'";
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
	 * 更新一条记录
	 */
	public void updOneIncomeDispatch(){
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
		String fpriority = request.getParameter("fpriority");
		String facceptdate = request.getParameter("facceptdate");
		String fincome_no = request.getParameter("fincome_no");
		String ftitle = request.getParameter("ftitle");
		String fincome_unit = request.getParameter("fincome_unit");
		String fincome_num = request.getParameter("fincome_num");
		String farchiver = request.getParameter("farchiver");
		String fhanding_person = request.getParameter("fhanding_person");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("fid", fid);
		map.put("fpriority", fpriority);
		map.put("facceptdate", facceptdate);
		map.put("fincome_no", fincome_no);
		map.put("ftitle", ftitle);
		map.put("fincome_unit", fincome_unit);
		map.put("fincome_num", fincome_num);
		map.put("farchiver", farchiver);
		map.put("fhanding_person", fhanding_person);
		try {
			// 保存用户上传的附件
			if (file != null) {
				String fileid1 = JDUuid.createID("qwer21ui");
				String physicalFileName = fileid1
						+ fileFileName.substring(fileFileName.lastIndexOf("."));
				HashMap<String, Object> fileMap = new HashMap<String, Object>();
				fileMap.put("fid", fileid1);
				fileMap.put("ftitle", fileFileName);
				fileMap.put("ffilepath", physicalFileName);
				fileMap.put("fremark", "1");// 1:用户上传的附件;0:自动生成的附件
				fileMap.put("funit", fincome_unit);
				fileMap.put("fcreate_person", user.getPerson().getFname());
				fileMap.put("fcreate_date", sf.format(new Date()));
				jdbcUtil.executeUpdateSql(MapUtil.getSQL(fileMap, "t_file"));
				FileInputStream inputStream = new FileInputStream(
						this.getFile());
				FileOutputStream outputStream = new FileOutputStream(basePath
						+ "//收文文件//" + physicalFileName);
				byte[] buf = new byte[1024];
				int length = 0;
				while ((length = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, length);
				}
				inputStream.close();
				outputStream.flush();
				outputStream.close();
				String sql = "select ffilepath from t_incom_dispatch where fid='"+fid+"'";
				ResultSet rs = jdbcUtil.executeQuery(sql);
				while(rs.next()){
					String ffilepath = rs.getString("ffilepath");
					map.put("ffilepath", ffilepath+";"+fileid1);
				}
			}
			jdbcUtil.executeUpdateSql(MapUtil.getUpdateSql(map, "t_incom_dispatch"));
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
	public void findAllIncomeDispatchesToGrid(){
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit= Integer.parseInt(request.getParameter("limit"));//每页展示行数
		String findSql = "select * from t_incom_dispatch where fediter='"+user.getPerson().getFname()+"'";
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
	 * 根据fid获取单条记录
	 */
	public void getOneIncomDispatchByFid(){
		HttpServletRequest request = ActionUtil.getRequest();
		String fid = request.getParameter("fid");
		String findSql = "select * from t_incom_dispatch where fid='" + fid + "'";
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
			if(!StringUtil.isEmpty(tempArgs)){
				String[] filepaths = tempArgs.split(";");
				for (String filepath : filepaths) {
					files = new HashMap<String, String>();
					String findSql_ = "select ftitle,fremark from t_file where fid='"
							+ filepath + "'";
					ResultSet rs_ = jdbcUtil.executeQuery(findSql_);
					while(rs_.next()){
						String ffiletitle = rs_.getString("ftitle");
						String fremark = rs_.getString("fremark");
						files.put("ffileid", filepath);
						files.put("ffiletitle", ffiletitle);
						files.put("fremark", fremark);
						fileItems.add(files);
					}
				}
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
	 * 添加一条记录
	 */
	public void addOneIncomeDispatch(){
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
		String fid = JDUuid.createID("qwer12ui");
		String facceptdate = request.getParameter("facceptdate");
		String fpriority = request.getParameter("fpriority");
		String fincome_no = request.getParameter("fincome_no");
		String fincome_unit = request.getParameter("fincome_unit");
		String fincome_num = request.getParameter("fincome_num");
		String ftitle = request.getParameter("ftitle");
		String farchiver = request.getParameter("farchiver");
		String fhanding_person = request.getParameter("fhanding_person");
		HashMap<String,Object> maps = new HashMap<String,Object>();
		maps.put("fid", fid);
		maps.put("facceptdate", facceptdate);
		maps.put("fpriority", fpriority);
		maps.put("fincome_no", fincome_no);
		maps.put("fincome_unit", fincome_unit);
		maps.put("fincome_num", fincome_num);
		maps.put("ftitle", ftitle);
		maps.put("farchiver", farchiver);
		maps.put("fhanding_person", fhanding_person);
		maps.put("fcreate_date", sf.format(new Date()));
		maps.put("fediter", user.getPerson().getFname());
		String fileFolderStr = basePath + "//收文文件";
		File fileFolder = new File(fileFolderStr);
		if (!fileFolder.exists()) {
			fileFolder.mkdir();
		}
		// 保存文件到数据库
		if (file != null) {
			String fileid1 = JDUuid.createID("qwert3ui");
			String physicalFileName = fileid1
					+ fileFileName.substring(fileFileName.lastIndexOf("."));
			HashMap<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("fid", fileid1);
			fileMap.put("ftitle", fileFileName);
			fileMap.put("ffilepath", physicalFileName);
			fileMap.put("fremark", "1");// 1:用户上传的附件;0:自动生成的附件
			fileMap.put("funit", fincome_unit);
			fileMap.put("fcreate_person", user.getPerson().getFname());
			fileMap.put("fcreate_date", sf.format(new Date()));
			jdbcUtil.executeUpdateSql(MapUtil.getSQL(fileMap, "t_file"));
			// 保存用户上传的附件
			try {
				FileInputStream inputStream = new FileInputStream(
						this.getFile());
				FileOutputStream outputStream = new FileOutputStream(fileFolderStr + "//" + physicalFileName);
				byte[] buf = new byte[1024];
				int length = 0;
				while ((length = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, length);
				}
				inputStream.close();
				outputStream.flush();
				outputStream.close();
				maps.put("ffilepath", fileid1);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		jdbcUtil.executeUpdateSql(MapUtil.getSQL(maps, "t_incom_dispatch"));
		try {
			ActionUtil.getResponse().getWriter().write("ok");
		}catch (IOException e) {
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
				File file = new File(basePath + "//收文文件//" + physicalFileName);
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
		File delFile = new File(basePath + "//收文文件//" + ffileid + ffileName.substring(ffileName.lastIndexOf(".")));
		delFile.delete();
		//数据库删除
		String delSql = "delete from t_file where fid='"+ffileid+"'";
		jdbcUtil.executeUpdateSql(delSql);
		String findSql = "select ffilepath from t_incom_dispatch where fid='"+fid+"'";
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
				if(filepath.length() == 0){
					HashMap<String,Object> map = new HashMap<String,Object>();
					map.put("fid", fid);
					map.put("ffilepath", "");
					jdbcUtil.executeUpdateSql(MapUtil.getUpdateSql(map, "t_incom_dispatch"));
					ActionUtil.getResponse().getWriter().write("isnull");
					return;
				}
				filepath.deleteCharAt(filepath.length()-1);
				HashMap<String,Object> map = new HashMap<String,Object>();
				map.put("fid", fid);
				map.put("ffilepath", filepath.toString());
				jdbcUtil.executeUpdateSql(MapUtil.getUpdateSql(map, "t_incom_dispatch"));
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
	 * 根据检索条件和查询关键字查询
	 */
	public void queryIncomeDispatchesToGrid(){
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		TSysUser user = (TSysUser) session.getAttribute("user");
		String ftitle = request.getParameter("ftitle");
		String fincome_unit = request.getParameter("fincome_unit");
		String date1 = request.getParameter("date1");
		String date2 = request.getParameter("date2");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit= Integer.parseInt(request.getParameter("limit"));//每页展示行数
		String findSql = "select * from t_incom_dispatch where fediter='"+user.getPerson().getFname()+"'";
		if(!StringUtil.isEmpty(ftitle)){
			findSql +=" and ftitle like '%"+ftitle+"%'";
		}
		if(!StringUtil.isEmpty(fincome_unit)){
			findSql +=" and fincome_unit like '%"+fincome_unit+"%'";
		}
		if(!StringUtil.isEmpty(date1)&&!StringUtil.isEmpty(date2)){
			findSql +=" and to_date('"+date1+"','yyyy-MM-dd')<=to_date(facceptdate,'yyyy-MM-dd') and to_date(facceptdate,'yyyy-MM-dd')<=to_date('"+date2+"','yyyy-MM-dd')";
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
	
	/**
	 * 查询文件下发人员
	 */
	public void querySendPerson(){
		   HttpServletRequest request=ActionUtil.getRequest();
		   String fbmid=request.getParameter("fbmid");
		   String ffileid=request.getParameter("ffileid");
		   String sql=null;
		   if(StringUtil.isEmpty(fbmid)){
			   sql="select p.fid,p.fssbm,p.fname,f.freader_statu,f.freader_date,p.fbrdh from t_person p left join t_filesend f on p.fid=f.fpersonid and f.ffileid='"+ffileid+"'";
		   }else{
			   sql="select p.fid,p.fssbm,p.fname,f.freader_statu,f.freader_date,p.fbrdh from t_person p left join t_filesend f on p.fid=f.fpersonid and f.ffileid='"+ffileid+"' where fbmid='"+fbmid+"'";
		   }
		   int starta=Integer.valueOf(request.getParameter("start"));
		   int limitb=Integer.valueOf(request.getParameter("limit"));
		   try {
			String tab=TableUtil.getTabStr(sql, starta, limitb);
			ActionUtil.getResponse().getWriter().write(tab);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 文件下发
	 */
	public void fileSend(){
		HttpServletRequest request=ActionUtil.getRequest();
		HttpSession session = request.getSession();
		String sendPersonId = (String) session.getAttribute("fuserId");
		TSysUser user = (TSysUser)session.getAttribute("user");
		String sendPersonName = user.getPerson().getFname();
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String fileId = request.getParameter("fileId");
		String items = request.getParameter("personIdItems");
		String[] personIdItems = items.split(",");
		//以&menu结尾代表是菜单而不是个人
		String tempStr = personIdItems[0];
		if(tempStr.endsWith("&menu")){
			String parentId = tempStr.substring(0, tempStr.length()-5);
			ArrayList<String> tempids = new ArrayList<String>();
			ArrayList<String> deptids = getChildDeptID(tempids,parentId);
			personIdItems = getEmployeesByDeptId(deptids);
		}
		HashMap<String,Object> maps = null;
		ArrayList<String> sqls = new ArrayList<String>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (String item : personIdItems) {
			maps = new HashMap<String,Object>();
			String fid = JDUuid.createID("qwert123");
			maps.put("fid", fid);
			maps.put("fpersonid", item);
			maps.put("ffileid", fileId);
			maps.put("freader_statu", "0");
			maps.put("fincome_date", sf.format(new Date()));
			maps.put("fincome_statu", "0");
			maps.put("fsendperson_id", sendPersonId);
			maps.put("fsendperson_name", sendPersonName);
			sqls.add(MapUtil.getSQL(maps, "t_filesend"));
		}
		jdbcUtil.executeBatch(sqls);
		try {
			ActionUtil.getResponse().getWriter().write("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据部门ID获得该部门下所有的员工ID
	 */
	private String[] getEmployeesByDeptId(ArrayList<String> dids){
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		ArrayList<String> employees = new ArrayList<String>();
		for(String did:dids){
			String sql = "select fid from t_person where fbmid='"+did+"'";
			ResultSet rs = jdbcUtil.executeQuery(sql);
			try {
				while(rs.next()){
					employees.add(rs.getString("fid"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		String[] temps = employees.toArray(new String[employees.size()]);
		return temps;
	}
	
	/**
	 * 递归求子部门ID
	 */
	private ArrayList<String> getChildDeptID(ArrayList<String> ids,String parentId){
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String querySql = "select fid from t_org where fparentid='"+parentId+"'";
		ResultSet rs = jdbcUtil.executeQuery(querySql);
		try {
			if(rs.next()==false){
			 ids.add(parentId);
			}else{
			 boolean flag = true;
			 while(flag){
				 String fid = rs.getString("fid");
				 flag = rs.next();
				getChildDeptID(ids,fid);
			 }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ids;
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
