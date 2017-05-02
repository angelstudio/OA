<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人员调动</title>
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
	        var fusername =$("#qfusername").val();
	        var fbmid =$("#qfbmid").val();
	        
			//------------------------------grid基本信息开始------------------------------------
	        var fheight=parent.tabElement.height()-150;
	        var coM=[  
                   	  {header:'调离人', name : 'fusername', width : '100', align : 'center'},
                   	  {header:'性别', name : 'fsex', width : '100', align : 'center'},
                      {header:'申请人', name : 'fapplicant', width : '100', align : 'center'},
                      {header:'申请部门', name : 'fdepartment', width : 100, align : 'center'},
                      {header:'当前职位', name : 'fnowposition', width : 100, align : 'center'},
                      {header:'调动部门', name : 'fbmmc', width : 100, align : 'center'},
                      {header:'调动职位', name : 'ftransfer', width : 100, align : 'center'},
                      {header:'申请时间', name : 'fapplydate', width : 100, align : 'center'},
                      {header:'状态', name : 'faudit', width : 100, align : 'center',renderer:getstatus}
                      ];
		    $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"RedeployList?fusername="+fusername+"&fbmid="+fbmid,
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
		     $.ajax({
  							type: 'POST',
  							url:'getOrg', 
  							dataType:'json', 							
  							contentType:'application/x-www-form-urlencoded; charset=utf-8',
  							success: function(data){
  							   for( var i in data){
  							     $("#fbmid").append('<option value="'+i+'">'+data[i]+'</option>');
  							   }
  							}	
			            }); 
			   $.ajax({
  							type: 'POST',
  							url:'getOrg', 
  							dataType:'json', 							
  							contentType:'application/x-www-form-urlencoded; charset=utf-8',
  							success: function(data){
  							   for( var i in data){
  							     $("#qfbmid").append('<option value="'+i+'">'+data[i]+'</option>');
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
            	 		 	showDialog('新增人员调动');
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
                			showDialog('修改人员调动',data);//显示dialog
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
									$.post('deleteRedeploy',{operation:'delete',fid:id},function(){
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
                 var d = new Date();  
                var datestr =  d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	            $("#fapplydate").val(datestr);
                $("input[name='fid']",dialog).val(rowData.fid);
                $("input[name='fusername']",dialog).val(rowData.fusername);
                $("input[name='fexpression']",dialog).val(rowData.fexpression);
                $("input[name='ftransfer']",dialog).val(rowData.ftransfer);
                $("input[name='ftransfercause']",dialog).val(rowData.ftransfercause);
                $("select[name='fbmid']",dialog).val(rowData.fbmid);
                $("input[name='fnowposition']",dialog).val(rowData.fnowposition);
                $("select[name='fsex']",dialog).val(rowData.fsex);
                
                if(rowData.fapplicant!=null){
                $("input[name='fapplicant']",dialog).val(rowData.fapplicant);
                $("input[name='fapplydate']",dialog).val(rowData.fapplydate);
                $("input[name='fdepartment']",dialog).val(rowData.fdepartment);
                }
                
                dialog.omDialog("option", "title", title);
                dialog.omDialog("open");//显示dialog
            };
            //dialog中点提交按钮时将数据提交到后台并执行相应的add或modify操作
            var submitDialog = function(){
            
	                    var operation = isAdd?'add':'modify';
	                    
	                    $.post('addOrEditRedeploy',$("#ipForm").serialize()+"&operation="+operation,function(){
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
                 调动人:<span ><input id="qfusername" style="height:18px;"/></span>
                 调动部门:<span ><select name="qfbmid" id="qfbmid"class="sSelect"><option>请选择</option></select></span>
          	<button onclick="loadGrid()">查询</button>
       </div>
       <div id="buttonbar"></div>
       <div>
       <table id="grid"></table>
       </div>
       <div id="dialog-form">
        <form id="ipForm">
	        <input name="fid" style="display: none"/>
	        <table width="100%" >
	           <tr>
        	        <td width="12%" >申请人:</td>
        	        <td width="23%" ><input type="text" class="sText" disabled="disabled" id="fapplicant"  name="fapplicant"   value="${user.person.fname}" /></td>
        	        <td width="12%" >申请部门:</td>
        		    <td width="23%" ><input type="text" class="sText" disabled="disabled" id="fdepartment" name="fdepartment"  value="${user.person.fssbm}"/></td>
        		    <td width="12%" >申请时间:</td>
        		    <td width="23%" ><input type="text" class="sText" disabled="disabled" id="fapplydate" name="fapplydate"  /></td>
        	    </tr>
	            <tr>
	                <td >调动人：</td>
	                <td ><input name="fusername" id="fusername" class="sText" /></td>
	                <td >性别：</td>
	                <td ><select name="fsex" id="fsex"  class="sSelect"><option>请选择</option> <option value="男">男</option><option value="女">女</option></select></td>
	                <td >当前职位：</td>
	                <td ><input name="fnowposition" id="fnowposition" class="sText"/></td>
	            </tr>
	            <tr>
	                <td  width="12%" >在职期间表现：</td>
	                <td colspan="5"><input name="fexpression" id="fexpression" class="sText" style="width: 99.5%;"/></td>
	            </tr>
	            <tr>
	                <td >调动职位：</td>
	                <td ><input name="ftransfer" id="ftransfer"class="sText" /></td>
	                <td >调动部门：</td>
	                <td ><select name="fbmid" id="fbmid"class="sSelect"><option>请选择</option></select></td>
	            </tr>
	            <tr>
	                <td >调动原由：</td>
	                <td  colspan="5"><input name="ftransfercause" id="ftransfercause" class="sText" style="width: 99.5%;"/></td>
	            </tr>
	        </table>
        </form>
    </div>
</html>