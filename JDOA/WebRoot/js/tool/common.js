	//创建xmlHttpRequest请求
	function createXMLHttpReqeust() {
			var request;
			if (window.XMLHttpRequest) { // Mozilla,...
			    request = new XMLHttpRequest();
			    }else if (window.ActiveXObject) { // IE
			       request = new ActiveXObject("Msxml2.XMLHTTP"); 
			 }
			return request;
		};
		
		//发送ajax请求async为true时为异步 ,false时为同步
		function sendAjaxReqAsync(reqMethod,url,postParam,parseMsg200,parseMsg404,parseMsg500,loading,async){
				//创建 XMLHttpRequest 对象
				var request = createXMLHttpReqeust();
							
				//通过XMLHttpRequest发送消息
				request.open(reqMethod,url,async);
				if("post"==reqMethod){
						request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8 ");
				}
				
				//注册事件，通过XMLHttpRequest获取响应内容
				request.onreadystatechange = function(){
					if(request.readyState==4){
						if(request.status==200){
						
							if(parseMsg200)
								parseMsg200(request);
						}else if(request.status==404){
							if(parseMsg404)
								parseMsg404(request);
						}else if(request.status==500){
							if(parseMsg500)
								parseMsg500(request);
						}
					}else{
						if(loading)
							loading(request);				
					}
				};
				if("post"==reqMethod){
					request.send(postParam);
				}else{
					request.send(null);    //firefox
				}
		}		
		
	//发送ajax请求
	function sendAjaxReq(reqMethod,url,postParam,parseMsg200,parseMsg404,parseMsg500,loading){
			//创建 XMLHttpRequest 对象
			var request = createXMLHttpReqeust();
						
			//通过XMLHttpRequest发送消息
			request.open(reqMethod,url);
			if("post"==reqMethod){
					request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8 ");
			}
			
			//注册事件，通过XMLHttpRequest获取响应内容
			request.onreadystatechange = function(){
				  
				if(request.readyState==4){
					if(request.status==200){
					
						if(parseMsg200)
							parseMsg200(request);
					}else if(request.status==404){
						if(parseMsg404)
							parseMsg404(request);
					}else if(request.status==500){
						if(parseMsg500)
							parseMsg500(request);
					}
				}else{
					if(loading)
						loading(request);				
				}
			};
			if("post"==reqMethod){
				request.send(postParam);
			}else{
				request.send(null);    //firefox
			}
		};
		
		//工具栏跟随滚动条移动 add by qingfeng_li 2014-3-14
       $(window).scroll(function(){
           var top = $('#toolbarContain').offset().top + $('#toolbarContain').height();
           var windowScroll = $(window).scrollTop();
           if( top <= windowScroll ){
               $("#toolbar").addClass("toolbar-float");
           }else{
               $("#toolbar").removeClass("toolbar-float");
           }
       });
       
   	//新页签打开	
   	function openNewTabByName(tabName,url,fname){
   			if(url==null || url ==""){
   				alert("无请求资源！");
   				return ;
   			}
   			if(fname=='main'){
   			  parent.$('#center-tab').omTabs('activate', 0);
   			  return;
   			}
   			var tabId = parent.tabElement.omTabs('getAlter', 'tab_'+fname);
   			if (tabId) {
   				parent.$('#center-tab').omTabs('activate', tabId);
   			}else{
   			 var total = parent.$('#center-tab').omTabs('getLength');
   			 var idx = 'tab_'+fname;
   			 var hgt = $(window).height()-85;
   			 parent.$('#center-tab').omTabs('add', {
   			    index : total,
   			    title : tabName,
   			    content : "<iframe id='"+idx+"' border=0 frameBorder='no' name='inner-frame' src="+url+" height="+hgt+" width='100%'></iframe>",
   			    tabId : idx,
   			    closable : true
   			});
   		}
   	}
   	
   	//新页签打开	
   	function openNewTab(tabName,url){
   			if(url==null || url ==""){
   				alert("无请求资源！");
   				return ;
   			}
   			var total = parent.$('#center-tab').omTabs('getLength');
   			var idx = Math.round(Math.random()*10000);
   			var hgt = $(window).height();
   			parent.$('#center-tab').omTabs('add', {
   			    index : total,
   			    title : tabName,
   			    content : "<iframe id='"+idx+"' border=0 frameBorder='no' name='inner-frame' src="+url+" height="+hgt+" width='100%'></iframe>",
   			    tabId : idx,
   			    closable : true
   			});
   	}
		//移除控件 
		function removeNode(id){
			var obj = document.getElementById(id);  
			if(obj!=null){
	    		obj.parentNode.removeChild(obj);	
			}
		}

		//单头F7选择后填充数据 add by qingfeng_li 2013-8-2
		function fillDataByHeadF7(rowData){
		
		}
		
		//分录F7选择后填充数据 add by qingfeng_li 2013-8-2
		function fillDataByEntryF7(rowData){
			//(var key in rowData.queryFieldMap){
			//(key); //别名
			//(rowData.queryFieldMap[key]);//字段名
			//(rowData[rowData.queryFieldMap[key]]);//字段值
		}
		
		//设置分录控件值   add by qingfeng_li 2013-8-7
		function setEntryValue(basicdata,id,value){
			document.getElementById(basicdata+id).value = value;

		}

		//行背景样式处理 add by qingfeng_li 2014-3-6
		function dealEntryRowClasses(rowIndex,rowData){
			if(rowIndex%2==0){
				return 'oddRow';
			}else{
				return 'evenRow';
			}
		}

