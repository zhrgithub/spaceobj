// 环境的切换
let pubUrl = "http://localhost:8083/"
if (process.env.NODE_ENV === 'development') {
	// 开发环境
	pubUrl = "http://localhost:8083/"
} else if (process.env.NODE_ENV === 'debug') {
	pubUrl = "http://localhost:8083/" // 调试环境
} else if (process.env.NODE_ENV === 'production') {
	pubUrl = "http://localhost:8083/" // 生产环境
}
// 允许当前axios携带cookie
const http = (options) => {
	return new Promise((resolve, reject) => {
		uni.request({
			//官方文档说是默认携带cookie（h5环境），实际体验并非如此
			// withCredentials: true,
			url: pubUrl + options.url,
			method: options.method || 'get',
			data: options.data || {},
			header: options.header || {
				'content-type': 'application/x-www-form-urlencoded',
				// 只在非h5的小程序添加下面的  cookie
				//#ifdef !H5 
				cookie: uni.getStorageSync("satoken")
				// #endif
			},
			//请求成功的回调，可以在方法做返回状态码的判断。也可以直接返回resolve  ==》success: resolve，fail：reject
			success: (res) => {
				resolve(res)
			},
			//请求失败的回调，可以在方法。反馈用户、提示用户，也可以直接返回reject  ==》success: resolve，fail：reject
			fail: (err) => {
				reject(err)
			},
		})
	})
}
export default http
