
// 返回界面控件id与值的json字符串
function loadData() {
	debugger;
	var buffer = new StringBuffer();
	buffer.append("{");
	var tables = document.getElementsByTagName("table");
	for (var ids = 0; ids < tables.length; ids++) {
		var headTable = tables[ids];
		var headTableRows = headTable.rows;
		var headRowsLength = headTableRows.length;
		for (var i = 0; i < headRowsLength; i++) {
			var headTableColumns = headTable.rows[i].cells;
			for (var j = 0; j < headTableColumns.length; j++) {
				var cell = headTableRows[i].cells[j];
				var id = cell.childNodes[0].id;
				if (typeof(id) != "undefined" && id != '') {// 去掉id为空的控件取值
					var value = '';
					buffer.append("'");
					buffer.append(id);
					buffer.append("'");
					buffer.append(":");
					buffer.append("'");
					// 如果是单选或者多选，判断里面有没有checktype和choiceradio属性
					if ($('#' + id).attr('checktype')
							|| $('#' + id).attr('choiceradio')) {
						var div = document.getElementById(id);
						var oLis = div.getElementsByTagName('input');
						for (var p = 0; p < oLis.length; p++) {
							if (oLis[p].checked) {
								value += oLis[p].value + ",";
							}
						}
						if (value != '') {
							value = value.substring(0, value.length - 1);
						}
						buffer.append(value);
					} else if (id != ''
							&& document.getElementById(id).value != ''
							&& document.getElementById(id).value != undefined) {
						value = document.getElementById(id).value;
						buffer.append(value);
					}
					buffer.append("'");
					if (!(i == headRowsLength - 1
							&& j == headTableColumns.length - 1 && ids == tables.length
							- 1)) {
						buffer.append(",");
					}
				}
			}
		}

	}
	buffer.append("}");
	return buffer;
}