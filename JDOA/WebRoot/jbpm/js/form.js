function getFormPanel(currentParam) {
	currentCard = new Ext.form.FormPanel({
		id : currentParam,
		border : false,
		bodyStyle : {
			background : "#dfe7f4",
			padding : "10 0 0 20"
		},
		labelAlign : 'left',
		items : [{
			layout : 'column',
			border : false,
			bodyStyle : {
				background : "#dfe7f4"
			},
			items : [{
				layout : 'fit',
				width : 180,
				height : 350,
				border : false,
				items : [{
					xtype : 'panel',
					id : 'formListPanel',
					html : '<div class="panel_list" id="formList"></div>',
					tbar : new Ext.Toolbar({
						height : 31,
						border : false,
						items : ['->', {
									text : '&nbsp;新增',
									iconCls : 'picon23',
									handler : function() {
										removeAllClass("form");
										// 清空参数名称
										Ext.getCmp("sqlParamNameBox1")
												.setValue("");
										// 清空参数类型
										Ext.getCmp("sqlParamTypeCombo1")
												.setValue("");
										// 清空参数值
										Ext.getCmp("sqlParamValueBox1")
												.setValue("");
										//清空功能值
										Ext.getCmp("facadeCombo").setValue("");
									}
								}, '-', {
									text : '&nbsp;添加',
									iconCls : 'picon23',
									handler : function() {
										// 验证参数名称不能为空
										if (Ext.getCmp("sqlParamNameBox1")
												.getValue() == "") {
											Ext.MessageBox.alert(tip,
													'参数名称不能为空！');
											return false;
										}
										if (findXmlNodeByName("form",
												Ext.getCmp("sqlParamNameBox1")
														.getValue()) != null) {
											Ext.MessageBox.alert(tip,
													'参数名称不能重复!');
											return false;
										}
										
										//判断参数名称不能重复
										if (hasExistParamName(Ext.getCmp("sqlParamNameBox1").getValue())) {
											Ext.MessageBox.alert(tip,
													'参数名称不能重复!');
											return false;
										}
										
										
										// 验证任务不能为空
										if (Ext.getCmp("billFormBox")
												.getValue() == "") {
											Ext.MessageBox.alert(tip,
													'任务值不能为空！');
											return false;
										}

										var forms = getParentNode("form");// 获得forms节点

										var newNode;//
										// 使用回填的话，那么就是使用已有的数据
										if (selectNodeId != null) {
											for (var i = 0; i < forms.childNodes.length; i++) {// 遍历所有子节点
												if (forms.childNodes[i]
														.getAttribute("id") == selectNodeId) {
													newNode = forms.childNodes[i];
													RemoveAllChildNodes(newNode);// 因为是修改，那么就删除所有的子节点
													Ext.MessageBox.alert("成功","修改成功");
													break;
												}
											}
										} else {
											newNode = tempDataXml
													.createElement("form");
											addXmlAttribute(
													newNode,
													"id",
													xmlNode.getAttribute("name") + "-form-"
															+ formNum);
											addXmlAttribute(
													newNode,
													"name",
													xmlNode.getAttribute("name") + "-form-"
															+ formNum);
											// 添加数据到左边的树结构中
											Ext.MessageBox.alert("成功","添加成功");
											// 移除事件
											Ext.get(Ext.DomQuery
													.select(".form_btn"))
													.un("click");
											// 添加树列表
											var formId = xmlNode.getAttribute("name") + "-form-" + formNum;

											addTreeList(Ext.getDom("formList"),
													formId, formId, "form");
											// 触发事件
											Ext.getDom(formId)
													.fireEvent('onclick');
										}

										// 创建新的节点
										var billFormBox_node = tempDataXml
												.createElement("billFormBox");// 创建任务节点
										var facadeCombo_node = tempDataXml
												.createElement("facadeCombo");// 创建功能节点
										var sqlParamNameBox1_node = tempDataXml
												.createElement("sqlParamNameBox1");// 创建参数名称节点
										var sqlParamTypeCombo1_node = tempDataXml
												.createElement("sqlParamTypeCombo1");// 创建参数类型节点
										var sqlParamValueBox1_node = tempDataXml
												.createElement("sqlParamValueBox1");// 创建参数值节点
										var factionname_node = tempDataXml
												.createElement("factionname");// 功能对应的actionname
										var factiontype_node = tempDataXml
												.createElement("factiontype");// 功能对应的actionname

										// 得到文本框的值
										var billFormBox = Ext
												.getCmp("billFormBox")
												.getValue();// 任务
										var facadeCombo = Ext
												.getCmp("facadeCombo")
												.getValue();// 功能
										var sqlParamNameBox1 = Ext
												.getCmp("sqlParamNameBox1")
												.getValue();// 参数名称
										var sqlParamTypeCombo1 = Ext
												.getCmp("sqlParamTypeCombo1")
												.getValue();// 参数类型
										var sqlParamValueBox1 = Ext
												.getCmp("sqlParamValueBox1")
												.getValue();// 参数值
										var factionname = Ext
												.getCmp("factionname")
												.getValue();// action值
										var factiontype = Ext
												.getCmp("factiontype")
												.getValue();// type值

										// 把文本框中相应的值写入xml中
										addXmlAttribute(billFormBox_node,
												"value", billFormBox);// 给节点添加一个属性及属性值
										addXmlAttribute(facadeCombo_node,
												"value", facadeCombo);// 给节点添加一个属性及属性值
										addXmlAttribute(sqlParamNameBox1_node,
												"value", sqlParamNameBox1);// 给节点添加一个属性及属性值
										addXmlAttribute(
												sqlParamTypeCombo1_node,
												"value", sqlParamTypeCombo1);// 给节点添加一个属性及属性值
										addXmlAttribute(sqlParamValueBox1_node,
												"value", sqlParamValueBox1);// 给节点添加一个属性及属性值
										addXmlAttribute(factionname_node,
												"value", factionname);// 给节点添加一个属性及属性值
										addXmlAttribute(factiontype_node,
												"value", factiontype);// 给节点添加一个属性及属性值

										newNode.appendChild(billFormBox_node);
										newNode.appendChild(facadeCombo_node);
										newNode
												.appendChild(sqlParamNameBox1_node);
										newNode
												.appendChild(sqlParamTypeCombo1_node);
										newNode
												.appendChild(sqlParamValueBox1_node);
										newNode.appendChild(factionname_node);
										newNode.appendChild(factiontype_node);

										if (selectNodeId == null) {
											forms.appendChild(newNode);
										}

										formNum++;
										removeAllClass("form");
										clearFormPage();

										// 如果forms含有子节点，那么就会设置不可编辑
										if (forms.hasChildNodes()) {
											Ext
													.getCmp("billFormBox")
													.setValue(forms.childNodes[0].childNodes[0]
															.getAttribute("value"));
											Ext.getCmp("billFormBox").disable();// 设置任务为不可编辑
											Ext.getCmp("btn_selectTask")
													.disable();// 设置按钮为不可编辑
										}
										
										var taskValue = tempDataXml.documentElement.getElementsByTagName("taskvalue");
										if(taskValue.length<=0){
											var taskValue_node = tempDataXml.createElement("taskvalue");// 创建任务节点
											var tasktype_node = tempDataXml.createElement("tasktype");// 创建任务节点
											taskValue_node.setAttribute("value",billFormBox);
											tasktype_node.setAttribute("value",factiontype);
											tempDataXml.documentElement.appendChild(taskValue_node);
											tempDataXml.documentElement.appendChild(tasktype_node);
										}
										
									}
								}, '-', {
									text : '&nbsp;删除',
									iconCls : 'picon13',
									handler : function() {
										deleteXmlNodeByType("form", "");
									}
								}]
					})
				}]
			}, {
				border : false,
				bodyStyle : {
					background : "#dfe7f4",
					padding : "0 0 0 10"
				},
				items : [{
					layout : 'form',
					labelWidth : 80,
					defaults : {
						width : '85%'
					},
					border : false,
					bodyStyle : {
						background : "#dfe7f4"
					},
					items : [{
						layout : 'column',
						bodyStyle : {
							background : "#dfe7f4"
						},
						border : false,
						items : [{
									layout : 'form',
									border : false,
									width : 300,
									bodyStyle : {
										background : "#dfe7f4"
									},
									items : [{
												xtype : 'textfield',
												fieldLabel : '任务',
												id : 'billFormBox'
											}]
								}, {
									layout : 'form',
									border : false,
									bodyStyle : {
										background : "#dfe7f4"
									},
									items : [{
										xtype : 'button',
										id : 'btn_selectTask',
										labelAlign : 'right',
										width : 50,
										text : '选择',
										modal : true,
										maximizable : true,
										handler : function() {
											if (task_win != null) {
												task_win.show();
											} else {
												task_win = new Ext.Window({
													id : 'task_win',
													title : "任务选择",
													width : 600,
													closeAction : 'hide',
													height : 470,
													html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="task_select.html"></iframe>'
												});
												task_win.show();
											}
										}
									}]
								}]
					}, {
						layout : 'column',
						bodyStyle : {
							background : "#dfe7f4"
						},
						border : false,
						items : [{
							layout : 'form',
							border : false,
							width : 300,
							bodyStyle : {
								background : "#dfe7f4"
							},
							items : [{
								xtype : 'combo',
								fieldLabel : '功能',
								id : 'facadeCombo',
								autoLoad :true,
								displayField : 'text',
								triggerAction : 'all',
								emptyText : '请选择',
								mode : 'local',
								store : typeStore,
								listeners : {
									"select" : function(combo, record, index) {
										Ext.getCmp("factionname")
												.setValue(record.data.value);
									}
								}
							}, {
								layout: 'form',
								labelWidth: 10,
								bodyStyle: {background:"#dfe7f4"},
								border: false,
								items : [{
											xtype : 'hidden',
											id : 'factionname',
											width : 150,
											disabled : true
										},{
											xtype: 'hidden',
											id: 'factiontype',
											width: 150,
											disabled: true
										}]
							}]
						}]
					}, {
						layout : 'column',
						border : false,
						bodyStyle : {
							background : "#dfe7f4",
							padding : "0 0 0 88"
						},
						items : [{
									layout : 'form',
									border : false,
									width : 175,
									bodyStyle : {
										background : "#dfe7f4"
									},
									labelWidth : 65,
									defaultType : 'textfield',
									items : [{
												id : 'sqlParamNameBox1',
												fieldLabel : '参数名称',
												width : 100
											}]
								}, {
									layout : 'form',
									border : false,
									width : 175,
									bodyStyle : {
										background : "#dfe7f4"
									},
									labelWidth : 65,
									defaultType : 'combo',
									items : [{
										fieldLabel : '参数类型',
										id : 'sqlParamTypeCombo1',
										width : 100,
										editable : false,
										valueField : 'value',
										displayField : 'text',
										triggerAction : 'all',
										emptyText : '请选择',
										mode : 'local',
										store : new Ext.data.SimpleStore({
													fields : ['value', 'text'],
													data : [['1', '字符串'],
															['2', '数字型'],
															['3', '浮点型'],
															['4', '布尔型']]
												})
									}]
								}, {
									layout : 'form',
									border : false,
									width : 165,
									bodyStyle : {
										background : "#dfe7f4"
									},
									labelWidth : 55,
									defaultType : 'textfield',
									items : [{
												id : 'sqlParamValueBox1',
												fieldLabel : '参数值',
												width : 100
											}]
								}, {
									layout : 'form',
									border : false,
									width : 80,
									bodyStyle : {
										background : "#dfe7f4"
									},
									items : [{
										xtype : 'button',
										id : 'btn_selectRefer',
										labelAlign : 'left',
										width : 80,
										text : '变量引用',
										modal : true,
										maximizable : true,
										handler : function() {
//											var task_val = window.parent.Ext
//													.getCmp("billFormBox")
//													.getValue();
//											if (task_val == '') {
//												Ext.MessageBox.alert(tip,
//														'请先选择任务！');
//												return false;
//											}
											if (refer_win != null) {
												refer_win.show();
											} else {
												refer_win = new Ext.Window({
													id : 'refer_win',
													title : "变量引用",
													width : 600,
													closeAction : 'hide',
													height : 470,
													html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="refer_select.html"></iframe>'
												});
												refer_win.show();
											}
										}
									}]
								}]
					}]
				}]
			}]
		}]
	});
	
	return currentCard;
}

// 选择表单 返填数据，并查询改表单可配置工作流的action
function fillBackReferData(data) {
	var fname = data.fname;
	var aliasName = data.aliasname;
	var ftype = data.ftype;
	var type = data.ftype;
	var flength = data.flength;
	var fdefualtvalue = data.fdefualtvalue;
	Ext.getCmp("sqlParamValueBox1").setValue(fname);
	refer_win.close();
	refer_win = null;
}

function hideReferWin() {
	refer_win.close();
	refer_win = null;
}

// 选择表单 返填数据，并查询改表单可配置工作流的action
function fillBackTaskData(data) {
	var fname = data.fname;
	var fbm = data.fbm;
	Ext.getCmp("billFormBox").setValue(fname);
	Ext.getCmp("factiontype").setValue("");
	
	if(Ext.getCmp("facadeCombo").store!=null){
		Ext.getCmp("facadeCombo").clearValue();
	}
	
	typeStore.proxy= new Ext.data.HttpProxy({url: 'queryAction.action?fname='+fname}); 
                        
	typeStore.load();
	
	Ext.getCmp("facadeCombo").setValue("");
	task_win.hide();
}

function hideTaskWin() {
	task_win.hide();
}
function chooseTask() {
	focusInput = this;
	$("#dialog-modal").omDialog('open');
	var frameLoc = window.frames[0].location;
	if (frameLoc.href == 'about:blank') {
		frameLoc.href = 'select-user.html';
	}
	return false;
}

