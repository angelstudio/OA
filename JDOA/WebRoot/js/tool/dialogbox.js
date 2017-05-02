 	
 	function openDialogBox(url){
 	 	if(url==null){
       		return ;
       	}
        $( "#dialog-modal").omDialog('open');
        var frameLoc=window.frames[0].location;
        frameLoc.href = url;
        return false;
 	}
    function loadDialogBox() {
        $("#dialog-modal").omDialog({
            autoOpen: false,
            width:535,
            height: 465,
            modal: true
        });
        $("#dialog-modal").omDialog({onClose : function(event) {onClose(event)}});//添加对话框关闭时事件 add by qingfeng_li 2014-1-8
    };
     	
    function loadDialog(w,h) {
        $("#dialog-modal").omDialog({
            autoOpen: false,
            width:w,
            height: h,
            modal: true
        });
         $("#dialog-modal").omDialog({onClose : function(event) {onClose(event)}});//添加对话框关闭时事件 add by qingfeng_li 2014-1-8
    };
    
    //添加对话框关闭时事件 add by qingfeng_li 2014-1-8
    function onClose(event){
    
    }
    
    function loadDialogBox_JBPM() {
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