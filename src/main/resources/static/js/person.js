$(document).ready(function () {
   //上传个人头像
   $(function () {
      $("#image").click(function () {
         $("#uploadfile").click();
      })
      $("#uploadfile").change(function (e) {
         var exSrc = e.target;
         exGetImg(exSrc);
      })
      var exGetImg = function (extag) {
         var file = extag.files[0];                               //选中文件的第一个文件（当成固定的格式）
         var readers = new FileReader();                         //新建文件阅读对象，来转意路径
         readers.readAsDataURL(file);                           //将读取的文件路径，将路径转换为url类型    
         readers.onload = function () {                         //转换之后调用onload（）方法
            var imgsSrc = this.result;                      //图片地址读出来之后result结果为 DataURL  
            $("#image").attr('src', imgsSrc)                //生成一个图片标签，路径和为表单的内容（即路径）
            //将这个图片标签插入到指定位置,照片显示。                                         
         }
      }
   })

   //切换页面的点击事件方法      
   $(".sameclass").click(function () {
      $(".sameclass").removeClass("ativeclass");
      $(this).addClass("ativeclass");
      if ($(".ativeclass").text() == '收货地址') {
         $(".person_center").css('display', 'none')
         $(".address").css('display', 'block')
         $(".coupon").css('display', 'none')
         $(".accountSet").css('display', 'none')
      } else if ($(".ativeclass").text() == '个人首页') {
         $(".person_center").css('display', 'block')
         $(".address").css('display', 'none')
         $(".coupon").css('display', 'none')
         $(".accountSet").css('display', 'none')
         show_myself()
      } else if ($(".ativeclass").text() == '账号设置') {
         $(".person_center").css('display', 'none')
         $(".address").css('display', 'none')
         $(".coupon").css('display', 'none')
         $(".accountSet").css('display', 'block')
      } else if ($(".ativeclass").text() == '购物车') {
        window.location.href="mycart.html"
      } else {
         $(".person_center").css('display', 'none')
         $(".address").css('display', 'none')
         $(".coupon").css('display', 'block')
         $(".accountSet").css('display', 'none')

      }
   })

   //退出登陆按钮事件   
   $('#logOut').click(function () {
      layer.confirm('是否要退出登陆', function (index) {
         $.cookie('userId', '', {expires: -1})
         window.location.href = "login.html";
      });
   })

   //点击修改按钮事件
   $('.clickCorret').click(function () {
      $('.showPerson').hide()
      $('.corretPerson').show()
   })
   var uId = $.cookie('userId')
   var defaultAdId = 0 //默认地址ID
   $('#userId').val(uId)
   var person_data ={}
   //渲染页面，获取个人信息数据
   function show_myself() {
      $.ajax({
         url: "/user/getUser",
         type: "post",
         data: { "uId": uId },
         dataType: "json",
         success: function (data) {
            person_data = data
            defaultAdId = data.adId
            $('#person_headImg').attr('src', data.uheadSculpture);
            $('#left_img').attr('src', data.uheadSculpture);
            $('#image').attr('src', data.uheadSculpture);
            $("#person_name").text(data.uaccount);
            $("#big_name").text(data.uaccount);
            $("#name").val(data.uaccount);
            $("#person_userName").text(data.uusername);
            $("#person_birth").text(data.suBirth);
            $("#person_sex").text(data.usex);
            $("#person_integral").text(data.uintegral);
            $("#person_phone").text(data.uphoneNumber);
            $("#person_email").text(data.uemail);
            $("#birth").val(data.suBirth);
            $(".star_phone").text(data.uphoneNumber.replace(/^(\d{3})\d{4}(\d+)/, "$1****$2"))
            if (data.usex === '男') {
               $("#man").attr("checked", "checked")
            } else {
               $("#woman").attr("checked", "checked")
            }
         },
         error: function () {
            layer.msg('获取失败');
         }
      })
   }
   show_myself()

   //取消修改信息
   $('.cancle_buttom').click(function () {
      $('.corretPerson').hide()
      $('.showPerson').show()
   })

   //时间选择器的方法
   $(function () {
      $('#datetimepicker1').datetimepicker({
         format: 'YYYY-MM-DD',
         locale: moment.locale('zh-cn'),
         maxDate: new Date(),
      });
   });

   //修改个人首页信息确认修改按钮的点击事件
   $('.confirm_buttom').click(function () {
      $('#personBirth').val($('#year').val() + '-' + $('#month').val() + '-' + $('#day').val())
      layer.confirm('您确认要修改吗？', {
         btn: ['确认', '取消'] //按钮
      }, function () {
         $('#person_form').ajaxSubmit(function (data) {
            if (data == '200') {
               layer.msg('修改成功', { icon: 1 });
               $('.corretPerson').hide()
               $('.showPerson').show()
               show_myself()
            }
            else {
               layer.msg('修改失败', { icon: 5 });
            }
         })
      });
      // });
      return false;
   })

   //渲染页面，获取优惠券数据
   $('#getCoupon').click(function () {
      $.ajax({
         url: "/user/getUserCouPon",
         type: "post",
         data: { "uId": uId },
         dataType: "json",
         success: function (data) {
            var str = "";
            $('.coupon_bottom_box').html('')
            for (i = 0; i < data.length; i++) {
               str += '<div class="coupon_box coupon-wave-left ' + (data[i].cnStatus === 1 ? ' coupon-gray' : ' coupon-yellow') + '">' +
                  ' <div class="coupon-info coupon-hole coupon-info-right-dashed">' +
                  '<div class="coupon-price">&yen;' + data[i].cnValue + '</div>' +
                  '<div class="coupon-expiry-date">有效期：' + data[i].cnUsetime + '-' + data[i].cnExpire + '</div>' +
                  '</div>' +
                  '<div class="coupon-get coupon-get-already">' + (data[i].cnStatus === 1 ? '已使用' : data[i].cnName) + '</div>' +
                  '</div> '
            }
            $('.coupon_bottom_box').append(str)
         },
         error: function () {
            layer.msg('获取失败');
         }
      })
   })

   var adId = ''
   //渲染页面，获取收货地址数据
   function getAddress() {
      $.ajax({
         url: "/address/getAddress",
         type: "post",
         data: { "uId": uId },
         dataType: "json",
         success: function (data) {
            var str = "";
            $('.address_bottom_box').html('')
            for (i = 0; i < data.length; i++) {
               if(defaultAdId === data[i].adId){
                  str += '<div class="address-news">' +
                      '<div class="news" >' +
                      '<div>收货人：' + data[i].adUser + '</div>' +
                      '<div>电话：' + data[i].adPhone + '</div>' +
                      '<div>地址：' + data[i].adAddress + '</div>' +
                      '</div>' +
                      '<div style="color:red;font-size: 18px" >默认地址</div>'+
                      '<div  class="operate">' +
                      '<div   data-adId=' + data[i].adId + ' data-toggle="modal" data-target="#corretDressModal" class="corretBtn">修改</div>' +
                      '<div   data-adId=' + data[i].adId + ' class="deleteAddress">删除</div>' +
                      '</div>' +
                      '</div>'
               }
            }
            for (j = 0; j < data.length; j++) {
               if(defaultAdId != data[j].adId){
                  str += '<div class="address-news">' +
                      '<div class="news" >' +
                      '<div>收货人：' + data[j].adUser + '</div>' +
                      '<div>电话：' + data[j].adPhone + '</div>' +
                      '<div>地址：' + data[j].adAddress + '</div>' +
                      '</div>' +
                      '<button data-adId=' + data[j].adId + ' class="setDefalut" >设为默认</button>'+
                      '<div  class="operate">' +
                      '<div   data-adId=' + data[j].adId + ' data-toggle="modal" data-target="#corretDressModal" class="corretBtn">修改</div>' +
                      '<div   data-adId=' + data[j].adId + ' class="deleteAddress">删除</div>' +
                      '</div>' +
                      '</div>'
               }
            }
            $('.address_bottom_box').append(str)
            //删除地址
            $(".deleteAddress").on('click', function (e) {
               var event = e || window.event;
               adId = event.target.getAttribute('data-adId')-0
               var type = false
               if(defaultAdId === adId){
                  type = true
               }
               $.ajax({
                  url: "/address/deleteAddress",
                  type: "post",
                  data: { uId:uId, adId: adId,type:type},
                  dataType: "json",
                  success: function (data) {
                     if (data == 500) {
                        layer.msg('删除失败');
                     } else {
                        layer.msg('删除成功');
                        getAddress()
                     }
                  },
               })
            })
            //设为默认
            $(".setDefalut").on('click', function (e) {
               var event = e || window.event;
               let id = event.target.getAttribute('data-adId')
               $.ajax({
                  url: "/address/setDefaultAddres",
                  type: "post",
                  data: { uId:uId, adId: id },
                  dataType: "json",
                  success: function (data) {
                     if (data == 500) {
                        layer.msg('设置失败');
                     } else {
                        layer.msg('设置成功');
                        show_myself()
                        getAddress()
                     }
                  },
               })
            })
            //点击修改获取地址Id
            $('.corretBtn').on('click', function (e) {
               var event = e || window.event;
               adId = event.target.getAttribute('data-adId')
            })
            //点击修改获取地址信息
            $('.address_bottom_box').on('click', '.address-news', function () {
               var i = $(this).index()
               $('#staticBackdropLabel').text('修改地址')
               $('.btn-primary').text('修改')
               $('.dress').val(data[i].adAddress)
               $('.person').val(data[i].adUser)
               $('.phoneNumber').val(data[i].adPhone)
            })
         },
         error: function () {
            layer.msg('获取失败');
         }
      })
   }
   //点击收货地址获取收货地址数据
   $('#getAddress').click(function () {
      getAddress()
   })

   //添加地址按钮事件
   $('#addbtn').click(function () {
      $('#staticBackdropLabel').text('添加地址')
      $('.btn-primary').text('添加')
      $('.dress').val('')
      $('.person').val('')
      $('.phoneNumber').val('')
      adId = ''
   })
   //修改地址按钮事件
   $('#corretDress').click(function (e) {
      if ($('#corretDress').text() == '添加') {
         $.ajax({
            url: "/address/insertOrUpData",
            type: "post",
            data: { uId: uId, adAddress: $('.dress').val(), adUser: $('.person').val(), adPhone: $('.phoneNumber').val() },
            dataType: "json",
            success: function (data) {
               if (data == 500) {
                  layer.msg('添加失败');
               } else {
                  layer.msg('添加成功');
                  getAddress()
                  $("#corretDressModal").modal('hide');  //手动关闭
               }
            },
         })
      } else {
         $.ajax({
            url: "/address/insertOrUpData",
            type: "post",
            data: { uId: uId, adId: adId, adAddress: $('.dress').val(), adUser: $('.person').val(), adPhone: $('.phoneNumber').val() },
            dataType: "json",
            success: function (data) {
               if (data == 500) {
                  layer.msg('修改失败');
               } else {
                  layer.msg('修改成功');
                  getAddress()
                  $("#corretDressModal").modal('hide');  //手动关闭
               }
            },
         })
      }
   })

   // 百度地图API功能
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
            map.centerAndZoom(point, 19)
            map.clearOverlays()
            map.addOverlay(new BMap.Marker(point));
            var pointA = new BMap.Point(121.66149, 29.890624);  // 创建点坐标A--大渡口区
            var pointB = new BMap.Point(point.lng, point.lat);  // 创建点坐标B--江北区
            if (map.getDistance(pointA, pointB).toFixed(2) > 3000) {
               layer.msg('超出配送范围')
               $('#corretDress').attr('disabled', true)
            } else {
               $('#corretDress').attr('disabled', false)
            }
         } else {
            alert("您选择地址没有解析到结果!");
         }
      }, '宁波市');
   })

   //通过手机60S倒计时获取验证码方法
   $('#J_getCode').click(function () {
      $.ajax({
         url: "/user/updataPhoneOrNumber",
         type: "post",
         data: { type:'number', oldNumber: person_data.uphoneNumber },
         // dataType: "json",
         success: function (data) {
            layer.msg('获取验证码成功');
         },
         error: function () {
            layer.msg('获取验证码失败');
         }
      })
      $('#J_getCode').hide();
      $('#J_second').html('60');
      $('#J_resetCode').show();
      var second = 60;
      var timer = null;
      timer = setInterval(function () {
         second -= 1;
         if (second > 0) {
            $('#J_second').html(second);
         } else {
            clearInterval(timer);
            $('#J_getCode').show();
            $('#J_resetCode').hide();
         }
      }, 1000);
   })
   // 用旧手机修改新的手机号
   $(".submitByPhone").click(function () {
      if ($('#person_newPhone3').val() == '') {
         layer.alert('请填写手机号');
      } else if ($('#code4').val() == '') {
         layer.alert('请填写验证码');
      } else {
         console.log(person_data);
         $.ajax({
            url: "/user/verify",
            type: "post",
            data: {code: $('#code4').val(),type: 'number', oldNumber: person_data.uphoneNumber, number: $('#person_newPhone3').val(), uId: uId,},
            dataType: "json",
            success: function (data) {
               if (data == 500) {
                  layer.msg('修改失败');
               } else {
                  layer.msg('修改成功');
               }
            },
         })
         }

   });


   //通过邮箱60S倒计时获取验证码方法
   $('#X_getCode').click(function () {
      $.ajax({
         url: "/user/updataPhoneOrNumber",
         type: "post",
         data: { type: 'mail', oldMail: person_data.uemail },
         // dataType: "Xson",
         success: function (data) {
            layer.msg('获取验证码成功');
         },
         error: function () {
            layer.msg('获取验证码失败');
         }
      })
      $('#X_getCode').hide();
      $('#X_second').html('60');
      $('#X_resetCode').show();
      var second = 60;
      var timer = null;
      timer = setInterval(function () {
         second -= 1;
         if (second > 0) {
            $('#X_second').html(second);
         } else {
            clearInterval(timer);
            $('#X_getCode').show();
            $('#X_resetCode').hide();
         }
      }, 1000);
   })
   // 用邮箱修改新的手机号
   $("#submitByEmail").click(function () {
      if ($('#person_newPhone4').val() == '') {
         layer.alert('请填写手机号');
      } else if ($('#code2').val() == '') {
         layer.alert('请填写验证码');
      } else {
         $.ajax({
            url: "/user/verify",
            type: "post",
            data: { type: 'mail', number: $('#person_newPhone4').val(), oldMail: person_data.uemail, uId: uId, code: $('#code2').val() },
            dataType: "json",
            success: function (data) {
               if (data == 200) {
                  layer.msg('修改成功');
               } else {
                  layer.msg('修改失败');
               }
            },
         })
      }

   });

   //通过手机60S倒计时获取验证码方法
   $('#A_getCode').click(function () {
      $.ajax({
         url: "/user/updataPhoneOrNumber",
         type: "post",
         data: { type: 'number', oldNumber: person_data.uphoneNumber },
         // dataType: "json",
         success: function (data) {
            layer.msg('获取验证码成功');
         },
         error: function () {
            layer.msg('获取验证码失败');
         }
      })
      $('#A_getCode').hide();
      $('#A_second').html('60');
      $('#A_resetCode').show();
      var second = 60;
      var timer = null;
      timer = setInterval(function () {
         second -= 1;
         if (second > 0) {
            $('#A_second').html(second);
         } else {
            clearInterval(timer);
            $('#A_getCode').show();
            $('#A_resetCode').hide();
         }
      }, 1000);
   })
   // 用旧手机修改新的邮箱
   $(".submitByPhone1").click(function () {
      if ($('#person_newMail').val() == '') {
         layer.alert('请填写邮箱号');
      } else if ($('#code3').val() == '') {
         layer.alert('请填写验证码');
      } else {
         $.ajax({
            url: "/user/verify",
            type: "post",
            data: { type: 'number', mail: $('#person_newMail').val(), oldNumber: person_data.uphoneNumber, uId: uId, code: $('#code3').val() },
            dataType: "json",
            success: function (data) {
               if (data == 500) {
                  layer.msg('修改失败');
                  $("#corretByEmail").modal('hide');  //手动关闭
               } else {
                  layer.msg('修改成功');
               }
            },
         })
      }
   });

   //通过邮箱60S倒计时获取验证码方法
   $('#B_getCode').click(function () {
      $.ajax({
         url: "/user/updataPhoneOrNumber",
         type: "post",
         data: { type: 'mail', oldMail: person_data.uemail },
         // dataType: "Xson",
         success: function (data) {
            layer.msg('获取验证码成功');
         },
         error: function () {
            layer.msg('获取验证码失败');
         }
      })
      $('#B_getCode').hide();
      $('#B_second').html('60');
      $('#B_resetCode').show();
      var second = 60;
      var timer = null;
      timer = setInterval(function () {
         second -= 1;
         if (second > 0) {
            $('#B_second').html(second);
         } else {
            clearInterval(timer);
            $('#B_getCode').show();
            $('#B_resetCode').hide();
         }
      }, 1000);
   })
   // 用邮箱修改新的邮箱号
   $("#submitByEmail1").click(function () {
      if ($('#person_newMail1').val() == '') {
         layer.alert('请填写邮箱号');
      } else if ($('#code1').val() == '') {
         layer.alert('请填写验证码');
      } else {
         $.ajax({
            url: "/user/verify",
            type: "post",
            data: { type: 'mail', mail: $('#person_newMail1').val(), oldMail: person_data.uemail, uId: uId, code: $('#code1').val() },
            dataType: "json",
            success: function (data) {
               layer.msg('修改成功');
               $("#corretMailByEmail").modal('hide');  //手动关闭
            },
            error: function () {
               layer.msg('修改失败');
            }
         })
      }

   });

   // 修改密码
   $(".sure").click(function () {
      if ($('#oldPassword').val() == '') {
         layer.alert('请输入旧密码');
      } else if ($('#newPassword').val() == '') {
         layer.alert('请输入新密码');
      } else if ($('#newPassword').val() != $('#newPassword1').val()) {
         layer.alert('两次输入的新密码不一样');
      } else {
         $.ajax({
            url: "/user/updataUserPassWord",
            type: "post",
            data: { uId: uId,mPasd:getpass($('#newPassword').val()) ,oldPasd: $('#oldPassword').val(), newPasd: $('#newPassword').val() },
            dataType: "json",
            success: function (data) {
               layer.msg('修改成功');
            },
            error: function () {
               layer.msg('修改失败');
            }
         })
      }

   });

});
