if(window.top!==self){
	window.top.location.href=self.location.href;
};
function userLogin() {
	var fuserName=$("#fusername").val();
	var fpassword=$("#fpassword").val();
	if(fuserName=="") {
		alert("用户名称不能为空");
		$("#fusername").focus();
		return;
	}else if(fuserName.search("[\f\n\r\t\v]")!=-1||fuserName.indexOf(" ")!=-1){
		alert("用户名中不能有空白字符或空格");
		$("#fusername").focus();
		return;
	}else{
	
	}
	var fpassword=$("#fpassword").val();
	if(fpassword==""||fpassword=="null"){
		 alert("密码不能为空");
		 $("#fpassword").focus();
		return;
	 }else if(fpassword.length<6||fpassword.length>16){
	 	alert("密码长度必需在6-16之间");
	 	$("#fpassword").focus();
		return;
	}else if(fpassword.search("[\f\n\r\t\v]")!=-1||fpassword.indexOf(" ")!=-1){
	     alert("密码中不能有空白字符或空格");
	     $("#fpassword").focus();
		return;
	 }	
	$("#userForm").attr("action","userLogin"); 
    $("#userForm").submit();
}