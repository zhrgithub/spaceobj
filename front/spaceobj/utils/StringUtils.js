/**
 * 校验字符串非空
 * @param {Object} str
 */
function isBlank(str) {
	if (str == null || str == "" || str.length == 0) {
		return true;
	} else {
		return false;
	}
}


function isUndefined(obj) {
	if (obj == 'undefined' || obj == undefined) {
		return true;
	} else {
		return false;
	}
}

function timeStampTurnTime(str) {
	var date = new Date(str); // 参数需要毫秒数，所以这里将秒数乘于 1000
	var Y = date.getFullYear() + '-';
	var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
	var D = date.getDate() + ' ';
	return Y + M + D;
}
export default {
	isBlank,
	timeStampTurnTime,
	isUndefined
	
}
