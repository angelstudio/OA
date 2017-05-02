<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>我的资产</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />

<script type="text/javascript">
	function loadGrid(){
	        var fassetsn =$("#fassetsn").val();
	        var fassetname =$("#fassetname").val();
	        
			//------------------------------grid基本信息开始------------------------------------
	        var fheight=parent.tabElement.height()-110;
	        var coM=[  
                   	  {header : '资产序号', name : 'fassetsn', width : 100, align : 'left'},
                             {header : '资产名称', name : 'fassetname', width : 100, align : 'left'}, 
                             {header : '型号与规格', name : 'fsepcification', width : 120, align : 'left'}, 
                             {header : '制造商', name : 'fmaker', width : 100, align : 'left'}, 
                             {header : '领用数量', name : 'fnumber', width : 80, align : 'left'}, 
                             {header : '单价', name : 'funitprice', width : 80, align : 'left'}, 
                             {header : '创建人', name : 'fcost', width : 80, align : 'left'},
                             {header : '创建时间', name : 'fcreatedate', width : 80, align : 'left'},
                             {header : '备注', name : 'fnotes', width : 120, align : 'left'}
                      
                    ];
		    $('#grid').omGrid({
                limit:10,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"myProperty?fassetsn="+fassetsn+"&fassetname="+fassetname,
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
			            collapsed : false,//组件创建后为收起状态
			            collapsible : true,//渲染收起与展开按钮
			            closable : false //渲染关闭按钮
		    });
		 });
</script>
</head>
<body>
       <div id="fcommonquery">
                 资产序号:<span ><input id="fassetsn" style="height:18px;"/></span>
                 资产名称:<span ><input id="fassetname" style="height:18px;"/></span>
          	<button onclick="loadGrid()">查询</button>
       </div>
       <div id="buttonbar"></div>
       <div>
       <table id="grid"></table>
       </div>
</html>