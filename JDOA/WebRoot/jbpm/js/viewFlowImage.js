

function viewFlowImage(fid,ftype){
	$.ajax({
		type : 'POST',
		url : 'findExecutionId',
		data : "fid="+fid+"&ftype="+ftype,
		dataType : 'json',
		contentType : 'application/x-www-form-urlencoded; charset=utf-8',
		success : function(request) {
			var fcurrentinstanceprocessid=request.fcurrentinstanceprocessid;
			var dbid=request.dbid;
			if(fcurrentinstanceprocessid.length<=0){
				alert("该单据没有走工作流");
				return false;
			}else{
				var id=dbid;
				var fexecutionId=fcurrentinstanceprocessid;
				checkOutProcess(id,fexecutionId);
			}
		}
	});
}

//查看流程，实现跳转
function checkOutProcess(id,fexecutionId){
	   var curWwwPath = window.document.location.href;
       var pathName = window.document.location.pathname;
       var pos = curWwwPath.indexOf(pathName);
       var localhostPath = curWwwPath.substring(0, pos);
       var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
       var url=localhostPath+projectName;
       var userAgent = navigator.userAgent;
       if( navigator.appName == "Microsoft Internet Explorer"||!!window.ActiveXObject || "ActiveXObject" in window){
		  window.open(url+'/jbpm/jsp/myindex.html?id=' +id+"&fexecutionId="+fexecutionId,'查看流程图');
	    }else{
		  window.open(url+'/jbpm/jsp/specialCheck.html?id='+id+"&fexecutionId="+fexecutionId,'查看流程图');
	    }
}