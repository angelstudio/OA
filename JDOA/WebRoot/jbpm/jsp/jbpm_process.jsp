<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="GBK"%>
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
				header : '编号',
				name : 'fnumber',
				width : 100,
				align : 'center',
				sort : 'clientSide'
			}, {
				header : '名称',
				name : 'fname',
				width : 100,
				align : 'left',
				sort : 'clientSide'
			}, {
				header : '状态',
				name : 'fstatus',
				width : 80,
				align : 'left',
				sort : 'clientSide',
				renderer : readValue
			},
			{
				header : '部署状态',
				name : 'fdeploymentid',
				width : 80,
				align : 'left',
				sort : 'clientSide'
			}],
			dataSource : 'jbpm_getProcessList.action'//后台取数的URL
		});

		//新增
			$('#add').click(function() {
				openProcessDesign();
			});
			//修改 暂不实现
			$('#edit').click(
					function() {
						var selectedRecords = $('#grid').omGrid(
								'getSelections', true);
						if (selectedRecords.length <= 0) {
							alert('请选择修改的记录！');
							return false;
						}
						var fstatus = selectedRecords[0].fstatus;
						//if (fstatus == 2) {
						//	alert('流程已启用,不能修改');
						//	return false;
						//}
						window
								.open('../wf/editindex.html?process_id='
										+ selectedRecords[0]['fid']
										+ "&process_number="
										+ selectedRecords[0]['fnumber']
										+ "&process_name="
										+ selectedRecords[0]['fname']);
					});
			//删除  先检查流程是否禁用
			$('#del').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("请先选择要删除的流程");
					return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				if (fstatus == 2) {
					alert('流程已启用,不能删除');
					return false;
				}
				if (checkBox("确定删除选择的记录吗？")) {
					deleteProcess(fprocessId);
				}
			});
			//流程启用
			$('#start').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("请先选择要启用的流程。");
					return false;
				}
				if(processRecords[0].deploymentID=='未部署'){
				  alert("请先部署");
				  return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				startProcess(fprocessId);
			});
			//流程停用
			$('#stop').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("请先选择要启用的流程。");
					return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				stopProcess(fprocessId);
			});
			//启用 检查当前流程状态。
			$('#deploy').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("请先选择要启用的流程。");
					return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				deployProcess(fprocessId);
			});
			//禁用 检查当前状态。
			$('#undeploy').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("请先选择要停用的流程");
					return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				if (fstatus != 2) {
					//alert('该流程没有启用');
				}
				undeployProcess(fprocessId);
			});
		});

	//流程反部署成功。
	function undeployProcess(fprocessId) {
		var param = "processid=" + fprocessId;
		sendAjaxReq("post", "jbpm_unDeployProcess", param, function(request) {
			if (request.responseText == "ok") {
				alert("流程禁用成功");
			} else if (request.responseText == "fail") {
				alert("流程禁用失败");
			} else if (request.responseText == "exsit") {
				alert("有未结束的流程,禁用失败");
			}
			$('#grid').omGrid('reload');
		}, null, null, null);
	}

	//流程部署成功。
	function deployProcess(fprocessId) {
		var param = "processid=" + fprocessId;
		sendAjaxReq("post", "jbpm_deployProcess", param, function(request) {
			if (request.responseText == "ok") {
				alert("流程部署成功");
			} else {
				alert("流程部署失败");
			}
			$('#grid').omGrid('reload');
		}, null, null, null);
	}
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
	//停用流程
	function stopProcess(fprocessId) {
		$.ajax( {
			type : 'POST',
			url : 'stopProcess',
			data : "fprocessId=" + fprocessId,
			dataType : 'text',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(request) {
				if (request == 'ok') {
					alert('停用成功');
				} else {
					alert('停用失败');
				}
				$('#grid').omGrid('reload');
			}
		});
	}
	//删除流程
	function deleteProcess(fprocessId) {
		$.ajax( {
			type : 'POST',
			url : 'deleteProcess',
			data : "fprocessId=" + fprocessId,
			dataType : 'text',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(request) {
				if (request == 'ok') {
					alert('删除成功');
				} else {
					alert('删除失败');
				}
				$('#grid').omGrid('reload');
			}
		});
	}
	//打开流程界面
	function openProcessDesign(process_id) {
		var name_text = '工作流设计';
		ifh = window.parent.$('#center-tab').height();
		window.parent
				.$('#center-tab')
				.omTabs(
						"add",
						{
							title : name_text,
							content : "<iframe id='123456'"
									+ "' border=0 frameBorder='no' name='process-main-frame' src='../wf/index.html"
									+ "' width='100%' height='" + ifh
									+ "'>__tag_159$20_",
							closable : true
						});
	}

	function readValue(colValue, rowData, rowIndex) {
		if (colValue == 1) {
			return "保存";
		} else if (colValue == 2) {
			return "启用";
		} else {
			return "保存";
		}
	};
	function readTime(colValue, rowData, rowIndex) {
		if (colValue != null) {
			return getFormatDate(new Date(colValue.time), null);
		}
	}
</script>
	<div id="demo">
		<input type="button" id="add" value="新增" />
		<input type="button" id="edit" value="修改" />
		<input type="button" id="del" value="删除" />
		<input type="button" id="start" value="启用" />
		<input type="button" id="stop" value="停用" />
		<input type="button" id="deploy" value="部署 " />
		<input type="button" id="undeploy" value="反部署 " />
		<div>
			<table id="grid"></table>
		</div>
	</div>
</html>
