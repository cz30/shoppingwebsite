// 用户协议的布尔型
var checkit = false
var repeat = []
const comajax = (key, value, url, string) => {
  let num
  let obj = {}
  let nKey = key
  let nobj = {}
  obj[nKey] = value //相当于obj['name'] = 'shilei';
  // ajax封装成一个函数 用来检测用户名 手机号 邮箱 是否和数据库重复
  $.ajax({
    type: 'POST',   //提交的方法
    url: `/register/${url}`, //提交的地址
    data: obj,
    success: function (data) { //成功
      if (!data) {
        Swal.fire(string)
        // nobj[key] = num;
        repeat.push(key)
        console.log(repeat)
      } else {
        let pos = repeat.indexOf(key)
        if (pos !== -1) {
          repeat.splice(pos, 1)
        }
        // num =1
        // nobj[key] = num;
        // console.log(nobj);

      }
    },
    error: function (request) {  //失败的话
      alert('网络出错了')
      window.location.reload()
    }
  })
}
$(function () {
  //邮箱地址验证
  $('#exampleInputEmail1').blur(function () {

    var email = $('#exampleInputEmail1').val()
    if(email === ''){
      return
    }
    var emailRegExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
    var ok = emailRegExp.test(email)
    if (!ok && email) {
      $('#emailError').text('邮箱地址不合法').css('color', 'red')
    } else {
      $('#emailError').text('')

    }
    comajax('email', email, 'checkuemail', '邮箱已存在')
  })
  //密码输入一致验证
  $('#exampleInputPassword').blur(function () {
    var pwd1 = $('#exampleInputPassword').val()
    var pwd2 = $('#exampleInputPassword1').val()
    if (!(pwd1)) {
      $('#passwordError').text('密码不能为空').css('color', 'red')
    } else if ((pwd1 !== pwd2) && pwd2) {
      $('#passwordError1').text('两次密码输入不相等').css('color', 'red')
      $('#passwordError').text('')
    } else {
      $('#passwordError').text('')
      $('#passwordError1').text('')
    }
  })
  //密码输入一致验证
  $('#exampleInputPassword1').blur(function () {
    var pwd1 = $('#exampleInputPassword').val()
    var pwd2 = $('#exampleInputPassword1').val()
    if (!(pwd2)) {
      $('#passwordError1').text('密码不能为空').css('color', 'red')
    } else if (pwd1 != pwd2) {
      $('#passwordError1').text('两次密码输入不相等').css('color', 'red')
    } else {
      $('#passwordError').text('')
      $('#passwordError1').text('')
    }
  })

  //手机号验证
  $('#examplePhone').blur(function () {
    var phone = $('#examplePhone').val()
    var phoneRegExp = /^1([3456789])\d{9}$/
    var ok = phoneRegExp.test(phone)
    if (!ok) {
      $('#phoneError').text('手机号码不合法').css('color', 'red')

    } else {
      $('#phoneError').text('')

    }
    comajax('phoneNumber', phone, 'checkuphone', '手机号已存在')
  })
  //账号验证--限制长度/首字符为英文
  $('#number').blur(function () {
    var num = $('#number').val()
    var numRegExp = /^[A-Za-z][A-Za-z0-9]{4,14}$/
    var ok = numRegExp.test(num)
    if (!ok) {
      $('#numberError').text('账号不合法,必须以字母开头不超过15位').css('color', 'red')
    } else {
      $('#numberError').text('')

    }
    comajax('username', num, 'checkuname', '账号已存在')

  })
  // 勾选用户协议
  $('#check').click(function () {
    if ($('#checkbox').is(':checked')) {
      checkit = true
    } else {
      Swal.fire('请先同意用户协议')
      checkit = false
    }
  })


})

//序列化表单，去到一个表单里的所有参数
$.fn.parseForm = function () {
  var serializeObj = {}
  var array = this.serializeArray()
  var str = this.serialize()
  $(array).each(function () {
    if (serializeObj[this.name]) {
      if ($.isArray(serializeObj[this.name])) {
        serializeObj[this.name].push(this.value)
      } else {
        serializeObj[this.name] = [serializeObj[this.name], this.value]
      }
    } else {
      serializeObj[this.name] = this.value
    }
  })
  return serializeObj
}

// 获取obj对象的长度
function length(obj) {
  var count = 0
  for (var i in obj) {
    count++
  }
  return count
}
// 提交事件
$(function () {
  $('#check').click(function () {
    // md5对password加密
    md()
    // 8个有效值
    let ajaxCan = 4
    let obj = $('#fm').parseForm()
    obj.perms = 'user'
    // 如果邮箱为空不提交邮箱这个字段
    if (obj.uEmail === '') {
      delete obj['uEmail']
      //7个有效值
      ajaxCan = 3
    }
    // 对邮箱进行判断
    // let email = $('#exampleInputEmail1').val()
    // let emailRegExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
    // let ok = emailRegExp.test(email)
    // // 邮箱有值或者不符合要求均无法提交ajax请求
    // if (email && !ok) {
    //   checkit = false
    // }
    // 判断form表单所有obj属性是否有空，有空的每个属性都会失去焦点
    for (let key in obj) {
      if (obj[key] === '' || obj[key] === null || obj[key] === undefined || obj[key] === NaN) {
        let eq = document.getElementsByName(key)
        $(eq).blur()
      }
    }
    // 每有一个有效值，ajaxCan+1
    $('.redTip').each(function (ii, vv) {
      //ii 指第几个元素的序列号。
      //vv 指遍历得到的元素。
      if (!($(vv).text())) {
        ajaxCan += 1
      }
    })

    // obj=7或者8可以提交（邮箱排除在外）如果有一个字段没有填 就无法提交因为只有6个有效值
    if (ajaxCan === length(obj)) {
      if (obj.uPassword2) {
        delete obj['uPassword2']
      }
      if (checkit && (repeat.length === 0)) {
        $.ajax({
          type: 'POST',   //提交的方法
          url: '/register/user', //提交的地址
          data: obj,
          success: function () {  //成功
            Swal.fire('注册账号成功').then(function () {
              window.location.href = 'Login.html'
            })  //就将返回的数据显示出来

          },
          error: function (request) {  //失败的话
            Swal.fire('注册账号失败')
            // window.location.reload()
          }
        })

      }

    }


  })
})
