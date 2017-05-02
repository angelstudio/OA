function initNodesJs() {
	addParamsjs("../js/mailto.js");
	addParamsjs("../js/case.js");
	addParamsjs("../js/notice.js");
	addParamsjs("../js/swimlane.js");
	addParamsjs("../js/change.js");
	addParamsjs("../js/form.js");
	addParamsjs("../js/autoDelegate.js");
	addParamsjs("../js/method.js");
	addParamsjs("../js/delegate.js");
	addParamsjs("../js/sql.js");
	addParamsjs("../js/control.js");

}

function addParamsjs(src) {
	js_element = document.createElement("script");
	js_element.setAttribute("type", "text/javascript");
	js_element.setAttribute("src", src);
	document.body.appendChild(js_element);
}
function createNodeID(nodetype) {
	var guid = nodetype + "_";
	for (var i = 1; i <= 32; i++) {
		var n = Math.floor(Math.random() * 16.0).toString(16);
		guid += n;
		if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
			guid += "-";
	}
	return guid;
}
