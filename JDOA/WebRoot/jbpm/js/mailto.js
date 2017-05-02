function getMailtoPanel(currentParam) {
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
					return '<span >是</span>';
				}else{
					return '<span >否</span>';
				}
			}
			
	var genderVisableFieldEditor = {
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
					   { value:'1', text:'是'  },
					   { value:'0', text:'否'  }
					]
				}
			};
	
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
				   { value:'1', text:'是'  },
				   { value:'0', text:'否'  }
				]
			}
		};
			
	var cm = new Ext.grid.ColumnModel({ // 对列的定义，cm是它的简写，作为真个表格的列模式，需要首先创建的
			columns:[{
					header : '字段名称',dataIndex : 'fname'
				}, // header是表的首部显示文本。dataIndex是列对应的记录集字段,sortable表示列是否可排序，可能还会用到的参数：renderer列的渲染函数，format格式化信息
				{
					header : '字段别名',dataIndex : 'faliasname'
				}, {
					header : '只读',dataIndex : 'fonlyread',renderer:genderRenderer,editor:genderFieldEditor
				}, {
					header : '只见',dataIndex : 'fviewvisable',renderer:genderRenderer,editor:genderVisableFieldEditor
				}]
			,defaults:{
					resizable:false
				}	
		});

	var store = new Ext.data.Store({
				proxy : new Ext.data.MemoryProxy([[]]), // 是指获取数据的方式
				autoLoad : true,
				reader : new Ext.data.ArrayReader({}, [ // 是指如何解析这一堆数据，ArrayReader就是解析数组的
						{
									name : 'fname'
								}, {
									name : 'faliasname'
								}, {
									name : 'fonlyread'
								}, {
									name : 'fviewvisable'
								}])
			});
	store.load();
	
	//保存方法
	var save=function(){
		// 首先获得mailtos
		var configs = getParentNode("config");// 获得configs节点
		
		var taskValue = tempDataXml.documentElement.getElementsByTagName("taskvalue");
		configs.setAttribute("value", taskValue[0].getAttribute("value"));// 设置来自哪张表

		RemoveAllChildNodes(configs);// 删除config所有的子节点

		for (var i = 0; i < store.getCount(); i++) {
			// 新建config节点
			var config = tempDataXml.createElement("config");
			// 创建新的节点
			var fname_node = tempDataXml.createElement("fname");// 创建任务节点
			var faliasname_node = tempDataXml
					.createElement("faliasname");// 创建功能节点
			var fonlyread_node = tempDataXml.createElement("fonlyread");// 创建参数名称节点
			var fviewvisable_node = tempDataXml
					.createElement("fviewvisable");// 创建参数类型节点

			var record = store.getAt(i);

			// 给每个节点进行属性赋值
			fname_node.setAttribute("value", record.get('fname'));
			faliasname_node.setAttribute("value", record
							.get('faliasname'));
			fonlyread_node.setAttribute("value", record
							.get('fonlyread'));
			fviewvisable_node.setAttribute("value", record
							.get('fviewvisable'));

			// 把新建的节点添加父节点中
			config.appendChild(fname_node);
			config.appendChild(faliasname_node);
			config.appendChild(fonlyread_node);
			config.appendChild(fviewvisable_node);

			configs.appendChild(config);
		}
		Ext.MessageBox.alert("成功","添加成功");
		store.commitChanges();
	}
	
	//查询功能
	var queryConfigStore = function(){
			var targetString = Ext.getCmp('queryConfig').getValue();
			if(targetString == ''){
				store.load();
				return;
			}
			store.filterBy(function(record, id){
				if(record.get('faliasname').indexOf(targetString)!= -1||
					record.get('fname').indexOf(targetString)!= -1){
					   return true;
				}
				return false;
			});
		}
	
	///多行设置
	var setStore=function(){
		var stores=Ext.getCmp('mailto').getSelectionModel().getSelections();
		if(stores.length>0){
			var rowType=Ext.getCmp("rowType").getValue();
			var rowResult=Ext.getCmp("rowResult").getValue();
			
			if(rowType==""){
				Ext.MessageBox.alert("失败","请选择需要更新的列!");
				return;
			}
			if(rowResult==""){
				Ext.MessageBox.alert("失败","请选择需要更新的值!");
				return;
			}
			
			for(var i=0;i<stores.length;i++){
				var index=Ext.getCmp('mailto').getStore().find('fname',stores[i].data.fname);   
				
				if(rowType=="0"){//只读
					Ext.getCmp('mailto').getStore().getAt(index).data.fonlyread=rowResult;
				}else{
					Ext.getCmp('mailto').getStore().getAt(index).data.fviewvisable=rowResult;
				}
			}
			Ext.getCmp('mailto').reconfigure(Ext.getCmp('mailto').getStore(),cm);//重新加载
			
			Ext.getCmp('mailto').getSelectionModel().clearSelections();//清除选中的数据
		}else{
			Ext.MessageBox.alert("失败","请选择需要设置的数据!");
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
						{ xtype:'textfield', emptyText:'请输入关键字...', width:220, id:'queryConfig',
							listeners :{
							  specialkey : function(field, e) {
			                    if (e.getKey() == Ext.EventObject.ENTER) {
							            queryConfigStore();
			                    }
			               	  }
							}
						},
						{ xtype:'spacer', width:5 },
						{ xtype:'button', text:'查询', handler:queryConfigStore, scope:this },
						{ xtype:'button', text:'保存', handler:save, scope:this },
						{
							xtype:'combo',
							id: 'rowType',
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
									['0','只读'],
									['1','只见']								
								]
							}),
							scope: this
						},
						{ xtype:'spacer', width:15 },
						{
							xtype:'combo',
							id: 'rowResult',
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
									['0','否'],
									['1','是']								
								]
							}),
							scope: this
						},
						{ xtype:'spacer', width:5 },
						{ xtype:'button', text:'更新', handler:setStore, scope:this }
					]
			}
	});
	return currentCard;
}
	