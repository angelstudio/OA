function getMethodPanel(currentParam) {
	current_Card = new Ext.form.FormPanel(
			{
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
					items : [
							{
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
								items : [
										{
											fieldLabel : '分配策略',
											boxLabel : '分配给所有人',
											inputValue : '1',
											id:'methodDistributionRadio1',
											name : 'methodDistributionRadio',
											checked: true 
										},
										{
											boxLabel : '任意分配给一个人',
											inputValue : '2',
											id:'methodDistributionRadio2',
											name : 'methodDistributionRadio'
										},
										{
											fieldLabel : '完成策略',
											boxLabel : '任意完成一个',
											inputValue : '1',
											id:'methodCompleteRadio1',
											name : 'methodCompleteRadio',
											checked: true 
											,listeners : {
												"focus" : function() {
													clearMethodPage();//设置个数和百分比的文本框和单选框为不可编辑
												}
											}
										},
										{
											boxLabel : '必须完成所有',
											inputValue : '2',
											id:'methodCompleteRadio2',
											name : 'methodCompleteRadio'
											,listeners : {
												"focus" : function() {
													clearMethodPage();
												}
											}
										},
										{
											boxLabel : '按完成个数',
											inputValue : '3',
											id:'methodCompleteRadio3',
											name : 'methodCompleteRadio',
											listeners : {
												"focus" : function() {
													Ext.getCmp("methodPecentBox").disable();
													Ext.getCmp("methodPecentBox").setValue("");
													Ext.getCmp("methodNumBox").enable();
												}
												,"blur":function(){
													if(Ext.getCmp("method").form.findField("methodCompleteRadio").getGroupValue()!="3"){
														Ext.getCmp("methodNumBox").disable();
													}
												}
											}
										},
										{
											boxLabel : '按完成百分比',
											inputValue : '4',
											id:'methodCompleteRadio4',
											name : 'methodCompleteRadio',
											listeners : {
												"focus" : function() {
													 Ext.getCmp("methodNumBox").disable();
													 Ext.getCmp("methodNumBox").setValue("");
													 Ext.getCmp("methodPecentBox").enable();
												}
												,"blur":function(){
													if(Ext.getCmp("method").form.findField("methodCompleteRadio").getGroupValue()!="4"){
														Ext.getCmp("methodPecentBox").disable();
													}
												}
											}
										}, {
											xtype : 'button',// 保存按钮
											id : 'btn_saveMethod',
											labelAlign : 'right',
											width : 50,
											text : '保存',
											modal : true,
											maximizable : true,
											disabled : false,
											handler : function() {
												//验证输入文本框的值
												if(Ext.getCmp("method").form.findField("methodCompleteRadio").getGroupValue()=="3"){
													var reg = /^\+?[1-9][0-9]*$/;
									var regExp = new RegExp(reg);
									if (!regExp.test(Ext.getCmp("methodNumBox")
											.getValue())) {
										Ext.MessageBox.alert("提示","完成个数 ：'"
												+ Ext.getCmp("methodNumBox")
														.getValue()
												+ "'只能是非0的正整数,请核实重新输入");
										return;
									}
												}else if(Ext.getCmp("method").form.findField("methodCompleteRadio").getGroupValue()=="4"){
													var reg=/(^[1-9](\d)?(\.\d+)?$)|(^0(\.\d+)?$)|(^100$)/;
													var regExp = new RegExp(reg);
													if(!regExp.test(Ext.getCmp("methodPecentBox").getValue())){
														Ext.MessageBox.alert("提示","完成百分比 ：'"+Ext.getCmp("methodPecentBox").getValue()+"'有误,请输入0到100的数字!");
														return;
													}
												}
												
												//获取父节点，若没有，则创建一个父节点
												var elementDoc = getParentNode("method");
												if( elementDoc.hasChildNodes() ){
													RemoveAllChildNodes(elementDoc);
												}
												//任务节点的id
												var nodeId = xmlNode.getAttribute("id");
												//分配策略
												var methodDistributionRadio_value = Ext.getCmp("method").form.findField("methodDistributionRadio").getGroupValue();
												
												//完成策略
												var methodCompleteRadio_value =Ext.getCmp("method").form.findField("methodCompleteRadio").getGroupValue();
												
												var methodNumBox_value = Ext.getCmp("methodNumBox").getValue();//完成个数
												var methodPecentBox_value = Ext.getCmp("methodPecentBox").getValue();//完成百分比
												
												var newNode = tempDataXml.createElement("method");
												//给节点添加属性
												addXmlAttribute(newNode, "id", xmlNode.getAttribute("id"));
												addXmlAttribute(newNode, "name", xmlNode.getAttribute("name")+"-method");
												//创建子节点
												var methodDistributionNode = tempDataXml.createElement("methodDistribution");
												var methodCompleteNode = tempDataXml.createElement("methodComplete");
												//给子节点添加属性
												addXmlAttribute(methodDistributionNode, "value", methodDistributionRadio_value);
												addXmlAttribute(methodCompleteNode, "value", methodCompleteRadio_value);
												
												if( methodCompleteRadio_value  == "3" ){
													var methodNumBoxNode = tempDataXml.createElement("methodNumBox_value");
													addXmlAttribute(methodNumBoxNode, "value", methodNumBox_value);
													methodCompleteNode.appendChild(methodNumBoxNode);
												}else if( methodCompleteRadio_value  == "4" ){
													var methodPecentBoxNode = tempDataXml.createElement("methodPecentBox_value");
													addXmlAttribute(methodPecentBoxNode, "value", methodPecentBox_value);
													methodCompleteNode.appendChild(methodPecentBoxNode);
												}
												
												newNode.appendChild(methodDistributionNode);
												newNode.appendChild(methodCompleteNode);
												
												elementDoc.appendChild(newNode);
												
												Ext.MessageBox.alert("成功","添加成功");
											}
										} ]
							}, {
								width : 100,
								layout : 'form',
								bodyStyle : {
									background : "#dfe7f4"
								},
								border : false,
								defaultType : 'textfield',
								labelWidth : 5,
								items : [ {
									xtype : 'label',
									height : 113
								}, {
									width : 80,
									id : 'methodNumBox',
									disabled : true
								}, {
									width : 80,
									id : 'methodPecentBox',
									disabled : true
								} ]
							}, {
								width : 60,
								layout : 'form',
								bodyStyle : {
									background : "#dfe7f4"
								},
								border : false,
								defaultType : 'label',
								items : [ {
									height : 140
								}, {
									text : '个',
									height : 30,
									width : 60
								}, {
									text : '%',
									height : 30,
									width : 60
								} ]
							} ]
				} ]
			});
	return current_Card;
}
