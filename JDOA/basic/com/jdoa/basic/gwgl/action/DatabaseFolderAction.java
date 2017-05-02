package com.jdoa.basic.gwgl.action;

import java.io.File;
import java.io.IOException;
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
import net.sf.json.JSONObject;

import com.jdoa.basic.login.model.TSysUser;
import com.jdoa.tool.ActionUtil;
import com.jdoa.tool.DataUtil;
import com.jdoa.tool.JDBCUtil;
import com.jdoa.tool.JDUuid;
import com.jdoa.tool.MapUtil;

/**
 * 文件夹模块
 * @author ningjianguo
 *
 */
public class DatabaseFolderAction {
	//查询所有文件夹
	public void queryAllFolder() {
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		HttpServletRequest request = ActionUtil.getRequest();
		//判断根目录是否被创建
		ResultSet rs_ = jdbcUtil.executeQuery("select count(*) from t_database_folder where fparentid='000000'");
		try {
			while(rs_.next()){
				//根目录未创建
				if(rs_.getInt(1)==0){
					//创建根目录
					HashMap<String, String> maps = new HashMap<String, String>();
					String fid = JDUuid.createID("qwer11ui");
					maps.put("fid", fid);
					maps.put("fparentid", "000000");
					maps.put("fname", "文件目录");
					maps.put("flevel", "1");
					int count = jdbcUtil.executeUpdateSql(MapUtil.getSQL(maps, "t_database_folder"));
					if(count==1){
						//创建磁盘物理文件夹
						String basePath = request.getRealPath("/uploadfile");
						File fileFolder = new File(basePath+"//文件目录");
						fileFolder.mkdirs();
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String querySql = "select * from t_database_folder";
		JSONArray jsonArr = new JSONArray();
		JSONObject json = null;
		ResultSet rs = jdbcUtil.executeQuery(querySql);
		try {
			while(rs.next()){
				json = new JSONObject();
				json.put("id", rs.getString("fid"));
				json.put("pid", rs.getString("fparentid"));
				json.put("text", rs.getString("fname"));
				json.put("flevel", rs.getInt("flevel"));
				jsonArr.add(json);
			}
			ActionUtil.getResponse().getWriter().write(jsonArr.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	//新增文件夹
	public void addFileFolder(){
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		HttpServletRequest request = ActionUtil.getRequest();
		HttpSession session = request.getSession();
		String basePath = request.getRealPath("/uploadfile");
		TSysUser user = (TSysUser) session.getAttribute("user");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String fid = JDUuid.createID("qwer12ui");
		String parentId = request.getParameter("fid");
		String fname = request.getParameter("fname");
		String parentFlevel = request.getParameter("flevel");
		HashMap<String, String> maps = new HashMap<String, String>();
		maps.put("fid", fid);
		maps.put("fparentid", parentId);
		maps.put("fname", fname);
		maps.put("flevel", (Integer.parseInt(parentFlevel)+1)+"");
		maps.put("fcreate_person", user.getPerson().getFname());
		maps.put("fcreate_date", sf.format(new Date()));
		//判断同一文件夹下是否有相同文件夹名
		String sql = "select count(*) from t_database_folder where fparentid='"+parentId+"' and fname='"+fname+"'";
		ResultSet rs = jdbcUtil.executeQuery(sql);
		//有文件夹命名冲突
		try {
			if(rs.next()){
				if(rs.getInt(1)!=0){
					try {
						ActionUtil.getResponse().getWriter().write("error");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					int count = jdbcUtil.executeUpdateSql(MapUtil.getSQL(maps, "t_database_folder"));
					//插入数据库成功
					if(count!=0){
						//在本地磁盘新建文件夹
						List<String> parentNames = getAllParentName(new ArrayList<String>(),fid);
						String filePath = "";
						for(int i = parentNames.size()-1; i >= 0; i--){
							filePath+=parentNames.get(i)+"//";
						}
						File fileFolder = new File(basePath+"//"+filePath);
						fileFolder.mkdirs();
						ActionUtil.getResponse().getWriter().write("ok");
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//更新文件夹
	public void updFileFolder(){
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		String fid = request.getParameter("fid");
		String fname = request.getParameter("fname");
		//判断同一文件夹下是否有相同文件夹名
		String sql = "select count(*) from t_database_folder where fparentid=(select fparentid from t_database_folder where fid='"+fid+"') and fname='"+fname+"'";
		ResultSet rs = jdbcUtil.executeQuery(sql);
		try {
			//有文件夹命名冲突
			if(rs.next()){
				if(rs.getInt(1)!=0){
					try {
						ActionUtil.getResponse().getWriter().write("error");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					//没有文件夹命名冲突
					HashMap<String, String> maps = new HashMap<String, String>();
					maps.put("fid", fid);
					maps.put("fname", fname);
					List<String> parentNames = getAllParentName(new ArrayList<String>(),fid);
					int count = jdbcUtil.executeUpdateSql(MapUtil.getUpdateSql(maps, "t_database_folder"));
					if(count != 0){
						String filePath = "";
						for(int i = parentNames.size()-1; i >= 0; i--){
							filePath+=parentNames.get(i)+"//";
						}
						File fileFolder = new File(basePath+"//"+filePath);
						filePath = basePath+"//"+filePath.substring(0, filePath.length()-1);
						String newFolderName = filePath.substring(0,filePath.lastIndexOf("//"))+"//"+fname;
						fileFolder.renameTo(new File(newFolderName));
					}
					ActionUtil.getResponse().getWriter().write("ok");
				}
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	//删除文件夹
	public void delFileFolder(){
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		HttpServletRequest request = ActionUtil.getRequest();
		String basePath = request.getRealPath("/uploadfile");
		String fid = request.getParameter("fid");
		List<String> parentNames = getAllParentName(new ArrayList<String>(),fid);	
		List<String> childFolderId = getChildFolderByParent(new ArrayList<String>(),fid);
		childFolderId.add(fid);
		List<String> listSql = new ArrayList<String>();
		for (String folderId : childFolderId) {
			String delSql = "delete from t_database_folder where fid='"+folderId+"'";
			listSql.add(delSql);
		}
		jdbcUtil.executeBatch(listSql);
		//删除本地磁盘物理文件夹
		String filePath = "";
		for(int i = parentNames.size()-1; i >= 0; i--){
			filePath+=parentNames.get(i)+"//";
		}
		if(deleteDir(new File(basePath+"//"+filePath))){
			try {
				ActionUtil.getResponse().getWriter().write("ok");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				ActionUtil.getResponse().getWriter().write("error");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//获取子文件夹的父目录
	protected List<String> getAllParentName(List<String> parentNames,String fid){
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String querySql = "select fparentid,fname from t_database_folder where fid='"+fid+"'";
		ResultSet rs = jdbcUtil.executeQuery(querySql);
		String fparentid = null;
		try {
			while(rs.next()){
				String fname = rs.getString("fname");
				fparentid = rs.getString("fparentid");
				parentNames.add(fname);
			}
			if(!fparentid.equals("000000")){
				getAllParentName(parentNames, fparentid);
			}else{
				return parentNames;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parentNames;
	}
	//根据父目录获取子目录
	
	private List<String> getChildFolderByParent(List<String> childFolderId,String parentId){
		JDBCUtil jdbcUtil = DataUtil.getJdbcUtil();
		String querySql = "select fid from t_database_folder where fparentid='"+parentId+"'";
		ResultSet rs = jdbcUtil.executeQuery(querySql);
		try {
			while(rs.next()){
				String fid = rs.getString("fid");
				childFolderId.add(fid);
				getChildFolderByParent(childFolderId, fid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return childFolderId;
	}
	//删除本地磁盘文件
	private boolean deleteDir(File dir) {
	        if (dir.isDirectory()) {
	            String[] children = dir.list();
	            for (int i=0; i<children.length; i++) {
	                boolean success = deleteDir(new File(dir, children[i]));
	                if (!success) {
	                    return false;
	                }
	            }
	        }
	        // 目录此时为空，可以删除
	        return dir.delete();
	    }
}
