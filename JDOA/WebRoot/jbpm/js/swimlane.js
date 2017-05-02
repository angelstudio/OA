function swimlane(currentParam){
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
								id: 'swimlaneListPanel',
								html: '<div class="panel_list" id="swimlaneList"></div>',
								tbar: new Ext.Toolbar({
									height: 31,
									border: false,
									items: [
										'->',
										{text: '&nbsp;添加',iconCls: 'picon23',handler: function(){
											if(Ext.getCmp("swimlaneDescriptionBox").getValue() == ""){
												Ext.MessageBox.alert(tip,'泳道描述不能为空！');
												return false;
											}
											if(findXmlNodeByName("swimlane",Ext.getCmp("swimlaneDescriptionBox").getValue()) != null){
												Ext.MessageBox.alert(tip,'泳道描述不能重复！');
												return false;
											}
											//获取泳道定义所有的参数
											var swimlaneDescription_value = Ext.getCmp("swimlaneDescriptionBox").getValue();//泳道描述
											//流程发起者
											var swimlaneSponsor_value="";//选中了赋值，没有选中的就为空字符串
											if(Ext.getCmp("swimlaneSponsorCheck").getValue()){//如果流程发起者为true，也就是选中了
												swimlaneSponsor_value=Ext.getCmp("swimlaneSponsorCombo").getValue();
											}
											//流程执行者
											var swimlaneExecutor_value="";
											if(Ext.getCmp("swimlaneExecutorCheck").getValue()){//如果流程发起者为true，也就是选中了
												//临时变量
												var Executor_value=Ext.getCmp("swimlaneExecutorCombo").getValue();//是否包含或出外
												//对应的类型
												var Executor_type=Ext.getCmp("swimlaneExecutorTypeCombo").getValue();
												//判定选中类型的值，因为关系到节点的值
												var Executor_node=Ext.getCmp("swimlaneExecutorAreaCombo").getValue();
												swimlaneExecutor_value=Executor_value+"-"+Executor_type+"-"+Executor_node;
											}
											//流程参与者
											var swimlanePartner_value="";
											if(Ext.getCmp("swimlanePartnerCheck").getValue()){//如果流程发起者为true，也就是选中了
												//临时变量
												var Partner_value=Ext.getCmp("swimlanePartnerCombo").getValue();//是否包含或出外
												//对应的类型
												var Partner_type=Ext.getCmp("swimlanePartnerTypeCombo").getValue();
												//判定选中类型的值，因为关系到节点的值
												var Partner_node=Ext.getCmp("swimlanePartnerAreaCombo").getValue();
												swimlanePartner_value=Partner_value+"-"+Partner_type+"-"+Partner_node;
											}
											//用户
											var swimlaneuser_value="";
											if (!Ext.getCmp("swimlaneUserBox").disabled) {
												swimlaneuser_value=Ext.getCmp("swimlaneUserBox").getValue();
											}
											//角色
											var swimlanerole_value="";
											if (!Ext.getCmp("swimlaneRoleBox").disabled) {
												swimlanerole_value=Ext.getCmp("swimlaneRoleBox").getValue();
											}
											//部门
											var swimlanedept_value="";
											if (!Ext.getCmp("swimlaneDeptBox").disabled) {
												swimlanedept_value=Ext.getCmp("swimlaneDeptBox").getValue();
											}
											$.ajax({
												type : 'POST',
												url : "addOneSwimlane",
												data : "swimlaneDescription=" + encodeURI(swimlaneDescription_value)+
												"&swimlaneSponsor="+encodeURI(swimlaneSponsor_value)+
												"&swimlaneExecutor="+encodeURI(swimlaneExecutor_value)+
												"&swimlanePartner="+encodeURI(swimlanePartner_value)+
												"&swimlaneuser="+encodeURI(swimlaneuser_value)+
												"&swimlanerole="+encodeURI(swimlanerole_value)+
												"&swimlanedept="+encodeURI(swimlanedept_value),
												dataType : 'text',
												contentType : 'application/x-www-form-urlencoded; charset=utf-8',
												success : function(data) {
													if( data == "ok" ){
														alert("添加成功");
														//添加swimlane节点
														addXmlNodeByName("swimlane",Ext.getCmp("swimlaneDescriptionBox").getValue(),"");
														//移除事件
														Ext.get(Ext.DomQuery.select(".swimlane_btn")).un("click");
														//添加树列表
														var swimlaneId = "swimlane" + swimlaneNum;
														swimlaneNum ++;
														addTreeList(Ext.getDom("swimlaneList"),swimlaneId,Ext.getCmp("swimlaneDescriptionBox").getValue(),"swimlane");
														//触发事件
														Ext.getDom(swimlaneId).fireEvent('onclick');
													}else{
														alert("系统异常");
													}
												}
											});
									}},'-',
										{text: '&nbsp;删除',iconCls: 'picon13',handler: function(){
											deleteXmlNodeByType("swimlane");		
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
									id: 'swimlaneDescriptionBox',
									xtype: 'textfield',
									width: '85%',
									fieldLabel: '泳道描述'
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
										id: 'swimlaneSponsorCheck',
										fieldLabel: '参与者',
										boxLabel: '流程发起者',
										listeners: {
											"check": function(){
												Ext.getCmp("swimlaneSponsorCombo").disabled?Ext.getCmp("swimlaneSponsorCombo").enable():Ext.getCmp("swimlaneSponsorCombo").disable();
											}
										}	
									},{
										id: 'swimlaneExecutorCheck',
										boxLabel: '流程执行者',
										listeners: {
											"check": function(){
												Ext.getCmp("swimlaneExecutorCombo").disabled?Ext.getCmp("swimlaneExecutorCombo").enable():Ext.getCmp("swimlaneExecutorCombo").disable();
												Ext.getCmp("swimlaneExecutorTypeCombo").disabled?Ext.getCmp("swimlaneExecutorTypeCombo").enable():Ext.getCmp("swimlaneExecutorTypeCombo").disable();
												if(Ext.getCmp("swimlaneExecutorTypeCombo").getValue() == 2 && !Ext.getCmp("swimlaneExecutorTypeCombo").disabled)
													Ext.getCmp("swimlaneExecutorAreaCombo").enable();
												else
													Ext.getCmp("swimlaneExecutorAreaCombo").disable();
											}
										}	
									},{
										id: 'swimlanePartnerCheck',
										boxLabel: '流程参与者',
										listeners: {
											"check": function(){
												Ext.getCmp("swimlanePartnerCombo").disabled?Ext.getCmp("swimlanePartnerCombo").enable():Ext.getCmp("swimlanePartnerCombo").disable();
												Ext.getCmp("swimlanePartnerTypeCombo").disabled?Ext.getCmp("swimlanePartnerTypeCombo").enable():Ext.getCmp("swimlanePartnerTypeCombo").disable();
												if(Ext.getCmp("swimlanePartnerTypeCombo").getValue() == 2 && !Ext.getCmp("swimlanePartnerTypeCombo").disabled)
													Ext.getCmp("swimlanePartnerAreaCombo").enable();
												else
													Ext.getCmp("swimlanePartnerAreaCombo").disable();
											}
										}	
									},{
										boxLabel: '用户',
										id: 'swimlaneUserCheck',
										listeners: {
											"check": function(){
												Ext.getCmp("swimlaneUserBox").disabled?Ext.getCmp("swimlaneUserBox").enable():Ext.getCmp("swimlaneUserBox").disable();
												var node = findXmlNodeByName("swimlane",Ext.getCmp("swimlaneDescriptionBox").getValue());
												if(node != null){
													if(Ext.getCmp("swimlaneUserBox").disabled){
														node.removeAttribute("candidate-users");
													}else{
														node.setAttribute("candidate-users",Ext.getCmp("swimlaneUserBox").getValue());
													}
												}
											}
										}	
									},{
										boxLabel: '角色',
										id: 'swimlaneRoleCheck',
										listeners: {
											"check": function(){
												Ext.getCmp("swimlaneRoleBox").disabled?Ext.getCmp("swimlaneRoleBox").enable():Ext.getCmp("swimlaneRoleBox").disable();
												var node = findXmlNodeByName("swimlane",Ext.getCmp("swimlaneDescriptionBox").getValue());
												if(node != null){
													if(Ext.getCmp("swimlaneRoleBox").disabled){
														node.removeAttribute("candidate-roles");
														node.setAttribute("candidate-groups",Ext.getCmp("swimlaneDeptBox").disabled?"":Ext.getCmp("swimlaneDeptBox").getValue());
														if(node.getAttribute("candidate-groups") == "")
															node.removeAttribute("candidate-groups");
													}else{
														node.setAttribute("candidate-roles",Ext.getCmp("swimlaneRoleBox").getValue());
														if(node.getAttribute("candidate-groups")!=""&&node.getAttribute("candidate-groups")!=null){
															if(Ext.getCmp("swimlaneRoleBox").getValue() != "")
																node.setAttribute("candidate-groups",node.getAttribute("candidate-groups")+","+Ext.getCmp("swimlaneRoleBox").getValue());
														}else{
															node.setAttribute("candidate-groups",Ext.getCmp("swimlaneRoleBox").getValue());
														}
													}
												}
											}
										}
									},{
										boxLabel: '部门',
										id: 'swimlaneDeptCheck',
										listeners: {
											"check": function(){
												Ext.getCmp("swimlaneDeptBox").disabled?Ext.getCmp("swimlaneDeptBox").enable():Ext.getCmp("swimlaneDeptBox").disable();
												var node = findXmlNodeByName("swimlane",Ext.getCmp("swimlaneDescriptionBox").getValue());
												if(node != null){
													if(Ext.getCmp("swimlaneDeptBox").disabled){
														node.removeAttribute("candidate-depts");
														node.setAttribute("candidate-groups",Ext.getCmp("swimlaneRoleBox").disabled?"":Ext.getCmp("swimlaneRoleBox").getValue());
														if(node.getAttribute("candidate-groups") == "")
															node.removeAttribute("candidate-groups");
													}else{
														node.setAttribute("candidate-depts",Ext.getCmp("swimlaneDeptBox").getValue());
														if(node.getAttribute("candidate-groups")!=""&&node.getAttribute("candidate-groups")!=null){
															if(Ext.getCmp("swimlaneRoleBox").getValue() != "")
																node.setAttribute("candidate-groups",node.getAttribute("candidate-groups")+","+Ext.getCmp("swimlaneDeptBox").getValue());
														}else{
															node.setAttribute("candidate-groups",Ext.getCmp("swimlaneDeptBox").getValue());
														}
													}
												}
											}
										}
									}]
								},{
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
											id: 'swimlaneSponsorCombo',
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
											id: 'swimlaneExecutorCombo',
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
											id: 'swimlanePartnerCombo',
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
											id: 'swimlaneExecutorTypeCombo',
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
														Ext.getCmp("swimlaneExecutorAreaCombo").disable();
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
														Ext.getCmp("swimlaneExecutorAreaCombo").store = store;
														Ext.getCmp("swimlaneExecutorAreaCombo").enable();
													}
												},
												"beforerender": function(){
													this.setValue(1);								
												}
											}
										},{
											id: 'swimlanePartnerTypeCombo',
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
														Ext.getCmp("swimlanePartnerAreaCombo").disable();
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
														Ext.getCmp("swimlanePartnerAreaCombo").store = store;
														Ext.getCmp("swimlanePartnerAreaCombo").enable();
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
											id: 'swimlaneExecutorAreaCombo',
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
											id: 'swimlanePartnerAreaCombo',
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
									labelWidth: 10,
									bodyStyle: {background:"#dfe7f4"},
									border: false,
									defaults: {width: '83%'},
									items: [{
										xtype: 'textfield',
										id: 'swimlaneUserBox',
										disabled: true
									},{
										xtype: 'textfield',
										id: 'swimlaneRoleBox',
										disabled: true
									},{
										xtype: 'textfield',
										id: 'swimlaneDeptBox',
										disabled: true
									}]
								}]							
							}]
						}]
					}]
				});	
	return currentCard;
}