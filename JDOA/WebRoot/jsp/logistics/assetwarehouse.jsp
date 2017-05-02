<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>固定资产仓库</title>
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
	        var fassetname =$("#qfassetname").val();
	        var fuseunit =$("#qfuseunit").val();
	        
			//------------------------------grid基本信息开始------------------------------------
	        var fheight=parent.tabElement.height()-150;
	        var coM=[  
                   	  {header:'资产序号', name : 'fassetsn', width : '150', align : 'center'},
                   	  {header:'资产名称', name : 'fassetname', width : '150', align : 'center'},
                      {header:'型号与规格', name : 'fsepcification', width : '150', align : 'center'},
                      {header:'单价', name : 'funitprice', width : 100, align : 'center'},
                      {header:'数量', name : 'fnumber', width : 100, align : 'center'},
                      {header:'制造商', name : 'fmaker', width : 150, align : 'center'},
                      {header:'使用单位', name : 'fuseunit', width : 100, align : 'center'},
                      {header:'资产类型', name : 'ftype', width : 100, align : 'center'},
                      {header:'申请人', name : 'fapplicant', width : 100, align : 'center'},
                      {header:'创建时间', name : 'fcreatedate', width : 120, align : 'center'},
                      {header:'用途', name : 'fnotes', width : 120, align : 'center'},
                      {header : '状态', name : 'faudit', width : 80, align : 'left',renderer:getstatus}
                    ];
		    $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"warehouseList?fassetname="+fassetname+"&fuseunit="+fuseunit,
				onRowDblClick:function(rowIndex,rowData,event){},
				onSuccess:function(data,testStatus,XMLHttpRequest,event){}
		    });
		    //--------------------------------grid基本信息结束-------------------------------------
	}
	  //状态
     function getstatus(value){
	     if(value == "0"){
	       return "未使用";
	       }else if(value == "1"){
	       return "已使用";
	       }else if(value == "2"){
	       return "已损坏";
	      }else if(value == ""){
	       return "";
	      }
     }
	$(document).ready(function() {
			loadGrid();
		    //检索条件面板
		    $('#fcommonquery').omPanel({
                        title:"检索条件",
                        width: "100%",
			            collapsed : false,//组件创建后为收起状态
			            collapsible : true,//渲染收起与展开按钮
			            closable : false //渲染关闭按钮
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
            	 		 	showDialog('新增资产');
            	 		 }
            			},
            			{separtor:true},
            	        {label:"修改",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
            	 		 	var selections=$('#grid').omGrid('getSelections',true);
                			if (selections.length == 0) {
                    			alert('请至少选择一行记录');
                    			return false;
                			}
                			isAdd = false;
                			showDialog('修改资产信息',selections[0]);//显示dialog
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
									$.post('deleteWarehouse',{operation:'delete',fid:id},function(){
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
                $("input[name='fid']",dialog).val(rowData.fid);
                $("input[name='fassetsn']",dialog).val(rowData.fassetsn);
                $("input[name='fassetname']",dialog).val(rowData.fassetname);
                $("input[name='fsepcification']",dialog).val(rowData.fsepcification);
                $("input[name='fmaker']",dialog).val(rowData.fmaker);
                $("input[name='fuseunit']",dialog).val(rowData.fuseunit);
                $("input[name='fnotes']",dialog).val(rowData.fnotes);
                $("input[name='ftype']",dialog).val(rowData.ftype);
                $("input[name='fassetstate']",dialog).val(rowData.fassetstate);
                $("input[name='funitprice']",dialog).val(rowData.funitprice);
                $("input[name='fnumber']",dialog).val(rowData.fnumber);
               
                dialog.omDialog("option", "title", title);
                dialog.omDialog("open");//显示dialog
            };
            //dialog中点提交按钮时将数据提交到后台并执行相应的add或modify操作
            var submitDialog = function(){
            
	                    var operation = isAdd?'add':'modify';
	                    
	                    $.post('addOrEditwarehouse',$("#ipForm").serialize()+"&operation="+operation,function(){
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
                 资产名称:<span ><input id="qfassetname" style="height:18px;"/></span>
                 使用单位:<span ><input id="qfuseunit" style="height:18px;"/></span>
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
	                <td width="10%" >资产序号：</td>
	                <td width="35%"><input name="fassetsn" class="sText" id="fassetsn" /></td>
	                <td width="10%" >资产名称：</td>
	                <td width="35%"><input name="fassetname" class="sText"id="fassetname" /></td>
	            </tr>
	            <tr>
	                <td width="10%" >型号与规格：</td>
	                <td width="35%"><input name="fsepcification" class="sText"id="fcontractsite" /></td>
	                <td width="10%" >制造商：</td>
	                <td width="35%"><input name="fmaker" class="sText"id="fmaker" /></td>
	            </tr>
	            <tr>
	                <td width="10%" >数量：</td>
	                <td width="35%"><input name="fnumber"class="sText" id="fnumber" /></td>
	                <td width="10%" >单价：</td>
	                <td width="35%"><input name="funitprice" class="sText"id="funitprice" /></td>
	            </tr>
	            <tr>
	                <td width="10%" >资产类型：</td>
	                <td width="35%" ><select  id="ftype" name="ftype" class="sSelect"><option >请选择</option></select></td>
	                <td width="10%" >资产现状：</td>
	                <td width="35%"><input name="fassetstate" class="sText"id="fassetstate" /></td>
	            </tr>
	            <tr>
	                <td width="10%" >用途：</td>
	                <td width="35%"><input name="fnotes"class="sText" id=fnotes /></td>
	                <td width="10%" >使用单位：</td>
	                <td width="35%"><input name="fuseunit"class="sText" id="fuseunit" /></td>
	            </tr>
	        </table>
        </form>
    </div>
</html>