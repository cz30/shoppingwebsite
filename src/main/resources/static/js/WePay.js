var outTradeNo = $.cookie("outTradeNo");
var Delid = $.cookie("VipOdDelid");
var isV = $.cookie("isV");
var uId = $.cookie("userId");

$(function(){
	
	// console.log(isV);
	
	if (isV == 0) {
		/* 普通订单微信支付 */
		$.ajax({
			url:"/wx/submit",
			type: "POST",
			datatype:"json",
				async: false,
				data:{
					outTradeNo:outTradeNo
				},
			success:function(data){
				console.log(data);
				
				/* 生成二维码 */
				var qrcode = new QRCode(document.getElementById("QRcode"), {
					text: data.code_url,
				    width: 200,
				    height: 200,
				    colorDark : "#000000",
				    colorLight : "#ffffff",
				    correctLevel : QRCode.CorrectLevel.H
				});
				
				/* 修改订单号 金额 */
				$('#order-Id')[0].append(data.out_trade_no);
				$('#mount-payable')[0].append((data.total_fee / 100).toFixed(2));
			}
		})
		
	} else {
		/* vip充值订单微信支付 */
		$.ajax({
			url:"/vippay/wx/submit",
			type: "POST",
			datatype:"json",
				async: false,
				data:{
					UId:2,
					Delid:Delid
				},
			success:function(data){
				// console.log(data);
				
				/* 生成二维码 */
				var qrcode = new QRCode(document.getElementById("QRcode"), {
					text: data.code_url,
				    width: 200,
				    height: 200,
				    colorDark : "#000000",
				    colorLight : "#ffffff",
				    correctLevel : QRCode.CorrectLevel.H
				});
				
				/* 修改订单号 金额 */
				$('#order-Id')[0].append(data.out_trade_no);
				$('#mount-payable')[0].append((data.total_fee / 100).toFixed(2));
			}
		})
		
	}
	
	
	
	//每5秒执行一次myFunction()方法
	if (isV == 0) {
		var pttimer = setInterval(pollingTask, 1000 * 3);
	} else {
		var vippttimer = setInterval(vippollingTask, 1000 * 3);
	}
	
	
	var i = 1;
	
	function pollingTask() {
			
			
		if (i == 600) {
			clearInterval(pttimer);
		}
		
		console.log("轮询任务=================>" + i++);
		
		$.ajax({
			url:"/wx/pay/status",
			type: "POST",
			datatype:"json",
			data: {
				outTradeNo:outTradeNo
			},
			success:function(data){
				console.log(data);
				if (data == 1) {
					clearInterval(pttimer);
					window.open('successjump', '_self');
				}
			},
			error: function (){
				console.log('error');
			}
		})
			
	}
	
	function vippollingTask() {
			
			
		if (i == 600) {
			clearInterval(vippttimer);
		}
		
		console.log("轮询任务=================>" + i++);
		
		$.ajax({
			url: "/vippay/wx/pay/status",
			type: "POST",
			datatype: "json",
			data: {
				outTradeNo: outTradeNo
			},

			success:function(data){
				console.log(data);
				if (data == 1) {
					clearInterval(vippttimer);
					window.open('successjump', '_self');
				}
			},
			error: function (){
				console.log('error');
			}
		})
			
	}
})


