$(function(){
    // 判断用户是否登录
    console.log($.cookie('userId')); //用户id
    if ($.cookie('userId') !== undefined) {
      $('.go_login').empty()
      console.log('用户已登录')
      $('#person').click(function () {
        window.location.href = 'person.html'
      });
      // 跳转我的订单页yz
      $('#order').click(function(){
        window.location.href='indent.html'
      })
      $('#mycart').click(function () {
          window.location.href='mycart.html'
      })
    } else {
      console.log('用户未登录')
      $('.exit_login').empty()
      $('.rightText a').click(function () {
        window.location.href = 'Login.html'
      })
    }
    //退出登录
    $('.exit_login').click(function () {
      $.cookie('userId', '', {expires: -1})
      window.location.href = 'Login.html'
    })
})
