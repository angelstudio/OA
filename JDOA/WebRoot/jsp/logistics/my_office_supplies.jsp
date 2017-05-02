<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>我的办公用品</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<script type="text/javascript" src="../../js/tool/om-grid-roweditor.js"></script>
<script type="text/javascript" src="../../js/tool/time.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
<script type="text/javascript">
	  
      function loadGrid(){
              var fheight=parent.tabElement.height()-150;
	          var coM=[ 	  
                     	  	  {header:'办公用品名称', name : 'fname', align : 'center', width : '150'},
                     		  {header:'数量', name : 'famount',width: 150,align : 'center'},
                     		  {header:'办公用品型号',name:'ftype',width : 150,align : 'center'},
                     		  {header:'领用日期',name:'frecipients_date',width : 150,align : 'center'},
                     		  {header:'审核人',name:'fauditor',width : 150,align : 'center'},
                     		  {header:'审核日期',name:'faudit_date',width : 150,align : 'center'},
                     		  {header:'备注',name:'fcomment',width: 150,align : 'center'},
                              
                             ];
                       
            $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"queryAllMyOfficeSupplies",
				onRowDblClick:function(rowIndex,rowData,event){},
				onSuccess:function(data,testStatus,XMLHttpRequest,event){}
		        });
     }

        $(document).ready(function() {
        	 
             loadGrid();
             showPersonInfo();
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
            	btns : [{label:"新增",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
            	 		 	alert("请申请领用办公用品！");
            	 		 	//showNewPurchase();
            	 		 }
            			},
            			 {separtor:true},
            	        {label:"修改",
            			 id:"button-update",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
            	 		 	//editInventory();
            	 		 	
	            	 		alert("已领用不能修改！");
            	 		 	
            	 		 }
            	        }, 
            	         /* {separtor:true},
            	        {label:"删除",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
            	 		 	
            	 		 	//delForTheSummary();
            	 		 }
            	        }  */
            	]
            });
            
            //新增的添加按钮
            $('#addpropertybut').omButtonbar({
            	btns : [{label:"新增",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
            	 		 	
            	 		 	$('#add_detail').omGrid('insertRow',0);
            	 		 	
            	 		 }
            			}   
            	]
            });
            
            //保存   新增
             $('#savedata').click(function(){
             
            	//var data = $("#personInfo").omGrid("getData").rows[0];
            	
            	/*****此处传递data到后台并处理*******/
                //var formDataStr = JSON.stringify(data);//详情数据
                
                //
            	//var fapprover = $("#fassist").val();
                
                var dataMap = $('#dataFrom').serialize();
                
                var operation ="add";
                
                 $.ajax({  
		            type: "POST",  
		            url: "addSummaryOfRecipients", 
		            data: dataMap+"&operation="+operation,  
		            async: false,  
		            dataType: "json",  
		            success: function(data){  
		                if(data.success == "success"){
		                  $("#dialog-modal").omDialog('close');
		                  document.getElementById("dataFrom").reset();
		                  loadGrid();
		                  $.omMessageTip.show({title: "操作成功", content: "保存成功", timeout: 1500});
		                }  
                   }  
               });     
		});
        
             //新增
             $( "#dialog-modal").omDialog({
	            autoOpen: false,
				height: 350,
				width:900,
				modal: true
       		 });
       		 
       		 //办公用品申领详情
       		 $("#detail").omDialog({
	            autoOpen: false,
				height: 600,
				width:900,
				modal: true
       		 });
       		 
       		  //编辑
       		 $("#editInventory").omDialog({
	            autoOpen: false,
				height: 400,
				width:900,
				modal: true
       		 });
       		 
       		 
       		 $('#query').bind('click', function(e) {
       		 	//办公物品名称
                var conditions=$('#find').val().trim();
                
                if(conditions == ""){ //没要有查询条件，要显示全部数据
                    $('#grid').omGrid("setData", 'queryAllMyOfficeSupplies');
                    $('#conditions').val('');
                }else{ //有查询条件，显示查询数据
                    $('#grid').omGrid("setData", 'fuzzyQueryMyOfficeSupplies?conditions='+ conditions );
                    $('#conditions').val('');
                  }
			});
			
			
			$('#delete').click(function(){
             	
            	var detailData = $("#detailPurchase").omGrid("getData").rows[0];
            	
            	alert(detailData);
            	
            	delOfficeSuppliesPurchase(detailData.fid);
            	
            	alert(detailData.fid);
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
        
       function showNewPurchase(){
       		
	 	 	$( "#dialog-modal").omDialog('open');
	 	 	
	 	 	
	 	 	/* showPersonInfo(); */
	 	 	
	 	 	setPersonInfo();
	 	 	//showaddxq();
	 	 	
	 	 	
		}
		
	    //显示添加详细
		function showaddxq(){
		
		    $('#add_detail').omGrid({
                limit:0,
                title : '详情',
                width:880,
                height : 390,
                colModel : [ 
                             {header : '备注信息', name : 'fnotes', width : 100, align : 'left',editor:{editable:true}}, 
                             {header : '数量', name : 'fnumber', width : 80, align : 'left',editor:{editable:true}}, 
                             {header : '创建者', name : 'fcreator', width : 80, align : 'left',editor:{editable:true}}, 
                             {header : '创建日期', name : 'fcreationdate', width : 80, align : 'left',editor:{editable:true}},
                             {header : '修改日期', name : 'fmodificationdate', width : 80, align : 'left',editor:{editable:true}},
                             {header : '办公用品型号规格', name : 'fmodel', width : 120, align : 'left',editor:{editable:true}},
                             {header : '办公用品名称', name : 'fname', width : 120, align : 'left',editor:{editable:true}}
                            ],
                            dataSource : "addOfficeSuppliesPurchase"
		      });
		}
		//显示申购人信息
		function showPersonInfo(){
		
		    $('#personInfo').omGrid({
                limit:5,
                title : '',
                width:880,
                height : 50,
                colModel : [ 
                             {header : '申购人', name : 'freceiver', width : 290, align : 'left'}, 
                             {header : '申购部门', name : 'frecipientsdepartment', width : 290, align : 'left'}, 
                             {header : '申购日期', name : 'frecipientsdate', width : 300, align : 'left'}, 
                            ],
                            dataSource : "queryPersonalInformation"
		      });
			
		}
	  function setPersonInfo(){
	  
	  		 var personData = $("#personInfo").omGrid("getData").rows[0];
	  		
	  		$("#frecipientsdepartment").val(personData.frecipientsdepartment).attr("disabled","disabled");
	  		$("#freceiver").val(personData.freceiver).attr("disabled","disabled");
	  		$("#frecipientsdate").val(personData.frecipientsdate).attr("disabled","disabled");  
	  }
      //编辑
      function editInventory(){
      	
        var index = $('#grid').omGrid('getSelections');
      
        var data = $("#grid").omGrid("getData").rows[index];
         if(index.length<=0){
           alert("请选择一条数据");
           return ;
         }
        
        $("#editInventory").omDialog('open');
        
        $("#edfname").val(data.fname).attr("disabled","disabled");
        $("#editpid").val(data.fid);
        $("#edftype").val(data.ftype);
        $("#edfdescription").val(data.fdescription);
        
        $("#edfdepartment").val(data.fdepartment).attr("disabled","disabled");
        $("#edfcreation_time").val(data.fcreation_time).attr("disabled","disabled");
        $("#edfprice").val(data.fprice);   
        $("#edfcategory").val(data.fcategory).attr("disabled","disabled");
        $("#edfsupplier").val(data.fsupplier);
        $("#edfcreator").val(data.fcreator).attr("disabled","disabled");
        $("#fassist").val(data.fapprover); 
        $("#edfcomment").val(data.fcomment); 
        $("#edfamount").val(data.famount);
        
        loadEditDetailTable(data.fid); 
      }
    
       //加载编辑详情数据
       function loadEditDetailTable(fid){
		    $('#editDetail').omGrid({
                limit:5,
                title : '详情',
                width:890,
                height : 390,
                colModel : [ 
                             {header : '数量', name : 'fnumber', width : 100, align : 'left',editor:{editable:true}}, 
                             {header : '创建者', name : 'fcreator', width : 100, align : 'left',editor:{editable:true}}, 
                             {header : '创建日期', name : 'fcreationdate', width : 80, align : 'left',editor:{editable:true}}, 
                             {header : '办公用品型号规格', name : 'fmodel', width : 80, align : 'left',editor:{editable:true}},
                             {header : '办公用品名称', name : 'fname', width : 120, align : 'left',editor:{editable:true}},
                            ],
                              
				dataSource : 'queryOfficesTuffPurchaseDetail?fofficestuffpurchaseid='+fid
            });
            
             //修改   保存
             $('#save').click(function(){
             	
            	var personData = $("#personInfo").omGrid("getData").rows[0];
            	
            	
            	/*****此处传递data到后台并处理*******/
                var formDataStr = JSON.stringify(personData);
                var editdata = $("#editfrom").serialize();
                var operation ="modify";
                $.ajax({  
		            type: "POST",  
		            url: "addInventory", 
		            data: editdata+"&operation="+operation,  
		            async: false,  
		            dataType: "json",  
		            success: function(data){  
		                if(data.success == "success"){ 
		                  $("#editInventory").omDialog('close');
		                  loadGrid();
		                  $.omMessageTip.show({title: "操作成功", content: "修改数据成功", timeout: 1500});
		                }  
                   }  
               });     
		});      
	}
	     //详情
		function showRowdata(index,e){
		
		var data = $("#grid").omGrid("getData").rows[index];//拿到数据库表的字段
		
		$("#detail").omDialog('open');
		$("#xqtitile").html(data.ftitle);
		
		
        $("#xqftitle").val(data.ftitle).attr("disabled","disabled");
        $("#xqfdepartment").val(data.fdepartment).attr("disabled","disabled");
        $("#xqfapplicant").val(data.fapplicant).attr("disabled","disabled");
        $("#xqfapplydate").val(data.fapplydate).attr("disabled","disabled");
        $("#xqfapprover").val(data.fapprover).attr("disabled","disabled"); 
        $("#xqfstatus").val(data.fstatus).attr("disabled","disabled"); 
        
		loadDetailTable(data.fid);
		 
		}
		
		//加载详情数据
		function loadDetailTable(fid){
		    $('#detailPurchase').omGrid({
                limit:0,
                title : '详情',
                width:890,
                height : 200,
                colModel : [ 
                             {header : '数量', name : 'fnumber', width : 155, align : 'left'}, 
                             {header : '创建者', name : 'fcreator', width : 155, align : 'left'}, 
                             {header : '创建日期', name : 'fcreationdate', width : 155, align : 'left'},
                             {header : '办公用品型号规格', name : 'fmodel', width : 160, align : 'left'},
                             {header : '办公用品名称', name : 'fname', width : 165, align : 'left'}
                            ],
                              
				dataSource : 'queryOfficesTuffPurchaseDetail?fofficedepotid='+fid
            });
            
	}
	
	//删除
    function delForTheSummary(){
		
	  var index = $('#grid').omGrid('getSelections');
      
      var data = $("#grid").omGrid("getData").rows[index];
      if(index.length<=0){
           alert("请选择一条数据");
           return ;
        }
	   
       $.ajax({  
		         type: "POST",  
		         url: "deleteSummaryOfRecipients?fid="+data.fid, 
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
                                 办公物品名称:<input id="find" />
         <button id="query" style="width:15%">查询</button>
       </div>
       <div id="buttonbar"></div>
       <div>
    	<table id="grid"></table>
    	
         <div id="dialog-modal" title="新增个人领用汇总">
        	<div style="text-align: center;color: red;"><h1>新增个人领用汇总</h1></div>
        	<hr style="height:3px;border:none;border-top:3px double red;margin-bottom: 30px;"/>
        	
         <form  id="dataFrom">
        	<table width="100%" style="text-align: left;margin-bottom: 20px;">
        	
        	    <tr>
        	    <td  width="10%" >办公用品名称:</td>
        	    <td  width="35%" ><input type="text"  id="" name="fname" /></td>
        	    <td  width="10%" >领用总数量:</td>
        	    <td  width="35%" ><input type="text"  id="" name="ftotal_quantity" /></td>
        	    </tr>
        	    <tr>
        	    
        	    <td  width="10%" >型号:</td>
        	    <td  width="35%" ><input type="text"  id="ftype" name="ftype" /></td>
        	    </tr>
        	    <tr> 
        	    <td width="10%" >备注:</td>
        	    <td width="35%" colspan="3"><textarea id="" name="fcomment" rows="5" cols="" style="width: 90%;" maxlength="450" ></textarea></td>
        	    </tr> 
        	</table>
        	</form>
        	<div style="display: none;" >
        	<table id="personInfo"></table>
            </div> 
           
         <div style="float: left; margin-top: 15px;">
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
   	   
	 <div id="editInventory" title="修改我的领用明细">
        	<div style="text-align: center;color: red;"><h1>修改我的领用明细</h1></div>
        	<hr style="height:3px;border:none;border-top:3px double red;"/>

				<form id="editfrom">
					<table width="100%" style="text-align: left;margin-top: 30px;">
						<input type="hidden" id="editpid" name="editpid" value="">
						<tr>
							<td width="10%">办公用品型号:</td>
							<td width="35%"><input type="text" id="edftype"
								name="ftype" /></td>
							<td width="10%">办公用品单价:</td>
							<td width="35%"><input type="text" id="edfprice"
								name="fprice" /></td>
						</tr>
						<tr>
							<td width="10%">供应商名称:</td>
							<td width="35%"><input type="text" id="edfsupplier"
								name="fsupplier" /></td>
							
							<td width="10%">数量:</td>
							<td width="35%"><input type="text" id="edfamount"
								name="famount" /></td>
						</tr>
						<tr>
							
							
							<td width="10%">办公用品描述:</td>
							<td width="35%"><input type="text" id="edfdescription"
								name="fdescription" /></td>
							
							<td width="10%">类别:</td>
							<td width="35%"><input type="text" id="edfcategory"
								name="fcategory" /></td>
							
						</tr>
						<tr>
							<td width="10%">创建部门:</td>
							<td width="35%"><input type="text" id="edfdepartment"
								name="fdepartment" /></td>
							
							<td width="10%">创建时间:</td>
							<td width="35%"><input type="text" id="edfcreation_time"
								name="fcreation_time" /></td>
						</tr>
						<tr>
							<td width="10%">办公用品名称:</td>
							<td width="35%"><input type="text" id="edfname"
								name="fname" /></td>
							<td width="10%">创建者:</td>
							<td width="35%"><input type="text" id="edfcreator"
								name="fcreator" /></td>
						</tr>
						<tr>
							<td width="10%" >备注:</td>
        	    			<td width="35%" colspan="3"><textarea id="edfcomment" name="fcomment" rows="5" cols="" style="width: 90%;" maxlength="450" ></textarea></td>	
						<tr>
					</table>
				</form>
				<!-- <table id="editDetail"></table> -->
            <div style="float: left;margin-top: 30px;">
		     <input type="button" id="save"  value="保存修改"/>
		     <input type="button" onclick="closeModel('editInventory')" value="取消"/>
	       </div>
   	   </div>	
		
	   <div id="detail" title="办公用品申购详情">
        	<div style="text-align: center;color: red;"><h1><span id ="xqtitile"></span></h1></div>
        	<hr style="height:3px;border:none;border-top:3px double red;margin-bottom: 30px;"/>
        	
        	<table width="100%" style="text-align: left;margin-bottom: 30px;">
        		<tr>
        		    <td width="10%" >标题:</td>
        			<td width="35%" ><input type="text"  id="xqftitle" name="ftitle"  /></td>
        			<td width="10%" >申请部门:</td>
        			<td width="35%" ><input type="text" id="xqfdepartment" name="fdepartment" /></td>
        		</tr>
        		<tr>
        			<td width="10%" >申请人:</td>
        			<td width="35%" ><input type="text" id="xqfapplicant" name="fapplicant"  /></td>
        			<td width="10%" >申请日期:</td>
        	   	 	<td width="35%" ><input type="text" id="xqfapplydate" name="fapplydate" /></td>
        		</tr>
        	    <tr>
        	    	<td width="10%" >流程状态:</td>
        	    	<td width="35%" ><input type="text" id="xqfstatus" name="fstatus" /></td>  
        	    	<td width="10%" >审核人:</td>
        	    	<td width="35%" ><input type="text" id="xqfapprover" name="fapprover" /></td>  
        	    <tr> 
        	</table>
            <table id="detailPurchase"></table>
            <div style="float: right; margin-top: 30px;">
          	 <input type="button" id="submitData"  value="提交"/>
		  	 <input type="button" id="delete"  value="删除"/>
		 	 <input type="button" onclick="closeModel('detail')" value="取消"/>
	     </div>
   	   </div> 
</body>
</html>
