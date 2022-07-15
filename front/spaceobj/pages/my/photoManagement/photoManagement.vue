<template>
	<view class="container">
		<view class="photo-image-btn-background-style">
			<view class="photo-image-background-style">
				<image src="/static/photo.jpg" mode=""></image>
			</view>
			<view class="btn-background-style">
				<button @click="editPhoto">编辑</button>
			</view>
		</view>


		<view class="click-release-style" @click="addPhoto">添加</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {
			addPhoto() {
				uni.showModal({
					title:'新增头像',
					editable:true,
					confirmColor:'#000',
					success(e) {
						console.log(e.content)
						if(e.confirm){
							uni.showToast({
								icon:"none",
								title:'新增成功'
							})
						}
					}
				})
			},
			editPhoto(){
				uni.showModal({
					title:'编辑头像',
					cancelText:'删除头像',
					confirmText:'保存修改',
					confirmColor:'#000',
					editable:true,
					success(e) {
						console.log(e.content)
						if(e.confirm){
							uni.showToast({
								icon:"none",
								title:'保存成功'
							})
						}
						if(e.cancel){
							uni.showToast({
								icon:"none",
								title:'删除成功'
							})
						}
					}
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

	.photo-image-btn-background-style {
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

	.photo-image-background-style {
		width: 70%;
		height: 100%;
		display: flex;
		justify-content: left;
		align-items: center;
	}

	.photo-image-background-style image {
		width: 70rpx;
		height: 70rpx;
	}

	.btn-background-style {
		width: 25%;
		height: 100%;
		justify-content: center;
		display: flex;
		align-items: center;
	}

	.btn-background-style button {
		width: 80%;
		height: 70%;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 14px;
		background-color: #49A8E7;
		color: #fff;
	}

	.click-release-style {
		width: 100rpx;
		height: 100rpx;
		position: fixed;
		z-index: 2;
		bottom: 120px;
		right: 40px;
		background-color: #49A8E7;
		color: white;
		font-size: 15px;
		border-radius: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: #49A8E7 1px 0px 5px 1px;
	}
</style>
