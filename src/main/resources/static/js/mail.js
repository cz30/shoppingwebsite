$(function () {
  //邮箱验证
  $('#exampleMail').blur(function () {
    let mail = $('#exampleMail').val()
    let mailRegExp = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
    let ok = mailRegExp.test(mail)
    if (!ok) {
      $('#mailError').text('邮箱为空或不合法').css('color', 'red')
    } else {
      $('#mailError').text('')
    }
  })
  //密码输入一致验证
  $('#exampleInputPassword').blur(function () {
    let pwd1 = $('#exampleInputPassword').val()
    let pwd2 = $('#exampleInputPassword1').val()
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
  //确认密码输入一致验证
  $('#exampleInputPassword1').blur(function () {
    let pwd1 = $('#exampleInputPassword').val()
    let pwd2 = $('#exampleInputPassword1').val()
    if (!(pwd2)) {
      $('#passwordError1').text('密码不能为空').css('color', 'red')
    } else if (pwd1 !== pwd2) {
      $('#passwordError1').text('两次密码输入不相等').css('color', 'red')
    } else {
      $('#passwordError').text('')
      $('#passwordError1').text('')
    }
  })
  // 验证码
  $('#exampleInputCheck').blur(function () {
    let check = $('#exampleInputCheck').val()
    if (!check) {
      $('#checkError').text('验证码不能为空').css('color', 'red')
    } else {
      $('#checkError').text('')
    }
  })
})
//序列化表单，去到一个表单里的所有参数
$.fn.parseForm = function () {
  let serializeObj = {}
  let array = this.serializeArray()
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
  let count = 0
  for (let i in obj) {
    count++
  }
  return count
}

//加密
function md() {
  const p = $('#exampleInputPassword').val()
  const e = getpass(p)
  $('#pwd').val(e)
}

// 验证码计时器
function throttle(fn, delay) {
  let canUse = true
  return function () {
    if (canUse) {
      fn.apply(this, arguments)
      canUse = false
      setTimeout(() => canUse = true, delay)
    }
  }
}

/*获取验证码*/
let isMail = 1

function getCode() {
  if (isMail) {
    resetCode() //倒计时
  } else {
    $('#phone').focus()
  }
}

// 验证码计时器的时间
function resetCode() {
  $('#sendCheck').hide()
  $('#J_second').html('60')
  $('#J_resetCode').show()
  let second = 60
  let timer = null
  timer = setInterval(function () {
    second -= 1
    if (second > 0) {
      $('#J_second').html(second)
    } else {
      clearInterval(timer)
      $('#sendCheck').show()
      $('#J_resetCode').hide()
    }
  }, 1000)
}

// 提交事件
$(function () {
  //验证码发送ajax
  let yzm = () => {
    const mailValue = $('#exampleMail').val()
    if (mailValue) {
      $.ajax({
        type: 'POST',
        url: '/sendCode',
        data: {number: mailValue, type: 'mail'},
        success: function (data) {
          if (data === 1) {
            Swal.fire('验证码发送成功')
          }
        },
        error: function (data) {
          if (data === 0) {
            Swal.fire('验证码发送失败')
          }
        }
      })
    } else {Swal.fire('请输入邮箱')}
  }
  const throttled = throttle(yzm, 5000)
  $('#sendCheck').click(function (event) {
    const phoneValue = $('#exampleMail').val()
    if (!phoneValue) {
      Swal.fire('请输入邮箱')
      event.preventDefault()
      return
    }
    getCode(event)
    throttled()
    event.preventDefault() //阻止冒泡
  })

  $('#check').click(function () {
    // md5对password加密
    md()
    // 6个有效值
    let ajaxCan = 2
    let obj = $('#fm').parseForm()
    obj.type = 'mail'
    // obj.uid = '1'
    // 判断form表单所有obj属性是否有空，有空的每个属性都会失去焦点
    for (let key in obj) {
      //解决警告
      if (!obj.hasOwnProperty(key)) continue
      if (obj[key] === '' || obj[key] === null || obj[key] === undefined || isNaN(obj[key])) {
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
    // obj=6可以提交,如果有一个字段没有填 就无法提交因为只有6个有效值
    if (ajaxCan === length(obj)) {
      if (obj.password2) {
        delete obj['password2']
      }
      $.ajax({
        type: 'POST',
        url: '/update/byCode',
        data: obj,
        success: function (data) {
          if(data === 1){
            Swal.fire('修改密码成功').then(
                function () {
                  window.location.href = '../templates/Login.html'
                })  //就将返回的数据显示出来
          }else {
            Swal.fire('账号或验证码错误').then(
                function () {
                  window.location.reload()
                }
            )
          }
        },
        error: function () {
          Swal.fire('修改密码失败').then(
            function () {
              window.location.reload()
            }
          )
        }
      })
    }
  })
})
