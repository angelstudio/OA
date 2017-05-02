<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用车管理</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />

<script type="text/javascript">
	function loadGrid(){
			//------------------------------grid基本信息开始------------------------------------
	        var fheight=parent.tabElement.height()-150;
	        var coM=[  
                   	  {header:'车型', name : 'licence', width : '150', align : 'center',wrap:true},
                      {header:'品牌', name : 'brand', width : '100', align : 'center',wrap:true},
                      {header:'车辆信息', name : 'specification', width : 160, align : 'center'},
                      {header:'车辆状态', name : 'status', width : 100, align : 'center'},
                      {header:'责任人', name : 'person', width : 100, align : 'center'},
                      {header:'车架号', name : 'serialnumber', width : 100, align : 'center'},
                      {header:'创建人', name : 'createman', width : 100, align : 'center'},
                      {header:'创建时间', name : 'createdate', width : 150, align : 'center'}
                    ];
		    $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"getClglList",
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
                        width: "100%",
			            collapsed : false,//组件创建后为收起状态
			            collapsible : true,//渲染收起与展开按钮
			            closable : false //渲染关闭按钮
		    });
		    
		    //-------------------------------增删改查按钮--------------------------------------------
		    var isAdd = true; //弹出窗口中是添加操作还是修改操作？
            $('#buttonbar').omButtonbar({
            	btns : [{label:"新增",
            		     id:"button-new" ,
            		     icons : {left : '../../image/common/add.png'},
            	 		 onClick:function(){
            	 		 	showDialog('新增车辆');
            	 		 }
            			},
            			{separtor:true},
            	        {label:"修改",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/remove.png'},
            	 		 onClick:function(){
            	 		 	var selections=$('#grid').omGrid('getSelections',true);
                			if (selections.length == 0) {
                    			alert('请至少选择一行记录');
                    			return false;
                			}
                			isAdd = false;
                			showDialog('修改车辆信息',selections[0]);//显示dialog
            	 		 }
            	        },
            	        {separtor:true},
            	        {label:"删除",
            			 id:"button-remove",
            			 icons : {left : '../../image/common/op-edit.png'},
            	 		 onClick:function(){
            	 		 	var selections=$('#grid').omGrid('getSelections',true);
			                if (selections.length == 0) {
			                    alert('请至少选择一行记录');
			                    return false;
			                }
                			//将选择的记录的id传递到后台去并执行delete操作
                			var id = selections[0].id;
		                	$.post('deleteClglxx',{operation:'delete',id:id},function(){
		                    	$('#grid').omGrid('reload');//刷新当前页数据
		                    	$.omMessageTip.show({title: "操作成功", content: "删除数据成功", timeout: 1500});
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
                $("input[name='licence']",dialog).val(rowData.licence);
                $("input[name='brand']",dialog).val(rowData.brand);
                $("input[name='specification']",dialog).val(rowData.specification);
                $("input[name='person']",dialog).val(rowData.person);
                $("input[name='serialnumber']",dialog).val(rowData.serialnumber);
                $("input[name='photo']",dialog).val(rowData.photo);
                $("input[name='arctic']",dialog).val(rowData.arctic);
                $("input[name='info']",dialog).val(rowData.info);
                $("input[name='notes']",dialog).val(rowData.notes);
                $("input[name='fid']",dialog).val(rowData.fid);
                dialog.omDialog("option", "title", title);
                dialog.omDialog("open");//显示dialog
            };
            //dialog中点提交按钮时将数据提交到后台并执行相应的add或modify操作
            var submitDialog = function(){
                if (validator.form()) {
	                var submitData={
	                    oper:isAdd?'1':'2',
	                    licence:$("input[name='licence']",dialog).val(),
	                    brand:$("input[name='brand']",dialog).val(),
	                    specification:$("input[name='specification']",dialog).val(),
	                    photo:$("input[name='photo']",dialog).val(),
	                    person:$("input[name='person']",dialog).val(),
	                    serialnumber:$("input[name='serialnumber']",dialog).val(),
	                    arctic:$("input[name='arctic']",dialog).val(),
	                    info:$("input[name='info']",dialog).val(),
	                    fid:$("input[name='fid']",dialog).val(),
	                    notes:$("input[name='notes']",dialog).val()
	                };
	                $.post('saveClglxx',submitData,function(){
	                    if(isAdd){
	                        $('#grid').omGrid('reload',1);//如果是添加则滚动到第一页并刷新
	                        $.omMessageTip.show({title: "操作成功", content: "添加数据成功", timeout: 1500});
	                    }else{
	                        $('#grid').omGrid('reload');//如果是修改则刷新当前页
	                        $.omMessageTip.show({title: "操作成功", content: "修改数据成功", timeout: 1500});
	                    }
	                    $("#dialog-form").omDialog("close"); //关闭dialog
	                });
                }
            };
            
            // 对表单进行校验
            var validator = $('#ipForm').validate({
                rules : {
                    licence : {required: true}, 
                    person : {required : true},
                    serialnumber : {required : true}
                }, 
                messages : {
                    licence : {required : "车牌号不能为空"},
                    person : {required : "责任人不能为空"},
                    serialnumber : {required : "车架号不能为空"}
                }
            });
	});
	
</script>
</head>
<body>
       <div id="fcommonquery">
                  车牌号:<span id="fxmspan"><input id="fggxmmc" style="height:18px;"/></span>
                  车辆状态:<span id="fsgspan"><input id="fggsgdw" style="height:18px;"/></span>
          	<button id="fggquery">查询</button>
           <button id="fggreset">重置</button>
       </div>
       <div id="buttonbar"></div>
       <div>
       <table id="grid"></table>
       </div>
       <div id="dialog-form">
        <form id="ipForm">
	        <input name="fid" id="fid" style="display: none"/>
	        <table>
	            <tr>
	                <td width="10%" align="right">车牌号：</td>
	                <td width="35%"><input name="licence" id="licence" /></td>
	                <td width="10%" align="right">品牌：</td>
	                <td width="35%"><input name="brand" id="brand" /></td>
	            </tr>
	            <tr>
	                <td width="10%" align="right">车容量：</td>
	                <td width="35%"><input name="specification" id="specification" /></td>
	                <td width="10%" align="right">责任人：</td>
	                <td width="35%"><input name="person" id="person" /></td>
	            </tr>
	            <tr>
	                <td width="10%" align="right">车架号：</td>
	                <td width="35%"><input name="serialnumber" id="serialnumber" /></td>
	                <td width="10%" align="right">车辆照片：</td>
	                <td width="35%"><input name="photo" id="photo" /></td>
	            </tr>
	            <tr>
	                <td width="10%" align="right">车型：</td>
	                <td width="35%"><input name="arctic" id="arctic" /></td>
	                <td width="10%" align="right">车辆信息：</td>
	                <td width="35%"><input name="info" id="info" /></td>
	            </tr>
	            <tr>
	                <td width="10%" align="right">备注：</td>
	                <td colspan="2">
	                	<textarea name="notes" id="notes" style="width:95.8%;">
	                	</textarea>
	                </td>
	            </tr>
	        </table>
        </form>
    </div>
</html>