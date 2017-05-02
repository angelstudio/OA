<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>默认主页</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<%-- <script type="text/javascript" src="../../js/urlUtil.js"></script> --%>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<link rel="stylesheet" type="text/css"
	href="../../css/tool/ui/default/om-default.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/manage.css" />
<link rel="stylesheet" href="../../css/tool/ui/main.css" type="text/css"></link>
<!-- <link href="../css/style.css" rel="stylesheet" type="text/css" />
<link href="../css/Icon.css" rel="stylesheet" type="text/css" /> -->
<style type="text/css">
td {
	height: 170px;
	width: 314px;
	background: url("../images/navigation/beijing01.jpg") repeat-y;
}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		var menuid = request("menuid");
		$.ajax({
			type : 'POST',
			url : 'loadingAnalysis?menuid=' + menuid,
			dataType : 'text',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(data) {
				document.getElementById("navigationDiv").innerHTML = data;
				_loadUIDraw();
			}
		});

		// 		$('#navigationDiv').omPanel({
		// 			title : '测试',
		// 			collapsed : false,
		// 			collapsible : true
		// 		});

	});

	//鼠标点击事件处理方法
	function clickEvent(id, furl, faliasname) {
		openTabs(id, furl, faliasname);
	}
	//鼠标覆盖事件处理方法
	function mouseOverEvent(id) {
		document.getElementById(id).style.backgroundImage = "url('../images/navigation/beijing02.jpg')";
	}
	//鼠标移开事件处理方法
	function mouseOutEvent(id) {
		document.getElementById(id).style.backgroundImage = "url('../images/navigation/beijing01.jpg')";
	}
	//打开新页签
	function openTabs(name, url, aliasname) {
		 if(url==""||url=='null'){
	      return false;
	    }else{
		openNewTab(aliasname,url);
		}
	}

	function _loadUIDraw() {
		// 分组渲染
		var divs = $('#navigationDiv > div');
		for ( var i = 0; i < divs.length; i++) {
			var id = divs[i].id;
			var title = divs[i].getAttribute("title");
			var visible = divs[i].getAttribute("visible");
			var open = false;
			if (visible == 0) {
				open = true;
			}
			$("#" + id).omPanel({
				title : title,
				collapsed : open,
				collapsible : true
			});
		}
	}
</script>
</head>
<body>
	<center>
		<div id="navigationDiv"></div>
	</center>
	<div id="dialog-modal">
		<iframe frameborder="0" id="message"
			style="width:100%;height:99%;height:100%\9;" src="about:blank"></iframe>
	</div>
</body>
</html>