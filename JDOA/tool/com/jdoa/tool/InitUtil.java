package com.jdoa.tool;

import java.util.HashMap;
import java.util.Map;

public class InitUtil {

	private static Map baseAuthorityMap;
//	private static String[] NotNeedInterceptCommonActions = {"getBillURL","getDataCenters","findCacheMenu","getMaterialColModel","getEntryColModel"};  //不需要拦截器拦截的共有action
	private static String[] NeedInterceptCommonActions = {"action_show","action_list","action_save","action_delete"};  //需要拦截器拦截的共有action
	
	public static Map getBaseAuthorityMapList(){
		if(baseAuthorityMap==null){
			baseAuthorityMap = new HashMap();
			baseAuthorityMap.put("action_show", "新增");
			baseAuthorityMap.put("action_delete", "删除");
			baseAuthorityMap.put("action_save", "修改");
			baseAuthorityMap.put("action_list", "查看");
		}
		
		return baseAuthorityMap;
	}

//	public static String[] getNotNeedInterceptCommonActions() {
//		return NotNeedInterceptCommonActions;
//	}
//
//	public static void setNotNeedInterceptCommonActions(
//			String[] notNeedInterceptCommonActions) {
//		NotNeedInterceptCommonActions = notNeedInterceptCommonActions;
//	}

	public static String[] getNeedInterceptCommonActions() {
		return NeedInterceptCommonActions;
	}

	public static void setNeedInterceptCommonActions(
			String[] needInterceptCommonActions) {
		NeedInterceptCommonActions = needInterceptCommonActions;
	}

	
}
