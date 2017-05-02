Ext.namespace("DesignConfig");
var testProcess = '<?xml version="1.0"?>'
		+ '<process name="借款流程" description="借款流程" xmlns="http://jbpm.org/4.4/jpdl">'
		+ '<swimlane name="测试" candidate-users="wind" candidate-groups="开发部,管理员" candidate-depts="开发部" candidate-roles="管理员" />'
		+ '<swimlane name="测试1" candidate-users="autumn" candidate-groups="销售部" candidate-depts="销售部" />'
		+ '<start name="流程开始" g="478,3,110,50" id="001">'
		+ '<transition name="借款发起" to="填写借款申请" g="499,65" id="01"/>'
		+ '</start>'
		+ '<task name="填写借款申请" candidate-users="wind,autumn" g="479,127,110,50" id="002">'
		+ '<transition name="提交申请" to="部门经理审批" g="498,185"  id="02"/>'
		+ '</task>'
		+ '<task name="部门经理审批" g="477,243,110,50" candidate-users="经理" swimlane="测试" id="003">'
		+ '<transition name="部门经理审批通过" to="金额判断" g="313,243"  id="03" />'
		+ '<transition name="部门经理驳回" to="流程结束2" g="657,311"/>'
		+ '</task>'
		+ '<task name="总经理审批" g="108,374,110,50" swimlane="测试" id="005">'
		+ '<transition name="总经理审批通过" to="财务拨款" g="314,376" id="007"/>'
		+ '</task>'
		+ '<task name="财务拨款" candidate-depts="财务部" candidate-roles="财务" id="006" candidate-groups="财务部,财务" g="480,378,110,50">'
		+ '<transition name="邮件通知" to="流程结束2" g="655,379"/>' + '</task>'
		+ '<decision name="金额判断" g="108,242,110,50" >'
		+ '<transition name="&gt;5000总经理审批" to="总经理审批" g="128,308"/>'
		+ '<transition name="&lt;=5000财务拨款" to="财务拨款" g="314,310"/>'
		+ '</decision>' + '<end name="流程结束2" g="789,380,110,50" id="008"/>'
		+ '</process>';
var swimlaneValue = "";
var sqlScriptValue = "";
var noticeValue = "";
var mailtoValue = "";
var process_number = "";
var process_name = "";
var currentChooseNode=null;
var currentClassName=null;
var f_className=null;
var width=55;
var height=90;
var MonitorFlag=true;//判断是否监听，在查看流程图时不需要监听任务事件
var DesignConfig = {
	// 初始化布局
	init : function() {
		tip = "提示";
		// 全局变量
		// 流程ID,列表地址携带过来，如果没有则新增流程。
		process_id = null;
		url = location.href;
		var paraString = url.substring(url.indexOf("?") + 1, url.length)
				.split("&");
		for (i = 0; j = paraString[i]; i++) {
			if ("process_id" == j.substring(0, j.indexOf("=")).toLowerCase()
					&& "ADDNEW" != j.substring(j.indexOf("=") + 1, j.length)) {
				process_id = j.substring(j.indexOf("=") + 1, j.length);
			}
		}

		currentBtn = "select";
		currentParam = "base";
		// --- 图形化设计 ---
		lineFlag = false;
		dragable = true;
		eventsrc = null;// eventsrc是当前触发事件的对象（节点对象）
		presrc = null; // 前一个选中的对象
		currentSelectNode = null;
		xmlNode = null;// 当前选中的XML节点
		selectNodeId = null;// 在左边文本域中选中后赋予其id
		process_fstatus = false;// 流程状态，用于判断选中是否变不变色
		x = 0, y = 0;
		popeventsource = "";
		temp1 = 0;
		temp2 = 0;
		// --- 画线 ---
		// 连线的源和目标
		srcRect = null, desRect = null;
		x0 = 0, x1 = 0, y0 = 0, y1 = 0; // 连线的头尾坐标
		fontX = 0, fontY = 0; // 文字的坐标
		xml = null;// xml对象
		tempDataXml = null;// xml对象
		// 各个节点所拥有的属性
		nodeParams = {
			base : ["base"],// 文本节点
			mail : ["base"],// mail节点
			process : ["base"],// 流程定义
			start : ["base"],// start、end节点
			task : ["base", "form", "method", "sql", "notice", "mailto",
					"change", "delegate","control"],// 任务节点
			transition : ["base", "sql", "notice" ,
					"case"]
		};
		task_win = null;
		refer_win = null;
		user_win = null;
		dept_win = null;
		role_win = null;
		saveWin = null;
		case_win = null;
		sql_win = null;
		variable_win=null;
		// 定义一个初始化的id集合
		ids = "";
		currentNodeId = "";
		// 虚线
		dashLine = null;
		// 显示xml窗口
		xmlWin = null;
		designWin = null;
		var elePanel = new Ext.Panel({
					title : '工具栏',
					region : 'west',
					id : "elePanel",
					iconCls : 'picon05',
					width : 150,
					split : true,
					minSize : 150,
					maxSize : 150,
					contentEl : 'west',
					collapsible : true
				});
		var mainPanel = new Ext.Panel({
			region : 'center',
			autoScroll : true,
			id : "mainPanel",
			contentEl : 'center',
			tbar : new Ext.Toolbar({
						height : 31,
						border : false,
						items : [{
									text : '&nbsp;新建流程',
									iconCls : 'picon17',
									handler : function() {
										Ext.getDom("center").innerHTML = "";
										xml = null;
										initProcess();
									}
								}, '-', {
									text : '&nbsp;导入流程',
									iconCls : 'picon15',
									handler : function() {
									}
								}, '-', {
									text : '&nbsp;导出流程',
									iconCls : 'picon16',
									handler : ''
								}, '-', {
									text : '&nbsp;栅格',
									iconCls : 'picon21',
									handler : function() {
										if (Ext
												.fly(Ext.getDom("center").parentNode)
												.hasClass("center"))
											Ext
													.fly(Ext.getDom("center").parentNode)
													.removeClass("center");
										else
											Ext
													.fly(Ext.getDom("center").parentNode)
													.addClass("center");
									}
								}, '-', {
									text : '&nbsp;查看XML',
									iconCls : 'picon18',
									handler : showXml
								}, '-', {
									text : '&nbsp;查看TEMPDATAXML',
									iconCls : 'picon18',
									handler : showTempDataXml
								}, '-', {
									text : '&nbsp;同步权限',
									iconCls : 'picon18',
									handler : function(){
										Synchronization_xml();
									}
								}, '->', {
									text : '&nbsp;发布流程',
									iconCls : 'picon20',
									handler : ''
								}, '-', {
									text : '&nbsp;保存流程',
									iconCls : 'picon19',
									handler : function(){
										deleteInvalidNodes();
										//setTaskControlData();
										saveProcess();
									}
								}]
					})
		});
		var keyPanel = new Ext.Panel({
					x : 0,
					y : 0,
					layout : 'fit',
					border : false,
					anchor : '0 100%',
					id : "keyPanel",
					contentEl : 'south_key'
				});
		var valuePanel = new Ext.Panel({
			border : false,
			layout : 'card',
			id : 'paramCard',
			activeItem : 0,
			x : 155,
			y : 0,
			anchor : '0 100%',
			items : new Ext.form.FormPanel({
				id : 'base',
				border : false,
				bodyStyle : {
					background : "#dfe7f4",
					padding : "10 0 0 20"
				},
				labelAlign : 'left',
				labelWidth : 80,
				defaults : {
					width : '85%'
				},
				defaultType : 'textfield',
				items : [{
					fieldLabel : '名称',
					id : 'baseName',
					enableKeyEvents : true,
					listeners : {
						'keyup' : function(e) {
							if (presrc != null) {
								if (presrc.tagName.toLowerCase() == "roundrect") {
									presrc.lastChild.lastChild.data = e
											.getValue();
									modifyXmlNodeAttr(presrc, "name", e
													.getValue());
									// 修改指向该节点的transition的to属性
									var lines = Ext.DomQuery
											.select("line[project=" + presrc.id
													+ "]");
									var line2 = Ext.DomQuery
											.select("line[source=" + presrc.id
													+ "]");
									for (var i = 0; i < line2.length; i++){
										modifyXmlNodeAttr(line2[i], "from", e
														.getValue());
									}
									
									for (var i = 0; i < lines.length; i++){
										modifyXmlNodeAttr(lines[i], "to", e
														.getValue());
										modifyXmlNodeAttr(lines[i], "name","to "+ e
														.getValue());
										lines[i].title="to "+e.getValue();
										
										//修改连线间的显示值
										Ext.DomQuery.select("span[title=" 
												+ lines[i].id+ "]")[0].innerHTML="to "+e.getValue();
									
										
									}
								} else if (presrc.tagName.toLowerCase() == "line") {
									Ext.DomQuery.select("span[title="
											+ presrc.id + "]")[0].innerHTML = e
											.getValue();
									modifyXmlNodeAttr(presrc, "name", e
													.getValue());
								} else {
									presrc.innerHTML = e.getValue();
									modifyXmlNodeAttr(Ext.getDom(presrc.title),
											"name", e.getValue());
								}
								if (presrc.tagName.toLowerCase() != "span")
									presrc.title = e.getValue();
							} else {// 为空时表明是流程定义属性
								modifyXmlNodeAttr("process", "name", e
												.getValue());
								// 为临时xml的根节点设置属性值
								tempDataXml.documentElement.setAttribute(
										"name", e.getValue());
							}
						}
					}
				}, {
					xtype : 'textarea',
					fieldLabel : '描述',
					id : 'baseDescrip',
					enableKeyEvents : true,
					listeners : {
						'keyup' : function(e) {
							if (presrc != null) {
								// 修改对应的XML
								if (presrc.tagName.toLowerCase() == "span") {
									modifyXmlNodeAttr(Ext.getDom(presrc.title),
											"description", e.getValue());
								}
								modifyXmlNodeAttr(presrc, "description", e
												.getValue());
							} else {
								modifyXmlNodeAttr("process", "description", e
												.getValue());
								// 为临时xml的根节点设置属性值
								tempDataXml.documentElement.setAttribute(
										"description", e.getValue());
							}
						}
					}
				}]
			})
		});
		var paraPanel = new Ext.Panel({
					title : '流程定义',
					region : 'south',
					id : 'paraPanel',
					iconCls : 'picon01',
					layout : 'absolute',
					collapsible : true,
					autoScroll : true,
					split : true,
					height : 200,
					items : [keyPanel, valuePanel],
					collapseFirst : false,
					tools : [{
								id : 'maximize',
								qtip : '最大化',
								handler : function() {
									paraPanel.setHeight(400);
									designWin.doLayout();
								}
							}, {
								id : 'restore',
								qtip : '还原',
								handler : function() {
									paraPanel.setHeight(145);
									designWin.doLayout();
								}
							}]
				});

		designWin = new Ext.Window({
					title : '流程设计器',
					layout : 'border',
					iconCls : 'picon06',
					width : 900,
					height : 600,
					autoScroll : false,
					plain : false,
					modal : true,
					// maximizable: true,
					maximized : true,
					closeAction : 'hide',
					items : [elePanel, mainPanel, paraPanel]
				});
		designWin.show();
		Ext.getDom("west").style.display = "block";
		Ext.getDom("south_key").style.display = "block";
		// 初始流程属性
		initProcess();
		// 为工具栏按钮注册事件
		Ext.get(Ext.DomQuery.select(".btn")).on("click", function() {
			if (!Ext.fly(this).hasClass("btn_down")) {
				currentBtn = this.title;
				Ext.get(Ext.DomQuery.select(".btn_down"))
						.removeClass("btn_down");
				Ext.fly(this).addClass("btn_down");
				setParams();
			}
		});
		// 为属性栏按钮注册事件
		Ext.get(Ext.DomQuery.select(".key_btn")).on("click", function() {
			if (!Ext.fly(this).hasClass("key_btn_down")) {
				currentParam = this.title;
				Ext.get(Ext.DomQuery.select(".key_btn_down"))
						.removeClass("key_btn_down");
				Ext.fly(this).addClass("key_btn_down");
				// 转换面板
				changeCard();
				// 设置值
				if (this.title != "base") {
					if (presrc == null)
						XmlSetParams("process");
					else if (presrc.tagName.toLowerCase() == "span")
						XmlSetParams(Ext.getDom(presrc.title));
					else
						XmlSetParams(presrc);
				}
			}
		});
		// 为流程区添加右键菜单
		var target = null;
		var contextmenu = new Ext.menu.Menu({
			items : [{
				text : '删除',
				iconCls : 'picon13',
				handler : function() {
					if (presrc == target)
						initProcess();
					// 删除与之相关的文本节点
					if (target.tagName.toLowerCase() == "line"){
						var lineObj=Ext.DomQuery.select("span[title=" + target.id+ "]");
						Ext.get(lineObj).remove();
						
						//删除流程xml中的相关节点,因为已知数组长度为1
						if(lineObj.length==1){
							var leftPoint=lineObj[0].style.left;
							var topPoint=lineObj[0].style.top;
							
							//遍历xml中的所有节点，对比left和top的节点，如果相等那么就删除
							var flagNode=getLineByGpoint(leftPoint,topPoint);
							if(flagNode!=null){
								deleteTempDataXmlById(flagNode.getAttribute("id"));
								flagNode.parentNode.removeChild(flagNode);
							}
							
						}
					}
					// 删除与之相关的连线
					if (target.tagName.toLowerCase() == "roundrect") {
						var sources = Ext.DomQuery.select("line[source="
								+ target.id + "]");
						var projects = Ext.DomQuery.select("line[project="
								+ target.id + "]");
						// 删除与之对应的文本节点
						var lines = sources.concat(projects);
						for (var i = 0; i < lines.length; i++) {
							deleteXmlNode(lines[i]);
							Ext.get(Ext.DomQuery.select("span[title="
									+ lines[i].id + "]")).remove();
						}
						Ext.get(lines).remove();
					}
					if (target.tagName.toLowerCase() == "span") {
						target.innerHTML = "";
						modifyXmlNodeAttr(Ext.getDom(target.title), "name", "");
						Ext.getDom(target.title).title = "";
						target.style.border = 0;
						if (presrc != null && presrc.id == target.title)
							Ext.getCmp("baseName").setValue("");
					} else {
						Ext.fly(target).remove();
						deleteXmlNode(target);
					}
				}
			}]
		});
		Ext.getBody().on('mousedown', function(e) {
			// 判断是否是右键
			if (e.button != "2")
				return false;

			target = e.target;
			// 判断是否在center区域
			if (!Ext.get(target).findParent("div[id=center]", Ext.getBody()))
				return false;
			if (e.target.tagName.toLowerCase() == 'textbox')
				target = event.srcElement.parentElement;
			if (e.target.tagName.toLowerCase() == 'b')
				target = event.srcElement.parentElement.parentElement;
			var tagName = target.tagName.toLowerCase();
			if (tagName != "line" && tagName != "roundrect"
					&& tagName != "span")
				return false;
			contextmenu.showAt(e.getXY());
		});
		// document.onselectstart = function(){return false;}//禁用复制事件
		if(MonitorFlag){
			document.onmousedown = downAction; // 开始移动
			document.onmouseup = upAction; // 结束移动
		}
	},
	winShow : function() {
		designWin.show();
	}
};
// 初始设置流程定义
function initProcess() {
	presrc = null;
	if (xml == null) {
		// 节点计数器
		taskNum = 1;
		lineNum = 1;
		endNum = 1;
		boolNum = 1;
		joinNum = 1;
		forkNum = 1;
		mailNum = 1;
		deleNum = 1;
		controlNum=1;		
		// 树列表计数器
		swimlaneNum = 1;
		sqlNum = 1;
		noticeNum = 1;
		mailtoNum = 1;
		delegateNum = 1;
		formNum = 1;
		caseNum = 1;
		orderValue = 1;
	}
	createDashLine();
	showNodeParams(nodeParams.process, "流程定义", "picon01");
	// 创建xml对象
	createXml();
	// 设置显示属性
	XmlSetParams("process");
}
// 根据点击的按钮设置参数
function setParams() {
	switch (currentBtn) {
		case "select" :
			dragable = true;
			break;
		case "grid" :
			break;
		case "transition" :
			lineFlag = true;
			break;
		default :
	}
}
// 左键按下时方法
function downAction() {
	// 判断是否是左键被按下
	if (event.button != 1)
		return;
	switch (currentBtn) {
		case "select" :
			// 拖动
			drags();
			// 显示属性
			showParams();
			currentSelectNode = eventsrc;
			break;
		case "transition" :
			createLine();
			break;
		default :
			createNode();
	}
}
// 左键释放时方法
function upAction() {
	try
	{
		if (!Ext.get(eventsrc).findParent("div[id=center]", Ext.getBody()))
		{
			return false;
		}
		switch (currentBtn) {
			case "select" :
				dragable = false;
				break;
			case "transition" :
				drawLine();
				break;
			default :
		}
	}
	catch (ex)
	{
		
	}
	
}
// 创建节点
function createNode() {
	if (!nodeOrNot())
		return false;
	var node = document.createElement("v:roundrect");
	node.inset = '2pt,2pt,2pt,2pt';
	node.style.pixelLeft = event.x + Ext.getDom("center").parentNode.scrollLeft;
	node.style.pixelTop = event.y + Ext.getDom("center").parentNode.scrollTop;
	node.style.zIndex = 1;
	node.style.pixelWidth = width;
	node.style.pixelHeight = height;
	
	//node.strokeColor = "#27548d";
	//node.fillcolor = '#EEEEEE';
	
	Ext.fly(node).addClass("node");
	switch (currentBtn) {
		case "start" :
			node.id = "start";
			node.title = "流程开始";
			node.flowtype = "start";
			node.innerHTML = "<v:textbox class='node_start' inset='1pt,2pt,1pt,1pt'></v:textbox>" +
					"<div style='margin-top:60px;margin-left:0px;align:left;padding-left:0px;algin:left;'>流程开始</div>";
			break;
		case "task" :
			node.id = "task" + taskNum;
			node.title = "任务节点" + taskNum;
			node.flowtype = "task";
			node.innerHTML ="<v:textbox class='node_task' inset='1pt,2pt,1pt,1pt'></v:textbox>"+
				"<div style='margin-top:60px;margin-left:0px;align:left;padding-left:0px;algin:left;'>任务节点"+taskNum+"</div>";
			taskNum++;
			break;
		case "end" :
			node.id = "end" + endNum;
			node.title = "流程结束" + endNum;
			node.flowtype = "end";
			node.innerHTML = "<v:textbox class='node_end' inset='1pt,2pt,1pt,1pt'></v:textbox>"+
				"<div style='margin-top:60px;margin-left:0px;align:left;padding-left:0px;algin:left;'>流程结束</div>";
			endNum++;
			break;
		case "bool" :
			node.id = "bool" + boolNum;
			node.title = "决策" + boolNum;
			node.flowtype = "decision";
			node.innerHTML = "<v:textbox class='node_bool' inset='1pt,2pt,1pt,1pt'></v:textbox>"+
				"<div style='margin-top:60px;margin-left:0px;align:left;padding-left:0px;algin:left;'>决策"+boolNum+"</div>";
			boolNum++;
			break;
		case "mail" :
			node.id = "mail" + mailNum;
			node.title = "邮件" + mailNum;
			node.flowtype = "mail";
			node.innerHTML = "<v:textbox class='node_mail' inset='1pt,2pt,1pt,1pt'></v:textbox>"+
				"<div style='margin-top:60px;margin-left:0px;align:left;padding-left:0px;algin:left;'>邮件"+mailNum+"</div>";
			mailNum++;
			break;
		case "join" :
			node.style.pixelWidth = 50;
			node.id = "join" + joinNum;
			node.title = "合并" + joinNum;
			node.flowtype = "join";
			node.innerHTML =  "<v:textbox class='node_join' inset='1pt,2pt,1pt,1pt'><br /></v:textbox>";
			joinNum++;
			break;
		case "fork" :
			node.style.pixelWidth = 50;
			node.id = "fork" + forkNum;
			node.title = "分支" + forkNum;
			node.flowtype = "fork";
			node.innerHTML = "<v:textbox class='node_fork' inset='1pt,2pt,1pt,1pt'><br /></v:textbox>";
			forkNum++;
			break;
		default :
	}
	
	node.strokeColor = "white";
	node.strokeWeight = "0px";
	
	document.getElementById("center").appendChild(node);
	addXmlNode(node);
}
// 判断是否创建节点
function nodeOrNot() {
	// 点击事件事是否发生在工作区
	// 防止右键菜单弹出时点击阴影出错
	if (event.srcElement == null || event.srcElement.firstChild == null)
		return false;
	if (event.srcElement.firstChild.id != "center"
			&& event.srcElement.id != "center")
		return false;
	// 如果是start节点判断是否已经存在
	if (currentBtn == "start" && document.getElementById("start") != null) {
		return false;
	}
	return true;
}
// 将当前触发事件的节点内对象转为节点对象
function selectNode() {
	if (process_fstatus) {// 判断流程状态，如果是true，那么就不会继续执行代码
		return;
	}

	eventsrc = event.srcElement;
	// 如果事件对象是textbox，将事件对象变为它的父对象
	if (event.srcElement.tagName.toLowerCase() == 'textbox')
		eventsrc = event.srcElement.parentElement;
	// 如果事件对象是b，将事件对象变为它的父对象的父对象
	if (event.srcElement.tagName.toLowerCase() == 'b')
		eventsrc = event.srcElement.parentElement.parentElement;
	// 如果是选择并且在center区域，执行下面的选中
	if (currentBtn == "select"
			&& !!Ext.get(eventsrc).findParent("div[id=center]", Ext.getBody())) {
		// 如果前次选择与当前选中一致，不执行以下语句
		// alert(presrc.id + "," + eventsrc.id);
		if (presrc == eventsrc)
			return false;
		if (presrc != null) {
			if (presrc.tagName.toLowerCase() == "span") {
				presrc.style.border = "0";
			} else if(presrc.tagName.toLowerCase() == "line"){
				presrc.strokeColor = "black";
				presrc.strokeWeight = "1px";
				presrc.style.zIndex = 1;
			}else if(presrc.tagName.toLowerCase() == "roundrect"){
				presrc.strokeColor = "white";
				presrc.strokeWeight = "0px";
				presrc.style.zIndex = 1;
				presrc.firstChild.className=cutStrContains(presrc.firstChild.className,"_current");
			}else{
				presrc.strokeColor = "white";
				presrc.strokeWeight = "0px";
				presrc.style.zIndex = 1;
			}
		}
		switch (eventsrc.tagName.toLowerCase()) {
			case "roundrect" :
				eventsrc.strokeColor = "white";
				eventsrc.strokeWeight = "0px";
				eventsrc.style.zIndex = 2;
				f_className=eventsrc.firstChild.className;
				eventsrc.firstChild.className=f_className+"_current";
				break;
			case "line" :
				eventsrc.strokeColor = "red";
				eventsrc.strokeWeight = "2px";
				break;
			case "span" :
				eventsrc.style.border = "2px solid #ff0000";
				break;
		}
		// 将当前节点赋值给presrc
		presrc = eventsrc;
	} else if (currentBtn == "select") {
		if (presrc != null && eventsrc.tagName.toLowerCase() == "div"
				&& eventsrc.firstChild != null
				&& eventsrc.firstChild.id == "center"
				|| eventsrc.id == "center") {
			if (presrc.tagName.toLowerCase() == "span") {
				presrc.style.border = "0";
			} else if(presrc.tagName.toLowerCase() == "line"){
				presrc.strokeColor = "black";
				presrc.strokeWeight = "1px";
				presrc.style.zIndex = 1;
			} else if(presrc.tagName.toLowerCase() == "roundrect"){
				presrc.strokeColor = "white";
				presrc.strokeWeight = "0px";
				presrc.style.zIndex = 1;
				presrc.firstChild.className=cutStrContains(presrc.firstChild.className,"_current");;
			}else{
				presrc.strokeColor = "white";
				presrc.strokeWeight = "0px";
				presrc.style.zIndex = 1;
				
			}
			presrc = null;
		}
	}
}

//查询是否包含某个字符
function cutStrContains(currentStr,str){
	var returnStr;
	if(currentStr.length>1&&currentStr.indexOf(str)!=-1){//是否包含某个字符
		returnStr=currentStr.substring(0,currentStr.indexOf(str));
	}else{
		returnStr=currentStr;
	}
	return returnStr;
}

function move() {
	if (event.button == 1 && dragable) {
		var newleft = temp1 + event.x - x;
		var newtop = temp2 + event.y - y;
		// 重新设置节点的坐标
		modifyXmlNodeAttr(eventsrc, "g", newleft + "," + newtop + ","
						+ eventsrc.style.pixelWidth + ","
						+ eventsrc.style.pixelHeight);
		eventsrc.style.pixelLeft = newleft;
		eventsrc.style.pixelTop = newtop;
		// 重画与节点相关的线和文字节点
		reDrawLine();
		return false;
	}
}
function lineMove() {
	// 移动时的虚线随鼠标移动
	if (lineFlag) {
		// 判断是否有滚动条，有的话加上滚动条的滚动长度
		dashLine.to = (event.x + Ext.getDom("center").parentNode.scrollLeft)
				+ "," + (event.y + Ext.getDom("center").parentNode.scrollTop);
		return false;
	}
}
// 查看选择节点的属性
function showParams() {
	if (presrc == null && eventsrc.tagName.toLowerCase() == "div"
			&& eventsrc.firstChild != null
			&& eventsrc.firstChild.id == "center" || eventsrc.id == "center") {
		// 如果前次选中为null的话，显示流程定义属性
		initProcess();
		return false;
	}

	if (!Ext.get(eventsrc).findParent("div[id=center]", Ext.getBody()))
		return false;
	switch (eventsrc.flowtype) {
		case "start" :
			showNodeParams(nodeParams.start, "开始节点", "picon08");
			break;
		case "end" :
			showNodeParams(nodeParams.start, "结束节点", "picon09");
			break;
		case "decision" :
			showNodeParams(nodeParams.base, "决策节点", "picon04");
			break;
		case "mail" :
			showNodeParams(nodeParams.mail, "邮件节点", "picon10");
			break;
		case "join" :
			showNodeParams(nodeParams.base, "合并", "picon11");
			break;
		case "fork" :
			showNodeParams(nodeParams.base, "分支", "picon12");
			break;
		case "task" :
			showNodeParams(nodeParams.task, "任务节点", "picon02");
			break;
		case "transition" :
			showNodeParams(nodeParams.transition, "转换", "picon03");
			break;
		default :
			// 当选中的是文本节点的话，转换成对应的连线
			eventsrc = Ext.getDom(eventsrc.title);
			showNodeParams(nodeParams.transition, "转换", "picon03");
			break;
	}
	// 设置显示属性
	XmlSetParams(eventsrc);
}
// 显示各节点对应的属性
function showNodeParams(params, title, icon) {
	Ext.getCmp("paraPanel").setTitle(title);
	// Ext.getCmp("paraPanel").setIconClass(icon);
	// 隐藏所有属性
	Ext.get(Ext.DomQuery.select(".key_btn")).setDisplayed("none");
	// 显示对应属性
	for (var i = 0; i < params.length; i++) {
		var param = Ext.DomQuery.select("div[title=" + params[i] + "]")[0];
		param.style.display = "block";
		if (params[i] == "base") {
			param.fireEvent('onclick');
		}
	}
}
function drags() {
	if (event.button != 1)
		return;
	selectNode();
	if (eventsrc.tagName.toLowerCase() == 'roundrect') {
		dragable = true;
		temp1 = eventsrc.style.pixelLeft;
		temp2 = eventsrc.style.pixelTop;
		x = event.x;
		y = event.y;
		document.onmousemove = move;
	}
}
// 创建虚线
function createDashLine() {
	if (document.getElementById("dashLine") == null) {
		dashLine = document.createElement("v:line");
		dashLine.style.display = "none";
		dashLine.style.position = "absolute";
		dashLine.id = "dashLine";
		dashLine.strokeWeight = "2pt";
		dashLine.fillcolor = "#f441ff";
		dashLine.strokeColor = "#f441ff";
		dashLine.innerHTML = "<v:stroke dashstyle='longDash'/><v:shadow on='t' type='single' color='#cccccc' offset='1px,1px'/>";
		document.getElementById("center").appendChild(dashLine);
	}
}
function createLine() {
	selectNode();
	if (eventsrc.tagName == 'roundrect' && eventsrc.flowtype != "end") {
		srcRect = eventsrc;
		// 将虚线显示，并将虚线的起点和终点设为点击事件对象的中心
		var dx = srcRect.style.pixelLeft + srcRect.style.pixelWidth / 2;
		var dy = srcRect.style.pixelTop + srcRect.style.pixelHeight / 2;
		dashLine.from = dx + "," + dy;
		dashLine.to = dx + "," + dy;
		dashLine.style.pixelLeft = dx + 'px';
		dashLine.style.pixelTop = dy + 'px';
		dashLine.style.display = "block";
		document.onmousemove = lineMove;
	} else {
		srcRect = null;
	}
}
function drawLine() {
	if (srcRect == null)
		return;
	selectNode();
	if (eventsrc.tagName == 'roundrect' && srcRect != eventsrc) {
		desRect = eventsrc;
		// 创建线
		// 判断是否画线
		if (drawOrNot()) {
			var line = document.createElement("v:line");
			direction();
			line.from = x0 + "," + y0;
			line.to = x1 + "," + y1;
			line.style.pixelLeft = x0 + 'px';
			line.style.pixelTop = y0 + 'px';
			line.style.position = "absolute";
			line.style.display = "block";
			line.id = "line" + lineNum;
			line.flowtype = "transition";
			line.strokeWeight = "1pt";
			line.style.cursor = "pointer";
			line.strokeColor = "#27548d";
			line.source = srcRect.id;
			line.project = desRect.id;
			// 创建箭头
			line.innerHTML = "<v:stroke endarrow='Classic' />";
			document.getElementById("center").appendChild(line);
			// 在连线上生成文字
			var font = createFont();
			line.title = font.innerHTML;
			lineNum++;
			addXmlNode(line, srcRect);
		}
	}
	// onmouseup事件后隐藏虚线和取消移动事件
	dashLine.style.display = "none";
	document.onmousemove = null;
}
function reDrawLine() {
	var lines = Ext.DomQuery.select("line[project=" + eventsrc.id + "]")
			.concat(Ext.DomQuery.select("line[source=" + eventsrc.id + "]"));
	for (var i = 0; i < lines.length; i++) {
		if (eventsrc.id == lines[i].source) {
			// 将源与目的赋值为线的源与目的
			srcRect = document.getElementById(lines[i].source);
			desRect = document.getElementById(lines[i].project);
			direction();
			lines[i].to = x1 + "," + y1;
			lines[i].from = x0 + "," + y0;
			// 重新设置文本位置
			fontLocation();
			var font = Ext.DomQuery.select("span[title=" + lines[i].id + "]")[0];
			if (font != null) {
				modifyXmlNodeAttr(lines[i], "g", fontX + "," + fontY);
				font.style.pixelLeft = fontX;
				font.style.pixelTop = fontY;
			}
		}
		if (eventsrc.id == lines[i].project) {
			// 将源与目的赋值为线的源与目的
			srcRect = document.getElementById(lines[i].source);
			desRect = document.getElementById(lines[i].project);
			var locations = direction();
			lines[i].to = x1 + "," + y1;
			lines[i].from = x0 + "," + y0;
			// 重新设置文本位置
			fontLocation();
			var font = Ext.DomQuery.select("span[title=" + lines[i].id + "]")[0];
			if (font != null) {
				// 在修改坐标前先修改XML中的坐标，否则改完后找不到相应的节点
				modifyXmlNodeAttr(lines[i], "g", fontX + "," + fontY);
				font.style.pixelLeft = fontX;
				font.style.pixelTop = fontY;
			}
		}
	}
}
// 判断是否画线
function drawOrNot() {
	// 目的地址不能是start
	if (desRect.flowtype == "start")
		return false;
	// 是否已存在
	var lines = document.getElementsByTagName('line');
	for (var i = 0; i < lines.length; i++) {
		if (srcRect.id == lines[i].source && desRect.id == lines[i].project)
			return false;
		if (srcRect.id == "start" && lines[i].source == "start")
			return false;
	}
	return true;
}
// 在横线上生成文字
function createFont() {
	var textNode = document.createElement("span");
	fontLocation();
	textNode.style.pixelLeft = fontX;
	textNode.style.pixelTop = fontY;
	textNode.innerHTML = "to " + desRect.title;
	Ext.fly(textNode).addClass("font_node");
	textNode.title = "line" + lineNum;
	textNode.id = "text" + lineNum;
	document.getElementById("center").appendChild(textNode);
	return textNode;
}
// 判断文字的位置
function fontLocation() {
	fontX = Math.round(x0 + (x1 - x0) / 2 - 30);
	fontY = Math.round(y0 + (y1 - y0) / 2 - 25);
}
// 箭头方向判断
function direction() {
	if (srcRect.style.pixelLeft > desRect.style.pixelLeft) {
		if ((srcRect.style.pixelLeft - desRect.style.pixelLeft) <= desRect.style.pixelWidth) {
			x0 = srcRect.style.pixelLeft + srcRect.style.pixelWidth / 2;
			x1 = desRect.style.pixelLeft + desRect.style.pixelWidth / 2;
			if (srcRect.style.pixelTop > desRect.style.pixelTop) {
				y0 = srcRect.style.pixelTop;
				y1 = desRect.style.pixelTop + desRect.style.pixelHeight;
			} else {
				y0 = srcRect.style.pixelTop + srcRect.style.pixelHeight;
				y1 = desRect.style.pixelTop;
			}
		} else {
			x0 = srcRect.style.pixelLeft;
			x1 = desRect.style.pixelLeft + desRect.style.pixelWidth;
			y0 = srcRect.style.pixelTop + srcRect.style.pixelHeight / 2;
			y1 = desRect.style.pixelTop + desRect.style.pixelHeight / 2;
		}
	} else {
		if ((desRect.style.pixelLeft - srcRect.style.pixelLeft) <= desRect.style.pixelWidth) {
			x0 = srcRect.style.pixelLeft + srcRect.style.pixelWidth / 2;
			x1 = desRect.style.pixelLeft + desRect.style.pixelWidth / 2;
			if (srcRect.style.pixelTop > desRect.style.pixelTop) {
				y0 = srcRect.style.pixelTop;
				y1 = desRect.style.pixelTop + desRect.style.pixelHeight;
			} else {
				y0 = srcRect.style.pixelTop + srcRect.style.pixelHeight;
				y1 = desRect.style.pixelTop;
			}
		} else {
			x0 = srcRect.style.pixelLeft + srcRect.style.pixelWidth;
			x1 = desRect.style.pixelLeft;
			y0 = srcRect.style.pixelTop + srcRect.style.pixelHeight / 2;
			y1 = desRect.style.pixelTop + desRect.style.pixelHeight / 2;
		}
	}
}
// --------------保存流程------------------
function saveProcess() {
	if (false) {
		// 直接保存
	} else {
		// 弹出新增对话框。
		if (saveWin != null) {
			saveWin.show();
		} else {
			var savePanel = new Ext.form.FormPanel({
						id : "savePanel",
						border : true,
						bodyStyle : {
							background : "#dfe7f4",
							padding : "10 0 0 20"
						},
						defaultType : 'textfield',
						items : [{
									fieldLabel : '流程编码',
									id : 'txt_process_Number',
									enableKeyEvents : true,
									labelAlign : 'left',
									disabled : false,
									width : '40%'
								}, {
									fieldLabel : '流程名称',
									id : 'txt_process_Name',
									enableKeyEvents : true,
									labelAlign : 'left',
									disabled : false,
									width : '40%'
								}]

					});
			if(process_number!=""&&process_name!=""){
				Ext.getCmp("txt_process_Number").setValue(process_number);
				Ext.getCmp("txt_process_Name").setValue(process_name);
				
				Ext.getCmp("txt_process_Number").disable();
				Ext.getCmp("txt_process_Name").disable();
			}
			
			saveWin = new Ext.Window({
				id : 'saveWin',
				title : '保存',
				width : 400,
				layout : 'fit',
				height : 300,
				iconCls : 'picon18',
				modal : true,
				closeAction : 'hide',
				maximizable : true,
				items : [savePanel],
				buttons : [{
					text : '保存',
					iconCls : 'picon19',
					handler : function() {
						var processName = Ext.getCmp("txt_process_Name")
								.getValue();
						var processNumber = Ext.getCmp("txt_process_Number")
								.getValue();
						// 获得转换模式中的是否启动节点的值
						var str_process_name = encodeURI(encodeURI(processName));
						modifyXmlNodeAttr("process", "name", processNumber);
						// 为临时xml的根节点设置属性值
						tempDataXml.documentElement.setAttribute("name",
								processNumber);
						var _tempDataXml = tempDataXml.xml.replace(/></g,
								'>\n\r<').replace(/xmlns=\"\"/g, '');
						var _dataXml = xml.xml.replace(/></g, '>\n\r<')
								.replace(/xmlns=\"\"/g, '');
						// 判断 ID 为空，为空新增流程产生新流程ID。 不为空则 修改流程 。
						if (process_id == null) {
							process_id = createNodeID("process");
						}
						Ext.Ajax.request({
							// 被用来向服务器发起请求默认的url
							url : "jbpm_saveProcessTempDataXml.action",
							// 请求时发送后台的参数,既可以是Json对象，也可以直接使用“name =
							// value”形式的字符串
							params : {
								process_id : process_id,
								name : str_process_name,
								number : processNumber
							},
							// 请求时发送后台的xml数据
							xmlData : _tempDataXml,
							// 请求时使用的默认的http方法
							method : "post",
							// 请求成功时回调函数
							success : function(response, options) {
								Ext.Ajax.request({
											// 被用来向服务器发起请求默认的url
											url : "jbpm_saveProcess.action",
											// 请求时发送后台的参数,既可以是Json对象，也可以直接使用“name
											// =
											// value”形式的字符串
											params : {
												process_id : process_id,
												name : str_process_name,
												number : processNumber
											},
											// 请求时发送后台的xml数据
											xmlData : _dataXml,
											// 请求时使用的默认的http方法
											method : "post",
											// 请求成功时回调函数
											success : function(response,
													options) {
												Ext.MessageBox.alert("成功","保存成功！");
												Ext.getCmp("saveWin").hide();
											},
											// 请求失败时
											failure : function(response,
													options) {
												Ext.MessageBox
														.alert(
																'失败',
																'保存失败：'
																		+ response.status);
											}
										});
								Ext.getCmp("saveWin").hide();
							},
							// 请求失败时
							failure : function(response, options) {
								Ext.MessageBox.alert('失败', '保存失败：'
												+ response.status);

							}
						});

					}
				}, {
					text : '取消',
					iconCls : 'picon19',
					handler : function() {
						Ext.getCmp("saveWin").hide();
					}
				}]
			}).show();
		}
	}
}

// 在导入流程的时候加载临时xml数据
function processTempDataXmlFromId(processid) {
	Ext.Ajax.request({
				// 被用来向服务器发起请求默认的url
				url : "jbpm_selectTaskDataAction",
				// 请求时发送后台的参数,既processXmlFromId可以是Json对象，也可以直接使用“name = value”形式的字符串
				params : {
					process_id : processid
				},
				// 请求时使用的默认的http方法
				method : "post",
				// 请求成功时回调函数
				success : function(response, options) {
					var process_data = response.responseText;
					tempDataXml.loadXML(process_data);
				},
				// 请求失败时回调函数
				failure : function(response, options) {
					Ext.MessageBox.alert("失败","导入失败！");
				}
			});
}

//根据流程id导入流程xml
function processXmlFromId(processid){
	Ext.Ajax.request({
				// 被用来向服务器发起请求默认的url
				url : "selectProcessXmlFromId",
				// 请求时发送后台的参数,既可以是Json对象，也可以直接使用“name = value”形式的字符串
				params : {
					process_id : processid
				},
				// 请求时使用的默认的http方法
				method : "post",
				// 请求成功时回调函数
				success : function(response, options) {
					var process_data = response.responseText;
					XmlToProcess(process_data);
				},
				// 请求失败时回调函数
				failure : function(response, options) {
					Ext.MessageBox.alert("失败","导入失败！");
				}
			});
}

// ------------ xml操作 -------------
// 创建xml
function createXml() {
	if (xml == null) {
		xml = new ActiveXObject("Microsoft.XMLDOM");
		var p = xml.createProcessingInstruction("xml",
				"version='1.0' encoding='UTF-8'");
		xml.appendChild(p);
		var root = xml.createElement("process");
		root.setAttribute("name", "新建流程");
		root.setAttribute("xmlns", "http://jbpm.org/4.4/jpdl");
		xml.appendChild(root);
	}

	if (tempDataXml == null) {
		tempDataXml = new ActiveXObject("Microsoft.XMLDOM");
		var p = tempDataXml.createProcessingInstruction("xml",
				"version='1.0' encoding='UTF-8'");
		tempDataXml.appendChild(p);
		var root = tempDataXml.createElement("process");
		root.setAttribute("name", "新建流程");
		root.setAttribute("xmlns", "http://jbpm.org/4.4/jpdl");
		tempDataXml.appendChild(root);
	}
	
	
}
// 查看临时xml
function showTempDataXml() {
	var str = tempDataXml.xml.replace(/></g, '>\n\r<');
	str = str.replace(/xmlns=\"\"/g, '');
	if (xmlWin != null) {
		Ext.getCmp("xmlTextArea").setValue(str);
		xmlWin.show();
		return false;
	}
	xmlWin = new Ext.Window({
				title : '查看XML',
				width : 700,
				layout : 'fit',
				iconCls : 'picon18',
				height : 450,
				modal : true,
				closeAction : 'hide',
				maximizable : true,
				items : new Ext.form.TextArea({
							id : 'xmlTextArea',
							value : str
						}),
				buttons : [{
							text : '保存',
							iconCls : 'picon19',
							handler : function() {
								Ext.MessageBox.alert("提示",Ext.getCmp("xmlTextArea").getValue());
								xmlWin.hide();
							}
						}, {
							text : '取消',
							iconCls : 'picon09',
							handler : function() {
								xmlWin.hide();
							}
						}]
			}).show();
}

// 查看xml
function showXml() {
	var str = xml.xml.replace(/></g, '>\n\r<');
	str = str.replace(/xmlns=\"\"/g, '');
	if (xmlWin != null) {
		Ext.getCmp("xmlTextArea").setValue(str);
		xmlWin.show();
		return false;
	}
	xmlWin = new Ext.Window({
				title : '查看XML',
				width : 700,
				layout : 'fit',
				iconCls : 'picon18',
				height : 450,
				modal : true,
				closeAction : 'hide',
				maximizable : true,
				items : new Ext.form.TextArea({
							id : 'xmlTextArea',
							value : str
						}),
				buttons : [{
							text : '保存',
							iconCls : 'picon19',
							handler : function() {
								XmlToProcess(Ext.getCmp("xmlTextArea")
										.getValue());
								xmlWin.hide();
							}
						}, {
							text : '取消',
							iconCls : 'picon09',
							handler : function() {
								xmlWin.hide();
							}
						}]
			}).show();
}
// 添加xml节点
function addXmlNode(node, parentNode) {
	var newNode = null;// 新节点
	var attr = null;// 属性
	var parent = findXmlNode(parentNode);
	if (parent == null)
		parent = xml.documentElement;// 指向根节点
	switch (node.flowtype) {
		case "start" :
			newNode = xml.createElement("start");
			break;
		case "end" :
			newNode = xml.createElement("end");
			break;
		case "task" :
			newNode = xml.createElement("task");
			break;
		case "decision" :
			newNode = xml.createElement("decision");
			break;
		case "mail" :
			newNode = xml.createElement("mail");
			break;
		case "join" :
			newNode = xml.createElement("join");
			break;
		case "fork" :
			newNode = xml.createElement("fork");
			break;
		case "transition" :
			newNode = xml.createElement("transition");
			// 当选择节点的文本值为null，包括的节点有(join,fork)
			addXmlAttribute(newNode, "from",srcRect.title);
			addXmlAttribute(newNode, "name", "to " + desRect.title);
			addXmlAttribute(newNode, "to", desRect.title);
			// 设置transition节点的g属性的值为对应文本节点的位置
			var font = Ext.DomQuery.select("span[title=" + node.id + "]")[0];
			addXmlAttribute(newNode, "g", font.style.pixelLeft + ","
							+ font.style.pixelTop);
			parent.appendChild(newNode);
			break;
	}

	// 添加ID属性
	var tempNodeid = createNodeID(node.flowtype);
	addXmlAttribute(newNode, "id", tempNodeid);

	if (node.flowtype != "transition") {
		addXmlAttribute(newNode, "name", node.title);
		addXmlAttribute(newNode, "g", node.style.pixelLeft + ","
						+ node.style.pixelTop + "," + node.style.pixelWidth
						+ "," + node.style.pixelHeight);
		parent.appendChild(newNode);
	} else {
		addXmlAttribute(newNode, "condition", tempNodeid);
		var root_son = tempDataXml.createElement(tempNodeid);
		tempDataXml.documentElement.appendChild(root_son);
	}

	if (node.flowtype == "task") {// 新建任务时设置转换模式的默认值
		var root_son = tempDataXml.createElement(tempNodeid);
		// // 新增数据XML节点 tempDataXml
		tempDataXml.documentElement.appendChild(root_son);
	}

}

// 添加xml属性
function addTempXmlAttribute(node, attr, value) {
	var attribute = tempDataXml.createAttribute(attr);
	attribute.value = value;
	node.setAttributeNode(attribute);
}

// 添加xml属性
function addXmlAttribute(node, attr, value) {
	var attribute = xml.createAttribute(attr);
	attribute.value = value;
	node.setAttributeNode(attribute);
}
// 通过XML节点查找流程图节点，当前只用到通过name查找roundrect节点
function findNodeXml(xmlNode) {
	var nodes = [];
	var sameNodes = [];
	// 如果不是节点的话通过title查找，如果是节点的话通过flowtype查找
	if (typeof xmlNode == "object") {
		nodes = Ext.DomQuery.select("[flowtype=" + xmlNode.tagName + "]");
		for (var i = 0; i < nodes.length; i++) {
			if (xmlNode.getAttribute("name") == nodes[i].title)
				sameNodes.push(nodes[i]);
		}
		if (sameNodes.length == 1) {
			return sameNodes[0];
		} else {
			for (i = 0; i < sameNodes.length; i++) {
				if (sameNodes[i].getAttribute("g") == xmlNode.style.pixelLeft
						+ "," + xmlNode.style.pixelTop + ","
						+ xmlNode.style.pixelWidth + ","
						+ xmlNode.style.pixelHeight)
					return sameNodes[i];
			}
		}
	} else {
		// 如果有多个title相同的节点，只能返回第一个
		return Ext.DomQuery.select("roundrect[title=" + xmlNode + "]")[0];
	}
	return null;
}
// 通过流程图节点查找XML节点
function findXmlNode(node) {
	if (node == "process") {
		return xml.documentElement;// 返回根节点
	}
	if (node == null || node.flowtype == null)
		return null;
	var nodes = xml.getElementsByTagName(node.flowtype);
	var sameNodes = [];

	for (var i = 0; i < nodes.length; i++) {
		// 判断是连线还是节点，是节点的话用节点坐标比较，是连线的话，用连线对应的文本节点坐标比较
		if (node.flowtype == "transition") {
			var font = Ext.DomQuery.select("span[title=" + node.id + "]")[0];
			for (i = 0; i < nodes.length; i++) {
				try {
					if (nodes[i].getAttribute("g") == font.style.pixelLeft + ","
						+ font.style.pixelTop){
						sameNodes.push(nodes[i]);
					}
				} catch (e) {
					throw "font没有style属性！";
				 	return;
				}
			}
		} else {
			for (i = 0; i < nodes.length; i++) {
				try {
					if (nodes[i].getAttribute("g") == node.style.pixelLeft + ","
						+ node.style.pixelTop + "," + node.style.pixelWidth
						+ "," + node.style.pixelHeight){
						sameNodes.push(nodes[i]);
					}
				} catch (e) {
				 	throw "node没有style属性！";
				 	return;
				}
				
			}
		}
	}
	if (sameNodes.length == 1) {
		return sameNodes[0];
	} else {
		for (var i = 0; i < sameNodes.length; i++) {
			if (sameNodes[i].getAttribute("name") == node.title) {
				return sameNodes[i];
			}
		}
	}
	/*
	 * 下面是先通过title查找,再通过坐标查找,这样查找的次数比较多【sameNodes的长度比较小】
	 * 上面面先通过坐标查找，再通过title查找，坐标相同的几率比较小
	 * 因为移动的时候文本节点的坐标改变了，是用上面的方法无法定位到对应的transition
	 * 【通过修改重画reDrawLine方法，在修改坐标前先修改XML这样就能定位到相应的transition】 最终使用上面的方法
	 */

	// 先用名字进行查找，如果找到相同的，在用坐标进行匹配，如果还有相同的，返回第一个
	/*
	 * for(var i=0;i<nodes.length;i++){ if(nodes[i].getAttribute("name") ==
	 * node.title){ sameNodes.push(nodes[i]); } } if(sameNodes.length == 1){
	 * return sameNodes[0]; }else{ //判断是连线还是节点，是节点的话用节点坐标比较，是连线的话，用连线对应的文本节点坐标比较
	 * if(node.flowtype == "transition"){ var font =
	 * Ext.DomQuery.select("span[title="+node.id+"]")[0]; for(i=0;i<sameNodes.length;i++){
	 * if(sameNodes[i].getAttribute("g") ==
	 * font.style.pixelLeft+","+font.style.pixelTop) return sameNodes[i]; }
	 * }else{ for(i=0;i<sameNodes.length;i++){
	 * if(sameNodes[i].getAttribute("g") ==
	 * node.style.pixelLeft+","+node.style.pixelTop+","+node.style.pixelWidth+","+node.style.pixelHeight)
	 * return sameNodes[i]; } } } /* //通过一个code进行查找，每个节点必须带code，不通用，不能适用于其他的XML
	 * for(var i=0;i<nodes.length;i++){ if(nodes[i].getAttribute("code") ==
	 * node.id) return nodes[i]; }
	 */
	return null;
}
// 删除节点
function deleteXmlNode(node) {
	// 如果是文本节点，获取对应的k节点，清空name属性
	if (node.tagName.toLowerCase() == "span") {
		node = findXmlNode(Ext.getDom(node.title));
		node.setAttribute("name", "");
		return false;
	}
	// 如果不是文本节点，获取父节点，通过父节点删除自己
	node = findXmlNode(node);
	if(node.getAttribute("id")!=null){
		deleteTempDataXmlById(node.getAttribute("id"));
	}
	if (node != null)
		node.parentNode.removeChild(node);
}
// 修改节点对应的属性
function modifyXmlNodeAttr(node, param, value) {
	node = findXmlNode(node);
	if (node != null)
		node.setAttribute(param, value);
}
// XML的逆向转换，生成节点
function XmltoNode(child) {
	var showNode = true;// 是否创建节点
	var locations = child.getAttribute("g") != null ? child.getAttribute("g")
			.split(",") : [0, 0, 50, 90];
	var node = document.createElement("v:roundrect");
	node.inset = '2pt,2pt,2pt,2pt';
	node.style.pixelLeft = locations[0];
	node.style.pixelTop = locations[1];
//	node.style.pixelWidth = locations[2];
//	node.style.pixelHeight = locations[3];
	node.style.pixelWidth = width;
	node.style.pixelHeight = height;
	
	node.strokeColor = "white";
	node.strokeWeight = "0px";
	Ext.fly(node).addClass("node");
	switch (child.tagName) {
		case "start" :
			node.id = "start";
			node.title = child.getAttribute("name");
			node.flowtype = "start";
			node.innerHTML = ""
					+ "<v:textbox class='node_start' inset='1pt,2pt,1pt,1pt'>"
					 + "</v:textbox><div style='margin-top:60px;margin-left:0px;align:left;padding-left:0px;algin:left;'>" +
					 child.getAttribute("name")+"</div>";
			break;
		case "task" :
			var title = "TaskNode";
			// 标识泳道
			if (child.getAttribute("swimlane") != null
					&& child.getAttribute("swimlane") != "")
				title += "<span title='" + child.getAttribute("swimlane")
						+ "' class='sign'>泳</span>";
			node.id = "task" + taskNum;
			node.title = child.getAttribute("name");
			node.flowtype = "task";
			node.innerHTML = "<v:textbox class='node_task' inset='1pt,2pt,1pt,1pt'>"
					+ "</v:textbox><div style='margin-top:60px;margin-left:0px;align:left;padding-left:0px;algin:left;'>" +
					 child.getAttribute("name")+"</div>";
			taskNum++;
			break;
		case "end" :
			node.id = "end" + endNum;
			node.title = child.getAttribute("name");
			node.flowtype = "end";
			node.innerHTML = "<v:textbox class='node_end' inset='1pt,2pt,1pt,1pt'></v:textbox><div style='margin-top:60px;margin-left:0px;align:left;padding-left:0px;algin:left;'>" +
					 child.getAttribute("name")+"</div>";
			endNum++;
			break;
		case "decision" :
			node.id = "decision" + boolNum;
			node.title = child.getAttribute("name");
			node.flowtype = "decision";
			node.innerHTML = "<v:shadow on='T' type='single' color='#b3b3b3' offset='2px,2px' />"
					+ "<v:textbox class='node_bool' inset='1pt,2pt,1pt,1pt'><b>Decision</b><br />"
					+ child.getAttribute("name") + "</v:textbox>";
			boolNum++;
			break;
		case "mail" :
			node.id = "mail" + mailNum;
			node.title = child.getAttribute("name");
			node.flowtype = "mail";
			node.innerHTML = "<v:textbox class='node_mail' inset='1pt,2pt,1pt,1pt'></v:textbox><div style='margin-top:60px;margin-left:0px;align:left;padding-left:0px;algin:left;'>" +
					 child.getAttribute("name")+"</div>";
			mailNum++;
			break;
		case "join" :
			node.style.pixelWidth = 50;
			node.style.pixelHeight = 50;
			node.id = "join" + joinNum;
			node.title = child.getAttribute("name");
			node.flowtype = "join";
			node.innerHTML = "<v:shadow on='T' type='single' color='#b3b3b3' offset='2px,2px' />"
					+ "<v:textbox class='node_join' inset='1pt,2pt,1pt,1pt'><br /></v:textbox>";
			joinNum++;
			break;
		case "fork" :
			node.style.pixelWidth = 50;
			node.style.pixelHeight = 50;
			node.id = "fork" + forkNum;
			node.title = child.getAttribute("name");
			node.flowtype = "fork";
			node.innerHTML = "<v:shadow on='T' type='single' color='#b3b3b3' offset='2px,2px' />"
					+ "<v:textbox class='node_fork' inset='1pt,2pt,1pt,1pt'><br /></v:textbox>";
			forkNum++;
			break;
		default :
			showNode = false;
	}
	if (showNode)
		document.getElementById("center").appendChild(node);
}
// XML的逆向转换，生成连线及文本节点
function XmltoLine(child) {
	srcRect = findNodeXml(child.parentNode);
	desRect = findNodeXml(child.getAttribute("to"));
	var locations = child.getAttribute("g") != null ? child.getAttribute("g")
			.split(",") : [0, 0];
	var line = document.createElement("v:line");
	direction();
	line.title = child.getAttribute("name");
	line.from = x0 + "," + y0;
	line.to = x1 + "," + y1;
	line.style.pixelLeft = x0 + 'px';
	line.style.pixelTop = y0 + 'px';
	line.style.position = "absolute";
	line.style.display = "block";
	line.id = "line" + lineNum;
	line.flowtype = "transition";
	line.strokeWeight = "1pt";
	line.style.cursor = "pointer";
	line.strokeColor = "#27548d";
	line.source = srcRect.id;
	line.project = desRect.id;
	// 创建箭头
	line.innerHTML = "<v:stroke endarrow='Classic' />";
	document.getElementById("center").appendChild(line);

	// 生成文本节点
	var textNode = document.createElement("span");
	textNode.style.pixelLeft = locations[0];
	textNode.style.pixelTop = locations[1];
	textNode.innerHTML = child.getAttribute("name");
	Ext.fly(textNode).addClass("font_node");
	textNode.title = "line" + lineNum;
	textNode.id = "text" + lineNum;
	document.getElementById("center").appendChild(textNode);
	lineNum++;
}
// XML的逆向转换，将XML转换成为流程图
function XmlToProcess(loadXml) {
	// 清空已有的流程图
	Ext.getDom("center").innerHTML = "";
	xml.loadXML(loadXml);
	// 初始化流程信息
	initProcess();

	var root = findXmlNode("process");
	var rootChilds = root.childNodes;
	for (var i = 0; i < rootChilds.length; i++)
		XmltoNode(rootChilds[i]);
	var lines = xml.getElementsByTagName("transition");
	for (i = 0; i < lines.length; i++)
		XmltoLine(lines[i]);
}
// 根据节点获取对应的XML节点，并对相应的属性页面赋值
function XmlSetParams(node) {
	xmlNode = findXmlNode(node);
	if (xmlNode == null)
		return false;
	switch (currentParam) {
		case "base" :
			if (node != null) {
				Ext.getCmp("baseName").setValue(xmlNode.getAttribute("name"));
				Ext.getCmp("baseDescrip").setValue(xmlNode
						.getAttribute("description"));
			}
			break;
		case "change" :
			if (node != null) {
				var id = xmlNode.getAttribute("id");
				var arr_id = tempDataXml.documentElement
						.getElementsByTagName(id);
				if (arr_id == null
						|| arr_id.length == 0
						|| arr_id[0].getElementsByTagName("changes") == null
						|| arr_id[0].getElementsByTagName("changes").length == 0) {
					// 还原为默认值
					Ext.getCmp("change").form.findField("changeMethodRadio")
							.setValue("1");
					Ext.getCmp("change").form.findField("startMethodRadio")
							.setValue("1");
				} else {
					if (arr_id[0].getElementsByTagName("changes")[0]
							.hasChildNodes()) {

						var change = (arr_id[0].getElementsByTagName("changes"))[0];

						if (change.hasChildNodes()) {
							Ext.getCmp("change").form
									.findField("changeMethodRadio")
									.setValue(change.childNodes[0]
											.getAttribute("value")*1+1);
							Ext.getCmp("change").form
									.findField("startMethodRadio")
									.setValue(change.childNodes[1]
											.getAttribute("value")*1+1);

						}

					}
				}
			}
			break;
		case "form" :
			selectNodeId = null;
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.documentElement.getElementsByTagName(id);
			var forms;
			if (arr_id[0].getElementsByTagName("forms").length != 0) {
				forms = (arr_id[0].getElementsByTagName("forms"))[0];// 获得对应的forms集合
			} else {
				forms = tempDataXml.createElement("forms");// 创建一个新的forms节点
				forms.setAttribute("id", createNodeID("form"));
				arr_id[0].appendChild(forms);
			}
			Ext.getDom("formList").innerHTML = "";// 清空页面树列表
			formNum = 1;// 重新置为1，当回填树节点时重新累计初始值
			// 当页面第一次进入时需要清空数据
			clearFormPage();// 清除转换条件页面数据
			// 设置任务值
			var taskValue=tempDataXml.documentElement.getElementsByTagName("taskvalue");
			var taskType = tempDataXml.documentElement.getElementsByTagName("tasktype");
			if(taskValue.length>0&&taskType.length>0){
				var formValue=taskValue[0].getAttribute("value");
				var actiontype=taskType[0].getAttribute("value");
				
				Ext.getCmp("billFormBox").setValue(formValue);
				Ext.getCmp("billFormBox").disable();// 设置任务为不可编辑
				Ext.getCmp("btn_selectTask").disable();// 设置按钮为不可编辑
				
				typeStore.proxy= new Ext.data.HttpProxy({url: 'queryAction?ftype=' + formValue}); 
                        
				typeStore.load();
			}
			if (forms.hasChildNodes()) {// 存储过数据，回填树节点
				for (var i = 0; i < forms.childNodes.length; i++) {
					var param = forms.childNodes[i].getAttribute("id");
					// 移除事件
					Ext.get(Ext.DomQuery.select(".form_btn")).un("click");
					// 添加树列表
					addTreeList(Ext.getDom("formList"), param,param, "form");
							
					if(i==(forms.childNodes.length-1)){
						formNum=forms.childNodes[i].getAttribute("id").split("-")[2];
					}
					formNum++;
				}
			}
			break;
		case "method" :
			if (node != null) {
				var id = xmlNode.getAttribute("id");
				var arr_id = tempDataXml.documentElement
						.getElementsByTagName(id);
				if (arr_id == null
						|| arr_id.length == 0
						|| arr_id[0].getElementsByTagName("methods") == null
						|| arr_id[0].getElementsByTagName("methods").length == 0) {
					// 还原为默认值
					Ext.getCmp("method").form
							.findField("methodDistributionRadio").setValue("1");
					Ext.getCmp("method").form.findField("methodCompleteRadio")
							.setValue("1");
					clearMethodPage();// 设置个数和百分比的文本框和单选框为不可编辑
				} else {
					clearMethodPage();
					if (arr_id[0].getElementsByTagName("methods")[0]
							.hasChildNodes()) {
						var method = (arr_id[0].getElementsByTagName("methods"))[0].childNodes[0];
						var methodComplete = method.childNodes[1]
								.getAttribute("value");

						Ext.getCmp("method").form
								.findField("methodDistributionRadio")
								.setValue(method.childNodes[0]
										.getAttribute("value"));
						Ext.getCmp("method").form
								.findField("methodCompleteRadio")
								.setValue(methodComplete);

						if (methodComplete == "3") {
							Ext
									.getCmp("methodNumBox")
									.setValue(method.childNodes[1].childNodes[0]
											.getAttribute("value"));
							Ext.getCmp("methodNumBox").enable();
						}
						if (methodComplete == "4") {
							Ext
									.getCmp("methodPecentBox")
									.setValue(method.childNodes[1].childNodes[0]
											.getAttribute("value"));
							Ext.getCmp("methodPecentBox").enable();
						}
					}
				}
			}
			break;
		case "case" :
			selectNodeId = null;
			Ext.getDom("caseList").innerHTML = "";// 清空页面树列表
			caseNum = 1;// 重新置为1，当回填树节点时重新累计初始值
			orderValue=1;
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.documentElement.getElementsByTagName(id);
			var cases;
			if (arr_id != null && arr_id.length > 0
					&& arr_id[0].getElementsByTagName("cases").length != 0) {
				cases = (arr_id[0].getElementsByTagName("cases"))[0];// 获得对应的forms集合
			} else {
				cases = tempDataXml.createElement("cases");// 创建一个新的forms节点
				cases.setAttribute("id", createNodeID("case"));
				arr_id[0].appendChild(cases);
			}
			clearCasePage();// 清除转换条件页面数据
			if (cases.hasChildNodes()) {// 存储过数据，回填树节点
				for (var i = 0; i < cases.childNodes.length; i++) {
					var param = cases.childNodes[i].getAttribute("name");
					// 移除事件
					Ext.get(Ext.DomQuery.select(".case_btn")).un("click");
					// 添加树列表
					addTreeList(Ext.getDom("caseList"), cases.childNodes[i]
									.getAttribute("id"), param, "case");
							
					if(i==(cases.childNodes.length-1)){
						caseNum=cases.childNodes[i].getAttribute("id").split("-")[2];
					}
					caseNum++;
					orderValue++;
				}
			}
			break;
		case "delegate" :
			selectNodeId = null;
			clearDelegatePage();// 清空任务委派界面的数据
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.getElementsByTagName(id);
			var delegates;
			if (arr_id[0].getElementsByTagName("delegates").length == 0) {// 判断临时xml中是否存在delegates节点
				// delegates节点不存在，创建一个delegates节点
				delegates = tempDataXml.createElement("delegates");
				// 给节点添加属性
				delegates.setAttribute("id", createNodeID("delegate"));
				arr_id[0].appendChild(delegates);
			} else {
				// delegates存在，获取delegates节点
				delegates = (arr_id[0].getElementsByTagName("delegates"))[0];
			}
			// 清空页面树列表
			Ext.getDom("delegateList").innerHTML = "";
			delegateNum = 1;// 重新置为1，当回填树节点时重新累计初始值
			// 初次进入delegate页面，若delegates节点有子节点，则解析xml，添加相应的树列表
			if (delegates.hasChildNodes()) {
				for (var i = 0; i < delegates.childNodes.length; i++) {
					var delegateId = delegates.childNodes[i].getAttribute("id");
					// 移除事件
					Ext.get(Ext.DomQuery.select(".delegate_btn")).un("click");
					// 添加树列表
					addTreeList(Ext.getDom("delegateList"), delegateId, delegateId,
							"delegate");
					if(i==(delegates.childNodes.length-1)){
						delegateNum=delegates.childNodes[i].getAttribute("id").split("-")[2];
					}
					delegateNum++;
				}
			}
			break;
		case "swimlane" :
			break;
		case "sql" :
			selectNodeId = null;
			Ext.getDom("sqlList").innerHTML = "";// 清空页面树列表
			sqlNum = 1;// 重新置为1，当回填树节点时重新累计初始值
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.documentElement.getElementsByTagName(id);
			var sqls;
			if (arr_id[0].getElementsByTagName("sqls").length != 0) {
				sqls = (arr_id[0].getElementsByTagName("sqls"))[0];// 获得对应的forms集合
			} else {
				sqls = tempDataXml.createElement("sqls");// 创建一个新的forms节点
				addXmlAttribute(sqls, "id", createNodeID("sql"));
				arr_id[0].appendChild(sqls);
			}
			//清空数据
			Ext.getCmp("sqlParamName").setValue("");
			// 设置任务、功能、选中可编辑
			Ext.getCmp("classname").enable();// 设置类名为可编辑
			Ext.getCmp("methodname").enable();// 设置方法名为可编辑
			Ext.getCmp("classname").setValue("");// 设置类名
			Ext.getCmp("methodname").setValue("");// 设置方法
			if (sqls.hasChildNodes()){// 存储过数据，回填树节点
				for (var i = 0; i < sqls.childNodes.length; i++) {
					var sqlId = sqls.childNodes[i].getAttribute("id");
					// 移除事件
					Ext.get(Ext.DomQuery.select(".sql_btn")).un("click");
					// 添加树列表
					addTreeList(Ext.getDom("sqlList"), sqlId, sqlId, "sql");
					if(i==(sqls.childNodes.length-1)){
						sqlNum=sqls.childNodes[i].getAttribute("id").split("-")[2];
					}
					sqlNum++;
				}
				// 添加后，如果树节点不为空（就是含有数据），那么设置任务、功能、选中不可编辑
				if (sqls.childNodes.length != 0) {
					Ext.getCmp("classname").disable();// 设置任务为不可编辑
					Ext.getCmp("methodname").disable();// 设置功能为不可编辑

					var class_node = (arr_id[0].getElementsByTagName("classname"))[0];
					var method_node = (arr_id[0].getElementsByTagName("methodname"))[0];

					Ext.getCmp("classname").setValue(class_node.getAttribute("value"));// 设置类名
					Ext.getCmp("methodname").setValue(method_node.getAttribute("value"));// 设置方法
				}
			}
			break;
		case "notice" :
			selectNodeId = null;
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.getElementsByTagName(id);
			var notices;
			if (arr_id[0].getElementsByTagName("notices").length == 0) {// 判断临时xml中是否存在notices节点
				// notices节点不存在，创建一个notices节点
				notices = tempDataXml.createElement("notices");
				// 给节点添加属性
				addXmlAttribute(notices, "id", createNodeID("notice"));
				arr_id[0].appendChild(notices);
			} else {
				// notices存在，获取notices节点
				notices = (arr_id[0].getElementsByTagName("notices"))[0];
			}
			// 清空页面树列表
			Ext.getDom("noticeList").innerHTML = "";
			noticeNum = 1;// 重新置为1，当回填树节点时重新累计初始值
			clearNoticePage();// 清空任务委派界面的数据
			// 初次进入notice页面，若notices节点有子节点，则解析xml，添加相应的树列表
			if (notices.hasChildNodes()) {
				for (var i = 0; i < notices.childNodes.length; i++) {
					var noticeId = notices.childNodes[i].getAttribute("id");
					var prama = notices.childNodes[i].getAttribute("name");
					// 移除事件
					Ext.get(Ext.DomQuery.select(".notice_btn")).un("click");
					// 添加树列表
					addTreeList(Ext.getDom("noticeList"), noticeId, prama,
							"notice");
					if(i==(notices.childNodes.length-1)){
						noticeNum=notices.childNodes[i].getAttribute("id").split("-")[2];
					}
					noticeNum++;
				}
			}
			break;
		case "mailto" :
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.documentElement.getElementsByTagName(id);
			var configs;
			if (arr_id[0].getElementsByTagName("configs").length != 0) {
				configs = (arr_id[0].getElementsByTagName("configs"))[0];// 获得对应的forms集合
			} else {
				configs = tempDataXml.createElement("configs");// 创建一个新的forms节点
				configs.setAttribute("id", createNodeID("configs"));
				arr_id[0].appendChild(configs);
			}

			// 当页面第一次进入时就是查询数据库中的数据
			if (!configs.hasChildNodes()) {
				// 查询任务对应的数据库数据
				// 获得form节点中的任务数据
				var taskValue = tempDataXml.documentElement.getElementsByTagName("taskvalue");
				var taskType = tempDataXml.documentElement.getElementsByTagName("tasktype");
				if (taskValue.length==0||taskType.length==0) {
					Ext.MessageBox.alert("提示","请先选择'业务表单'中任务!");
					currentCard = getFormPanel("form");
					currentParam = "form";

					Ext.getCmp('paramCard').items.add(currentCard);

					Ext.getCmp('paramCard').layout.setActiveItem(currentParam);

					Ext.get(Ext.DomQuery.select(".key_btn_down"))
							.removeClass("key_btn_down");
					Ext.fly(Ext.DomQuery.select("div[title=form]")[0])
							.addClass("key_btn_down");
				} else {
					var ftablename = taskValue[0].getAttribute("value");// 获得第一个form节点
					var ftype = taskType[0].getAttribute("value");
					Ext.Ajax.request({
								// 被用来向服务器发起请求默认的url
								url : "getFormTableInfo.action",
								async : false,
								params : {
									ftablename : ftablename,
									ftype : ftype
								},
								// 请求时使用的默认的http方法
								method : "post",
								// 请求成功时回调函数
								success : function(response, options) {
									var store = Ext.getCmp("mailto").getStore();
									store.proxy = new Ext.data.MemoryProxy(eval("("
											+ response.responseText + ")"));
									store.load();
									Ext.getCmp("mailto").reconfigure(
											store,
											Ext.getCmp("mailto")
													.getColumnModel());
								},
								// 请求失败时
								failure : function(response, options) {
									Ext.MessageBox.alert("失败","获取数据失败" + response.status);
								}
							});
				}
			} else {// 存储过数据，回填树节点
				var store_arr = [];
				// 解析xml中的数据
				for (var i = 0; i < configs.childNodes.length; i++) {
					var config_arr = [];
					config_arr.push(configs.childNodes[i].childNodes[0]
							.getAttribute("value"));
					config_arr.push(configs.childNodes[i].childNodes[1]
							.getAttribute("value"));
					config_arr.push(configs.childNodes[i].childNodes[2]
							.getAttribute("value"));
					config_arr.push(configs.childNodes[i].childNodes[3]
							.getAttribute("value"));
					
					store_arr.push(config_arr);
				}
				var store = Ext.getCmp("mailto").getStore();
				store.proxy = new Ext.data.MemoryProxy(store_arr);
				store.load();
				Ext.getCmp("mailto").reconfigure(store,
						Ext.getCmp("mailto").getColumnModel());
			}
			break;
		case "control" :
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.documentElement.getElementsByTagName(id);
			var controls;
			if (arr_id[0].getElementsByTagName("controls").length != 0) {
				controls = (arr_id[0].getElementsByTagName("controls"))[0];// 获得对应的forms集合
			} else {
				controls = tempDataXml.createElement("controls");// 创建一个新的forms节点
				controls.setAttribute("id", createNodeID("control"));
				arr_id[0].appendChild(controls);
			}
			// 当页面第一次进入时就是查询数据库中的数据
			if (!controls.hasChildNodes()) {
				// 查询任务对应的数据库数据
				// 获得form节点中的任务数据
				var taskValue = tempDataXml.documentElement.getElementsByTagName("taskvalue");
				if (taskValue.length==0) {
					Ext.MessageBox.alert("提示","请先选择'业务表单'中任务!");
					currentCard = getFormPanel("form");
					currentParam = "form";

					Ext.getCmp('paramCard').items.add(currentCard);

					Ext.getCmp('paramCard').layout.setActiveItem(currentParam);

					Ext.get(Ext.DomQuery.select(".key_btn_down"))
							.removeClass("key_btn_down");
					Ext.fly(Ext.DomQuery.select("div[title=form]")[0])
							.addClass("key_btn_down");
				} else {
					var ftablename = taskValue[0].getAttribute("value");// 获得第一个form节点
					Ext.Ajax.request({
								// 被用来向服务器发起请求默认的url
								url : "getAllControlButInfo.action",
								async : false,
								params : {
									ftablename : ftablename
								},
								// 请求时使用的默认的http方法
								method : "post",
								// 请求成功时回调函数
								success : function(response, options) {
									var store = Ext.getCmp("control").getStore();
									store.proxy = new Ext.data.MemoryProxy(eval("("
											+ response.responseText + ")"));
									store.load();
									Ext.getCmp("control").reconfigure(store,Ext.getCmp("control").getColumnModel());
								},
								// 请求失败时
								failure : function(response, options) {
									Ext.MessageBox.alert("失败","获取数据失败" + response.status);
								}
							});
				}
			} else {// 存储过数据，回填树节点
				var store_arr = [];
				// 解析xml中的数据
				for (var i = 0; i < controls.childNodes.length; i++) {
					var control_arr = [];
					control_arr.push(controls.childNodes[i].childNodes[0]
							.getAttribute("value"));
					control_arr.push(controls.childNodes[i].childNodes[1]
							.getAttribute("value"));
					control_arr.push(controls.childNodes[i].childNodes[2]
							.getAttribute("value"));
					store_arr.push(control_arr);
				}
				var store = Ext.getCmp("control").getStore();
				store.proxy = new Ext.data.MemoryProxy(store_arr);
				store.load();
				Ext.getCmp("control").reconfigure(store,
						Ext.getCmp("control").getColumnModel());
			}
			break;
		default :
	}
}
// 根据节点名字和类型查找XML节点
function findXmlNodeByName(nodeType, nodeName) {
	var nodes = xml.getElementsByTagName(nodeType);
	for (var i = 0; i < nodes.length; i++)
		if (nodes[i].getAttribute("name") == nodeName)
			return nodes[i];
}

// 根据节点ID和类型查找XML节点
function findXmlNodeById(nodeType, nodeName) {
	var nodes = xml.getElementsByTagName(nodeType);
	for (var i = 0; i < nodes.length; i++)
		if (nodes[i].getAttribute("id") == nodeName)
			return nodes[i];
}

function deleteXmlNodeByType(nodeType, desc) {
	if (selectNodeId == null) {
		Ext.MessageBox.alert("提示","请选择删除的节点!");
		return;
	}

	var treeNode = Ext.getDom(currentTreeNode);
	if (treeNode == null)
		return false;
	var node = findXmlNodeByName(nodeType, treeNode.title);
	var currentNo=currentTreeNode.split("-")[currentTreeNode.split("-").length-1];
	if (nodeType == "form") {
		var node_id = xmlNode.getAttribute("name") + "-form-"
				+ currentNo;// 获得选中的编号，也就是在xml中form对应的id
		// 判断是哪个任务节点的值
		var id = xmlNode.getAttribute("id");// 获得tempDataXml中对应的id字符
		var arr_id = tempDataXml.getElementsByTagName(id);// 获得节点
		if (arr_id.length > 0) {// 数据在临时xml中存在
			var forms = (arr_id[0].getElementsByTagName("forms"))[0];// 获得节点forms
			if (forms.hasChildNodes()) {// 判断forms节点下是否含有数据
				for (var i = 0; i < forms.childNodes.length; i++) {
					if (forms.childNodes[i].getAttribute("id") == node_id) {
					selectNodeId = null;
					forms.removeChild(forms.childNodes[i]);
					Ext.fly(treeNode).remove();
					// 对页面数据进行清空
					Ext.getCmp("facadeCombo").setValue("");//清除任务
					Ext.getCmp("sqlParamNameBox1").setValue("");// 清空参数名称
					Ext.getCmp("sqlParamTypeCombo1").setValue("");// 清空参数类型
					Ext.getCmp("sqlParamValueBox1").setValue("");// 清空参数值
					//清空功能值
					Ext.MessageBox.alert("成功","删除成功");
					// 如果当前删除的为最后一个节点信息，那么清空所有的页面数据
					if (!forms.hasChildNodes()) {
						arr_id[0].removeChild(forms);
						if(clearBillFormBox()){
							Ext.getCmp("billFormBox").setValue("");
							// 设置任务、功能、选中可编辑
							Ext.getCmp("billFormBox").enable();// 设置任务为不可编辑
							Ext.getCmp("btn_selectTask").enable();// 设置按钮为不可编辑
							
							//清除taskvalue和tasktype节点信息
							var taskValue=tempDataXml.documentElement.getElementsByTagName("taskvalue");
							var tasktype=tempDataXml.documentElement.getElementsByTagName("tasktype");
							tempDataXml.documentElement.removeChild(taskValue[0]);
							tempDataXml.documentElement.removeChild(tasktype[0]);
							
							Ext.getCmp("facadeCombo").clearValue();
							
							// 清空formconfig配置的数据
							var configs = (arr_id[0].getElementsByTagName("configs"));//
							if(configs.length>0&&configs[0].hasChildNodes()){
								RemoveAllChildNodes(configs);
							}
						}
					}
					break;
				}
			}
		  }
		}
	} else if (nodeType == "case") {
		// 删除临时xml节点
		var node_id = xmlNode.getAttribute("name") + "-case-"
				+ currentNo;// 获取要删除的节点的ID
		// 判断是哪个任务节点的值
		var id = xmlNode.getAttribute("id");
		var arr_id = tempDataXml.getElementsByTagName(id);
		// 对页面数据进行清空
		clearCasePage();// 调用清空页面数据的方法
		if (arr_id.length > 0) {// 数据在临时xml中存在
			var cases = (arr_id[0].getElementsByTagName("cases"))[0];// 获得节点cases
			if (cases.hasChildNodes()) {// 判断cases节点下是否含有子节点
				for (var i = 0; i < cases.childNodes.length; i++) {// 遍历子节点，删除指定的节点
					if ((cases.childNodes[i]).getAttribute("id") == node_id) {
						
						cases.removeChild(cases.childNodes[i]);
						Ext.fly(treeNode).remove();
						selectNodeId = null;
						Ext.MessageBox.alert("成功","删除成功");
						// 如果当前删除的为最后一个节点信息，那么清空所有的页面数据
						if (!cases.hasChildNodes()) {
							arr_id[0].removeChild(cases);
						}
						break;
					}
				}
			}
			// 更新顺序
			if (cases.hasChildNodes()) {// 判断cases节点下是否含有子节点
				for (var i = 0; i < cases.childNodes.length; i++) {// 遍历子节点，更改其顺序节点
					cases.childNodes[i].childNodes[5].setAttribute("value", i+1);
				}
			}
			orderValue--;
		}
	} else if (nodeType == "sql") {
		var node_id = xmlNode.getAttribute("name") + "-sql-"
				+ currentNo;// 获得选中的编号，也就是在xml中sql对应的id
		// 判断是哪个任务节点的值
		var id = xmlNode.getAttribute("id");// 获得tempDataXml中对应的id字符
		var arr_id = tempDataXml.getElementsByTagName(id);// 获得节点
		if (arr_id.length > 0) {// 数据在临时xml中存在
			var sqls = (arr_id[0].getElementsByTagName("sqls"))[0];// 获得节点sqls
			if (sqls.hasChildNodes()) {// 判断sqls节点下是否含有数据
				for (var i = 0; i < sqls.childNodes.length; i++) {
					if (sqls.childNodes[i].getAttribute("id") == node_id) {
						sqls.removeChild(sqls.childNodes[i]);
						Ext.fly(treeNode).remove();
						selectNodeId = null;
						Ext.MessageBox.alert("成功","删除成功");
						// 对页面数据进行清空
						Ext.getCmp("sqlParamName").setValue("");
						// 如果当前删除的为最后一个节点信息，那么清空所有的页面数据
						if (!sqls.hasChildNodes()) {
							arr_id[0].removeChild(sqls);
							// 删除临时xml中classname和methodname节点，也就是说删除sqls节点下所有信息
							// RemoveAllChildNodes(sqls);
							var classname = (arr_id[0].getElementsByTagName("classname"))[0];// 获得节点classname
							var methodname = (arr_id[0].getElementsByTagName("methodname"))[0];// 获得节点classname
							arr_id[0].removeChild(classname);
							arr_id[0].removeChild(methodname);
							// 对页面数据进行清空
							Ext.getCmp("classname").setValue("");
							Ext.getCmp("methodname").setValue("");
							// 设置任务、功能、选中可编辑
							Ext.getCmp("classname").enable();// 设置任务为不可编辑
							Ext.getCmp("methodname").enable();// 设置功能为不可编辑
							
						}
						break;
					}
				}
			}
		}
	} else if (nodeType == "notice") {
		// 删除临时xml节点
		var node_id = currentTreeNode;// 获取要删除的节点的ID
		// 判断是哪个任务节点的值
		var id = xmlNode.getAttribute("id");
		var arr_id = tempDataXml.getElementsByTagName(id);
		if (arr_id.length > 0) {// 数据在临时xml中存在
			var notices = (arr_id[0].getElementsByTagName("notices"))[0];// 获得节点notices
			if (notices.hasChildNodes()) {// 判断delegates节点下是否含有子节点
				for (var i = 0; i < (notices.childNodes).length; i++) {// 遍历子节点，删除指定的节点
					if ((notices.childNodes[i]).getAttribute("id") == node_id) {
						notices.removeChild(notices.childNodes[i]);
						// 删除树节点
						Ext.fly(treeNode).remove();
						selectNodeId = null;
						Ext.MessageBox.alert("成功","删除成功");
						// 如果当前删除的为最后一个节点信息，那么删除临时xml中的notices节点
						if (!notices.hasChildNodes()) {
							arr_id[0].removeChild(notices);
						}
						break;
					}
				}
			}
		}
		clearNoticePage();// 清空页面数据
	} else if (nodeType == "swimlane") {
	} else if (nodeType == "mailto") {
	} else if (nodeType == "delegate") {
		// 删除临时xml节点
		var node_id = xmlNode.getAttribute("name") + "-delegate-"
				+ currentNo;// 获取要删除的节点的ID
		// 判断是哪个任务节点的值
		var id = xmlNode.getAttribute("id");
		var arr_id = tempDataXml.getElementsByTagName(id);
		if (arr_id.length > 0) {// 数据在临时xml中存在
			var delegates = (arr_id[0].getElementsByTagName("delegates"))[0];// 获得节点delegates
			if (delegates.hasChildNodes()) {// 判断delegates节点下是否含有子节点
				for (var i = 0; i < (delegates.childNodes).length; i++) {// 遍历子节点，删除指定的节点
					if ((delegates.childNodes[i]).getAttribute("id") == node_id) {
						delegates.removeChild(delegates.childNodes[i]);
						Ext.fly(treeNode).remove();
						selectNodeId = null;
						Ext.MessageBox.alert("成功","删除成功");
						// 如果当前删除的为最后一个节点信息，那么删除临时xml中的notices节点
						if (!delegates.hasChildNodes()) {
							arr_id[0].removeChild(delegates);
						}
						break;
					}
				}
			}
		}
		clearDelegatePage();// 清空页面数据
	}else if(nodeType == "control"){
		var node_id = xmlNode.getAttribute("name") + "-control-"
				+ currentNo;// 获得选中的编号，也就是在xml中form对应的id
		// 判断是哪个任务节点的值
		var id = xmlNode.getAttribute("id");// 获得tempDataXml中对应的id字符
		var arr_id = tempDataXml.getElementsByTagName(id);// 获得节点
		if (arr_id.length > 0) {// 数据在临时xml中存在
			var controls = (arr_id[0].getElementsByTagName("controls"))[0];// 获得节点forms
			if (controls.hasChildNodes()) {// 判断forms节点下是否含有数据
				for (var i = 0; i < controls.childNodes.length; i++) {
					if (controls.childNodes[i].getAttribute("id") == node_id) {
						controls.removeChild(controls.childNodes[i]);
						Ext.fly(treeNode).remove();
						// 对页面数据进行清空
						Ext.getCmp("controlname").setValue("");//清空页面数据
						Ext.getCmp("control").form.findField("startUpMethodRadio").setValue("1");
						selectNodeId = null;
						Ext.MessageBox.alert("成功","删除成功");
						// 如果当前删除的为最后一个节点信息，那么清空所有的页面数据
						if (!controls.hasChildNodes()) {
							arr_id[0].removeChild(controls);
						}
						break;
					}
				}
			}
		}
	} else {
		node.parentNode.removeChild(node);
		Ext.fly(treeNode).remove();
	}
}
// 根据节点名称和类型添加节点到临时xml
function addXmlNodeByNameToTempXml(nodeType, nodeName, parentNode) {
	if (parentNode == null) {
		parentNode = tempDataXml.documentElement;
	}
	var newNode = tempDataXml.createElement(nodeType);
	parentNode.appendChild(newNode);
	// 给节点添加属性
	addXmlAttribute(newNode, "name", nodeName);
}

// 根据节点名字和类型添加节点
function addXmlNodeByName(nodeType, nodeName, backdata, parentNode) {
	if (parentNode == null)
		parentNode = xml.documentElement;

	var newNode = xml.createElement(nodeType);
	parentNode.appendChild(newNode);
	addXmlAttribute(newNode, "name", nodeName);
	switch (nodeType) {
		case "swimlane" :
			break;
		case "sql" :
			break;
		case "notice" :
			break;
		case "mailto" :
			break;
		default :
	}
}

// ------------ 属性表单操作 -----------
// 重置表单
function formClear(formId) {
	Ext.getCmp(formId).getForm().getEl().dom.reset();
}
// 为表单赋值
function formFill(type) {
	var node = findXmlNodeByName(type, Ext.getDom(currentTreeNode).title);
	var currentNo=currentTreeNode.split("-")[currentTreeNode.split("-").length-1];
	switch (type) {
		case "form" :
			var node_id = xmlNode.getAttribute("name") + "-form-"
					+ currentNo;
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.getElementsByTagName(id);
			var forms = (arr_id[0].getElementsByTagName("forms"))[0];// 获得对应的forms集合
			if (forms.hasChildNodes()) {
				for (var i = 0; i < forms.childNodes.length; i++) {
					if (forms.childNodes[i].getAttribute("id") == node_id) {
						var node = forms.childNodes[i].childNodes;
						selectNodeId = forms.childNodes[i].getAttribute("id");
						// 设置任务
						Ext.getCmp("billFormBox").setValue(node[0]
								.getAttribute("value"));
						// 设置功能
						Ext.getCmp("facadeCombo").setValue(node[1]
								.getAttribute("value"));
						//设置功能对应的方法名称，就是actionname
						Ext.getCmp("factionname").setValue(node[5]
								.getAttribute("value"));
						//设置功能对应的，就是actiontype
						Ext.getCmp("factiontype").setValue(node[6]
								.getAttribute("value"));
						// 设置参数名称
						Ext.getCmp("sqlParamNameBox1").setValue(node[2]
								.getAttribute("value"));
						// 设置参数类型
						Ext.getCmp("sqlParamTypeCombo1").setValue(node[3]
								.getAttribute("value"));
						// 设置参数值
						Ext.getCmp("sqlParamValueBox1").setValue(node[4]
								.getAttribute("value"));
						break;
					}
				}
			}
			break;
		case "control" :
			var node_id = xmlNode.getAttribute("name") + "-control-"
					+ currentNo;
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.getElementsByTagName(id);
			var controls = (arr_id[0].getElementsByTagName("controls"))[0];// 获得对应的forms集合
			if (controls.hasChildNodes()) {
				for (var i = 0; i < controls.childNodes.length; i++) {
					if (controls.childNodes[i].getAttribute("id") == node_id) {
						var node = controls.childNodes[i].childNodes;
						selectNodeId = controls.childNodes[i].getAttribute("id");
						// 设置控件名称
						Ext.getCmp("controlname").setValue(node[0].getAttribute("value"));
						
						Ext.getCmp("control").form.findField("startUpMethodRadio").setValue(node[1].getAttribute("value"));
						break;
					}
				}
			}
			break;
		case "case" :
			var node_id = xmlNode.getAttribute("name") + "-case-"
					+ currentNo;
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.getElementsByTagName(id);
			var cases = arr_id[0].getElementsByTagName("cases")[0];// 获得对应的cases集合
			if (cases.hasChildNodes()) {
				for (var i = 0; i < cases.childNodes.length; i++) {
					if (cases.childNodes[i].getAttribute("id") == node_id) {
						var node = cases.childNodes[i].childNodes;
						selectNodeId = cases.childNodes[i].getAttribute("id");
						// 设置判断条件
						Ext.getCmp("caseLeftValueBox1").setValue(node[0]
								.getAttribute("value"));
						// 设置符号
						Ext.getCmp("caseCompareCombo1").setValue(node[1]
								.getAttribute("value"));
						// 设置参数值
						Ext.getCmp("caseRightValueBox1").setValue(node[2]
								.getAttribute("value"));
						// 设置连接符
						Ext.getCmp("caseRelationCombo").setValue(node[3]
								.getAttribute("value"));
						// 设置表达式
						Ext.getCmp("caseExpressionBox").setValue(node[4]
								.getAttribute("value"));
						break;
					}
				}
			}
			break;
		case "swimlane" :
			break;
		case "sql" :
			var node_id = xmlNode.getAttribute("name") + "-sql-"
					+ currentNo;
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.getElementsByTagName(id);
			var sqls = (arr_id[0].getElementsByTagName("sqls"))[0];// 获得对应的forms集合
			var classname = arr_id[0].getElementsByTagName("classname");// 获得对应的classname集合
			var methodname = arr_id[0].getElementsByTagName("methodname");// 获得对应的methodname集合
			if (sqls.hasChildNodes()) {
				for (var i = 0; i < sqls.childNodes.length; i++) {
					if (sqls.childNodes[i].getAttribute("id") == node_id) {
						var node = sqls.childNodes[i].childNodes;
						selectNodeId = sqls.childNodes[i].getAttribute("id");
						// 设置参数名称
						Ext.getCmp("classname").setValue(classname[0].getAttribute("value"));
						// 设置参数类型
						Ext.getCmp("methodname").setValue(methodname[0].getAttribute("value"));
						// 设置参数值
						Ext.getCmp("sqlParamName").setValue(node[0].getAttribute("value"));
						break;
					}
				}
			}
			break;
		case "notice" :
			// 获取节点的ID
			var node_id = currentTreeNode;
			// 获取需要回填的任务节点
			var task_id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.getElementsByTagName(task_id);
			var notices = (arr_id[0].getElementsByTagName("notices"))[0];// 获得节点notices
			if (notices.hasChildNodes()) {// 判断notices节点下是否存在子节点，若存在，则继续回填
				for (var i = 0; i < (notices.childNodes).length; i++) {
					if ((notices.childNodes[i]).getAttribute("id") == node_id) {
						var child = notices.childNodes[i].childNodes;
						selectNodeId = notices.childNodes[i].getAttribute("id");
						// 回填数据
						// 消息描述
						if (child[0].getAttribute("value") != ""
								&& child[0].getAttribute("value") != null) {
							Ext.getCmp("noticeDescriptionBox")
									.setValue(child[0].getAttribute("value"));
						} else {
							Ext.getCmp("noticeDescriptionBox").setValue("");
						}
						// 事件类型
						if (child[1].getAttribute("value") != ""
								&& child[1].getAttribute("value") != null) {
							Ext.getCmp("noticeCaseTypeCombo").setValue(child[1]
									.getAttribute("value"));
						} else {
							Ext.getCmp("noticeCaseTypeCombo").setValue("");
						}
						// 接收者
						var recipient = child[2].childNodes;
						// 流程发起者
						if (recipient[0].getAttribute("value") != ""
								&& recipient[0].getAttribute("value") != null) {
							Ext.getCmp("noticeSponsorCheck").setValue(true);
							Ext.getCmp("noticeSponsorCombo")
									.setValue(recipient[0]
											.getAttribute("value"));
							Ext.getCmp("noticeSponsorCombo").enable();
						} else {
							Ext.getCmp("noticeSponsorCheck").setValue(false);
							Ext.getCmp("noticeSponsorCombo").setValue("");
							Ext.getCmp("noticeSponsorCombo").disable();
						}
						// 流程执行者
						var executor = recipient[1].childNodes;
						// 包含或除外
						if (executor[0].getAttribute("value") != ""
								&& executor[0].getAttribute("value") != null) {
							Ext.getCmp("noticeExecutorCheck").setValue(true);
							Ext
									.getCmp("noticeExecutorCombo")
									.setValue(executor[0].getAttribute("value"));
							Ext.getCmp("noticeExecutorCombo").enable();
						} else {
							Ext.getCmp("noticeExecutorCheck").setValue(false);
							Ext.getCmp("noticeExecutorCombo").setValue("");
							Ext.getCmp("noticeExecutorCombo").disable();
						}
						// 对应类型
						if (executor[1].getAttribute("value") != ""
								&& executor[1].getAttribute("value") != null) {
							Ext
									.getCmp("noticeExecutorTypeCombo")
									.setValue(executor[1].getAttribute("value"));
							Ext.getCmp("noticeExecutorTypeCombo").enable();
						} else {
							Ext.getCmp("noticeExecutorTypeCombo")
									.setRawValue("");
							Ext.getCmp("noticeExecutorTypeCombo").disable();
						}
						// 节点
						if (executor[1].getAttribute("value") == 2) {
							Ext
									.getCmp("noticeExecutorAreaCombo")
									.setValue(executor[2].getAttribute("value"));
							Ext.getCmp("noticeExecutorAreaCombo").enable();
						} else {
							Ext.getCmp("noticeExecutorAreaCombo")
									.setRawValue("");
							Ext.getCmp("noticeExecutorAreaCombo").disable();
						}
						// 流程参与者
						var partner = recipient[2].childNodes;
						// 包含或除外
						if (partner[0].getAttribute("value") != ""
								&& partner[0].getAttribute("value") != null) {
							Ext.getCmp("noticePartnerCheck").setValue(true);
							Ext.getCmp("noticePartnerCombo")
									.setValue(partner[0].getAttribute("value"));
							Ext.getCmp("noticePartnerCombo").enable();
						} else {
							Ext.getCmp("noticePartnerCheck").setValue(false);
							Ext.getCmp("noticePartnerCombo").setValue("");
							Ext.getCmp("noticePartnerCombo").disable();
						}
						// 对应类型
						if (partner[1].getAttribute("value") != ""
								&& partner[1].getAttribute("value") != null) {
							Ext.getCmp("noticePartnerTypeCombo")
									.setValue(partner[1].getAttribute("value"));
							Ext.getCmp("noticePartnerTypeCombo").enable();
						} else {
							Ext.getCmp("noticePartnerTypeCombo")
									.setRawValue("");
							Ext.getCmp("noticePartnerTypeCombo").disable();
						}
						// 节点
						if (partner[1].getAttribute("value") == 2) {
							Ext.getCmp("noticePartnerAreaCombo")
									.setValue(partner[2].getAttribute("value"));
							Ext.getCmp("noticePartnerAreaCombo").enable();
						} else {
							Ext.getCmp("noticePartnerAreaCombo")
									.setRawValue("");
							Ext.getCmp("noticePartnerAreaCombo").disable();
						}
						// 用户
						if (recipient[3].getAttribute("value") != ""
								&& recipient[3].getAttribute("value") != null) {
							Ext.getCmp("noticeUserCheck").setValue(true);
							Ext.getCmp("noticeUserBox").setValue(recipient[3]
									.getAttribute("value"));
							Ext.getCmp("noticeUserBox").enable();
							Ext.getCmp("btn_noticeUser").enable();
						} else {
							Ext.getCmp("noticeUserCheck").setValue(false);
							Ext.getCmp("noticeUserBox").setRawValue("");
							Ext.getCmp("noticeUserBox").disable();
							Ext.getCmp("btn_noticeUser").disable();
						}
						// 角色
						if (recipient[4].getAttribute("value") != ""
								&& recipient[4].getAttribute("value") != null) {
							Ext.getCmp("noticeRoleCheck").setValue(true);
							Ext.getCmp("noticeRoleBox").setValue(recipient[4]
									.getAttribute("value"));
							Ext.getCmp("noticeRoleBox").enable();
							Ext.getCmp("btn_noticeRole").enable();
						} else {
							Ext.getCmp("noticeRoleCheck").setValue(false);
							Ext.getCmp("noticeRoleBox").setRawValue("");
							Ext.getCmp("noticeRoleBox").disable();
							Ext.getCmp("btn_noticeRole").disable();
						}
						// 部门
						if (recipient[5].getAttribute("value") != ""
								&& recipient[5].getAttribute("value") != null) {
							Ext.getCmp("noticeDeptCheck").setValue(true);
							Ext.getCmp("noticeDeptBox").setValue(recipient[5]
									.getAttribute("value"));
							Ext.getCmp("noticeDeptBox").enable();
							Ext.getCmp("btn_noticeDept").enable();
						} else {
							Ext.getCmp("noticeDeptCheck").setValue(false);
							Ext.getCmp("noticeDeptBox").setRawValue("");
							Ext.getCmp("noticeDeptBox").disable();
							Ext.getCmp("btn_noticeDept").disable();
						}
						// 消息内容
						if (child[3].getAttribute("value") != ""
								&& child[3].getAttribute("value") != null) {
							Ext.getCmp("noticeContentBox").setValue(child[3]
									.getAttribute("value"));
						} else {
							Ext.getCmp("noticeContentBox").setValue("");
						}
					}
				}
			}
			break;
		case "mailto" :
			break;
		case "delegate" :
			// 获取节点的ID
			var node_id = currentTreeNode;
			// 判断是哪个任务节点的值
			var id = xmlNode.getAttribute("id");
			var arr_id = tempDataXml.getElementsByTagName(id);
			var delegates = (arr_id[0].getElementsByTagName("delegates"))[0];// 获得节点delegates
			if (delegates.hasChildNodes()) {// 判断delegates节点下是否含有子节点
				for (var i = 0; i < (delegates.childNodes).length; i++) {// 遍历子节点，删除指定的节点
					if ((delegates.childNodes[i]).getAttribute("id") == node_id) {
						var child = delegates.childNodes[i].childNodes;
						selectNodeId = delegates.childNodes[i].getAttribute("id");
						// 变量参与者
						if (child[0].getAttribute("value") != ""
								&& child[0].getAttribute("value") != null) {
							Ext.getCmp("variablePartnerCheck").setValue(true);

							Ext.getCmp("variablePartnerBox")
									.setValue(child[0].getAttribute("value"));
							Ext.getCmp("variablePartnerBox").enable();
							
							//设置按钮和隐藏的文本域
							Ext.getCmp("btn_selectVariableParam").enable();
							Ext.getCmp("variable_TaskId")
									.setValue(child[6].getAttribute("value"));
							
						} else {
							Ext.getCmp("variablePartnerCheck").setValue(false);

							Ext.getCmp("variablePartnerBox").setRawValue("");
							Ext.getCmp("variablePartnerBox").disable();
							
							Ext.getCmp("btn_selectVariableParam").disable();
							Ext.getCmp("variable_TaskId")
									.setValue("");
						}
						// 关系参与者
						var relationPartner = child[1].childNodes;
						//
						if (relationPartner[0].getAttribute("value") != ""
								&& relationPartner[0].getAttribute("value") != null) {
							Ext.getCmp("relationPartnerCheck").setValue(true);

							Ext.getCmp("relationPartnerBox").setValue(relationPartner[0].getAttribute("value"));
							Ext.getCmp("relationPartnerBox").enable();
							
							//设置按钮和隐藏的文本框
							Ext.getCmp("relation_TaskName")
									.setValue(relationPartner[2].getAttribute("value"));
									
						} else {
							Ext.getCmp("relationPartnerCheck").setValue(false);

							Ext.getCmp("relationPartnerBox").setRawValue("");
							Ext.getCmp("relationPartnerBox").disable();
							
							Ext.getCmp("relation_TaskName")
									.setValue("");
						}
						// 类型
						if (relationPartner[1].getAttribute("value") != ""
								&& relationPartner[1].getAttribute("value") != null) {
							Ext
									.getCmp("relationPartnerTypeCombo")
									.setValue(relationPartner[1].getAttribute("value"));
							Ext.getCmp("relationPartnerTypeCombo").enable();
						} else {
							Ext.getCmp("relationPartnerTypeCombo")
									.setRawValue("");
							Ext.getCmp("relationPartnerTypeCombo").disable();
						}
						// 用户
						if (child[2].getAttribute("value") != ""
								&& child[2].getAttribute("value") != null) {
							Ext.getCmp("delegateUserCheck").setValue(true);

							Ext.getCmp("delegateUserBox").setValue(child[2]
									.getAttribute("value"));
							Ext.getCmp("delegateUserBox").enable();
							Ext.getCmp("btn_selectUser").enable();
						} else {
							Ext.getCmp("delegateUserCheck").setValue(false);

							Ext.getCmp("delegateUserBox").setRawValue("");
							Ext.getCmp("delegateUserBox").disable();
							Ext.getCmp("btn_selectUser").disable();
						}
						// 角色
						if (child[3].getAttribute("value") != ""
								&& child[3].getAttribute("value") != null) {
							Ext.getCmp("delegateRoleCheck").setValue(true);

							Ext.getCmp("delegateRoleBox").setValue(child[3]
									.getAttribute("value"));
							Ext.getCmp("delegateRoleBox").enable();
							Ext.getCmp("btn_selectRole").enable();
						} else {
							Ext.getCmp("delegateRoleCheck").setValue(false);

							Ext.getCmp("delegateRoleBox").setRawValue("");
							Ext.getCmp("delegateRoleBox").disable();
							Ext.getCmp("btn_selectRole").disable();
						}
						// 部门
						if (child[4].getAttribute("value") != ""
								&& child[4].getAttribute("value") != null) {
							Ext.getCmp("delegateDeptCheck").setValue(true);

							Ext.getCmp("delegateDeptBox").setValue(child[4]
									.getAttribute("value"));
							Ext.getCmp("delegateDeptBox").enable();
							Ext.getCmp("btn_selectDept").enable();
						} else {
							Ext.getCmp("delegateDeptCheck").setValue(false);

							Ext.getCmp("delegateDeptBox").setRawValue("");
							Ext.getCmp("delegateDeptBox").disable();
							Ext.getCmp("btn_selectDept").disable();
						}
						// 泳道
						if (child[5].getAttribute("value") != ""
								&& child[5].getAttribute("value") != null) {
							Ext.getCmp("delegateSwimlaneCheck").setValue(true);

							Ext.getCmp("delegateSwimlaneCombo")
									.setValue(child[5].getAttribute("value"));
							Ext.getCmp("delegateSwimlaneCombo").enable();
						} else {
							Ext.getCmp("delegateSwimlaneCheck").setValue(false);

							Ext.getCmp("delegateSwimlaneCombo").setRawValue("");
							Ext.getCmp("delegateSwimlaneCombo").disable();
						}
					}
				}
			}
			break;
		default :
	}
}
// 添加树列表
function addTreeList(tree, id, title, type) {
	var treeNode = "<div title='" + title + "'" + " id='" + id + "' class='"
			+ type + "_btn'>" + "<span class='icon_" + type + "'>" + title
			+ "</span></div>";
	tree.innerHTML += treeNode;
	// 注册事件
	Ext.get(Ext.DomQuery.select("." + type + "_btn")).on("click", function() {
		if (!Ext.fly(this).hasClass(type + "_btn_down")) {
			Ext.get(Ext.DomQuery.select("." + type + "_btn_down"))
					.removeClass(type + "_btn_down");
			Ext.fly(this).addClass(type + "_btn_down");
			currentTreeNode = this.id;
			formFill(type);
		}
	});
}

// ------------ 属性操作 -------------
// 根据属性按钮转换card布局面板
function changeCard() {
	var currentCard = Ext.getCmp(currentParam);
	if (currentCard == null) {
		switch (currentParam) {
			case "sql" :
				currentCard = sql(currentParam);
				break;
			case "swimlane" :
				currentCard = swimlane(currentParam);
				break;
			case "notice" :
				currentCard = notice(currentParam);
				break;
			case "mailto" :
				currentCard = getMailtoPanel(currentParam);
				break;
			case "change" :
				currentCard = getChangePanel(currentParam);
				break;
			case "delegate" :
				currentCard = getDelegatePanel(currentParam);
				break;
			case "autoDelegate" :
				currentCard = getAutoDelegatePanel(currentParam);
				break;
			case "method" :
				currentCard = getMethodPanel(currentParam);
				break;
			case "form" :
				currentCard = getFormPanel(currentParam);
				break;
			case "control":
				currentCard = getControlPanel(currentParam);
				break;
			case "case" :
				currentCard = casese(currentParam);
				break;
		}
		current_Card = currentCard;
		Ext.getCmp('paramCard').items.add(currentCard);

	}
	Ext.getCmp('paramCard').layout.setActiveItem(currentParam);
}

// ---------------------------------清空页面数据---------------------------------------
// 清空"转换条件"
function clearCasePage() {
	// 清空判断条件
	Ext.getCmp("caseLeftValueBox1").setValue("");
	// 清空符号
	Ext.getCmp("caseCompareCombo1").setValue("");
	// 清空参数值
	Ext.getCmp("caseRightValueBox1").setValue("");
	// 清空连接符
	Ext.getCmp("caseRelationCombo").setValue("");
	// 情况弄个表达式
	Ext.getCmp("caseExpressionBox").setValue("");
}

// 清空"业务表单"
function clearFormPage() {
	// 对页面数据进行清空
	Ext.getCmp("facadeCombo").setValue("");// 清空功能
	Ext.getCmp("sqlParamNameBox1").setValue("");// 清空参数名称
	Ext.getCmp("sqlParamTypeCombo1").setValue("");// 清空参数类型
	Ext.getCmp("sqlParamValueBox1").setValue("");// 清空参数值
}

// 清空任务策略界面,
function clearMethodPage() {
	Ext.getCmp("methodNumBox").setValue("");
	Ext.getCmp("methodPecentBox").setValue("");
	Ext.getCmp("methodNumBox").disable();
	Ext.getCmp("methodPecentBox").disable();
}

// 清空消息提醒面板控件
function clearNoticePage() {
	// 清空消息描述
	Ext.getCmp("noticeDescriptionBox").setValue("");
	// 清空事件类型
	Ext.getCmp("noticeCaseTypeCombo").setRawValue("请选择");
	// 清空接收者
	// 清空发起者
	Ext.getCmp("noticeSponsorCheck").setValue(false);
	Ext.getCmp("noticeSponsorCombo").setRawValue("请选择");
	Ext.getCmp("noticeSponsorCombo").disable();

	// 清空执行者
	Ext.getCmp("noticeExecutorCheck").setValue(false);
	Ext.getCmp("noticeExecutorCombo").setRawValue("请选择");
	Ext.getCmp("noticeExecutorCombo").disable();
	Ext.getCmp("noticeExecutorTypeCombo").setRawValue("请选择");// 类型
	Ext.getCmp("noticeExecutorTypeCombo").disable();
	Ext.getCmp("noticeExecutorAreaCombo").setRawValue("请选择");// 节点
	Ext.getCmp("noticeExecutorAreaCombo").disable();

	// 清空参与者
	Ext.getCmp("noticePartnerCheck").setValue(false);
	Ext.getCmp("noticePartnerCombo").setRawValue("请选择");
	Ext.getCmp("noticePartnerCombo").disable();
	Ext.getCmp("noticePartnerTypeCombo").setRawValue("请选择");// 类型
	Ext.getCmp("noticePartnerTypeCombo").disable();
	Ext.getCmp("noticePartnerAreaCombo").setRawValue("请选择");// 节点
	Ext.getCmp("noticePartnerAreaCombo").disable();

	// 清空用户
	Ext.getCmp("noticeUserCheck").setValue(false);
	Ext.getCmp("noticeUserBox").setValue("");
	Ext.getCmp("noticeUserBox").disable();
	Ext.getCmp("btn_noticeUser").disable();

	// 清空角色
	Ext.getCmp("noticeRoleCheck").setValue(false);
	Ext.getCmp("noticeRoleBox").setValue("");
	Ext.getCmp("noticeRoleBox").disable();
	Ext.getCmp("btn_noticeRole").disable();

	// 清空部门
	Ext.getCmp("noticeDeptCheck").setValue(false);
	Ext.getCmp("noticeDeptBox").setValue("");
	Ext.getCmp("noticeDeptBox").disable();
	Ext.getCmp("btn_noticeDept").disable();

	// 清空消息内容
	Ext.getCmp("noticeContentBox").setValue("");
}

// 清空任务策略界面,
function clearMethodPage() {
	Ext.getCmp("methodNumBox").setValue("");
	Ext.getCmp("methodPecentBox").setValue("");
	Ext.getCmp("methodNumBox").disable();
	Ext.getCmp("methodPecentBox").disable();
}

// 清空"任务委派"
function clearDelegatePage() {
	//变量参与者
	Ext.getCmp("variablePartnerCheck").setValue(false);

	Ext.getCmp("variablePartnerBox").disable();
	Ext.getCmp("variablePartnerBox").setValue("");
	// 关系参与者
	Ext.getCmp("relationPartnerCheck").setValue(false);

	Ext.getCmp("relationPartnerBox").disable();
	Ext.getCmp("relationPartnerBox").setValue("");
	Ext.getCmp("relationPartnerTypeCombo").setRawValue("请选择");// 关系
	Ext.getCmp("relationPartnerTypeCombo").disable();

	// 用户
	Ext.getCmp("delegateUserCheck").setValue(false);

	Ext.getCmp("delegateUserBox").setValue("");
	Ext.getCmp("delegateUserBox").disable();
	Ext.getCmp("btn_selectUser").disable();
	// 角色
	Ext.getCmp("delegateRoleCheck").setValue(false);

	Ext.getCmp("delegateRoleBox").setValue("");
	Ext.getCmp("delegateRoleBox").disable();
	Ext.getCmp("btn_selectRole").disable();
	// 部门
	Ext.getCmp("delegateDeptCheck").setValue(false);

	Ext.getCmp("delegateDeptBox").setValue("");
	Ext.getCmp("delegateDeptBox").disable();
	Ext.getCmp("btn_selectDept").disable();
	// 泳道
	Ext.getCmp("delegateSwimlaneCheck").setValue(false);

	Ext.getCmp("delegateSwimlaneCombo").setRawValue("请选择");
	Ext.getCmp("delegateSwimlaneCombo").disable();

}

// ---------------------------------共用的方法-------------------------------------
// 传入一个页面标识，获得临时xml中的父节点
function getParentNode(nodeType) {
	// 判断当前任务节点是否存在，如果不存在的话就添加一个id节点。这段代码主要是获得id节点
	var root_id = xmlNode.getAttribute("id");
	var temp_root_id = tempDataXml.getElementsByTagName(root_id);
	var root;
	if (temp_root_id.length == 0) {
		root = tempDataXml.createElement(root_id);// 创建一个id节点
		tempDataXml.documentElement.appendChild(root);
	} else {
		root = temp_root_id[0];
	}

	// 首先判断forms节点是否存在，不存在的话就创建一个forms节点
	var parents_node = root.getElementsByTagName(nodeType + "s");
	var parent;
	if (parents_node.length == 0) {
		parent = tempDataXml.createElement(nodeType + "s");// 创建一个新的forms节点
		addXmlAttribute(parent, "id", createNodeID(nodeType));
		root.appendChild(parent);
	} else {
		parent = parents_node[0];
	}
	return parent;
}

// 搜索xml文件中是否存在此节点
function isExistNode(nodeName) {
	// 判断当前任务节点是否存在，如果不存在的话就添加一个id节点。这段代码主要是获得id节点
	var root_id = xmlNode.getAttribute("id");
	var temp_root_id = tempDataXml.getElementsByTagName(root_id);
	var root;
	if (temp_root_id.length == 0) {
		root = tempDataXml.createElement(root_id);// 创建一个id节点
		tempDataXml.documentElement.appendChild(root);
	} else {
		root = temp_root_id[0];
	}

	var node_arr = root.getElementsByTagName(nodeName);

	if (node_arr.length > 0) {
		return true;
	} else {
		return false;
	}
}

// 删除所有的子节点
function RemoveAllChildNodes(node) {
	if (node.hasChildNodes()) {// 当前节点存在子节点
		var len = node.childNodes.length;
		for (var i = 0; i < len; i++) {
			node.removeChild(node.childNodes[0]);
		}
	}
}

// 新增清除选中的样式
function removeAllClass(nodeType) {
	var treeNode = Ext.getDom(currentTreeNode);
	if (treeNode != null) {
		Ext.fly(treeNode).removeClass(nodeType + "_btn_down");
	}
	selectNodeId = null;// 把选中的节点id置为空
}

// -----------------------------同步权限-------------------------------------
function Synchronization_xml() {
	Ext.Ajax.request({
				// 被用来向服务器发起请求默认的url
				url : "updatePermissionInfo.action",
				async : true,
				// 请求时使用的默认的http方法
				method : "post",
				// 请求成功时回调函数
				success : function(response, options) {
					Ext.MessageBox.alert("成功","同步成功");
				},
				// 请求失败时
				failure : function(response, options) {
					Ext.MessageBox.alert("失败","同步失败" + response.status);
				}
			});
}

var allnode = new Array();// 存储所有节点
// 获得xml中所有节点的结合
function getAllNode(root) {// 第一次调用使用的是根节点
	if (root.hasChildNodes()) {// 节点含有子节点
		for (var i = 0; i < root.childNodes.length; i++) {
			allnode.push(root.childNodes[i]);
			// Ext.getDom(root.childNodes[i].getAttribute("id")).stopEvent();
			getAllNode(root.childNodes[i]);
		}
	}
}

// 通过id获得节点
function getNodeById(idString) {
	var returnValue = null;
	if (allnode.length > 0) {
		for (var i = 0; i < allnode.length; i++) {
			if (allnode[i].getAttribute("id") != null
					&& allnode[i].getAttribute("id") == idString) {
				returnValue = allnode[i];
			}
		}
	}
	return returnValue;
}

// 适合所有的节点
function getAllLegalNode() {
	var parent = xml.documentElement;// 获得根节点
	// 首先获得start节点
	var start = parent.getElementsByTagName("start")[0];

}

var hasSelectNode = new Array();// 这是选中的节点集合
// 比较给定的id对应的xml中节点信息
function getHasSelectNode() {
	if (ids.length > 0) {
		for (var i = 0; i < ids.length; i++) {
			if (getNodeById(ids[i]) != null) {
				hasSelectNode.push(getNodeById(ids[i]));
			}
		}
	}
}

var arrname = new Array();
// 获得xml文件中所有的节点的name属性的集合并且存储在arrname
function getAllNodeAttrName(root) {
	if (root.hasChildNodes()) {// 节点含有子节点
		for (var i = 0; i < root.childNodes.length; i++) {
			for (var j = 0; j < ids.length; j++) {
				if (root.childNodes[i].getAttribute("id") == ids[j]) {
					arrname.push(root.childNodes[i].getAttribute("name"));
				}
			}
			getAllNodeAttrName(root.childNodes[i]);
		}
	}
}

// 查看数组中是否包含某个元素
function hasContain(value) {
	var flag = false;
	if(currentNodeId!=""){
		if (getNodeById(currentNodeId).getAttribute("name") == value) {// 如果连线的节点指向当前节点则返回true
			return true;
		}
	}
	for (var i = 0; i < hasSelectNode.length; i++) {// 判断是否为数组
		if (hasSelectNode[i].getAttribute("name") == value) {
			flag = true;
			break;
		}
	}
	return flag;
}

// 设置颜色
function setXmlNodeColor(node) {
	if (node.hasChildNodes()) {// 含有子节点
		for (var i = 0; i < node.childNodes.length; i++) {
			// 判断此节点的指向是否包含在指定的节点中
			if (hasContain(node.childNodes[i].getAttribute("to"))) {
				var param = ownFindNodeXml(node.childNodes[i],node);
				param.strokeColor = "green";
				param.fillcolor = 'green';
				//param.strokeWeight = "2pt";
			}
		}
	}
}

//
// 获得插入流程图的已完成的id
function getCompletedNodeId(fid,fexecutionId) {
	Ext.Ajax.request({
				// 被用来向服务器发起请求默认的url
				url : "selectCompletedNodeFromProcess.action",
				async : true,
				params : {
					fid : fid,
					fexecutionId:fexecutionId
				},
				// 请求时使用的默认的http方法
				method : "post",
				// 请求成功时回调函数
				success : function(response, options) {
					var json = eval("(" + response.responseText + ")");
					ids = json["completedNode"];
					currentNodeId = json["currentNode"];
					var isChangeFlag=json["changeFlag"];
					HideToolbar(fid,fexecutionId,isChangeFlag);
				},
				// 请求失败时
				failure : function(response, options) {
					Ext.MessageBox.alert("失败","取数出错" + response.status);
				}
			});
}

// 查询数据库中xml文件字符串
function xmlDataFromData(fid,fexecutionId) {
	Ext.Ajax.request({
				// 被用来向服务器发起请求默认的url
				url : "selectXmlFromProcess.action",
				async : true,
				params : {
					fid : fid,
					fexecutionId:fexecutionId
				},
				// 请求时使用的默认的http方法
				method : "post",
				// 请求成功时回调函数
				success : function(response, options) {
					XmlToProcess(response.responseText);
				},
				// 请求失败时
				failure : function(response, options) {
					Ext.MessageBox.alert("失败","取数失败" + response.status);
				}
			});
}

// --------------------------------HideToolbar-----------------------------------
function HideToolbar(fid,fexecutionId,isChangeFlag) {
	process_fstatus = true;
	if(isChangeFlag){
		var parent = xml.documentElement;// 获得根节点
		getAllNode(parent);// 获得所有的节点并赋值
		getHasSelectNode();// 获取所有选中的节点进行赋值
	
		var notAuditNode = null;// 下一步操作的节点
		
		var startNode=xml.documentElement.getElementsByTagName("start");//获得start节点
	
		if(startNode.length>0){
			var start=startNode[0];
			var p = findNodeXml(getNodeById(start.getAttribute("id")));// 获得相应的流程节点
			p.strokeColor = "white";
			p.strokeWeight = "0px";
			
			var changeNode=Ext.DomQuery.select("roundrect[flowtype=start] textbox")[0];
			changeNode.className=changeNode.className+"_complete";
			
			setXmlNodeColor(getNodeById(start.getAttribute("id")));// 节点对应的连线节点也变颜色
		}
		for (var i = 0; i < ids.length; i++) {
			if (getNodeById(ids[i]) != null) {// 如果在xml中存在
				var param = findNodeXml(getNodeById(ids[i]));// 获得相应的流程节点
				
				param.strokeColor = "white";
				param.strokeWeight = "0px";
				
				var changeNode=Ext.DomQuery.select("roundrect[id="+param.id+"] textbox")[0];
				changeNode.className=changeNode.className+"_complete";
				
				setXmlNodeColor(getNodeById(ids[i]));// 节点对应的连线节点也变颜色
			}
		}
		
		// 当前查看的节点变颜色
		if (getNodeById(currentNodeId) != null) {
			notAuditNode = getNodeById(currentNodeId);
			var current = findNodeXml(getNodeById(currentNodeId));// 获得相应的流程节点
			
			current.strokeColor = "white";
			current.strokeWeight = "0px";
			
			//当前节点闪烁
			currentChooseNode=current;
			currentClassName=current.firstChild.className;
			Setlinkblink();
		}
	}else{//流程走完了的情况
		
		var parent = xml.documentElement;// 获得根节点
		getAllNode(parent);// 获得所有的节点并赋值
		getHasSelectNode();// 获取所有选中的节点进行赋值
	
		var notAuditNode = null;// 下一步操作的节点
		
		var startNode=xml.documentElement.getElementsByTagName("start");//获得start节点
		var endNode=xml.documentElement.getElementsByTagName("end");//获得end节点
	
		if(startNode.length>0){//设置开始节点
			var start=startNode[0];
			var p = findNodeXml(getNodeById(start.getAttribute("id")));// 获得相应的流程节点
			p.strokeColor = "white";
			p.strokeWeight = "0px";
			
			var changeNode=Ext.DomQuery.select("roundrect[flowtype=start] textbox")[0];
			changeNode.className=changeNode.className+"_complete";
			
			setXmlNodeColor(getNodeById(start.getAttribute("id")));// 节点对应的连线节点也变颜色
		}
		
		if(endNode.length>0){//设置开始节点
			var end=endNode[0];
			var p = findNodeXml(getNodeById(end.getAttribute("id")));// 获得相应的流程节点
			p.strokeColor = "white";
			p.strokeWeight = "0px";
			
			hasSelectNode.push(end);//把结束节点添加到已完成节点id数组中
			
			var changeNode=Ext.DomQuery.select("roundrect[flowtype=end] textbox")[0];
			changeNode.className=changeNode.className+"_complete";
			
			setXmlNodeColor(getNodeById(start.getAttribute("id")));// 节点对应的连线节点也变颜色
		}
		
		for (var i = 0; i < ids.length; i++) {
			if (getNodeById(ids[i]) != null) {// 如果在xml中存在
				var param = findNodeXml(getNodeById(ids[i]));// 获得相应的流程节点
				
				param.strokeColor = "white";
				param.strokeWeight = "0px";
				
				var changeNode=Ext.DomQuery.select("roundrect[id="+param.id+"] textbox")[0];
				changeNode.className=changeNode.className+"_complete";
				
				setXmlNodeColor(getNodeById(ids[i]));// 节点对应的连线节点也变颜色
			}
		}
	}

	var mygrid = getGridPanel(fid,fexecutionId);// 获得生成的面板

	
	//designWin.getComponent("elePanel").collapse(false);
	//designWin.remove("elePanel",true);
	//designWin.getComponent("elePanel").isVisible=false;
	//designWin.getComponent("elePanel").destroy();
	 designWin.getComponent("elePanel").setVisible(false);//隐藏
	//删除panel
	//designWin.remove("elePanel",true);
	//移除mainPanel面板中的工具栏
	var mainp=designWin.getComponent("mainPanel");
	mainp.getTopToolbar().removeAll();
	mainp.doLayout();

	// 定义一个面板说明下一步操作节点是什么
	var lastPanel = new Ext.Panel({
				region : 'north',
				border : false,
				bodyStyle : {
					background : "#dfe7f4",
					margin : "0 90 90 0"
				},
				contentEl : 'south'
			});

	var newpanel = designWin.getComponent("paraPanel");
	newpanel.setTitle("流程状态");

	// 设置组件不可见
	newpanel.getComponent("keyPanel").setVisible(false);
	newpanel.getComponent("paramCard").setVisible(false);

	mygrid.setHeight(250);
	lastPanel.setHeight(30);

	mygrid.doLayout();
	lastPanel.doLayout();

	mygrid.show();

	newpanel.add(mygrid);
	newpanel.add(lastPanel);

	//newpanel.doLayout();
	//designWin.doLayout();
	
	try
	{
		newpanel.doLayout();
	}
	catch (ex)
	{
	}finally{
		try
		{
			designWin.doLayout();
		}
		catch (ex)
		{
		}
	}
}


//设置当前节点闪烁
function Setlinkblink(){
      if (currentChooseNode.firstChild.className == currentClassName){
    	  currentChooseNode.firstChild.className="node";
      }else {
    	  currentChooseNode.firstChild.className=currentClassName;
      }
       setTimeout("Setlinkblink()", 800);
}

// -------------------------------添加表格-------------------------------
function getGridPanel(fid,fexecutionId) {
	// 表格
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var cm = new Ext.grid.ColumnModel([ // 对列的定义，cm是它的简写，作为真个表格的列模式，需要首先创建的
			{
				header : '操作人',
				dataIndex : 'operator'
				// header是表的首部显示文本。dataIndex是列对应的记录集字段,sortable表示列是否可排序，可能还会用到的参数：renderer列的渲染函数，format格式化信息
			}, {
				header : '时间',
				dataIndex : 'opertime'
			}, {
				header : '节点名称',
				dataIndex : 'nodename'
			}, {
				header : '结果',
				dataIndex : 'result'
			}, {
				header : '建议',
				dataIndex : 'fauditidea'
			}]);

	var store = new Ext.data.JsonStore({
				autoDestroy : true,
				url : 'jbpm_queryProcessStatusData.action?fid='+fid+"&fexecutionId="+fexecutionId,
				storeId : 'myStore',
				fields : [
						{
							name : 'operator',
							mapping : 'operator',
							type : 'string'
						}, {
							name : 'opertime',
							mapping : 'opertime',
							type : 'string'
						}, {
							name : 'nodename',
							mapping : 'nodename',
							type : 'string'
						}, {
							name : 'result',
							mapping : 'result',
							type : 'string'
						}, {
							name : 'fauditidea',
							mapping : 'fauditidea',
							type : 'string'
						}]
			});
	store.load();

	var grid = new Ext.grid.GridPanel({
				frame : true,
				title : "已审核",
				store : store,
				region : 'center',
				sm : sm,
				cm : cm,
				viewConfig : {
					forceFit : true// 让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
				}
			});
	return grid;
}

// ------------根据节点name属性的值判断节点是否存在----------------
function isOrNotExistByNodeName(nodeType, nodeName) {
	var flag = false;
	var task_id = xmlNode.getAttribute("id");
	var task_node = tempDataXml.getElementsByTagName(task_id);

	var selectNodeTypes = task_node[0].getElementsByTagName(nodeType + "s")[0];// 获得唯一一个带+s的节点
	if (selectNodeTypes.hasChildNodes()) {// 如果+s的节点的子节点存在
		var selectNodeType = selectNodeTypes.childNodes;
		for (var i = 0; i < selectNodeType.length; i++) {
			if (selectNodeType[i].getAttribute("name") == nodeName) {
				flag = true;
				break;
			}
		}
	}
	return flag;
}

//自定义通过XML节点查找流程图节点，通过name和父节点的source,这个方法主要用于任务节点间的连线
function ownFindNodeXml(xmlNode,parentNode){
	var source=findNodeXml(parentNode);//通过父几点的task获得对应流程的source
	var nodes = [];
	var sameNodes = [];
	// 如果不是节点的话通过title查找，如果是节点的话通过flowtype查找
	if (typeof xmlNode == "object") {
		nodes = Ext.DomQuery.select("[flowtype=" + xmlNode.tagName + "][source="+source.getAttribute("id")+"]");
		for (var i = 0; i < nodes.length; i++) {
			if (xmlNode.getAttribute("name") == nodes[i].title)
				sameNodes.push(nodes[i]);
		}
		if (sameNodes.length == 1) {
			return sameNodes[0];
		} else {
			for (i = 0; i < sameNodes.length; i++) {
				if (sameNodes[i].getAttribute("g") == xmlNode.style.pixelLeft
						+ "," + xmlNode.style.pixelTop + ","
						+ xmlNode.style.pixelWidth + ","
						+ xmlNode.style.pixelHeight)
					return sameNodes[i];
			}
		}
	}
	return null;
}

/**
 * 解析临时xml中的数据,组装成json数组
 * @return
 */
function getTempDataXmlToArray(){
	//首先获得form节点的集合
	var form_coll=tempDataXml.documentElement.getElementsByTagName("form");
	var jsonarr=[];;//用于配置 Ext.data.JsonStore需要的数据
	for(var i=0;i<form_coll.length;i++){
		var arr=new Array();
		var form=form_coll[i];//获得单个form
		var node=form.childNodes;//获得每个form的集合
		//针对每个form获得参数名称、参数值、参数类型，外加上每个参数对应的节点id
		arr[0]=node[2].getAttribute("value");//参数名称
		arr[1]=node[3].getAttribute("value");//参数类型
		arr[2]=node[4].getAttribute("value");//参数值
		
		jsonarr.push(arr);
	}
	return jsonarr;
}


//获得整个流程中第一个节点，也就是起草节点
function getFirstNode(){
	var returnNode=null;
	var start=xml.documentElement.getElementsByTagName("start");//获得开始节点
	if(start.length>0){
		var firstLine=start.childNodes[0];//获得开始节点的指向线条
		var name=firstLine.getAttribute("to");//获得指向节点的name属性
		
		var task_list=xml.documentElement.getElementsByTagName("task");//获得所有的任务节点
		for(var i=0;i<task_list.length;i++){
			if(task_list[i].getAttribute("name")==name){
				returnNode=task_list[i];
				break;
			}
		}
	}
	return returnNode;
}

//删除临时xml中一些无用的子节点，如<sqls ...  />也就是说无<sql></sql>子节点,这个数据是没有任何作用的,也不能保存到数据库中
function deleteInvalidNodes(){
	//获取根节点下所有的子节点（任务节点id形成的节点）
	if(tempDataXml.documentElement.hasChildNodes()){
		var node_list=tempDataXml.documentElement.childNodes;
		for(var i=0;i<node_list.length;i++){
			var id_node=node_list[i];//获得单个id节点
			if(id_node.hasChildNodes()){
				for(var j=0;j<id_node.childNodes.length;j++){
					var sonNode=id_node.childNodes[j];
					if(sonNode.tagName=="forms"||sonNode.tagName=="sqls"||sonNode.tagName=="delegates"
						||sonNode.tagName=="notices"||sonNode.tagName=="controls"||sonNode.tagName=="cases"){//这个可以继续添加判断
						if(!sonNode.hasChildNodes()){
							id_node.removeChild(sonNode);
						}
					}
				}
			}
		}
	}
}


//遍历所有的任务节点，如果不存在的话那么就清除任务节点的值
function clearBillFormBox(){
	var flag=true;
	if(tempDataXml.documentElement.hasChildNodes()){
		var node_list=tempDataXml.documentElement.childNodes;
		for(var i=0;i<node_list.length;i++){
			var id_node=node_list[i];//获得单个id节点
			if(id_node.hasChildNodes()){
				for(var j=0;j<id_node.childNodes.length;j++){
					var sonNode=id_node.childNodes[j];
					if(sonNode.tagName=="forms"&&sonNode.hasChildNodes()){
						flag=false;
					}
				}
			}
		}
	}
	return flag;
}

//根据g点的坐标删除xml中的transition
function getLineByGpoint(leftPoint,topPoint){
	var transition=xml.documentElement.getElementsByTagName("transition");
	if(transition.length>0){
		for(var i=0;i<transition.length;i++){
			var line=transition[i];
			var gPoint=line.getAttribute("g").split(",");
			if((gPoint[0]+"px"==leftPoint)&&(gPoint[1]+"px"==topPoint)){
				return line;
			}
		}
	}
	return null;
}

//对每个任务节点如果没有保存“动态设置”，那么就自动添加数据
function setTaskControlData(){
	//获得ftablename的值
	var taskValue = tempDataXml.documentElement.getElementsByTagName("taskvalue");
	if(taskValue.length>0){
		var ftablename = taskValue[0].getAttribute("value");// 获得第一个form节点
		Ext.Ajax.request({
			// 被用来向服务器发起请求默认的url
			url : "",
			async : true,
			params : {
				ftablename : ftablename
			},
			// 请求时使用的默认的http方法
			method : "post",
			// 请求成功时回调函数
			success : function(response, options) {
				//对临时xml进行判断
				if(tempDataXml.documentElement.hasChildNodes()){//如果根节点含有子子节点
					var taskNodes=tempDataXml.documentElement.childNodes;
					if(taskNodes.length>0){
						for(var i=0;i<taskNodes.length;i++){
							var taskNode=taskNodes[i];//获得单个id节点
							var child=taskNode.getElementsByTagName("controls");
							if(child.length==0&&taskNode.tagName!='tasktype'&&taskNode.tagName!='taskvalue'){
								var controls=getControlsNode(response.responseText);
								taskNode.appendChild(controls);
							}
						}
					}
				}
			},
			// 请求失败时
			failure : function(response, options) {
				Ext.MessageBox.alert("失败","获取数据失败" + response.status);
			}
		});
	}
}

function getControlsNode(data){
	var json=eval("("+data+")");
	var controls = tempDataXml.createElement("controls");
	if(json.length>0){
		for(var j=0;j<json.length;j++){
			//创建一个control节点
			var control = tempDataXml.createElement("control");
			// 创建新的节点
			var fbuttonname_node = tempDataXml.createElement("fbuttonname");// 创建功能节点
			var fenable_node = tempDataXml.createElement("fenable");// 创建参数类型节点
			var fid_node = tempDataXml.createElement("fid");// 创建参数类型节点
			// 给每个节点进行属性赋值
			fbuttonname_node.setAttribute("value", json[j][0]);
			fenable_node.setAttribute("value", json[j][1]);
			fid_node.setAttribute("value",json[j][2]);
			
			control.appendChild(fbuttonname_node);
			control.appendChild(fenable_node);
			control.appendChild(fid_node);
			
			controls.appendChild(control);
		}
	}
	return controls;
}

//通过传入节点的id删除xml数据tempDataXml信息
function deleteTempDataXmlById(id){
	if(id!=null&&id!=""){
		var delNodes=tempDataXml.documentElement.getElementsByTagName(id);
		if(delNodes.length>0){
			tempDataXml.documentElement.removeChild(delNodes[0]);
		}
	}
}


//修改流程时调用此方法设置流程编码和流程名称
function setProcessMessage(number,name){
	process_number = number;
	process_name = name;
}

//参数名称不能存在相同
function hasExistParamName(currentName){
	var parent = tempDataXml.documentElement;// 获得根节点
	
	var sqlParamName=parent.getElementsByTagName("sqlParamNameBox1");
	
	var flag=false;
	
	if(sqlParamName.length>0){
		for(var i=0;i<sqlParamName.length;i++){
			var paramName=sqlParamName[i].getAttribute("value");
			if(paramName==currentName){
				flag=true
				break;
			}
		}
	}
	return flag;
}


//根据流程中出现的任务节点设置variableStroe
function getTaskNodeData(){
	var parent = xml.documentElement;// 获得根节点
	
	var taskNode=parent.getElementsByTagName("task");
	
	var data=[];
	
	if(taskNode.length>0){
		for(var i=0;i<taskNode.length;i++){
			var cell=[];
			var cellName=taskNode[i].getAttribute("name");
			var cellId=taskNode[i].getAttribute("id");
			cell.push(cellName);
			cell.push(cellId);
			data.push(cell);
		}
	}
	
	return data;
}

	typeStore = new Ext.data.Store({
				proxy: new Ext.data.HttpProxy({        
		             url: 'queryAction?ftype='
		         }),        
		         reader: new Ext.data.JsonReader({        
			         idProperty: 'id',
				     root: 'rows',
				     totalProperty: 'results'    
		         }, [        
		             {name: 'value', mapping: 'value'},        
		             {name: 'text', mapping: 'text'}        
		         ])        
			});

	variableStore = new Ext.data.Store({
				proxy: new Ext.data.HttpProxy({        
		             url: 'jbpm_getVariableAction?farr='
		         }),        
		         reader: new Ext.data.JsonReader({        
			         idProperty: 'id',
				     root: 'rows',
				     totalProperty: 'results'    
		         }, [        
		             {name: 'value', mapping: 'value'},        
		             {name: 'text', mapping: 'text'}        
		         ])        
			});		
			
			