function getChangePanel(currentParam) {
	current_Card = new Ext.form.FormPanel({
		id : currentParam,
		border : false,
		bodyStyle : {
			background : "#dfe7f4",
			padding : "10 0 0 20"
		},
		labelAlign : 'left',
		items : [ {
			layout : 'column',
			border : false,
			bodyStyle : {
				background : "#dfe7f4"
			},
			items : [{
					width : 210,
					layout : 'form',
					defaultType : 'radio',
					defaults : {
						height : 22
					},
					bodyStyle : {
						background : "#dfe7f4"
					},
					border : false,
					labelWidth : 80,
					items : [{
								fieldLabel : '转换模式',
								boxLabel : '人工任务',
								inputValue : '1',
								id:'changeMethod1',
								name : 'changeMethodRadio'
							}, {
								boxLabel : '审批任务',
								inputValue : '2',
								id:'changeMethod2',
								name : 'changeMethodRadio'
							}, {
								fieldLabel : '启动节点',
								boxLabel : '否',
								inputValue : '1',
								id:'startMethod1',
								name : 'startMethodRadio'
							}, {
								boxLabel : '是',
								inputValue : '2',
								id:'startMethod2',
								name : 'startMethodRadio'
						}, {
							xtype : 'button',
							id : 'btnChangeSave',
							labelAlign : 'right',
							width : 50,
							text : '保存',
							modal : true,
							maximizable : true,
							disabled : false,
							handler : function() {
								var changes = getParentNode("change");
								RemoveAllChildNodes(changes);
								var changeMethodRadio_node = tempDataXml
										.createElement("changeMethodRadio");// 创建任务节点
								changeMethodRadio_node.setAttribute("value",
										Ext.getCmp("change").form
												.findField("changeMethodRadio")
												.getGroupValue()-1);

								var startMethodRadio_node = tempDataXml
										.createElement("startMethodRadio");// 创建任务节点
								startMethodRadio_node.setAttribute("value",
										Ext.getCmp("change").form
												.findField("startMethodRadio")
												.getGroupValue()-1);

								changes.appendChild(changeMethodRadio_node);
								changes.appendChild(startMethodRadio_node);
								Ext.MessageBox.alert("成功","添加成功");
							}
						}]
				}]
				}]
			});
	return current_Card;
}
