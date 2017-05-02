var svgXml=null;
var xml=null;
var xmlWin=null;
var arr=[];
var ids = null;
var currentNodeId =null;

var width=55;
var height=90;
var max_x=0;
var max_y=0;
					
function init(){
	if (xml == null) {
		xml = document.implementation.createDocument("","",null);
		var p = xml.createProcessingInstruction("xml",
				"version='1.0' encoding='UTF-8'");
		xml.appendChild(p);
		var root = xml.createElement("process");
		root.setAttribute("name", "新建流程");
		root.setAttribute("xmlns", "http://jbpm.org/4.4/jpdl");
		xml.appendChild(root);
	}
	
	if (svgXml == null) {
		svgXml = document.implementation.createDocument("","",null);
		var p = svgXml.createProcessingInstruction("xml",
				"version='1.0' standalone='no'");
		svgXml.appendChild(p);
		
		var root = svgXml.createElement("svg");//创建svg节点
		root.setAttribute("width", "100%");
		root.setAttribute("height", "100%");
		root.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		svgXml.appendChild(root);
		
		//创建连线的箭头指向
		var defs = svgXml.createElement("defs");//创建defs节点
		
		//定义一个正常规范的箭头
		var marker = svgXml.createElement("marker");//创建marker节点
		var path = svgXml.createElement("path");//创建path节点
		
		addSvgXmlAttribute(path,"d","M2,2 L2,11 L10,6 L2,2");

		addSvgXmlAttribute(marker,"id","markerArrow");
		addSvgXmlAttribute(marker,"markerWidth",13);
		addSvgXmlAttribute(marker,"markerHeight",13);
		addSvgXmlAttribute(marker,"refx",2);
		addSvgXmlAttribute(marker,"refy",6);
		addSvgXmlAttribute(marker,"orient","auto");
		addSvgXmlAttribute(marker,"style","fill: #000000;");
		
		//顶一个选中的箭头的样式
		var selectMarker = svgXml.createElement("marker");//创建marker节点
		var selectPath = svgXml.createElement("path");//创建path节点
		
		addSvgXmlAttribute(selectPath,"d","M2,2 L2,11 L10,6 L2,2");
		addSvgXmlAttribute(selectMarker,"id","selectMarker");
		addSvgXmlAttribute(selectMarker,"markerWidth",13);
		addSvgXmlAttribute(selectMarker,"markerHeight",13);
		addSvgXmlAttribute(selectMarker,"refx",2);
		addSvgXmlAttribute(selectMarker,"refy",6);
		addSvgXmlAttribute(selectMarker,"orient","auto");
		addSvgXmlAttribute(selectMarker,"markerUnits","userSpaceOnUse");
		addSvgXmlAttribute(selectMarker,"style","fill: green;");
		
		selectMarker.appendChild(selectPath);
		marker.appendChild(path);
		
		defs.appendChild(selectMarker);
		defs.appendChild(marker);
		
		root.appendChild(defs);
		
		//创建g点
		var g = svgXml.createElement("g");//创建g节点
		addSvgXmlAttribute(g,"fill","#dfe7f4");
		addSvgXmlAttribute(g,"stroke-width",3);
		root.appendChild(g);
	}
}

//为节点添加属性
function addSvgXmlAttribute(node, attr, value) {
	var attribute = svgXml.createAttribute(attr);
	attribute.value = value;
	node.setAttributeNode(attribute);
}

//解析xml对象成svg需要的格式
function changeXmlToSvg(changeFlag){
	//每个流程中只有一个开始节点和结束节点
	var start=xml.documentElement.getElementsByTagName("start");
	var end=xml.documentElement.getElementsByTagName("end");
	
	//流程中存在多个消息、决策和任务节点
	var mail=xml.documentElement.getElementsByTagName("mail");
	var decision=xml.documentElement.getElementsByTagName("decision");
	var task=xml.documentElement.getElementsByTagName("task");
	
	
	arr.push(start);
	arr.push(end);
	arr.push(mail);
	arr.push(decision);
	arr.push(task);
	createSvgRect(changeFlag);
}

//解析xml对象中transition  成svg需要的格式
function changeXmlTransitionToSvg(changeFlag){
	//每个流程中只有一个开始节点和结束节点
	var transition=xml.documentElement.getElementsByTagName("transition");
	var end=xml.documentElement.getElementsByTagName("end");
	createSvgLine(transition,changeFlag,end);
}

function createSvgLine(transition,changeFlag,end){
	var g=svgXml.documentElement.getElementsByTagName("g")[0];//创建g点
		if(transition.length>0){//如开始节点、任务节点等，这些都是数组形式的数据
			for(var i=0;i<transition.length;i++){
				var from=transition[i].getAttribute("from");//
				var to=transition[i].getAttribute("to");//
				var name=transition[i].getAttribute("name");//
			 
				var point=getPoint(from,to);
				
				var p_arr=point.split(",");
				
				var style=null;
				
				var line=svgXml.createElement("line");
				//创建线条的属性
				addSvgXmlAttribute(line,"x1",p_arr[0]);
				addSvgXmlAttribute(line,"y1",p_arr[1]);
				addSvgXmlAttribute(line,"x2",p_arr[4]);
				addSvgXmlAttribute(line,"y2",p_arr[5]);
				
				if(p_arr[6]==1){
					style="stroke:green;fill:green;stroke-width:2;";
					addSvgXmlAttribute(line,"marker-end","url(#selectMarker)");
				}else{
					style="stroke:#000000;fill:#000000;stroke-width:1;";
					addSvgXmlAttribute(line,"marker-end","url(#markerArrow)");
				}
				
				if(!changeFlag&&p_arr[6]==1&&p_arr[7]==1){//代表整个流程已经走完
					if(end.length>0){
						for(var j=0;j<end.length;j++){
							if(to==end[j].getAttribute("name")){
								style="stroke:green;fill:green;stroke-width:2;";
								addSvgXmlAttribute(line,"marker-end","url(#selectMarker)");
							}
						}
					}
				}
				
				addSvgXmlAttribute(line,"style",style);
				g.appendChild(line);
				
				//创建线条中的文字说明
				var text=svgXml.createElement("text");
				addSvgXmlAttribute(text,"x",(p_arr[0]*1+p_arr[2]*1)/2);
				addSvgXmlAttribute(text,"y",(p_arr[1]*1+p_arr[3]*1)/2);
				addSvgXmlAttribute(text,"fill","black");
				addSvgXmlAttribute(text,"text-anchor","middle");
				addSvgXmlAttribute(text,"font-size",12);
				//创建文本
				var info=svgXml.createTextNode(name);
				text.appendChild(info);
				g.appendChild(text);
			}
	}
}


//根据两端的矩形坐标计算线条的实际坐标
function getRealPoint(x1,y1,x2,y2){
	var fx1=null;
	var fy1=null;
	var fx2=null;
	var fy2=null;
	var px=x2-x1;
	var py=y2-y1;
	var endx=null;
	var endy=null;
	
	if((px>0&&py>=0&&px>=py)||(px>0&&py<=0&&px>=Math.abs(py))){
		fx1=x1*1+110;
		fy1=y1*1+25;
		fx2=x2;
		fy2=y2*1+25;
		endx=fx2-10;
		endy=fy2;
	}else if((px<0&&py>=0&&Math.abs(px)>=py)||(px<0&&py<=0&&Math.abs(px)>=Math.abs(py))){
		fx1=x1;
		fy1=y1*1+25;
		fx2=x2*1+110;
		fy2=y2*1+25;
		endx=fx2+10;
		endy=fy2;
	}else if((py>=0&&px>0&&px<=py)||(py>=0&&px<0&&Math.abs(px)<=py)){
		fx1=x1*1+55;
		fy1=y1*1+50;
		fx2=x2*1+55;
		fy2=y2;
		endx=fx2;
		endy=fy2-10;
		
	}else if((py<=0&&px>0&&Math.abs(px)<=py)||(py<=0&&px<0&&Math.abs(px)<=Math.abs(py))){
		fx1=x1*1+55;
		fy1=y1;
		fx2=x2*1+55;
		fy2=y2*1+50;
		endx=fx2;
		endy=fy2-10;
	}
	return fx1+","+fy1+","+fx2+","+fy2+","+endx+","+endy;
}


//根据线条的两端的端点名称获得线条的起始坐标和终点坐标，返回一个字符串
function getPoint(from,to){
	var fpoint=null;
	var tpoint=null;
	var mark=0;
	var fromComplete=0;
	var toComplete=0;
	if(arr.length>0){
		for(var i=0;i<arr.length;i++){
			var node=arr[i];
			if(node.length>0){//如开始节点、任务节点等，这些都是数组形式的数据\
				for(var j=0;j<node.length;j++){
					if(node[j].getAttribute("name")==from){
						fpoint=node[j].getAttribute("g");
						if(node[j].getAttribute("mark")!=0){
							fromComplete=1;
						}
					}
					if(node[j].getAttribute("name")==to){
						tpoint=node[j].getAttribute("g");
						if(node[j].getAttribute("mark")!=0){
							toComplete=1;
						}
					}
					if(fpoint!=null&&tpoint!=null){
						var x1=fpoint.split(",")[0];
						var y1=fpoint.split(",")[1];
						var x2=tpoint.split(",")[0];
						var y2=tpoint.split(",")[1];
						
						if(fromComplete==1&&toComplete==1){
							mark=1;
						}
						
						return getRealPoint(x1,y1,x2,y2)+","+mark+","+fromComplete;
					}
				}
			}
		}
	}
	return null;
}

//根据节点信息创建svg需要的信息 node_arr节点数组
function createSvgRect(changeFlag){
	var flag=true;
	var g=svgXml.documentElement.getElementsByTagName("g")[0];//创建g点
	if(arr.length>0){
		for(var i=0;i<arr.length;i++){
			var node=arr[i];
			if(node.length>0){//如开始节点、任务节点等，这些都是数组形式的数据
				for(var j=0;j<node.length;j++){
					 	var typeValue=node[j].tagName;//获得是什么类型的节点
						var name=node[j].getAttribute("name");//图标内显示的文字
						var id=node[j].getAttribute("id");//节点的id
						var sit=node[j].getAttribute("g").split(",");//获得节点的坐标
						var x=sit[0];
						var y=sit[1];
						
						//计算最大的x、y坐标
						max_x=max_x*1>x*1?max_x:x;
						max_y=max_y*1>y*1?max_y:y;
					 	
						var style=null;
						var path=null;
						var flag=false;
						var returnValue=isPassNode(id);
						
						if(typeValue=="start"){//"流程开始"节点定义为已完成
							returnValue=1;
						}
						
						if(returnValue==0){//未走的节点
							style="fill:white;stroke-width:0px;stroke:white;display:block;cursor:pointer;position:absolute";
							path="../images/icon/"+typeValue+".gif";
						}else if(returnValue==1){//已走过的节点
							style="fill:white;stroke-width:0px;stroke:white;display:block;cursor:pointer;position:absolute";
							path="../images/icon/"+typeValue+"2.gif";
						}else if(returnValue==2){//当前节点
							style="fill:white;stroke-width:0px;stroke:white;display:block;cursor:pointer;position:absolute";
							path="../images/icon/"+typeValue+".gif";
							flag=true;
						}
						if(!changeFlag&&typeValue=="end"){//代表流程已经走完，设置结束节点为已完成
							style="fill:white;stroke-width:0px;stroke:white;display:block;cursor:pointer;position:absolute";
							path="../images/icon/"+typeValue+"2.gif";
							returnValue=1;
						}
						
						node[j].setAttribute("mark",returnValue);
						
						var rect=svgXml.createElement("rect");
					 
						//单纯的创建矩形图标
						addSvgXmlAttribute(rect,"x",x);
						addSvgXmlAttribute(rect,"y",y);
						addSvgXmlAttribute(rect,"width",width);
						addSvgXmlAttribute(rect,"height",height);
						addSvgXmlAttribute(rect,"rx",5);
						addSvgXmlAttribute(rect,"ry",5);
						addSvgXmlAttribute(rect,"style",style);
						g.appendChild(rect);
						
						//创建引入的类别小图标
						var image=svgXml.createElement("image");
						addSvgXmlAttribute(image,"x",x*1+5);
						addSvgXmlAttribute(image,"y",y*1+5);
						addSvgXmlAttribute(image,"width",50);
						addSvgXmlAttribute(image,"height",50);
						addSvgXmlAttribute(image,"flag",flag);
						addSvgXmlAttribute(image,"fill-opacity","0");
						addSvgXmlAttribute(image,"xlink:href",path);
						g.appendChild(image);

						//创建类别字母
						var textLetter=svgXml.createElement("text");
						addSvgXmlAttribute(textLetter,"x",x*1+90);
						addSvgXmlAttribute(textLetter,"y",y*1+15);
						addSvgXmlAttribute(textLetter,"fill","black");
						addSvgXmlAttribute(textLetter,"text-anchor","middle");
						addSvgXmlAttribute(textLetter,"font-size","12");
						
						//创建文本
						var letter=svgXml.createTextNode(typeValue);
						textLetter.appendChild(letter);
						//g.appendChild(textLetter);
						
						//创建节点信息
						var text=svgXml.createElement("text");
						addSvgXmlAttribute(text,"x",x*1+30);
						addSvgXmlAttribute(text,"y",y*1+70);
						addSvgXmlAttribute(text,"fill","black");
						addSvgXmlAttribute(text,"text-anchor","middle");
						addSvgXmlAttribute(text,"font-size","12");
						//创建文本
						var info=svgXml.createTextNode(name);
						text.appendChild(info);
						g.appendChild(text);
						
				}
			}
		}
	}
}


//验证节点在是否已经审批完成或处于当前节点
function isPassNode(fid){
	if(currentNodeId==fid){
		return 2;
	}
	if(ids.length>0){
		for(var i=0;i<ids.length;i++){
			if(ids[i]==fid){
				return 1;
			}
		}
	}
	return 0;
}
//获得组装好的svg字符串
function getSvgXml(){
	var str = svgXml.xml.replace(/></g, '>\n\r<');
	str=str.replace("<?xml version='1.0' standalone='no'?>",'<?xml version="1.0" standalone="no"?>\r\n<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">');
	str = str.replace(/xmlns=\"\"/g, '');
	return str;
}