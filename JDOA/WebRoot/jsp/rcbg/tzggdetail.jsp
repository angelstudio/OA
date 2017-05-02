<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>通知公告详情</title>
	<link href="../../js/tool/ueditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../../js/tool/ueditor/third-party/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/tool/ueditor/third-party/template.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/tool/ueditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/tool/ueditor/umeditor.min.js"></script>
    <script type="text/javascript" src="../../js/tool/ueditor/lang/zh-cn/zh-cn.js"></script>
  </head>
  <style>
	#page_table input{
		height: 25px;
		width: 100%;
	}
	#page_table{
		width:100%;
		border-collapse: separate; 
		border-spacing: 15px;
	}
  </style>
  <script type="text/javascript">
  	var um;
   $(function(){
   	 um = UM.getEditor('myEditor');
   	 initForm();
   });
   
   function initForm(){
   	var fid = '${param.fid}';
   	$.ajax({
   		type:'POST',
   		url:'findOneInformDetail',
   		data:{fid:fid},
   		dataType:'text',
		contentType:'application/x-www-form-urlencoded; charset=utf-8',
		success:function(data){
			data = data.substring(1,data.length-1);
			var forms = data.split(",");
			$("input[name='ftitle']").val(forms[1]);
			um.setContent(forms[2]);
			$("input[name='fcreate_person']").val(forms[3]);
			$("input[name='fcreate_date']").val(forms[4]);
		}
   	});
   }
  </script>
  <body>
    <div style="text-align: center;color: red;">
		<h1>通知公告</h1>
	</div>
	<hr style="height:3px;border:none;border-top:3px double red;" />
	<table id="page_table">
		<tr>
			<td width="15%">创建人:</td>
			<td width="35%"><input type="text" name="fcreate_person" disabled="disabled"/></td>
			<td width="15%">创建时间:</td>
			<td width="35%"><input type="text" name="fcreate_date" disabled="disabled"/></td>
		</tr>
		<tr>
			<td>标题:</td>
			<td colspan="3"><input type="text" name="ftitle" disabled="disabled"></td>
		</tr>
		<tr>
			<td>正文:</td>
			<td></td>
		</tr>
	</table>
   	 <script type="text/plain" id="myEditor" style="width:100%;height:240px;"></script>
  </body>
</html>
