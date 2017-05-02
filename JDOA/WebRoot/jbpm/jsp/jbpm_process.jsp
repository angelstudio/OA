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
			limit : 10, //��ҳ��ʾ��ÿҳ��ʾ8��
			colModel : [ {
				header : '���',
				name : 'fnumber',
				width : 100,
				align : 'center',
				sort : 'clientSide'
			}, {
				header : '����',
				name : 'fname',
				width : 100,
				align : 'left',
				sort : 'clientSide'
			}, {
				header : '״̬',
				name : 'fstatus',
				width : 80,
				align : 'left',
				sort : 'clientSide',
				renderer : readValue
			},
			{
				header : '����״̬',
				name : 'fdeploymentid',
				width : 80,
				align : 'left',
				sort : 'clientSide'
			}],
			dataSource : 'jbpm_getProcessList.action'//��̨ȡ����URL
		});

		//����
			$('#add').click(function() {
				openProcessDesign();
			});
			//�޸� �ݲ�ʵ��
			$('#edit').click(
					function() {
						var selectedRecords = $('#grid').omGrid(
								'getSelections', true);
						if (selectedRecords.length <= 0) {
							alert('��ѡ���޸ĵļ�¼��');
							return false;
						}
						var fstatus = selectedRecords[0].fstatus;
						//if (fstatus == 2) {
						//	alert('����������,�����޸�');
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
			//ɾ��  �ȼ�������Ƿ����
			$('#del').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("����ѡ��Ҫɾ��������");
					return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				if (fstatus == 2) {
					alert('����������,����ɾ��');
					return false;
				}
				if (checkBox("ȷ��ɾ��ѡ��ļ�¼��")) {
					deleteProcess(fprocessId);
				}
			});
			//��������
			$('#start').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("����ѡ��Ҫ���õ����̡�");
					return false;
				}
				if(processRecords[0].deploymentID=='δ����'){
				  alert("���Ȳ���");
				  return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				startProcess(fprocessId);
			});
			//����ͣ��
			$('#stop').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("����ѡ��Ҫ���õ����̡�");
					return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				stopProcess(fprocessId);
			});
			//���� ��鵱ǰ����״̬��
			$('#deploy').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("����ѡ��Ҫ���õ����̡�");
					return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				deployProcess(fprocessId);
			});
			//���� ��鵱ǰ״̬��
			$('#undeploy').click(function() {
				var processRecords = $('#grid').omGrid('getSelections', true);
				var len = processRecords.length;
				if (len < 1) {
					alert("����ѡ��Ҫͣ�õ�����");
					return false;
				}
				var fprocessId = processRecords[0].fid;
				var fstatus = processRecords[0].fstatus;
				if (fstatus != 2) {
					//alert('������û������');
				}
				undeployProcess(fprocessId);
			});
		});

	//���̷�����ɹ���
	function undeployProcess(fprocessId) {
		var param = "processid=" + fprocessId;
		sendAjaxReq("post", "jbpm_unDeployProcess", param, function(request) {
			if (request.responseText == "ok") {
				alert("���̽��óɹ�");
			} else if (request.responseText == "fail") {
				alert("���̽���ʧ��");
			} else if (request.responseText == "exsit") {
				alert("��δ����������,����ʧ��");
			}
			$('#grid').omGrid('reload');
		}, null, null, null);
	}

	//���̲���ɹ���
	function deployProcess(fprocessId) {
		var param = "processid=" + fprocessId;
		sendAjaxReq("post", "jbpm_deployProcess", param, function(request) {
			if (request.responseText == "ok") {
				alert("���̲���ɹ�");
			} else {
				alert("���̲���ʧ��");
			}
			$('#grid').omGrid('reload');
		}, null, null, null);
	}
	//��������
	function startProcess(fprocessId) {
		$.ajax( {
			type : 'POST',
			url : 'startProcess',
			data : "fprocessId=" + fprocessId,
			dataType : 'text',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(request) {
				if (request == 'ok') {
					alert('�����ɹ�');
				} else {
					alert('����ʧ��');
				}
				$('#grid').omGrid('reload');
			}
		});
	}
	//ͣ������
	function stopProcess(fprocessId) {
		$.ajax( {
			type : 'POST',
			url : 'stopProcess',
			data : "fprocessId=" + fprocessId,
			dataType : 'text',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(request) {
				if (request == 'ok') {
					alert('ͣ�óɹ�');
				} else {
					alert('ͣ��ʧ��');
				}
				$('#grid').omGrid('reload');
			}
		});
	}
	//ɾ������
	function deleteProcess(fprocessId) {
		$.ajax( {
			type : 'POST',
			url : 'deleteProcess',
			data : "fprocessId=" + fprocessId,
			dataType : 'text',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(request) {
				if (request == 'ok') {
					alert('ɾ���ɹ�');
				} else {
					alert('ɾ��ʧ��');
				}
				$('#grid').omGrid('reload');
			}
		});
	}
	//�����̽���
	function openProcessDesign(process_id) {
		var name_text = '���������';
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
			return "����";
		} else if (colValue == 2) {
			return "����";
		} else {
			return "����";
		}
	};
	function readTime(colValue, rowData, rowIndex) {
		if (colValue != null) {
			return getFormatDate(new Date(colValue.time), null);
		}
	}
</script>
	<div id="demo">
		<input type="button" id="add" value="����" />
		<input type="button" id="edit" value="�޸�" />
		<input type="button" id="del" value="ɾ��" />
		<input type="button" id="start" value="����" />
		<input type="button" id="stop" value="ͣ��" />
		<input type="button" id="deploy" value="���� " />
		<input type="button" id="undeploy" value="������ " />
		<div>
			<table id="grid"></table>
		</div>
	</div>
</html>
