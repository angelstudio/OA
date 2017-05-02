<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>uploadfile</title>
  </head>
    <script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
	<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
	<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
	<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
	<script type="text/javascript" src="../../js/tool/ajaxfileupload.js"></script>
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
	<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
<style>
#page_table input{
	height: 30px;
	width: 100%;
}
#page_table{
	border-collapse: separate; 
	border-spacing: 20px;
}
</style>
  <body>
    <div style="text-align: center;">
		<h1>文件上传</h1>
	</div>
	<hr />
	<div align="center">
	<form id="uploadForm">
		<table id="page_table" >
			<tr>
				<td width="20%">发文机关:</td>
				<td width="30%">
					<input type="text" name="funit" disabled="disabled"/>
				</td>
				<td >文号:</td>
				<td >
					<input type="text" name="fdispatch_no" disabled="disabled"/>
				</td>
			</tr>
			<tr>
				<td ><span style="color:red">*</span>文件标题:</td>
				<td colspan="3">
					<input type="text" name="ftitle" id="ftitle"/>
				</td>
			</tr>
			<tr>
				<td width="15%">关键词:</td>
				<td colspan="3">
					<input type="text" name="fkeyword" id="fkeyword"/>
				</td>
			</tr>
			<tr>
				<td width="15%"><span style="color:red">*</span>文件:</td>
				<td colspan="3">
					<input type="file" name="file" id="file" />
				</td>
			</tr>
			<tr>
				<td width="15%"></td>
				<td colspan="3">
					<span style="color:gray">允许.doc、.docx、.xls、.xlsx、.ppt、.pptx、.pdf、.txt、.zip、.rar格式文件上传</span>
				</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align: right;"><button type="button" onclick="fileUpload()">上传</button>&nbsp;&nbsp;<button type="button" onclick="onCancel()">取消</button></td>
			</tr>
		</table>
		</form>
		</div>
  </body>
	<script type="text/javascript">
	   function fileUpload() {  
	   		var fid = '${param.fid}';
	   		var ftitle = $('#ftitle').val();
	   		var fkeyword = $('#fkeyword').val();
	   		if(ftitle == null || ftitle==""){
	   			alert("文件标题必须填写!");
	   			return;
	   		}
		    $.ajaxFileUpload( {  
		    	type:'POST',
		        url : 'uploadfile',			//用于文件上传的服务器端请求地址  
		        secureuri : false,          //一般设置为false  
		        fileElementId : 'file',     //文件上传空间的id属性  <input type="file" id="file" name="file" />  
		        data:{fid:fid,ftitle:ftitle,fkeyword:fkeyword},
		        dataType : 'text',          
		        success : function(data, status) {  
		        	data = data.substring(data.indexOf(">")+1,data.lastIndexOf("<"));
		        	if(data == 'ok'){
			        	//清空表单内容
			           	document.getElementById("uploadForm").reset();
			           	//关闭模态框
			           	window.parent.closeModel();
			           	//重新加载表格
			           	window.parent.reloadgrid();
		        	}else if(data == 'error'){
		        		alert("文件格式错误!");
		        	}else{
		        		alert("系统内部错误");
		        	}
		        }  
		    }) ; 
	  }  
	  function onCancel(){
	  	//清空表单内容
		document.getElementById("uploadForm").reset();
		//关闭模态框
		window.parent.closeModel();
	  }
  </script>
</html>
