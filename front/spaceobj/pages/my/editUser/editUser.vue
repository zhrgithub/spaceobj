<template>
	<view class="container">
		<view class="login-background-style" @click="editPhoto">
			<view class="userinfo-background-style">
				修改头像
			</view>
			<view class="photo-image-background-style">
				<image :src="loginPhoto" mode=""></image>
			</view>
		</view>

		<view class="edit-background-style">
			<view class="change-tips">
				昵称
			</view>
			<view class="change-input-style">
				<input type="text" placeholder="哈哈哈">
			</view>
		</view>

		<view class="edit-background-style">
			<view class="change-tips">
				手机号
			</view>
			<view class="change-input-style">
				<input type="text" placeholder="哈哈哈">
			</view>
		</view>

		<view class="edit-background-style">
			<view class="change-tips">
				邮箱
			</view>
			<view class="change-input-style">
				<input type="text" placeholder="哈哈哈">
			</view>
		</view>
		<view class="edit-background-style">
			<view class="change-tips">
				实名状态
			</view>
			<view class="change-input-style">
				未实名
			</view>
		</view>
		<view class="edit-background-style">
			<view class="change-tips">
				位置
			</view>
			<view class="change-input-style">
				广东深圳
			</view>
		</view>



		<view class="save-btn-style">
			<button @click="logout">退出登录</button> <button>保存</button>
		</view>

		<uni-popup ref="popup" background-color="#fff">
			<view class="need-description-budget-style">
				选择头像
			</view>
			<view class="scroll-item-line-two-style"></view>
			<view class="description-doller-style">
				<image src="/static/photo.jpg" mode="" v-for="(item,idx) in selectPhotoList" :key="idx"></image>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	var that;
	import sk from '@/common/StoryKeys.js'
	export default {
		data() {
			return {
				loginPhoto: '/static/photo.jpg',
				selectPhotoList: [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
			}
		},
		created() {
			that = this;
		},
		methods: {
			editPhoto() {
				this.$refs.popup.open('bottom');
			},
			logout() {
				// 移除缓存中的key
				for (var i = 0; i < sk.clearKey.length; i++) {
					uni.removeStorage({
						key: sk.clearKey[i]
					})
				}
				uni.switchTab({
					url: '/pages/my/my'
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

	.edit-background-style {
		width: 96%;
		margin-left: 2%;
		height: 110rpx;
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
		width: 70%;
		height: 70%;
		display: flex;
		justify-content: left;
		align-items: center;
	}

	.change-tips {
		width: 20%;
		height: 70%;
		display: flex;
		justify-content: left;
		align-items: center;
	}

	.change-input-style {
		width: 70%;
		height: 70%;
		display: flex;
		justify-content: left;
		align-items: center;
		margin-left: 2%;
	}

	.change-input-style input {
		width: 100%;
	}

	.save-btn-style {
		width: 96%;
		margin-left: 2%;
		height: 100rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		position: fixed;
		bottom: 20rpx;
	}

	.save-btn-style button {
		width: 30%;
		height: 90%;
		background-color: #49A8E7;
		color: #fff;
	}

	/* 修改头像 */
	.need-description-budget-style {
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 80rpx;
	}

	.scroll-item-line-two-style {
		width: 100%;
		border-top: 1rpx solid #e1e1e1;
		content: '';
	}

	.description-doller-style {
		width: 98%;
		margin-left: 1%;
		height: 700rpx;
	}

	.description-doller-style image {
		width: 70rpx;
		height: 70rpx;
		margin: 14rpx 15rpx;
		border: solid #fff;
	}
</style>
