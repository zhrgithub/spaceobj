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
				<image :src="photoUrl" mode=""></image>
			</view>
			<view class="userinfo-background-style">

				<text class="nick-name-style">{{username}}</text>
				<text class="address-background-style">IP属地：{{ipTerritory}}</text>
			</view>
		</view>

		<!-- 实名认证 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				实名认证：未实名
			</view>

			<view class="invite-btn-stye" @click="userVerified">
				去认证
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

			<view class="invite-btn-stye" @click="copyWeChat">
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

		<!-- 日志管理 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				日志管理
			</view>

			<view class="invite-btn-stye" @click="logManagement">
				查看日志
			</view>
		</view>

		<!-- 用户管理 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				用户管理
			</view>

			<view class="invite-btn-stye" @click="userManagement">
				查看用户
			</view>
		</view>

		<!-- 实名管理 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				实名管理
			</view>

			<view class="invite-btn-stye" @click="userVerifiedMangeMent">
				审核实名
			</view>
		</view>

		<!-- 项目管理 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				项目管理
			</view>

			<view class="invite-btn-stye" @click="auditProject">
				审核项目
			</view>
		</view>

		<!-- 头像管理 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				头像管理
			</view>

			<view class="invite-btn-stye" @click="photoManagement">
				管理头像
			</view>
		</view>

		<!-- 广告投放 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				广告投放
			</view>

			<view class="invite-btn-stye" @click="advertiseManagement">
				广告管理
			</view>
		</view>

		<!-- 其它管理 -->
		<view class="invite-value-background-style">
			<view class="link-us-style">
				其它管理
			</view>

			<view class="invite-btn-stye" @click="otherManagement">
				管理
			</view>
		</view>
		<view class="space-line-style">

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
				loginStatus: true,
				photoUrl: '/static/photo.png',
				username: '昵称未设置',
				ipTerritory: '广东 深圳'
			}
		},
		created() {
			that = this;
		},
		onShow() {
			this.timer = setTimeout(() => {
				that.loginStatus = uni.getStorageSync(sk.loginStatus);
				var userInfo = uni.getStorageSync(sk.userInfo);
				console.log(userInfo)
				that.photoUrl = strigUtils.isBlank(userInfo.photoUrl) ? that.photoUrl : userInfo.photoUrl;
				that.username = strigUtils.isBlank(userInfo.username) ? that.username : userInfo.username;
				that.ipTerritory = userInfo.ipTerritory;
			}, 200)
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
			auditProject() {
				uni.navigateTo({
					url: '/pages/my/auditProject/auditProject'
				})
			},
			logManagement() {
				uni.navigateTo({
					url: '/pages/my/logManagement/logManagement'
				})
			},
			userManagement() {
				uni.navigateTo({
					url: '/pages/my/userManageMent/userManageMent'
				})
			},
			userVerified() {
				uni.navigateTo({
					url: '/pages/my/userVerified/userVerified'
				})
			},
			photoManagement() {
				uni.navigateTo({
					url: '/pages/my/photoManagement/photoManagement'
				})
			},
			advertiseManagement() {
				uni.navigateTo({
					url: '/pages/my/advertiseManagement/advertiseManagement'
				})
			},
			userVerifiedMangeMent() {
				uni.navigateTo({
					url: '/pages/my/userVerifiedMangeMent/userVerifiedMangeMent'
				})
			},
			otherManagement() {
				uni.navigateTo({
					url: '/pages/my/otherManagement/otherManagement'
				})
			},
			copyWeChat() {
				uni.setClipboardData({
					data: 'hello',
					success: function() {
						console.log('success');
					}
				});
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

	.space-line-style {
		width: 100%;
		height: 200rpx;
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
		width: 30%;
		display: block;
		font-size: 14px;
		justify-content: left;
		align-items: center;
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
	}

	.address-background-style {
		width: 55%;
		font-size: 14px;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
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
</style>
