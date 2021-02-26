    // 商品搜索
   function ajax() {
    $.get({
      url: 'http://192.168.0.125:8080/demo/commodity/elasticsearch',
      data: {'key': $('.search-text').val()},
      xhrFields: {
        'Access-Control-Allow-Origin': '*',
      },

      success: function (data) {
        // console.log('data：' + JSON.stringify(data))
        //储存到游览器端localstorage
        window.localStorage.setItem('product', JSON.stringify(data))
      }
    })
  }
  // 点击搜索跳转到商品搜索页面
  $(function () {
    $('#btn1').click(function () {
      if ($('.search-text').val() == '') {
        alert('搜索内容不可为空')
      } else {
        // 给超链接添加地址
        $('#checka').attr('href', 'SearchProduct.html')
        // 超链接同时触发点击事件
        $('#checka')[0].click()
      }
    })
  })
   $(function () {
  // 左侧导航栏
  $.ajax({
    url: '/homePage/getTree',
    type: 'GET',
    datatype: 'json',
    success: function (data) {
      let str = '';
      for (let i = 0; i < data.length; i++) {
        let str1 = '';
        let cgid = data[i].cgId;
        for (let t = 0; t < data[i].next.length; t++) {
          let str2 = '';
          for (let j = 0; j < data[i].next[t].next.length; j++) {
            let cgid2 = data[i].next[t].next[j].cgId
            str2 += `<li ><a  class="navType" data-id="${cgid2}">${data[i].next[t].next[j].cgName} </a></li>`
          };
          str1 += `<ul class="navType-box">
                    <li>${data[i].next[t].cgName}</li>
                    <ul>${str2}</ul>
                  </ul>`
        };
        str += `<li class="nav-list">
                  <a class="navType" data-id="${cgid}">${data[i].cgName}</a>
                  <ul class="hide-box">${str1}</ul>
                </li>`
      };
      $('.nav-item').html(str);
      $('.navType').on('click', function () {
        let Id = this.dataset.id
        console.log(Id)
        let url = '../menu.html?id=' + Id
        $('.navType').attr('href', url)
      });
      // 二级导航栏显示与隐藏
      function showFunction(index, dpy,tp) {
        $('.nav-list').eq(index).find('.hide-box').css('display', dpy)
        $('.nav-list').eq(index).find('.hide-box').stop().animate({width:tp + 'px'}, 'fast')
      }
      let  index = 0;
      $('.nav-list').hover(function () {
        index = $(this).index()
        showFunction(index, 'block',260)
      }, function () {
        showFunction(index, 'none',0)
      })
    }
  })

  //轮播图
  $.ajax({
    url: 'homePage/slideShow',
    type: 'GET',
    datatype: 'json',
    success: function (data) {
      let project = ''
      for (let i = 0; i < data.length; i++) {
        let Img = data[i].slideImage
        project = `<div class="banner banner1 swiper-slide"  style='background-image: url(${data[i].slideImage}); background-size: 100% 100%;'></div>`
        $('#banner-img').append(project)
      }
      let swiper = new Swiper('.swiper-container', {
        spaceBetween: 0,
        centeredSlides: true,
        autoplay: {
          delay: 4000,
          disableOnInteraction: false,
        },
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev',
        },
        pagination: {
          el: '.swiper-pagination',
          bulletActiveClass: 'pagination-dot',
        },
      })
    }
  })

})
// 商品数据传输
$(function () {
  $.ajax({
    //json地址的值
    url: '/homePage/shouzhan',
    type: 'GET',
    datatype: 'json',
    success: function (data) {
      let project = ''
      //循环遍历data的值
      for (const a of data) {
        //project存储遍历的值 data-id一种传参方式
        project = `<div class="productdiv"><a class="product" data-id=${a.cmdId}>
                    <img src="${a.cmdImage}">
                      <div class="text">
                        <span class="textb">${a.cmdName}</span><br>
                        <span class="texta">￥${a.cmdPrice}</span>
                      </div> 
                  </a></div>`
        //字符串拼接追加到div中
        $('.middle').append(project)
      }
      //点击获取商品id传值给商品详情页
      $('.product').click(function () {
        //data-id取值方式
        let id = this.dataset.id
        console.log(id)
        //往商品详情地址传商品的id
        let url = 'productDetail.html?id=' + id
        $('.product').attr('href', url)
      })
    }
  })
})
//今日推荐图片
$(function () {
  $.ajax({
    url: '/homePage/recommendImage',
    type: 'GET',
    datatype: 'json',
    success: function (data) {
      let project = ''
      for (const a of data) {
        project = `<a data-id=${a.cmdId}>
                            <img src="${a.cmdImage}"></a>`
        $('.tuijian-box-2').append(project)
      }
      $('.tuijian-box-2 a').click(function () {
        //data-id取值方式
        let id = this.dataset.id
        console.log(id)
        let url = 'productDetail.html?id=' + id
        $('.tuijian-box-2 a').attr('href', url)
      })
    }
  })
})
// 商品图片推荐（大小图）
$(function () {
  $.ajax({
    url: '/homePage/hotImage',
    type: 'GET',
    datatype: 'json',
    success: function (data) {
      let project = ''
      for (const a of data) {
        project = `<a data-id="${a.cmdId}"><img src="${a.cmdImage}"></a>`
        $('.product-image').append(project)
      }
      $('.product-image a').click(function () {
        //data-id取值方式
        let id = this.dataset.id
        console.log(id)
        let url = 'productDetail.html?id=' + id
        $('.product-image a').attr('href', url)
      })
    }
  })
})
// $(function () {
//     $.ajax({
//         url: "http://192.168.0.109:8888/vipHome",
//         type: "GET",
//         datatype: "json",
//         success: function (data) {
//            console.log(data);
//         }
//     })
// })


