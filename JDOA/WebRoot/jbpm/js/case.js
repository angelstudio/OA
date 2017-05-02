function casese(currentParam){
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
							id : 'caseListPanel',
							html : '<div class="panel_list" id="caseList"></div>',
							tbar : new Ext.Toolbar({
										height : 31,
										border : false,
										items : ['->', {
													text : '&nbsp;新增',
													iconCls : 'picon23',
													handler : function() {
														removeAllClass("case");
														clearCasePage();
													}
												}, '-', {
													text : '&nbsp;添加',
													iconCls : 'picon23',
													handler : function() {
														//获得页面数据
														var caseLeftValueBox1=Ext.getCmp("caseLeftValueBox1").getValue();//判断提交
														var caseCompareCombo1=Ext.getCmp("caseCompareCombo1").getValue();//选择符号
														var caseRightValueBox1=Ext.getCmp("caseRightValueBox1").getValue();//参数值
														var caseRelationCombo=Ext.getCmp("caseRelationCombo").getValue();//连接符
														var caseExpressionBox=Ext.getCmp("caseExpressionBox").getValue();//表达式
														
														//验证一些参数不能为空
														if(caseLeftValueBox1 == ""){
															Ext.MessageBox.alert(tip,'判断条件不能为空！');
															return false;
														}
														if(caseCompareCombo1 == ""){
															Ext.MessageBox.alert(tip,'符号不能为空！');
															return false;
														}
														if(caseRightValueBox1 == ""){
															Ext.MessageBox.alert(tip,'参数值不能为空！');
															return false;
														}
														if(caseRelationCombo == ""){
															Ext.MessageBox.alert(tip,'连接符不能为空！');
															return false;
														}
														
														//判断当前任务节点是否存在，如果不存在的话就添加一个id节点。这段代码主要是获得id节点
														var root_id=xmlNode.getAttribute("id");
														var temp_root_id=tempDataXml.getElementsByTagName(root_id);
														var root;
														if(temp_root_id.length==0){
															root=tempDataXml.createElement(root_id);//创建一个id节点
															tempDataXml.documentElement.appendChild(root);
														}else{
															root=temp_root_id[0];
														}
														
														//首先判断cases节点是否存在，不存在的话就创建cases节点
														var cases_node=root.getElementsByTagName("cases");
														var cases;
														if(cases_node.length==0){
															cases=tempDataXml.createElement("cases");//创建一个新的forms节点
															addXmlAttribute(cases,"id",createNodeID("case"));
															root.appendChild(cases);
														}else{
															cases=cases_node[0];
														}
														
														var newNode;//
														var caseOrderValue="";
														//使用回填的话，那么就是使用已有的数据
														if(selectNodeId!=null){
															for(var i=0;i<cases.childNodes.length;i++){//遍历所有子节点
																if(cases.childNodes[i].getAttribute("id")==selectNodeId){
																	newNode=cases.childNodes[i];
																	caseOrderValue=newNode.childNodes[5].getAttribute("value");
																	RemoveAllChildNodes(newNode);//因为是修改，那么就删除所有的子节点
																	Ext.MessageBox.alert("成功","修改成功");
																	break;
																}
															}
														}else{
															newNode = tempDataXml.createElement("case");
															addXmlAttribute(newNode, "id", xmlNode.getAttribute("name")+"-case-"+caseNum);
															addXmlAttribute(newNode, "name", xmlNode.getAttribute("name")+"-case-"+caseNum);
															//添加数据到左边的树结构中
															Ext.MessageBox.alert("成功","添加成功");
															//移除事件
															Ext.get(Ext.DomQuery.select(".case_btn")).un("click");
															//添加树列表
															var caseId = xmlNode.getAttribute("name")+"-case-"+caseNum;
															
															addTreeList(Ext.getDom("caseList"),caseId,xmlNode.getAttribute("name")+"-case-"+caseNum,"case");
															//触发事件
															Ext.getDom(caseId).fireEvent('onclick');
														}
														
														//创建页面对应节点
														var caseLeftValueBox1_node=tempDataXml.createElement("caseLeftValueBox1");
														var caseCompareCombo1_node=tempDataXml.createElement("caseCompareCombo1");
														var caseRightValueBox1_node=tempDataXml.createElement("caseRightValueBox1");
														var caseRelationCombo_node=tempDataXml.createElement("caseRelationCombo");
														var caseExpressionBox_node=tempDataXml.createElement("caseExpressionBox");
														var caseOrderId_node=tempDataXml.createElement("caseOrderId");
														
														//给每个节点添加value的值
														addXmlAttribute(caseLeftValueBox1_node,"value",caseLeftValueBox1);
														addXmlAttribute(caseCompareCombo1_node,"value",caseCompareCombo1);
														addXmlAttribute(caseRightValueBox1_node,"value",caseRightValueBox1);
														addXmlAttribute(caseRelationCombo_node,"value",caseRelationCombo);
														addXmlAttribute(caseExpressionBox_node,"value",caseExpressionBox);
														
														if(caseOrderValue==""){//代表的是新增
															addXmlAttribute(caseOrderId_node,"value",orderValue);
															orderValue++;//新增的话，排序数不变
														}else{//代表的是修改
															addXmlAttribute(caseOrderId_node,"value",caseOrderValue);
														}
														
														//添加子节点
														newNode.appendChild(caseLeftValueBox1_node);
														newNode.appendChild(caseCompareCombo1_node);
														newNode.appendChild(caseRightValueBox1_node);
														newNode.appendChild(caseRelationCombo_node);
														newNode.appendChild(caseExpressionBox_node);
														newNode.appendChild(caseOrderId_node);
														
														//如果是newNode是新增的，那么就需要添加到cases节点下
														if(selectNodeId==null){
															cases.appendChild(newNode);
														}
														
														caseNum++;
														removeAllClass("case");
														clearCasePage();
													}
												}, '-', {
													text : '&nbsp;删除',
													iconCls : 'picon13',
													handler : function() {
														deleteXmlNodeByType("case","");
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
						//
						items: [{
							layout: 'column',
							border: false,
							bodyStyle: {background:"#dfe7f4"},
							items: [{
								layout: 'form',
								width: 220,
								border: false,
								bodyStyle: {background:"#dfe7f4"},
								defaultType: 'textfield',
								items: [{
									id: 'caseLeftValueBox1',
									fieldLabel: '判断条件',
									width: 120
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
										if (case_win != null) {
											case_win.show();
										} else {
											case_win = new Ext.Window({
												id : 'case_win',
												title : "参数选择",
												width : 600,
												closeAction: 'hide',
												height : 470,
												html : '<iframe frameborder="0" style="width:100%;height:99%;height:100%\9;" src="../wf/jsp/param_select.html"></iframe>'
											});
											case_win.show();
										}
									}
								}]
							},{
								layout: 'form',
								width: 135,
								border: false,
								labelWidth: 5,
								bodyStyle: {background:"#dfe7f4"},
								items: [{
									id: 'caseCompareCombo1',
									xtype: 'combo',
									width: 120,
									valueField: 'value',
									editable: false,
									displayField: 'text',
									triggerAction: 'all',
									emptyText: '请选择',
									mode: 'local',
									store: new Ext.data.SimpleStore({
										fields: ['value','text'],
										data:[
											['1','小于'],
											['2','大于'],
											['3','等于'],
											['4','不等于'],
											['5','小于等于'],
											['6','大于等于']
										]
									})
								}]		
							},{
								layout: 'form',
								width: 145,
								border: false,
								labelWidth: 5,
								bodyStyle: {background:"#dfe7f4"},
								items: [{
									id: 'caseRightValueBox1',
									xtype: 'textfield',
									width: 120
								}]				
							},{
								layout: 'fit',
								xtype: 'panel',
								border: false,
								height: 20,
								width: 20,
								bodyStyle: {background:"#dfe7f4"},
								html: '<span title="引用变量" class="img_btn picon22" />'			
							}]
						},{
							layout: 'column',
							border: false,
							bodyStyle: {background:"#dfe7f4"},
							items: [{
								layout: 'form',
								border: false,
								width: 220,
								bodyStyle: {background:"#dfe7f4"},
								items: [{
									id: 'caseRelationCombo',
									xtype: 'combo',
									width: 120,
									editable: false,
									valueField: 'value',
									displayField: 'text',
									triggerAction: 'all',
									emptyText: '请选择',
									mode: 'local',
									store: new Ext.data.SimpleStore({
										fields: ['value','text'],
										data:[
											['1','并且(and)'],
											['2','或者(or)']
										]
									})
								}]
							},{
								layout: 'fit',
								xtype: 'panel',
								border: false,
								height: 20,
								width: 20,
								bodyStyle: {background:"#dfe7f4"},
								html: '<span title="添加" class="img_btn picon23" />'			
							}]
						},{
							layout: 'form',
							border: false,
							bodyStyle: {background:"#dfe7f4"},
							items: [{
								id: 'caseExpressionBox',
								xtype: 'textarea',
								fieldLabel: '表达式',
								width: '85%'
							}]
						}]
						//
					}]
				}]
			}]
		}]
	});
	return currentCard;
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
		//针对每个form获得参数名称、参数值、参数类型
		arr[0]=node[2].getAttribute("value");//参数名称
		arr[1]=node[3].getAttribute("value");//参数类型
		arr[2]=node[4].getAttribute("value");//参数值
		
		jsonarr.push(arr);
	}
	return jsonarr;
}

//选择表单 返填数据，并查询改表单可配置工作流的action
function fillBackParamData(data) {
	var ParamName = data.ParamName;
	var ParamType = data.ParamType;
	var ParamValue = data.ParamValue;
	
	Ext.getCmp("caseLeftValueBox1").setValue(ParamName);
	
	case_win.hide();
	case_win=null;
}

//关闭窗口
function hideParamWin() {
	case_win.hide();
	case_win=null;
}



