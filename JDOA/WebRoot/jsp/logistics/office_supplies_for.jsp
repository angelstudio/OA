<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>办公用品申领</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<script type="text/javascript" src="../../js/tool/om-grid-roweditor.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
<script type="text/javascript">
	  
      function loadGrid(){
              var fheight=parent.tabElement.height()-150;
	          var coM=[ 	  
                     		  {header:'领用物品', name : 'fname', width : '145', align : 'center',wrap:true}, 
                     	  	  {header:'当前流程状态', name : 'fstatus', align : 'center', width : '145'},
                     		  {header:'当前处理人', name : 'fapprover',width: 145,align : 'center',wrap:true},
                     		  {header:'领用人', name : 'freceiver',width: 145,align : 'center'},
                     		  {header:'领用部门',name:'frecipientsdepartment',width: 145,align : 'center'},
                     		  {header:'领用日期',name:'frecipientsdate',width : 145,align : 'center'},
                              {header : '操作', name : 'operation', width: 150, align:'center', renderer:function(colValue, rowData, rowIndex){
                            	 var data = rowData;
                            	 return '<button onClick="showRowData('+rowIndex+',event)" >查看</button>';
                             }}
                             ];
                       
            $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"queryAllApply",
				onRowDblClick:function(rowIndex,rowData,event){},
				onSuccess:function(data,testStatus,XMLHttpRequest,event){}
		        });
     }

        $(document).ready(function() {
        	 $.ajax({
  							type: 'POST',
  							url:'getOrg', 
  							dataType:'json', 							
  							contentType:'application/x-www-form-urlencoded; charset=utf-8',
  							success: function(data){
  							   for( var i in data){
  							   	 
  							     $("#fssbm").append('<option value="'+i+'">'+data[i]+'</option>');
  							   }
  							}	
			       });
			       
             loadGrid();
             showadddetail();
             //检索条件面板
		    $('#fcommonquery').omPanel({
                        title:"检索条件",
                        width: "100%",
			            collapsed : false,//组件创建后为收起状态
			            collapsible : true,//渲染收起与展开按钮
			            closable : false //渲染关闭按钮
		    });
            //--------------增删改-------------------
            $('#buttonbar').omButtonbar({
            	btns : [{label:"新增申请",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
            	 		 	showDetailDialog();
            	 		 }
            			},
            			{separtor:true},
            	        {label:"修改",
            			 id:"button-update",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
            	 		 	editOfficeSuppliesFor();
            
            	 		 }
            	        }, 
            	         {separtor:true},
            	        {label:"删除",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
            	 		 	delInventory();
            	 		 }
            	        } 
            	]
            });
            
            //保存办公用品申领
             $('#savedata').click(function(){
             	
            	var personData = $("#addDetail").omGrid("getData").rows[0];
            	
            	var itemsData = $("#itemDetails").omGrid("getData").rows[0];
            	
				var fnumber = itemsData.fnumber;
				
				var famount = itemsData.famount;            	
            	
            	var fapprover = $("#fassist").val();
            	
            	if(itemsData.fnumber==""){
            		alert("请输入要申请的数量！");
            		
            		return ;
            	}
            	
            	//fnumber  输入数量         famount  库存
            	if(fnumber > famount){
            		
            		alert("要申请的数量已经大于库存了!");
            		
            		return ;
            	}
            	if(fapprover == ""){
            		alert("请选择办公用品管理员");
            		
            		return ;
            	}
            	/*****此处传递data到后台并处理*******/
                var personDataStr = JSON.stringify(personData);
                
                var itemsDataStr = JSON.stringify(itemsData);
                
                var operation ="add";
                
                 $.ajax({  
		            type: "POST",  
		            url: "addOfficeSuppliesFor?fapprover="+fapprover, 
		            data: "&personDataStr="+personDataStr+"&itemsDataStr="+itemsDataStr+"&operation="+operation,  
		            async: false,  
		            dataType: "json",  
		            success: function(data){  
		                if(data.success == "success"){
		                  $("#dialog-modal").omDialog('close');
		                  loadGrid();
		                  $.omMessageTip.show({title: "操作成功", content: "保存成功", timeout: 1500});
		                }  
                   }  
               });     
		});
		
		//删除
		$('#delete').click(function(){
             	
            	var detailData = $("#detailStationery").omGrid("getData").rows[0];
            	
            	delOfficeSuppliesFor(detailData.fid);
		});
        
             //新增
             $( "#dialog-modal").omDialog({
	            autoOpen: false,
				height: 470,
				width:900,
				modal: true
       		 });
       		 
       		 //办公用品申领详情
       		 $("#detail").omDialog({
	            autoOpen: false,
				height: 470,
				width:900,
				modal: true
       		 });
       		 
       		  //编辑
       		 $("#editStationery").omDialog({
	            autoOpen: false,
				height: 470,
				width:900,
				modal: true
       		 });
       		 
       		/*  $('#AddOrDeleteBtn').omButtonbar({
            	btns : [{label:"新增",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
            	 		 	$('#itemDetails').omGrid('insertRow',0);
            	 		 }
            		},
            			{separtor:true},
            	        {label:"删除",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
	            	 		 var dels = $('#itemDetails').omGrid('getSelections');
	            	         if(dels.length <= 0 ){
	            		     alert('请选择删除的记录！');
	            		     return;
            	            }
            	            $('#itemDetails').omGrid('deleteRow',dels[0]);
            	 		 }
            	    }   
            	]
            }); */
       		 
       		 $('#query').bind('click', function(e) {
                var conditions = $('#find').val();
                var recipient = $('#recipient').val();
                var querySelect = $("#fssbm").find("option:selected").text();
                
                if(conditions == "" && recipient == "" && querySelect == ""){ //没要有查询条件，要显示全部数据
                    $('#grid').omGrid("setData", 'queryAllApply');
                }else{ //有查询条件，显示查询数据
                	
                    $('#grid').omGrid("setData", 'fuzzyQuery?conditions='+ conditions+'&recipient='+recipient+'&querySelect='+querySelect);
                	
                	$('#find').val('');
                	$('#recipient').val('');
                	$('##querySelect option:selected').val('');
                }
			});
			
			$("#dialog-modal-add").omDialog({
					title:'办公用品管理员',
					autoOpen: false,
					height: 380,
					width: 850,
					modal: true
					});
			
       		initSendPersonGrid();
        });
        
        function showPersonnel(){
  			
		  $("#dialog-modal-add").omDialog('open');
        }
        
        function closeModel(data){
        
          $("#"+data).omDialog('close');
        
        }
        
       function showDetailDialog(){
       		
	 	 	$( "#dialog-modal").omDialog('open');
	 	 	
	 	 	/* showadddetail(); */
	 	 	
	 	 	showItemDetail();
	 	 	
	 	 	showPersonInfo();
	 	 	
		}
		
	    //显示添加详细
		function showadddetail(){
		    $('#addDetail').omGrid({
                limit:5,
                title : '',
                width:880,
                height : 50,
                colModel : [ 
                             {header : '领用人', name : 'freceiver', width : 290, align : 'left'}, 
                             {header : '领用部门', name : 'frecipientsdepartment', width : 290, align : 'left'}, 
                             {header : '领用日期', name : 'frecipientsdate', width : 300, align : 'left'}, 
                            ],
                            dataSource : "queryPersonalInformation"
		      });
		}
		function showItemDetail(){
			$('#itemDetails').omGrid({
                limit:0,
                title : '',
                width:870,
                height : 150,
                colModel : [ 
                             {header : '请选择物品', name : 'fname', width : 155, align : 'center',renderer:function(colValue, rowData, rowIndex){
                            	 var data = rowData;
                            	 return '<select id="secretLevel" name="from" reg="[^0]" onchange="select()"><option >请选择</option><option value="pen">签字笔</option><option value="bumf">卷纸</option><option value="pencil">铅笔</option><option value="notebookComputer">笔记本电脑</option></select>';
                             }}, 
                             {header : '物品', name : 'fname', width : 155, align : 'center'}, 
                             {header : '型号', name : 'ftype', width : 155, align : 'center'}, 
                             {header : '库存', name : 'famount', width : 155, align : 'center'}, 
                             {header : '请双击输入要申领的数量', name : 'fnumber', width : 155, align : 'center',editor:{editable:true}}, 
                            ],
                            dataSource : "queryPersonalInformation"
		      });
		}
	  	function showPersonInfo(){
	  		
	  		
	  		var personData = $("#addDetail").omGrid("getData").rows[0];
	  		
	  		$("#frecipientsdepartment").val(personData.frecipientsdepartment).attr("disabled","disabled");
	  		$("#freceiver").val(personData.freceiver).attr("disabled","disabled");
	  		$("#frecipientsdate").val(personData.frecipientsdate).attr("disabled","disabled");
	  		
	  	}
	  
	  //下拉框被选择
	  
	  function select(){
	  
	  	var secretLevel = $('#secretLevel').find('option:selected').text();
	  	
	  	$('#itemDetails').omGrid({
                limit:0,
                title : '',
                width:870,
                height : 150,
                
                colModel : [ 
                             {header : '物品', name : 'fname', width : 197, align : 'center'}, 
                             {header : '型号', name : 'ftype', width : 197, align : 'center'}, 
                             {header : '库存', name : 'famount', width : 197, align : 'center'}, 
                             {header : '请双击输入要申领的数量', name : 'fnumber', width : 197, align : 'center',editor:{editable:true}}, 
                            ],
                            dataSource : "searchByName?fname="+secretLevel
		      });
	  }
	
      //编辑
      function editOfficeSuppliesFor(){
      
      var index = $('#grid').omGrid('getSelections');
      
      var data = $("#grid").omGrid("getData").rows[index];
      
         if(index.length<=0){
           alert("请选择一条数据");
           return ;
         }
       
        $("#editStationery").omDialog('open');
        
        $("#editpid").val(data.fid);
        //领用人
        $("#edfreceiver").val(data.freceiver);
        //申请部门
        $("#edfdepartment").val(data.fdepartment).attr("disabled","disabled");
        //领用日期
        $("#edfrecipientsdate").val(data.frecipientsdate);
        //领用部门
        $("#edfrecipientsdepartment").val(data.frecipientsdepartment); 
        //申请人
        $("#edfapplicant").val(data.fapplicant).attr("disabled","disabled"); 
        //申请日期
        $("#edfapplydate").val(data.fapplydate).attr("disabled","disabled");
        //审核人
        $("#edfapprover").val(data.fapprover).attr("disabled","disabled");
        
       // $("#edfapproveldate").val(data.fapproveldate);
        
        showModification();
        
        loadEditDetailTable(data.fid); 
		        
      }
    
       //加载编辑用户数据
       function loadEditDetailTable(fid){
		    $('#editDetail').omGrid({
                limit:0,
                title : '详情(双击修改)',
                width:870,
                height : 150,
                colModel : [ 
                             {header : '数量', name : 'fnumber', width : 110, align : 'left',editor:{editable:true}}, 
                             {header : '创建者', name : 'fcreateman', width : 110, align : 'left'}, 
                             {header : '创建日期', name : 'fcreatedate', width : 110, align : 'left'}, 
                             {header : '修改者', name : 'fmodifier', width : 95, align : 'left',renderer:function(colValue, rowData, rowIndex){
                            	 var data = rowData;
                            	 return '('+$("#addDetail").omGrid("getData").rows[0].freceiver+')';
                            	 }
                             }, 
                             {header : '修改日期', name : 'fmodifieddate', width : 115, align : 'left',renderer:function(colValue, rowData, rowIndex){
                            	 var data = rowData;
                            	 return '('+$("#addDetail").omGrid("getData").rows[0].frecipientsdate+')';
                            	 }},
                             {header : '办公用品型号规格', name : 'fmodel', width : 95, align : 'left'},
                             {header : '办公用品名称', name : 'fname', width : 115, align : 'left'},
                            ],
                              
				dataSource : 'queryApplyDetail?fofficedepotid='+fid
            });
            
            
             //修改   保存
             $('#save').click(function(){
             	
            	var data = $('#editDetail').omGrid('getData').rows[0];
            	
            	var personData = $('#addDetail').omGrid('getData').rows[0];
            	
            	/*****此处传递data到后台并处理*******/
                var formDataStr = JSON.stringify(data);
                var personInfo = JSON.stringify(personData);
                
                var editdata = $("#editfrom").serialize();
                var operation ="modify";
                 $.ajax({  
		            type: "POST",  
		            url: "addOfficeSuppliesFor", 
		            data: editdata+"&formData="+formDataStr+"&operation="+operation+"&personDataStr="+personInfo,  
		            async: false,  
		            dataType: "json",  
		            success: function(data){  
		                if(data.success == "success"){ 
		                  $("#editStationery").omDialog('close');
		                  loadGrid();
		                  $.omMessageTip.show({title: "操作成功", content: "修改数据成功", timeout: 1500});
		                }  
                   }  
               });     
		});      
	}
		//修改人
		function showModification(){
            	
            	var personData = $("#addDetail").omGrid("getData").rows[0];
	  			
	  			$("#edfmodifier").val(personData.freceiver).attr("disabled","disabled"); 
        
        		$("#edfmodifydate").val(personData.frecipientsdate).attr("disabled","disabled"); 
            	
            }
	     //详情
		function showRowData(index,e){
		
		var data = $("#grid").omGrid("getData").rows[index];//拿到数据库表的字段
		$("#detail").omDialog('open');
		$("#detail_title").html(data.ftitle);
		
        $("#detail_fdepartment").val(data.fdepartment).attr("disabled","disabled");
        $("#detail_freceiver").val(data.freceiver).attr("disabled","disabled");
        $("#detail_fapplicant").val(data.fapplicant).attr("disabled","disabled");
        $("#detail_fapplydate").val(data.fapplydate).attr("disabled","disabled");
        $("#detail_fapprover").val(data.fapprover).attr("disabled","disabled"); 
        $("#detail_fstatus").val(data.fstatus).attr("disabled","disabled"); 
        
		loadDetailTable(data.fid);
		
		}
		
		//加载详情数据
		function loadDetailTable(fid){
		    $('#detailStationery').omGrid({
                limit:0,
                title : '详情',
                width:850,
                height : 200,
                colModel : [ 
                             {header : '数量', name : 'fnumber', width : 150, align : 'left'}, 
                             {header : '创建者', name : 'fcreateman', width : 150, align : 'left'}, 
                             {header : '创建日期', name : 'fcreatedate', width : 150, align : 'left'},
                             {header : '办公用品型号规格', name : 'fmodel', width : 150, align : 'left'},
                             {header : '办公用品名称', name : 'fname', width : 150, align : 'left'}
                             
                            ],
                              
				dataSource : 'queryApplyDetail?fofficedepotid='+fid
            });
            
	}
	
	 function delInventory(){
		
	  var index = $('#grid').omGrid('getSelections');
      
      var data = $("#grid").omGrid("getData").rows[index];
      if(index.length<=0){
           alert("请选择一条数据");
           return ;
        }
	   var operation = "delete";
       $.ajax({  
		         type: "POST",  
		         url: "deleteOfficeSuppliesFor?fid="+data.fid+"&operation="+operation, 
		         dataType: "json",  
		         success: function(data){  
		            if(data.success == "success"){ 
		                loadGrid();
		                //删除
		                $.omMessageTip.show({title: "操作成功", content: "删除数据成功", timeout: 1500});
		             }  
                }  
            }); 
     }
	
	   //删除详情
    function delDetailStationery(index,e){
	
	   var data = $("#editDetail").omGrid("getData").rows[index];
          $.ajax({  
		            type: "POST",  
		            url: "deleteOfficeSuppliesFor?fid="+data.fid, 
		            dataType: "json",  
		            success: function(data){  
		                if(data.success == "success"){ 
		                   //页面删除
		                    $('#editDetail').omGrid('deleteRow',index);
		                }  
                   }  
               }); 
    	 }
     
  		
  		//初始化发送人员表
  	 	function initSendPersonGrid(){
  	 		 var f_height=300;
	          $('#panel').css("height",f_height);
	          $('#center_panel').css("height",f_height);
	          $('#west_panel').css("height",f_height);
	          var coML=[  
                        {header : '所属部门', name : 'fssbm',width : 150, align : 'center'},
	                    {header : '人员姓名', name : 'fname',width : 150, align : 'center'},
                        {header : '本人电话', name : 'fbrdh',width : 200, align : 'left'}
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
                height : f_height-3,
                colModel : coML,
                singleSelect:false,
                autoFit:true,
				dataSource :'queryPerson',
				onRowDblClick:function(rowIndex,rowData,event){
                 },
				onSuccess:function(data,testStatus,XMLHttpRequest,event){
   				       
   				       }
		        });
  	 	}
  	 	
  	 	//填充表单'fassist'字段
  	 	function fillThisArgs(){
  	 		var selectedRecords = $('#ftab').omGrid('getSelections',true);
  	 		var personIdItems = '';
			if(selectedRecords == ""){
				alert('请选择接收人员！');
				return;
			}
			for(var i = 0; i < selectedRecords.length; i++){
					personIdItems+=selectedRecords[i].fname+';';
				}
			personIdItems = personIdItems.substring(0, personIdItems.length-1);
			$('#fassist').val(personIdItems);
  	 		$("#dialog-modal-add").omDialog('close');
  	 	}
  	 	
     //删除办公用品申领
     function delOfficeSuppliesFor(fid){
         
      $.ajax({  
		    type: "POST",  
		    url: "deleteOfficeSuppliesFor?fid="+fid, 
		    dataType: "json",  
		    success: function(data){  
		    if(data.success == "success"){ 
		       loadGrid();
		       $("#detail").omDialog('close');
		       $.omMessageTip.show({title: "操作成功", content: "删除数据成功", timeout: 1500});
		      }  
           }  
       }); 
 
     }
     
</script>

<style>
	  input{
	    /* width:150px; */
	    height:20px
	  };
	  select {
	   /*  width:150px; */
	    height:30px;
	  };
	  td{
	    align:right;
	  }
</style>
</head>
<body style="padding: 0px;margin: 0px;">
       <div id="fcommonquery">
                                 领用物品:<input id="find" /> 
                                 领用人:<input id = "recipient"/>
                                 领用部门:
      			<select id="fssbm" name="fssbm" class="sSelect" >
	              <option value=0></option>
	            </select></td>
         <button id="query" style="width:15%">查询</button>
       </div>
       <div id="buttonbar"></div>
       <div>
    	  <table id="grid"></table>
       </div>
       <div id="dialog-modal" title="办公用品领用单">
        	<div style="text-align: center;color: red;"><h1>办公用品领用单</h1></div>
        		<hr style="height:3px;border:none;border-top:3px double red;margin-bottom: 20px"/>
        		<table width="100%" style="text-align: left;margin-bottom: 20px;">
        		<tr>
        		<td width="10%">办公用品管理员:</td>
        		<td>
        		<table id="page2_table2" ">
         		<tr>
				<td width="35%"><input type="text" name="fassist" id="fassist" onclick="showPersonnel()" /></td>
				</tr>
				</table>
        		</td>
        		<td width="10%" >领用人:</td>
        		<td width="35%" ><input type="text" id="freceiver" name="freceiver" /></td>
        		</tr>
        		<tr>
        		<td width="10%" >领用日期:</td>
        		<td width="35%" ><input type="text" id="frecipientsdate" name="frecipientsdate"  /></td>
        		<td width="10%" >领用部门:</td>
        		<td width="35%" ><input type="text" id="frecipientsdepartment" name="frecipientsdepartment" /></td>
        		</tr>
        	</table>
        	<!--  -->
        	<div style="display: none;" >
        		<table id="addDetail" ></table> 
        	</div>
        	<p style="margin-top: 20px;margin-bottom: 20px;">办公用品领用详情:</p>
        	<!-- <div id="AddOrDeleteBtn"></div> -->
        	<div>
        		<table id="itemDetails"></table>
        	</div>
         <div style="float: left; margin-top: 20px;">
          <input type="button" id="submitData"  value="提交"/>
		  <input type="button" id="savedata"  value="保存"/>
		  <input type="button" onclick="closeModel('dialog-modal')" value="取消"/>
	     </div>
	   
   	  </div> 
	  <div id="dialog-modal-add">
		<div id="panel" style="width:100%">
	     <div id="west_panel" >
		    <ul id="treeInfo"></ul>
	     </div>
		<div id="center_panel">	
			<div><table id="ftab"></table></div>
		</div>
  		</div>
  		<div align="center" style="margin-top: 10px;"><button onclick="fillThisArgs()">确定</button></div>
   	   </div>
	  
	 <div id="editStationery" title="修改办公用品申领">
        	<div style="text-align: center;color: red;"><h1>修改办公用品申领</h1></div>
        	<hr style="height:3px;border:none;border-top:3px double red;"/>

				<form id="editfrom">
					<table width="100%" style="text-align: left;">
						<input type="hidden" id="editpid" name="editpid" value="">
						<tr>
							<td width="10%">领用人:</td>
							<td width="35%"><input type="text" id="edfreceiver"
								name="freceiver" /></td>
							<td width="10%">领用部门:</td>
							<td width="35%"><input type="text" id="edfrecipientsdepartment"
								name="frecipientsdepartment" /></td>	
						</tr>
						<tr>
							<td width="10%">领用日期:</td>
							<td width="35%"><input type="text" id="edfrecipientsdate"
								name="frecipientsdate" /></td>	
							<td width="10%">申请人:</td>
							<td width="35%"><input type="text" id="edfapplicant"
								name="fapplicant" /></td>
						</tr>
						<tr>
							<td width="10%">申请部门:</td>
							<td width="35%"><input type="text" id="edfdepartment"
								name="fdepartment" /></td>
							<td width="10%">申请日期:</td>
							<td width="35%"><input type="text" id="edfapplydate"
								name="fapplydate" /></td>
						</tr>
						<tr>
							<td width="10%">审核人:</td>
							<td width="35%"><input type="text" id="edfapprover"
								name="fapprover" /></td>
							<td width="10%">修改人:</td>
							<td width="35%"><input type="text" id="edfmodifier"
								name="fmodifier" /></td>
						</tr>
						<tr>
							<td width="10%">修改日期:</td>
							<td width="35%"><input type="text" id="edfmodifydate"
								name="fmodifydate" /></td>
						</tr>
						<!-- <tr> 
        	    		<td width="10%" >备注:</td>
        	    		<td width="35%" colspan="3"><textarea id="edfdescription" name="fdescription" rows="5" cols="" style="width: 90%;" maxlength="450" ></textarea></td>
        	    		</tr>  -->
					</table>
				</form>
				<table id="editDetail"></table>
            <div style="float: right;margin-top: 30px;">
		     <input type="button" id="save"  value="保存修改"/>
		     <input type="button" onclick="closeModel('editStationery')" value="取消"/>
	       </div>
   	   </div>	
		
	   <div id="detail" title="办公用品申领详情">
        	<div style="text-align: center;color: red;"><h1><span id ="detail_title"></span></h1></div>
        	<hr style="height:3px;border:none;border-top:3px double red;"/>
        	
        	<table width="100%" style="text-align: left;margin-bottom: 20px;margin-top: 15px;">
        		<tr>
        		<td width="10%" >申请部门:</td>
        		<td width="35%" ><input type="text" id="detail_fdepartment" name="fdepartment" /></td>
        		<td width="10%" >领用人:</td>
        		<td width="35%" ><input type="text" id="detail_freceiver" name="freceiver" /></td>
        		</tr>
        		<tr>
        		<td width="10%" >申请人:</td>
        		<td width="35%" ><input type="text" id="detail_fapplicant" name="fapplicant"  /></td>
        		<td width="10%" >流程状态:</td>
        	    <td width="35%" ><input type="text" id="detail_fstatus" name="fstatus" /></td> 
        		</tr>
        	    <tr>
        	    <td width="10%" >申请日期:</td>
        	    <td width="35%" ><input type="text" id="detail_fapplydate" name="fapplydate" /></td>
        	    <td width="10%" >审核人:</td>
        	    <td width="35%" ><input type="text" id="detail_fapprover" name="fapprover" /></td>  
        	    </tr> 
        	    <tr>
        	     
        	</table>
            <table id="detailStationery"></table>
           <div style="float: right; margin-top: 30px;">
          	 <input type="button" id="submitData"  value="提交"/>
		  	 <input type="button" id="delete"  value="删除"/>
		 	 <input type="button" onclick="closeModel('detail')" value="取消"/>
	     </div>
   	   </div> 
</body>
</html>
