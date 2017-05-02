<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>内部邮件</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/ajaxfileupload.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<!-- <link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />  -->
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />

<style>

#ipForm table{
	border-collapse: separate;
	border-spacing: 15px;
}
#ipForm table input{
	height: 25px;
	width: 100%;
}
</style>
<script type="text/javascript">
	function loadGrid(){
	        var ftitle =$("#fbt").val();
	        var fsender =$("#ffsr").val();
	        
			//------------------------------grid基本信息开始------------------------------------
	        var fheight=parent.tabElement.height()-150;
	        var coM=[  
                   	         {header : '标题', name : 'ftitle', width : 100, align : 'left'},
                             {header : '接收人', name : 'fusername', width : 100, align : 'left'}, 
                             {header : '发送人', name : 'fsender', width : 50, align : 'left'}, 
                             {header : '附件', name : 'faccessory', width : 80, align : 'left'}, 
                             {header : '部门', name : 'fsenddepartment', width : 50, align : 'left'}, 
                             {header : '发送时间', name : 'fcreatedate', width : 80, align : 'left'}
                    ];
		    $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"EmailList?ftitle="+ftitle+"&fsender="+fsender,
				onRowDblClick:function(rowIndex,rowData,event){},
				onSuccess:function(data,testStatus,XMLHttpRequest,event){}
		    });
		    //--------------------------------grid基本信息结束-------------------------------------
	}
	  
	$(document).ready(function() {
	
			loadGrid();
			
		    //检索条件面板
		    $('#fcommonquery').omPanel({
                        title:"检索条件",
			            collapsed : false,//组件创建后为收起状态
			            collapsible : true,//渲染收起与展开按钮
			            closable : false //渲染关闭按钮
		    });
		    
		    $("#dialog-panel").omDialog({
	            autoOpen: false,
	            height: 380,
			    width: 850,
				modal: true
       		 });
		    
		    $('#fassist').focus(function(){
			  $("#dialog-panel").omDialog('open');
  		    });
  		    //选择发送人
		    initSendPersonGrid();
		     //-------------------------------增删改查按钮--------------------------------------------
            $('#buttonbar').omButtonbar({
            	btns : [{label:"新增",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
            	 		    isAdd = true;
            	 		    $("#showtable input").attr("disabled",false);
                			$("#showtable textarea").attr("disabled",false);
            	 		    showDialog('发送邮件');
            	 		    
            	 		 }
            			} ,
            			{separtor:true},
            	        {label:"查看",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
            	 		 	 var selections=$('#grid').omGrid('getSelections',true);
                			if (selections.length == 0) {
                    			alert('请至少选择一行记录');
                    			return false;
                			}
                			isAdd = false;
                			
                			$("#showtable input").attr("disabled",true);
                			$("#showtable textarea").attr("disabled",true);
                			
                			showDialog('查看',selections[0]);//显示dialog 
            	 		 }
            	        }  ,
            	        {separtor:true},
            	        {label:"删除",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
            	 		 	 var selections=$('#grid').omGrid('getSelections',true);
			                if (selections.length == 0) {
			                    alert('请至少选择一行记录');
			                    return false;
			                }
                			//将选择的记录的id传递到后台去并执行delete操作
		                	var id = selections[0].fid;
					        $.omMessageBox.confirm({
								title:'删除确认！',
								content:'删除后将不可恢复，确定要删除吗？',
								onClose:function(v){
								if(v){
								//将选择的记录的id传递到后台去并执行delete操作
									$.post('delEmail',{operation:'delete',fid:id},function(){
									loadGrid();//刷新当前页数据
									$.omMessageTip.show({title: "操作成功", content: "删除数据成功", timeout: 1500});
								});
								}
						      }
							}); 
            	 		 }
            	     }
            	]
            });
           //---------------------------------增删改按钮结束------------------------------------------------
           var dialog = $("#dialog-form").omDialog({
                width: 600,
                height : 450,
                autoOpen : false,
                modal : true,
                resizable : false,
                buttons : {
                    "提交" : function(){
                    
                      if(isAdd){
		                submitDialog();
		                }else{
		                $("#dialog-form").omDialog("close");//关闭dialog
		                }
		                return false; //阻止form的默认提交动作
		            },
                    "取消" : function() {
                        $("#dialog-form").omDialog("close");//关闭dialog
                    }
                }
            });
            
            //显示dialog并初始化里面的输入框的数据
            var showDialog = function(title,rowData){
                rowData = rowData || {};
                $("#ftitle",dialog).val(rowData.ftitle);
                $("#fcontent",dialog).val(rowData.fcontent);
                $("#fassist",dialog).val(rowData.fusername);
                /* $("#file",dialog).val(rowData.faccessory); */
                 
                dialog.omDialog("option", "title", title);
                dialog.omDialog("open");//显示dialog
            };
            //dialog中点提交按钮时将数据提交到后台并执行相应的add或modify操作
            var submitDialog = function(){
            
	            var operation = isAdd?'add':'show';
	            var ftitle =$("#ftitle").val();
	            var fusername =$("#fassist").val();
	            var fuserid =$("#fuserid").val();
	            var fcontent =$("#fcontent").val();
	            var femailnum =$("#femailnum").val();
	            
	            $.ajaxFileUpload( {  
		    	type:'POST',
		        url : 'sendEmail',			//用于文件上传的服务器端请求地址  
		        secureuri : false,          //一般设置为false  
		        fileElementId : 'file',     //文件上传空间的id属性  <input type="file" id="file" name="file" />  
		        data:{ftitle:ftitle,fusername:fusername,fuserid:fuserid,fcontent:fcontent,femailnum:femailnum},
		        dataType : 'text',          //返回值类型 一般设置为json  
		        success : function(data, status) {  
		        	data = data.substring(data.indexOf(">")+1,data.lastIndexOf("<"));
		        	if(data == 'success'){
			        	//清空表单内容
			           	document.getElementById("ipForm").reset();
			           	$('#grid').omGrid('reload',1);//如果是添加则滚动到第一页并刷新
	                    $.omMessageTip.show({title: "操作成功", content: "添加数据成功", timeout: 1500});
	                    $("#dialog-form").omDialog("close"); //关闭dialog
		        	}else if(data == 'error'){
		        		alert("文件格式错误!");
		        	}else if(data == 'fail'){
		        		alert("仔细检查用户中有没有填写邮箱");
		        	}else{
		        		alert("系统内部错误");
		        	}
		        }  
		    }) ; 
        };
	});
	
	    //初始化人员选择框方法
        function initSendPersonGrid(){
  	          var fheight=300;
	          $('#panel').css("height",fheight);
	          $('#center_panel').css("height",fheight);
	          $('#west_panel').css("height",fheight);
	          loadDialog('60%',fheight); 
	          var coM=[  
                        {header : '所属部门', name : 'fssbm',width : 100, align : 'center'},
	                    {header : '人员姓名', name : 'fname',width : 100, align : 'center'},
                        {header : '本人电话', name : 'fbrdh',width : 120, align : 'left'},
                        {header : '邮箱', name : 'femail',width : 150, align : 'left'}
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
	           	        width:200
	           	    }
	           	    ]
	            });
	         $("#treeInfo").omTree({
                dataSource:"queryAllOrg",
                simpleDataModel: true,
                showCheckbox:false,
                //选择树节点回调方法
                onSelect: function(node){
                	
                },
                onClick: function(nodeData, event){
                    var  fbmid=nodeData.id;
                    $('#ftab').omGrid('setData', 'queryPerson?fbmid='+fbmid);
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
				dataSource :'queryPerson',
				onRowDblClick:function(rowIndex,rowData,event){
                 },
				onSuccess:function(data,testStatus,XMLHttpRequest,event){
   				       
   				       }
		        });
  	 	}

		//获取选中行的方法
		function fillThisArgs(){
  	 		var selectedRecords = $('#ftab').omGrid('getSelections',true);
  	 		var personIdItems = '';
  	 		var userids='';
  	 		var emails='';
			if(selectedRecords == ""){
				alert('请选择接收人员！');
				return;
			}
			for(var i = 0; i < selectedRecords.length; i++){
					personIdItems+=selectedRecords[i].fname+',';
					userids+=selectedRecords[i].fid+',';
					emails+=selectedRecords[i].femail+',';
				}
			personIdItems = personIdItems.substring(0, personIdItems.length-1);
			userids = userids.substring(0, userids.length-1);
			emails = emails.substring(0, emails.length-1);
			//把选中的人添加到input输入框中去
			$('#fassist').val(personIdItems);
			$('#fuserid').val(userids);
			$('#femailnum').val(emails);
  	 		$("#dialog-panel").omDialog('close');
  	 		$('#ftab').omGrid('reload',1);
  	 	}
</script>
</head>
<body>
       <div id="fcommonquery">
                 标题:<span ><input id="fbt" style="height:18px;"/></span>
                 发送人:<span ><input id="ffsr" style="height:18px;"/></span>
          	<button onclick="loadGrid()">查询</button>
       </div>
       <div id="buttonbar"></div>
       <div>
       <table id="grid"></table>
       </div>
       
       <div id="dialog-form" align="center">
        <form id="ipForm">
            <input name="fid" style="display: none"/> 
        	<table id="showtable"  width="100%">
        		 <tr>
					<td width="10%">标题:</td>
					<td width="90%"><input type="text" name="ftitle" id="ftitle" /></td>
				</tr>
				<tr>
					<td>内容:</td>
					<td ><textarea rows="10" cols="" id="fcontent" name="fcontent" style="width: 100%"></textarea><td>
				</tr>
				<tr>
					<td>接收人:</td>
					<td >
					<input type="text" name="fusername" id="fassist" readOnly="true" />
					<input type="text" name="fuserid" id="fuserid"  style="display: none" />
					<input type="text" name="femailnum" id="femailnum" style="display: none"
					  />
					</td>
				</tr>
				<tr>
					<td>文件:</td>
					<td ><input type="file" name="file" id="file" /></span></td>
				</tr>
        	</table>
        </form>
    </div>
       
     <div id="dialog-panel">
		        <div id="panel" style="width:100%" >
			     <div id="west_panel" >
				    <ul id="treeInfo"></ul>
			     </div>
				<div id="center_panel">	
					<div><table id="ftab"></table></div>
				</div>
		      </div>
		     <button onclick="fillThisArgs()">确定</button>
   </div>   
</html>