<template>
	<view class="container">
		<view class="logo-background-style">
			spaceObj
		</view>
		<view v-if="stepStatus==0">
			<!-- 用户的电话 邮件  -->
			<view class="phone-email-background-style">
				<view class="phone-background-style">
					<view class="phone-style">
						<input type="text" maxlength="18" placeholder="请输入手机号/微信">
					</view>
				</view>

				<view class="phone-background-style">
					<view class="phone-style">
						<input type="text" maxlength="11" placeholder="请输入邮箱号">
					</view>
				</view>
			</view>
			<!-- 登录 -->
			<view class="login-background-style">
				<button @click="toLogin">一键登录</button>
			</view>

		</view>
		<view v-if="stepStatus==1">
			<view class="email-verification-style">
				邮箱验证
			</view>
			<view class="email-verification-hint-style">
				请您先用邮箱<text style="font-weight: bold;margin-left: 5rpx;margin-right: 5rpx;">testtesttesttest@163.com</text>
				发邮件，然后点击“我已发送”按钮
			</view>
			<view class="send-email-panel-style">
				<view class="send-content-background">
					<text style="width: 70%;">发送内容：<text style="font-weight: bold;">666</text> </text>
					<button class="copy-button-style" @click="copyContent"> 复制</button>
				</view>
				<view class="send-content-background">
					<text style="width: 70%;">收件箱：<text style="font-weight: bold;">spaceobj@163.com</text></text>
					<button class="copy-button-style" @click="copyEmail"> 复制</button>
				</view>
			</view>

			<!-- 已发送 -->
			<view class="login-background-style">
				<button @click="sendOk">我已发送</button>
			</view>
		</view>



	</view>
</template>

<script>
	import app from '@/App.vue'
	var that;
	export default {
		data() {
			return {
				stepStatus: 0,
			}
		},
		created() {
			that = this;
		},
		methods: {
			toLogin() {
				this.stepStatus = 1;
			},
			sendOk() {
				app.globalData.loginStatus = true;
				uni.showToast({
					title: '登录成功，正在跳转',
					icon: "none"
				})
				uni.switchTab({
					url:'/pages/my/my'
				})
			},
			copyContent(){
				uni.showToast({
					title: '已复制内容',
					icon: "none"
				})
			},
			copyEmail(){
				uni.showToast({
					title: '已复制收件箱',
					icon: "none"
				})
			}

		}
	}
</script>

<style scoped>
	.container {
		width: 100%;
		height: 100%;
		position: absolute;
	}

	.logo-background-style {
		width: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 46px;
		height: 300rpx;
		font-weight: bold;
		color: #49A8E7;
	}

	.phone-email-background-style {
		width: 96%;
		margin-left: 2%;
		height: 250rpx;
		margin-bottom: 30rpx;
	}

	.phone-background-style {
		width: 100%;
		height: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.phone-style {
		width: 70%;
		height: 65%;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 50rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.phone-style input {
		width: 90%;
	}

	.login-background-style {
		width: 96%;
		margin-left: 2%;
		height: 100rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 100rpx;
	}

	.login-background-style button {
		width: 70%;
		height: 90%;
		display: flex;
		justify-content: center;
		align-items: center;
		background-color: #49A8E7;
		color: #fff;
	}

	/* 邮箱验证 */
	.email-verification-style {
		width: 96%;
		margin-left: 2%;
		display: flex;
		justify-content: left;
		align-items: center;
		height: 30rpx;
		font-weight: bold;
	}

	.email-verification-hint-style {
		width: 96%;
		margin-left: 2%;
		font-size: 12px;
		margin-top: 10rpx;
		margin-bottom: 10rpx;
	}

	.send-email-panel-style {
		width: 96%;
		margin-left: 2%;
		height: 200rpx;
	}

	.send-content-background {
		width: 100%;
		height: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.copy-button-style {
		width: 20%;
		margin-left: 10%;
		height: 60rpx;
		font-size: 14px;
		display: flex;
		justify-content: center;
		align-items: center;
		background-color: #49A8E7;
		color: #fff;
	}
</style>
