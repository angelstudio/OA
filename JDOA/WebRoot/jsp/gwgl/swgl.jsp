<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>收文管理</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
<script type="text/javascript" src="../../js/tool/time.js"></script>	
	<script type="text/javascript">
	var fheight=parent.tabElement.height()-150;
	function loadGrid(){
			//------------------------------grid基本信息开始------------------------------------
	        var coM=[  
                             {header : '公文标题', name : 'ftitle', width : 120, align : 'center'}, 
                             {header : '来文单位', name : 'fincome_unit', width : 120, align : 'center'}, 
                             {header : '紧急程度', name : 'fpriority', align : 'center', width : '120',renderer :jjStatu},
                             {header : '流程状态', name : 'fassist', width : 120, align : 'center',renderer :lcStatu}, 
                             {header : '当前审核人', name : '', width : 120, align : 'center'}, 
                   			 {header : '收文日期', name : 'facceptdate', width : 120, align : 'center'},
                             {header : '创建日期', name : 'fcreate_date', width : '120', align : 'center'},
                             {header : '操作', name : 'fid', width : '120', align : 'center',renderer :xqStatu}
                    ];
		    $('#grid').omGrid({
                limit:5,
                height : fheight,
                colModel : coM,
                singleSelect:false,
                autoFit:true,
				dataSource :"findAllIncomeDispatchesToGrid",
				onRowDblClick:function(rowIndex,rowData,event){},
				onSuccess:function(data,testStatus,XMLHttpRequest,event){}
		    });
		    //--------------------------------grid基本信息结束-------------------------------------
	}
	$(document).ready(function() {
			loadGrid();
			//--------------------------------模态框初始化------------------------------------------
			$('#dialog-modal').omDialog({
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
            	btns : [{
            				label:"新增",
            				icons : {left : '../../image/common/add.png'},
            				onClick:function(){
				            	$("#dialog-modal").omDialog({title:'新增收文'});
				            	$('#frame').attr('src',"addincomedispatchform.jsp");
				            	$("#dialog-modal").omDialog('open');
            				}
            			},
            	        {separtor:true},
            	        {label:"修改",
            			 id:"button-edit",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
            	 			 var selectedRecords = $('#grid').omGrid('getSelections',true);
				             if(selectedRecords == "" ){
				            		alert('请选择要修改的收文！');
				            		return;
				            	}
				             var fid = selectedRecords[0].fid;
							 $("#dialog-modal").omDialog({title : '编辑收文'});
	            	 		 $('#frame').attr('src',"updincomedispatchform.jsp?fid="+fid);
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
									$.post('delOneIncomeDispatch',{operation:'delete',fidItems:fidItems},function(){
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
            //-----------------------------------------删除按钮结束----------------------------------------
			});
			//-----------------------------------------常用方法-------------------------------------------
			 //根据检索条件和关键字查询
	  function queryDispatchByArgs(){
	   		var ftitle = $("input[name='ftitle']").val();
	   		var fincome_unit = $("input[name='fincome_unit']").val();
	   		var date1 = $("input[name='date1']").val();
	   		var date2 = $("input[name='date2']").val();
	   		$('#grid').omGrid('setData','queryIncomeDispatchesToGrid?ftitle='+ftitle+'&fincome_unit='+fincome_unit+'&date1='+date1+'&date2='+date2);
            //清空查询框
	   		document.getElementById("queryForm").reset();
	   }
	   
	   function closeModel(){
	   		$("#dialog-modal").omDialog('close');
	   		$("#dialog-modal-edite").omDialog('close');
	   }
	   
	   function reloadgrid(){
	  	 $('#grid').omGrid('reload');
	   }
	   function xqStatu(value){
	   		return "<a style='color:green' href='javascript:showDetails(\""+value+"\")'>详情</a>";
	   }
	   function jjStatu(value){
	   	if(value=='0'){
	   		return '一般';
	   	}else if(value=='1'){
	   		return '紧急';
	   	}else{
	   		return '特急';
	   	}
	   }
	   function lcStatu(value){
	   		if(value=='0'){
		   		return '<span style="background-color:#5DC0E2;color:#fff"><b>拟办</b></span>';
		   	} if(value=='1'){
		   		return '<span style="background-color:#5DC0E2;color:#fff"><b>领导传阅</b></span>';
		   	}else{
		   		return '<span style="background-color:#5DC0E2;color:#fff"><b>领导审阅</b></span>';
		   	}
	   }
	    function showDetails(fid){
	   		$("#dialog-modal").omDialog({title : '收文详情'});
			$('#frame').attr('src',"incomedispatchformdetail.jsp?fid="+fid);
			$("#dialog-modal").omDialog('open');
	   }
	</script>
  </head>
  
  <body>
  	<form id="queryForm">
 	 <div id="fcommonquery">
           <tr>
           		<td>标题:</td>
           		<td><input type="text" name="ftitle" value=""/></td>
           		<td>来文单位:</td>
           		<td><input type="text" name="fincome_unit" value=""/></td>
           		<td>收文日期:</td>
           		<td><input id="frzrq" name="date1" onkeypress="return false;" onfocus="SelectDate(this,'yyyy-MM-dd')" style="background:url(../../image/basic/date.png) no-repeat 160px  -4px;"/></td>
          		~
           		<td><input id="frzrq" name="date2" onkeypress="return false;" onfocus="SelectDate(this,'yyyy-MM-dd')" style="background:url(../../image/basic/date.png) no-repeat 160px  -4px;"/></td>
           </tr>
        	<button type="button" onclick="queryDispatchByArgs()" >查询</button>
       </div>
       </form>
       <div id="buttonbar"></div>
       <div><table id="grid"></table></div>
       <div id="dialog-modal">
        	<iframe id="frame" style="border: 0px; width:100%;height:100%"></iframe>
   	   </div>
  </body>
</html>
