<template>
	<view class="release-background-style">
		<view v-if="contenList.length!=0" class="content-null-style">
			<view class="image-title-background-style">
				<view class="not-release-image-style-background">
					<image src="/static/notAnything.png" mode=""></image>
				</view>
				<view class="title-context">
					您还没有发布项目信息~
				</view>
			</view>
		</view>

		<view class="click-release-style" @click="showMoreUser('bottom')">发布</view>


		<!-- 搜索框 -->
		<view class="search-background-style">
			<view class="search-logo-background-style">
				<view class="image-background-style">
					<image src="/static/searchInput.png" mode=""></image>
				</view>
				<view class="input-background-style">
					<input :value="seachText" type="text" maxlength="15" placeholder="元宇宙、商城、聊天、APP、网站"
						placeholder-style="font-size:14px" confirm-type="search" @confirm="doSearch" @input="inputText">
					<image src="/static/deleteSearch.png" @click="clearInput">
				</view>
			</view>

		</view>
		<view class="top-space-line-style"></view>

		<uni-popup ref="popup" background-color="#fff">
			<view class="need-description-budget-style">
				项目描述和预算
			</view>
			<view class="scroll-item-line-two-style"></view>
			<view class="description-doller-style">
				<view class="doller-num-style">
					<input placeholder="请输入预算(单位:元)" type="number" maxlength="7">
				</view>
				<view class="description-style">
					<textarea maxlength="1000" name="needDescription" id="" cols="30" rows="100"
						placeholder="请输入您的需求信息"></textarea>
				</view>
				<view class="button-style">
					<button @click="cancelSubmit">取消</button>
					<view class="button-space"></view>
					<button @click="submit">确认发布</button>
				</view>
			</view>
		</uni-popup>

		<!-- 发布的列表 -->
		<view class="project-list-style" @click="toProjecDetail">
			<view class="date-status-style">
				<view class="date-style">
					2022-06-18
				</view>
				<view class="status-style">
					待接包
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


		<view class="shop-list-style">
			<swiper class="swiper" @change="getItem" :current="act" autoplay="true" interval="3000" circular="true">
				<swiper-item v-for="(item,ids) in shopList" :key="ids">
					<uni-link href="https://item.jd.com/100010599633.html#crumb-wrap" showUnderLine="false">
						<view class="commodity-image-description-background-style">
							<view class="commodity-image-background-style">
								<image src="https://img13.360buyimg.com/n1/jfs/t1/180386/10/10394/148552/60d012f9Ed0a2ad10/ad4326c6c4e2a90d.jpg" mode=""></image>
							</view>
							<view class="description-background-style">
								<view class="title-description-style">
									京东：家香味 沂蒙土榨 花生仁油6.18L 食用油 中粮福临门出品
								</view>
								<view class="store-description-style">
									商家：中粮家香味京东自营旗舰店
								</view>

								<view class="discount-description-style">
									优惠卷：满11元可用、满169享8.5折
								</view>

								<view class="price-good-diss-style">
									<view class="price-num-style">
										价格：￥300
									</view>
									<view class="good-diss-style">
										好评：99万+
									</view>
								</view>

							</view>
						</view>
					</uni-link>
				</swiper-item>
			</swiper>
		</view>

		<view class="project-list-style" @click="toProjecDetail">
			<view class="date-status-style">
				<view class="date-style">
					2022-06-18
				</view>
				<view class="status-style">
					待接包
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
				contenList: [],
				type: 'center',
				seachText: "",
				// 默认激活样式是第一个
				act: '',
				//数据索引
				currentIndex: 0,
				shopList: [{
					id: 0,
					name: "测试"
				}, {
					id: 0,
					name: "测试"
				}, {
					id: 0,
					name: "测试"
				}],
			}
		},
		methods: {
			showMoreUser(type) {
				this.type = type;
				// open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
				this.$refs.popup.open(type)
			},
			submit() {
				this.$refs.popup.close();
				uni.showToast({
					title: "提交成功",
					icon: 'none',
					duration: 2000
				})
				uni.switchTab({
					url: "/pages/release/release"
				})
			},
			cancelSubmit() {
				this.$refs.popup.close();
			},
			toProjecDetail() {
				uni.navigateTo({
					url: '/pages/projectDetail/projectDetail'
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
			getItem(e) {
				console.log(e)
				this.act = e.detail.current;
			},
		}
	}
</script>

<style scoped>
	/* 广告插入 */
	.swiper {
		height: 100%;
		width: 100%;
	}

	.swiper-item {
		display: block;
		height: 100%;
		line-height: 100%;
		text-align: center;
		border: 1rpx solid black;
		/* display: flex;
			justify-content: center;
			align-items: center;
			background-color: green; */
	}

	.commodity-image-description-background-style {
		width: 98%;
		margin-left: 1%;
		height: 98%;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 2.5%;
	}

	.commodity-image-background-style {
		width: 25%;
		height: 98%;
	}

	.commodity-image-background-style image {
		height: 160rpx;
		width: 160rpx;
	}

	.description-background-style {
		width: 75%;
		height: 100%;
	}

	.title-description-style {
		width: 100%;
		overflow: hidden;
		text-overflow: ellipsis;
		color: black;
		font-size: 14px;
		white-space: nowrap;
	}

	.price-good-diss-style {
		display: flex;
		color: red;
		font-size: 15px;
		font-weight: bold;
	}

	.price-num-style {
		width: 50%;
		display: flex;
		justify-content: left;
		align-items: center;
	}

	.good-diss-style {
		width: 50%;
		font-size: 12px;
		color: grey;
		display: flex;
		justify-content: right;
		align-items: center;
	}

	.discount-description-style {
		display: flex;
		color: red;
		font-size: 14px;

	}

	.store-description-style {
		width: 100%;
		color: black;
		font-size: 14px;
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

	.project-list-style {
		width: 96%;
		margin-left: 2%;
		height: 280rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 10rpx;
		margin-bottom: 30rpx;
	}

	.shop-list-style {
		width: 96%;
		margin-left: 2%;
		height: 200rpx;
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

	.button-style {
		width: 90%;
		margin-left: 5%;
		height: 150rpx;
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
		height: 700rpx;
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
		font-size: 15px;
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
