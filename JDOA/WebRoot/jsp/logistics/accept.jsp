<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>资产验收</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />
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
	        var ftitle =$("#qftitle").val();
	        var fapplicant =$("#qfapplicant").val();
	        
			//------------------------------grid基本信息开始------------------------------------
	        var fheight=parent.tabElement.height()-150;
	        var coM=[  
                   	  {header:'标题', name : 'ftitle', width : '150', align : 'center'},
                      {header:'资产类型', name : 'ftype', width : 100, align : 'center'},
                      {header:'单位名称', name : 'fuseunit', width : 100, align : 'center'},
                   	  {header:'申请部门', name : 'fdepartment', width : '150', align : 'center'},
                   	  {header:'申请人', name : 'fapplicant', width : 150, align : 'center'},
                      {header:'申请日期', name : 'fapplydate', width : 150, align : 'center'},
                      {header:'审核人', name : 'fapprover', width : 100, align : 'center'},
                      {header : '状态', name : 'faudit', width : 80, align : 'left',renderer:getstatus}
                    ];
		    $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"acceptList?ftitle="+ftitle+"&fapplicant="+fapplicant,
				onRowDblClick:function(rowIndex,rowData,event){},
				onSuccess:function(data,testStatus,XMLHttpRequest,event){}
		    });
		    //--------------------------------grid基本信息结束-------------------------------------
	}
	
	function showPurchase(fid){
	
			 $('#addchasetable').omGrid({
			    limit:0,
                title : '详情',
                width:785,
                height : 300,
                colModel : [ {header:'标题', name : 'ftitle', width : '50', align : 'center'},
                      		 {header:'编号', name : 'fsn', width : '80', align : 'center'},
                     		 {header:'单位名称', name : 'funit', width : 50, align : 'center'},
                   	 		 {header:'申请部门', name : 'fdepartment', width : '50', align : 'center'},
                   	 		 {header:'申请人', name : 'fapplicant', width : 50, align : 'center'},
                    		 {header:'申请日期', name : 'fapplydate', width : 100, align : 'center'},
                     		 {header:'申请金额', name : 'fspend', width : 80, align : 'center'}
                            ],
                            dataSource : "qureyonepurchase?fid="+fid
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
      
      //加载采购信息
      function loadpurchase(){
      
       var pruchaseid = $("#selpurchase").val();
       
       showPurchase(pruchaseid);   
               
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
		    
		     $.ajax({  
			    type: "POST",  
			    url: "qureypurchase", 
			    dataType: "json",  
			    success: function(data){  
			    if(data.success == "success"){ 
			       var listdata = data.listdata;
			       for (var i = listdata.length - 1; i >= 0; i--) {
				   $("#selpurchase").prepend('<option value="' + listdata[i].fpurchaseid + '">' + listdata[i].ftitle + '</option>')
				  };
				  $("#selpurchase").prepend('<option value="0">请选择</option>')
			     }  
	           }  
            });  
            
               $.ajax({
  							type: 'POST',
  							url:'propertyType', 
  							dataType:'json', 							
  							contentType:'application/x-www-form-urlencoded; charset=utf-8',
  							success: function(data){
  							   for( var i in data){
  							     $("#ftype").append('<option value="'+data[i]+'">'+data[i]+'</option>');
  							   }
  							}	
			            });	
            
		    //-------------------------------增删改查按钮--------------------------------------------
            $('#buttonbar').omButtonbar({
            	btns : [{label:"新增",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
            	 		    isAdd = true;
            	 		 	showDialog('新增采购');
            	 		 	$("#selpurchase").val("0");
            	 		 	showPurchase('');
            	 		   }
            			},
            			{separtor:true},
            	        {label:"修改",
            			 id:"button-remove",
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
                			loadpurchase();
            	 		 }
            	        },
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
									$.post('deleteAccept',{operation:'delete',fid:id},function(){
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
                width: 800,
                height : 500,
                autoOpen : false,
                modal : true,
                resizable : false,
                buttons : {
                    "提交" : function(){
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
                $("#selpurchase").val(rowData.pruchaseid);
                $("input[name='fid']",dialog).val(rowData.fid);
                $("input[name='ftitle']",dialog).val(rowData.ftitle);
                $("input[name='fuseunit']",dialog).val(rowData.fuseunit);
                $("input[name='ftype']",dialog).val(rowData.ftype);
                dialog.omDialog("option", "title", title);
                dialog.omDialog("open");//显示dialog
            };
            //dialog中点提交按钮时将数据提交到后台并执行相应的add或modify操作
            var submitDialog = function(){
            
	                    var operation = isAdd?'add':'modify';
	                    var fpurchaseid = $("#selpurchase").val();
	                    if(fpurchaseid == "0"){
	                       alert("请选择一行记录");
	                       return ;
	                    }
	                    $.post('addOrEditAccept',$("#ipForm").serialize()+"&operation="+operation+"&fpurchaseid="+fpurchaseid,function(){
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
	});
	
</script>
</head>
<body>
       <div id="fcommonquery">
                 标题:<span ><input id="qftitle" style="height:18px;"/></span>
                 申请人:<span ><input id="qfapplicant" style="height:18px;"/></span>
          	<button onclick="loadGrid()">查询</button>
       </div>
       <div id="buttonbar"></div>
       <div>
       <table id="grid"></table>
       </div>
       <div id="dialog-form">
        <form id="ipForm">
	        <input name="fid" style="display: none"/>
	        <table>
	            <tr>
	                <td width="10%" ">标题：</td>
	                <td width="35%"><input name="ftitle" id="ftitle" class="sText" /></td>
	                <td width="10%" ">单位名称：</td>
	                <td width="35%"><input name="fuseunit" id="fuseunit"  class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="10%" >资产类型：</td>
	                <td width="35%" ><select  id="ftype" name="ftype"  class="sSelect" ><option >请选择</option></select></td>
	            </tr>
	            
	        </table>
        </form>
                 已审核通过的采购单：
        <select id="selpurchase" onchange="loadpurchase()" class="sSelect"></select>
        <table id="addchasetable"></table>
    </div>
</html>