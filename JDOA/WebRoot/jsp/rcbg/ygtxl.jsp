<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>员工通信录</title>
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
		
			<div>
			<div id="fcommonquery">
                        姓名:<span ><input id="fname" style="height:18px;"/></span>
          	<button id="fpquery" onclick="loadGrid()">查询</button>
       </div>
			<table id="ftab"></table>
			</div>
		</div>
	 </div>
	 <div id="dialog-modal" title=""><iframe frameborder="0" style="width: 100%; height: 99%; height: 100% \9;" src="about:blank"></iframe></div>
</body>
<script type="text/javascript">

   
   function loadGrid(){
   var fheight=parent.tabElement.height()-70;
    var fname =$("#fname").val(); 
	          $('#panel').css("height",fheight);
	          $('#center_panel').css("height",fheight);
	          $('#west_panel').css("height",fheight);
	          loadDialog('60%',fheight); 
           var coM=[  
	                         {header : '姓名', name : 'fname', width : 80, align : 'left'},
                             {header : '性别', name : 'fsex', width : 80, align : 'left',renderer :jxSex}, 
                             {header : '部门', name : 'fssbm', width : 80, align : 'left'}, 
                             {header : '职位', name : 'fzw', width : 80, align : 'left'}, 
                             {header : '手机号', name : 'fbrdh', width : 80, align : 'left'}, 
                             {header : '座机电话', name : 'fzj', width : 80, align : 'left'}, 
                             {header : '邮箱', name : 'femail', width : 120, align : 'left'},
                             {header : '生日', name : 'fcsrq', width : 80, align : 'left'},
                             {header : '工号', name : 'fgh', width : 80, align : 'left'},
                       ];
                       
                $('#ftab').omGrid({
                limit:100,
                height : fheight-77,
                colModel : coM,
                autoFit:true,
				dataSource :"queryEmployee?fname="+fname,
				onRowDblClick:function(rowIndex,rowData,event){
                 },
				onSuccess:function(data,testStatus,XMLHttpRequest,event){
   				       }
		        });    
  
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