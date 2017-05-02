Ext.onReady(function() {
	var treeNodes = {
			    text:'任务类型',
			    id :'root',
			    expanded:true,
			    children:[
                    { text:'业务单据',id : 't_bill',cls : "file",leaf:true}
			    ]
			};
			
	var tree = new Ext.tree.TreePanel({
		title:'任务类型',
		root:treeNodes,
		width : 150,
		height : 400,
		autoScroll:true
	});		
		
	tree.on('click', loadClickData);
	
	function loadClickData(node,e){
		if(node.id=="t_bill"){
			store.filterBy(function(record, id){
				if(record.get('ftype') == 't_bill' ){
					   return true;
				}
				return false;
			});
		}else if(node.id=="t_baseData"){
			store.filterBy(function(record, id){
				if(record.get('ftype') == 't_baseData' ){
					   return true;
				}
				return false;
			});
		}else if(node.id=="t_action"){
			store.filterBy(function(record, id){
				if(record.get('ftype') != 't_baseData'&&record.get('ftype') != 't_bill' ){
					   return true;
				}
				return false;
			});
		}else if(node.id=="root"){
			store.load();
		}
	}
	
	
	var store = new Ext.data.JsonStore({
				autoDestroy : true,
				url : 'queryBill.action',
				storeId : 'myStore',
				fields : [{
							name : 'fid',
							mapping : 'fid',
							type : 'string'
						}, {
							name : 'fname',
							mapping : 'fname',
							type : 'string'
						}, {
							name : 'fbm',
							mapping : 'fbm',
							type : 'string'
						}, {
							name : 'fbz',
							mapping : 'fbz',
							type : 'string'
						}]
			});
	store.load();
	
	//对分类中的值进行解析
	function genderFieldType(abbr){
		//把具体的枚举值转换成相应的枚举别名
		if(abbr=='t_bill'){
			return '<span >单据</span>';
		}else if(abbr=='t_baseData'){
			return '<span >基础资料</span>';
		}else {
			return '<span >未分类</span>';
		}
	}

	var cm=new Ext.grid.ColumnModel([  //对列的定义，cm是它的简写，作为真个表格的列模式，需要首先创建的
               {header:'主键',dataIndex:'fid'},  //header是表的首部显示文本。dataIndex是列对应的记录集字段,sortable表示列是否可排序，可能还会用到的参数：renderer列的渲染函数，format格式化信息
               {header:'名称',dataIndex:'fname'},
               {header:'别名',dataIndex:'fbm'},
               {header:'备注',dataIndex:'fbz'}
	       ]);
	
	// 表格
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var grid = new Ext.grid.GridPanel({
		frame : true,
		width : 400,
		height : 340,
		title : "任务",
		store : store,
		sm : sm,
		cm:cm,
		viewConfig : {
		       forceFit : true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
		},
		listeners : {
			"rowdblclick" : function(grid, rowIndex, e) {
				window.parent.fillBackTaskData(grid.getStore().getAt(rowIndex).data);
			}
		},
		buttons : [{
			text : '确认',
			iconCls : 'picon19',
			handler : function() {
				var selectRow = grid.getSelectionModel().getSelected();//
				if (selectRow != null) {
					window.parent.fillBackTaskData(grid.getSelectionModel()
							.getSelected().data);
				} else {
					Ext.MessageBox.alert("提示", "请先选择行！ ");
				}
			}
		}, {
			text : '取消',
			iconCls : 'picon09',
			handler : function() {
				window.parent.hideTaskWin();
			}
		}]
	});
	
	//查询功能
	var queryTaskStore = function(){
			var targetString = Ext.getCmp('search_task').getValue();
			if(targetString == ''){
				store.load();
				return;
			}
			store.filterBy(function(record, id){
				if(record.get('aliasname').indexOf(targetString)!= -1 ){
					   return true;
				}
				if(record.get('number').indexOf(targetString)!= -1 ){
					   return true;
				}
				if(record.get('name').indexOf(targetString)!= -1 ){
					   return true;
				}
				return false;
			});
		}
		
	var dataForm = new Ext.FormPanel({
				labelAlign : 'left',
				title : '自定义过滤',
				buttonAlign : 'right',
				bodyStyle : 'padding:5px',
				width : 600,
				frame : true,
				labelWidth : 80,
				tbar:{
					xtype:'toolbar',
					frame:true,
					border:false,
					padding:2,
					items:[		
						{ xtype:'label', width:55,text:'名      称 :' },
						{ xtype:'spacer', width:15 },
						{ xtype:'textfield', emptyText:'请输入关键字...',cls : 'key', width:220, id:'search_task',
							listeners :{
							  specialkey : function(field, e) {
			                    if (e.getKey() == Ext.EventObject.ENTER) {
							            queryTaskStore();
			                    }
			               	  }
							}
						},
						{ xtype:'spacer', width:15 },
						{ xtype:'tbbutton', text:'查询',width:60,iconCls :'search',labelAlign : 'center', handler:queryTaskStore
//						{
//							xtype : 'tbbutton',
//							id : 'btn_selectTask',
//							labelAlign : 'center',
//							width : 60,
//							text : '查询',
//							handler:queryTaskStore
//						}
						}
					]
				}
			})
	var viewport = new Ext.Viewport({
				layout : 'border',
				renderTo : Ext.getBody(),
				items : [{
							region : 'north',
							xtype : 'panel',
							width : 600,
							height : 65,
							split : true,
							items : [dataForm]
						}, {
							region : 'west',
							xtype : 'panel',
							width : 150,
							align : 'left',
							height : 400,
							split : true,
							items : [tree]
						}, {
							region : 'center',
							xtype : 'panel',
							width : 600,
							height : 400,
							layout:'fit',
							split : true,
							items : [grid]
						}]
			});
});


