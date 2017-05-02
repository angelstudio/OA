<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>dispatchform1</title>
  </head>
    <script type="text/javascript" src="../../js/tool/ueditor/third-party/jquery.min1.7.js"></script>
    <script type="text/javascript" src="../../js/tool/ajaxfileupload.js"></script>
	<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
	<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
	<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
	<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
	<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
	<link href="../../js/tool/ueditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../../js/tool/ueditor/third-party/template.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/tool/ueditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/tool/ueditor/umeditor.min.js"></script>
    <script type="text/javascript" src="../../js/tool/ueditor/lang/zh-cn/zh-cn.js"></script>
<style>
#page1_table1 input{
	width: 0px;
	margin-left: 30px;
}
#page2_table2 input{
	height: 30px;
	width: 100%;
}
#page1_table1,#page2_table2{
	border-collapse: separate; 
	border-spacing: 20px;
}
a:link {
 text-decoration: none;
}
a:hover {
 text-decoration: underline;
}
</style>
  <body>
	<form id="dispatchForm" method="POST" action="updOneDispatch" enctype="multipart/form-data">
		<div id="tabletitle" align="center"><img src=""></div>
		<table id="page1_table1" >
			<tr>
				<td width="10%">行文类型：</td>
				<td>
					<input name="ftype" type="radio" value="0" checked="checked"/>一般
					<input name="ftype" type="radio" value="1" />上行
					<input name="ftype" type="radio" value="2" />下行
				</td>
			</tr>
			<tr>
				<td>公文模板</td>
				<td>
					<input name="ftemplate" type="radio" value="1.png" checked="checked"/><img src="img/1.png">
					<input name="ftemplate" type="radio" value="2.png" /><img src="img/2.png">
				</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<input name="ftemplate" type="radio" value="3.png" /><img src="img/3.png">
					<input name="ftemplate" type="radio" value="4.png" /><img src="img/4.png">
				</td>
			</tr>
			</table>
			<table id="page2_table2">
			<tr>
				<td width="14%">发文单位:</td>
				<td width="36%">
					<select name="funit" id="funit" style="width:100%;height:35px;">
	    				<option value="湘交结">湘交结</option> 
						<option value="湘交研">湘交研</option> 
						<option value="湘智司">湘智司</option> 
						<option value="湘交结算党">湘交结算党</option> 
						<option value="湘交结算工">湘交结算工</option> 
						<option value="湘交结算纪">湘交结算纪 </option> 
					</select>
				</td>
				<td width="12%">紧急程度:</td>
				<td width="38%">
					<select name="fpriority" id="fpriority" style="width:100%;height:35px;">
	    				<option value="0">一般</option> 
						<option value="1">紧急</option> 
						<option value="2">特急</option> 
					</select>
				</td>
			</tr>
			<tr>
				<td>发文号:</td>
				<td><input type="text" name=""  disabled="disabled"/></td>
				<td>发文日期:</td>
				<td><input type="text" name="" disabled="disabled"/></td>
			</tr>
			<tr>
				<td>签发:</td>
				<td><input type="text" name="" disabled="disabled"/></td>
				<td>会签:</td>
				<td><input type="text" name="" disabled="disabled"/></td>
			</tr>
			<tr>
				<td>主送:</td>
				<td colspan="3"><input type="text" name="fmain_send" /></td>
			</tr>
			<tr>
				<td>抄送:</td>
				<td colspan="3"><input type="text" name="fcopy_send" /></td>
			</tr>
			<tr>
				<td>拟稿单位:</td>
				<td><input type="text" name="fdraftunit" value="${user.person.fssbm}" disabled="disabled"/></td>
				<td>拟稿:</td>
				<td><input type="text" name="fediter" value="${user.person.fname}" disabled="disabled"/></td>
			</tr>
			<tr>
				<td>核稿:</td>
				<td><input type="text" name="" disabled="disabled"/></td>
				<td>审稿:</td>
				<td><input type="text" name="" disabled="disabled"/></td>
			</tr>
			<tr>
				<td>打印:</td>
				<td><input type="text" name="" disabled="disabled"/></td>
				<td>份数:</td>
				<td><input type="text" name="" disabled="disabled"/></td>
			</tr>
			<tr>
				<td>标题:</td>
				<td colspan="3"><input type="text" name="ftitle" /></td>
			</tr>
			<tr>
				<td>正文:</td>
				<td colspan="3"><script type="text/plain" id="myEditor" style="width:100%;height:240px;"></script><td>
			</tr>
			<tr>
				<td width="13%">审稿或协助人:</td>
				<td colspan="3"><input type="text" name="fassist" id="fassist" readOnly="true" /></td>
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
				<td></td>
				<td id="existfile"></td>
			</tr>
			<tr>
				<td colspan="4" style="text-align: right;"><button type="button" onclick="updDispatchForm('0')">暂存</button>&nbsp;&nbsp;<button type="button" onclick="updDispatchForm('1')">提交</button></td>
			</tr>
		</table>
		<input type="hidden" name="fid" />
		<input type="hidden" name="fbody" id="fbody"/>
		<input type="hidden" name="fbody_" id="fbody_"/>
		<input type="hidden" name="autoFilepath" id="autoFilepath"/>
		<input type="hidden" name="fstatu" id="fstatu"/>
	</form>
		<div id="dialog-modal-upd">
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
  </body>
	<script type="text/javascript">
	var um;
   	$(function(){
  	 	um = UM.getEditor('myEditor');
  		//--------------------------------模态框初始化------------------------------------------
			$("#dialog-modal-upd").omDialog({
				title:'审稿或协助人',
				autoOpen: false,
				height: 380,
				width: 850,
				modal: true
				});
			//-------------------------------人员表格初始化--------------------------------------
			initSendPersonGrid();
			//-------------------------------初始化表单-----------------------------------------
			var fid = '${param.fid}';
			$.ajax({
	   			type:'POST',
				url : 'queryDispatchDetail',
				data:{fid:fid},
				dataType:'json',
				contentType:'application/x-www-form-urlencoded; charset=utf-8',
				success:function(forms){
				//设置表头
				$('#tabletitle img').attr('src','img/'+forms[21].trim());
				$("input[name='ftype']").eq(forms[3]).attr("checked", true);
				$("input[name='funit']").each(function(){
					if($(this).val() == forms[2]){
						$(this).attr('checked','true');
					}
				});
				$("input[name='ftemplate']").each(function(){
					if($(this).val() == forms[21]){
						$(this).attr('checked','true');
					}
				});
				$("#fpriority option[value="+forms[4]+"]").attr('selected','selected');
				$("input[name='fmain_send']").val(forms[9]);
				$("input[name='fcopy_send']").val(forms[10]);
				$("input[name='ftitle']").val(forms[1]);
				um.setContent(forms[14]);
				$("input[name='fassist']").val(forms[17]);
				$("input[name='ftemplate']").change(function(){
					$('#tabletitle img').attr('src','img/'+$(this).val());
				});
				$("input[name='fid']").val(forms[0]);
				var linkStr = "已上传的附件:";
				for(var i = 0; i < forms[23].length; i++){
					if(forms[23][i].fremark == 0){
						linkStr += "<li>"+forms[23][i].ffiletitle+"</li>";
						$('#autoFilepath').val(forms[23][i].ffileid);
						continue;
					}
					linkStr += "<li>"+forms[23][i].ffiletitle+"&nbsp;&nbsp;<a href=\"javascript:deleteDispatchFile('"+forms[0]+"','"+forms[23][i].ffileid+"','"+forms[23][i].ffiletitle+"')\">移除</a></li>";
				}
				$('#existfile').html(linkStr);	
					}
	   			});
			
			//------------------------------初始化表单结束---------------------------------------
  		$('#fassist').focus(function(){
			 $("#dialog-modal-upd").omDialog('open');
  		});
  	});
  	//删除发文中的文件
  	function deleteDispatchFile(fid,ffileid,fname){
  		$.omMessageBox.confirm({
			title:'删除确认！',
			content:'删除后将不可恢复，确定要删除吗？',
			onClose:function(v){
			if(v){
				$.post('delOneDispatchFile',{operation:'delete',fid:fid,ffileid:ffileid,ffileName:fname},function(data){
				var forms = eval("("+data+")");
				var linkStr = "已上传的附件:";
				for(var i = 0; i < forms.length; i++){
					if(forms[i].fremark == 0){
						linkStr += "<li>"+forms[i].ffiletitle+"</li>";
						$('#autoFilepath').val(forms[i].ffileid);
						continue;
					}
					linkStr += "<li>"+forms[i].ffiletitle+"&nbsp;&nbsp;<a href=\"javascript:deleteDispatchFile('"+forms[i].fid+"','"+forms[i].ffileid+"','"+forms[i].ffiletitle+"')\">移除</a></li>";
				}
				$('#existfile').html(linkStr);
				$.omMessageTip.show({title: "操作成功", content: "删除数据成功", timeout: 1500});
				}
				);
			}else{
			}
			}
		});
  	}
  	//更新发文
  	function updDispatchForm(statu){
  		$('#fbody_').val(um.getPlainTxt());//不带格式的文本
  		var temp = um.getContent();
  		var endTemp = temp.replace(/&nbsp/g,"nbsp");//解决ajaxFileUpload不能传递包含特殊字符的文本缺陷
  		$('#fbody').val(endTemp);
  		$('#fstatu').val(statu);
  		$.ajaxFileUpload( {  
		    	type:'POST',
		        url : 'updOneDispatch',		//用于文件上传的服务器端请求地址  
		        secureuri : false,          //一般设置为false  
		        fileElementId : 'file',     //文件上传空间的id属性  <input type="file" id="file" name="file" />  
		        data : $('#dispatchForm').serializeArray(),
		        dataType : 'text',          
		        success : function(data, status) {  
		        	data = data.substring(data.indexOf(">")+1,data.lastIndexOf("<"));
		        	if(data == 'ok'){
			        	//清空表单内容
			           	document.getElementById("dispatchForm").reset();
			           	//关闭模态框
			           	window.parent.closefawenModeDialog();
			           	//重新加载表格
			           	window.parent.reloadGrid();
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
  	 	
  	 	//填充表单'fassist'字段
  	 	function fillThisArgs(){
  	 		var selectedRecords = $('#ftab').omGrid('getSelections',true);
  	 		var personIdItems = '';
			if(selectedRecords == ""){
				alert('请选择接收人员！');
				return;
			}
			for(var i = 0; i < selectedRecords.length; i++){
					personIdItems+=selectedRecords[i].fname+',';
				}
			personIdItems = personIdItems.substring(0, personIdItems.length-1);
			$('#fassist').val(personIdItems);
  	 		$("#dialog-modal-upd").omDialog('close');
  	 	}
  </script>
</html>
