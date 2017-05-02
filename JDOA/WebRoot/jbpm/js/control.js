function getControlPanel(currentParam) {
	// 表格
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
			
	var rowEditor = new Ext.ux.grid.RowEditor({
				saveText: '确定',
			    cancelText: '取消',
			    commitChangesText: '请按下「确定」'
			});			
	
	var genderRenderer = function(value){
				if(value == '1'){
					return '<span >启用</span>';
				}else{
					return '<span >不启用</span>';
				}
			}
	
		var genderFieldEditor = {
			xtype:'combo',
			triggerAction:'all',
			forceSelection:true,
			displayField:'text',
			valueField:'value',
			mode:'local',
			store:{
				xtype:'jsonstore',
				fields:[
					'value', 'text'
				],
				data:[
				   { value:'1', text:'启用'  },
				   { value:'0', text:'不启用'  }
				]
			}
		};
			//按钮名称 按钮方法 按钮id
		var cm = new Ext.grid.ColumnModel({ // 对列的定义，cm是它的简写，作为真个表格的列模式，需要首先创建的
			columns:[{
					header : '控件名称',dataIndex : 'fbuttonname'
				}, {
					header : '启用',dataIndex : 'fenable',renderer:genderRenderer,editor:genderFieldEditor
				}, {
					header : '唯一性',dataIndex : 'fid',hidden :true
				}]
			,defaults:{
					resizable:false
				}	
		});

	var store = new Ext.data.Store({
				proxy : new Ext.data.MemoryProxy([[]]), // 是指获取数据的方式
				reader : new Ext.data.ArrayReader({}, [ // 是指如何解析这一堆数据，ArrayReader就是解析数组的
								{
									name : 'fbuttonname'
								}, {
									name : 'fenable'
								}, {
								name : 'fid'
								}])
			});
	store.load();
	
	//保存方法
	var save=function(){
		// 首先获得mailtos
		var controls = getParentNode("control");// 获得configs节点
		
		var taskValue = tempDataXml.documentElement.getElementsByTagName("taskvalue");
		
		controls.setAttribute("value",taskValue[0].getAttribute("value"));// 设置来自哪张表

		RemoveAllChildNodes(controls);// 删除config所有的子节点

		for (var i = 0; i < store.getCount(); i++) {
			// 新建control节点
			var control = tempDataXml.createElement("control");
			// 创建新的节点
			var fbuttonname_node = tempDataXml.createElement("fbuttonname");// 创建功能节点
			var fenable_node = tempDataXml.createElement("fenable");// 创建参数类型节点
			var fid_node = tempDataXml.createElement("fid");// 创建参数类型节点

			var record = store.getAt(i);

			// 给每个节点进行属性赋值
			fbuttonname_node.setAttribute("value", record.get('fbuttonname'));
			fenable_node.setAttribute("value", record.get('fenable'));
			fid_node.setAttribute("value", record.get('fid'));

			// 把新建的节点添加父节点中
			control.appendChild(fbuttonname_node);
			control.appendChild(fenable_node);
			control.appendChild(fid_node);

			controls.appendChild(control);
		}
		Ext.MessageBox.alert("成功","添加成功");
		store.commitChanges() 
	}
	
	//查询功能
	var queryControlStore = function(){
			var targetString = Ext.getCmp('queryControl').getValue();
			if(targetString == ''){
				store.load();
				return;
			}
			store.filterBy(function(record, id){
				if(record.get('fbuttonname').indexOf(targetString)!= -1){
					   return true;
				}
				return false;
			});
		}
	
	//设置是否启用
	var fun_enable = function(){
		var stores=Ext.getCmp('control').getSelectionModel().getSelections();
		
		if(stores.length>0){
			var setIsEnable=Ext.getCmp("setIsEnable").getValue();
			
			if(setIsEnable==''){
				Ext.MessageBox.alert("失败","请选择需要更新的数据!");
				return;
			}else{
				for(var i=0;i<stores.length;i++){
					var index=Ext.getCmp('control').getStore().find('fid',stores[i].data.fid);   
					Ext.getCmp('control').getStore().getAt(index).data.fenable=setIsEnable;
				}
				Ext.getCmp('control').reconfigure(Ext.getCmp('control').getStore(),cm);
				
				Ext.getCmp('control').getSelectionModel().clearSelections();
			}
			
			
		}else{
			Ext.MessageBox.alert("失败","请选择需要更新的数据!");
			return;
		}
	}
	
	currentCard = new Ext.grid.EditorGridPanel({
		frame : true,
		title : "表格信息",
		store : store,
		id : currentParam,
		height : 400,
		width : 600,
		enableColumnMove : false,
		region : 'center',
		sm : sm,
		cm : cm,
		plugins:[ rowEditor ],
		viewConfig : {
			forceFit : true
			// 让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
		},
		tbar:{
					xtype:'toolbar',
					frame:true,
					border:false,
					padding:2,
					items:[
						{ xtype:'textfield', emptyText:'请输入关键字...', width:220, id:'queryControl',
							listeners :{
							  specialkey : function(field, e) {
			                    if (e.getKey() == Ext.EventObject.ENTER) {
							            queryControlStore();
			                    }
			               	  }
							}
						},
						{ xtype:'spacer', width:5 },
						{ xtype:'button', text:'查询', handler:queryControlStore, scope:this },
						{ xtype:'button', text:'保存', handler:save, scope:this },
						{ xtype:'spacer', width:15 },
						{
							xtype:'combo',
							id: 'setIsEnable',
							width: 80,
							height: 25,
							editable: false,
							valueField: 'value',
							displayField: 'text',
							triggerAction: 'all',
							emptyText: '请选择',
							mode: 'local',
							store: new Ext.data.SimpleStore({
								fields: ['value','text'],
								data:[
									['0','不启用'],
									['1','启用']								
								]
							}),
							scope: this
						},
						{ xtype:'button', text:'更新', handler:fun_enable, scope:this }
					]
			}
	});
	return currentCard;
}