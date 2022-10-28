<template>
	<view class="space-obj-background-style">
		<image src="/static/index.png" mode=""></image>
	</view>
</template>

<script>
	var that = this;
	import sk from "@/common/StoryKeys.js"
	import api from "@/common/api.js"
	import su from "@/utils/StringUtils.js"
	export default {
		data() {
			return {
				bgImage: "/static/index.png",
			}
		},
		onLoad(e) {
			// console.log(uni.getStorageSync(sk.inviteUserId))
			if (!su.isBlank(e)) {
				// 设置邀请人id
				if (!su.isUndefined(e.inviteUserId)) {
					uni.setStorage({
						key: sk.inviteUserId,
						data: e.inviteUserId
					})
				}
				// 设置项目助力projectHelpShare
				if (!su.isUndefined(e.projectHelpShare)) {
					var projectHelpShare = JSON.parse(e.projectHelpShare);
					uni.setStorage({
						key: sk.projectHelpShare,
						data: projectHelpShare
					})
					// 如果已经登录，那么直接用当前登录用户给他助力
					that.doUpdateProjectHelp();
				}
			}
		},
		onShow() {
			uni.showLoading({
				title: "加载中..."
			})
			api.post({}, api.getOther).then(res => {
				var data = res.data;
				uni.setStorage({
					key: sk.otherInfo,
					data: data
				})
				if (data.online == 1) {
					uni.switchTab({
						url: '/pages/project/project'
					})
				}
				uni.hideLoading();
			})
		},
		created() {
			that = this;

			const res = uni.getSystemInfoSync();
			uni.setStorage({
				key: sk.deviceModel,
				data: res
			})
			console.log("设备信息:", res) // 设备信息
			if (res.model != "PC") {

			}
		},
		methods: {
			doUpdateProjectHelp() {
				var projectHelpShare = uni.getStorageSync(sk.projectHelpShare);
				if (!su.isUndefined(projectHelpShare)&&!su.isBlank(projectHelpShare)) {
					api.post({
						hpId: projectHelpShare.hpId,
					}, api.updateProjectHelpNumber).then(res => {
						if (res.code == 200) {
							uni.removeStorage({
								key: sk.projectHelpShare
							})
						}
						uni.showToast({
							title: res.msg,
							icon: "none"
						})
					});
				}
			}
		}
	}
</script>

<style scoped>
	.space-obj-background-style {
		width: 100%;
		height: 100%;
		position: fixed;
	}

	.space-obj-background-style image {
		width: 100%;
		height: 100%;
	}
</style>
