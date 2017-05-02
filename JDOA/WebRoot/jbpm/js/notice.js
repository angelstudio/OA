function notice(currentParam){
	currentCard = new Ext.form.FormPanel({
					id: currentParam,
					border: false,
					bodyStyle: {background:"#dfe7f4",padding:"10 0 0 20"},
					labelAlign: 'left',									
					items: [{
						layout: 'column',
						border: false,
						bodyStyle: {background:"#dfe7f4"},
						items: [{
							layout: 'fit',
							width: 180,
							height: 350,
							border: false,
							items: [{
								xtype: 'panel',
								id: 'noticeListPanel',
								html: '<div class="panel_list" id="noticeList"></div>',
								tbar: new Ext.Toolbar({
									height: 31,
									border: false,
									items: [
										'->',{
											text : '&nbsp;新增',
											iconCls : 'picon23',
											handler : function() {
												removeAllClass("notice");
												clearNoticePage();
											}
										}, '-',
										{text: '&nbsp;添加',iconCls: 'picon23',handler:function(){
											if(Ext.getCmp("noticeDescriptionBox").getValue() == ""){
												Ext.MessageBox.alert(tip,'消息描述不能为空！');
												return false;
											}
											//接收所有的参数
											//消息描述
											var noticeDescription_value=Ext.getCmp("noticeDescriptionBox").getValue();
											//事件类型
											var noticeCaseType_value=Ext.getCmp("noticeCaseTypeCombo").getValue();
											//流程发起者
											var noticeSponsor_value="";//选中了赋值，没有选中的就为空字符串
											if(Ext.getCmp("noticeSponsorCheck").getValue()){//如果流程发起者为true，也就是选中了
												noticeSponsor_value=Ext.getCmp("noticeSponsorCombo").getValue();
											}
											//流程执行者
											var executor_value = "";//临时变量
											var executor_type = "";//对应的类型
											var executor_node = "";//判定选中类型的值，因为关系到节点的值
											if(Ext.getCmp("noticeExecutorCheck").getValue()){//如果流程发起者为true，也就是选中了
												executor_value = Ext.getCmp("noticeExecutorCombo").getValue();//是否包含或出外
												executor_type = Ext.getCmp("noticeExecutorTypeCombo").getValue();
												if( executor_type == 2 && !Ext.getCmp("noticeExecutorTypeCombo").disabled ){
													executor_node = Ext.getCmp("noticeExecutorAreaCombo").getValue();
												}
											}
											
											//流程参与者
											var partner_value = "";//临时变量
											var partner_type = "";//对应的类型
											var partner_node = "";//判定选中类型的值，因为关系到节点的值
											if(Ext.getCmp("noticePartnerCheck").getValue()){//如果流程发起者为true，也就是选中了
												partner_value = Ext.getCmp("noticePartnerCombo").getValue();//是否包含或出外
												partner_type = Ext.getCmp("noticePartnerTypeCombo").getValue();
												if( partner_type == 2 && !Ext.getCmp("noticePartnerTypeCombo").disabled ){
													partner_node=Ext.getCmp("noticePartnerAreaCombo").getValue();
												}
											}
											//用户
											var noticeuser_value="";
											var noticeuserId_value="";
											if (!Ext.getCmp("noticeUserBox").disabled) {
												noticeuser_value=Ext.getCmp("noticeUserBox").getValue();
												noticeuserId_value=Ext.getCmp("noticeUserIdBox").getValue();
											}
											//角色
											var noticerole_value="";
											var noticeroleId_value="";
											if (!Ext.getCmp("noticeRoleBox").disabled) {
												noticerole_value=Ext.getCmp("noticeRoleBox").getValue();
												noticeroleId_value=Ext.getCmp("noticeRoleIdBox").getValue();
											}
											//部门
											var noticedept_value="";
											var noticedeptId_value="";
											if (!Ext.getCmp("noticeDeptBox").disabled) {
												noticedept_value=Ext.getCmp("noticeDeptBox").getValue();
												noticedeptId_value=Ext.getCmp("noticeDeptIdBox").getValue();
											}
											//消息内容
											var noticeContext_value=Ext.getCmp("noticeContentBox").getValue();
																						
											
											//notices节点
											var elementDoc = getParentNode("notice");
											var newNode;//
											//使用回填的话，那么就是使用已有的数据
											if(selectNodeId!=null){
												for(var i=0;i<elementDoc.childNodes.length;i++){//遍历所有子节点
													if(elementDoc.childNodes[i].getAttribute("id")==selectNodeId){
														newNode=elementDoc.childNodes[i];
														RemoveAllChildNodes(newNode);//因为是修改，那么就删除所有的子节点
														Ext.MessageBox.alert("成功","修改成功");
														clearNoticePage();
														break;
													}
												}
											}else{
												newNode = tempDataXml.createElement("notice");
												addXmlAttribute(newNode, "id", xmlNode.getAttribute("id")+"-notice-"+noticeNum);
												addXmlAttribute(newNode, "name", xmlNode.getAttribute("name")+"-notice-"+noticeDescription_value);
												//添加数据到左边的树结构中
												if( !isOrNotExistByNodeName("notice",xmlNode.getAttribute("name")+"-notice-"+noticeDescription_value) ){
													//移除事件
													Ext.get(Ext.DomQuery.select(".notice_btn")).un("click");
													//添加树列表
													var noticelineId = xmlNode.getAttribute("id")+"-notice-"+ noticeNum;
													
													addTreeList(Ext.getDom("noticeList"),noticelineId,xmlNode.getAttribute("name")+"-notice-"+noticeDescription_value,"notice");
													//触发事件
													Ext.getDom(noticelineId).fireEvent('onclick');
													Ext.MessageBox.alert("成功","添加成功");
													clearNoticePage();
												}else{
													Ext.MessageBox.alert("提示","消息描述不能重复");
													return false;
												}
											}
											//消息描述节点
											var noticeDesc = tempDataXml.createElement("noticeDesc");
											addXmlAttribute(noticeDesc, "value", noticeDescription_value);
											//事件类型
											var eventType = tempDataXml.createElement("eventType");
											addXmlAttribute(eventType, "value", noticeCaseType_value);
											//接收者
											var recipient = tempDataXml.createElement("recipient");
											//1、发起者
											var sponsor = tempDataXml.createElement("sponsor");
											addXmlAttribute(sponsor, "value", noticeSponsor_value);
											//2、执行者
											var executor = tempDataXml.createElement("executor");
											//2.1、临时变量   （包含或除外）
											var executorValue = tempDataXml.createElement("executor_value");
											addXmlAttribute(executorValue, "value", executor_value);
											//2.2、对应类型
											var executorType = tempDataXml.createElement("executor_type");
											addXmlAttribute(executorType, "value", executor_type);
											//2.3、节点
											var executorNode = tempDataXml.createElement("executor_node");
											addXmlAttribute(executorNode, "value", executor_node);
											
											executor.appendChild(executorValue);
											executor.appendChild(executorType);
											executor.appendChild(executorNode);
											
											//3、参与者
											var partner = tempDataXml.createElement("partner");
											//3.1、临时变量   （包含或除外）
											var partnerValue = tempDataXml.createElement("partner_value");
											addXmlAttribute(partnerValue, "value", partner_value);
											//2.2、对应类型
											var partnerType = tempDataXml.createElement("partner_type");
											addXmlAttribute(partnerType, "value", partner_type);
											//2.3、节点
											var partnerNode = tempDataXml.createElement("partner_node");
											addXmlAttribute(partnerNode, "value", partner_node);
											
											partner.appendChild(partnerValue);
											partner.appendChild(partnerType);
											partner.appendChild(partnerNode);
											
											//4、用户
											var noticeUser = tempDataXml.createElement("noticeUser");
											addXmlAttribute(noticeUser, "value", noticeuser_value);
											addXmlAttribute(noticeUser, "id", noticeuserId_value);
											//5、角色
											var noticeRole = tempDataXml.createElement("noticeRole");
											addXmlAttribute(noticeRole, "value", noticerole_value);
											addXmlAttribute(noticeRole, "id", noticeroleId_value);
											//6、部门
											var noticeDept = tempDataXml.createElement("noticeDept");
											addXmlAttribute(noticeDept, "value", noticedept_value);
											addXmlAttribute(noticeDept, "id", noticedeptId_value);
											
											recipient.appendChild(sponsor);
											recipient.appendChild(executor);
											recipient.appendChild(partner);
											recipient.appendChild(noticeUser);
											recipient.appendChild(noticeRole);
											recipient.appendChild(noticeDept);
											
											//消息内容
											var noticeContext = tempDataXml.createElement("noticeContext");
											addXmlAttribute(noticeContext, "value", noticeContext_value);
											
											newNode.appendChild(noticeDesc);
											newNode.appendChild(eventType);
											newNode.appendChild(recipient);
											newNode.appendChild(noticeContext);
											
											elementDoc.appendChild(newNode);
											
											noticeNum ++;
											removeAllClass("notice");
										}},'-',
										{text: '&nbsp;删除',iconCls: 'picon13',handler: function(){
												deleteXmlNodeByType("notice");
												//removeAllClass("notice");
										}}
									]
								})
							}]
						},{
							border: false,
							bodyStyle: {background:"#dfe7f4",padding:"0 0 0 10"},
							items: [{
								layout: 'form',
								bodyStyle: {background:"#dfe7f4"},
								border: false,
								labelWidth: 80,
								items: [{
									id: 'noticeDescriptionBox',
									xtype: 'textfield',
									width: '85%',
									fieldLabel: '消息描述'
								},{
									xtype: 'combo',
									fieldLabel: '事件类型',
									id: 'noticeCaseTypeCombo',
									width: 100,
									editable: false,
									valueField: 'value',
									displayField: 'text',
									triggerAction: 'all',
									emptyText: '请选择',
									mode: 'local',
									store: new Ext.data.SimpleStore({
										fields: ['value','text'],
										data:[
											['1','流程启动'],
											['2','流程结束']								
										]
									})
								}]
							},{
								bodyStyle: {background:"#dfe7f4"},
								border: false,
								layout: 'column',	
								items: [{
									width: 170,
									labelWidth: 80,
									border: false,
									layout: 'form',
									defaults: {height: 22},
									bodyStyle: {background:"#dfe7f4"},
									defaultType: 'checkbox',
									items: [{
										id: 'noticeSponsorCheck',
										fieldLabel: '接收者',
										boxLabel: '流程发起者',
										listeners: {
											"check": function(){
												Ext.getCmp("noticeSponsorCombo").disabled?Ext.getCmp("noticeSponsorCombo").enable():Ext.getCmp("noticeSponsorCombo").disable();
											}
										}	
									},{
										id: 'noticeExecutorCheck',
										boxLabel: '流程执行者',
										listeners: {
											"check": function(){
												Ext.getCmp("noticeExecutorCombo").disabled?Ext.getCmp("noticeExecutorCombo").enable():Ext.getCmp("noticeExecutorCombo").disable();
												Ext.getCmp("noticeExecutorTypeCombo").disabled?Ext.getCmp("noticeExecutorTypeCombo").enable():Ext.getCmp("noticeExecutorTypeCombo").disable();
												if(Ext.getCmp("noticeExecutorTypeCombo").getValue() == 2 && !Ext.getCmp("noticeExecutorTypeCombo").disabled)
													Ext.getCmp("noticeExecutorAreaCombo").enable();
												else
													Ext.getCmp("noticeExecutorAreaCombo").disable();
											}
										}	
									},{
										id: 'noticePartnerCheck',
										boxLabel: '流程参与者',
										listeners: {
											"check": function(){
												Ext.getCmp("noticePartnerCombo").disabled?Ext.getCmp("noticePartnerCombo").enable():Ext.getCmp("noticePartnerCombo").disable();
												Ext.getCmp("noticePartnerTypeCombo").disabled?Ext.getCmp("noticePartnerTypeCombo").enable():Ext.getCmp("noticePartnerTypeCombo").disable();
												if(Ext.getCmp("noticePartnerTypeCombo").getValue() == 2 && !Ext.getCmp("noticePartnerTypeCombo").disabled)
													Ext.getCmp("noticePartnerAreaCombo").enable();
												else
													Ext.getCmp("noticePartnerAreaCombo").disable();
											}
										}	
									},{
										boxLabel: '用户',
										id: 'noticeUserCheck',
										listeners: {
											"check": function(){
												var noticeFlagUser=Ext.getCmp("noticeUserBox").disabled;
												noticeFlagUser?Ext.getCmp("noticeUserBox").enable():Ext.getCmp("noticeUserBox").disable();
												noticeFlagUser?Ext.getCmp("btn_noticeUser").enable():Ext.getCmp("btn_noticeUser").disable();
												var node = findXmlNodeByName("notice",Ext.getCmp("noticeDescriptionBox").getValue());
												if(node != null){
													if(Ext.getCmp("noticeUserBox").disabled){
														node.removeAttribute("candidate-users");
													}else{
														node.setAttribute("candidate-users",Ext.getCmp("noticeUserBox").getValue());
													}
												}
											}
										}	
									},{
										boxLabel: '角色',
										id: 'noticeRoleCheck',
										listeners: {
											"check": function(){
												var noticeFlagRole=Ext.getCmp("noticeRoleBox").disabled;
												noticeFlagRole?Ext.getCmp("noticeRoleBox").enable():Ext.getCmp("noticeRoleBox").disable();
												noticeFlagRole?Ext.getCmp("btn_noticeRole").enable():Ext.getCmp("btn_noticeRole").disable();
												var node = findXmlNodeByName("notice",Ext.getCmp("noticeDescriptionBox").getValue());
												if(node != null){
													if(Ext.getCmp("noticeRoleBox").disabled){
														node.removeAttribute("candidate-roles");
														node.setAttribute("candidate-groups",Ext.getCmp("noticeDeptBox").disabled?"":Ext.getCmp("noticeDeptBox").getValue());
														if(node.getAttribute("candidate-groups") == "")
															node.removeAttribute("candidate-groups");
													}else{
														node.setAttribute("candidate-roles",Ext.getCmp("noticeRoleBox").getValue());
														if(node.getAttribute("candidate-groups")!=""&&node.getAttribute("candidate-groups")!=null){
															if(Ext.getCmp("noticeRoleBox").getValue() != "")
																node.setAttribute("candidate-groups",node.getAttribute("candidate-groups")+","+Ext.getCmp("noticeRoleBox").getValue());
														}else{
															node.setAttribute("candidate-groups",Ext.getCmp("noticeRoleBox").getValue());
														}
													}
												}
											}
										}
									},{
										boxLabel: '部门',
										id: 'noticeDeptCheck',
										listeners: {
											"check": function(){
												var noticeFlagDept=Ext.getCmp("noticeDeptBox").disabled;
												noticeFlagDept?Ext.getCmp("noticeDeptBox").enable():Ext.getCmp("noticeDeptBox").disable();
												noticeFlagDept?Ext.getCmp("btn_noticeDept").enable():Ext.getCmp("btn_noticeDept").disable();
												var node = findXmlNodeByName("notice",Ext.getCmp("noticeDescriptionBox").getValue());
												if(node != null){
													if(Ext.getCmp("noticeDeptBox").disabled){
														node.removeAttribute("candidate-depts");
														node.setAttribute("candidate-groups",Ext.getCmp("noticeRoleBox").disabled?"":Ext.getCmp("noticeRoleBox").getValue());
														if(node.getAttribute("candidate-groups") == "")
															node.removeAttribute("candidate-groups");
													}else{
														node.setAttribute("candidate-depts",Ext.getCmp("noticeDeptBox").getValue());
														if(node.getAttribute("candidate-groups")!=""&&node.getAttribute("candidate-groups")!=null){
															if(Ext.getCmp("noticeRoleBox").getValue() != "")
																node.setAttribute("candidate-groups",node.getAttribute("candidate-groups")+","+Ext.getCmp("noticeDeptBox").getValue());
														}else{
															node.setAttribute("candidate-groups",Ext.getCmp("noticeDeptBox").getValue());
														}
													}
												}
											}
										}
									}]
								},{
									layout: 'form',
									labelWidth: 10,
									bodyStyle: {background:"#dfe7f4"},
									border: false,
									//defaults: {width: '83%'},
									items: [{

										layout: 'column',
										border: false,
										bodyStyle: {background:"#dfe7f4"},
										items: [{
											layout: 'form',
											labelWidth: 10,
											bodyStyle: {background:"#dfe7f4"},
											border: false,
											width: 130,
											defaultType: 'combo',
											items:[{
												id: 'noticeSponsorCombo',
												width: 100,
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
														['1','包含'],
														['2','除外']								
													]
												}),
												scope: this,
												listeners: {
													"beforerender": function(){
														this.setValue(1);
													}
												}
											},{
												id: 'noticeExecutorCombo',
												width: 100,
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
														['1','包含'],
														['2','除外']								
													]
												}),
												scope: this,
												listeners: {
													"beforerender": function(){
														this.setValue(1);
													}
												}
											},{
												id: 'noticePartnerCombo',
												width: 100,
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
														['1','包含'],
														['2','除外']								
													]
												}),
												scope: this,
												listeners: {
													"beforerender": function(){
														this.setValue(1);
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
												xtype: 'label',
												height: 25
											},{
												id: 'noticeExecutorTypeCombo',
												fieldLabel: '类型',
												width: 120,
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
														['1','整个流程执行者'],
														['2','某个节点执行者']								
													]
												}),
												scope: this,
												listeners: {
													"select": function(){
														if(this.getValue() == 1){
															Ext.getCmp("noticeExecutorAreaCombo").disable();
														}else{
															//获取所有XML任务节点
															var tasks = xml.getElementsByTagName("task");
															var arrays = [];
															for(var i=0;i<tasks.length;i++){
																arrays.push([tasks[i].getAttribute("name"),tasks[i].getAttribute("name")]);
															}
															var store = new Ext.data.SimpleStore({
																fields: ['value','text'],
																data: arrays
															});
															Ext.getCmp("noticeExecutorAreaCombo").store = store;
															Ext.getCmp("noticeExecutorAreaCombo").enable();
														}
													},
													"beforerender": function(){
														this.setValue(1);								
													}
												}
											},{
												id: 'noticePartnerTypeCombo',
												fieldLabel: '类型',
												width: 120,
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
														['1','整个流程参与者'],
														['2','某个节点参与者']								
													]
												}),
												scope: this,
												listeners: {
													"select": function(){
														if(this.getValue() == 1){
															Ext.getCmp("noticePartnerAreaCombo").disable();
														}else{
															//获取所有XML任务节点
															var tasks = xml.getElementsByTagName("task");
															var arrays = [];
															for(var i=0;i<tasks.length;i++){
																arrays.push([tasks[i].getAttribute("name"),tasks[i].getAttribute("name")]);
															}
															var store = new Ext.data.SimpleStore({
																fields: ['value','text'],
																data: arrays
															});
															Ext.getCmp("noticePartnerAreaCombo").store = store;
															Ext.getCmp("noticePartnerAreaCombo").enable();
														}
													},
													"beforerender": function(){
														this.setValue(1);								
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
												xtype: 'label',
												height: 25
											},{
												id: 'noticeExecutorAreaCombo',
												fieldLabel: '节点',
												width: 120,
												disabled: true,
												editable: false,
												valueField: 'value',
												displayField: 'text',
												triggerAction: 'all',
												emptyText: '请选择',
												mode: 'local'	
											},{
												id: 'noticePartnerAreaCombo',
												fieldLabel: '节点',
												width: 120,
												disabled: true,
												editable: false,
												valueField: 'value',
												displayField: 'text',
												triggerAction: 'all',
												emptyText: '请选择',
												mode: 'local'	
											}]				
										}]
									},{
										layout: 'form',
										border: false,
										bodyStyle: {background:"#dfe7f4"},
										items: [{
											layout: 'column',
											bodyStyle: {background:"#dfe7f4"},
											border: false,
											//defaults: {width: '83%'},
											items: [{
												xtype: 'textfield',
												id: 'noticeUserBox',
												width: 400,
												disabled: true
											},{
												xtype: 'hidden',
												id: 'noticeUserIdBox',
												width: 150,
												disabled: true
											},{

												xtype : 'button',//用户选择按钮
												id : 'btn_noticeUser',
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
															html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="../wf/jsp/taskDeledate_selectUser.html?tableName=noticeUser"></iframe>'
														});
														user_win.show();
													}
												}
											}]
										},{
											layout: 'column',
											bodyStyle: {background:"#dfe7f4"},
											border: false,
											items: [{
												xtype: 'textfield',
												id: 'noticeRoleBox',
												width: 400,
												disabled: true
											},{
												xtype: 'hidden',
												id: 'noticeRoleIdBox',
												width: 150,
												disabled: true
											},{
												xtype : 'button',//角色选择按钮
												id : 'btn_noticeRole',
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
															html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="../wf/jsp/taskDeledate_selectRole.html?tableName=noticeRole"></iframe>'
														});
														role_win.show();
													}
												}
											}]
										},{
											layout: 'column',
											bodyStyle: {background:"#dfe7f4"},
											border: false,
											items: [{xtype: 'textfield',
												id: 'noticeDeptBox',
												width: 400,
												disabled: true
											},{
												xtype: 'hidden',
												id: 'noticeDeptIdBox',
												width: 150,
												disabled: true
											},{
												xtype : 'button',//部门选择按钮
												id : 'btn_noticeDept',
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
															html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="../wf/jsp/taskDeledate_selectDept.html?tableName=noticeDept"></iframe>'
														});
														dept_win.show();
													}
												}
											}]
										}]
									}]
								}]
							},{
								layout: 'form',
								bodyStyle: {background:"#dfe7f4"},
								border: false,
								id : 'fatherNoticeContext',
								labelWidth: 80,
								items: [{
									xtype: 'htmleditor',
								    enableColors: false,
								    enableAlignments: false,
									fieldLabel: '消息内容',
									id: 'noticeContentBox',
									width: '85%'
								}]
							}]
						}]
					}]
				});		
		return currentCard;
}


function fillBackNoticeData(data,tableName){
	if( tableName == "noticeUser" ){
		var frealname = data.frealname;
		Ext.getCmp("noticeUserIdBox").setValue(data.fid);
		Ext.getCmp("noticeUserBox").setValue(frealname);
		user_win.close();
		user_win = null;
	}else if( tableName == "noticeDept" ){
		var faliasname = data.faliasname;
		Ext.getCmp("noticeDeptIdBox").setValue(data.fid);
		Ext.getCmp("noticeDeptBox").setValue(faliasname);
		dept_win.close();
		dept_win = null;
	}else if( tableName == "noticeRole" ){
		var frolename = data.frolename;
		Ext.getCmp("noticeRoleIdBox").setValue(data.fid);
		Ext.getCmp("noticeRoleBox").setValue(frolename);
		role_win.close();
		role_win = null;
	}
}

function hideNoticeWin(tableName) {
	if( tableName == "noticeUser" ){
		user_win.close();
		user_win = null;
	}else if( tableName == "noticeDept" ){
		dept_win.close();
		dept_win =null;
	}else if( tableName == "noticeRole" ){
		role_win.close();
		role_win = null;
	}
}
