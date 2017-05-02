Ext.onReady(function() {
	var store = new Ext.data.JsonStore({
				autoDestroy : true,
				url : 'queryBillField.action?formName='+encodeURI(window.parent.Ext.getCmp("billFormBox").getValue()),
				storeId : 'myStore',
				fields : [{
							name : 'fname',
							mapping : 'fname',
							type : 'string'
						}, {
							name : 'faliasname',
							mapping : 'faliasname',
							type : 'string'
						}, {
							name : 'ftype',
							mapping : 'ftype',
							type : 'string'
						}]
			});
	store.reload();
	
	// 表格
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var grid = new Ext.grid.GridPanel({
		frame : true,
		width : 600,
		height : 340,
		title : "引用变量",
		store : store,
		sm : sm,
		viewConfig : {
		       forceFit : true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
		},
		columns : [{
					header : "字段名称",
					dataIndex : 'fname'
				}, {
					header : "字段别名",
					dataIndex : 'faliasname'
				}, {
					header : "类型",
					dataIndex : 'ftype'
					,renderer:genderFieldEnum
				}],
		listeners : {
			"rowdblclick" : function(grid, rowIndex, e) {
				window.parent
						.fillBackReferData(grid.getStore().getAt(rowIndex).data);
			}
		},
		buttons : [{
			text : '确认',
			iconCls : 'picon19',
			handler : function() {
				var selectRow = grid.getSelectionModel().getSelected();//
				if (selectRow != null) {
					window.parent.fillBackReferData(grid.getSelectionModel()
							.getSelected().data);
				} else {
					Ext.MessageBox.alert("提示", "请先选择行！      ");
				}
				window.parent.hideReferWin();
			}
		}, {
			text : '取消',
			iconCls : 'picon09',
			handler : function() {
				window.parent.hideReferWin();
			}
		}]
	});
	
	
	//把具体的枚举值转换成相应的枚举别名
	function genderFieldEnum(abbr){
		if(abbr=='2'){
			return '<span >数字</span>';
		}else{
			return '<span >文本</span>';
		}
	}
	
	//查询功能
	var queryReferStore = function(){
			var targetString = Ext.getCmp('search_refer').getValue();
			if(targetString == ''){
				store.load();
				return;
			}
			store.filterBy(function(record, id){
				if(record.get('faliasname').indexOf(targetString)!= -1 ){
					   return true;
				}
				if(record.get('fname').indexOf(targetString)!= -1 ){
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
						{ xtype:'textfield', emptyText:'请输入关键字...',cls : 'key', width:220, id:'search_refer',
							listeners :{
							  specialkey : function(field, e) {
			                    if (e.getKey() == Ext.EventObject.ENTER) {
							            queryReferStore();
			                    }
			               	  }
							}
						},
						{ xtype:'spacer', width:15 },
						{ xtype:'tbbutton', id:'search', text:'查询',width:60,iconCls :'searchBtn',labelAlign : 'center', handler:queryReferStore}
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
							layout:'fit',
							split : true,
							items : [dataForm]
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