<template>
	<view class="release-background-style">
		<view v-if="contenList.length!=0" class="content-null-style">
			<view class="image-title-background-style">
				<view class="not-release-image-style-background">
					<image src="/static/notAnything.png" mode=""></image>
				</view>
				<view class="title-context">
					没有发起助力信息~
				</view>
			</view>
		</view>
		
		<uni-popup ref="popup" background-color="#fff">
			<view class="need-description-budget-style">
				项目描述和预算
			</view>
			<view class="scroll-item-line-two-style"></view>
			<view class="description-doller-style">
				<view class="doller-num-style">
					<input  placeholder="请输入预算(单位:元)" type="number" maxlength="7">
				</view>
				<view class="description-style">
					<textarea maxlength="1000" name="needDescription" id="" cols="30" rows="100" placeholder="请输入您的需求信息"></textarea>
				</view>
				<view class="button-style">
					<button @click="submit">取消</button>
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
					助力成功
				</view>
				<view class="detail-style">
					查看详情
				</view>
			</view>
			<view class="brief-information-style">
				<text style="color: #7CBF80;font-weight: bold;font-size: 15px;">项目描述：</text>查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手大脚看看是科斯康查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手
			</view>
			<view class="slider-style">
				<slider value="10" activeColor="green" backgroundColor="darkgray" block-color="#49A8E7" block-size="10" show-value min="0" max="10" disabled="true" />
			</view>
		</view>
		
		<view class="project-list-style">
			<view class="date-status-style">
				<view class="date-style">
					2022-06-17
				</view>
				<view class="status-pass-style">
					还差3人
				</view>
				<view class="detail-style">
					查看详情
				</view>
			</view>
			<view class="brief-information-style">
				<text style="color: #7CBF80;font-weight: bold;font-size: 15px;">项目描述：</text>查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手大脚看看是科斯康查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手
			</view>
			<slider value="7" activeColor="green" backgroundColor="darkgray" block-color="#49A8E7" block-size="10" show-value min="0" max="10" disabled="true" />
		</view>
		
		<view class="project-list-style">
			<view class="date-status-style">
				<view class="date-style">
					2022-06-17
				</view>
				<view class="status-refuse-style">
					甲方已成交
				</view>
				<view class="detail-style">
					查看详情
				</view>
			</view>
			<view class="brief-information-style">
				<text style="color: #7CBF80;font-weight: bold;font-size: 15px;">项目描述：</text>查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手大脚看看是科斯康查看详情初三初四就看出的扩散才开始单词卡死此电脑萨克才能上看大手
			</view>
			<slider value="7" activeColor="green" backgroundColor="darkgray" block-color="#49A8E7" block-size="10" show-value min="0" max="10" disabled="true" />
		</view>

		
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				contenList:[],
			}
		},
		methods: {
			submit(){
			   this.$refs.popup.close();	
			   uni.showToast({
			   	title:"提交成功",
				icon:'none',
				duration:2000
			   })
			},
			sliderChange(e) {
            console.log('value 发生变化：' + e.detail.value)
			},
			toProjecDetail(){
				uni.navigateTo({
					url:'/pages/help/helpProjectDetail/helpProjectDetail'
				})
			},
		}
	}
</script>

<style scoped>
	.project-list-style{
		width: 96%;
		margin-left: 2%;
		height: 285rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 10rpx;
		margin-top: 30rpx;
	}
	.date-status-style{
		width: 98%;
		margin-left: 1%;
		height: 30%;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.brief-information-style{
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
	.slider-style{
		width: 30%;
		width: 100%;
	}
	
	.date-style{
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
	
	.status-style{
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
	.status-pass-style{
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
	.status-refuse-style{
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
	
	.detail-style{
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
	.button-style{
		width: 90%;
		margin-left: 5%;
		height: 180rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.button-style button{
		width: 40%;
		background-color: #49A8E7;
		color: white;
	}
	.button-space{
		width: 10%;
	}
	.description-style{
		width: 90%;
		margin-left: 5%;
		display: flex;
		align-items: center;
		margin-top: 30rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		border-radius: 10px;
	}
	.description-style textarea{
		width: 96%;
		margin-top: 20rpx;
		margin-left: 2%;
	}
	.doller-num-style{
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
	.doller-num-style input{
		width: 90%;
	}
	.description-doller-style{
		width: 100%;
		height: 600rpx;
	}
.release-background-style{
	width: 100%;
	height: 100%;
	position: absolute;
}
.content-null-style{
	width: 100%;
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
}
.image-title-background-style{
	width: 50%;
	height: 500rpx;
}

.title-context{
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 14px;
	color: lightgrey;
}
.not-release-image-style-background{
	width: 100%;
	height: 70%;
	display: flex;
	align-items: center;
	justify-content: center;
}
.not-release-image-style-background image{
	width: 250rpx;
	height: 250rpx;
}

.need-description-budget-style{
	width: 100%;
	font-weight: bold;
	display: flex;
	align-items: center;
	justify-content: center;
	height: 80rpx;
}
.scroll-item-line-two-style{
	width: 100%;
	border-top: 1rpx solid #e1e1e1;
	content: '';
}
</style>
