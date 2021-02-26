$(document).ready(function(){
    function getVipNews(){
            $.ajax({
                url: "/vip/vipHome",
                type: "post",
                dataType: "json",
                data:{UId:$.cookie('userId')},
                success: function(data){
                    $('.offerBox').html('')
                    let str ="";
                    for(i =0 ;i < data.bjfCommodityList.length;i++){ 
                        str += '<div class="one_offer">'+
                                    '<img src="'+data.bjfCommodityList[i].cmdImage+'" alt="">'+
                                    '<span class="offer_title">'+data.bjfCommodityList[i].cmdName+'</span>'+
                                    '<span style="color: #f80606;">会员价：'+data.bjfCommodityList[i].cmdPrice+' RMB</span>'+
                                '</div>'
    
                    }                    
                    $('.offerBox').append(str)
                    $('.allIcon').html('')
                    var right = "";
                    for(j =0 ;j < data.bjfMenberRightsList.length;j++){ 
                        right += '<div>'+
                                    '<img src="/images/17.png" class="img-circle" alt="">'+
                                    '<span>'+data.bjfMenberRightsList[j].mrContent+'</span>'+
                                 '</div>'
                    }
                    $('.allIcon').append(right)
                    var  vipBtn = ''
                    var  vipNew = ''
                    if(data.memberRecord == 0){
                        vipBtn = '<a class="vipBtnEnter" href="VipPay.html">开通</a>'
                    }else if(data.memberRecord == null ){
                        vipBtn = '<a class="vipBtnEnter" href="login.html">去登录</a>'
                    }
                    else if(data.menberRecord == 1 ||data.memberState == 1 ){
                        vipBtn = '<a class="vipBtnRenew" href="VipPay.html">续费</a>'
                        vipNew = '<div class="vipName">'+
                                    '<span style="font-size: 24px;">'+data.uaccount+'</span>'+
                                  '</div>'+
                                    '<div class="mbTimes">剩余配送次数：'+data.mbTimes+'</div>'+
                                    '<div class="mbEtime">'+data.mbEtime+'到期</div>'
                        $('.userHead').attr('src',data.uheadSculpture)
                     }else if(data.menberRecord == 1 ||data.memberState == 0 ){
                        vipBtn = '<a class="vipBtnRenew" href="VipPay.html">续费</a>'
                        vipNew = '<div class="vipName">'+
                                    '<span style="font-size: 24px;">'+data.uaccount+'</span>'+
                                 '</div>'+
                                   '<div class="mbTimes">您的会员已过期，续费立即称为超级vip</div>'
                        $('.userHead').attr('src',data.uheadSculpture)
                     }
                     $('.vipNewsBox').append(vipNew)
                     $('.vipBtnGroup').append(vipBtn)
                },
            })
        }
    getVipNews()

})