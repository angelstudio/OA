<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	    <script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
        <script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
        <script type="text/javascript" src="../../js/tool/stringbuffer.js"></script>
        <script type="text/javascript" src="../../js/tool/time.js"></script>
        <script type="text/javascript" src="../../js/tool/jsonstr.js"></script>
        <script type="text/javascript" src="../../js/tool/urlUtil.js"></script>
	     <link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
	     <link rel="stylesheet" type="text/css" href="../../css/tool/ui/default/om-default.css" />
	     <link rel="stylesheet" type="text/css" href="../../css/tool/ui/main.css" />
	     <link rel="stylesheet" type="text/css" href="../../css/mycss.css" />
	</head>
	<style>
	  input{
	    width:150px;
	    height:20px
	  };
	  select {
	    width:150px;
	    height:24px;
	  };
	  td{
	    align:right;
	  }
	</style>
	<body>
	 <div id="toolbar">
	 <button id="fbc">保存</button>
	 <button id="fgb">关闭</button>
	 </div>
	 <div>
	   <table>
	      <tr>
	         <td>人员名称:</td>
	         <td><input id="fname" name="fname" class="sText"/><input id="fid" name="fname"  type="hidden"/></td>
	         <td>身份证号码:</td>
	         <td><input id="fsfzhm" name="fsfzhm" class="sText"/></td>
	         <td>性别:</td>
	         <td>
	            <select id="fsex" name="fsex" class="sSelect" >
	                <option value=0></option>
	                <option value=1>男</option>
	                <option value=2>女</option>
	            </select>
	         </td>
	      </tr>
	      <tr>
	         <td>出生日期:</td>
	         <td><input id="fcsrq" name="fcsrq" onkeypress="return false;" onfocus="SelectDate(this,&quot;yyyy-MM-dd&quot;);" style="background:url(../../image/basic/date.png) no-repeat 130px  0px;" /></td>
	         <td>民族:</td>
	         <td><input id="fmz" name="fmz" class="sText"/></td>
	         <td>政治面貌:</td>
	         <td><input id="fzzmm" name="fzzmm" class="sText"/></td>
	      </tr>
	      <tr>
	         <td>籍贯:</td>
	         <td><input id="fjg" name="fjg" class="sText"/></td>
	         <td>户籍地址:</td>
	         <td><input id="fhjdz" name="fhjdz" class="sText"/></td>
	         <td>入职日期:</td>
	         <td><input id="frzrq" name="frzrq" onkeypress="return false;" onfocus="SelectDate(this,&quot;yyyy-MM-dd&quot;);" style="background:url(../../image/basic/date.png) no-repeat 130px  0px;"/></td>
	      </tr>
	      <tr>
	         <td>本人电话:</td>
	         <td><input id="fbrdh" name="fbrdh" class="sText"/></td>
	         <td>参加工作时间:</td>
	         <td><input id="fcjgzsj" name="fcjgzsj"  onkeypress="return false;" onfocus="SelectDate(this,&quot;yyyy-MM-dd&quot;);" style="background:url(../../image/basic/date.png) no-repeat 130px  0px;"/></td>
	         <td>入党时间:</td>
	         <td><input id="frdsj" name="frdsj" onkeypress="return false;" onfocus="SelectDate(this,&quot;yyyy-MM-dd&quot;);" style="background:url(../../image/basic/date.png) no-repeat 130px  0px;"/></td>
	      </tr>
	      <tr>
	         <td>合同执行日期:</td>
	         <td><input id="fhtzxrq" name="fhtzxrq" onkeypress="return false;" onfocus="SelectDate(this,&quot;yyyy-MM-dd&quot;);" style="background:url(../../image/basic/date.png) no-repeat 130px  0px;"/></td>
	         <td>合同结束日期:</td>
	         <td><input id="fhtjsrq" name="fhtjsrq" onkeypress="return false;" onfocus="SelectDate(this,&quot;yyyy-MM-dd&quot;);" style="background:url(../../image/basic/date.png) no-repeat 130px  0px;"/></td>
	         <td>合同签订单位:</td>
	         <td><input id="fhtqddw" name="fhtqddw" class="sText"/></td>
	      </tr>
	      <tr>
	         <td>紧急联系人:</td>
	         <td><input id="fjjlxr" name="fjjlxr" class="sText"/></td>
	         <td>紧急联系人电话:</td>
	         <td><input id="fjjlxrdh" name="fjjlxrdh" class="sText"/></td>
	         <td>转正日期:</td>
	         <td><input id="fzzrq" name="fzzrq" onkeypress="return false;" onfocus="SelectDate(this,&quot;yyyy-MM-dd&quot;);" style="background:url(../../image/basic/date.png) no-repeat 130px  0px;"/></td>
	      </tr>
	      <tr>
	         <td>学历:</td>
	         <td><input id="fxl" name="fxl" class="sText"/></td>
	         <td>座机:</td>
	         <td><input id="fzj" name="fzj" class="sText"/></td>
	         <td>学位学历:</td>
	         <td><input id="fxwxl" name="fxwxl" class="sText"/></td>
	      </tr>
	      <tr>
	         <td>是否续签:</td>
	         <td>
	            <select id="fsfxq" name="fsfxq" class="sSelect" >
	                <option value=0></option>
	                <option value=1>是</option>
	                <option value=2>否</option>
	            </select>
	         </td>
	         <td>是否转正:</td>
	         <td>
	             <select id="fsfzz" name="fsfzz" class="sSelect" >
	                <option value=0></option>
	                <option value=1>是</option>
	                <option value=2>否</option>
	            </select>
	         </td>
	         <td>所属部门:</td>
	         <td> <select id="fssbm" name="fssbm" class="sSelect" >
	              <option value=0></option>
	            </select></td>
	      </tr>
	      <tr>
		       <td>邮箱:</td>
		       <td><input id="femail" name="femail" class="sText"/></td>
		       <td>职位:</td>
		       <td><input id="fzw" name="fzw" class="sText"/></td>
		       <td>工号:</td>
		       <td><input id="fgh" name="fgh" class="sText"/></td>
	      </tr>
	      <tr>
		       <td>工作地址:</td>
		       <td><input id="fgzdz" name="fgzdz" class="sText"/></td>
	      </tr>
	  </table>
	 </div> 
	</body>
	<script type="text/javascript">
		  $(document).ready(function(){
		      var oper=request("oper");
		        if(oper==2){
		          $("#fbc").attr("disabled",true);
		        }
		            $.ajax({
  							type: 'POST',
  							url:'getOrg', 
  							dataType:'json', 							
  							contentType:'application/x-www-form-urlencoded; charset=utf-8',
  							success: function(data){
  							   for( var i in data){
  							     $("#fssbm").append('<option value="'+i+'">'+data[i]+'</option>');
  							   }
  							    var selectedRecords = parent.$('#ftab').omGrid('getSelections',true);
  							    setInput(selectedRecords);
  							}	
			            });	
			  $("#fbc").click(function(){
			      var jsonstr=JSON.stringify(getJson());
			       $.ajax({
  					   type: 'POST',
  					   url:'editPerson',
  					   data:"json="+ jsonstr,
  					   dataType:'text', 							
  					   contentType:'application/x-www-form-urlencoded; charset=utf-8',
  					   success: function(data){
  							 if(data=="ok"){
  							  alert("修改成功");
  							  $("#fbc").attr("disabled",true);
  							 }
  						}	
			        });	
			  });
		     $('#toolbar').omButtonbar({});
          });
          //获取json对象
          function getJson(){
             var json={};
             var fname=$("#fname").val();
             var fsfzhm=$("#fsfzhm").val();
             var fsex=$("#fsex").val();
             var fcsrq=$("#fcsrq").val();
             var fmz=$("#fmz").val();
             var fzzmm=$("#fzzmm").val();
             var fjg=$("#fjg").val();
             var fhjdz=$("#fhjdz").val();
             var frzrq=$("#frzrq").val();
             var fbrdh=$("#fbrdh").val();
             var fcjgzsj=$("#fcjgzsj").val();
             var frdsj=$("#frdsj").val();
             var fhtzxrq=$("#fhtzxrq").val();
             var fhtjsrq=$("#fhtjsrq").val();
             var fhtqddw=$("#fhtqddw").val();
             var fjjlxr=$("#fjjlxr").val();
             var fjjlxrdh=$("#fjjlxrdh").val();
             var fzzrq=$("#fzzrq").val();
             var fxl=$("#fxl").val();
             var fzj=$("#fzj").val();
             var fxwxl=$("#fxwxl").val();
             var fsfxq=$("#fsfxq").val();
             var fsfzz=$("#fsfzz").val();
             var fssbm=$('#fssbm option:selected').text();//选中的文本;
             var fbmid=$("#fssbm").val();
             var fid=$("#fid").val();
             var femail=$("#femail").val();
             var fzw=$("#fzw").val();
             var fgh=$("#fgh").val();
             var fgzdz=$("#fgzdz").val();
             json.femail=femail;
             json.fzw=fzw;
             json.fgh=fgh;
             json.fgzdz=fgzdz;
             json.fname=fname;
             json.fsfzhm=fsfzhm;
             json.fsex=fsex;
             json.fcsrq=fcsrq;
             json.fmz=fmz;
             json.fzzmm=fzzmm;
             json.fjg=fjg;
             json.fhjdz=fhjdz;
             json.frzrq=frzrq;
             json.fbrdh=fbrdh;
             json.fcjgzsj=fcjgzsj;
             json.frdsj=frdsj;
             json.fhtzxrq=fhtzxrq;
             json.fhtjsrq=fhtjsrq;
             json.fhtqddw=fhtqddw;
             json.fjjlxr=fjjlxr;
             json.fjjlxrdh=fjjlxrdh;
             json.fzzrq=fzzrq;
             json.fxl=fxl;
             json.fzj=fzj;
             json.fxwxl=fxwxl;
             json.fsfxq=fsfxq;
             json.fsfzz=fsfzz;
             json.fssbm=fssbm;
             json.fbmid=fbmid;
             json.fid=fid;
             return json;
          };
          //获取json对象
          function setInput(selectedRecords){
             $("#fid").val(selectedRecords[0].fid);
             $("#fname").val(selectedRecords[0].fname);
             $("#fsfzhm").val(selectedRecords[0].fsfzhm);
             $("#fsex").val(selectedRecords[0].fsex);
             $("#fcsrq").val(selectedRecords[0].fcsrq);
             $("#fmz").val(selectedRecords[0].fmz);
             $("#fzzmm").val(selectedRecords[0].fzzmm);
             $("#fjg").val(selectedRecords[0].fjg);
             $("#fhjdz").val(selectedRecords[0].fhjdz);
             $("#frzrq").val(selectedRecords[0].frzrq);
             $("#fbrdh").val(selectedRecords[0].fbrdh);
             $("#fcjgzsj").val(selectedRecords[0].fcjgzsj);
             $("#frdsj").val(selectedRecords[0].frdsj);
             $("#fhtzxrq").val(selectedRecords[0].fhtzxrq);
             $("#fhtjsrq").val(selectedRecords[0].fhtjsrq);
             $("#fhtqddw").val(selectedRecords[0].fhtqddw);
             $("#fjjlxr").val(selectedRecords[0].fjjlxr);
             $("#fjjlxrdh").val(selectedRecords[0].fjjlxrdh);
             $("#fzzrq").val(selectedRecords[0].fzzrq);
             $("#fxl").val(selectedRecords[0].fxl);
             $("#fzj").val(selectedRecords[0].fzj);
             $("#fxwxl").val(selectedRecords[0].fxwxl);
             $("#fsfxq").val(selectedRecords[0].fsfxq);
             $("#fsfzz").val(selectedRecords[0].fsfzz);
             $("#fssbm").val(selectedRecords[0].fbmid);
             $("#femail").val(selectedRecords[0].femail);
             $("#fgh").val(selectedRecords[0].fgh);
             $("#fzw").val(selectedRecords[0].fzw);
             $("#fgzdz").val(selectedRecords[0].fgzdz);
          }
    </script>
</html>