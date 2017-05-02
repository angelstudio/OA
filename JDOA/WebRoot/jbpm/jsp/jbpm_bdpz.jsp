<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
		<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
		<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
		<script type="text/javascript" src="../../js/tool/common.js"></script>
		<script type="text/javascript" src="../js/json2.js"></script>
		<script type="text/javascript" src="../js/BPJbpmCommon.js"></script>
		<link rel="stylesheet" type="text/css"  href="../../css/tool/ui/default/om-default.css" />

	</head>
	<script type="text/javascript">
	$(document).ready(function() {
		$('#grid').omGrid( {
			height : 450,
			autoFit : true,
			limit : 10, //分页显示，每页显示8条
			colModel : [ {
				header : '表单名称',
				name : 'fname',
				width : 100,
				align : 'center',
				editor:{rules:["required",true,"为必填项"]}
			}, {
				header : '别名',
				name : 'fbm',
				width : 100,
				align : 'left',
				sort : 'clientSide',
				editor:{rules:["required",true,"为必填项"]}
			}, {
				header : '关联表',
				name : 'ftable',
				width : 80,
				align : 'left',
				editor:{rules:["required",true,"为必填项"]}
			},
			{
				header : 'URL路径',
				name : 'furl',
				width : 80,
				align : 'left',
				editor:{rules:["required",true,"为必填项"]}
			},
			{
				header : '备注',
				name : 'fbz',
				width : 100,
				align : 'left',
				editor:{}
			}],
			dataSource : 'jbpm_Form_List'//后台取数的URL
		});

		//新增
			$('#add').click(function() {
				$('#grid').omGrid('insertRow',0,{});
			});
			//修改 暂不实现
			$('#edit').click(function() {
						var selectedRecords = $('#grid').omGrid(
								'getSelections');
						if (selectedRecords.length <= 0) {
							alert('请选择修改的记录！');
							return false;
						};
						$("#grid").omGrid("editRow" , selectedRecords);
					});
			//删除  先检查流程是否禁用
			$('#del').click(function() {
				var records = $('#grid').omGrid('getSelections');
				var len = records.length;
				if (len < 1) {
					alert("请先选择要删除的纪录");
					return false;
				}
                $('#grid').omGrid('deleteRow',records[0]);
			});
			$("#fsave").click(function(){
			     
			      var formData = $('#grid').omGrid('getChanges');
            	  var formDataStr = JSON.stringify(formData);
            	  var formAdd=$('#grid').omGrid('getChanges','insert');
            	  var formAddStr = JSON.stringify(formAdd);
            	  //保存之前数据重复检测
            	   $.ajax({
  						type: 'POST',
  						url:'jbpm_Form_SJJC',
 						data:"json="+formAddStr,
  						dataType:'json',
  						contentType:'application/x-www-form-urlencoded; charset=utf-8',
  						 success: function(json){
  						 		if(json.flag=='ok'){
  						 			/*****此处传递data到后台并处理*******/
            	   $.ajax({
  						type: 'POST',
  						url:'jbpm_Form_Deal',
 						data:"json="+formDataStr,
  						dataType:'json',
  						contentType:'application/x-www-form-urlencoded; charset=utf-8',
  						 success: function(json){
  						 		if(json.returnValue=='ok'){
  						 			 alert('保存成功');
            						 $('#grid').omGrid('reload');
  						 		}else {
  						 			 alert('保存失败');
  						 		}
   							 }
						});
  						 			
  						 		}else {
  						 			var fname=json.text;
  						 			alert(fname+"已存在");
  						 		}
   							 }
						});
            	
			});
			$("#fzdpz").click(function(){
			  var selectedRecords = $('#grid').omGrid('getSelections',true);
			  if (selectedRecords.length < 1) {
					alert("请先选择纪录");
					return false;
				}
              var fname = selectedRecords[0].fname;
			   openNewTabByName('字段信息','../../jbpm/jsp/jbpm_zdxx.jsp?fname='+fname,'fzdxx');
			});
			$("#fanpz").click(function(){
			  var selectedRecords = $('#grid').omGrid('getSelections',true);
			  if (selectedRecords.length < 1) {
					alert("请先选择纪录");
					return false;
				}
              var fname = selectedRecords[0].fname;
			   openNewTabByName('按钮信息','../../jbpm/jsp/jbpm_anxx.jsp?fname='+fname,'fanxx');
			});
			$("#fqqpz").click(function(){
			  var selectedRecords = $('#grid').omGrid('getSelections',true);
			  if (selectedRecords.length < 1) {
					alert("请先选择纪录");
					return false;
				}
              var fname = selectedRecords[0].fname;
			   openNewTabByName('请求配置信息','../../jbpm/jsp/jbpm_qqpzxx.jsp?fname='+fname,'fqqpzxx');
			});
			$('#toolbar').omButtonbar({});
		});

	
	//启用流程
	function startProcess(fprocessId) {
		$.ajax( {
			type : 'POST',
			url : 'startProcess',
			data : "fprocessId=" + fprocessId,
			dataType : 'text',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(request) {
				if (request == 'ok') {
					alert('启动成功');
				} else {
					alert('启动失败');
				}
				$('#grid').omGrid('reload');
			}
		});
	}
	
	
</script>
	<div id="toolbar">
		<input type="button" id="add" value="新增" />
		<input type="button" id="edit" value="修改" />
		<input type="button" id="fsave" value="保存" />
		<input type="button" id="del" value="删除" />
		<input type="button" id="fzdpz" value="字段配置" />
		<input type="button" id="fanpz" value="按钮配置" />
		<input type="button" id="fqqpz" value="请求配置" />
	</div>
		<div>
			<table id="grid"></table>
		</div>
</html>