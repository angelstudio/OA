<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
        <script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
		<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
		<script type="text/javascript" src="../../js/tool/common.js"></script>
		<script type="text/javascript" src="../../js/tool/json2.js"></script>
		<script type="text/javascript" src="../../js/tool/urlUtil.js"></script>
		<script type="text/javascript" src="../js/viewFlowImage.js"></script>
		<script type="text/javascript" src="../js/BPJbpmCommon.js"></script>
		<script type="text/javascript" src="../../js/tool/common.js"></script>
		<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
        <link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
        <link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
		<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	padding: 0;
	margin: 0;
}

#center-tab .om-panel-body {
	padding: 0;
}
</style>
	</head>
	<body>
		<script type="text/javascript">
	$(document).ready(function(){
	        var ftaskMsg=request("ftaskMsg");
	        var fmsg=request("fmsg");
	        var fheight=parent.tabElement.height()-70;
			$('body').omBorderLayout( {
							panels : [ {
								id : "center-panel",
								header : false,
								region : "center"
							}, {
								id : "west-panel",
								resizable : true,
								collapsible : false,
								region : "west",
								title : "消息分类",
								width : 130,
								height : 330
							} ]
						});
						$('#deal').click(function() {
									var sr = $('#grid').omGrid('getSelections', true);
									var biztype = sr[0].biztype;
									var bizid = sr[0].bizid;
									var currentTaskId = sr[0].id;
									var taskid=sr[0].ftaskId;
									var fexecutionId=sr[0].fexecutionId;
									var fmessageType=sr[0].fmessageType;
									//处理之前进行判断
									if(fmessageType==1){
									if (beforeDeal(currentTaskId)) {
										skip(bizid,biztype,currentTaskId,taskid,fexecutionId);
									} else {
										alert("该流程已经处理，请刷新");
									}
									}else{
									////由于jbpm数据缓存问题,fmessageType为0时dbid为null;
									 var jsonObject=createDbid(taskid,fexecutionId);
									 var reval=jsonObject.returnVal;
									 var fdbid=jsonObject.fdbid;
									 if(reval=='error'){
									   alert("该流程已经处理，请刷新");
									 }else{
									   currentTaskId=fdbid;
									   skip(bizid, biztype,1,flag,currentTaskId, taskid,fexecutionId);
									 }
									}
								});
								
						var dataTree = [{id:"fwcl",text:"未处理",expanded:true},{id:"fycl",text:"已处理",expanded:true}];
						
						$("#mytree").omTree(
								{
									dataSource : dataTree,
									simpleDataModel : true,
									//选择树节点回调方法
									onSelect : function(node) {
										
									},

									onSuccess : function(data, testStatus,
											XMLHttpRequest, event) {
									},
									onError : function(XMLHttpRequest,
											textStatus, errorThrown, event) {
										alert(errorThrown);
									}
								});
						$('#grid').omGrid( {
							dataSource : 'getJBPMMessage',
							limit : 15,
							height : fheight,
							colModel : [ {
								header : '主题',
								name : 'title',
								width : 300,
								align : 'left'
							}, {
								header : '发起时间',
								name : 'receiveTime',
								align : 'left',
								width : 180
							}, {
								header : '流程发起者',
								name : 'sender',
								align : 'left',
								width : 180
							} ]
						});

						//点击“查看流程图”后新建
						$("#view")
								.click(
										function() {
											var selectedRecords = $('#grid')
													.omGrid('getSelections',
															true);
											if (selectedRecords.length <= 0) {
												alert('请选择查看的记录！');
												return;
											} else {
												checkOutProcess(selectedRecords[0]['id'],selectedRecords[0]['fexecutionId']);
											}
									});
					});
	function skip(id, billType,currentTaskId,taskid,fexecutionId){
	              //根据billType 打开相应界面;
	              $.ajax({
  						type: 'POST',
  						url:'jbpm_Form_URL',
 						data:"fname="+billType,
  						dataType:'json',
  						contentType:'application/x-www-form-urlencoded; charset=utf-8',
  						success: function(json){
  						 	var fbm=json.fbm;
  						 	var furl=json.furl;
  						 	furl=furl+"?id="+id+"&currentTaskId="+currentTaskId+"&taskid="+taskid+"&fexecutionId="+fexecutionId+"&billType="+billType;
  						 	openNewTabByName(fbm,furl,billType);
  						 	}
						});
	}
	//未处理任务
	function taskingInfo(type) {
	    var butStr='<button class="butDef" id="deal" >处理</button>&nbsp;'+
					'<button class="butDef" id="view" >查看流程图</button>'+
					'<table id="grid" Style="float: center;" border="1"></table>';
		 document.getElementById("tab1").innerHTML="";
	     document.getElementById("tab2").innerHTML="";
	    document.getElementById("msg").innerHTML="";
	     document.getElementById("sentMsg").innerHTML="";
	      document.getElementById("tab1").innerHTML=butStr;
		var col = [ {
			header : '主题',
			name : 'title',
			width : 100,
			align : 'left'
		}, {
			header : '接收时间',
			name : 'receiveTime',
			align : 'left',
			width : 100
		}, {
			header : '发送人',
			name : 'sender',
			align : 'left',
			width : 100
		} ];
		$('#grid').omGrid( {
			dataSource : "jbpm_getMessage?type=" + type,
			limit : 15,
			autoFit : true,
			height : 480,
			colModel : col
		});
		$("#view").click(function() {
								var selectedRecords = $('#grid').omGrid('getSelections',true);
							    if (selectedRecords.length <= 0) {
										alert('请选择查看的记录！');
									return;
								} else {
									checkOutProcess(selectedRecords[0]['id'],selectedRecords[0]['fexecutionId']);
							}
						});
	}
	//已任务处理
	function taskedInfo(type) {
	     document.getElementById("tab1").innerHTML="";
	     document.getElementById("tab2").innerHTML="";
	     document.getElementById("msg").innerHTML="";
	     document.getElementById("sentMsg").innerHTML="";
	     var tab='<table id="grid" Style="float: center;" border="1"></table>';
	     document.getElementById("tab1").innerHTML=tab;
		var col = [
		    {
			header : "流程名称",
			name : "fname",
			align : 'center',
			width : 100
		   },
		   {
			header : "审批结果",
			name : "fauditresult",
			align : 'center',
			width : 100
		   },
		   {
			header : "审批意见",
			name : "fauditidea",
			align : 'center',
			width : 100
		}, {
			header : "审核时间",
			name : "faudittime",
			align : 'center',
			width : 100
		} ];
		$('#grid').omGrid( {
			dataSource : "jbpm_getMessage?type=" + type,
			limit : 15,
			autoFit : true,
			height : 480,
			colModel : col
		});
	}
	function beforeDeal(currentTaskId) {
		var returnVal = true;
		$.ajax( {
			type : 'POST',
			url : 'beforeDeal',
			data : 'taskid=' + currentTaskId,
			async : false,
			dataType : 'text',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(data) {
				if (data == "error") {
					returnVal = false;
				}
			},
			error : function(XMLResponse) {
				alert("系统出错");
				returnVal = false;
			}
		});
		return returnVal;
	}
	function messageInfo(type){
	  var butStr='<input type="button" id="send" value="发送消息" style="width:70px;height:25px;">&nbsp;'+
	  '<input type="button" id="del1" value="删除" style="width:50px;height:25px;">';
	  document.getElementById("tab1").innerHTML="";
	  document.getElementById("tab2").innerHTML=butStr;
	  document.getElementById("sentMsg").innerHTML = "";
	  document.getElementById("msg").innerHTML = "";
	  document.getElementById("msg").innerHTML = "<table id='message'></table>";
	  var col1 = [{header:'操作',name:'faction',width:60,align:'center',
				   renderer : function(colValue, rowData, rowIndex) {
                                   return '<button onClick="reply('+rowIndex+',event)">回复</button>';
                               }},
				   {header:'发件人',name:'fmsgsender',width:160,align:'center'},
				   {header:'发送时间',name: 'fmsgsendtime',width:150,align:'center'},
				   {header:'状态',name:'fmsgstatus',width:40,align:'center',
				   renderer : function(colValue, rowData, rowIndex) {
                                   if (colValue == '未读') {
                                       return '<span style="color:red;"><b>' + colValue + '</b></span>';
                                   }
                                   return colValue;
                               }
				   },
				    {header:'内容',name:'fmsgcontent',width:300,wrap :true,align:'center'}];
	   $('#message').omGrid({
			height : 440,
			dataSource : 'getSysMsg',
			limit:12,
            autoFit: true,
			colModel : col1,
			rowDetailsProvider:function(rowData,rowIndex){
                return rowData.fmsgcontent;
            },
            onRowDblClick:function(rowIndex,rowData,event){
        		 $('#message').omGrid("setData",'getSysMsg?id=receive&fid='+rowData.fid);
     		}
		});
		$('#send').click(function(){
			           $("#dialog-modal").omDialog({
		                      autoOpen: false,
		                      width:740,
		                      height:340,
		                      title:'发送消息',
		                      modal: true
    		             });
   			             $( "#dialog-modal").omDialog('open');
		                   var frameLoc=document.getElementById("ifameMessage");
		                       frameLoc.src='sendMsg.jsp';
    		              return false;
		                  });
		  $('#del1').click(function(){
			var dels = $('#message').omGrid('getSelections',true);
            if(dels.length <= 0){
            	alert('请选择删除的记录！');
            	return;
            }
            if( checkBox("确定要删除这条信息吗？") ){
            	$('#message').omGrid("setData",'getSysMsg?id=receive&selectid='+dels[0].fid);
            }
		});
	}
	
	function messagedInfo(type){
	     var butStr='<input type="button" id="send" value="发送消息" style="width:70px;height:25px;">&nbsp;'+
		'<input type="button" id="del2" value="删除" style="width:50px;height:25px;">' ;
	    document.getElementById("tab1").innerHTML="";
	     document.getElementById("tab2").innerHTML=butStr;
		var col2 = [{header:'收件人',name:'fmsgrecipient',width:100,align:'center'},
				   {header:'发送时间',name: 'fmsgsendtime',width:150,align:'center'},
				   {header:'内容',name:'fmsgcontent',width:300,wrap :true,align:'center'}];
		    document.getElementById("msg").innerHTML = "";
		    document.getElementById("sentMsg").innerHTML = "";
		    document.getElementById("sentMsg").innerHTML = "<table id='mes'></table>";
			$('#mes').omGrid({
				height : 440,
				dataSource : 'getSysMsg?id=sent',
				limit:12,
            	autoFit: true,
				colModel : col2
			});
	   $('#send').click(function(){
			           $("#dialog-modal").omDialog({
		                      autoOpen: false,
		                      width:740,
		                      height:340,
		                      title:'发送消息',
		                      modal: true
    		             });
   			             $( "#dialog-modal").omDialog('open');
		                   var frameLoc=document.getElementById("ifameMessage");
		                       frameLoc.src='sendMsg.jsp';
    		              return false;
		                  });
		  $('#del2').click(function(){
			var dels = $('#mes').omGrid('getSelections',true);
            if(dels.length <= 0){
            	alert('请选择删除的记录！');
            	return;
            }
            if( checkBox("确定要删除这条信息吗？") ){
            	$('#mes').omGrid("setData",'getSysMsg?id=sent&selectid='+dels[0].fid);
            }
		});
	}
	function reply(rowIndex,event){
		$('#message').omGrid('setSelections',[rowIndex]);
		var selectedRecords = $('#message').omGrid('getSelections', true);
	    var fmsgsender = selectedRecords[0].fmsgsender;
	    $("#dialog-modal").omDialog({
		        autoOpen: false,
		        width:740,
		        height:340,
		        title:'发送消息',
		        modal: true
    		 });
   		$( "#dialog-modal").omDialog('open');
		var frameLoc=document.getElementById("ifameMessage");
		frameLoc.src='sendMsg.jsp?fmsgsender='+fmsgsender;
    	return false;
	}
	function  createDbid(taskid,fexecutionId){
	      var jsonObject;
	      $.ajax( {
			type : 'POST',
			url : 'createDbid',
			data : 'taskid=' + taskid+"&fexecutionId="+fexecutionId,
			async : false,
			dataType : 'json',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(data) {
				jsonObject=data;
			},
			error : function(XMLResponse) {
				alert("系统出错");
			}
		});
		return jsonObject;
	}
</script>
		<div id="center-panel">
			<div id="center-tab">
				<div id="tab1" style="margin-right: 5px">
					<button  id="deal" >处理</button>&nbsp;
					<button  id="view" >查看流程图</button>
					<table id="grid" Style="float: center;" border="1"></table>
				</div>
				<div id="tab2"></div>
				<div id="msg"><table id="message"></table></div>
				<div id="sentMsg"><table id='mes'></table></div>
				<div id="dialog-modal">
					<iframe id="ifameMessage" frameborder="0"
						style="width: 100%; height: 99%; height: 100% \9;"
						src="about:blank"></iframe>
				</div>
			</div>
		</div>
		<div id="west-panel">
			<ul id="mytree"></ul>
		</div>
	</body>
</html>