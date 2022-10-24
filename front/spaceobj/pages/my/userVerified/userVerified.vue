<template>
	<view class="container">
		<view class="base-infos">
			<view class="base-infos-style">
				<input placeholder="请输入您的真实姓名(必填)" :value="username" maxlength="15"></input>
			</view>
			<view class="base-infos-style-two">
				<input placeholder="请输入您的身份证号码(必填)" :value="idCardNum" maxlength="18" type="number"></input>
			</view>
		</view>

		<view class="id-card-style">
			<view class="image-background-one" v-if="idCardPic==''">
				<image src="/static/camera.png"></image>
				<view class="image-title">本人手举身份证正面(必填)</view>
			</view>
			<block class="image-background-two" v-if="idCardPic!=''">
				<image :src="idCardPic" style="width:100%;height:100%;margin-left:0%;border-radius: 20rpx;" />
			</block>
		</view>

		<view class="button-background" v-if="realNameStatus==0||realNameStatus==3">
			<button>提交</button>
		</view>
	</view>
</template>

<script>
	var that;
	import app from '@/App.vue'
	import sk from '@/common/StoryKeys.js'
	import strigUtils from '@/utils/StringUtils.js'
	export default {
		data() {
			return {
				realNameStatus: 3,
				idCardPic: '',
				username: '',
				idCardNum: '',

			}
		},
		created() {
			that = this;
		},
		onShow() { 
			this.timer = setTimeout(() => {
				// 第一步：加载用户基本信息
				that.loginStatus = uni.getStorageSync(sk.loginStatus);
				var userInfo = uni.getStorageSync(sk.userInfo);
				console.log(userInfo);
				if (userInfo != '') {
					that.realNameStatus = userInfo.realNameStatus;
					that.idCardPic = userInfo.idCardPic;
					that.username = userInfo.username;
					that.idCardNum = userInfo.idCardNum;
					if (that.realNameStatus == 3) {
						uni.showModal({
							showCancel: false,
							title: "审核结果",
							content:userInfo.auditMsg
						})
					}
				}
			})
		},
		methods: {

		}
	}
</script>

<style scoped>
	.container {
		width: 100%;
		height: 100%;
		position: absolute;
	}

	.base-infos {
		width: 95%;
		margin-left: 2.5%;
		height: 250rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 20rpx;
		margin-top: 25rpx;
	}

	.base-infos-style {
		width: 95%;
		margin-left: 2.5%;
		margin-top: 5rpx;
		display: flex;
	}

	.base-infos-style input {
		margin-top: 30rpx;
		box-shadow: 0rpx 0rpx 1rpx 1rpx #ebebeb;
		border-radius: 10rpx;
		height: 80rpx;
		font-size: 16px;
		width: 100%;
		justify-content: center;
		text-align: center;
	}


	.base-infos-style-two {
		width: 95%;
		margin-left: 2.5%;
		margin-top: 5rpx;
		display: flex;
	}

	.base-infos-style-two input {
		margin-top: 30rpx;
		box-shadow: 0rpx 0rpx 1rpx 1rpx #ebebeb;
		border-radius: 10rpx;
		width: 100%;
		height: 80rpx;
		font-size: 16px;
		justify-content: center;
		text-align: center;
	}

	.id-card-style {
		width: 95%;
		margin-left: 2.5%;
		margin-top: 50rpx;
		height: 400rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 20rpx;
	}

	.image-background-one {
		width: 100%;
		height: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.image-background-one image {
		width: 50rpx;
		height: 50rpx;
		margin-right: 10rpx;
	}

	.image-background-two {
		width: 100%;
		height: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.image-background-two image {
		width: 50rpx;
		height: 50rpx;
	}

	.button-background {
		width: 90%;
		margin-left: 5%;
		text-align: center;
		position: absolute;
		margin-top: 100rpx;
		bottom: 30rpx;
	}

	.button-background button {
		background-color: #49A8E7;
		color: white;
		border-radius: 20rpx;
		width: 100%;
	}

	.realnameSatatus-style {
		width: 50%;
		height: 100rpx;
		background-color: #34495E;
		color: white;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 20rpx;
	}
</style>
