package com.jdoa.tool;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class EncodeFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request2, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 过滤器工作的处理方法
		//System.out.println("过滤器工作中");
		HttpServletRequest request =(HttpServletRequest)request2;
	    String method =	request.getMethod();
	    
	    if("POST".equals(method)){
	    	request.setCharacterEncoding("UTF-8");
	    }else{
	    	//get请求
	    	//从请求中获取所有的参数放入集合Map中，从集合中取出每一个参数的value进行转码
	    	//得到所有请求中参数的键值对对象
			Map map = request.getParameterMap();
			//将map中每一个值进行编码转换
			Set keySet = map.keySet();
			Iterator it = keySet.iterator();
			while(it.hasNext()){
				Object value = map.get(it.next());
				//判断value是否为字符串数组
				if(value instanceof String[]){
					String[] values = (String[]) value;
					for(int i=0;i<values.length;i++){
						values[i] = new String(values[i].getBytes("ISO-8859-1"),"utf-8");
					}
				}else if(value instanceof String){
					String strValue = (String) value;
					strValue = new String(strValue.getBytes("ISO-8859-1"),"utf-8");
				}
			}

	    }
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}


}
