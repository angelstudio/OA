var currentTaskId=request("currentTaskId");
var taskid=request("taskid");
var billType=request("billType");
var id=request("id");
if(!!taskid){
	//工作流进入;追加表头按钮信息
	$.ajax({
		type : 'POST',
		url : 'jbpm_Add_FORM',
		data:'taskid='+taskid+"&billType="+billType+"&id="+id,
		dataType : 'json',
		contentType : 'application/x-www-form-urlencoded; charset=utf-8',
		success : function(data) {
		    var butHtml=data.workButInfo;
			$('#jbpm_toolbar').html(butHtml);
			for(var i in data){
				if(i!="workButInfo"){
					$("#"+i).val(data[i]);
				}
			};
			$("body").append('<div id="fjbpm"><iframe id="fjbpmLoc"  frameborder="0" style="width:100%;height:99%;height:100%\9;" src="about:blank"></iframe></div>');
			loadDialogBox_JBPM();
		}
	});
	
}

function loadDialogBox_JBPM(){
    $("#fjbpm").omDialog({
        autoOpen: false,
        width:300,
        height: 250,
        modal: true
    });
    $("#fjbpm").omDialog({onClose : function(event) {onClose(event)}});//添加对话框关闭时事件 add by qingfeng_li 2014-1-8
};
function openDialogBox_JBPM(){
    $( "#fjbpm").omDialog('open');
    $('#fjbpmLoc').attr('src','../../jbpm/jsp/jbpm_sh.jsp');
    return false;
	}
/**
 * @author Action
 * @param fid
 * @param formName
 * @return
 * @describe 流程提交
 */
function jbpmSubmit(fid,formName){
	$.ajax({
		type : 'POST',
		url : 'jbpmSubmit',
		data:'id='+fid+"&type="+formName,
		dataType : 'text',
		contentType : 'application/x-www-form-urlencoded; charset=utf-8',
		success : function(data) {
			if(data=="JBPM_SUCCESS"){
				alert("提交成功");
			}else{
				alert("提交失败");
			}
		}
	});
}
/**
 * @author Action
 * @date 2017-05-01
 * @describe 工作流审核
 * @return
 */
function jbpmAudit(){
	openDialogBox_JBPM();
};
