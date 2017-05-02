<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户登陆</title>
<link rel="stylesheet" href="css/login/longin.css" />
<script type="text/javascript" src="js/tool/jquery.min.js"></script>
<script type="text/javascript" src="js/tool/jquery-1.12.0.min.js"></script>
<script type="text/javascript" src="js/login/login.js"></script>
</head>
 <body >
 	
	<!--头部-->
	<header class="head">
		<div class="logo">
			
			
		</div>
		<ul class="head-by">
				<li>XXXX</li> 
				<li>XXXX</li> 
				<li>XXXX</li>  
				<li>XXXX</li>
			</ul>
	</header>
	
	<!--中间-->
	<section class="sectionA">
		<!--登录框-->
		<div class="login-stn">
			<div class="login-lt">
				<img src="image/login/img1.jpg" class="lt_imgst"/>
				<img src="image/login/img2.jpg" class="lt_imgst"/>
				<img src="images/login/img3.jpg" class="lt_imgst"/>
			</div>
			<div class="login-bdr"></div>
			<div class="login-rt">
				<form  name="userForm" id="userForm" action="" method="post" target="_top">
					<h2>用户登录</h2>
					<div class="col-3">
						<input  id="fusername" class="put" type="text" name="fusername" placeholder="" >
						<label>请输入账户名</label>
						<span class="focus-border">
		            		<i></i>
		            	</span>
					</div>
					<div class="col-3">
						<input id="fpassword" class="put" type="password"  name="fpassword" placeholder="">
						<label>请输入密码</label>
						<span class="focus-border">
		            		<i></i>
		            	</span>
					</div>
					 <div >
		                <font color="red"> &nbsp;
		                 <%
                         if (null != request.getAttribute("error")) {
                           %><%=request.getAttribute("error")%>
		                  <%
		                   System.out.println(request.getAttribute("error"));
			               request.getSession().removeAttribute("error");
		                   %> <%
                          }
                         %>
        </font>
		</div>
				</form>
				<div class="col-3">
						<button id="fdl" class="subtn" onclick="userLogin()">登录</button>
			  </div>
			</div>
		</div>
	</section>
	
	<footer class="foot">
		<div class="foot-tp">
			<a>主办单位：XXXX</a>
			<font>承办单位：XXXX</font>
		</div>
		<div class="foot-bt">
				<font>技术支持：XXXX</font>
		</div>
	</footer>

</body>
<script type="text/javascript">
	lt_imgst();
$(window).load(function(){
	$(".col-3 input").val("");
	$(".col-3 input").focusout(function(){
		if($(this).val() != ""){
			$(this).addClass("has-content");
		}else{
			$(this).removeClass("has-content");
		}
	})
});


function lt_imgst(){
	$(".lt_imgst").eq(0).show();
	$(".lt_imgst").not(":eq(0)").hide();
	var sltIndex = 0;
	setInterval(
		function(){
		var indeximg = $(".lt_imgst").length;
		if(sltIndex == indeximg){
			sltIndex = 0
		}
		SwHe();
		sltIndex++;
	},10000);
	function SwHe(){
		$(".lt_imgst").eq(sltIndex).fadeIn(4000);
		$(".lt_imgst").not(":eq("+sltIndex+")").fadeOut(1000);
	}
	
}
	</script>
</html>
