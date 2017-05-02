Ext.onReady(function() {
	//首先获得父界面中从临时xml文件中解析后的数据
	var data=window.parent.getTempDataXmlToArray();
	
	var store=new Ext.data.Store({
        proxy:new Ext.data.MemoryProxy(data),  //是指获取数据的方式
        reader:new Ext.data.ArrayReader({},[    //是指如何解析这一堆数据，ArrayReader就是解析数组的
        {name:'ParamName'},
        {name:'ParamType'},
        {name:'ParamValue'}
        ]) 
	});
	store.load();//要执行一次，以对数据初始化，很重要
	
	
	var genderParamType = function(value){
		if(value == '1'){
			return '<span >字符串</span>';
		}else if(value == '2'){
			return '<span >数字型</span>';
		}else if(value == '3'){
			return '<span >浮点型</span>';
		}else if(value == '4'){
			return '<span >布尔型</span>';
		}
	}
	
	// 表格
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	
	var cm=new Ext.grid.ColumnModel([  //对列的定义，cm是它的简写，作为真个表格的列模式，需要首先创建的
           {header:'参数名称',dataIndex:'ParamName'},  //header是表的首部显示文本。dataIndex是列对应的记录集字段,sortable表示列是否可排序，可能还会用到的参数：renderer列的渲染函数，format格式化信息
           
           {header:'参数类型',dataIndex:'ParamType',renderer:genderParamType},
           {header:'参数值',dataIndex:'ParamValue'}
	    ]);

	var grid = new Ext.grid.GridPanel({
		frame : true,
		width : 600,
		height : 340,
		title : "参数选择",
		store : store,
		sm : sm,
		cm:cm,
		viewConfig : {
		       forceFit : true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
		},
		listeners : {
			"rowdblclick" : function(grid, rowIndex, e) {
				window.parent
						.fillBackParamData(grid.getStore().getAt(rowIndex).data);
			}
		},
		buttons : [{
			text : '确认',
			iconCls : 'picon19',
			handler : function() {
				var selectRow = grid.getSelectionModel().getSelected();//
				if (selectRow != null) {
					window.parent.fillBackParamData(grid.getSelectionModel().getSelected().data);
				} else {
					Ext.MessageBox.alert("提示", "请先选择行！      ");
				}
			}
		}, {
			text : '取消',
			iconCls : 'picon09',
			handler : function() {
				window.parent.hideParamWin();
			}
		}]
	});
	
	
	//查询功能
	var queryParamStore = function(){
			var targetString = Ext.getCmp('search_param').getValue();
			if(targetString == ''){
				store.load();
				return;
			}
			store.filterBy(function(record, id){
				if(record.get('ParamName').indexOf(targetString)!= -1 ){
					   return true;
				}
				if(record.get('ParamType').indexOf(targetString)!= -1 ){
					   return true;
				}
				if(record.get('ParamValue').indexOf(targetString)!= -1 ){
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
						{ xtype:'label', width:55,text:'名    称 : ' },
						{ xtype:'spacer', width:15 },
						{ xtype:'textfield', emptyText:'请输入关键字...',cls : 'key', width:220, id:'search_param' ,
							listeners :{
							  specialkey : function(field, e) {
			                    if (e.getKey() == Ext.EventObject.ENTER) {
							            queryParamStore();
			                    }
			               	  }
							}
						},
						{ xtype:'spacer', width:15 },
						{ xtype:'tbbutton', text:'查询',width:60,iconCls :'btn',labelAlign : 'right', handler:queryParamStore}
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
							region : 'center',
							xtype : 'panel',
							width : 600,
							height : 400,
							split : true,
							items : [grid]
						}]
			});

});