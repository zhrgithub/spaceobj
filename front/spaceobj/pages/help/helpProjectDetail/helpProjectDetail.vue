<template>
	<view class="project-detail-background-style">
		
		<view class="base-info-panel-style">
			<view class="project-num-status-style">
				<view class="project-numer-style">
					项目编号：001
				</view>
				<view class="project-status-style">
					状态：待接包
				</view>
			</view>
			
			<view class="project-num-status-style">
				<view class="project-numer-style">
					预算：2000元
				</view>
				<view class="project-status-style">
					浏览：2000次
				</view>
			</view>
			
			<view class="project-num-status-style">
				<view class="project-numer-style">
					IP属地：深圳
				</view>
				<view class="project-numer-style">
					用户名：张三
				</view>
			</view>
		</view>

		
		
		<view class="description-requirement-style">
			<view class="description-content-style">
				<text style="font-weight: bold;font-size: 14px;color: #7CBF80;">项目描述：</text>测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试吧吧v测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试吧吧v测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试吧吧v测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试吧吧v测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试吧吧v测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试吧吧v测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试吧吧v测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试
			</view>
			
		</view>
		<view class="btn-background-style" >
			<button @click="getUserInfo">立即联系</button>
			
		</view>
		
	</view>
</template>

<script>
	var that;
	export default {
		data() {
			return {
				//项目状态，如果甲方已关闭，那么拒绝获取客户联系方式
				projectStatus:1,
				//用户的邀请值>1,或者助力值>10
				helpValue:0,//助力值
				inviteValue:0,//邀请值
				//用户已经获取过
				getStatus:false,
			}
		},
		created() {
			that = this;
		},
		methods: {
			getUserInfo(){
				if(this.projectStatus==0){
					uni.showModal({
						title:"温馨提示",
						content:"已成交，甲方隐藏联系方式",
						confirmText:"确定",
						showCancel:false,
						confirmColor:"black",
						success(e) {
							
						}
					})
					
				}else{
					
					if(that.getStatus){
						uni.showModal({
							title:"联系方式",
							content:"13362620045",
							showCancel:false
						})
						return;	
					}
					if(that.helpValue>=10||that.inviteValue>=1){
						uni.showModal({
							title:"联系方式",
							content:"13362620045",
							showCancel:false
						})
						that.helpValue-=10;
						that.inviteValue-=1;
						that.getStatus = true;
						return;	
					}
					uni.showModal({
						cancelText:"放弃",
						title:"温馨提示",
						content:"您需要分享好友助力获取",
						confirmText:"立即分享",
						confirmColor:"black",
						success(e) {
							if(e.confirm){
								uni.showToast({
									title:"助力链接已复制到剪切板，快去分享吧",
									icon:"none",
									duration:3000
								})
							}
						}
					})
				}
				
			}
		}
	}
</script>

<style scoped>
.project-detail-background-style{
	width: 100%;
	height: 100%;
	position: absolute;
}
.base-info-panel-style{
	width: 96%;
	margin-left: 2%;
	height: 300rpx;
	box-shadow: darkgray 0px 0px 2px 0px;
	margin-top: 50rpx;
	border-radius: 10px;
}
.project-num-status-style{
	width: 90%;
	margin-left: 5%;
	height: 95rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 14px;
}

.project-numer-style{
	width: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
}
.project-status-style{
	width: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
}
.description-requirement-style{
	width: 96%;
	margin-left: 2%;
	margin-top: 50rpx;
	border-radius: 10rpx;
	box-shadow: darkgray 0px 0px 2px 0px;
	display: flex;
	justify-content: center;
	align-items: center;
}
.description-content-style{
	width: 90%;
	margin-top: 30rpx;
	margin-bottom: 30rpx;
	font-size: 15px;
}
.btn-background-style{
	width: 96%;
	height: 280rpx;
	margin-left: 2%;
	margin-top: 80rpx;
	margin-bottom: 500rpx;
}
.btn-background-style button{
	width: 100%;
	background-color: #49A8E7;
	color: white;
	height: 100rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}
</style>
