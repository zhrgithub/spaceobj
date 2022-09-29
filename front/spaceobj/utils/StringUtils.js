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
export default {
	isBlank,
}
