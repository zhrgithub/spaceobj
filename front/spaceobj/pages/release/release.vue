<template>
	<view class="release-background-style">
		<view v-if="list.length==0" class="content-null-style">
			<view class="image-title-background-style">
				<view class="not-release-image-style-background">
					<image src="/static/notAnything.png" mode=""></image>
				</view>
				<view class="title-context">
					下拉刷新项目信息~
				</view>
			</view>
		</view>

		<view class="click-release-style" @click="releaseProject('bottom')">发需求</view>

		<uni-popup ref="popup" background-color="#fff">
			<view class="need-description-budget-style">
				项目描述和预算
			</view>
			<view class="scroll-item-line-two-style"></view>
			<view class="description-doller-style">
				<view class="doller-num-style">
					<input placeholder="请输入预算(单位:元)" type="number" maxlength="15" :value="price" @input="setPrice">
				</view>
				<view class="description-style">
					<textarea maxlength="1000" cols="30" rows="2" placeholder="请输入您的需求信息" :value="content"
						@input="setContent"></textarea>
				</view>
				<view class="button-style">
					<button @click="cancelSubmit">取消</button>
					<view class="button-space"></view>
					<button @click="submit">确认发布</button>
				</view>
			</view>
		</uni-popup>

		<!-- 发布的列表 -->
		<view class="project-list-style" @click="toProjecDetail(item)" v-for="(item,idx) in list" :key="idx"
			v-if="item.status!=3">
			<view class="date-status-style">
				<view class="date-style">
					{{timeStampTurnTime(item.createTime)}}
				</view>
				<view class="status-style" v-if="item.status==0">
					审核中
				</view>
				<view class="status-pass-style" v-if="item.status==1">
					发布成功
				</view>
				<view class="status-refuse-style" v-if="item.status==2">
					发布失败
				</view>
				<view class="status-cancel-style" v-if="item.status==4">
					已成交
				</view>
				<view class="status-style">
					查看详情
				</view>
			</view>
			<view class="brief-information-style">
				<text style="color: #7CBF80;font-weight: bold;font-size: 15px;">项目描述：</text>{{item.content}}
			</view>
		</view>


		<view class="space-line-style">

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
				type: 'center',
				seachText: '',
				list: [],
				projecObj: null,
				currentPage: 1,
				pageSize: 10,
				price: "",
				content: " ",
				userInfo: "",
				// 根据屏幕高度显示输入框
				screenHeight: "0px",
			}
		},
		created() {
			that = this;
		},
		onLoad() {
			uni.showLoading({
				title: '加载中...',
			})

			// 获取设备信息
			var deviceModel = uni.getStorageSync(sk.deviceModel);
			that.screenHeight = deviceModel.screenHeight;
			console.log(that.screenHeight)

			that.list = [];
			that.currentPage = 1;
			that.pageSize = 10;
			that.loadList();
		},
		onShow() {
			var userInfo = uni.getStorageSync(sk.userInfo);
			that.userInfo = userInfo;


		},
		// 触底加载更多
		onReachBottom() {
			uni.showLoading({
				title: "加载中..."
			})
			that.loadList();
		},
		// 下拉刷新
		onPullDownRefresh() {
			uni.showLoading({
				title: "加载中..."
			})
			that.currentPage = 1;
			that.list = [];
			that.loadList();
			uni.stopPullDownRefresh();
		},
		methods: {
			setPrice(e) {
				that.price = e.detail.value;
			},
			setContent(e) {
				that.content = e.detail.value;
			},

			timeStampTurnTime(str) {
				var date = new Date(str); // 参数需要毫秒数，所以这里将秒数乘于 1000
				var Y = date.getFullYear() + '-';
				var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
				var D = date.getDate() + ' ';
				return Y + M + D;
			},

			getAuditStatus(e) {
				if (e == 0) {
					return "待审核";
				}
				if (e == 1) {
					return "审核通过";
				}
				if (e == 2) {
					return "审核不通过";
				}
				if (e == 3) {
					return "已删除";
				}
				if (e == 4) {
					return "已成交";
				}
			},

			loadList() {

				api.post({
					projectType: 1,
					currentPage: that.currentPage,
					pageSize: that.pageSize
				}, api.projectFindList).then(res => {
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
			releaseProject(type) {
				this.type = type;
				// open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
				this.$refs.popup.open(type);
			},
			submit() {
				if (su.isBlank(that.price)) {
					uni.showToast({
						icon: 'none',
						title: '预算不为空',
					})
					return;
				}
				if (that.price < 50) {
					uni.showToast({
						icon: 'none',
						title: '预算低于50元',
					})
					return;
				}

				if (su.isBlank(that.content)) {
					uni.showToast({
						icon: 'none',
						title: '需求内容不为空',
					})
					return;
				}

				uni.showLoading({
					title: "加载中..."
				});
				api.post({
					content: that.content,
					price: that.price,
					ipAddress: uni.getStorageSync(sk.ipTerritory),
					nickname: that.userInfo.nickName,
				}, api.projectAddProject).then(res => {
					if (res.code == 200) {

						uni.showLoading({
							title: '加载中...',
						})
						that.content = '';
						that.price = '';
						that.list = [];
						that.currentPage = 1;
						that.pageSize = 10;
						that.loadList();
						this.$refs.popup.close();
					} else {
						uni.hideLoading();
					}
					uni.showToast({
						icon: 'none',
						title: res.msg
					})
				});
			},
			cancelSubmit() {
				this.$refs.popup.close();
			},
			toProjecDetail(e) {
				uni.navigateTo({
					url: '/pages/release/releaseProjectDetail/releaseProjectDetail?obj=' + encodeURIComponent(JSON
						.stringify(e))
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.space-line-style {
		width: 100%;
		height: 200rpx;
	}

	.project-list-style {
		width: 96%;
		margin-left: 2%;
		height: 280rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 10rpx;
		margin-top: 30rpx;
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

	.status-pass-style {
		width: 30%;
		background-color: #7CBF80;
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

	.status-refuse-style {
		width: 30%;
		background-color: #E26C65;
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

	.status-cancel-style {
		width: 30%;
		background-color: #AAACB7;
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

	.button-style {
		width: 90%;
		margin-left: 5%;
		height: 150rpx;
		display: flex;
		justify-content: center;
		align-items: center;

		position: fixed;
		bottom: 10rpx;

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
		margin-top: 30rpx;
		margin-bottom: 30rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 10px;
		height: 320rpx;
	}

	.description-style textarea {
		width: 96%;
		margin-top: 20rpx;
		margin-left: 2%;
		height: 280rpx;
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
		height: 950rpx;
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

	.click-release-style {
		width: 100rpx;
		height: 100rpx;
		position: fixed;
		z-index: 2;
		bottom: 120px;
		right: 40px;
		background-color: #49A8E7;
		color: white;
		font-size: 14px;
		border-radius: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: #49A8E7 1px 0px 5px 1px;
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
