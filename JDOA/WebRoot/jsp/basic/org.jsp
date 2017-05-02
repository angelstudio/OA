<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	    <script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
        <script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
	    <script type="text/javascript" src="../../js/tool/common.js"></script>
	    <link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
         <link rel="stylesheet" type="text/css" href="../../css/tool/ui/manage.css" />
	</head>
	<body>
    <script type="text/javascript">
          var oper=0;//定义全局操作变量，默认为0,1新增，2修改,3删除。
		  $(document).ready(function() {
		        $("#mytree").omTree({
                dataSource : 'queryAllOrg',
                simpleDataModel: true,
                onSelect: function(node){
                	document.getElementById('nodeInfoForm').style.visibility='visible';
                	document.getElementById('buttons').style.visibility='hidden';
	   				$("#fname").attr("disabled",true);
	   				$("#fseq").attr("disabled",true);
		   			$('#fname').val(node.text);
		   			$('#fseq').val(node.fseq);
               }
            });
		     $("#ftj").click(function(){
		          var fid=$("#fid").val();
		          var fname=$("#fname").val();
		          var fparentId=$("#fparentId").val();
		          var flevel=$("#flevel").val();
		          var pflongNumber=$("#pflongNumber").val();
		          var fseq=$("#fseq").val();
		          var params="";
		          var actionUrl="";
		          if(oper==1){
		            actionUrl="addOrg";
		            params="fid="+fid+"&fname="+fname+"&fparentId="+fparentId+"&flevel="
		          +flevel+"&pflongNumber="+pflongNumber+"&fseq="+fseq;
		          }else if(oper==2){
		             actionUrl="editOrg";
		             params="fid="+fid+"&fname="+fname+"&fseq="+fseq;
		          }else if(oper==3){
		             actionUrl="delOrg";
		             params="fid="+fid;
		          }
		          $.ajax({
						type : 'POST',
						url : actionUrl,
						data:params,
						dataType : 'text',
						contentType : 'application/x-www-form-urlencoded; charset=utf-8',
						success : function(request) {
					         if(request=="ok"){
					            alert("操作成功");
					         }else{
					            alert("操作失败");
					         };
					         $('#mytree').omTree('refresh');
					         document.getElementById('nodeInfoForm').style.visibility='hidden';
					         document.getElementById('buttons').style.visibility='hidden';
						}
					});
		     });
             var fseq = $("#fbill").validate({
                rules : {
                    fseq : "number"
                   },
                messages : {
                    fseq : {
                        number : "请输入数字"
                    }
                }
            });
            $("#freset").click(function(){
                $("#fname").val("");
	   		    $("#fseq").val(999);
            });
        });
        //组织新增
        function addOrg(){
                oper=1;
                var node = $("#mytree").omTree("getSelected");
	   			document.getElementById('nodeInfoForm').style.visibility='visible';
	   			document.getElementById('buttons').style.visibility='visible';
	   			$("#fname").attr("disabled",false);
	   		    $("#fseq").attr("disabled",false);
	   		    $("#fname").val("");
	   		    $("#fseq").val(999);
				var fparentid="000000";
			    var pflevel=0;
				var pflongNumber="000000";
		   		if(node!=null){
		   		  fparentid=node.id;
		   		  pflevel=node.flevel;
		   		  pflongNumber=node.flongNumber;
		   	    }
		   		$('#fparentId').val(fparentid);
		   	    $('#flevel').val(pflevel+1);
		   		$('#pflongNumber').val(pflongNumber);
        };
         //组织修改
        function editOrg(){
                oper=2;
                var node = $("#mytree").omTree("getSelected");
                if(!node){
                   alert("请选择菜单");
                   return;
                }
	   			document.getElementById('nodeInfoForm').style.visibility='visible';
	   			document.getElementById('buttons').style.visibility='visible';
	   			$("#fname").attr("disabled",false);
	   		    $("#fseq").attr("disabled",false);
	   		    $("#fid").val(node.id);
        };
        //组织删除
        function delOrg(){
             oper=3;
             var node = $("#mytree").omTree("getSelected");
                if(!node){
                   alert("请选择菜单");
                   return;
                }
            $("#mytree").omTree("remove", node);
            document.getElementById('nodeInfoForm').style.visibility='visible';
	   	    document.getElementById('buttons').style.visibility='visible';
            $("#fid").val(node.id);
        };
    </script>
    
    <table  align="center">
       <tr>
          <td style="color: blue;" width="200">组织架构:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          	 <a href="javascript:void(0)" style="color: red;"  onclick="addOrg()" >新增</a>|
       	  	 <a href="javascript:void(0)" style="color: red;"  onclick="editOrg()" >修改</a>|
        	 <a href="javascript:void(0)" style="color: red;"  onclick="delOrg()" >删除</a>
          </td>
       	<td ></td>
       </tr>
       <tr valign="top">
          <td width="200"><ul id="mytree"></ul></td>
       	<td>
       		<div id="nodeInfoForm" style="visibility: hidden" align="center">
       		<form id="fbill">
	   	      <table class="grid_layout">
	            <tr>
	                <td ><input id="fid" name="fid"  readonly ="readonly" type="hidden" /></td>
	            </tr>
	            <tr>
	                <td ><input id="fparentId" name="fparentId" readonly ="readonly" type="hidden" /></td>
	            </tr>
	            <tr>
	                <td ><input id="flevel" name="flevel" readonly ="readonly" type="hidden" /></td>
	            </tr>
	            <tr>
	                <td ><input id="flongNumber" name="flongNumber" readonly ="readonly"  type="hidden" /></td>
	            </tr>
	            <tr>
	                <td ><input id="pflongNumber" name="pflongNumber" readonly ="readonly"  type="hidden" /></td>
	            </tr>
	            <tr>
	                <td>名称：</td>
	                <td ><input id= "fname" name="fname" /></td>
	            </tr>
	            <tr>
	                <td>排序：</td>
	                <td ><input id= "fseq" name="fseq" /></td>
	            </tr>
	            <tr>
	            	<td></td>
	            	<td>
	            		
	            	</td>
	            </tr>
	        </table>
	        </form>
	        <div id="buttons" style="visibility: hidden">
	            			<button id="ftj"  >提交</button> 
	           	 			<button id="freset" >重置</button>
	            		</div>
	        <div class="separator" ></div><br>
	  </div>
       	</td>
       </tr>
    </table>
	</BODY>
</html>