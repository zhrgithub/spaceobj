<template>
	<view class="container">
		
		<!-- 搜索框 -->
		<view class="search-background-style">
			<view class="search-logo-background-style">
				<view class="image-background-style">
					<image src="/static/searchInput.png" mode=""></image>
				</view>
				<view class="input-background-style">
					<input :value="seachText" type="text" maxlength="15" placeholder="用户ID、昵称、邮箱、名称"
						placeholder-style="font-size:14px" confirm-type="search" @confirm="doSearch" @input="inputText">
					<image src="/static/deleteSearch.png" @click="clearInput">
				</view>
			</view>
		</view>
		<view class="top-space-line-style"></view>
		
		
		<view class="photo-image-btn-background-style">
			<view class="photo-image-background-style">
				<image src="/static/photo.jpg" mode=""></image>
				<view class="base-info-panel-style">
					<view class="phone-background-style">
						状态：正常
					</view>
					<view class="phone-background-style">
						用户ID：342323232
					</view>
				</view>
			</view>
			<view class="btn-background-style">
				<button @click="editUser">编辑</button>
			</view>
		</view>



		<uni-popup ref="popup" background-color="#fff">
			<view class="need-description-budget-style">
				基本信息
			</view>
			<view class="scroll-item-line-two-style"></view>
			<view class="description-doller-style">
				<view class="edit-background-style">
					<view class="change-tips">
						用户名
					</view>
					<view class="change-input-style">
						<input type="text" placeholder="张三">
					</view>
				</view>
				<view class="edit-background-style">
					<view class="change-tips">
						昵称
					</view>
					<view class="change-input-style">
						<input type="text" placeholder="张三">
					</view>
				</view>
				<view class="edit-background-style">
					<view class="change-tips">
						实名状态
					</view>
					<view class="change-input-style">
						<input type="text" placeholder="未实名" disabled="true">
					</view>
				</view>
				<view class="edit-background-style">
					<view class="change-tips">
						联系方式
					</view>
					<view class="change-input-style">
						<input type="text" placeholder="110">
					</view>
				</view>
				<view class="edit-background-style">
					<view class="change-tips">
						邮箱
					</view>
					<view class="change-input-style">
						<input type="text" placeholder="email_demo@163.com">
					</view>
				</view>
				<view class="edit-background-style">
					<view class="change-tips">
						邀请值
					</view>
					<view class="change-input-style">
						<input type="text" placeholder="100">
					</view>
				</view>

				<view class="edit-background-style">
					<view class="change-tips">
						ip属地
					</view>
					<view class="change-input-style">
						<input type="text" placeholder="广东深圳" disabled="true">
					</view>
				</view>
				<view class="edit-background-style">
					<view class="change-tips">
						用户状态
					</view>
					<view class="change-input-style">
						<input type="text" placeholder="正常" disabled="true">
					</view>
				</view>

				<view class="button-background" v-if="auditStatus==0||auditStatus==3">
					<button>冻结</button>
					<button @click="unApprove">保存</button>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				auditStatus: 3,
				imageUrl: '',
				name: '',
				idCard: '',
				phone: '',
				seachText:''
			}
		},
		methods: {
			editUser() {
				this.$refs.popup.open('bottom')
			},
			unApprove() {
				this.$refs.popup.close()
				uni.showModal({
					editable: true,
					title: '未通过原因',
					confirmColor: '#000',
					success(e) {
						if (e.confirm) {
							uni.showToast({
								icon: "none",
								title: '提交成功'
							})
						}
					}
				})
			},
			doSearch(e) {
				console.log(e.detail.value)
			},
			inputText(e) {
				this.seachText = e.detail.value
			},
			clearInput() {
				this.seachText = "";
			},
		}
	}
</script>

<style scoped>
	.container {
		width: 100%;
		height: 100%;
		position: absolute;
	}
	
	/* 搜索框 */
	.search-background-style {
		width: 100%;
		height: 130rpx;
		position: absolute;
		z-index: 9;
		display: flex;
		justify-content: center;
		align-items: center;
	
	}
	
	.search-logo-background-style {
		width: 90%;
		height: 60%;
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 10px;
		background-color: #e6e6e6;
	}
	
	.image-background-style {
		width: 10%;
		height: 30rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.image-background-style image {
		width: 40rpx;
		height: 40rpx;
	}
	
	.input-background-style {
		width: 85%;
		height: 80%;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.input-background-style input {
		width: 90%;
		height: 100%;
	}
	
	.input-background-style image {
		width: 40rpx;
		height: 40rpx;
	}
	
	
	.top-space-line-style {
		height: 130rpx;
		width: 100%;
	}
	
	
	

	.photo-image-btn-background-style {
		width: 96%;
		margin-left: 2%;
		height: 120rpx;
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
		height: 80%;
		display: flex;
		justify-content: left;
		align-items: center;
	}

	.photo-image-background-style image {
		width: 70rpx;
		height: 70rpx;
	}

	.base-info-panel-style {
		width: 80%;
		margin-left: 20rpx;
		height: 100%;
	}

	.phone-background-style {
		width: 100%;
		display: block;
		justify-content: left;
		align-items: center;
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
	}

	.btn-background-style {
		width: 25%;
		height: 80%;
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

	/* 弹出框 */

	.need-description-budget-style {
		width: 100%;
		font-weight: bold;
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
		width: 100%;
		height: 900rpx;
	}

	/* 表单信息 */
	.base-infos {
		width: 95%;
		margin-left: 2.5%;
		height: 250rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 20rpx;
		margin-top: 25rpx;
	}

	.button-background {
		width: 90%;
		margin-left: 5%;
		text-align: center;
		position: absolute;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.button-background button {
		background-color: #49A8E7;
		color: white;
		border-radius: 20rpx;
		width: 30%;
	}

	.edit-background-style {
		width: 96%;
		margin-left: 2%;
		height: 80rpx;
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
</style>
