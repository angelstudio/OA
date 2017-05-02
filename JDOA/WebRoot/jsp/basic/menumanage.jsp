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
	    <style type="text/css">
	       label.error{
	        background: #fff6bf url(images/alert.png) center no-repeat;
			background-position: 5px 50%;
			text-align: left;
			padding: 2px 20px 2px 25px;
			border: 1px solid #ffd324;
			display: none;
			width: 200px;
			margin-left: 10px;
	       };
	       html, body{ width: 100%; height: 100%; padding: 0; margin: 0;}
    		#center-tab .om-panel-body{
    		padding: 0;
    	}
    	</style>
	</head>
	<body>
    <script type="text/javascript">
          var oper=0;//定义全局操作变量，默认为0,1新增，2修改,3删除。
		  $(document).ready(function() {
		        $("#mytree").omTree({
                dataSource : 'queryAllMenu',
                simpleDataModel: true,
                onSelect: function(node){
                	document.getElementById('nodeInfoForm').style.visibility='visible';
                	document.getElementById('buttons').style.visibility='hidden';
	   				$("#fname").attr("disabled",true);
	   				$("#furl").attr("disabled",true);
	   				$("#fseq").attr("disabled",true);
		   			$('#fname').val(node.text);
		   			$('#furl').val(node.furl);
		   			$('#fseq').val(node.fseq);
               }
            });
		     $("#ftj").click(function(){
		          var fid=$("#fid").val();
		          var fname=$("#fname").val();
		          var fparentId=$("#fparentId").val();
		          var flevel=$("#flevel").val();
		          var furl=$("#furl").val();
		          var pflongNumber=$("#pflongNumber").val();
		          var fseq=$("#fseq").val();
		          var params="fid="+fid+"&fname="+fname+"&fparentId="+fparentId+"&flevel="
		          +flevel+"&furl="+furl+"&pflongNumber="+pflongNumber+"&fseq="+fseq+"&oper="+oper;
		          $.ajax({
						type : 'POST',
						url : 'operMenu',
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
	   		    $("#furl").val("");
	   		    $("#fseq").val(999);
            });
        });
        //菜单新增
        function addMenu(){
                oper=1;
                var node = $("#mytree").omTree("getSelected");
	   			document.getElementById('nodeInfoForm').style.visibility='visible';
	   			document.getElementById('buttons').style.visibility='visible';
	   			$("#fname").attr("disabled",false);
	   		    $("#furl").attr("disabled",false);
	   		    $("#fseq").attr("disabled",false);
	   		    $("#fname").val("");
	   		    $("#furl").val("");
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
         //菜单修改
        function editMenu(){
                oper=2;
                var node = $("#mytree").omTree("getSelected");
                if(!node){
                   alert("请选择菜单");
                   return;
                }
	   			document.getElementById('nodeInfoForm').style.visibility='visible';
	   			document.getElementById('buttons').style.visibility='visible';
	   			$("#fname").attr("disabled",false);
	   		    $("#furl").attr("disabled",false);
	   		    $("#fseq").attr("disabled",false);
	   		    $("#fid").val(node.id);
        };
        //菜单删除
        function delMenu(){
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
          <td style="color: blue;" width="200">菜单:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          	 <a href="javascript:void(0)" style="color: red;"  onclick="addMenu()" >新增</a>|
       	  	 <a href="javascript:void(0)" style="color: red;"  onclick="editMenu()" >修改</a>|
        	 <a href="javascript:void(0)" style="color: red;"  onclick="delMenu()" >删除</a>
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
	                <td>url：</td>
	                <td ><input id= "furl" name="furl" /></td>
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