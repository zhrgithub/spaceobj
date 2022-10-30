<template>
	<view class="project-detail-background-style">

		<view class="base-info-panel-style">
			<view class="project-num-status-style">
				<view class="project-numer-style">
					项目编号：{{projectObj.pid}}
				</view>
				<view class="project-numer-style">
					状态：{{getAuditStatus(projectObj.status)}}
				</view>
			</view>

			<view class="project-num-status-style">
				<view class="project-numer-style">
					预算：{{projectObj.price}}元
				</view>
				<view class="project-numer-style">
					浏览：{{projectObj.pageViews}}次
				</view>
			</view>

			<view class="project-num-status-style">
				<view class="project-numer-style">
					IP属地：{{projectObj.ipAddress}}
				</view>
				<view class="project-numer-style">
					用户：{{projectObj.nickname}}
				</view>
			</view>
		</view>



		<view class="description-requirement-style">
			<view class="description-content-style">
				<text style="font-weight: bold;font-size: 14px;color: #7CBF80;">项目描述：</text>{{projectObj.content}}
			</view>
		</view>
		<view class="btn-background-style">
			<button style="width: 40%;" @click="cancelRelease">删除</button>
			<button style="width: 40%;margin-left: 10%;" @click="repeatRelease('bottom')">重新发布</button>
		</view>
		<!-- 重新发布 -->
		<uni-popup ref="popup" background-color="#fff">
			<view class="need-description-budget-style">
				项目描述和预算
			</view>
			<view class="scroll-item-line-two-style"></view>
			<view class="description-doller-style">
				<view class="doller-num-style">
					<input placeholder="请输入预算(单位:元)" :value="projectObj.price" type="number" maxlength="15"
						@input="setPrice">
				</view>
				<view class="description-style">
					<textarea maxlength="1000" cols="30" rows="100" placeholder="请输入您的需求信息" :value="projectObj.content" @input="setContent" />
				</view>
				<view class="button-style">
					<button @click="submit">取消</button>
					<view class="button-space"></view>
					<button @click="submit">确认发布</button>
				</view>
			</view>
		</uni-popup>

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
				//项目状态，如果甲方已关闭，那么拒绝获取客户联系方式
				projectStatus: 1,
				//用户的邀请值>1,或者助力值>10
				helpValue: 0, //助力值
				inviteValue: 0, //邀请值
				//用户已经获取过
				getStatus: false,
				type: 'center',

				projectObj: ""
			}
		},
		created() {
			that = this;
		},
		onLoad(e) {
			var str = decodeURIComponent(e.obj);
			var obj = JSON.parse(str);
			that.projectObj = obj;
			if (obj.status == 2) {
				uni.showModal({
					title: "审核不通过",
					content: obj.message,
					showCancel: false
				})
			}
		},
		methods: {
			setPrice(e) {
				that.projectObj.price = e.detail.value;
			},
			setContent(e) {
				that.projectObj.content = e.detail.value;
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
			cancelRelease() {
				uni.showModal({
					content: "取消发布，其他人将无法联系您",
					success(e) {
						if (e.confirm) {
							that.projectObj.status = 3;
							that.updateProject();
						}
					}
				})
			},
			repeatRelease(type) {
				this.type = type;
				// open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
				this.$refs.popup.open(type)
			},

			submit() {
				this.$refs.popup.close();
				uni.showModal({
					title:"确认提交",
					success(e) {
						if(e.confirm){
							that.updateProject();
						}
					}
				})
				
			},

			updateProject() {
				var obj = that.projectObj;
				api.postJson(obj, api.projectUpdateProject).then(res => {
					uni.hideLoading();
					uni.showToast({
						title: res.msg,
						icon: 'none',
						success() {
							if(res.code==200){
								uni.navigateBack();
							}
						}
					})
				})
			},
		}
	}
</script>

<style scoped>
	.button-space {
		width: 10%;
	}

	.description-doller-style {
		width: 100%;
		height: 700rpx;
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

	.project-detail-background-style {
		width: 100%;
		height: 100%;
		position: absolute;
	}

	.base-info-panel-style {
		width: 96%;
		margin-left: 2%;
		height: 300rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		margin-top: 50rpx;
		border-radius: 10px;
	}

	.project-num-status-style {
		width: 90%;
		margin-left: 5%;
		height: 95rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 14px;
	}

	.project-numer-style {
		width: 50%;
		text-align: left;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.description-requirement-style {
		width: 96%;
		margin-left: 2%;
		margin-top: 50rpx;
		border-radius: 10rpx;
		box-shadow: darkgray 0px 0px 2px 0px;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.description-content-style {
		width: 90%;
		margin-top: 30rpx;
		margin-bottom: 30rpx;
		font-size: 15px;
	}

	.btn-background-style {
		width: 96%;
		height: 280rpx;
		margin-left: 2%;
		margin-bottom: 200rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.btn-background-style button {
		width: 100%;
		background-color: #49A8E7;
		color: white;
		height: 100rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>
