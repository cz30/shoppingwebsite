$(function(){
	$('label').mouseenter(function(){
		$(this).css('background','aliceblue');
	}).mouseleave(function(){
		$(this).css('background','none');
	})
})

var outTradeNo = $.cookie("outTradeNo");
var Delid = $.cookie("VipOdDelid");
var isV = $.cookie("isV");
var uId = $.cookie("userId");

$(function(){
	
	$('#payment-submit').on('click',function(){
		
		
		/* 订单方法 */
		if ($('#Alipay')[0].checked) {
			/* 支付宝请求 */
			
			if (isV == 0) {
				
				console.log("支付宝");
				
				window.location.href='/alipay/submit?outTradeNo='+ outTradeNo;
				
			} else {
				
				console.log("vip支付宝");
				
				window.location.href='/vippay/alipay/submit?UId='+uId+'&Delid='+ Delid;
				
			}
			
			
		} else if ($('#WeChat-Pay')[0].checked) {
			
			console.log("微信");
			
			window.open('WePay','_self');
		}
		
		
		ovtimer = setInterval(orderValidity,1000);
		
		
		
	})
	
})


//定义倒计时的时间(倒计时30分钟)
var minutes = 29;
var seconds = 58;

function orderValidity(){

	//判断时间到了没
	if(seconds == 0 && minutes == 0){
		clearInterval(ovtimer);//清除定时器
		console.log("时间到");
		
		
		if (isV == 1){
			
			$.ajax({
				url:"/vip/deleteVipOrderRedis",
				type: "POST",
				datatype:"json",
				success:function(data){
					console.log(data);
					window.open('vipHome','_self');
				},
				error: function (){
					console.log('error');
				}
			})
			
		} 
		
		alert("订单已过期，请重新下单");
		

		return;
	}

	seconds--;

	if(seconds<0){
		seconds = 59;
		minutes--;
	}

	minutes = (minutes+"").length==1?"0"+minutes:minutes;//(minutes+"")是将其数据类型转换成字符串类型
	seconds = (seconds+"").length==1?"0"+seconds:seconds;
	console.log(minutes+":"+seconds);
}

/* 删除历史 禁止后退 */
$(function() {
　　if (window.history && window.history.pushState) {
	　　$(window).on('popstate', function () {
		　　window.history.pushState('forward', null, '#');
		　　window.history.forward(1);
　　		});
　　}
　　window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
　　window.history.forward(1);
})