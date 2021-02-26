//搜索页面显示商品
$(function(){
    //从游览器端的localstorage里取数据
    // console.log((window.localStorage.getItem('product')))
    if ((window.localStorage.getItem('product') )==='[]'){
       alert("未搜索到任何商品");
       //跳转到前一个页面并且刷新
        window.location.href=document.referrer;
    }
    //转换成json的格式
    let Data=JSON.parse(window.localStorage.getItem('product'));
    let project = "";
    // 遍历改分类的json
    for (const a of Data) {
        let item= a;
        project += `<a class="product" data-id="${item.cmdId}">
                       <img src="${item.bjfCommodityDetails.cmaPicture}">
                        <div class="text">
                        <span class="textb">${item.cmdName}</span>
                        <br><span class="texta">￥${item.cmdPrice}</span></div>
                        
                    </a>`;
            $(".middle").empty().append(project);
    }
    //点击获取商品id传值给商品详情页
    $('.product').click(function () {
        //data-id取值方式
        var id = this.dataset.id
        console.log(id)
        //往商品详情地址传商品的id
        var url = 'productDetail.html?id=' + id
        console.log(url);
        $('.product').attr('href', url)
    })
})
