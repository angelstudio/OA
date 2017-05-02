package com.jdoa.tool;

public class DataSourceContextHolder {
	 private static final ThreadLocal contextHolder = new ThreadLocal(); // 线程本地环境 
	 
	 	/**
	 	 * 设置数据源类型 
	 	 * @author lyle
	 	 * @param dataSourceType
	 	 * Jul 25, 2013
	 	 */
	   public static void setDataSourceType(String dataSourceType) {
		   contextHolder.set(dataSourceType); 
	   
	   } 
	   
	   /**
	    * 获取数据源类型
	    * @author lyle
	    * @return
	    * Jul 25, 2013
	    */
	   public static String getDataSourceType() { 
		   return (String) contextHolder.get();
	    } 
	   
	   /**
	    * 清除数据源类型
	    * @author lyle
	    * Jul 25, 2013
	    */ 
	    public static void clearDataSourceType () { 
	    	contextHolder.remove();
	 }
}
