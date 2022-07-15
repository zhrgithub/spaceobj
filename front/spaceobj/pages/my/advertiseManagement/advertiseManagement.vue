<template>
	<view class="container">
		<view class="shop-list-style" @click="editAdvertise">
			<view class="commodity-image-description-background-style">
				<view class="commodity-image-background-style">
					<image src="/static/bg.jpg" mode=""></image>
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
		</view>

		<view class="click-release-style" @click="addAdvertise">添加</view>



		<uni-popup ref="popup" background-color="#fff">
			<view class="need-description-budget-style">
				广告表单
			</view>
			<view class="scroll-item-line-two-style"></view>
			<view class="description-doller-style">

				<view class="doller-num-style">
					<input placeholder="商品超链接">
				</view>
				<view class="doller-num-style">
					<input placeholder="商品图片链接">
				</view>
				<view class="doller-num-style">
					<input placeholder="商品名称">
				</view>
				<view class="doller-num-style">
					<input placeholder="商家名称">
				</view>
				<view class="doller-num-style">
					<input placeholder="优惠券描述">
				</view>

				<view class="doller-num-style">
					<input placeholder="价格描述">
				</view>
				<view class="doller-num-style">
					<input placeholder="好评数量">
				</view>

				<view class="button-style">
					<button @click="del">删除</button>
					<view class="button-space"></view>
					<button @click="submit">保存</button>
				</view>
			</view>
		</uni-popup>


	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {

			editAdvertise() {
				this.$refs.popup.open('bottom')
			},
			addAdvertise() {
				this.$refs.popup.open('bottom')
			},
			del() {
				this.$refs.popup.close();
				uni.showToast({
					title: "删除成功",
					icon: 'none',
					duration: 2000
				})
			},
			submit() {
				this.$refs.popup.close();
				uni.showToast({
					title: "提交成功",
					icon: 'none',
					duration: 2000
				})

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

	.shop-list-style {
		width: 96%;
		margin-left: 2%;
		height: 200rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 10rpx;
		margin-top: 20px;
		margin-bottom: 10rpx;
		display: flex;
		justify-content: center;
		align-items: center;

	}

	.commodity-image-description-background-style {
		width: 98%;
		margin-left: 1%;
		height: 98%;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 2.5%;
		height: 90%;
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

	.description-doller-style {
		width: 100%;
		height: 900rpx;
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
</style>
