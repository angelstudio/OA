<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>updfile</title>
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
		<h1>编辑文件</h1>
	</div>
	<hr />
	<div align="center">
	<form id="updForm">
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
				<td width="20%">创建时间:</td>
				<td width="30%">
					<input type="text" name="fcreate_date" disabled="disabled"/>
				</td>
				<td >创建人:</td>
				<td >
					<input type="text" name="fcreate_person" disabled="disabled"/>
				</td>
			</tr>
			<tr>
				<td ><span style="color:red">*</span>文件标题:</td>
				<td colspan="2">
					<input type="text" name="ftitle" id="ftitle"/>
				</td>
				<td><span style="color:gray" id="fileType"></span></td>
			</tr>
			<tr>
				<td width="15%">关键词:</td>
				<td colspan="3">
					<input type="text" name="fkeyword" id="fkeyword"/>
				</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align: right;"><button type="button" onclick="updFile()">保存</button>&nbsp;&nbsp;<button type="button" onclick="onCancel()">取消</button></td>
			</tr>
		</table>
		</form>
		</div>
  </body>
	<script type="text/javascript">
		var fid = '${param.fid}';
		$(function(){
			initForm();
		});
		//初始化表单
		function initForm(){
			$.ajax({
				type:'POST',
				url:'findOneFile',
				data:{fid:fid},
				success:function(data){
					data = data.substring(1,data.length-1);
					var forms = data.split(",");
					var ftitle = forms[1].substring(0,forms[1].lastIndexOf("."));
					var fileType = forms[1].substring(forms[1].lastIndexOf("."));
					$("input[name='ftitle']").val(ftitle);
					$('#fileType').text(fileType);
					$("input[name='funit']").val(forms[2]);
					$("input[name='fdispatch_no']").val(forms[3]);
					$("input[name='fkeyword']").val(forms[4]);
					$("input[name='fcreate_person']").val(forms[6]);
					$("input[name='fcreate_date']").val(forms[5]);
				}
			});
		}
	  function onCancel(){
	  	//清空表单内容
		document.getElementById("updForm").reset();
		//关闭模态框
		window.parent.closeModel();
	  }
	  function updFile(){
	  	var ftitle = $("input[name='ftitle']").val()+$('#fileType').text();
	  	var fkeyword = $("input[name='fkeyword']").val();
	  	$("input[name='ftitle']").val($.trim(ftitle));
	  	$("input[name='fkeyword']").val($.trim(fkeyword));
	  	$.ajax({
	  		type:'POST',
	  		url:'updOneFile',
	  		data:$('#updForm').serialize()+"&fid="+fid,
	  		success:function(data){
	  			//清空表单内容
				document.getElementById("updForm").reset();
				//关闭模态框
				window.parent.closeModel();
				//重新加载表格
			    window.parent.reloadgrid();
	  		}
	  	});
	  }
    </script>
</html>
