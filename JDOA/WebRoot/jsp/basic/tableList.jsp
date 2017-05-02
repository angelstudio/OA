<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>表格示例</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
 <link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />
<script type="text/javascript">
	$(document).ready(function() {
	        var fheight=parent.tabElement.height()-70;
	          var coM=[  
	                   {header : '菜单名称', name : 'fname', width : '150', align : 'left',wrap:true},
                       {header : '菜单等级', name : 'flevel', width : '150', align : 'left',wrap:true},
                        {header : '菜单URL', name : 'furl', width : 100, align : 'center'}
                       ];
		    $('#ftab').omGrid({
                limit:0,
                height : fheight,
                colModel : coM,
                autoFit:true,
				dataSource :"getTable",
				onRowDblClick:function(rowIndex,rowData,event){
                 },
				onSuccess:function(data,testStatus,XMLHttpRequest,event){
   				       
   				       }
		        });

	});
</script>
</head>
<body>
	<div><table id="ftab"></table></div>
</body>
</html>