<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>资产采购</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
<style>
	  input{
	    width:150px;
	    height:20px
	  };
	  select {
	    width:150px;
	    height:30px;
	  };
	  td{
	    align:right;
	  }
</style>
<script type="text/javascript">
	function loadGrid(){
	          var fsn =$("#qfsn").val();
	          var ftitle =$("#qftitle").val();
	          var fapplicant =$("#qfapplicant").val(); 
			//------------------------------grid基本信息开始------------------------------------
	         var fheight=parent.tabElement.height()-150;
	         var coM=[ 	     {header:'标题', name : 'ftitle', width : '150', align : 'center'},
                      		 {header:'编号', name : 'fsn', width : '150', align : 'center'},
                     		 {header:'单位名称', name : 'funit', width : 100, align : 'center'},
                   	 		 {header:'申请部门', name : 'fdepartment', width : '150', align : 'center'},
                   	 		 {header:'申请人', name : 'fapplicant', width : 100, align : 'center'},
                    		 {header:'申请日期', name : 'fapplydate', width : 100, align : 'center'},
                     		 {header:'申请金额', name : 'fspend', width : 100, align : 'center'},
                     		 {header:'单据号', name : 'fbillno', width : 100, align : 'center'},
                     		 {header:'状态', name : 'faudit', width : 100, align : 'center',renderer:getstatus},
                             {header : '详情', name : 'operation', width: 120, align:'center', renderer:function(colValue, rowData, rowIndex){
                            	 var data = rowData;
                            	 return '<button onClick="showRowdata('+rowIndex+',event)">详情</button>';
                             }}
                       ];
                             
		    $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"purchaseList?fsn="+fsn+"&ftitle="+ftitle+"&fapplicant="+fapplicant,
				onRowDblClick:function(rowIndex,rowData,event){},
				onSuccess:function(data,testStatus,XMLHttpRequest,event){}
		    });
		    //--------------------------------grid基本信息结束-------------------------------------
	}
	
	    //详情
		function showRowdata(index,e){
		var data = $("#grid").omGrid("getData").rows[index];
		$("#showdetail").omDialog('open');
		$("#xqtitile").html(data.ftitle);
		$("#xqfdepartment").val(data.fdepartment).attr("disabled",true);
        $("#xqfbudget").val(data.fbudget).attr("disabled",true);
        $("#xqfbalance").val(data.fbalance).attr("disabled",true);
        $("#xqfapplicant").val(data.fapplicant).attr("disabled",true);
        $("#xqfselectsubmenu").val(data.fselectsubmenu).attr("disabled",true);
        $("#xqfspend").val(data.fspend).attr("disabled",true);
        $("#xqfbillno").val(data.fbillno).attr("disabled",true); 
        $("#xqfapproveldate").val(data.fapproveldate).attr("disabled",true); 
        $("#xqfapplydate").val(data.fapplydate).attr("disabled",true); 
        $("#xqfunit").val(data.funit).attr("disabled",true);
        $("#xqfsn").val(data.fsn).attr("disabled",true);
		loadDetail(data.fid);
		}
		
		//加载详情数据
		function loadDetail(fid){
		    $('#showdetailtable').omGrid({
                limit:0,
                title : '详情',
                width:980,
                height : 390,
                colModel : [ {header : '资产序号', name : 'fassetsn', width : 100, align : 'left'},
                             {header : '资产名称', name : 'fassetname', width : 100, align : 'left'}, 
                             {header : '型号与规格', name : 'fsepcification', width : 120, align : 'left'}, 
                             {header : '制造商', name : 'fmaker', width : 100, align : 'left'}, 
                             {header : '数量', name : 'fnumber', width : 80, align : 'left'}, 
                             {header : '单价', name : 'funitprice', width : 80, align : 'left'}, 
                             {header : '购买价格', name : 'fcost', width : 80, align : 'left'},
                             {header : '创建人', name : 'fcost', width : 80, align : 'left'},
                             {header : '创建时间', name : 'fcreatedate', width : 80, align : 'left'},
                             {header : '备注', name : 'fnotes', width : 120, align : 'left'}
                            ],
				dataSource : 'purchaseDetails?fid='+fid
            });
	}
	
	 //添加或者修改详细
		function showAddOrEdittable(fid){
		    $('#AddOrEdittable').omGrid({
                limit:0,
                title : '详情',
                width:980,
                height : 390,
                colModel : [ {header : '资产序号', name : 'fassetsn', width : 100, align : 'left',editor:{editable:true}},
                             {header : '资产名称', name : 'fassetname', width : 100, align : 'left',editor:{editable:true}}, 
                             {header : '型号与规格', name : 'fsepcification', width : 120, align : 'left',editor:{editable:true}}, 
                             {header : '制造商', name : 'fmaker', width : 100, align : 'left',editor:{editable:true}}, 
                             {header : '数量', name : 'fnumber', width : 80, align : 'left',editor:{editable:true}}, 
                             {header : '单价', name : 'funitprice', width : 80, align : 'left',editor:{editable:true}}, 
                             {header : '购买价格', name : 'fcost', width : 80, align : 'left',editor:{editable:true}},
                             {header : '备注', name : 'fnotes', width : 120, align : 'left',editor:{editable:true}}
                            ],
                            dataSource : "purchaseDetails?fid="+fid
		      });
		
		}
	    //状态
     function getstatus(value){
	     if(value == "0"){
	       return "草稿";
	       }else if(value == "1"){
	       return "进行中";
	       }else if(value == "2"){
	       return "已审核";
	      }else if(value == ""){
	       return "";
	      }
     }
   
    //删除资产
     function deldata(){
     
      var index = $('#grid').omGrid('getSelections');
      
      var data = $("#grid").omGrid("getData").rows[index];
         if(index.length<=0){
           alert("请选择一条数据");
           return ;
      }
       var id = data.fid;
	   $.omMessageBox.confirm({
			title:'删除确认！',
			content:'删除后将不可恢复，确定要删除吗？',
			onClose:function(v){
			if(v){
			//将选择的记录的id传递到后台去并执行delete操作
				$.post('deletePurchase',{operation:'delete',fid:id},function(){
				loadGrid();//刷新当前页数据
				$.omMessageTip.show({title: "操作成功", content: "删除数据成功", timeout: 1500});
			});
			}
	      }
		});
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
		    
		    //资产详情
       		 $("#showdetail").omDialog({
	            autoOpen: false,
				height: 600,
				width:1000,
				modal: true
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
       		 //-------------------------------添加 修改 删除 详情按钮--------------------------------------------
            $('#AddOrEditBut').omButtonbar({
            	btns : [{label:"新增",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
            	 		 	$('#AddOrEdittable').omGrid('insertRow',0);
            	 		 }
            		},
            			{separtor:true},
            	        {label:"删除",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
	            	 		 var dels = $('#AddOrEdittable').omGrid('getSelections');
	            	         if(dels.length <= 0 ){
	            		     alert('请选择删除的记录！');
	            		     return;
            	            }
            	            $('#AddOrEdittable').omGrid('deleteRow',dels[0]);
            	 		 }
            	    }   
            	]
            });
       		 
		    //-------------------------------增删改查按钮--------------------------------------------
            $('#buttonbar').omButtonbar({
            	btns : [{label:"新增",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
                			    isAdd = true;
                    			showDialog('新增采购');
                			    showAddOrEdittable(' ');
            	 		   }
            			},
            			{separtor:true},
            	        {label:"修改",
            			 id:"button-edit",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
            	 		 	var index = $('#grid').omGrid('getSelections');
                            var data = $("#grid").omGrid("getData").rows[index];
                			if (index.length == 0) {
                    			alert('请至少选择一行记录');
                    			return false;
                			}
                			if(data.faudit!="0"){
                			  alert('只能修改草稿');
                			  return false;
                			}
                			isAdd = false;
                			showDialog('修改采购信息',data);//显示dialog
                			showAddOrEdittable(data.fid);
            	 		 }
            	        },
            	        {separtor:true},
            	        {label:"删除",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
		                	deldata();
            	 		 }
            	        }
            	]
            });
           //---------------------------------增删改按钮结束------------------------------------------------
           var dialog = $("#dialog-form").omDialog({
                width: 1000,
                height : 650,
                autoOpen : false,
                modal : true,
                resizable : false,
                buttons : {
                    "提交" : function(){
		               alert('待开发');
		            },
                    "保存" : function(){
		                submitDialog();
		                document.getElementById("ipForm").reset();
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
                
                var d = new Date();  
                var datestr =  d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	            $("#fapplydate").val(datestr);
                $("#fid",dialog).val(rowData.fid);
		        $("#ftitle",dialog).val(rowData.ftitle);
		        $("#funit",dialog).val(rowData.funit);
		        $("#fsn",dialog).val(rowData.fsn);
		        $("#fspend",dialog).val(rowData.fspend);
                
                if(rowData.fapplicant!=null){
                $("#fapplicant",dialog).val(rowData.fapplicant);
                $("#fdepartment",dialog).val(rowData.fdepartment);
                $("#fapplydate",dialog).val(rowData.fapplydate);
                } 
                
                dialog.omDialog("option", "title", title);
                dialog.omDialog("open");//显示dialog
            };
            //dialog中点提交按钮时将数据提交到后台并执行相应的add或modify操作
            var submitDialog = function(){
                        var data = $('#AddOrEdittable').omGrid('getChanges');
		            	/*****此处传递data到后台并处理*******/
		                var formDataStr = JSON.stringify(data);
	                    var operation = isAdd?'add':'modify';
	                    
	                    $.post('addOrEditPurchase',$("#ipForm").serialize()+"&formData="+formDataStr+"&operation="+operation,function(){
	                    if(isAdd){
	                        $('#grid').omGrid('reload',1);//如果是添加则滚动到第一页并刷新
	                        $.omMessageTip.show({title: "操作成功", content: "添加数据成功", timeout: 1500});
	                    }else{
	                        $('#grid').omGrid('reload');//如果是修改则刷新当前页
	                        $.omMessageTip.show({title: "操作成功", content: "修改数据成功", timeout: 1500});
	                    }
	                    $("#dialog-form").omDialog("close"); //关闭dialog
	                });
                
            };
            //初始化人员选择框方法
            initSendPersonGrid();
	});
	
	    //初始化人员选择框方法
        function initSendPersonGrid(){
  	          var fheight=300;
	          $('#panel').css("height",fheight);
	          $('#center_panel').css("height",fheight);
	          $('#west_panel').css("height",fheight);
	          loadDialog('60%',fheight); 
	          var coM=[  
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
			if(selectedRecords == ""){
				alert('请选择接收人员！');
				return;
			}
			for(var i = 0; i < selectedRecords.length; i++){
					personIdItems+=selectedRecords[i].fname+';';
				}
			personIdItems = personIdItems.substring(0, personIdItems.length-1);
			//把选中的人添加到input输入框中去
			$('#fassist').val(personIdItems);
  	 		$("#dialog-panel").omDialog('close');
  	 	}
</script>
</head>
<body>
       <div id="fcommonquery">
                 标题:<span ><input id="qftitle" style="height:18px;"/></span>
                 申请人:<span ><input id="qfapplicant" style="height:18px;"/></span>
                 编号:<span ><input id="qfsn" style="height:18px;"/></span>
          	<button onclick="loadGrid()">查询</button>
       </div>
       <div id="buttonbar"></div>
       <div>
       <table id="grid"></table>
       </div>
       <div id="dialog-form" >
        <form id="ipForm">
	        <table>
	         <input name="fid" id="fid" type="hidden"/>
	             <tr>
        	        <td  width="10%" >申请人:</td>
        	        <td  width="35%" ><input type="text" disabled="disabled" id="fapplicant"  name="fapplicant"   value="${user.person.fname}" /></td>
        	        <td width="10%" >申请部门:</td>
        		    <td width="35%" ><input type="text"  disabled="disabled" id="fdepartment" name="fdepartment"  value="${user.person.fssbm}"/></td>
        		    <td width="10%" >申请时间:</td>
        		    <td width="35%" ><input type="text"  disabled="disabled" id="fapplydate" name="fapplydate"  /></td>
        	     </tr>
	             <tr>
        	        <td  width="10%" >标题:</td>
        	        <td  width="35%" ><input type="text" class="sText" id="ftitle" name="ftitle" /></td>
        	        <td width="10%" >单位名称:</td>
        		    <td width="35%" ><input type="text" class="sText"id="funit" name="funit" /></td>
        		    <td width="10%" >编号：</td>
	                <td width="35%"><input name="fsn" class="sText"id="fsn" /></td>
        	    </tr>
        		<tr>
	                <td width="10%" >申请金额：</td>
	                <td width="35%"><input name="fspend" class="sText"id="fspend" /></td>
	            </tr>
	        </table>
        </form>
        <div id="AddOrEditBut"></div><div>
        <table id="AddOrEdittable"> </table>
        <br/>
        <tr>
           <td width="10%" >申购管理员:</td>
           <td><input type="text" name="fapprover" id="fassist" readOnly="true" style="width: 450px;"/></td>
        </tr>
       
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
     <div id="showdetail" title="资产采购详情">
        	<div style="text-align: center;color: red;"><h1><span id ="xqtitile"></span></h1></div>
        	<hr style="height:3px;border:none;border-top:3px double red;"/>
        	
        	<table width="100%" style="text-align: left;">
        		<tr>
        	        <td width="10%" >编号：</td>
	                <td width="35%"><input name="fsn" id="xqfsn" /></td>
        	        <td width="10%" >单位名称:</td>
        		    <td width="35%" ><input  id="xqfunit" name="funit" /></td>
        		    <td width="10%" >申请部门:</td>
        		    <td width="35%" ><input type="text"   id="xqfdepartment" name="fdepartment" /></td>
        	    </tr>
	            <tr>
	                <td width="10%" >申请人：</td>
	                <td width="35%"><input name="fapplicant" id="xqfapplicant" /></td>
	                <td width="10%" >申请日期：</td>
	                <td width="35%"><input name="fapplydate" id="xqfapplydate" /></td>
	                <td width="10%" >申请金额：</td>
	                <td width="35%"><input name="fspend" id="xqfspend" /></td>
	            </tr>
        	</table>
            <table id="showdetailtable"></table>
   	   </div> 
   	   
</html>