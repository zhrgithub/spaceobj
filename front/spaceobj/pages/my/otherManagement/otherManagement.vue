<template>
	<view class="container">
		<view class="edit-background-style">
			<view class="change-tips">
				客服微信
			</view>
			<view class="change-input-style">
				<input type="text" placeholder="spaceobj">
			</view>
		</view>

		<view class="edit-background-style">
			<view class="change-tips">
				下载链接
			</view>
			<view class="change-input-style">
				<input type="text" placeholder="www.spaceobj.com">
			</view>
		</view>
		<view class="save-btn-style">
			<button>保存</button>
		</view>


	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
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
		width: 90%;
		height: 90%;
		background-color: #49A8E7;
		color: #fff;
	}
</style>
