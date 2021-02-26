$(function(){
    //封装商品列表
    function onPageFunction(data){
        let project = "";
        for (const a of data.content) {
            project += `<a class="product" data-id="${a.cmdId}">
                            <img src="${a.cmdImage}" alt="">
                            <div class="text">
                                <span class="textb">${a.cmdName}</span>
                                <br><span class="texta">￥${a.cmdPrice}</span>
                            </div>
                        </a>`;
            $(".middle").empty().append(project);
        }
    }



    let cgid = window.location.href.split("=")[1];
    $.ajax({
        url: "homePage/findPageGetTree",
        type: "GET",
        datatype:"json",
        data:{ "cgId" : cgid},
        success: function (data) {
            console.log(data)
            onPageFunction(data);
            $('.product').click(function () {
                //data-id取值方式
                var id = this.dataset.id
                console.log(id,1)
                //往商品详情地址传商品的id
                var url = 'productDetail.html?id=' + id
                console.log(url);
                $('.product').attr('href', url)
            })


            $('#pageLimit').bootstrapPaginator({
                totalPages: data.totalPages,//共有多少页。
                currentPage: data.pageNum,//当前的请求页面。
                size:"normal",//分页符的大小
                bootstrapMajorVersion: 3,//bootstrap的版本要求。
                alignment:"left",
                numberOfPages:data.pageSize,//一页列出多少数据。

                itemTexts: function (type, page) {//如下的代码是将页眉显示的中文显示我们自定义的中文。
                    switch (type) {
                        case "first": return "首页";
                        case "prev": return "上一页";
                        case "next": return "下一页";
                        case "last": return "末页";
                        case "page": return page;
                    }
                },

                onPageClicked: function(event, originalEvent, type,page){
                    page1 = page;
                    $.ajax({
                        url: "homePage/findPageGetTree",
                        type: "GET",
                        datatype:"json",
                        data:{"cgId" : cgid,
                            "pageNum":page1},
                        success: function (data) {
                            onPageFunction(data);
                        }
                    });
                }
            });
        }
    });

})
