$(function(){
    //封装商品列表
    function onPageFunction(data){
        let project = "";
        for (const a of data.content) {
            project += `<div class="product">
                            <img src="${a.cmdImage}">
                            <div class="text">
                            <span class="texta">${a.cmdPrice}</span>
                            <br><span class="textb">${a.cmdName}</span></div>
                            <button>加入购物车</button>
                            <button>立即购买</button>
                        </div>`;
            $(".middle").empty().append(project);
        };
    };

    var cgid = window.location.href.split("=")[1];
    var page1 = "";

    $.ajax({
        url: "/BjfCategory/findPage",
        type: "GET",
        datatype:"json",
        data:{
            "cgId" : cgid,
            // "pageNum": page1
        },
        success: function (data) {
            onPageFunction(data);
            $('#pageLimit').bootstrapPaginator({

                totalPages: data.totalPages,//共有多少页。
                currentPage: data.pageNum,//当前的请求页面。
                size:"normal",//分页符的大小
                bootstrapMajorVersion: 3,//bootstrap的版本要求。
                alignment:"left",
                numberOfPages:data.pageSize,//一页列出多少数据。

                itemTexts: function (type, page, current) {//如下的代码是将页眉显示的中文显示我们自定义的中文。
                    switch (type) {
                        case "first": return "首页";
                        case "prev": return "上一页";
                        case "next": return "下一页";
                        case "last": return "末页";
                        case "page": return page;
                    }
                },

                onPageClicked: function(event, originalEvent, type,page){
                    console.log("event:"+event);
                    console.log("originalEvent:"+originalEvent);
                    console.log("type:"+type);
                    console.log("page:"+page);
                    page1 = page;
                    $.ajax({
                        url: "/BjfCategory/findPage",
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
