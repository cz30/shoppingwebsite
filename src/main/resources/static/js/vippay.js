//获取会员套餐
$(function () {
        $.ajax({
            url: "/vip/bjfMemberPay",
            type: "GET",
            datatype: "json",
            success: function (data) {
                console.log(data);
                let project = "";
                //循环遍历data的值
                for (var i = 0; i < data.length; i++) {
                    //project存储遍历的值 data-id一种传参方式
                    project = `<div class="pay-box" data-id="${i + 1}" id="pay${i}">
                                            <h3>商城${data[i].mbpMonth}月VIP</h3>
                                            <div class="pay-box-price">
                                                <span class="price">${data[i].mbpMoney}</span>
                                                <span class="time">元/${data[i].mbpMonth}月</span>
                                            </div>`;
                    //字符串拼接追加到div中
                    $(".pay").append(project);
                }
                let num =1;
                $("#pay0").toggleClass("charge-click");
                $(".pay-box").click(function (event) {
                    $(".pay-box").removeClass("charge-click");
                    $(this).toggleClass("charge-click");
                    //获取套餐的id
                    num = event.currentTarget.getAttribute("data-id");
                });
                // 选中支付套餐框
                // 点击支付跳转到支付页面
                $(".submit").click(function () {
                    $.ajax({
                        url: "/vip/vipPay",
                        type: "GET",
                        data: {"mbpId":num,"UId":$.cookie('userId')},
                        success: function (data) {
                            console.log(data);
                            $.cookie('VipOdDelid',data,{expires: 1});
                            $.cookie('isV',1);
                            window.location.href='payment.html';
                        }
                    })
                });
            }
        })


});
// 选中支付套餐框
$(function () {


});
