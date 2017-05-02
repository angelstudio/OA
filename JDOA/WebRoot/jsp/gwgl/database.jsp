<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>资料库</title>
	<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
	<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
	<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
	<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
	<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
<script type="text/javascript">
	var fheight=parent.tabElement.height()-160;
	$(document).ready(function() {
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
            	btns : [{label:"新建文件夹",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
            	 		 	var node = $("#treeInfo").omTree("getSelected");
            	 		 		if(node==null){
            	 		 			alert("请选择文件夹!");
            	 		 			return;
            	 		 		}
							$.omMessageBox.prompt({
					           title:'新建文件夹',
					           content:'请输入文件夹名:',
					           onClose:function(v){
					              if(v===false){
					                   return;
					              	 }
					              if(v==''){
					                   alert('文件夹名不能为空');
					                   return false;
					                }else{
		   								//添加
		   								$.ajax({
		   									type:'POST',
		   									url:"addFileFolder",
		   									data:{fid:node.id,fname:v,flevel:node.flevel},
		   									dataType:'text',
										    contentType:'application/x-www-form-urlencoded; charset=utf-8',
										    success:function(data){
										    	if(data=='ok'){
										    		 $("#treeInfo").omTree({dataSource:"queryAllFolder",simpleDataModel: true});
										    	}
										    	if(data=='error'){
										    		alert("文件夹命名冲突!");
										    	}
										    }
		   								});
					                }
					           }
					       });
								
            	 		 }
            			},
            			{separtor:true},
            	        {label:"编辑文件夹",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
							var node = $("#treeInfo").omTree("getSelected");
            	 		 		if(node==null){
            	 		 			alert("请选择文件夹!");
            	 		 			return;
            	 		 		}
							$.omMessageBox.prompt({
					           title:'编辑文件夹',
					           content:'请输入文件夹名:',
					           onClose:function(v){
					              if(v===false){
					                   return;
					              	 }
					              if(v==''){
					                   alert('文件夹名不能为空');
					                   return false;
					                }else{
		   								//编辑
		   								$.ajax({
		   									type:'POST',
		   									url:"updFileFolder",
		   									data:{fid:node.id,fname:v},
		   									dataType:'text',
										    contentType:'application/x-www-form-urlencoded; charset=utf-8',
										    success:function(data){
										    	if(data=='ok'){
										    		 $("#treeInfo").omTree({dataSource:"queryAllFolder",simpleDataModel: true});
										    	}
										    	if(data=='error'){
										    		alert("文件夹命名冲突!");
										    	}
										    }
		   								});
					                }
					           }
					       });
            	 		 }
            	        },
            	        {separtor:true},
            	        {label:"删除文件夹",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
			            	var node = $("#treeInfo").omTree("getSelected");
            	 		 		if(node==null){
            	 		 			alert("请选择文件夹!");
            	 		 			return;
            	 		 		}
			             $.omMessageBox.confirm({
			                title:'确认删除',
			                content:'您确定要删除【'+node.text+'】文件夹以及里面的文件吗?',
			                onClose:function(v){
			                    if(v===false){
			                    	return;
			                    }else{
			                    	//删除
			                    	$.ajax({
		   									type:'POST',
		   									url:"delFileFolder",
		   									data:{fid:node.id},
		   									dataType:'text',
										    contentType:'application/x-www-form-urlencoded; charset=utf-8',
										    success:function(data){
										    	if(data=='ok'){
										    		 $("#treeInfo").omTree({dataSource:"queryAllFolder",simpleDataModel: true});
										    	}else if(data=='error'){
										    		alert("磁盘文件删除失败!");
										    	}
										    }
		   								});
			                    }
			                }
			            });
            	 		 }
            	        },
            	        {separtor:true},
            	        {label:"添加",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
							var node = $("#treeInfo").omTree("getSelected");
            	 		 		if(node==null){
            	 		 			alert("请选择文件夹!");
            	 		 			return;
            	 		 		}
            	 		 	 $('#frame').attr('src',"uploadfile.jsp?fid="+node.id);
            	 		 	 $("#dialog-modal").omDialog({title:'文件上传'});
						 	 $("#dialog-modal").omDialog('open');
            	 		 }
            			},
            			{separtor:true},
            	        {label:"编辑",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
							var selectedRecords = $('#ftab').omGrid('getSelections',true);
			            	if(selectedRecords == "" ){
			            		alert('请选择编辑的记录！');
			            		return;
			            	}
			            	 $('#frame').attr('src',"updfile.jsp?fid="+selectedRecords[0].fid);
			            	 $("#dialog-modal").omDialog({title:'文件编辑'});
						 	 $("#dialog-modal").omDialog('open');
            	 		 }
            	        },
            	        {separtor:true},
            	        {label:"删除",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
			            	var selectedRecords = $('#ftab').omGrid('getSelections',true);
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
									$.post('delOneFile',{operation:'delete',fidItems:fidItems},function(){
									$('#ftab').omGrid('reload');//刷新当前页数据
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
            initDeptGrid();
        });
        //---------------------------------------初始化部门表----------------------------------
        function initDeptGrid(){
	          $('#panel').css("height",fheight);
	          $('#center_panel').css("height",fheight);
	          $('#west_panel').css("height",fheight);
	          loadDialog('60%',fheight); 
	          var coM=[  
                        {header : '文件日期', name : 'fcreate_date',width : 150, align : 'center'},
	                	{header : '发文机关', name : 'funit',width : 150, align : 'center'},
                        {header : '文号', name : 'fdispatch_no',width : 150, align : 'center'},
                        {header : '文件标题', name : 'ftitle',width : 200, align : 'center'},
                        {header : '关键词', name : 'fkeyword',width : 200, align : 'center'},
                        {header : '操作', name : 'fid',width : 200, align : 'center',renderer :opera},
                       ];
             $('#panel').omBorderLayout({
	           	   panels:[
	           	    {	    
	           	        id:"center_panel",
	           	     	header:false,
	           	     	fit:true,
	           	        region:"center"
	           	    },{
	           	        id:"west_panel",
	           	        resizable:true,
	           	        collapsible:true,
	           	        fit:true,
	           	        region:"west",
	           	        width:250
	           	    }
	           	    ]
	            });
	         $("#treeInfo").omTree({
                dataSource:"queryAllFolder",
                simpleDataModel: true,
                showCheckbox:false,
                //选择树节点回调方法
                onSelect: function(node){
                	
                },
                onClick: function(nodeData, event){
                    var  fbmid=nodeData.id;
                    $('#ftab').omGrid('setData', 'findFileInfo?fid='+fbmid);
                 },
                onSuccess:function(data,testStatus,XMLHttpRequest,event){             
                }, 
                 onError:function(XMLHttpRequest,textStatus,errorThrown,event){
                        alert('系统错误');
                 }
              });
	   $('#ftab').omGrid({
                limit:100,
                height : fheight-3,
                colModel : coM,
                singleSelect:false,
                autoFit:true,
				dataSource :'findFileInfo',
				onRowDblClick:function(rowIndex,rowData,event){
                 },
				onSuccess:function(data,testStatus,XMLHttpRequest,event){
   				       
   				       }
		        });
  	 	}
  	 	function closeModel(){
	   		$("#dialog-modal").omDialog('close');
	   }
	   
	   function reloadgrid(){
	  	 $('#ftab').omGrid('reload');
	   }
	   
	   function queryFileByArgs(){
	   		var ftitle = $("input[name='ftitle']").val();
	   		var funit = $("input[name='funit']").val();
	   		var fdispatch_no = $("input[name='fdispatch_no']").val();
	   		var fkeyword = $("input[name='fkeyword']").val();
	   		$('#ftab').omGrid('setData','queryFileInfoToGrid?ftitle='+ftitle+'&funit='+funit+'&fdispatch_no='+fdispatch_no+'&fkeyword='+fkeyword);
            //清空查询框
	   		reset();
	   }
	   
	   function reset(){
	   	//清空表单内容
		document.getElementById("queryForm").reset();
	   }
	   
	   //操作
	   function opera(value){
	   		return "<a style='color:green' href='downloadFile?fid="+value+"'>下载</a>";
	   }
	   
</script>
</head>
<body>
	<form id="queryForm">
       <div id="fcommonquery">
			  <tr>
					<td>发文机关:</td>
              		<td><input type="text" name="funit"/></td>
              		<td>文号:</td>
              		<td><input type="text" name="fdispatch_no"/></td>
					<td>文件标题:</td>
              		<td><input type="text" name="ftitle"/></td>
              		<td>关键词:</td>
              		<td><input type="text" name="fkeyword"/></td>
				<td><button type="button" onclick="queryFileByArgs()">搜索</button></td>
				<td><button type="button" onclick="reset()">重置</button></td>
			</tr>
       </div>
       </form>
      	 <div id="buttonbar"></div>
       <div>
      	 <table id="grid"></table>
       </div>
       <div id="panel" style="width:100%">
		     <div id="west_panel" >
			    <ul id="treeInfo"></ul>
		     </div>
			<div id="center_panel">	
				<div><table id="ftab"></table></div>
			</div>
	</div>
	<div id="dialog-modal">
        	<iframe id="frame" style="border: 0px; width:100%;height:100%"></iframe>
   	</div>
</body>
</html>