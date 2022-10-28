<template>
	<view class="project-detail-background-style">


		<view class="base-info-panel-style">
			<view class="project-num-status-style">
				<view class="project-numer-style">
					项目编号：{{projectObj.pid}}
				</view>
				<view class="project-numer-style">
					用户：{{projectObj.nickname}}
				</view>
			</view>

			<view class="project-num-status-style">
				<view class="project-numer-style">
					预算：{{projectObj.price}}元
				</view>
				<view class="project-numer-style">
					浏览：{{projectObj.pageViews}}次
				</view>
			</view>

			<view class="project-num-status-style">
				<view class="ip-address-style">
					IP属地：{{projectObj.ipAddress}}
				</view>
			</view>
		</view>

		<view class="description-requirement-style">
			<view class="description-content-style">
				<text style="font-weight: bold;font-size: 14px;color: #7CBF80;">项目描述：</text>{{projectObj.content}}
			</view>

		</view>
		<view class="btn-background-style">
			<button @click="getUserInfo">立即联系</button>

		</view>

		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog :type="msgType" :cancelText="shareCancelText" :confirmText="shareConfirmText"
				:title="shareTitle" :content="shareContent" @confirm="dialogConfirm" @close="dialogClose">
			</uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	let that;
	import sk from '@/common/StoryKeys.js'
	import api from '@/common/api.js'
	import su from '@/utils/StringUtils.js'
	export default {
		data() {
			return {
				projectObj: "",
				
				projectHelp: "",
				// 自定义弹出框居中显示
				type: 'center',
				msgType: 'success',
				shareContent: '',
				shareTitle: '',
				shareCancelText: '',
				shareConfirmText: ''

			}
		},
		created() {
			that = this;
		},
		onLoad(e) {
			var obj = JSON.parse(e.obj);
			console.log(obj)
			that.projectObj = obj;
		},
		onShareAppMessage(res) {
			if (res.from === 'button') { // 来自页面内分享按钮
				console.log(res.target)
			}
			console.log("分享前的内容",that.projectHelp);
			return {
				title: '欢迎体验spaceObj，项目外包入口',
				path: 'pages/index/index?projectHelpShare=' + JSON.stringify(that.projectHelp)
			}
		},
		methods: {
			dialogClose() {
				console.log('点击关闭')
			},
			dialogConfirm() {
				console.log('点击确认')
				// this.$refs.message.open();
			},
			dialogToggle(type) {
				this.msgType = type
				this.$refs.alertDialog.open()
			},
			getAuditStatus(e) {
				if (e == 0) {
					return "待审核";
				}
				if (e == 1) {
					return "审核通过";
				}
				if (e == 2) {
					return "审核不通过";
				}
				if (e == 3) {
					return "已删除";
				}
				if (e == 4) {
					return "已成交";
				}
			},
			copyPhoneNumber(phoneNumber) {
				uni.showModal({
					title: "联系方式",
					content: phoneNumber,
					cancelText: "取消",
					confirmText: "复制",
					success(e) {
						if (e.confirm) {
							uni.setClipboardData({
								data: phoneNumber,
								success() {
									uni.showToast({
										title: '复制成功',
										icon: 'none'
									})
								},
								fail() {
									uni.showToast({
										title: '复制失败',
										icon: 'none'
									})
								}
							})
						}
					}
				})
			},
			getUserInfo() {
				uni.showLoading({
					title: '加载中...'
				})
				api.post({
					uuid: that.projectObj.uuid,
				}, api.getPhoneNumberByProjectId).then(res => {
					uni.hideLoading();
					console.log(res)
					if (res.code == 200) {
						that.copyPhoneNumber(res.data);
					}
					if (res.code == 202) {
						// 提前生成助力链接
						that.getShareProjectHelp();

						// 初始化自定义弹窗内容
						that.shareTitle = '温馨提示';
						that.shareContent = res.msg;
						that.shareCancelText = '放弃';
						that.shareConfirmText = '立即分享';
						that.dialogToggle('info');
						return;
					}
				});
			},
			getShareProjectHelp() {
				uni.showLoading({
					title: "加载中"
				})
				api.post({
					pUUID: that.projectObj.uuid,
				}, api.createProjectHelpLink).then(res => {
					uni.hideLoading();
					console.log(res)
					if (res.code == 200) {
						that.projectHelp = res.data;
					}
				});
			},
		}
	}
</script>

<style scoped>
	.main-content-right {
		width: 100%;
		height: 200rpx;
		display: none;
	}

	.project-detail-background-style {
		width: 100%;
		height: 100%;
		position: absolute;
	}

	.base-info-panel-style {
		width: 96%;
		margin-left: 2%;
		height: 300rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		margin-top: 50rpx;
		border-radius: 10px;
	}

	.project-num-status-style {
		width: 90%;
		margin-left: 5%;
		height: 95rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 14px;
	}

	.project-numer-style {
		width: 50%;
		text-align: left;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.ip-address-style {
		width: 100%;
		text-align: left;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}


	.description-requirement-style {
		width: 96%;
		margin-left: 2%;
		margin-top: 50rpx;
		border-radius: 10rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.description-content-style {
		width: 90%;
		margin-top: 30rpx;
		margin-bottom: 30rpx;
		font-size: 15px;
	}

	.btn-background-style {
		width: 96%;
		height: 280rpx;
		margin-left: 2%;
		margin-top: 80rpx;
		margin-bottom: 500rpx;
	}

	.btn-background-style button {
		width: 100%;
		background-color: #49A8E7;
		color: white;
		height: 100rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>
