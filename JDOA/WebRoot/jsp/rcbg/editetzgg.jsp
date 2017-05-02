<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>编辑通知公告</title>
	<link href="../../js/tool/ueditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../../js/tool/ueditor/third-party/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/tool/ueditor/third-party/template.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/tool/ueditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/tool/ueditor/umeditor.min.js"></script>
    <script type="text/javascript" src="../../js/tool/ueditor/lang/zh-cn/zh-cn.js"></script>
  </head>
  <script type="text/javascript">
  	var um;
   $(function(){
   	 um = UM.getEditor('myEditor');
   	 var opera = '${param.opera}';
   	 if(opera == 'add'){
   	 	$('#fabu').attr('onclick','addtzgg()');
   	 }else if(opera == 'upd'){
   	 	var arr = '${param.forms}';
   	 	var forms = arr.split(",");
   	 	$("input[name='ftitle']").val(forms[1]);
   	 	um.setContent(forms[2]);
   	 	$('#fabu').attr('onclick',"updtzgg('"+forms[0]+"')");
   	 }
   });
   
   function addtzgg(){
   	var ftitle = $("input[name='ftitle']").val();
   	var fbody = um.getContent();
   	$.ajax({
   		type:'POST',
   		url:'addInform',
   		data:{ftitle:ftitle,fbody:fbody},
   		dataType:'text',
		contentType:'application/x-www-form-urlencoded; charset=utf-8',
		success:function(data){
			if(data=='ok'){
		    	//清空表单内容
				$("input[name='ftitle']").val("");
		        //关闭模态框
		        window.parent.closeModel();
		       //重新加载表格
		       	window.parent.reloadgrid();
		    }
		}
   	});
   }
   
   function updtzgg(fid){
   	var ftitle = $("input[name='ftitle']").val();
   	var fbody = um.getContent();
   	$.ajax({
   		type:'POST',
   		url:'updInform',
   		data:{ftitle:ftitle,fbody:fbody,fid:fid},
   		dataType:'text',
		contentType:'application/x-www-form-urlencoded; charset=utf-8',
		success:function(data){
			if(data=='ok'){
		    	//清空表单内容
				$("input[name='ftitle']").val("");
		        //关闭模态框
		        window.parent.closeModel();
		       //重新加载表格
		       	window.parent.reloadgrid();
		    }
		}
   	});
   }
   
   function onCancel(){
   	//清空表单内容
	$("input[name='ftitle']").val("");
	//关闭模态框
	window.parent.closeModel();
   }
  </script>
  <body>
    <div style="text-align: center;color: red;">
		<h1>通知公告</h1>
	</div>
	<hr style="height:3px;border:none;border-top:3px double red;" />
	 <tr><td>标题:</td><td><input type="text" name="ftitle" style="width: 99%;height: 25px;margin: 5px;margin-left: 0px;"></td></tr><br/>
	 <tr><td>正文:</td></tr>
   	 <script type="text/plain" id="myEditor" style="width:100%;height:240px;"></script>
   	 <div align="right" style="margin-top:20px;"><button type="button" id="fabu">发布</button><button type="button" style="margin-left: 15px;" onclick="onCancel()">取消</button></div>
  </body>
</html>
