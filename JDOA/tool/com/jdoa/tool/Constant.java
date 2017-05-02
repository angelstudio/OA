package com.jdoa.tool;

public interface Constant {

	public static final String ORACLE_DRIVERCLASS = "oracle.jdbc.driver.OracleDriver";
	public static final String SQLSERVER_DRIVERCLASS = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static final String DB2_DRIVERCLASS ="com.ibm.db2.jcc.DB2Driver";
	public static final String MYSQL_DRIVERCLASS = "com.mysql.jdbc.Driver";
	

	public static final String TypeBeanMapping_PATH = "TypeBeanMapping.properties";//类型与bean配置文件
	
	public static final String BILLSUPERACTION = "billCreateAction";//父类单据beanid
	
	public static final String MATERIALSUPERACTION = "materialCreateAction";//父类基础资料beanid

	
	//日期格式
	public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
	public static final String HMS = "HH:mm:ss";
	public static final String YMD = "yyyy-MM-dd";

	
	public static final String BILL_OBJ_NAME = "billType";
	public static final String MATERIAL_OBJ_NAME = "materialName";
	
	//菜单class样式
	public static final String FIRST_LAYER = "FirstLayer";
	public static final String SECOND_LAYER = "SecondLayer";
	public static final String THIRD_LAYER = "ThirdLayer";
	
	public static String TypeBeanMapping_Basic_URL = "TypeBeanMapping_basic.properties";
	public static String TypeBeanMapping_URL =  "TypeBeanMapping.properties";
	
	//操作
	public static String ACTION_ADD = "add";
	public static String ACTION_DELETE = "delete";
	public static String ACTION_UPDATE = "update";
	public static String ACTION_VIEW = "view";
	public static String ACTION_LOGIN = "login";
	public static String ACTION_LOGIN_OUT = "loginout";	
	//返回值
	public static final String SUCCESS_RETURN_VALUE="ok";
	public static final String FAILED_RETURN_VALUE="fail";
	
}

