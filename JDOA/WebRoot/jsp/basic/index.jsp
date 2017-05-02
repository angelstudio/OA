<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>OA系统</title>
<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
<script type="text/javascript" src="../../js/tool/common.js"></script>
<script type="text/javascript" src="../../js/tool/dialogbox.js"></script>
<!-- 
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/manage.css" />
<link rel="stylesheet" href="../../css/tool/ui/main.css" type="text/css"></link>
 -->
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
<link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default_old.css" />
<link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	padding: 0;
	margin: 0;
}

#center-tab .om-panel-body {
	padding: 0;
}
</style>
<style>
body {
	margin: 0px;
	padding: 0px
}

.header {
	background: url(../../image/index/bj1.jpg) repeat-x;
	height: 50px;
	min-width: 930px
}

.header .logo {
	float: left;
	padding: 0px;
	height: 30px
}

.header .logo img {
	margin: 0px auto;
	border: 0;
	height: 65px
}

.header .account {
	float: right;
	padding: 6px;
	margin-right: 20px
}
#top-panel{
	overflow:hidden;
}
</style>
</head>
<BODY>
	<script type="text/javascript">
		var tabElement;
		var ifh;
		var fmainUrl;
		var blinkBool = false;
		var menuFlag = false;
		$(document).ready(
				function() {
					loadDialogBox();
					$('body').omBorderLayout({
						panels : [ {
							id : "top-panel",
							header : false,
							region : "north",
							height : 85
						}, {
							id : "center-panel",
							header : false,
							region : "center"
						}, {
							id : "west-panel",
							resizable : true,
							collapsible : true,
							title : "OA业务流程系统",
							region : "west",
							width : 220
						} ]
					});
					tabElement = $('#center-tab').omTabs({
						height : "fit",
						closable : false,
						tabMenu : true,
						active : 0,
						onClose : function(n, event) {
							var total = $('#center-tab').omTabs('getLength');
							if (total <= 0) {
								initDefaultFrame();
							}
						},
						onCloseAll : function(event) {
							initDefaultFrame();
						}
					});
					loadTree();
					ifh = tabElement.height()
							- tabElement.find(".om-tabs-headers").outerHeight()
							- 4; //为了照顾apusic皮肤，apusic没有2px的padding，只有边框，所以多减去2px
					loadMain();
					getMessageInfo();
					Setlinkblink();
					$('body').omBorderLayout('collapseRegion', 'west');//设置panel隐藏
					$('#main').height(ifh);
				});
		//初始化第一个页签
		function initDefaultFrame() {
			/** 关闭所有页签以后不能重新打开bug  add by qingfeng_li 2014-1-2**/
			var idx = Math.round(Math.random() * 10000);
			var tab = document.getElementById('center-tab');
			var divs = tab.getElementsByTagName("div");
			/** 追加首页frame（匹配div的innerHTML） **/
			divs[1].innerHTML = '<div class="om-widget om-panel"><div class="om-panel-body om-widget-content om-panel-noheader om-state-nobd" id="tab1" style="overflow: hidden;">'
					+ '<iframe style="height: 598px;" id="main" border="0" src="'
					+ fmainUrl
					+ '" frameborder="no" width="100%"></iframe></div></div>';

			tabElement
					.omTabs(
							'add',
							{
								title : '主页',
								tabId : 'tab_main',
								content : "<iframe id='"
										+ idx
										+ "' border=0 frameBorder='no' name='inner-frame' src='"
										+ fmainUrl + "' height='" + ifh
										+ "' width='100%'></iframe>",
								closable : true
							});
			$('#center-tab').omTabs({});
		}
		function loadTree() {
			$("#myTree")
					.omTree(
							{
								dataSource : 'findCacheMenu',
								simpleDataModel : true,
								onSelect : function(nodeData) {
									
								},
								onClick : showLink,
								onDblClick : function(nodeData, event) {
									return;
								},
								onSuccess : function(data, testStatus,
										XMLHttpRequest, event) {
									$("#myTree")
											.find(".listIcon")
											.each(
													function(index, domEle) {
														if ($(domEle)
																.attr("class")
																.indexOf(
																		'ui-icon-') >= 0) {
															var iconName = $(
																	domEle)
																	.attr(
																			"class")
																	.split(' ');
															for ( var i = 0; i < iconName.length; i++) {
																if (iconName[i]
																		.indexOf("ui-icon-") >= 0) {
																	$(domEle)
																			.prepend(
																					"<span class='ui-icon " + iconName[i] + "'></span>");
																	$(domEle)
																			.removeClass(
																					"ui-icon "
																							+ iconName[i]);
																	break;
																}
															}
														}
													});
								}
							});

		};

		var menuManage = {
			id : 10000,
			text : "菜单管理",
			fisleaf : 1,
			furl : 'menumanage.jsp'
		};
		function showLink(nodeData, event) {
			if (nodeData == null) {
				nodeData = menuManage;
			}
			var furl= nodeData.furl;
			if (!!furl) {
				//如果页签已打开则激活页签，否则新增页签
				var tabId = tabElement.omTabs('getAlter', 'tab_' + nodeData.id);
				if (tabId) {
					tabElement.omTabs('activate', tabId);
				} else {
					if (nodeData.furl != null && nodeData.furl != "") {
						var url = nodeData.furl;
						if (nodeData.furl.indexOf("?") == -1) {
							url += "?";
						} else {
							url += "&";
						}
						url += "title=" + escape(nodeData.text) + "&menuid="
								+ escape(nodeData.fid);//新增界面别名参数，以便在界面显示  add by qingfeng_li 2013-12-25
						//var idx = Math.round(Math.random()*10000);
						tabElement
								.omTabs(
										'add',
										{
											title : nodeData.text,
											tabId : 'tab_' + nodeData.id,
											//tabId : idx,
											content : "<iframe id='"
													+ nodeData.id
													+ "' border=0 frameBorder='no' name='inner-frame' src='"
													+ url
													+ "' height='"
													+ ifh
													+ "' width='100%'></iframe>",
											closable : true
										});
					}
				}
			} else {
				if (nodeData.expanded) {
					$('#myTree').omTree('collapse', nodeData);
					nodeData.expanded = false;
				} else {
					$('#myTree').omTree('expand', nodeData);
					nodeData.expanded = true;
				}
			}
		};
		function loadMain() {
			     $
					.ajax({
						type : 'POST',
						url : 'getMainUrl',
						dataType : 'json',
						contentType : 'application/x-www-form-urlencoded; charset=utf-8',
						success : function(request) {
							fmainUrl = request.fmainUrl;
							if (!!fmainUrl) {
								var mainIf = document.getElementById("main");
								mainIf.src = fmainUrl;
							} else {
								alert("主页加载失败");
							}
						}
					});
		}
		//打开修改密码界面
		function openUpdatePassword() {
			openNewTabByName("密码修改", "../admin/system/updateUserPassword.jsp",
					'password');
		}

		//修改个人信息
		function openUserinfo() {
			sendAjaxReq("post", "getCurUserInfo", "", function(request) {
				var jsonstr = request.responseText;
				var json = eval('(' + jsonstr + ')');
				var url = "system/userDialog.jsp";
				url += "?fid=" + json['fid'] + "&recordJsonStr="
						+ escape(jsonstr) + "&type=1";
				openNewTabByName("个人信息", url, 'userinfo');
				//openDialogBox(url);
			}, null, null, null);
		}

		function openHelp() {
			var url = "../baseinfo/Help.jsp";
			openNewTabByName("帮助", url, 'help');
		}

		//打开消息界面
		function openSys() {
			blinkBool = true;
			var ftask = $("#ftask").val();
			var fmsg = $("#fmsg").val();
			openNewTabByName("消息中心", "../../jbpm/jsp/messageCenter.jsp?ftaskMsg="
					+ ftask + "&fmsg=" + fmsg, 'message');
		}
		//打开主页
		function openMain() {
			openNewTabByName("主页", fmainUrl, 'main');
		}
		function getMessageInfo() {
			$.ajax({
						type : 'POST',
						url : 'getMessage',
						async : false,
						dataType : 'text',
						contentType : 'application/x-www-form-urlencoded; charset=utf-8',
						success : function(data) {
							var total = data;
							var message = $("#fmessage").html("消息(" + total + ")");
							$("#ftask").val(total);
						}
					});
		}
		//消息闪烁
		function Setlinkblink() {
			var val = $("#ftask").val();
			var message = document.getElementById('fmessage');
			if (val> 0&& blinkBool == false) {
				if (!message.style.color) {
					message.style.color = "white"
				}
				if (message.style.color == "red") {
					message.style.color = "white"
				} else {
					message.style.color = "red"
				}
				setTimeout("Setlinkblink()", 800);
			}
		}
		//展开/隐藏菜单
		function openMenuPanel() {
			if (!menuFlag) {
				$('body').omBorderLayout('expandRegion', 'west');//设置panel展开
				menuFlag = true;
			} else {
				$('body').omBorderLayout('collapseRegion', 'west');//设置panel隐藏
				menuFlag = false;
			}
		}
	</script>

	<div id="top-panel" class="header">
		<div class="logo">
			<a><img src="../../image/index/login.png" /> </a>${user.person.fname }
		</div>
		<div align="right" style="float:right;">
			<table style="margin:-5px 0px;width:200px;padding:0px 10px 0px 5px">
				<tr>
					<td style="text-align:center;width:25%;">
						<div>
							<img src="../../image/index/sy.png" onclick="openMain()" />
						</div>
						<div style="margin:-5px 0px;cursor:pointer;">
							<a href="javascript:void(0)" onclick="openMain()"><span
								style="color:white">首页</span> </a>
						</div>
					</td>
					<td style="text-align:center;width:25%;">
						<div>
							<img src="../../image/index/help.png" />
						</div>
						<div style="margin:-5px 0px;cursor:pointer;">
							<a href="javascript:void(0)" onclick="openHelp()"><span
								style="color:white">帮助</span> </a>
						</div>
					</td>
					<td style="text-align:center;width:25%;">
						<div>
							<img src="../../image/index/xx.png" />
						</div>
						<div style="margin:-5px;cursor:pointer;">
							<a href="javascript:void(0)" onclick="openSys()"><span
								style="color:white;" id="fmessage" value="">消息</span> </a>
						</div>
					</td>
					<td style="text-align:center;width:25%;">
						<div>
							<img src="../../image/index/grxx.png" />
						</div>
						<div style="margin:-5px;cursor:pointer;">
							<a href="javascript:void(0)" onclick="openUserinfo()"><span
								style="color:white">个人信息</span> </a>
						</div>
					</td>
					<td style="text-align:center;width:25%;">
						<div>
							<img src="../../image/index/mmxg.png" />
						</div>
						<div style="margin:-5px;cursor:pointer;">
							<a href="javascript:void(0)" onclick="openUpdatePassword()"><span
								style="color:white">密码修改</span> </a>
						</div>
					</td>
					<td style="text-align:center;width:25%;">
						<div>
							<img src="../../image/index/xttc.png" />
						</div>
						<div style="margin:-5px;cursor:pointer;">
							<a href="../../logout.action"> <span style="color:white">系统退出
							</span> </a>
						</div>
					</td>
				</tr>
			</table>

		</div>
		<div>
			<div style="float:left;margin:63px 0px 0px -700px;">
				<a onclick="openMenuPanel()">展开/隐藏菜单</a>&nbsp;&nbsp;
				<!-- 
				您当前所在:${datacenterAliasName}
				 -->
			</div>
			<div style="float:left;margin:60px 0px 0px -290px;"></div>
			<div style="float:right;margin:63px -360px;">
				今天是
				<%
				DateFormat fullFormat = DateFormat.getDateInstance(DateFormat.FULL);
				Date date = new Date();
			%>
				<%=fullFormat.format(date)%>
				&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
			</div>
		</div>
	</div>
	<div id="center-panel">
		<div id="center-tab">
			<ul>
				<li><a href="#tab1">主页</a></li>
			</ul>
			<div id="tab1" style="overflow:hidden">
				<iframe id='main' border=0 frameBorder='no' src='' width='100%'></iframe>
			</div>
		</div>
	</div>
	<div id="west-panel">
		<ul id="myTree"></ul>
	</div>
	<div>
		<input type="hidden" id="ftask" /> <input type="hidden" id="fmsg" />
	</div>
</html>