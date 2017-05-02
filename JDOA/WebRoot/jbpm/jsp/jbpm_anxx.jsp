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
		<script type="text/javascript" src="../../js/tool/urlUtil.js"></script>
		<script type="text/javascript" src="../js/BPJbpmCommon.js"></script>
		<link rel="stylesheet" type="text/css"  href="../../css/tool/ui/default/om-default.css" />

	</head>
	<script type="text/javascript">
	$(document).ready(function() {
	    var fname=request("fname");
		$('#grid').omGrid( {
			height : 450,
			autoFit : true,
			limit : 10, //分页显示，每页显示8条
			colModel : [ {
				header : '按钮名称',
				name : 'fname',
				width : 100,
				align : 'center',
				editor:{rules:["required",true,"名称为必填项"]}
			}, {
				header : '按钮CLASS',
				name : 'fanclass',
				width : 100,
				align : 'left',
				sort : 'clientSide',
				editor:{}
			}, {
				header : '按钮ID',
				name : 'fanid',
				width : 80,
				align : 'left',
				editor:{rules:["required",true,"名称为必填项"]},
				renderer:lxjx
			},
			{
				header : '按钮方法',
				name : 'fanff',
				width : 100,
				align : 'left',
				editor:{rules:["required",true,"名称为必填项"]}
			},
			{
				header : '排序',
				name : 'fpx',
				width : 100,
				align : 'left',
				editor:{rules:["required",true,"名称为必填项"]}
			},
			{
				header : '表单名称',
				name : 'fbdmc',
				width : 100,
				align : 'left'
			},
			],
			dataSource : 'jbpm_ANXX_List',
			extraData:{'fname':fname}
		});

		//新增
			$('#add').click(function() {
				$('#grid').omGrid('insertRow',0,{'fbdmc':fname});
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
            	  $.ajax({
  						type: 'POST',
  						url:'jbpm_ANXX_Deal',
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
			});
			$("#fzdpz").click(function(){
			   openNewTabByName('字段信息','fzdxx','fzdxx');
			});
			$('#toolbar').omButtonbar({});
		});
        function lxjx(value){
          if(value=='1'){
           return '文本'
          }else if(value=='2'){
           return '数字'
          }else{
           return value
          }
        }
	
	
</script>
	<div id="toolbar">
		<input type="button" id="add" value="新增" />
		<input type="button" id="edit" value="修改" />
		<input type="button" id="fsave" value="保存" />
		<input type="button" id="del" value="删除" />
	</div>
		<div>
			<table id="grid"></table>
		</div>
</html>