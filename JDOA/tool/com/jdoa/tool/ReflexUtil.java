package com.jdoa.tool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflexUtil {


	/**
	 * 获取某对象的属性
	 * @author lyle
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws Exception
	 * May 21, 2013
	 */
	public static Object getProperty(Object obj, String fieldName)
	              throws Exception {
	     Class ownerClass = obj.getClass();
	     Field field = ownerClass.getField(fieldName);
	     Object property = field.get(obj);
	     return property;
	 }
	
	/**
	 * 获取某个类的静态属性
	 * @author lyle
	 * @param className
	 * @param fieldName
	 * @return
	 * @throws Exception
	 * May 21, 2013
	 */
	public static Object getStaticProperty(String className, String fieldName)
	throws Exception {
		Class ownerClass = Class.forName(className);
		Field field = ownerClass.getField(fieldName);
		Object property = field.get(ownerClass);
		return property;
	}
	
	
	/**
	 * 根据类名获取类实例对象
	 * @author lyle
	 * @param className
	 * @return
	 * May 21, 2013
	 */
	public static Object getClassObj(String className){
		try {
			Class c = Class.forName(className);
			return  c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 执行某对象的方法
	 * @author lyle
	 * @param obj	对象名 
	 * @param methodName	方法名
	 * @param args	方法参数
	 * @return 执行方法的返回值
	 * @throws Exception
	 * May 21, 2013
	 */
	public static Object invokeMethod(Object obj, String methodName, Object[] args)
			throws Exception {
		Class ownerClass = obj.getClass();
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			if(args[i]==null){
				args[i]="";
			}
			argsClass[i] = args[i].getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(obj, args);
	}
	
	/**
	 * 执行某个类的方法
	 * @author lyle
	 * @param obj
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 * May 21, 2013
	 */
	public static Object invokeMethod(String className, String methodName, Object[] args)
	throws Exception {
		Class ownerClass = Class.forName(className);
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			if(args[i]==null){
				args[i]="";
			}
			argsClass[i] = args[i].getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(ownerClass.newInstance(), args);
	}
	
	
	/**
	 * 执行某个类的静态方法
	 * @author lyle
	 * @param className
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 * May 21, 2013
	 */
	public static Object invokeStaticMethod(String className, String methodName,
		             Object[] args) throws Exception {
	     Class ownerClass = Class.forName(className);
	     Class[] argsClass = new Class[args.length];
	     for (int i = 0, j = args.length; i < j; i++) {
	    	 if(args[i]==null){
					args[i]="";
				}
	         argsClass[i] = args[i].getClass();
	      }
	     Method method = ownerClass.getMethod(methodName, argsClass);
	     return method.invoke(null, args);
	}
	
	/**
	 * 执行某个对象的静态方法
	 * @author lyle
	 * @param className
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 * May 21, 2013
	 */
	public static Object invokeStaticMethod(Object obj, String methodName,
			Object[] args) throws Exception {
		Class ownerClass = obj.getClass();
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			if(args[i]==null){
				args[i]="";
			}
			argsClass[i] = args[i].getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(null, args);
	}

}
