<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>发文管理</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
<script type="text/javascript">
	var fheight=parent.tabElement.height()-150;
	function loadGrid(){
			//------------------------------grid基本信息开始------------------------------------
	        var coM=[  
                             {header : '公文标题', name : 'ftitle', width : 120, align : 'center'}, 
                             {header : '当前状态', name : 'fstatu', align : 'center', width : '120',renderer :dqStatu},
                             {header : '当前处理人', name : 'fassist', width : 120, align : 'center'}, 
                             {header : '发文号', name : 'fdispatch_no', width : 120, align : 'center'}, 
                             {header : '发文单位', name : 'fdraftunit', width : 120, align : 'center'}, 
                   			 {header : '行文类型', name : 'ftype', width : 120, align : 'center',renderer :xwStatu},
                   			 {header : '发文状态', name : 'fstatu', width : 120, align : 'center',renderer :ztStatu},
                             {header : '创建日期', name : 'fcreate_date', width : '120', align : 'center'},
                             {header : '操作', name : 'fid', width : '120', align : 'center',renderer :xqStatu}
                    ];
		    $('#grid').omGrid({
                limit:5,
                height : fheight,
                colModel : coM,
                singleSelect:false,
                autoFit:true,
				dataSource :"findAllDispatchesToGrid",
				onRowDblClick:function(rowIndex,rowData,event){},
				onSuccess:function(data,testStatus,XMLHttpRequest,event){}
		    });
		    //--------------------------------grid基本信息结束-------------------------------------
	}
	$(document).ready(function() {
			loadGrid();
			//--------------------------------模态框初始化------------------------------------------
			$("#dialog-modal").omDialog({
					            autoOpen: false,
								height: 600,
								width:1000,
								modal: true
				       		 });
		    //检索条件面板
		    $('#fcommonquery').omPanel({
                        title:"检索条件",
                        width: "100%",
			            collapsed : false,//组件创建后为收起状态
			            collapsible : true,//渲染收起与展开按钮
			            closable : false //渲染关闭按钮
		    });
		    
		    //-------------------------------增删改查按钮--------------------------------------------
            $('#buttonbar').omButtonbar({
            	btns : [{label:"新增",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
						 	 $("#dialog-modal").omDialog({title : '新增发文'});
	            	 		 $('#frame').attr('src',"adddispatchform.jsp");
						 	 $("#dialog-modal").omDialog('open');
            	 		 }
            			},
            			{separtor:true},
            	        {label:"修改",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
			            	 var selectedRecords = $('#grid').omGrid('getSelections',true);
			            	if(selectedRecords == "" ){
			            		alert('请选择需编辑的记录！');
			            		return;
			            	}
			            	if(selectedRecords[0].fstatu == 1){//为正文状态不允许编辑
			            		alert('此文已发送不允许编辑!');
			            		return;
			            	}
			            	$("#dialog-modal").omDialog({title : '编辑发文'});
			            	$('#frame').attr('src',"upddispatchform.jsp?fid="+selectedRecords[0].fid);
			            	$("#dialog-modal").omDialog('open');
            	 		 }
            	        },
            	        {separtor:true},
            	        {label:"删除",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
			            	var selectedRecords = $('#grid').omGrid('getSelections',true);
			            	if(selectedRecords == "" ){
			            		alert('请选择删除的记录！');
			            		return;
			            	}
			            	var fidItems = "";
			            	for(var i=0;i<selectedRecords.length;i++){
			            		fidItems += selectedRecords[i].fid+",";
			            	}
			            	$.omMessageBox.confirm({
								title:'删除确认！',
								content:'删除后将不可恢复，确定要删除吗？',
								onClose:function(v){
								if(v){
									$.post('delOneDispatch',{operation:'delete',fidItems:fidItems},function(){
									$('#grid').omGrid('reload');//刷新当前页数据
									$.omMessageTip.show({title: "操作成功", content: "删除数据成功", timeout: 1500});
								});
								}else{
								}
								}
							});
            	 		 }
            	        }
            	]
            });
        });
           //---------------------------------增删改按钮结束------------------------------------------------
           //---------------------------------常用方法----------------------------------------------------
      function reloadGrid(){
	  	 	$('#grid').omGrid('reload');
	   }
	  function closefawenModeDialog(){
	 	 	$('#dialog-modal').omDialog('close');
	   }
	   function dqStatu(value){
		   	if(value=='0'){
		   		return '<span style="background-color:#5DC0E2;color:#fff"><b>归档</b></span>';
		   	} if(value=='1'){
		   		return '<span style="background-color:#5DC0E2;color:#fff"><b>会签</b></span>';
		   	}else{
		   		return '<span style="background-color:#5DC0E2;color:#fff"><b>签发</b></span>';
		   	}
	   }
	   function xwStatu(value){
	   		if(value=='0'){
		   		return '一般';
		   	} if(value=='1'){
		   		return '上行';
		   	}else{
		   		return '下行';
		   	}
	   }
	   function xqStatu(value){
	   		return "<a style='color:green' href='javascript:showDetails(\""+value+"\")'>详情</a>";
	   }
	   function ztStatu(value){
	   		if(value=='0'){
		   		return '草稿';
		   	}else{
		   		return '正文';
		   	}
	   }
	   function showDetails(fid){
	   		$("#dialog-modal").omDialog({title : '发文详情'});
			$('#frame').attr('src',"dispatchformdetail.jsp?fid="+fid);
			$("#dialog-modal").omDialog('open');
	   }
	     //根据检索条件和关键字查询
	  function queryDispatchByArgs(){
	   		var ftitle = $('#ftitle').val();
	   		var fdispatch_no = $('#fdispatch_no').val();
	   		var funit = $('#funit option:selected').val();
	   		var ftype = $('#ftype option:selected').val();
	   		$('#grid').omGrid('setData','queryDispatchesToGrid?ftitle='+ftitle+'&fdispatch_no='+fdispatch_no+'&funit='+funit+'&ftype='+ftype);
            //清空查询框
		    document.getElementById("queryForm").reset();
	   }
</script>
</head>
<body>
	<form id="queryForm">
       <div id="fcommonquery">
			  <tr>
					<td>标题:</td>
              		<td><input type="text" name="ftitle" id="ftitle"/></td>
              		<td>行文号:</td>
              		<td><input type="text" name="fdispatch_no" id="fdispatch_no"/></td>
              		<td>发文单位:</td>
              		<td>
              		 <select name="funit" id="funit">
	    				<option value="">---请选择---</option> 
						<option value="湘交结">湘交结</option> 
						<option value="湘交研">湘交研</option> 
						<option value="湘智司">湘智司</option> 
						<option value="湘交结算党">湘交结算党</option> 
						<option value="湘交结算工">湘交结算工</option> 
						<option value="湘交结算纪">湘交结算纪 </option>  
					 </select>
              	  </td>
              	 <td>行文类型:</td>
              		<td>
              		  <select name="ftype" id="ftype">
	    				<option value="">---请选择---</option> 
						<option value="0">一般</option> 
						<option value="1">上行</option> 
						<option value="2">下行</option> 
					 </select>
              	   </td>
				<td><button type="button" onclick="queryDispatchByArgs()">搜索</button></td>
			</tr>
       </div>
       </form>
       <div id="buttonbar"></div>
       <div>
       <table id="grid"></table>
       </div>
       <div id="dialog-modal">
        	<iframe id="frame" style="border: 0px; width:100%;height:100%"></iframe>
   	   </div>
   </body>
</html>