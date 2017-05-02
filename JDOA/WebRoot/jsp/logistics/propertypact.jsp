<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>资产采购合同</title>
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
	        var fpactnum =$("#qfpactnum").val();
	        var fpactname =$("#qfpactname").val();
	        var fsuppliername =$("#qfsuppliername").val();
	        
			//------------------------------grid基本信息开始------------------------------------
	        var fheight=parent.tabElement.height()-150;
	        var coM=[  
                   	  {header:'合同编号', name : 'fpactnum', width : '150', align : 'center'},
                   	  {header:'合同名称', name : 'fpactname', width : '150', align : 'center'},
                      {header:'需方', name : 'fdemandername', width : '150', align : 'center'},
                      {header:'供方', name : 'fsuppliername', width : 150, align : 'center'},
                      {header:'需方地址', name : 'fdemandersite', width : 100, align : 'center'},
                      {header:'供方地址', name : 'fsupplierfaxes', width : 100, align : 'center'},
                      {header:'需方签字', name : 'fdemandersign', width : 100, align : 'center'},
                      {header:'供方签字', name : 'fsuppliersign', width : 100, align : 'center'},
                      {header:'签约地址', name : 'fcontractsite', width : 100, align : 'center'},
                      {header:'签约时间', name : 'fcontracttime', width : 120, align : 'center'},
                      {header : '状态', name : 'faudit', width : 80, align : 'left',renderer:getstatus}
                    ];
		    $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"propertyPactList?fpactnum="+fpactnum+"&fpactname="+fpactname+"&fsuppliername="+fsuppliername,
				onRowDblClick:function(rowIndex,rowData,event){},
				onSuccess:function(data,testStatus,XMLHttpRequest,event){}
		    });
		    //--------------------------------grid基本信息结束-------------------------------------
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
	$(document).ready(function() {
			loadGrid();
		    //检索条件面板
		    $('#fcommonquery').omPanel({
                        title:"检索条件",
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
            	 		    isAdd = true;
            	 		 	showDialog('新增合同');
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
                			showDialog('修改合同信息',selections[0]);//显示dialog
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
									$.post('deletePact',{operation:'delete',fid:id},function(){
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
                $("input[name='fdemandername']",dialog).val(rowData.fdemandername);
                $("input[name='fsuppliername']",dialog).val(rowData.fsuppliername);
                $("input[name='fdemandersite']",dialog).val(rowData.fdemandersite);
                $("input[name='fdemanderfaxes']",dialog).val(rowData.fdemanderfaxes);
                $("input[name='fdemanderphone']",dialog).val(rowData.fdemanderphone);
                $("input[name='fdemanderagent']",dialog).val(rowData.fdemanderagent);
                $("input[name='fdemanderbankdoor']",dialog).val(rowData.fdemanderbankdoor);
                $("input[name='fdemanderaccount']",dialog).val(rowData.fdemanderaccount);
                $("input[name='fdemanderdutynum']",dialog).val(rowData.fdemanderdutynum);
                $("input[name='fdemandersign']",dialog).val(rowData.fdemandersign);
                $("input[name='fsuppliersite']",dialog).val(rowData.fsuppliersite);
                $("input[name='fsupplierfaxes']",dialog).val(rowData.fsupplierfaxes);
                $("input[name='fsupplierphone']",dialog).val(rowData.fsupplierphone);
                $("input[name='fsupplieragent']",dialog).val(rowData.fsupplieragent);
                $("input[name='fsupplierbankdoor']",dialog).val(rowData.fsupplierbankdoor);
                $("input[name='fsupplieraccount']",dialog).val(rowData.fsupplieraccount);
                $("input[name='fsuppliersign']",dialog).val(rowData.fsuppliersign);
                $("input[name='fsupplierdutynum']",dialog).val(rowData.fsupplierdutynum);
                $("input[name='fcontractsite']",dialog).val(rowData.fcontractsite);
                $("input[name='fcontracttime']",dialog).val(rowData.fcontracttime);
                $("input[name='fpactnum']",dialog).val(rowData.fpactnum);
                $("input[name='fpactname']",dialog).val(rowData.fpactname);
                dialog.omDialog("option", "title", title);
                dialog.omDialog("open");//显示dialog
            };
            //dialog中点提交按钮时将数据提交到后台并执行相应的add或modify操作
            var submitDialog = function(){
            
	                    var operation = isAdd?'add':'modify';
	                    
	                    $.post('addOrEditpact',$("#ipForm").serialize()+"&operation="+operation,function(){
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
                 合同名称:<span ><input id="qfpactname" style="height:18px;"/></span>
                 供方:<span ><input id="qfsuppliername" style="height:18px;"/></span>
                 合同编号:<span ><input id="qfpactnum" style="height:18px;"/></span>
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
	                <td width="15%" >合同编号：</td>
	                <td width="35%"><input name="fpactnum" id="fpactnum" class="sText" /></td>
	                <td width="15%" >合同名称：</td>
	                <td width="35%"><input name="fpactname" id="fpactname" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >签约地址：</td>
	                <td width="35%"><input name="fcontractsite" id="fcontractsite" class="sText"/></td>
	                <td width="15%" >签约时间：</td>
	                <td width="35%"><input name="fcontracttime" id="fcontracttime" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >需方：</td>
	                <td width="35%"><input name="fdemandername" id="fdemandername"class="sText" /></td>
	                <td width="15%" >供方：</td>
	                <td width="35%"><input name="fsuppliername" id="fsuppliername" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >需方地址：</td>
	                <td width="35%"><input name="fdemandersite" id="fdemandersite" class="sText"/></td>
	                <td width="15%" >供方地址：</td>
	                <td width="35%"><input name="fsuppliersite" id="fsuppliersite" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >需方传真：</td>
	                <td width="35%"><input name="fdemanderfaxes" id="fdemanderfaxes" class="sText"/></td>
	                <td width="15%" >供方传真：</td>
	                <td width="35%"><input name="fsupplierfaxes" id="fsupplierfaxes" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >需方电话：</td>
	                <td width="35%"><input name="fdemanderphone" id="fdemanderphone" class="sText"/></td>
	                <td width="15%" >供方电话：</td>
	                <td width="35%"><input name="fsupplierphone" id="fsupplierphone" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >需方经办人：</td>
	                <td width="35%"><input name="fdemanderagent" id="fdemanderagent" class="sText"/></td>
	                <td width="15%" >供方经办人：</td>
	                <td width="35%"><input name="fsupplieragent" id="fsupplieragent" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >需方开户行：</td>
	                <td width="35%"><input name="fdemanderbankdoor" id="fdemanderbankdoor" class="sText"/></td>
	                <td width="15%" >供方开户行：</td>
	                <td width="35%"><input name="fsupplierbankdoor" id="fsupplierbankdoor" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >需方账号：</td>
	                <td width="35%"><input name="fdemanderaccount" id="fdemanderaccount"class="sText" /></td>
	                <td width="15%" >供方账号：</td>
	                <td width="35%"><input name="fsupplieraccount" id="fsupplieraccount" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >需方税号：</td>
	                <td width="35%"><input name="fdemanderdutynum" id="fdemanderdutynum" class="sText"/></td>
	                <td width="15%" >供方税号：</td>
	                <td width="35%"><input name="fsupplierdutynum" id="fsupplierdutynum" class="sText"/></td>
	            </tr>
	            <tr>
	                <td width="15%" >需方签字：</td>
	                <td width="35%"><input name="fdemandersign" id="fdemandersign" class="sText"/></td>
	                <td width="15%" >供方签字：</td>
	                <td width="35%"><input name="fsuppliersign" id="fsuppliersign" class="sText"/></td>
	            </tr>
	        </table>
        </form>
    </div>
</html>