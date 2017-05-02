function sql(currentParam) {
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
					id : 'sqlListPanel',
					html : '<div class="panel_list" id="sqlList"></div>',
					tbar : new Ext.Toolbar({
						height : 31,
						border : false,
						items : ['->', {
									text : '&nbsp;新增',
									iconCls : 'picon23',
									handler : function() {
										removeAllClass("sql");
										selectNodeId = null;
										Ext.getCmp("sqlParamName").setValue("");
									}
								}, '-', {
									text : '&nbsp;添加',
									iconCls : 'picon23',
									handler : function() {
										// 验证参数名称不能为空
										if (Ext.getCmp("classname")
												.getValue() == "") {
											Ext.MessageBox.alert(tip,
													'类名不能为空！');
											return false;
										}
										if (Ext.getCmp("methodname")
												.getValue() == "") {
											Ext.MessageBox.alert(tip,
													'方法名不能为空！');
											return false;
										}
										var sqls = getParentNode("sql");// 获得sqls节点
										var newNode;//
										// 使用回填的话，那么就是使用已有的数据
										if (selectNodeId != null) {
											for (var i = 0; i < sqls.childNodes.length; i++) {// 遍历所有子节点
												if (sqls.childNodes[i]
														.getAttribute("id") == selectNodeId) {
													newNode = sqls.childNodes[i];
													RemoveAllChildNodes(newNode);// 因为是修改，那么就删除所有的子节点
													break;
												}
											}
										} else {
											newNode = tempDataXml
													.createElement("sql");
											newNode
													.setAttribute(
															"id",
															xmlNode
																	.getAttribute("name")
																	+ "-sql-"
																	+ sqlNum);
											newNode
													.setAttribute(
															"name",
															xmlNode
																	.getAttribute("name")
																	+ "-sql-"
																	+ sqlNum);
											// 移除事件
											Ext.get(Ext.DomQuery
													.select(".sqlbtn"))
													.un("click");
											// 添加树列表
											var sqlId = xmlNode
													.getAttribute("name")
													+ "-sql-" + sqlNum;

											addTreeList(Ext.getDom("sqlList"),
													sqlId, sqlId, "sql");
											// 触发事件
											Ext.getDom(sqlId)
													.fireEvent('onclick');
										}

										// 根据文本框id设置节点
										var sqlParamName_node = tempDataXml
												.createElement("sqlParamName");// 创建参数名称节点

										// 获得文本框中的值
										var sqlParamName = Ext
												.getCmp("sqlParamName")
												.getValue();// 参数名称

										// 把文本框中的值添加为对应节点的value属性
										sqlParamName_node.setAttribute("value",
												sqlParamName);

										// 把值添加节点中
										newNode.appendChild(sqlParamName_node);

										// 判断classname和methodname的值是否存在
										if (!isExistNode("classname")) {// 不存在就添加节点
											var classname_node = tempDataXml
													.createElement("classname");// 创建参数名称节点
											var methodname_node = tempDataXml
													.createElement("methodname");// 创建参数类型节点

											var classname = Ext
													.getCmp("classname")
													.getValue();// 参数名称
											var methodname = Ext
													.getCmp("methodname")
													.getValue();// 参数类型

											classname_node.setAttribute(
													"value", classname);
											methodname_node.setAttribute(
													"value", methodname);

											var root_id = xmlNode
													.getAttribute("id");
											var temp_root_id = tempDataXml
													.getElementsByTagName(root_id);
											var task_node = temp_root_id[0];
											task_node
													.appendChild(classname_node);
											task_node
													.appendChild(methodname_node);

											Ext.getCmp("classname").disable();// 设置任务为不可编辑
											Ext.getCmp("methodname").disable();// 设置功能为不可编辑
										}

										if (selectNodeId == null) {
											sqls.appendChild(newNode);
											Ext.MessageBox.alert("成功","添加成功");
										} else {
											Ext.MessageBox.alert("成功","修改成功");
										}

										sqlNum++;
										removeAllClass("sql");
										Ext.getCmp("sqlParamName").setValue("");
									}
								}, '-', {
									text : '&nbsp;删除',
									iconCls : 'picon13',
									handler : function() {
										deleteXmlNodeByType("sql", "");
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
											width : 800,
											bodyStyle : {
												background : "#dfe7f4"
											},
											items : [{
														width : 600,
														xtype : 'textfield',
														fieldLabel : '类名',
														id : 'classname'
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
											width : 800,
											bodyStyle : {
												background : "#dfe7f4"
											},
											items : [{
														width : 600,
														xtype : 'textfield',
														fieldLabel : '方法名',
														id : 'methodname'
													}]
										}]
							}, {
								layout : 'column',
								border : false,
								bodyStyle : {
									background : "#dfe7f4"
								},
								items : [{
											layout : 'form',
											border : false,
											width : 300,
											bodyStyle : {
												background : "#dfe7f4"
											},
											items : [{
														labelWidth : 75,
														xtype : 'textfield',
														id : 'sqlParamName',
														fieldLabel : '参数名称',
														width : 200
											}]
								},{
								layout : 'fit',
								border : false,
								height: 20,
								width: 50,
								bodyStyle : {
									background : "#dfe7f4"
								},
								items : [{
									xtype : 'button',
									id : 'btn_selectParam',
									labelAlign : 'right',
									width : 50,
									text : '参数',
									modal : true,
									maximizable : true,
									handler : function() {
										if (sql_win != null) {
											sql_win.show();
										} else {
											sql_win = new Ext.Window({
												id : 'sql_win',
												title : "参数选择",
												width : 600,
												closeAction: 'hide',
												height : 470,
												html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="../wf/jsp/sqlparam_select.html"></iframe>'
											});
											sql_win.show();
										}
									}
								}]
								}, {
									layout: 'form',
									labelWidth: 10,
									bodyStyle: {background:"#dfe7f4"},
									border: false,
									items : [{
										xtype : 'hidden',
										id : 'sqlParamType',
										width: 150,
										disabled : true
										}, {
											xtype : 'hidden',
											width: 150,
											id : 'sqlParamValue',
											disabled : true
									}]
								}]
							}]
				}]
			}]
		}]
	});
	return currentCard;
}


//选择表单 返填数据，并查询改表单可配置工作流的action
function fillBackSqlParamData(data) {
	var ParamName = data.ParamName;
	var ParamType = data.ParamType;
	var ParamValue = data.ParamValue;
	
	Ext.getCmp("sqlParamName").setValue(ParamName);
	
	sql_win.close();
	sql_win=null;
}

//关闭窗口
function hideSqlParamWin() {
	sql_win.close();
	sql_win=null;
}



