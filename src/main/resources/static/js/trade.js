$(function(){
	var adId = ''
	var svipDiscount = 0;
	var defaultAdId = 0;
	$.ajax({
		url:"/show/trade",
		type: "GET",
		datatype:"json",
		data:{
			uId:$.cookie('userId')
		},
		async: false,
		success:function(data){
			console.log(data);
			console.log(data.defaultAdId);
			// let j = 1;
			/* 地址框渲染 */
			defaultAdId = data.defaultAdId;
			if (data.defaultAdId === null) {

				console.log("wudizhi");
				$('#addbtn').trigger('click');
				// alert("请先添加默认地址");
				// window.open('')


			} else {

				for ( let i = 0; i < data.addressList.length; i++){

					let obj = data.addressList[i];



					if (data.defaultAdId == obj.adId) {   //默认地址
						project =
							'<span class="address-name" style="color: green;">' + obj.adUser + '</span>&nbsp&nbsp收<br>' +
							'<span>' + obj.adAddress + '</span><br>' +
							'电话：<span>' + obj.adPhone + '</span>' +
							'<div class="default-address active">' +
							'默认地址' +
							'</div>' +
							'<div class="set-as-default">' +
							'设为默认' +
							'</div>' +
							'<div class="address-modify modifybtn' + i + '" data-toggle="modal" data-target="#addAddress-Modal">修改地址</div>' +
							'<div class="checkbtn checkbtn-selected" value="' + obj.adId + '">' +
							'<span>√</span>' +
							'</div>';

						$('#defaultAd-box').append(project);

					} else {     //其他地址

						project = '<div class="address-box">' +
							'<span class="address-name" style="color: green;">' + obj.adUser + '</span>&nbsp&nbsp收<br>' +
							'<span>' + obj.adAddress + '</span><br>' +
							'电话：<span>' + obj.adPhone + '</span>' +
							'<div class="default-address">' +
							'默认地址' +
							'</div>' +
							'<div class="set-as-default">' +
							'设为默认' +
							'</div>' +
							'<div class="address-modify modifybtn' + i + '" data-toggle="modal" data-target="#addAddress-Modal">修改地址</div>' +
							'<div class="checkbtn" value="' + obj.adId + '">' +
							'<span>√</span>' +
							'</div>' +
							'</div>';

						$('.address-boxs').append(project);
					}
				}
			}

			
			
			/* 商品渲染 */
			for ( let i = 0; i < data.cartList.length; i++){
				
				let obj = data.cartList[i];
				
				project = '<tr>' +
							'<td class="zy-item">' +
								'<div class="picturebox fl">' +
									'<img src="' + obj.cartImage + '" alt="">' +
								'</div>' +
								
								'<span>' + obj.cartTitle + '</span>' +
							'</td>' +
							'<td class="zy-itemAttributes">' +
								obj.cartContent +
							'</td>' +
							'<td class="zy-price" >' +
								'<span>' + obj.cartOprice.toFixed(2) + '</span>' +
							'</td>' +
							'<td class="zy-amount">' +
								'<span>' + obj.cartNum + '</span>' +
							'</td>' +
							'<td class="zy-sum">' + /*text-decoration: line-through;*/
								'<span class="Nprice">' + obj.cartNprice.toFixed(2) + '</span>' +
								'<span class="Vipprice">' + (obj.cartNprice * obj.discount).toFixed(2) + '</span>' +
							'</td>' +
						'</tr>';
						
				$('tbody').append(project);

				svipDiscount += obj.cartNprice * (1 - obj.discount);

			}

			/* 配送费渲染 */
			$('#delivery-fee').text((data.dpFee).toFixed(2));
			$('#delivery-fee').attr('cmdFee',data.cmdFee);

			$('#member-delivery-using').attr('mptimers',data.mptimers);

			/* 优惠券渲染 */
			for ( let i = 0; i < data.couponList.length; i++){
				
				let obj = data.couponList[i];
				
				project = '<li><a href="javascript:void(0);" value="' + obj.cnValue + '" cnId="' + obj.cnId + '">' + obj.cnName + '</a></li>';
						
				$('.drop-down>ul').append(project);
			}
			
			/* 积分渲染 */
			$('#cumulative-scores').text(data.integral);
			
			/* 会员渲染 */
			if (data.isMember) {
				$('#isMember').show();
				$('#isNotMember').hide();
				if (data.mptimers == null) {
					$('#member-delivery-using').hide()
					$('#isMember span').text("您剩余0次免配送权益");
				} else {
					$('#isMember span').text("您剩余" + data.mptimers + "次免配送权益");
				}

				/* 显示折扣价 */
				$('.Vipprice').show().addClass('total');
				$('.Nprice').css({
					'text-decoration': 'line-through',
					'color':'gray'
				});

				$('#SVIP-discount').text(-1 * svipDiscount.toFixed(2));

			} else {
				$('#isMember').hide();
				$('#isNotMember').show();

				$('.Nprice').addClass('total');
			}
			
		}
	});
	
	/* 调用结算总金额 */
	$('body').totalAmount();
	
	// 调用留言框高度自适应
	$('#handledMsg').txtaAutoHeight();

	/* 默认地址关联 */
	if (defaultAdId){
		$('.sum-addtrss').find('span')[0].innerText = $('.checkbtn-selected').siblings('span')[1].innerText;
		$('.sum-addtrss').find('span')[1].innerText = "收件人：" + $('.checkbtn-selected').siblings('span')[0].innerText + "  " +$('.checkbtn-selected').siblings('span')[2].innerText;
	}

	/* 地址框选择 */
	$('.address-box').on('mouseenter',function(){
		$(this).addClass('address-box-hover');
		// 在移入选中的地址框时 非默认显示设为默认
		if($(this).find($('.checkbtn')).hasClass('checkbtn-selected')&&!$(this).find($('.default-address')).hasClass('active')){
			$(this).find($('.set-as-default')).show();
		}
		if($(this).find($('.checkbtn')).hasClass('checkbtn-selected')){
			$(this).find($('.address-modify')).show();
		}
	}).on('mouseleave',function(){
		$(this).removeClass('address-box-hover');
		
		$(this).find($('.set-as-default')).hide();
		$(this).find($('.address-modify')).hide();
	}).on('click',function(){
		/* 显示选中标识 */
		$('.checkbtn').removeClass('checkbtn-selected');
		$(this).find($('.checkbtn')).addClass('checkbtn-selected');
		
		/* 关联选中地址 */
		$('.sum-addtrss').find('span')[0].innerText = $(this).find('span')[1].innerText;
		$('.sum-addtrss').find('span')[1].innerText = "收件人：" + $(this).find('span')[0].innerText + "  " +$(this).find('span')[2].innerText;
	});
	
	/* 设为默认地址 */
	$('.set-as-default').on('mouseenter',function(){
		$(this).addClass('set-as-default-hover');
	}).on('mouseleave',function(){
		$(this).removeClass('set-as-default-hover');
	}).on('click',function(){
		$('.default-address').removeClass('active');
		$(this).siblings('.default-address').addClass('active');
		adId = this.parentNode.children[8].attributes.value.value;
		
		$.ajax({
			url:"/default/address",
			type: "POST",
			datatype:"json",
			data: {
				uId:$.cookie('userId'),
				adId: adId
			},
			success:function(){
				console.log('success');
			},
			error:function(){
				console.log('error');
			}
		});
		
		
	});
	
	
	
	/* 修改地址 */
	$('.address-modify').on('mouseenter',function(){
		$(this).addClass('address-modify-hover');
	}).on('mouseleave',function(){
		$(this).removeClass('address-modify-hover');
	}).on('click',function(){
		console.log(this);
		console.log(this.parentNode.childNodes);
		adId = this.parentNode.children[8].attributes.value.value;
		$('.modal-title').text('修改地址');
		$('.btn-primary').text('修改');
		$('.dress').val(this.parentNode.childNodes[3].textContent)
		$('.person').val(this.parentNode.childNodes[0].textContent)
		$('.phoneNumber').val(this.parentNode.childNodes[6].textContent)
	});
	
	/* 创建新地址 */
	$('#addbtn').click(function () {
		$('.modal-title').text('添加新地址');
		$('.btn-primary').text('添加');
		$('.dress').val('')
		$('.person').val('')
		$('.phoneNumber').val('')
	})
	
	/* 修改添加地址按钮事件 */
	$('#corretDress').click(function () {
		if($('#corretDress').text() == '添加') {
			console.log('添加')
			$.ajax({
				url:"/add/new/address",
				type: "POST",
				datatype:"json",
				data: {
					uId:$.cookie('userId'),
					adAddress: $('.dress').val(), 
					adUser: $('.person').val(), 
					adPhone: $('.phoneNumber').val()
				},
				success:function(){
					console.log('添加成功');
					window.location.reload()
				},
				error:function(){
					console.log('error');
				}
			});
		} else {
			$.ajax({
				url:"/change/address",
				type: "POST",
				datatype:"json",
				data: {
					uId:$.cookie('userId'),
					adId: adId,
					adAddress: $('.dress').val(), 
					adUser: $('.person').val(), 
					adPhone: $('.phoneNumber').val()
				},
				success:function(){
					console.log('修改成功');
					window.location.reload()
				},
				error:function(){
					console.log('error');
				}
			});
		}
	})
	
	
	/* 百度地图API功能 */
	var map = new BMap.Map("allmap");
	var point = new BMap.Point(121.66149, 29.890624);
	map.centerAndZoom(point, 16);
	map.addOverlay(new BMap.Marker(point));
	map.panBy(400, 200)
	map.enableScrollWheelZoom();
	// 创建地址解析器实例
	var myGeo = new BMap.Geocoder();
	// 将地址解析结果显示在地图上,并调整地图视野百度
	$('.dress').blur(function () {
	   myGeo.getPoint($('.dress').val(), function (point) {
	      if (point) {
	         map.centerAndZoom(point, 19);
			  map.clearOverlays();   //覆盖
	         map.addOverlay(new BMap.Marker(point));
	         var pointA = new BMap.Point(121.66149, 29.890624);  // 创建点坐标A--大渡口区
	         var pointB = new BMap.Point(point.lng, point.lat);  // 创建点坐标B--江北区
	         if (map.getDistance(pointA, pointB).toFixed(2) > 3000) {

	            console.log(1)
	            console.log('超出配送范围')
				 alert("超出配送范围!");

	            $('#corretDress').attr('disabled', true)
	         } else {
	            console.log(2)
	            $('#corretDress').attr('disabled', false)
	         }
	      } else {
	         alert("您选择地址没有解析到结果!");
	      }
	   }, '宁波市');
	})



	// 定义优惠券ID
	var couponId = 0;
	
	/* 优惠券下拉框 */
	$('.drop-down>span').on('click',function(){
		$(this).siblings('ul').slideDown(200);
	})
	
	$('.drop-down').mouseenter(function () {
	 $(this).css("background","#DDDDDD");
	}).mouseleave(function () {
	  // $(this).find('ul').stop(true,false).slideUp(150);
	  $(this).find('ul').delay(800).slideUp(150);
	  $(this).css("background","none");
	});
	
	$('.drop-down>ul').mouseenter(function () {
		$(this).stop(true,false).slideDown(200);
	})
	
	//鼠标移出事件.
	$('.drop-down>span').mousedown(function () {
	 $('.drop-down').css("background","#BBBBBB");
	}).mouseup(function () {
	 $('.drop-down').css("background","#DDDDDD");
	});
	
	$('.drop-down>ul>li').mouseenter(function(){
		$(this).css("background","#F5F5F5");
	}).mouseleave(function(){
		$(this).css("background","none");
	})
	
	/* 优惠券选择 */
	$('.drop-down>ul>li').on('click',function () {
		$('#coupon-selected').text($(this).text());
		console.log($(this).find('a')[0].attributes.value.value);
		couponId = $(this).find('a')[0].attributes.cnId.value;
		$('#coupon-amount').text((-1 * $(this).find('a')[0].attributes.value.value).toFixed(2));
		$(this).parent().stop(true,false).slideUp(100);
		$('body').totalAmount();
	})
	
	
	/* 积分选择 */
	//积分计算
	var deducRatio = 100;//抵扣比率
	var deducAmount = $('#cumulative-scores').text()/deducRatio; //抵扣价
	$('#scores-discount-txt>span').text(deducAmount);
	$('#scores-discount').text((deducAmount*-1).toFixed(2));
	
	$('#cumulative-scores-using').on('click',function(){
		if($('#cumulative-scores-using').prop('checked')==true){
			$('#scores-discount').text((deducAmount*-1).toFixed(2));//.toFixed(2) 显示两位小数
			$('#scores-discount-txt').hide();
		}else{
			$('#scores-discount').text('0.00');
			$('#scores-discount-txt').show();
		}
		$('body').totalAmount();
	});

	/* 配送权益选择 */
	$('#member-delivery-using').on('click',function () {
		$('body').totalAmount();
	})
	
	
	/* 返回购物车 */
	$('#back-to-cart').on('click',function () {
		window.open('mycart.html','_self');
	})
	
	/* 提交订单 */
	$('#submit-order').on('click',function () {
		/* 选中地址/买家留言/选择优惠券/是否使用积分/是否使用免配送权益 */

		if (defaultAdId) {

			$.ajax({
				url:"/create/order",
				type: "POST",
				datatype:"json",
				// async: false,
				data: {
					uId:$.cookie('userId'),
					adId: $('.checkbtn-selected')[0].attributes.value.value,
					integralChecked: $('#cumulative-scores-using')[0].checked,
					cnId: (couponId==0)? null : couponId,
					useMbTimes: $('#member-delivery-using')[0].checked,
					message: ($('#handledMsg')[0].value.length == 0)? null : $('#handledMsg')[0].value
				},
				beforeSend:function(){
					$("#loading").show();
				},
				success:function(data){
					console.log(data);
					$.cookie("outTradeNo",data);
					$.cookie("isV",0);

					if (data == 0) {
						window.open('successjump.html','_self');
					} else {
						window.open('payment','_self');
					}
				},
				complete:function(){
					$("#loading").hide();
					// window.open('payment','_self');
				},
				error:function(){
					console.log('error');
					$("#loading").hide();
				}
			});

		} else {
			//	无地址提示
			// $('#Floating-frame').text('请添加地址！！');
			$('#Floating-frame').css('opacity','0.8');
			$('#Floating-frame').show();
			$('#Floating-frame').stop(true,false).fadeOut(2000);
		}

	});
	
});

/* 留言框计数 */
function setLength(obj,maxlength,id){
	var num=maxlength-obj.value.length;
	var leng=id;
	if(num<0){
		num=0;
	}
	document.getElementById(leng).innerHTML=num+"/200";
}
/* 留言框高度自适应 */
//jQuery实现textarea高度根据内容自适应
$.fn.extend({
	txtaAutoHeight: function() {
		return this.each(function() {
			var $this = $(this);
			if (!$this.attr('initAttrH')) {
				$this.attr('initAttrH', $this.outerHeight());
			}
			setAutoHeight(this).on('input', function() {
				setAutoHeight(this);
			});
		});
 
		function setAutoHeight(elem) {
			var $obj = $(elem);
			return $obj.css({
				height: $obj.attr('initAttrH'),
				'overflow-y': 'hidden'
			}).height(elem.scrollHeight);
		}
	}
});


/* 总金额结算 */
$.fn.totalAmount = function () {
	var sum = 0;
	var totalPrice = 0;
	var svipDiscountRate = 0.95;
	for (var i = 0; i < $('.total').length; i++) {
		sum += parseFloat($('.total').eq(i).text());
	}
	// console.log(sum);

	//配送费
	var delivery = parseFloat($('#delivery-fee').text());
	var cmdFee = parseFloat($('#delivery-fee').attr('cmdfee'));
	console.log(delivery);
	// console.log(cmdFee);

	/*
	* 	如果是会员
	* 	若无免配送次数  则无input选择
	* 	若有免配送次数
	* 	1. 达到免配送费资格
	* 		点击免配送提示本次不消耗
	* 	2.未达到配送资格
	* 		点击免配送
	*
	* */

	if (sum >= cmdFee) {
		console.log("价格满足免配");
		$('#delivery-fee').text('0.00');
		$('#member-delivery-using').prop("checked",false);
	} else {
		if ($('#member-delivery-using').prop('checked') == true) {
			console.log("使用免配");
			$('#delivery-fee').text('0.00');
		} else {
			console.log("不免配");
			$('#delivery-fee').text(delivery.toFixed(2));
		}

	}

	var deliveryFee = parseFloat($('#delivery-fee').text());


	//优惠券
	var couponAmount = parseFloat($('#coupon-amount').text());
	// console.log(couponAmount);

	//积分折扣
	var scoresDiscount = parseFloat($('#scores-discount').text());
	// console.log(scoresDiscount);

	//会员折扣
	// var svipDiscount = (-1 * sum*(1-svipDiscountRate)).toFixed(2);
	// // console.log(svipDiscount);
	// $('#SVIP-discount').text(svipDiscount);

	//合计
	totalPrice = (sum + couponAmount + scoresDiscount + deliveryFee);
	// console.log(totalPrice);
	if (totalPrice < 0) {
		totalPrice = 0;
	}
	$('#totalPrice').text(totalPrice.toFixed(2));
	$('#sum-amount').text(totalPrice.toFixed(2));
};


/* 使页面无法后退 */
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