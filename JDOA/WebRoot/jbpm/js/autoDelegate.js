function getAutoDelegatePanel(currentParam){
	current_Card = new Ext.form.FormPanel({
					id: currentParam,
					border: false,
					bodyStyle: {background:"#dfe7f4",padding:"10 0 0 20"},
					labelAlign: 'left',	
					items:[{
						layout: 'form',
						defaultType: 'radio',
						labelWidth: 80,
						border: false,
						bodyStyle: {background:"#dfe7f4"},
						items:[{
							xtype: 'radio',
							fieldLabel: '运行时委派',
							boxLabel: '是',
							value: '1',
							name: 'autoDeleRunRadion',
							width: 'auto'
						},{
							xtype: 'radio',
							boxLabel: '否',
							value: '2',
							checked: true,
							name: 'autoDeleRunRadion',
							width: 'auto'
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
								id: 'autoDeleSponsorCheck',
								fieldLabel: '范围',
								boxLabel: '流程发起者',
								listeners: {
									"check": function(){
										Ext.getCmp("autoDeleSponsorCombo").disabled?Ext.getCmp("autoDeleSponsorCombo").enable():Ext.getCmp("autoDeleSponsorCombo").disable();
									}
								}	
							},{
								id: 'autoDeleExecutorCheck',
								boxLabel: '流程执行者',
								listeners: {
									"check": function(){
										Ext.getCmp("autoDeleExecutorCombo").disabled?Ext.getCmp("autoDeleExecutorCombo").enable():Ext.getCmp("autoDeleExecutorCombo").disable();
										Ext.getCmp("autoDeleExecutorTypeCombo").disabled?Ext.getCmp("autoDeleExecutorTypeCombo").enable():Ext.getCmp("autoDeleExecutorTypeCombo").disable();
										if(Ext.getCmp("autoDeleExecutorTypeCombo").getValue() == 2 && !Ext.getCmp("autoDeleExecutorTypeCombo").disabled)
											Ext.getCmp("autoDeleExecutorAreaCombo").enable();
										else
											Ext.getCmp("autoDeleExecutorAreaCombo").disable();
									}
								}	
							},{
								id: 'autoDelePartnerCheck',
								boxLabel: '流程参与者',
								listeners: {
									"check": function(){
										Ext.getCmp("autoDelePartnerCombo").disabled?Ext.getCmp("autoDelePartnerCombo").enable():Ext.getCmp("autoDelePartnerCombo").disable();
										Ext.getCmp("autoDelePartnerTypeCombo").disabled?Ext.getCmp("autoDelePartnerTypeCombo").enable():Ext.getCmp("autoDelePartnerTypeCombo").disable();
										if(Ext.getCmp("autoDelePartnerTypeCombo").getValue() == 2 && !Ext.getCmp("autoDelePartnerTypeCombo").disabled)
											Ext.getCmp("autoDelePartnerAreaCombo").enable();
										else
											Ext.getCmp("autoDelePartnerAreaCombo").disable();
									}
								}	
							},{
								boxLabel: '用户',
								id: 'autoDeleUserCheck',
								listeners: {
									"check": function(){
										Ext.getCmp("autoDeleUserBox").disabled?Ext.getCmp("autoDeleUserBox").enable():Ext.getCmp("autoDeleUserBox").disable();
									}
								}	
							},{
								boxLabel: '角色',
								id: 'autoDeleRoleCheck',
								listeners: {
									"check": function(){
										Ext.getCmp("autoDeleRoleBox").disabled?Ext.getCmp("autoDeleRoleBox").enable():Ext.getCmp("autoDeleRoleBox").disable();
									}
								}
							},{
								boxLabel: '部门',
								id: 'autoDeleDeptCheck',
								listeners: {
									"check": function(){
										Ext.getCmp("autoDeleDeptBox").disabled?Ext.getCmp("autoDeleDeptBox").enable():Ext.getCmp("autoDeleDeptBox").disable();
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
									id: 'autoDeleSponsorCombo',
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
									id: 'autoDeleExecutorCombo',
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
									id: 'autoDelePartnerCombo',
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
									id: 'autoDeleExecutorTypeCombo',
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
												Ext.getCmp("autoDeleExecutorAreaCombo").disable();
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
												Ext.getCmp("autoDeleExecutorAreaCombo").store = store;
												Ext.getCmp("autoDeleExecutorAreaCombo").enable();
											}
										},
										"beforerender": function(){
											this.setValue(1);								
										}
									}
								},{
									id: 'autoDelePartnerTypeCombo',
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
												Ext.getCmp("autoDelePartnerAreaCombo").disable();
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
												Ext.getCmp("autoDelePartnerAreaCombo").store = store;
												Ext.getCmp("autoDelePartnerAreaCombo").enable();
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
									id: 'autoDeleExecutorAreaCombo',
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
									id: 'autoDelePartnerAreaCombo',
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
							defaults: {width: '85%'},
							items: [{
								xtype: 'textfield',
								id: 'autoDeleUserBox',
								disabled: true
							},{
								xtype: 'textfield',
								id: 'autoDeleRoleBox',
								disabled: true
							},{
								xtype: 'textfield',
								id: 'autoDeleDeptBox',
								disabled: true
							}]
						}]													
					}]
				});		
	return current_Card;
}

	