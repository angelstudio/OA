<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>dispatchform1</title>
  </head>
     <script type="text/javascript" src="../../js/tool/ueditor/third-party/jquery.min1.7.js"></script>
	<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
	<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
	<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
	<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
	<link href="../../js/tool/ueditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../../js/tool/ueditor/third-party/template.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/tool/ueditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/tool/ueditor/umeditor.min.js"></script>
    <script type="text/javascript" src="../../js/tool/ueditor/lang/zh-cn/zh-cn.js"></script>
<style>
#page1_table1 input{
	width: 0px;
	margin-left: 30px;
}
#page2_table2 input{
	height: 30px;
	width: 100%;
}
#page1_table1,#page2_table2{
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
		<form id="dispatchForm">
		<div id="tabletitle" align="center"><img src=""></div>
		<table id="page1_table1" >
			<tr>
				<td width="10%">行文类型：</td>
				<td>
					<input name="ftype" type="radio" value="0" checked="checked"/>一般
					<input name="ftype" type="radio" value="1" />上行
					<input name="ftype" type="radio" value="2" />下行
				</td>
			</tr>
			</table>
			<table id="page2_table2">
			<tr>
				<td width="12%">发文单位:</td>
				<td width="38%">
					<select name="funit" style="width:100%;height:35px;">
	    				<option value="湘交结">湘交结</option> 
						<option value="湘交研">湘交研</option> 
						<option value="湘智司">湘智司</option> 
						<option value="湘交结算党">湘交结算党</option> 
						<option value="湘交结算工">湘交结算工</option> 
						<option value="湘交结算纪">湘交结算纪 </option> 
					</select>
				</td>
				<td width="12%">紧急程度:</td>
				<td width="38%">
					<select name="fpriority" id="fpriority" style="width:100%;height:35px;">
	    				<option value="0">一般</option> 
						<option value="1">紧急</option> 
						<option value="2">特急</option> 
					</select>
				</td>
			</tr>
			<tr>
				<td>当前状态:</td>
				<td>
					<select name="fstatu" id="fstatu" style="width:100%;height:35px;">
	    				<option value="0">归档</option> 
						<option value="1">会签</option> 
						<option value="2">签发</option> 
					</select>
				</td>
				<td>创建日期:</td>
				<td><input type="text" name="fcreate_date" /></td>
			</tr>
			<tr>
				<td>发文号:</td>
				<td><input type="text" name="fdispatch_no"  /></td>
				<td>发文日期:</td>
				<td><input type="text" name="fdispatch_date" /></td>
			</tr>
			<tr>
				<td>签发:</td>
				<td><input type="text" name="fissuer" /></td>
				<td>会签:</td>
				<td><input type="text" name="fsigner" /></td>
			</tr>
			<tr>
				<td>主送:</td>
				<td colspan="3"><input type="text" name="fmain_send" /></td>
			</tr>
			<tr>
				<td>抄送:</td>
				<td colspan="3"><input type="text" name="fcopy_send" /></td>
			</tr>
			<tr>
				<td>拟稿单位:</td>
				<td><input type="text" name="fdraftunit" value="${user.person.fssbm}" /></td>
				<td>拟稿:</td>
				<td><input type="text" name="fediter" value="${user.person.fname}" /></td>
			</tr>
			<tr>
				<td>核稿:</td>
				<td><input type="text" name="fnuclearpeople" /></td>
				<td>审稿:</td>
				<td><input type="text" name="freviewer" /></td>
			</tr>
			<tr>
				<td>打印:</td>
				<td><input type="text" name="fprint" /></td>
				<td>份数:</td>
				<td><input type="text" name="fpart" /></td>
			</tr>
			<tr>
				<td>标题:</td>
				<td colspan="3"><input type="text" name="ftitle" /></td>
			</tr>
			<tr>
				<td>正文:</td>
				<td colspan="3"><script type="text/plain" id="myEditor" style="width:100%;height:240px;"></script></textarea><td>
			</tr>
			<tr>
				<td width="14%">审稿或协助人:</td>
				<td colspan="3"><input type="text" name="fassist" id="fassist" readOnly="true" /></td>
			</tr>
			<tr>
				<td>附件:</td>
				<td id="file"></td>
			</tr>
		</table>
		</form>
  </body>
	<script type="text/javascript">
    $("form[id='dispatchForm'] :text").attr("disabled","disabled");    
    $("form[id='dispatchForm'] textarea").attr("disabled","disabled");    
    $("form[id='dispatchForm'] select").attr("disabled","disabled");    
    $("form[id='dispatchForm'] :radio").attr("disabled","disabled"); 
   	var fid =  '${param.fid}';
   	var um = UM.getEditor('myEditor');
   	$(function(){
   		$.ajax({
   			type:'POST',
			url : 'queryDispatchDetail',
			data:{fid:fid},
			dataType:'json',
			contentType:'application/x-www-form-urlencoded; charset=utf-8',
			success:function(forms){
				insertForm(forms);
			}
   		});
  	});
  	function insertForm(dataArray){
  				//设置表头
				$("input[name='ftype']").eq(dataArray[3]).attr("checked", true);
				$("input[name='funit']").each(function(){
					if($(this).val() == dataArray[2]){
						$(this).attr('checked','true');
					}
				});
				$("#fpriority option[value="+dataArray[4]+"]").attr('selected','selected');
				$("#fstatu option[value="+dataArray[16]+"]").attr('selected','selected');
				$("input[name='fdispatch_no']").val(dataArray[5]);
				$("input[name='fdispatch_date']").val(dataArray[6]);
				$("input[name='fissuer']").val(dataArray[7]);
				$("input[name='fsigner']").val(dataArray[8]);
				$("input[name='fmain_send']").val(dataArray[9]);
				$("input[name='fcopy_send']").val(dataArray[10]);
				$("input[name='fdraftunit']").val(dataArray[11]);
				$("input[name='fediter']").val(dataArray[19]);
				$("input[name='fnuclearpeople']").val(dataArray[12]);
				$("input[name='freviewer']").val(dataArray[13]);
				$("input[name='fprint']").val(dataArray[20]);
				$("input[name='fpart']").val(dataArray[18]);
				$("input[name='ftitle']").val(dataArray[1]);
				um.setContent(dataArray[14]);
				$("input[name='fassist']").val(dataArray[17]);
				$('#tabletitle img').attr('src','img/'+dataArray[21].trim());
				$("input[name='fcreate_date']").val(dataArray[22]);
				var linkStr = "";
				for(var i = 0; i < dataArray[23].length; i++){
					linkStr += "<li>"+dataArray[23][i].ffiletitle+"&nbsp;<a href='downloadDispatchFile?fid="+dataArray[23][i].ffileid+"'>下载</a>&nbsp;&nbsp;<a href='readfile?fid="+dataArray[23][i].ffileid+"'>预览</a></li>";
				}
				$('#file').html(linkStr);
  	}
  </script>
</html>
