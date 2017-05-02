Ext.onReady(function() {
	var tableName = request("tableName");
	var store = new Ext.data.JsonStore( {
		autoDestroy : true,
		url : 'jbpm_queryRoleData.action',
		storeId : 'myStore',
		// root : "json",// json对象组
		fields : [ {
			name : 'fid',
			mapping : 'fid',
			type : 'string'
		}, {
			name : 'frolename',
			mapping : 'frolename',
			type : 'string'
		}, {
			name : 'fdescription',
			mapping : 'fdescription',
			type : 'string'
		} ]
	});
	store.load();

	// 表格
		var sm = new Ext.grid.CheckboxSelectionModel( {
			singleSelect : true
		});
		var grid = new Ext.grid.GridPanel(
				{
					frame : true,
					width : 580,
					height : 340,
					title : "任务",
					store : store,
					sm : sm,
					viewConfig : {
					      forceFit : true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
					},
					columns : [ {
						header : "角色名称",
						dataIndex : 'frolename'
					}, {
						header : "角色描述",
						dataIndex : 'fdescription',
						width : 200
					} ],
					listeners : {
						"rowdblclick" : function(grid, rowIndex, e) {
							if( tableName == "dalegateRole" ){
								window.parent.fillBackDelegateData(grid.getStore()
										.getAt(rowIndex).data, tableName);
							}else if( tableName == "noticeRole" ){
								window.parent.fillBackNoticeData(grid.getStore()
										.getAt(rowIndex).data, tableName);
							}
						}
					},
					buttons : [
							{
								text : '确认',
								iconCls : 'picon19',
								handler : function() {
									var selectRow = grid.getSelectionModel()
											.getSelected();//
									if (selectRow != null) {
										if( tableName == "dalegateRole" ){
											window.parent.fillBackDelegateData(grid
													.getSelectionModel()
													.getSelected().data, tableName);
										}else if( tableName == "noticeRole" ){
											window.parent.fillBackNoticeData(grid
													.getSelectionModel()
													.getSelected().data, tableName);
										}
									} else {
										Ext.MessageBox.alert("提示",
												"请先选择行！      ");
									}
									if( tableName == "dalegateRole" ){
										window.parent.hideDelegateWin(tableName);
									}else if( tableName == "noticeRole" ){
										window.parent.hideNoticeWin(tableName);
									}
								}
							}, {
								text : '取消',
								iconCls : 'picon09',
								handler : function() {
									if( tableName == "dalegateRole" ){
										window.parent.hideDelegateWin(tableName);
									}else if( tableName == "noticeRole" ){
										window.parent.hideNoticeWin(tableName);
									}
								}
							} ]
				});
				
			//查询功能
			var queryRoleStore = function(){
				var targetString = Ext.getCmp('search_Role').getValue();
				if(targetString == ''){
					store.load();
					return;
				}
				store.filterBy(function(record, id){
					if(record.get('frolename').indexOf(targetString)!= -1 ){
						   return true;
					}
					if(record.get('fdescription').indexOf(targetString)!= -1 ){
						   return true;
					}
					return false;
				});
			}		
				
	
		var dataForm = new Ext.FormPanel( {
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
						{ xtype:'label', width:55,text:'名   称 : ' },
						{ xtype:'spacer', width:15 },
						{ xtype:'textfield', emptyText:'请输入关键字...',cls : 'key', width:220, id:'search_Role',
							listeners :{
							  specialkey : function(field, e) {
			                    if (e.getKey() == Ext.EventObject.ENTER) {
							            queryRoleStore();
			                    }
			               	  }
							}
						},
						{ xtype:'spacer', width:15 },
						{ xtype:'button', id:'search', text:'查询',width:60,iconCls :'searchBtn',labelAlign : 'center', handler:queryRoleStore}
					]
				}
		});
		var viewport = new Ext.Viewport( {
			layout : 'border',
			renderTo : Ext.getBody(),
			items : [ {
				region : 'north',
				xtype : 'panel',
				width : 600,
				height : 65,
				split : true,
				layout:'fit',
				items : [ dataForm ]
			}, {
				region : 'center',
				xtype : 'panel',
				width : 400,
				height : 400,
				split : true,
				layout:'fit',
				items : [ grid ]
			} ]
		});

	});