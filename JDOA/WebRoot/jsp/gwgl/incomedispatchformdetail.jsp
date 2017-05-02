<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>incomedispatchform1</title>
  </head>
    <script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
	<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
	<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
	<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
	<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
	<script type="text/javascript" src="../../js/tool/time.js"></script>	
<style>
#page_table input{
	height: 30px;
	width: 100%;
}
#page_table{
	border-collapse: separate; 
	border-spacing: 20px;
}
a:link {
 text-decoration: none;
}
a:hover {
 text-decoration: underline;
}
</style>
  <body>
    <div style="text-align: center;color: red;">
		<h1>厅高速公路监控结算中心文书处理签</h1>
	</div>
	<hr style="height:3px;border:none;border-top:3px double red;" />
	<div align="center">
	<form id="incomedispatchForm">
		<table id="page_table" >
			<tr>
				<td width="15%">收文日期:</td>
				<td width="35%">
					<input name="facceptdate" onkeypress="return false;" onfocus="SelectDate(this,'yyyy-MM-dd')" readOnly="true" style="background:url(../../image/basic/date.png) no-repeat 250px  4px;"/>
				</td>
				<td width="15%">紧急程度:</td>
				<td width="35%">
					<select name="fpriority" id="fpriority" style="width:100%;height:35px;">
	    				<option value="0">一般</option> 
						<option value="1">紧急</option> 
						<option value="2">特急</option> 
					</select>
				</td>
			</tr>
			<tr>
				<td>收文号:</td>
				<td><input type="text" name="fincome_no" /></td>
			</tr>
			<tr>
				<td>来文单位:</td>
				<td><input type="text" name="fincome_unit" /></td>
				<td>来文编号:</td>
				<td><input type="text" name="fincome_num"/></td>
			</tr>
			<tr>
				<td>创建人:</td>
				<td><input type="text" name="fediter" /></td>
				<td>创建日期:</td>
				<td><input type="text" name="fcreate_date"/></td>
			</tr>
			<tr>
				<td>来文标题:</td>
				<td colspan="3"><input type="text" name="ftitle"/></td>
			</tr>
			<tr>
				<td>领导阅示意见:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
			<tr>
				<td>传阅意见:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
				<td>承办情况:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
			<tr>
				<td>拟办意见:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
				<td>计算中心办公室:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
				<td>归档人:</td>
				<td><input type="text" name="farchiver" id="farchiver" readOnly="true"/></td>
			</tr>
			<tr>
				<td>拟办人:</td>
				<td><input type="text" name="fhanding_person" id="fhanding_person" readOnly="true"/></td>
			</tr>
			<tr>
				<td>附件:</td>
				<td id="file"></td>
			</tr>
		</table>
		</form>
		</div>
  </body>
	<script type="text/javascript">
   	$(function(){
	    $("form[id='incomedispatchForm'] :text").attr("disabled","disabled");    
	    $("form[id='incomedispatchForm'] textarea").attr("disabled","disabled");    
	    $("form[id='incomedispatchForm'] select").attr("disabled","disabled");    
	    $("form[id='incomedispatchForm'] :radio").attr("disabled","disabled"); 
	    //--------------------------表单初始化-------------------------------
	    var fid = '${param.fid}';
	    $.ajax({
  			type:'POST',
  			url:'getOneIncomDispatchByFid',
  			data:{fid:fid},
  			dataType:'json',
		    contentType:'application/x-www-form-urlencoded; charset=utf-8',
  			success:function(forms){
				$("input[name='facceptdate']").val(forms[13]);
				$("#fpriority option[value="+forms[3]+"]").attr('selected','selected');
				$("input[name='fincome_no']").val(forms[4]);
				$("input[name='fincome_unit']").val(forms[2]);
				$("input[name='fincome_num']").val(forms[5]);
				$("input[name='ftitle']").val(forms[1]);
				$("input[name='farchiver']").val(forms[10]);
				$("input[name='fhanding_person']").val(forms[12]);
				$("input[name='fediter']").val(forms[16]);
				$("input[name='fcreate_date']").val(forms[17]);
				var linkStr = "";
				for(var i = 0; i < forms[18].length; i++){
					linkStr += "<li>"+forms[18][i].ffiletitle+"&nbsp;<a href='downloadIncomeDispatchFile?fid="+forms[18][i].ffileid+"'>下载</a>&nbsp;&nbsp;<a href='readfile?fid="+forms[18][i].ffileid+"'>预览</a></li>";
				}
				$('#file').html(linkStr);
  			}
  		});
  	});
  </script>
</html>
