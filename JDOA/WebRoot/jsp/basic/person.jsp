<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人员管理</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
</head>
<body>
	  <div id="panel" style="width:100%;">
	     <div id="west_panel" >
		    <ul id="treeInfo"></ul>
	     </div>
		<div id="center_panel">	
		    <div id ="toolbar">
		 	 <button  id="fadd" >新增</button>
	   		 <button  id="fedit" >修改</button>
	   		 <button  id="fdelete">删除</button>
	   		 <button  id="fview" >查看</button>
   		    </div>
			<div><table id="ftab"></table></div>
		</div>
	 </div>
	 <div id="dialog-modal" title=""><iframe frameborder="0" style="width: 100%; height: 99%; height: 100% \9;" src="about:blank"></iframe></div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
	          var fheight=parent.tabElement.height()-70;
	          $('#panel').css("height",fheight);
	          $('#center_panel').css("height",fheight);
	          $('#west_panel').css("height",fheight);
	          loadDialog('60%',fheight); 
	          var coM=[  
	                    {header : '人员姓名', name : 'fname', width : 100, align : 'left'},
                        {header : '性别', name : 'fsex', width : 50, align : 'left',renderer :jxSex},
                        {header : '民族', name : 'fmz', width : 50, align : 'center'},
                        {header : '政治面貌', name : 'fzzmm', width : 100, align : 'center'},
                        {header : '籍贯', name : 'fjg', width : 100, align : 'center'},
                        {header : '任职日期', name : 'frzrq', width : 100, align : 'center'},
                        {header : '本人电话', name : 'fbrdh', width : 100, align : 'center'},
                        {header : '入党时间', name : 'frdsj', width : 100, align : 'center'},
                        {header : '学历', name : 'fxl', width : 100, align : 'center'},
                        {header : '邮箱', name : 'femail', width : 170, align : 'center'},
                        {header : '所属部门', name : 'fssbm', width : 100, align : 'center'}
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
                height : fheight-30,
                colModel : coM,
                autoFit:true,
				dataSource :"queryPerson",
				onRowDblClick:function(rowIndex,rowData,event){
                 },
				onSuccess:function(data,testStatus,XMLHttpRequest,event){
   				       
   				       }
		        });
		      $("#fadd").click(function(){
		       openDialogBox("person_add.jsp");
		      });
		       $("#fedit").click(function(){
		       var selectedRecords = $('#ftab').omGrid('getSelections',true);
		       if(selectedRecords.length<1){
		          alert("请选择纪录");
		          return false;
		       }
		       openDialogBox("person_edit.jsp");
		      });
		       $("#fdelete").click(function(){
		         var selectedRecords = $('#ftab').omGrid('getSelections',true);
		       if(selectedRecords.length<1){
		          alert("请选择纪录");
		          return false;
		       };
		           if(checkBox("确定删除选择的记录吗？")){
		                  $.ajax({
  							type: 'POST',
  							url:'delPerson', 
  							data:"fid="+selectedRecords[0].fid,
  							dataType:'text', 							
  							contentType:'application/x-www-form-urlencoded; charset=utf-8',
  							success: function(data){
  							   if(data=="ok"){
  							     alert("删除成功");
  							     $('#ftab').omGrid('reload');//刷新当前页
  							   };
  							}	
			            });
		           }
		       });
		        $("#fview").click(function(){
		          var selectedRecords = $('#ftab').omGrid('getSelections',true);
		          if(selectedRecords.length<1){
		             alert("请选择纪录");
		            return false;
		          }
		       openDialogBox("person_edit.jsp?oper=2");
		      });
              $('#toolbar').omButtonbar({});
	});
	function jxSex(value){
	  if(value==1){
	   return"男";
	  }else if(value==2){
	   return"女";
	  }else{
	   return"无";
	  }
	}
</script>
</html>