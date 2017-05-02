function getDelegatePanel(currentParam){
	current_Card = new Ext.form.FormPanel({
					id: currentParam,
					border: false,
					bodyStyle: {background:"#dfe7f4",padding:"10 0 0 20"},
					labelAlign: 'left',							
					items:[{
						bodyStyle: {background:"#dfe7f4"},
						border: false,
						layout: 'column',	
						items: [{
							layout: 'fit',
							width: 180,
							height: 350,
							border: false,
							items: [{
								xtype: 'panel',
								id: 'delegateListPanel',
								html: '<div class="panel_list" id="delegateList"></div>',
								tbar: new Ext.Toolbar({
									height: 31,
									border: false,
									items : ['->', {
										text : '&nbsp;新增',
										iconCls : 'picon23',
										handler : function() {
											removeAllClass("delegate");
											clearDelegatePage();
										}
										}, '-',
										{text: '&nbsp;添加',iconCls: 'picon23',handler:  function(){
											if(findXmlNodeByName("delegate",xmlNode.getAttribute("name")+"-"+delegateNum) != null){
												Ext.MessageBox.alert(tip,'节点名称不能重复');
												return false;
											}
											
											//delegates节点
											var elementDoc = getParentNode("delegate");
											var newNode;//
											//使用回填的话，那么就是使用已有的数据
											if(selectNodeId!=null){
												for(var i=0;i<elementDoc.childNodes.length;i++){//遍历所有子节点
													if(elementDoc.childNodes[i].getAttribute("id")==selectNodeId){
														newNode=elementDoc.childNodes[i];
														RemoveAllChildNodes(newNode);//因为是修改，那么就删除所有的子节点
														Ext.MessageBox.alert("成功","修改成功");
														break;
													}
												}
											}else{
												newNode = tempDataXml.createElement("delegate");
												addXmlAttribute(newNode, "id", xmlNode.getAttribute("name")+"-delegate-"+delegateNum);
												addXmlAttribute(newNode, "name", xmlNode.getAttribute("name")+"-delegate-"+delegateNum);
												//添加数据到左边的树结构中
												Ext.MessageBox.alert("成功","添加成功");
												//移除事件
												Ext.get(Ext.DomQuery.select(".delegate_btn")).un("click");
												//添加树列表
												var delegateId = xmlNode.getAttribute("name")+"-delegate-"+ delegateNum;
												
												addTreeList(Ext.getDom("delegateList"),delegateId,xmlNode.getAttribute("name")+"-delegate-"+delegateNum,"delegate");
												//触发事件
												Ext.getDom(delegateId).fireEvent('onclick');
											}
											
											//接受所有的参数
											//变量参与者
											var variablePartner_value="";//选中了赋值，没有选中的就为空字符串
											//变量参与者中的ID
											var variable_taskid_value="";//选中了赋值，没有选中的就为空字符串
											if(Ext.getCmp("variablePartnerCheck").getValue()){//
												variablePartner_value=Ext.getCmp("variablePartnerBox").getValue();
												variable_taskid_value=Ext.getCmp("variable_TaskId").getValue();
											}
											
											//关系参与者
											var relationPartner_value="";
											var relationPartner_type="";
											var relationPartner_name="";//从下拉框中选择值后附带的节点名称
											if(Ext.getCmp("relationPartnerCheck").getValue()){//
												//节点名称
												relationPartner_value=Ext.getCmp("relationPartnerBox").getValue();//
												//对应的关系
												relationPartner_type=Ext.getCmp("relationPartnerTypeCombo").getValue();
												//对应节点的ID
												relationPartner_name=Ext.getCmp("relation_TaskName").getValue();
											}
											
											//用户
											var delegateuser_value="";
											var delegateuserId_value="";
											if (!Ext.getCmp("delegateUserBox").disabled) {
												delegateuser_value=Ext.getCmp("delegateUserBox").getValue();
												delegateuserId_value=Ext.getCmp("delegateUserIdBox").getValue();
											}
											//角色
											var delegaterole_value="";
											var delegateroleId_value="";
											if (!Ext.getCmp("delegateRoleBox").disabled) {
												delegaterole_value=Ext.getCmp("delegateRoleBox").getValue();
												delegateroleId_value=Ext.getCmp("delegateRoleIdBox").getValue();
											}
											//部门
											var delegatedept_value="";
											var delegatedeptId_value="";
											if (!Ext.getCmp("delegateDeptBox").disabled) {
												delegatedept_value=Ext.getCmp("delegateDeptBox").getValue();
												delegatedeptId_value=Ext.getCmp("delegateDeptIdBox").getValue();
											}
											//泳道
											var delegateswimlane_value="";
											if (!Ext.getCmp("delegateSwimlaneCheck").disabled) {
												delegateswimlane_value=Ext.getCmp("delegateSwimlaneCombo").getValue();
											}
											
											//给delegate节点添加子节点
											//创建子节点
											var delegateNode1 = tempDataXml.createElement("variablePartner");
											var delegateNode2 = tempDataXml.createElement("relationPartner");
											
											var delegateNode4 = tempDataXml.createElement("delegateuser");
											var delegateNode5 = tempDataXml.createElement("delegaterole");
											var delegateNode6 = tempDataXml.createElement("delegatedept");
											var delegateNode7 = tempDataXml.createElement("delegateswimlane");
											var delegateNode8 = tempDataXml.createElement("variable_taskid");
											
											//给节点添加属性
											addXmlAttribute(delegateNode1, "value", variablePartner_value);
											addXmlAttribute(delegateNode4, "value", delegateuser_value);
											addXmlAttribute(delegateNode4, "id", delegateuserId_value);
											addXmlAttribute(delegateNode5, "value", delegaterole_value);
											addXmlAttribute(delegateNode5, "id", delegateroleId_value);
											addXmlAttribute(delegateNode6, "value", delegatedept_value);
											addXmlAttribute(delegateNode6, "id", delegatedeptId_value);
											addXmlAttribute(delegateNode7, "value", delegateswimlane_value);
											addXmlAttribute(delegateNode8, "value", variable_taskid_value);
											
											//添加子节点						
											newNode.appendChild(delegateNode1);
											newNode.appendChild(delegateNode2);
											newNode.appendChild(delegateNode4);
											newNode.appendChild(delegateNode5);
											newNode.appendChild(delegateNode6);
											newNode.appendChild(delegateNode7);
											newNode.appendChild(delegateNode8);
											
											//流程执行者和参与者下有子元素，因此需添加子节点
											//流程执行者
											var Executor_v = tempDataXml.createElement("relationPartner_value");
											var Executor_t = tempDataXml.createElement("relationPartner_type");
											var Executor_n = tempDataXml.createElement("relationPartner_name");
											//给节点添加属性
											addXmlAttribute(Executor_v, "value", relationPartner_value);
											addXmlAttribute(Executor_t, "value", relationPartner_type);
											addXmlAttribute(Executor_n, "value", relationPartner_name);
											//添加节点
											delegateNode2.appendChild(Executor_v);
											delegateNode2.appendChild(Executor_t);
											delegateNode2.appendChild(Executor_n);
											
											if(selectNodeId==null){
												elementDoc.appendChild(newNode);
											}
											delegateNum ++;
											removeAllClass("delegate");
											clearDelegatePage();
									}},'-',
										{text: '&nbsp;删除',iconCls: 'picon13',handler: function(){
											deleteXmlNodeByType("delegate");//删除节点
										}}
									]
								})
							}]
						},{
							width: 170,
							labelWidth: 80,
							border: false,
							layout: 'form',
							defaults: {height: 22},
							bodyStyle: {background:"#dfe7f4"},
							defaultType: 'checkbox',
							items: [{
								id: 'variablePartnerCheck',//variablePartnerCheck delegateSponsorCheck
								fieldLabel: '任务委派',
								boxLabel: '变量参与者',
								listeners: {
									"check": function(){
										var variableFlag=Ext.getCmp("variablePartnerBox").disabled;
										variableFlag?Ext.getCmp("variablePartnerBox").enable():Ext.getCmp("variablePartnerBox").disable();
										variableFlag?Ext.getCmp("btn_selectVariableParam").enable():Ext.getCmp("btn_selectVariableParam").disable();
									}
								}	
							},{
								id: 'relationPartnerCheck',//relationPartnerCheck
								boxLabel: '关系参与者',
								listeners: {
									"check": function(){
										Ext.getCmp("relationPartnerBox").disabled?Ext.getCmp("relationPartnerBox").enable():Ext.getCmp("relationPartnerBox").disable();
										Ext.getCmp("relationPartnerTypeCombo").disabled?Ext.getCmp("relationPartnerTypeCombo").enable():Ext.getCmp("relationPartnerTypeCombo").disable();
										
										var farr=getTaskNodeData();
											
										variableStore.proxy= new Ext.data.HttpProxy({url: 'jbpm_getVariableAction?farr='+encodeURI(encodeURI(JSON.stringify(farr)))}); 
                    
										variableStore.load();
									}
								}	
							},{
								boxLabel: '用户',
								id: 'delegateUserCheck',
								listeners: {
									"check": function(){
										var flagUser=Ext.getCmp("delegateUserBox").disabled;
										flagUser?Ext.getCmp("delegateUserBox").enable():Ext.getCmp("delegateUserBox").disable();
										flagUser?Ext.getCmp("delegateUserIdBox").enable():Ext.getCmp("delegateUserIdBox").disable();
										flagUser?Ext.getCmp("btn_selectUser").enable():Ext.getCmp("btn_selectUser").disable();
										//Ext.getCmp('btn_selectUser').setDisabled(false); 
										if(xmlNode != null){
											if(Ext.getCmp("delegateUserBox").disabled){
												xmlNode.removeAttribute("candidate-users");
											}else{
												xmlNode.setAttribute("candidate-users",Ext.getCmp("delegateUserBox").getValue());
											}
										}
									}
								}	
							},{
								boxLabel: '角色',
								id: 'delegateRoleCheck',
								listeners: {
									"check": function(){
										var flagRole=Ext.getCmp("delegateRoleBox").disabled;
										flagRole?Ext.getCmp("delegateRoleBox").enable():Ext.getCmp("delegateRoleBox").disable();
										flagRole?Ext.getCmp("delegateRoleIdBox").enable():Ext.getCmp("delegateRoleIdBox").disable();
										flagRole?Ext.getCmp("btn_selectRole").enable():Ext.getCmp("btn_selectRole").disable();
										//Ext.getCmp('btn_selectRole').setDisabled(false); 
										if(xmlNode != null){
											if(Ext.getCmp("delegateRoleBox").disabled){
												xmlNode.removeAttribute("candidate-roles");
												xmlNode.setAttribute("candidate-groups",Ext.getCmp("delegateDeptBox").disabled?"":Ext.getCmp("delegateDeptBox").getValue());
												if(xmlNode.getAttribute("candidate-groups") == "")
													xmlNode.removeAttribute("candidate-groups");
											}else{
												xmlNode.setAttribute("candidate-roles",Ext.getCmp("delegateRoleBox").getValue());
												if(xmlNode.getAttribute("candidate-groups")!=""&&xmlNode.getAttribute("candidate-groups")!=null){
													if(Ext.getCmp("delegateRoleBox").getValue() != "")
														xmlNode.setAttribute("candidate-groups",xmlNode.getAttribute("candidate-groups")+","+Ext.getCmp("delegateRoleBox").getValue());
												}else{
													xmlNode.setAttribute("candidate-groups",Ext.getCmp("delegateRoleBox").getValue());
												}
											}
										}
									}
								}
							},{
								boxLabel: '部门',
								id: 'delegateDeptCheck',
								listeners: {
									"check": function(){
										var flagDept=Ext.getCmp("delegateDeptBox").disabled;
										flagDept?Ext.getCmp("delegateDeptBox").enable():Ext.getCmp("delegateDeptBox").disable();
										flagDept?Ext.getCmp("delegateDeptIdBox").enable():Ext.getCmp("delegateDeptIdBox").disable();
										flagDept?Ext.getCmp("btn_selectDept").enable():Ext.getCmp("btn_selectDept").disable();
										//Ext.getCmp('btn_selectDept').setDisabled(false); 
										if(xmlNode != null){
											if(Ext.getCmp("delegateDeptBox").disabled){
												xmlNode.removeAttribute("candidate-depts");
												xmlNode.setAttribute("candidate-groups",Ext.getCmp("delegateRoleBox").disabled?"":Ext.getCmp("delegateRoleBox").getValue());
												if(xmlNode.getAttribute("candidate-groups") == "")
													xmlNode.removeAttribute("candidate-groups");
											}else{
												xmlNode.setAttribute("candidate-depts",Ext.getCmp("delegateDeptBox").getValue());
												if(xmlNode.getAttribute("candidate-groups")!=""&&xmlNode.getAttribute("candidate-groups")!=null){
													if(Ext.getCmp("delegateDeptBox").getValue() != "")
														xmlNode.setAttribute("candidate-groups",xmlNode.getAttribute("candidate-groups")+","+Ext.getCmp("delegateDeptBox").getValue());
												}else{
													xmlNode.setAttribute("candidate-groups",Ext.getCmp("delegateDeptBox").getValue());
												}
											}
										}
									}
								}
							},{
								boxLabel: '泳道',
								id: 'delegateSwimlaneCheck',
								listeners: {
									"check": function(){
										Ext.getCmp("delegateSwimlaneCombo").disabled?Ext.getCmp("delegateSwimlaneCombo").enable():Ext.getCmp("delegateSwimlaneCombo").disable();
										if(this.checked){
											xmlNode.setAttribute("swimlane",Ext.getCmp("delegateSwimlaneCombo").getValue());
											if(Ext.getCmp("delegateSwimlaneCombo").getValue() != "")
												presrc.lastChild.firstChild.innerHTML = "TaskNode<span title='"+Ext.getCmp("delegateSwimlaneCombo").getValue()+"' class='sign'>泳</span>";
										}else{
											xmlNode.removeAttribute("swimlane");
											presrc.lastChild.firstChild.innerHTML = "TaskNode";
										}
									}
								}
							}]
						},{
							layout: 'form',
							border: false,
							bodyStyle: {background:"#dfe7f4"},
							items: [{
								layout: 'column',
								border: false,
								bodyStyle: {background:"#dfe7f4"},
								items: [{
									layout: 'form',
									height: 50,
									labelWidth: 15,
									bodyStyle: {background:"#dfe7f4"},
									border: false,
									width: 150,
									items:[{
										xtype: 'textfield',
										height: 22,
										disabled: true,
										id: 'variablePartnerBox'
//										width: 150
									},{
										xtype: 'combo',
										id: 'relationPartnerBox',
										width: 120,
										height: 25,
										disabled: true,
										editable: false,
										valueField: 'value',
										displayField: 'text',
										triggerAction: 'all',
										emptyText: '请选择',
										mode: 'local',
										store: variableStore,
										scope: this
										,listeners: {
											"select" : function(combo, record, index) {
															Ext.getCmp("relationPartnerBox")
																	.setValue(record.data.value);
											
															Ext.getCmp("relation_TaskName")
																	.setValue(record.data.text);
																	
											}
										}
									}]					
								},{
									layout: 'form',
									labelWidth: 33,
									bodyStyle: {background:"#dfe7f4"},
									border: false,
									width: 170,
									defaultType: 'combo',
									items:[{
										xtype : 'button',
										id : 'btn_selectVariableParam',
										labelAlign : 'right',
										width : 50,
										text : '参数',
										modal : true,
										disabled: true,
										maximizable : true,
										handler : function() {
											if (variable_win != null) {
												variable_win.show();
											} else {
												variable_win = new Ext.Window({
													id : 'variable_win',
													title : "参数选择",
													width : 600,
													closeAction: 'hide',
													height : 470,
													html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="variableParam_select.html"></iframe>'
												});
												variable_win.show();
											}
										}
									},{
										id: 'relationPartnerTypeCombo',
										fieldLabel: '关系',
										width: 120,
										height: 25,
										disabled: true,
										editable: false,
										valueField: 'value',
										displayField: 'text',
										triggerAction: 'all',
										emptyText: '请选择',
										mode: 'local',
										store: new Ext.data.SimpleStore({
											fields: ['value','text'],
											data:[
												['0','本人'],
												['1','直接上级'],
												['2','间接上级']								
											]
										}),
										scope: this
									}]
									
								},{
									layout: 'form',
									labelWidth: 10,
									bodyStyle: {background:"#dfe7f4"},
									border: false,
									items: [{
										xtype: 'hidden',
										id: 'variable_TaskId',
										width: 150,
										disabled: true
									},{
										xtype: 'hidden',
										id: 'relation_TaskName',
										width: 150,
										disabled: true
									}]
								}]
							}]
							},{
								layout: 'column',
								bodyStyle: {background:"#dfe7f4"},
								border: false,
								width: 400,
								items:[{
									layout: 'form',
									labelWidth: 10,
									bodyStyle: {background:"#dfe7f4"},
									border: false,
									//defaults: {width: '15%'},
									items: [{
										xtype: 'textfield',
										id: 'delegateUserBox',
										width: 350,
										disabled: true
									},{
										xtype: 'textfield',
										id: 'delegateRoleBox',
										width: 350,
										disabled: true
									},{
										xtype: 'textfield',
										id: 'delegateDeptBox',
										width: 350,
										disabled: true
									},{
										xtype: 'combo',
										id: 'delegateSwimlaneCombo',	
										width: 350,
										disabled: true,
										editable: false,
										valueField: 'value',
										displayField: 'text',
										triggerAction: 'all',
										emptyText: '请选择',
										mode: 'local',
										store: new Ext.data.SimpleStore({
											id: 'delegateSwimlaneStore',
											fields: ['value','text'],
											data: []
										}),
										listeners: {
											"select": function(e){
												xmlNode.setAttribute("swimlane",e.getValue());
												presrc.lastChild.firstChild.innerHTML = "TaskNode<span title='"+e.getValue()+"' class='sign'>泳</span>";
											}
										}
									}]}]
								},{
									layout: 'form',
									labelWidth: 10,
									bodyStyle: {background:"#dfe7f4"},
									border: false,
									//defaults: {width: '15%'},
									items: [{
										xtype: 'hidden',
										id: 'delegateUserIdBox',
										width: 150,
										disabled: true
									},{
										xtype: 'hidden',
										id: 'delegateRoleIdBox',
										width: 150,
										disabled: true
									},{
										xtype: 'hidden',
										id: 'delegateDeptIdBox',
										width: 150,
										disabled: true
									}]
								},{
									layout : 'form',
									border : false,
									bodyStyle : {background : "#dfe7f4"},
									items : [{
										xtype : 'button',//用户选择按钮
										id : 'btn_selectUser',
										labelAlign : 'right',
										width : 50,
										text : '选择',
										modal : true,
										maximizable : true,
										disabled: true,
										handler : function() {
											if (user_win != null) {
												user_win.show();
											} else {
												user_win = new Ext.Window({
													id : 'user_win',
													title : "用户选择",
													width : 600,
													closeAction: 'hide',
													height : 470,
													html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="taskDeledate_selectUser.html?tableName=dalegateUser"></iframe>'
												});
												user_win.show();
											}
										}
									},{
										xtype : 'button',//角色选择按钮
										id : 'btn_selectRole',
										labelAlign : 'right',
										width : 50,
										text : '选择',
										modal : true,
										maximizable : true,
										disabled: true,
										handler : function() {
											if (role_win != null) {
												role_win.show();
											} else {
												role_win = new Ext.Window({
													id : 'role_win',
													title : "角色选择",
													width : 600,
													closeAction: 'hide',
													height : 470,
													html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="taskDeledate_selectRole.html?tableName=dalegateRole"></iframe>'
												});
												role_win.show();
											}
										}
									},{
										xtype : 'button',//部门选择按钮
										id : 'btn_selectDept',
										labelAlign : 'right',
										width : 50,
										text : '选择',
										modal : true,
										maximizable : true,
										disabled: true,
										handler : function() {
											if (dept_win != null) {
												dept_win.show();
											} else {
												dept_win = new Ext.Window({
													id : 'dept_win',
													title : "部门选择",
													width : 600,
													closeAction: 'hide',
													height : 470,
													html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="taskDeledate_selectDept.html?tableName=dalegateDept"></iframe>'
												});
												dept_win.show();
											}
										}
//									}]
//								}]
							}]
						}]							
					}]
				});
	return current_Card;
}

function fillBackDelegateData(data,tableName){
	if( tableName == "dalegateUser" ){
		var frealname = data.frealname;
		Ext.getCmp("delegateUserIdBox").setValue(data.fid);
		Ext.getCmp("delegateUserBox").setValue(frealname);
		user_win.close();
		user_win = null;
	}else if( tableName == "dalegateDept" ){
		var faliasname = data.faliasname;
		Ext.getCmp("delegateDeptIdBox").setValue(data.fid);
		Ext.getCmp("delegateDeptBox").setValue(faliasname);
		dept_win.close();
		dept_win = null;
	}else if( tableName == "dalegateRole" ){
		var frolename = data.frolename;
		Ext.getCmp("delegateRoleIdBox").setValue(data.fid);
		Ext.getCmp("delegateRoleBox").setValue(frolename);
		role_win.close();
		role_win = null;
	}
}

function hideDelegateWin(tableName) {
	if( tableName == "dalegateUser" ){
		user_win.close();
		user_win = null;
	}else if( tableName == "dalegateDept" ){
		dept_win.close();
		dept_win =null;
	}else if( tableName == "dalegateRole" ){
		role_win.close();
		role_win = null;
	}
}

//选择表单 返填数据，并查询改表单可配置工作流的action
function fillBackVariableParamData(data) {
	var ParamName = data.ParamName;
	var variableTaskName = data.variableTaskName;
	var variableTaskId = data.variableTaskId;
	
	
	Ext.getCmp("variable_TaskId").setValue(variableTaskId);
	
	Ext.getCmp("variablePartnerBox").setValue(ParamName);
	
	variable_win.close();
	variable_win=null;
}

//关闭窗口
function hideDelegateParamWin() {
	variable_win.close();
	variable_win=null;
}

//获得
function getTempDataXmlParam(){
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
		
		//计算此form属于哪个任务节点所定义
		var ftaskid=form.parentNode.parentNode.tagName;//所属节点的id
		
		var node=findXmlNodeById("task",ftaskid);//所属节点的对象
		
		var ftaskname=node.getAttribute("name");//所属节点的名称
		
		arr[3]=ftaskname;
		arr[4]=ftaskid;
		
		jsonarr.push(arr);
	}
	return jsonarr;
}


//


