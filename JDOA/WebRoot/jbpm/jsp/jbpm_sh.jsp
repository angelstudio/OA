<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<script type="text/javascript" src="../../js/tool/controlCommon.js"></script>
		<script type="text/javascript" src="../../js/tool/jquery.min.js"></script>
		<script type="text/javascript" src="../../js/tool/operamasks-ui.min.js"></script>
		<script type="text/javascript" src="../../js/tool/common.js"></script>
		<script type="text/javascript" src="../js/json2.js"></script>
		<script type="text/javascript" src="../../js/tool/urlUtil.js"></script>
		<script type="text/javascript" src="../js/BPJbpmCommon.js"></script>
		<link rel="stylesheet" type="text/css"  href="../../css/tool/ui/default/om-default.css" />

	</head>
	<script type="text/javascript">
	  $(document).ready(function() {
		$("#fsure").click(function(){
		  $("#fsure").attr("disabled",true);
		  var currentTaskId= parent.currentTaskId;
		  var taskid= parent.taskid;
		  var billType= parent.billType;
		  var id= parent.id;
		  var spjg=$("input[type='radio']:checked").val();
		  var spyj=$("#fspyj").val();
		  var param = "id="+id+"&type="+billType+"&spjg="+spjg+"&spyj="+spyj+"&currentTaskId="+currentTaskId;
		     $.ajax({
              type:'POST',
              url:'jbpmAudit',
              data:param,
              dataType:'text',
              contentType:'application/x-www-form-urlencoded; charset=utf-8',
              success:function(data){
                  alert("审核成功");
                  parent.$( "#fjbpm").omDialog('close');
              },
             error:function(XMLResponse){
                   alert("系统出错");
                   parent.$( "#fjbpm").omDialog('close');
             }
           });
		});
		
		$('#toolbar').omButtonbar({});
		});
</script>
	<div id="toolbar">
		<input type="button" id="fsure" value="确认" />
	</div>
    <div>
       <table>
          <tr>
             <td>审批结果</td><td>同意<input name="fspjg" type="radio" value="agree" /> 不同意<input name="fspjg" type="radio" value="disagree" /></td>
          </tr>
            <tr>
             <td>审批意见</td><td><textarea id="fspyj" rows="3" style="width:100%;"></textarea></td>
          </tr>
       </table>
    </div>
</html>