<template>
	<view class="release-background-style">
		<view v-if="list.length==0" class="content-null-style">
			<view class="image-title-background-style">
				<view class="not-release-image-style-background">
					<image src="/static/notAnything.png" mode=""></image>
				</view>
				<view class="title-context">
					未找到助力信息~
				</view>
			</view>
		</view>



		<!-- 发布的列表 -->
		<view class="project-list-style" v-for="(item,idx) in list" :key="idx" @click="toProjecDetail(item)">
			<view class="date-status-style">
				<view class="date-style">
					{{timeStampTurnTime(item.createTime)}}
				</view>
				<view class="status-pass-style" v-if="item.hpStatus==0">
					还差{{10-item.hpNumber}}人
				</view>
				<view class="status-style" v-if="item.hpStatus==1">
					助力成功
				</view>
				<view class="status-refuse-style" v-if="item.hpStatus==2">
					已删除
				</view>
				<view class="status-refuse-style" v-if="item.hpStatus==3">
					甲方已成交
				</view>
				<view class="status-style">
					查看详情
				</view>
			</view>
			<view class="brief-information-style">
				<text style="color: #7CBF80;font-weight: bold;font-size: 15px;">项目描述：</text>{{item.pcontent}}
			</view>
			<view class="slider-style">
				<slider :value="item.hpNumber" activeColor="green" backgroundColor="darkgray" block-color="#49A8E7"
					block-size="10" show-value min="0" max="10" disabled="true" />
			</view>
		</view>

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
				list: [],
				projecObj: null,
				currentPage: 1,
				pageSize: 10,
			}
		},
		// 触底加载更多
		onReachBottom() {
			that.loadList();
		},
		// 下拉刷新
		onPullDownRefresh() {
			that.currentPage = 1;
			that.list = [];
			that.loadList();

			uni.stopPullDownRefresh();

		},
		created() {
			that = this;
		},
		onLoad() {
			uni.showLoading({
				title: '加载中...',
			})
			that.list = [];
			that.currentPage = 1;
			that.pageSize = 10;
			that.loadList();
		},
		onShow() {
			var userInfo = uni.getStorageSync(sk.userInfo);
			that.userInfo = userInfo;
			
			
		},
		methods: {
			timeStampTurnTime(str) {
				var date = new Date(str); // 参数需要毫秒数，所以这里将秒数乘于 1000
				var Y = date.getFullYear() + '-';
				var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
				var D = date.getDate() + ' ';
				return Y + M + D;
			},
			loadList() {
				api.post({
					currentPage: that.currentPage,
					pageSize: that.pageSize
				}, api.projectHelpList).then(res => {
					if (res.code == 200) {
						if (res.data.length > 0) {
							that.list = that.list.concat(res.data);
							that.currentPage++;
						}
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
					uni.hideLoading();
				});
			},
			submit() {
				this.$refs.popup.close();
				uni.showToast({
					title: "提交成功",
					icon: 'none',
					duration: 2000
				})
			},
			sliderChange(e) {
			},
			toProjecDetail(e) {
				uni.navigateTo({
					url: '/pages/help/helpProjectDetail/helpProjectDetail?obj=' + encodeURIComponent(JSON.stringify(e))
				})
			},
		}
	}
</script>

<style scoped>
	.project-list-style {
		width: 96%;
		margin-left: 2%;
		height: 285rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 10rpx;
		margin-top: 30rpx;
	}

	.date-status-style {
		width: 98%;
		margin-left: 1%;
		height: 30%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.brief-information-style {
		width: 96%;
		margin-left: 2%;
		height: 45%;
		display: flex;
		justify-content: center;
		align-items: center;

		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 3;
		-webkit-box-orient: vertical;
		font-size: 15px;
	}

	.slider-style {
		width: 30%;
		width: 100%;
	}

	.date-style {
		width: 30%;
		background-color: #49A8E7;
		display: flex;
		align-items: center;
		justify-content: center;
		color: white;
		border-radius: 10rpx;
		height: 70%;
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
		height: 70%;
		margin-left: 3%;
		margin-right: 3%;
		font-size: 14px;
	}

	.status-pass-style {
		width: 30%;
		background-color: #7CBF80;
		display: flex;
		align-items: center;
		justify-content: center;
		color: white;
		border-radius: 10rpx;
		height: 70%;
		margin-left: 3%;
		margin-right: 3%;
		font-size: 14px;
	}

	.status-refuse-style {
		width: 30%;
		background-color: #E26C65;
		display: flex;
		align-items: center;
		justify-content: center;
		color: white;
		border-radius: 10rpx;
		height: 70%;
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
		height: 70%;
		font-size: 14px;
	}

	.button-style {
		width: 90%;
		margin-left: 5%;
		height: 180rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.button-style button {
		width: 40%;
		background-color: #49A8E7;
		color: white;
	}

	.button-space {
		width: 10%;
	}

	.description-style {
		width: 90%;
		margin-left: 5%;
		display: flex;
		align-items: center;
		margin-top: 30rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 10px;
	}

	.description-style textarea {
		width: 96%;
		margin-top: 20rpx;
		margin-left: 2%;
	}

	.doller-num-style {
		width: 90%;
		height: 80rpx;
		margin-left: 5%;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: darkgray 0px 0px 2px 0px;
		margin-top: 30rpx;
		border-radius: 10px;
	}

	.doller-num-style input {
		width: 90%;
	}

	.description-doller-style {
		width: 100%;
		height: 600rpx;
	}

	.release-background-style {
		width: 100%;
		height: 100%;
		position: absolute;
	}

	.content-null-style {
		width: 100%;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.image-title-background-style {
		width: 50%;
		height: 500rpx;
	}

	.title-context {
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 14px;
		color: lightgrey;
	}

	.not-release-image-style-background {
		width: 100%;
		height: 70%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.not-release-image-style-background image {
		width: 250rpx;
		height: 250rpx;
	}

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
</style>
