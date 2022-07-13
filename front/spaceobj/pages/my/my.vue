<template>
	<view class="container">
		<!-- 未登录 -->
		<view class="login-background-style" @click="toLogin" v-if="!loginStatus">
			<view class="photo-image-background-style">
				<image src="/static/my.png" mode=""></image>
			</view>
			<view class="userinfo-background-style">
				<view class="btn-login-background">
					登录
				</view>
			</view>
		</view>
		<!-- 已登录 -->
		<view class="login-background-style" v-if="loginStatus" @click="editUserInfo">
			<view class="photo-image-background-style">
				<image :src="loginPhoto" mode=""></image>
			</view>
			<view class="userinfo-background-style">

				<text class="nick-name-style">{{loginName}}</text>
				<text class="address-background-style">位置：广东深圳</text>
			</view>
		</view>
		<!-- 邀请链接 -->
		<view class="invite-value-background-style">
			<view class="invite-tips-style">
				邀请值：
			</view>
			<view class="invite-numbers-style">
				200
			</view>
			<view class="invite-btn-stye">
				邀请链接
			</view>
		</view>
		<view class="invite-hint-background">
			提示：邀请值可以用来获取用户联系方式，每获取一次用户联系方式，邀请值会减少 1 ，邀请好友注册一次邀请值会增加 1
		</view>
		<!-- 联系客服 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				客服微信
			</view>

			<view class="invite-btn-stye">
				复制
			</view>
		</view>
		<!-- 下载 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				安卓APP
			</view>

			<view class="invite-btn-stye">
				下载
			</view>
		</view>

		<view class="sign-out-btn-style" v-if="loginStatus">
			<button @click="logout">退出登录</button>
		</view>

	</view>
</template>

<script>
	var that;
	export default {
		data() {
			return {
				loginStatus: true,
				loginPhoto: '/static/photo.jpg',
				loginName: 'spaceobj3232323233232'
			}
		},
		created() {
			that = this;
		},
		methods: {
			toLogin() {
				that.loginStatus = true;
				uni.navigateTo({
					url: '/pages/login/login'
				})
			},
			editUserInfo() {
				uni.navigateTo({
					url: '/pages/my/editUser/editUser'
				})
			},
			logout() {
				that.loginStatus = false;
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

	.login-background-style {
		width: 96%;
		margin-left: 2%;
		height: 150rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 20rpx;
		margin-bottom: 20rpx;
		border-radius: 10rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
	}

	.photo-image-background-style {
		width: 20%;
		height: 70%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.photo-image-background-style image {
		width: 100rpx;
		height: 100rpx;
	}

	.userinfo-background-style {
		width: 80%;
		height: 70%;
		display: flex;
		justify-content: left;
		align-items: center;
	}

	.nick-name-style {
		width: 55%;
		display: block;
		justify-content: left;
		align-items: center;
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
	}

	.address-background-style {
		width: 40%;
		margin-left: 5%;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 14px;
	}

	.btn-login-background {
		width: 160rpx;
		margin-left: 5%;
		height: 60rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		color: #fff;
		background-color: #49A8E7;
		border-radius: 10rpx;
	}


	/* 邀请值 */
	.invite-value-background-style {
		width: 96%;
		margin-left: 2%;
		height: 90rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 20rpx;
		margin-bottom: 20rpx;
		border-radius: 10rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
	}

	.invite-tips-style {
		width: 20%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.link-us-style {
		width: 60%;
	}

	.invite-numbers-style {
		width: 40%;
	}

	.invite-btn-stye {
		width: 20%;
		height: 60%;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-left: 10%;
		border-radius: 10rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		font-size: 14px;
		background-color: #49A8E7;
		color: #fff;
	}

	.invite-hint-background {
		width: 90%;
		margin-left: 5%;
		display: flex;
		height: 80rpx;
		font-size: 10px;
		color: #666;
	}

	.sign-out-btn-style {
		width: 100%;
		height: 110rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		position: fixed;
		bottom: 120rpx;
	}

	.sign-out-btn-style button {
		width: 90%;
		background-color: #666;
		color: #fff;
	}
</style>
