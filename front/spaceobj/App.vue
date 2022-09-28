<script>
	import sk from '@/common/StoryKeys.js'
	import api from '@/common/api.js'
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
			// 获取ip属地
			api.get({}, api.ipTerritory).then(res => {
				uni.setStorage({
					key: sk.ipTerritory,
					data: res.replace(/[\r\n]/g, "")
				})
			});
			api.post({
			}, api.getOther).then(res => {
				uni.setStorage({
					key:sk.otherInfo,
					data:res.data
				})
			})
			// 获取设备信息
			const res = uni.getSystemInfoSync();
			uni.setStorage({
				key: sk.deviceModel,
				data: res
			})
		},
		onHide: function() {
			console.log('App Hide')
		},

	}
</script>

<style>
	/*每个页面公共css */

	* {
		font-family: "arial, helvetica, sans-serif", "宋体";
		padding: 0;
		margin: 0;
		outline: none;
		text-decoration: none;
		list-style: none;
	}
</style>
