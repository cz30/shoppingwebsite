$(function(){
    var odDelid = window.location.href.split("=")[1];
    var status = window.location.href.split("=")[2];
    $.ajax({
        url: "/order/showItem",
        type: "post",
        datatype: "json",
        data:{
            "odDelid" : odDelid,
            "status" : status
        },
        success: function (data) {
            console.log(data)
            showGoods(data);
            //订单状态走势图
            $('.orderTime').text(data[0].odTimeStr);
            $('.payTime').text((data[0].odPayTimeStr === null) ? '' : data[0].odPayTimeStr);
            $('.accomplishTime').text((data[0].odModifiedTimeStr === null) ? '' : data[0].odModifiedTimeStr);
            barFunction();
            $('.state').text(function(){
                if(data[0].odStatus == 0){
                    return "未付款"
                }else if(data[0].odStatus == 1){
                    return "已付款"
                }else if(data[0].odStatus == 2){
                    return "已完成"
                }else if(data[0].odStatus == 9){
                    return "退款中"
                }
            });
            $('.courier').append(`<span>配送员:${data[0].odRecvName}</span><span>电话:${data[0].odRevcPhone}</span>`);
            $('.site').text(data[0].odRecvAddress);
            $('.number').text(data[0].odDelid);
            $('.order-time').text(data[0].odTimeStr);
            $('.pay-time').text((data[0].odPayTimeStr === null) ? '还未付款' : data[0].odPayTimeStr);
            $('.pay-way').text(function(){
                if(data[0].bjfPayment === null){
                    return "还未付款"
                }else{
                    if(data[0].bjfPayment.payWay === 0){
                        return "支付宝"
                    }else if(data[0].bjfPayment.payWay === 1){
                        return "微信"
                    }
                }
            });
        }
    });

    function showGoods(indentData){
        let str = '';
        let str2 = '';

        for(let i = 0;i < indentData.length; i++){
            let str1 = '';
            let str3 = '';
            let str4 = '';
            for(let j = 0;j < indentData[i].bjfOrderItems.length;j++){
                if((indentData[i].odStatus === 1) || (indentData[i].odStatus === 9)){
                    if(indentData[i].bjfOrderItems[j].oiSupport === 1){
                        if(indentData[i].bjfOrderItems[j].oiStatus === 6){
                            str3 = `<div class="consignee sales-return"  data-odDelid=${indentData[i].odDelid} 
                            data-oiId=${indentData[i].bjfOrderItems[j].oiId}>退款/退货</div>`;
                        }else if(indentData[i].bjfOrderItems[j].oiStatus === 0){
                            str3 = `<div class="consignee-box">
                                        <div>退款中</div>
                                        <div class="consignee cancel-return"  data-odDelid=${indentData[i].odDelid} 
                                        data-oiId=${indentData[i].bjfOrderItems[j].oiId}>取消退款</div>
                                    </div>`
                        }else if(indentData[i].bjfOrderItems[j].oiStatus === 1){
                            str3 = `<div>退款成功</div>`
                        }else if(indentData[i].bjfOrderItems[j].oiStatus === 2){
                            str3 = `<div class="consignee-box">
                                        <div>退款失败</div>
                                        <div class="consignee sales-return"  data-odDelid=${indentData[i].odDelid}
                                         data-oiId=${indentData[i].bjfOrderItems[j].oiId}>再次退款</div>
                                    </div>`
                        }
                    }else if(indentData[i].bjfOrderItems[j].oiSupport === 0){
                        str3 = `<div>不可退商品</div>`
                    }
                }else if(indentData[i].odStatus === 2){
                    if(indentData[i].bjfOrderItems[j].oiStatus === 3){
                        str3 = `<div class="evaluate"  data-odDelid=${indentData[i].odDelid} 
                        data-oiId=${indentData[i].bjfOrderItems[j].oiId}>未评价</div>`
                    }else if(indentData[i].bjfOrderItems[j].oiStatus === 4){
                        str3 = `<div class="evaluate"  data-odDelid=${indentData[i].odDelid} 
                        data-oiId=${indentData[i].bjfOrderItems[j].oiId}>追加评价</div>`
                    }else if(indentData[i].bjfOrderItems[j].oiStatus === 5){
                        str3 = `<div>已评价</div>`
                    }
                };
                //商品
                str1 +=`<div class="same-indent same-flex">
                            <div class="commodity-list same-flex">
                                <img class="commodity-img" src="${indentData[i].bjfOrderItems[j].oiImage}" alt="">
                                <div class="commodity-content">
                                     <div>${indentData[i].bjfOrderItems[j].oiName}</div>
                                     <div>${indentData[i].bjfOrderItems[j].oiContent.
                substring(1,indentData[i].bjfOrderItems[j].oiContent.length-1)}</div>
                                </div>
                            </div>
                            <div class="same-style unit-price">${indentData[i].bjfOrderItems[j].oiPrice}</div>
                            <div class="same-style quantity">${indentData[i].bjfOrderItems[j].oiNum}</div>
                            <div class="same-style refund">${str3}</div>
                        </div>`
            };

            if(indentData[i].odStatus === 0){     //未付款
                str2 = `<span>待付款</span>`//订单状态

                str4 = `<div class="handle"  data-odDelid=${indentData[i].odDelid}>立即付款</div>
                            <div class="cancel"  data-odDelid=${indentData[i].odDelid}>取消订单</div>`

            }else if(indentData[i].odStatus === 1){       //已付款
                str2 = `<span>待收货</span>` //订单状态

                str4 = `<div class="handle confirm-receipt" data-odDelid=${indentData[i].odDelid}>确认收货</div>`   //交易操作

            }else if(indentData[i].odStatus === 2){       //2已完成
                str2 = `<span>已完成</span>` //订单状态
            }

            str +=`<div class="indent-list" data-odDelid=${indentData[i].odDelid}>
                        <div class="indent-detail-box same-flex">
                            <div class="same-flex">
                                <div class="indent-time"> ${indentData[i].odTimeStr}</div>
                                <div>
                                    <span>订单编号:</span>
                                    <span> ${indentData[i].odDelid}</span>   
                                </div>
                            </div>
                          
                        </div>
                        <div class="same-flex indent-list-box">
                            <div class="commodity-banner">${str1}</div>
                            <div class="same-flex sa">
                                <div class="same-style gross-amount">
                                        <div>￥ ${indentData[i].odTotalAmount}</div>
                                        <div>(含运费:￥ ${indentData[i].mcDpfee})</div>
                                </div>
                                
                                <div class="same-style state">${str2}</div>
                                <div class="same-style operation-btn">${str4}</div>

                            </div>
                        </div>
                    </div>`
        };
        $('#content').html(str);


        // 事件代理获取当前点击按钮所对应的订单数据
        var odDelid ="";//订单编号
        var oiId = "";//商品ID
        $('.indent-list').click(function(e){
            var event = e || window.event;  // 兼容性处理
            console.log(event.target.getAttribute('data-odDelid'),"订单编号")
            odDelid = event.target.getAttribute('data-odDelid');

            oiId = event.target.getAttribute('data-oiId');
            console.log(oiId,"商品id")
        });
        //点击退款退货按钮跳转退款退货页面
        $(".sales-return").click(function () {
            setTimeout(function(){
                window.top.location.href = 'refund.html?id='+ odDelid +'=' + oiId;
            },0);
        });
        //取消退货事件
        $(".cancel-return").click(function (){
            var odDelid = odDelid;
            var oiId = oiId;
            layer.confirm('取消退货吗？', {
                btn: ['确认','取消'] //按钮
            }, function(){
                $.ajax({
                    url: "/order/notBack",
                    type: "GET",
                    datatype: "json",
                    data:{"odDelid" : odDelid,
                        "oiId":oiId},

                    success: function (data) {
                        console.log(data)
                        if(data == 1){
                            layer.msg('取消退货成功', {icon: 1});
                        }else {
                            layer.msg('取消退货失败', {icon: 5});
                        }
                    }
                })
            });
        });

        //确认收货事件
        $(".confirm-receipt").click(function () {
            var odDelid = odDelid;
            layer.confirm('确认已收到货吗？', {
                btn: ['确认','取消'] //按钮
            }, function(){
                $.ajax({
                    url: "/order/operation",
                    type: "GET",
                    datatype: "json",
                    data:{"odDelid" : odDelid,
                        "id" : 2},
                    success: function (data) {
                        console.log(data)
                        if(data == 1){
                            layer.msg('确认收货成功', {icon: 1});
                        }else {
                            layer.msg('确认收货失败', {icon: 5});
                        }
                    }
                })
            });
        });
        //取消订单事件
        $(".cancel").click(function () {
            var odDelid = odDelid;
            layer.confirm('您确定要取消当前订单吗???', {
                btn: ['确认','取消'] //按钮
            }, function(){
                $.ajax({
                    url: "/order/operation",
                    type: "GET",
                    datatype: "json",
                    data:{"odDelid" : odDelid,
                        "id" : 3},
                    success: function (data) {
                        console.log(data)
                        if(data == 1){
                            layer.msg('取消成功', {icon: 1});
                        }else{
                            layer.msg('取消失败', {icon: 5});
                        }
                    }
                })
            });
        });
    };

    function barFunction(){
        var obj1 = $('.details-list').find('.details-time').eq(0).text();
        var obj2 = $('.details-list').find('.details-time').eq(1).text();
        var obj3 = $('.details-list').find('.details-time').eq(2).text();
        function activeFunction(i){
            $('.details-list').find('.details-text-list1').eq(i).addClass('details-text-list1-1');
            $('.details-list').find('.details-text-list2').eq(i).addClass('details-text-list2-1');
            $('.details-list').find('.details-border').eq(i).addClass('details-border-1');
        };
        if(obj1 != '' && obj2 == ''){
            activeFunction(0);
        }else if(obj1 != '' &&  obj2 != ''  && obj3 == ''){
            activeFunction(0);
            activeFunction(1);
        }else if(obj1 != '' &&  obj2 != '' && obj3 != ''){
            activeFunction(0);
            activeFunction(1);
            activeFunction(2);
        }
    };
})
