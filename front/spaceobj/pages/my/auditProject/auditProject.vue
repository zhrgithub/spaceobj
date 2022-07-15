<template>
	<view class="release-background-style">

		<!-- 搜索框 -->
		<view class="search-background-style">
			<view class="search-logo-background-style">
				<view class="image-background-style">
					<image src="/static/searchInput.png" mode=""></image>
				</view>
				<view class="input-background-style">
					<input :value="seachText" type="text" maxlength="15" placeholder="项目编号、发布人昵称、邮箱、联系方式"
						placeholder-style="font-size:14px" confirm-type="search" @confirm="doSearch" @input="inputText">
					<image src="/static/deleteSearch.png" @click="clearInput">
				</view>
			</view>

		</view>
		<view class="top-space-line-style"></view>

		<!-- 发布的列表 -->
		<view class="project-list-style" @click="toAuditProjectDetail">
			<view class="date-status-style">
				<view class="date-style">
					2022-06-18
				</view>
				<view class="status-style">
					待审核
				</view>
				<view class="detail-style">
					查看详情
				</view>
			</view>
			<view class="brief-information-style">
				<text
					style="color: #7CBF80;font-weight: bold;font-size: 15px;">项目描述：</text>查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手大脚看看是科斯康查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手此电脑萨克才能上看大手此电脑萨克才能上看大手
			</view>
		</view>


		<view class="project-list-style" @click="toAuditProjectDetail">
			<view class="date-status-style">
				<view class="date-style">
					2022-06-18
				</view>
				<view class="status-style">
					待审核
				</view>
				<view class="detail-style">
					查看详情
				</view>
			</view>
			<view class="brief-information-style">
				<text
					style="color: #7CBF80;font-weight: bold;font-size: 15px;">项目描述：</text>查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手大脚看看是科斯康查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手此电脑萨克才能上看大手此电脑萨克才能上看大手
			</view>
		</view>




	</view>
</template>

<script>
	export default {
		data() {
			return {
				seachText: ''
			}
		},
		methods: {
			toAuditProjectDetail() {
				uni.navigateTo({
					url: '/pages/my/auditProject/auditProjectDetail/auditProjectDetail'
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

	/* 项目列表 */
	.project-list-style {
		width: 96%;
		margin-left: 2%;
		height: 280rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 10rpx;
		margin-bottom: 30rpx;
	}


	.date-status-style {
		width: 98%;
		margin-left: 1%;
		height: 35%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.brief-information-style {
		width: 96%;
		margin-left: 2%;
		height: 160rpx;
		display: flex;
		justify-content: center;
		align-items: center;

		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 4;
		-webkit-box-orient: vertical;
		font-size: 15px;
	}

	.date-style {
		width: 30%;
		background-color: #49A8E7;
		display: flex;
		align-items: center;
		justify-content: center;
		color: white;
		border-radius: 10rpx;
		height: 60%;
		font-size: 14px;
	}

	.status-style {
		width: 30%;
		background-color: #49A8E7;
		display: flex;
		align-items: center;
		justify-content: center;
		color: white;
		border-radius: 10rpx;
		height: 60%;
		margin-left: 3%;
		margin-right: 3%;
		font-size: 14px;
	}

	.detail-style {
		width: 30%;
		background-color: #EBA54B;
		display: flex;
		align-items: center;
		justify-content: center;
		color: white;
		border-radius: 10rpx;
		height: 60%;
		font-size: 14px;
	}

	.release-background-style {
		width: 100%;
		height: 100%;
		position: absolute;
	}
</style>
