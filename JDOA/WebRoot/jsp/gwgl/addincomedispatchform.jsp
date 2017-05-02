<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>incomedispatchform1</title>
  </head>
    <script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/tool/ajaxfileupload.js"></script>
	<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
	<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
	<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
	<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
	<script type="text/javascript" src="../../js/tool/time.js"></script>	
<style>
#page_table input{
	height: 30px;
	width: 100%;
}
#page_table{
	border-collapse: separate; 
	border-spacing: 20px;
}
</style>
  <body>
    <div style="text-align: center;color: red;">
		<h1>厅高速公路监控结算中心文书处理签</h1>
	</div>
	<hr style="height:3px;border:none;border-top:3px double red;" />
	<div align="center">
	<form id="incomedispatchForm">
		<table id="page_table" >
			<tr>
				<td width="15%">收文日期:</td>
				<td width="35%">
					<input name="facceptdate" onkeypress="return false;" onfocus="SelectDate(this,'yyyy-MM-dd')" readOnly="true" style="background:url(../../image/basic/date.png) no-repeat 250px  4px;"/>
				</td>
				<td width="15%">紧急程度:</td>
				<td width="35%">
					<select name="fpriority" style="width:100%;height:35px;">
	    				<option value="0">一般</option> 
						<option value="1">紧急</option> 
						<option value="2">特急</option> 
					</select>
				</td>
			</tr>
			<tr>
				<td>收文号:</td>
				<td><input type="text" name="fincome_no" /></td>
			</tr>
			<tr>
				<td>来文单位:</td>
				<td><input type="text" name="fincome_unit" /></td>
				<td>来文编号:</td>
				<td><input type="text" name="fincome_num"/></td>
			</tr>
			<tr>
				<td>来文标题:</td>
				<td colspan="3"><input type="text" name="ftitle"/></td>
			</tr>
			<tr>
				<td>领导阅示意见:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
			<tr>
				<td>传阅意见:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
				<td>承办情况:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
			<tr>
				<td>拟办意见:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
				<td>计算中心办公室:</td>
				<td colspan="3"><textarea rows="10" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr>
				<td>归档人:</td>
				<td><input type="text" name="farchiver" id="farchiver" readOnly="true"/></td>
			</tr>
			<tr>
				<td>拟办人:</td>
				<td><input type="text" name="fhanding_person" id="fhanding_person" readOnly="true"/></td>
			</tr>
			<tr>
				<td>附件:</td>
				<td><input type="file" name="file" id="file"></td>
			</tr>
			<tr>
				<td></td>
				<td colspan="3">
					<span style="color:gray">允许.doc、.docx、.xls、.xlsx、.ppt、.pptx、.pdf、.txt、.zip、.rar格式文件上传</span>
				</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align: right;"><button type="button" onclick="addIncomeDispatchForm()">暂存</button>&nbsp;&nbsp;<button type="button" onclick="addDispatchForm()">提交</button></td>
			</tr>
		</table>
		</form>
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
  		<div align="center" style="margin-top: 10px;"><button type="button" id="but">确定</button></div>
   	</div>
  </body>
	<script type="text/javascript">
   	$(function(){
  		//--------------------------------模态框初始化------------------------------------------
			$("#dialog-modal-add").omDialog({
				title:'人员列表',
				autoOpen: false,
				height: 380,
				width: 850,
				modal: true
				});
			//-------------------------------人员表格初始化------------------------------------------
			initSendPersonGrid();
  		$('#farchiver').focus(function(){
			 $("#dialog-modal-add").omDialog('open');
			 $('#but').attr('onclick','fillThisArgs("farchiver")');
  		});
  		$('#fhanding_person').focus(function(){
			 $("#dialog-modal-add").omDialog('open');
			 $('#but').attr('onclick','fillThisArgs("fhanding_person")');
  		});
  	});
  
  	
  	//添加发文
  	function addIncomeDispatchForm(){
  		$.ajaxFileUpload( {  
		    	type:'POST',
		        url : 'addOneIncomeDispatch',		//用于文件上传的服务器端请求地址  
		        secureuri : false,          //一般设置为false  
		        fileElementId : 'file',     //文件上传空间的id属性  <input type="file" id="file" name="file" />  
		        data : $('#incomedispatchForm').serializeArray(),
		        dataType : 'text',          
		        success : function(data, status) {  
		        	data = data.substring(data.indexOf(">")+1,data.lastIndexOf("<"));
		        	if(data == 'ok'){
			        	//清空表单内容
			           	document.getElementById("incomedispatchForm").reset();
			           	//关闭模态框
			           	window.parent.closeModel();
			           	//重新加载表格
			           	window.parent.reloadgrid();
		        	}else if(data == 'error'){
		        		alert("文件格式错误!");
		        	}else{
		        		alert("系统内部错误");
		        	}
		        }  
		    }) ; 
  	}  
  		//初始化发送人员表
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
  	 	
  	 	function fillThisArgs(obj){
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
			$("#"+obj).val(personIdItems);
  	 		$("#dialog-modal-add").omDialog('close');
  	 	}
  </script>
</html>
