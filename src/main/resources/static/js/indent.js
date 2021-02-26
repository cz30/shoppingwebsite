$(function () {
  let uid = $.cookie('userId')
  $('.user-id').text(uid)
  //全部订单循环部分
  function allOrderFunction() {
    $.ajax({
      url: '/order/showAllOrder',
      type: 'GET',
      datatype: 'json',
      data: {
        'uid': uid,
        'page': 1
      },
      success: function (data) {
        console.log(data)
        showGoods(data.list)
        pageFunction(data, '/order/showAllOrder')
      }
    })
  }
  //自调用渲染页面
  allOrderFunction()

  //左侧边栏按钮样式变换
  let index = 0
  const clickChange = (e) => {
    console.log('执行')
    if ($(e.target).hasClass('indent')) {
      $('.indent').removeClass('indent-focus')
      $(e.target).addClass('indent-focus')
      // 获得当前点击index
      index = $(e.target).index()
    }
    let odStatus = ''
    if (index === 0) {
      allOrderFunction()
      return ;
    } else if (index === 1) {
      odStatus = 0//待付款
    } else if (index === 2) {
      odStatus = 1//待收货
    } else if (index === 3) {
      odStatus = 8//待评价
    }
    $.ajax({
      url: '/order/showOthers',
      type: 'GET',
      datatype: 'json',
      data: {
        'status': odStatus,
        'uid': uid,
        'page': 1
      },
      success: function (data) {
        showGoods(data.list)
        pageFunction(data, '/order/showOthers', odStatus)
      }
    })
  }
  $('.indent').click(function (e) {
    clickChange(e)
  })

  function showGoods(indentData) {
    let str = ''
    let str2 = ''
    let conIndent = {}
    for (let i = 0; i < indentData.length; i++) {
      let str1 = ''
      let str3 = ''
      let str4 = ''
      conIndent[`${indentData[i].odDelid}`] = 0
      for (let j = 0; j < indentData[i].bjfOrderItems.length; j++) {
        if ((indentData[i].odStatus === 1) || (indentData[i].odStatus === 9)) {
          if (indentData[i].bjfOrderItems[j].oiSupport === 1) {
            if (indentData[i].bjfOrderItems[j].oiStatus === 6) {
              str3 = `<div class="consignee sales-return"  data-odDelid=${indentData[i].odDelid} 
                            data-oiId=${indentData[i].bjfOrderItems[j].oiId}>退款/退货</div>`
            } else if (indentData[i].bjfOrderItems[j].oiStatus === 0) {
              conIndent[`${indentData[i].odDelid}`] = 1
              str3 = `<div class="consignee-box" data-oiStatus=${indentData[i].bjfOrderItems[j].oiStatus}>
                          <div>退款中</div>
                          <div class="consignee cancel-return"  data-odDelid=${indentData[i].odDelid} 
                          data-oiId=${indentData[i].bjfOrderItems[j].oiId}>取消退款</div>
                      </div>`
            } else if (indentData[i].bjfOrderItems[j].oiStatus === 1) {
              str3 = `<div>退款成功</div>`
            } else if (indentData[i].bjfOrderItems[j].oiStatus === 2) {
              str3 = `<div class="consignee-box">
                          <div>退款失败</div>
                          <div class="consignee sales-return"  data-odDelid=${indentData[i].odDelid} 
                          data-oiId=${indentData[i].bjfOrderItems[j].oiId}>再次退款</div>
                      </div>`
            }
          } else if (indentData[i].bjfOrderItems[j].oiSupport === 0) {
            str3 = `<div>不可退商品</div>`
          }
        } else if (indentData[i].odStatus === 2) {
          if (indentData[i].bjfOrderItems[j].oiStatus === 3) {
            str3 = `<div class="evaluate" data-odDelid=${indentData[i].odDelid} 
                        data-oiStatus=${indentData[i].bjfOrderItems[j].oiStatus} 
                        data-oiId=${indentData[i].bjfOrderItems[j].oiId}
                        data-cmdId=${indentData[i].bjfOrderItems[j].cmdId}>未评价</div>`
          } else if (indentData[i].bjfOrderItems[j].oiStatus === 4) {
            str3 = `<div class="evaluate" data-odDelid=${indentData[i].odDelid} 
                        data-oiStatus=${indentData[i].bjfOrderItems[j].oiStatus} 
                        data-oiId=${indentData[i].bjfOrderItems[j].oiId}
                        data-cmdId=${indentData[i].bjfOrderItems[j].cmdId}>追加评价</div>`
          } else if (indentData[i].bjfOrderItems[j].oiStatus === 5) {
            str3 = `<div>已评价</div>`
          }
        }
        //商品
        str1 += `<div class="same-indent same-flex">
                            <div class="commodity-list same-flex">
                                <img class="commodity-img" src="${indentData[i].bjfOrderItems[j].oiImage}" alt="">
                                <div class="commodity-content">
                                     <div>${indentData[i].bjfOrderItems[j].oiName}</div>
                                     <div>${indentData[i].bjfOrderItems[j].oiContent}</div>
                                </div>
                            </div>
                            <div class="same-style unit-price">${indentData[i].bjfOrderItems[j].oiPrice}</div>
                            <div class="same-style quantity">${indentData[i].bjfOrderItems[j].oiNum}</div>
                            <div class="same-style refund">${str3}</div>
                        </div>`
      }

      if (indentData[i].odStatus === 0) {     //未付款
        str2 = `<span>待付款</span>`//订单状态
        str4 = `<div class="handle immediately-pay"  data-odDelid=${indentData[i].odDelid}>立即付款</div>
                            <div class="cancel"  data-odDelid=${indentData[i].odDelid}>取消订单</div>`
      } else if (indentData[i].odStatus === 1) {       //已付款
        str2 = `<span>待收货</span>` //订单状态
        str4 = `<div class="handle confirm-receipt" data-odDelid=${indentData[i].odDelid}>确认收货</div>`   //交易操作
      } else if ((indentData[i].odStatus === 2) || (indentData[i].odStatus === 9)) {       //2或者9已完成
        str2 = `<span>已完成</span>` //订单状态
      }

      str += `<div class="indent-list" data-odDelid=${indentData[i].odDelid}>
                        <div class="indent-detail-box same-flex">
                            <div class="same-flex">
                                <div class="indent-time"> ${indentData[i].odTimeStr}</div>
                                <div>
                                    <span>订单编号:</span>
                                    <span> ${indentData[i].odDelid}</span>   
                                </div>
                            </div>
                            <div class="same-flex">
                                <div class="indent-details" data-odDelid=${indentData[i].odDelid} data-odStatus=${indentData[i].odStatus}>订单详情</div>
                                <div class="indent-delete">
                                    <span class="glyphicon glyphicon-trash" aria-hidden="true"  data-odDelid=${indentData[i].odDelid}></span>
                                </div>
                            </div>
                        </div>
                        <div class="same-flex indent-list-box">
                            <div class="commodity-banner">${str1}</div>
                            <div class="same-flex sa">
                                <div class="same-style gross-amount">
                                    <div>￥${indentData[i].odTotalAmount}</div>
                                    <div>(含运费:￥${indentData[i].mcDpfee})</div>
                                </div>
                                <div class="same-style state">${str2}</div>
                                <div class="same-style operation-btn">${str4}</div>
                            </div>
                        </div>
                    </div>`
    }
    $('#content').html(str)

    // 事件代理获取当前点击按钮所对应的订单数据
    let odDelid = ''//订单编号
    let odStatus = ''//订单状态
    let oiId = ''//订单详情ID
    let cmdId = ''//订单详情ID
    let oiStatus = ''//判断是评论还是追评
    let spfId = ''//规格id
    $('.indent-list').click(function (e) {
      let event = e || window  // 兼容性处理
      odDelid = event.target.getAttribute('data-odDelid')
      odStatus = event.target.getAttribute('data-odStatus')
      oiId = event.target.getAttribute('data-oiId')
      cmdId = event.target.getAttribute('data-cmdId')
      oiStatus = event.target.getAttribute('data-oiStatus')
      spfId = event.target.getAttribute('data-spfId')
    })

    //点击订单详情按钮跳转订单详情页
    $('.indent-details').on('click', function () {
      setTimeout(function () {
        window.top.location.href = 'details.html?id=' + odDelid + '=' + odStatus
      }, 0)
    })
    //删除订单事件
    $('.indent-delete').click(function (e) {
      setTimeout(function () {
        if (conIndent[odDelid] !== 0) {
          return layer.msg('有退款商品', {icon: 5})
        }
        layer.confirm('您确定要删除该订单吗???', {
          btn: ['确认', '取消'] //按钮
        }, function () {
          $.ajax({
            url: '/order/operation',
            type: 'GET',
            datatype: 'json',
            data: {
              'odDelid': odDelid,
              'id': 1
            },
            success: function (data) {
              if (data === 1) {
                layer.msg('删除成功', {icon: 1})
                console.log(e)
                clickChange(e)
              } else {
                layer.msg('删除失败', {icon: 5})
              }
            }
          })
        })
      }, 0)
    })
    //立即付款
    $('.immediately-pay').on('click', function () {
      setTimeout(function () {
        $.cookie('outTradeNo', odDelid)
        window.top.location.href = 'payment.html'
      }, 0)
    })
    //取消订单事件
    $('.cancel').click(function (e) {
      setTimeout(function () {
        layer.confirm('您确定要取消当前订单吗???', {
          btn: ['确认', '取消'] //按钮
        }, function () {
          $.ajax({
            url: '/order/operation',
            type: 'GET',
            datatype: 'json',
            data: {
              'odDelid': odDelid,
              'id': 3
            },
            success: function (data) {
              if (data === 1) {
                layer.msg('取消成功', {icon: 1})
                clickChange(e)
              } else {
                layer.msg('取消失败', {icon: 5})
              }
            }
          })
        })
      }, 0)
    })
    //确认收货事件
    $('.confirm-receipt').click(function (e) {
      setTimeout(function () {
        if (conIndent[odDelid] !== 0) {
          layer.msg('有正在退款中商品，不能确认收货', {icon: 5})
        } else {
          layer.confirm('确认已收到货吗？', {
            btn: ['确认', '取消'] //按钮
          }, function () {
            $.ajax({
              url: '/order/operation',
              type: 'GET',
              datatype: 'json',
              data: {
                'odDelid': odDelid,
                'id': 2
              },
              success: function (data) {
                if (data === 1) {
                  layer.msg('确认收货成功', {icon: 1})
                  clickChange(e)
                } else {
                  layer.msg('确认收货失败', {icon: 5})
                }
              }
            })
          })
        }
      }, 0)
    })
    //点击退款退货按钮跳转退款退货页面
    $('.sales-return').click(function () {
      setTimeout(function () {
        window.top.location.href = 'refund.html?id=' + odDelid + '=' + oiId
      }, 0)
    })
    //取消退货事件
    $('.cancel-return').click(function (e) {
      setTimeout(function () {
        layer.confirm('取消退货吗？', {
          btn: ['确认', '取消'] //按钮
        }, function () {
          $.ajax({
            url: '/order/notBack',
            type: 'GET',
            datatype: 'json',
            data: {
              'odDelid': odDelid,
              'oiId': oiId
            },
            success: function (data) {
              if (data === 1) {
                layer.msg('取消退货成功', {icon: 1})
                clickChange(e)
              } else {
                layer.msg('取消退货失败', {icon: 5})
              }
            }
          })
        })
      }, 0)
    })
    //未评价/追加评价
    $('.evaluate').on('click', function (e) {
      //  userId /cmdId /oiStatus
      setTimeout(function () {
        let test = $(e.target).parent().prevAll().eq(2).children()
        let Img = test.eq(0).attr('src')
        let Name = test.eq(1).children().eq(0).text()
        console.log(oiId)
        window.localStorage.setItem('oiImage', Img)
        window.localStorage.setItem('oiName', Name)
        window.localStorage.setItem('odDelid', odDelid)
        window.localStorage.setItem('oiId', oiId)

        window.top.location.href = 'evaluate.html?id=' + oiStatus + '=' + cmdId
      }, 0)
    })

  }

  //分页符插件
  function pageFunction(data, url, odStatus) {
    $('#pageLimit').bootstrapPaginator({
      totalPages: data.totalPage,//共有多少页。
      currentPage: data.currentpage,//当前的请求页面。
      size: 'normal',//分页符的大小
      bootstrapMajorVersion: 3,//bootstrap的版本要求。
      alignment: 'left',
      numberOfPages: 6,//一页列出多少数据。
      itemTexts: function (type, page) {//如下的代码是将页眉显示的中文显示我们自定义的中文。
        switch (type) {
          case 'first':
            return '首页'
          case 'prev':
            return '上一页'
          case 'next':
            return '下一页'
          case 'last':
            return '末页'
          case 'page':
            return page
        }
      },
      onPageClicked: function (event, originalEvent, type, page) {
        $.ajax({
          url: url,
          type: 'GET',
          datatype: 'json',
          data: {
            'status': odStatus,
            'uid': uid,
            'page': page
          },
          success: function (data) {
            console.log(data)
            showGoods(data.list)
          }
        })
      }
    })
  }
})




