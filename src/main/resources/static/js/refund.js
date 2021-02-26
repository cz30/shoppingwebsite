$(function(){
    var odDelid = window.location.href.split("=")[1];
    var oiId = window.location.href.split("=")[2];
    console.log(odDelid)
    console.log(oiId)

    $('.refundId').val(odDelid);
    $('.oiId').val(oiId);
    $.ajax({
        url: "/order/showBack",
        type: "GET",
        datatype: "json",
        async:false,
        data:{  "odDelid" : odDelid,
            "oiId" : oiId},
        success: function (data) {
            console.log(data);
            $('.order-id').text(data.odDelid);//订单编号
            showGoods(data);
        }
    });

    function showGoods(indentData){
        let str1 = ''
        if(indentData.oiStatus === 6){
            str1 =`<span>退款退货</span>`
        }else if(indentData.oiStatus === 0){
            str1 =`<span>退款中</span>`
        }else if(indentData.oiStatus === 2){
            str1 =`<span>退款失败</span>`
        }
        let str =`<div class="indent-list">
                    <div class="same-flex indent-list-box">
                        <div class="commodity-banner">
                            <div class="same-indent same-flex">
                                <div class="commodity-list same-flex">
                                    <img class="commodity-img" src=${indentData.oiImage} alt="">
                                    <div class="commodity-content">
                                        <div>${indentData.oiName}</div>
                                        <div>${indentData.oiContent.substring(1,indentData.oiContent.length-1)}</div>
                                    </div>
                                </div>
                                <div class="same-style unit-price" > ${indentData.oiPrice}</div>
                                <div class="same-style quantity"> ${indentData.oiNum}</div>
                                <div class="same-style quantity">${str1}</div>
                            </div>
                        </div>
                        <div class="same-flex sa">
                            <div class="same-style gross-amount">${indentData.backMoney}</div>
                        </div>
                    </div>
                </div>`
        $('#content').html(str);
    };
    // 退款退货框上传图片
    let num = 0;
    $("#image").click(function(){
        $("#uploadfile").click();
    });
    $("#uploadfile").change(function(){
        let files = this.files;    //获取文件信息

        if(files.length<4){
            $.each(files,function(key,value){
                //每次都只会遍历一个图片数据
                let reader = new FileReader();  //调用FileReader
                reader.onload = function(evt){   //读取操作完成时触发。
                    if(num<3){
                        $("#image").before('<img src="" style="width:50px;height:50px;margin-right:6px;"/>').
                        siblings().eq(num).attr('src',evt.target.result);
                        num++;
                    };
                    if(num == 3){
                        $('#image').hide()
                    }
                }
                reader.readAsDataURL(value); //将文件读取为 DataURL(base64)
            })
        }else{
            alert("最多上传三张图片,请选择的图片不要大于四张！");
        }
    });

    // 提交退款理由和照片
    $('.evaluate-btn').click(function(){
        layer.confirm('确认提交吗？', {
            btn: ['确认','取消'] //按钮
        }, function(){
            $('#refund-form').ajaxSubmit(function(data){
                console.log(data)
                if(data == 1){
                    layer.msg('提交成功', {icon: 1});
                    window.top.location.href='indent.html';
                }else{
                    layer.msg('提交失败', {icon: 5});
                }
            });
        });
    })

    // 退货详情进度条
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

