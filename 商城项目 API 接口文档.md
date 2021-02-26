# 邦基帆商城项目 API 接口文档

## 1.1. 商城首页

### 1.1.1. 获取商家logo

- 请求路径：/homepage/logo
- 请求方法：get
- 响应数据

```json
http://192.168.0.200/group1/M00/00/01/wKgAyF8rZvaAQA9TAAODVqCvLus086.jpg
```



### 1.1.2. 类目展示

- 请求路径：/homepage/getTree

- 请求方法：get

- 响应参数

  | 参数名 | 参数说明          |                                             备注 |
  | ------ | ----------------- | -----------------------------------------------: |
  | cgId   | 类目 ID           |                                                  |
  | cgName | 类目名称          |                                                  |
  | next   | 二级类目/三级类目 | 递归实现，一级类目套二级类目，二级类目套三级类目 |

- 响应数据

  ```json
  [
    {
      "cgId": 1,
      "cgName": "水果",
      "next": []
    },
    {
      "cgId": 2,
      "cgName": "蔬菜",
      "next": []
    },
    {
      "cgId": 3,
      "cgName": "粮油",
      "next": []
    },
    {
      "cgId": 4,
      "cgName": "酒水",
      "next": [
        {
          "cgId": 10,
          "cgName": "红酒",
          "next": [
            {
              "cgId": 70,
              "cgName": "魔鬼1",
              "next": []
            }
          ]
        },
        {
          "cgId": 11,
          "cgName": "啤酒",
          "next": [
            {
              "cgId": 28,
              "cgName": "洋酒1",
              "next": []
            }
          ]
        },
        {
          "cgId": 12,
          "cgName": "白酒",
          "next": []
        }
      ]
    }
  ]
  ```



### 1.1.3. 轮播图展示

- 请求路径：/homepage/slideShow

- 请求方法：get

- 响应参数

  | 参数名     | 参数说明       | 备注 |
  | ---------- | -------------- | ---: |
  | slideId    | 轮播图 ID      |      |
  | slideImage | 轮播图图片地址 |      |

- 响应数据

  ```json
  [
    {
      "slideId": 7,
      "slideImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAA5hJAADPD1cyfEc345.jpg"
    },
    {
      "slideId": 8,
      "slideImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAAwQTAAvxwSEAt1M585.jpg"
    },
    {
      "slideId": 9,
      "slideImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAW2aIAAUQbKgn3Xk153.jpg"
    }
  ]
  ```

  

### 1.1.4.推荐图展示

- 请求路径：/homepage/recommendImage

- 请求方法：get

- 响应参数

  | 参数名   | 参数说明     | 备注 |
  | -------- | ------------ | ---: |
  | cmdId    | 商品 ID      |      |
  | cmdImage | 推荐图片地址 |      |

- 响应数据

  ```json
  [
    {
      "cmdId": 2,
      "cmdName": "香蕉",
      "cmdImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAAwQTAAvxwSEAt1M585.jpg",
      "cmdPrice": 10.12,
      "cgId": 1,
      "elId": 2,
      "cmdHot": 1,
      "cmdCake": 100,
      "cmdRecommend": 1,
      "cmdScore": 2,
      "cmdCountStatus": 1,
      "cmdDiscount": 0.98,
      "cmdSupport": 0,
      "bjfCommodityDetails": null,
      "bjfSpfList": null
    },
    {
      "cmdId": 4,
      "cmdName": "西瓜",
      "cmdImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAAwQTAAvxwSEAt1M585.jpg",
      "cmdPrice": 12.30,
      "cgId": 1,
      "elId": 4,
      "cmdHot": 1,
      "cmdCake": 70,
      "cmdRecommend": 1,
      "cmdScore": 2,
      "cmdCountStatus": 0,
      "cmdDiscount": 0.98,
      "cmdSupport": 0,
      "bjfCommodityDetails": null,
      "bjfSpfList": null
    }
  ]
  ```



### 1.1.4.热销图展示

- 请求路径：/homepage/hotImage

- 请求方法：get

- 响应参数

  | 参数名   | 参数说明     | 备注 |
  | -------- | ------------ | ---: |
  | cmdId    | 商品 ID      |      |
  | cmdImage | 热销图片地址 |      |

- 响应数据

  ```json
  [
    {
      "cmdId": 2,
      "cmdName": "香蕉",
      "cmdImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAAwQTAAvxwSEAt1M585.jpg",
      "cmdPrice": 10.12,
      "cgId": 1,
      "elId": 2,
      "cmdHot": 1,
      "cmdCake": 100,
      "cmdRecommend": 1,
      "cmdScore": 2,
      "cmdCountStatus": 1,
      "cmdDiscount": 0.98,
      "cmdSupport": 0,
      "bjfCommodityDetails": null,
      "bjfSpfList": null
    },
    {
      "cmdId": 3,
      "cmdName": "梨",
      "cmdImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAAwQTAAvxwSEAt1M585.jpg",
      "cmdPrice": 8.23,
      "cgId": 1,
      "elId": 3,
      "cmdHot": 1,
      "cmdCake": 20,
      "cmdRecommend": 0,
      "cmdScore": 2,
      "cmdCountStatus": 1,
      "cmdDiscount": 0.98,
      "cmdSupport": 1,
      "bjfCommodityDetails": null,
      "bjfSpfList": null
    }
  ]
  ```



### 1.1.5.类目跳转商品展示

- 请求路径：/homepage/findPageGetTree

- 请求方法：get

- 请求参数

  | 参数名  | 参数说明   | 备注                                     |
  | ------- | ---------- | ---------------------------------------- |
  | pageNum | 当前页码值 | 【可选参数】如果不传递，则默认展示第一页 |
  | cgId    | 类目ID     |                                          |

- 响应参数

  | 参数名     | 参数说明         | 备注                                                         |
  | ---------- | ---------------- | ------------------------------------------------------------ |
  | pageNum    | 当前页码         |                                                              |
  | pageSize   | 每页数量         |                                                              |
  | totalSize  | 类目下的商品总数 |                                                              |
  | totalPages | 总页数           |                                                              |
  | content    | 商品信息         | cmdId：商品id，cmdName：商品名称，cmdImage：商品图片，cmdPrice：商品价格 |

- 响应数据

  ```json
  {
    "pageNum": 1,
    "pageSize": 5,
    "totalSize": 7,
    "totalPages": 2,
    "content": [
      {
        "cmdId": 1,
        "cmdName": "苹果",
        "cmdImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAAwQTAAvxwSEAt1M585.jpg",
        "cmdPrice": 15.00,
        "cgId": null,
        "elId": null,
        "cmdHot": null,
        "cmdCake": null,
        "cmdRecommend": null,
        "cmdScore": null,
        "cmdCountStatus": null,
        "cmdDiscount": null,
        "cmdSupport": null,
        "bjfCommodityDetails": null,
        "bjfSpfList": null
      },
      {
        "cmdId": 2,
        "cmdName": "香蕉",
        "cmdImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAAwQTAAvxwSEAt1M585.jpg",
        "cmdPrice": 10.12,
        "cgId": null,
        "elId": null,
        "cmdHot": null,
        "cmdCake": null,
        "cmdRecommend": null,
        "cmdScore": null,
        "cmdCountStatus": null,
        "cmdDiscount": null,
        "cmdSupport": null,
        "bjfCommodityDetails": null,
        "bjfSpfList": null
      }
    ]
  }
  ```

  

## 1.2. 商品详情页

### 1.2.1.详情页展示商品信息

- 请求路径：/homepage/commodityDetails

- 请求方法：get

- 请求参数

  | 参数名 | 参数说明 | 备注 |
  | ------ | -------- | ---- |
  | cmdId  | 商品ID   |      |

- 响应参数

  | 参数名              | 参数说明         | 备注                             |
  | ------------------- | ---------------- | -------------------------------- |
  | cmdId               | 商品ID           |                                  |
  | cmdName             | 商品名称         |                                  |
  | cmdPrice            | 具体商品最低价   |                                  |
  | cmdCake             | 销量             |                                  |
  | cmdScore            | 商品赠送积分     |                                  |
  | cmdCountStatus      | 是否参与会员打折 |                                  |
  | cmdDiscount         | 会员折扣         |                                  |
  | bjfCommodityDetails | 商品详情信息     | 是一个商品详情表封装的对象       |
  | cmaName             | 出厂厂家名称     |                                  |
  | cmaAddress          | 厂家地址         |                                  |
  | cmaDay              | 保质期           |                                  |
  | cmaPhone            | 厂家联系电话     |                                  |
  | cmaBrname           | 品牌名称         |                                  |
  | cmaCadress          | 商品产地         |                                  |
  | cmaWeight           | 商品净重         |                                  |
  | cmaEnviro           | 商品存储环境     |                                  |
  | cmaImage            | 详情页商品图片   | 页面左上方那多张轮播的图片       |
  | cmaPicture          | 商品图片         | 页面最下面的详情图片             |
  | bjfSpfList          | 规格信息         | 用一个List封装了多个规格信息对象 |
  | spfId               | 规格ID           |                                  |
  | spfContent          | 规格内容         |                                  |
  | spfCount            | 库存             |                                  |
  | spfPrice            | 价格             |                                  |
  | spfImage            | 图片             |                                  |

- 响应数据

  ```json
  {
    "cmdId": 1,
    "cmdName": "苹果",
    "cmdImage": null,
    "cmdPrice": 15.00,
    "cgId": null,
    "elId": null,
    "cmdHot": null,
    "cmdCake": 200,
    "cmdRecommend": null,
    "cmdScore": 2,
    "cmdCountStatus": 1,
    "cmdDiscount": 0.98,
    "cmdSupport": null,
    "bjfCommodityDetails": {
      "cmaId": 1,
      "cmaName": "红苹果",
      "cmaAddress": "你家",
      "cmaDay": "2020/1/1-20207/7",
      "cmaPhone": "123456",
      "cmaBrname": "奥利给",
      "cmaCadress": "你家",
      "cmaWeight": "200",
      "cmaEnviro": "常温",
      "cmdId": 1,
      "cmaImage": "http://192.168.0.200/group1/M00/00/01/wKgAyF8fwFmAe7suAAODVqCvLus262.jpg,http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg,http://192.168.0.200/group1/M00/00/02/wKgAyF9EZKOAZjPlAAJmBBHwgDM446.jpg,http://192.168.0.200/group1/M00/00/02/wKgAyF9EZK-ANVOSAACiBiCwX60026.jpg,http://192.168.0.200/group1/M00/00/02/wKgAyF9EZLiAMXZUAAG1Jb08gf0049.jpg",
      "cmaPicture": "http://192.168.0.200/group1/M00/00/01/wKgAyF8fwFmAe7suAAODVqCvLus262.jpg"
    },
    "bjfSpfList": [
      {
        "spfId": 2,
        "spfContent": "{尺码:L,颜色:白}",
        "spfCount": 49,
        "cmdId": 1,
        "spfPrice": 0,
        "spfImage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAA5hJAADPD1cyfEc345.jpg"
      },
      {
        "spfId": 1,
        "spfContent": "{尺码:L,颜色:黑}",
        "spfCount": 81,
        "cmdId": 1,
        "spfPrice": 0,
        "spfImage": "1231231231"
      }
    ]
  }
  ```



### 1.2.2.详情页展示评价信息

- 请求路径：/evaluate/showComments

- 请求方法：get

- 请求参数

  | 参数名 | 参数说明 | 备注 |
  | ------ | -------- | ---- |
  | cmdId  | 商品ID   |      |

- 响应参数

  | 参数名      | 参数说明     | 备注 |
  | ----------- | ------------ | ---- |
  | cmdId       | 商品ID       |      |
  | eid         | 评价主键ID   |      |
  | econtent    | 初次评价内容 |      |
  | eimage      | 初次评价图片 |      |
  | egrepStatus | 好评/差评    | 0/1  |
  | cmdId       | 商品ID       |      |
  | u_id        | 用户ID       |      |
  | utime       | 初次评价时间 |      |
  | emback      | 商家回复内容 |      |
  | mtime       | 商家回复时间 |      |
  | euback      | 追加评价内容 |      |
  | ebackImages | 追加评价图片 |      |
  | ubackTime   | 追加评价时间 |      |

- 响应数据

  ```json
  [
    {
      "cmdId": 16,
      "odDelid": null,
      "eid": 133,
      "econtent": "很好吃",
      "emback": "你觉得行就行",
      "emtime": "2020-09-04T14:33:48.000+00:00",
      "eimage": "http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg#http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg#http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg",
      "egrepStatus": 1,
      "euback": "还行吧",
      "ebackImages": "http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg#http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg",
      "ubackTime": "2020-09-10 10:34:28",
      "mtime": "2020-09-04 10:33:48",
      "utime": "2020-09-03 10:33:44",
      "uheadSculpture": "http://192.168.0.200/group1/M00/00/01/wKgAyF803XuAahFjAAzodRl2Jqw845.jpg",
      "uid": null,
      "uaccount": "天下第一大大大猛男",
      "eubackTime": "2020-09-10T14:34:28.000+00:00",
      "eutime": "2020-09-03T14:33:44.000+00:00"
    },
    {
      "cmdId": 16,
      "odDelid": null,
      "eid": 134,
      "econtent": "不好吃",
      "emback": "好的，不好吃",
      "emtime": "2020-09-03T14:41:31.000+00:00",
      "eimage": "http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg#http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg",
      "egrepStatus": 0,
      "euback": "就是不好吃",
      "ebackImages": "http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg#http://192.168.0.200/group1/M00/00/02/wKgAyF9EZJyAA0PsAAZAhMP7u9M463.jpg",
      "ubackTime": "2020-09-03 10:41:50",
      "mtime": "2020-09-03 10:41:31",
      "utime": "2020-09-09 10:41:28",
      "uheadSculpture": "http://192.168.0.200/group1/M00/00/01/wKgAyF803XuAahFjAAzodRl2Jqw845.jpg",
      "uid": null,
      "uaccount": "张凯玉lsp",
      "eubackTime": "2020-09-03T14:41:50.000+00:00",
      "eutime": "2020-09-09T14:41:28.000+00:00"
    }
  ]
  ```



## 1.3. 评价

### 1.3.1.用户初次评价

- 请求路径：/evaluate/toBeEvaluated

- 请求方法：get

- 请求参数

  | 参数名      | 参数说明     | 备注                                         |
  | ----------- | ------------ | -------------------------------------------- |
  | cmdId       | 商品ID       |                                              |
  | uId         | 用户ID       |                                              |
  | odDelid     | 订单编号     |                                              |
  | eContent    | 初次评价内容 | 将对内容进行文明用语进行判断                 |
  | eImage      | 初次评价图片 | 接收的是数组格式      MultipartFile[] eImage |
  | eGrepStatus | 好评/差评    | 0/1                                          |

- 响应数据说明
  - 返回“civilization”则代表评价内容中包含敏感词，提示用户注意文明，且评价未成功
  - 返回“success”则代表评价成功

### 1.3.2.点击追加评价展示初次评价和商家回复

- 请求路径：/evaluate/additionalComments

- 请求方法：get

- 请求参数

  | 参数名 | 参数说明 | 备注 |
  | ------ | -------- | ---- |
  | cmdId  | 商品ID   |      |
  | uId    | 用户ID   |      |

- 响应参数

  | 参数名      | 参数说明     | 备注 |
  | ----------- | ------------ | ---- |
  | cmdId       | 商品ID       |      |
  | eid         | 评价主键ID   |      |
  | econtent    | 初次评价内容 |      |
  | eimage      | 初次评价图片 |      |
  | egrepStatus | 好评/差评    | 0/1  |
  | cmdId       | 商品ID       |      |
  | u_id        | 用户ID       |      |
  | utime       | 初次评价时间 |      |
  | emback      | 商家回复内容 |      |
  | mtime       | 商家回复时间 |      |

- 响应数据

  ```json
  {
    "cmdId": 12,
    "odDelid": "123",
    "egrepStatus": 1,
    "ebackImages": null,
    "emback": "asdasd",
    "euback": null,
    "econtent": "好吃",
    "emtime": "2020-09-25T16:11:24.000+00:00",
    "eid": 207,
    "eimage": "http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAA5hJAADPD1cyfEc345.jpg#http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAAwQTAAvxwSEAt1M585.jpg#http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAW2aIAAUQbKgn3Xk153.jpg",
    "uid": 12,
    "eubackTime": null,
    "eutime": "2020-09-15T09:52:29.466+00:00"
  }
  ```



### 1.3.3.追加评价提交

- 请求路径：/evaluate/additionalCommentsSubmit

- 请求方法：get

- 请求参数

  | 参数名      | 参数说明     | 备注                                              |
  | ----------- | ------------ | ------------------------------------------------- |
  | cmdId       | 商品ID       |                                                   |
  | uId         | 用户ID       |                                                   |
  | odDelid     | 订单编号     |                                                   |
  | eUback      | 追加评价内容 | 将对内容进行文明用语进行判断                      |
  | eBackImages | 初次评价图片 | 接收的是数组格式      MultipartFile[] eBackImages |

- 响应数据说明

  - 返回“civilization”则代表评价内容中包含敏感词，提示用户注意文明，且评价未成功

  - 返回“success”则代表评价成功

## 1.4. 会员中心

### 1.4.1.会员中心首页展示

- 请求路径：/vip/vipHome

- 请求方法：get

- 请求参数

  | 参数名 | 参数说明 | 备注 |
  | ------ | -------- | ---- |
  | uId    | 用户ID   |      |

- 响应参数

  | 参数名              | 参数说明                         | 备注                                                 |
  | ------------------- | -------------------------------- | ---------------------------------------------------- |
  | memberRecord        | 会员表中是否有这个用户的会员信息 | 1是有，0是没有，null则代表用户没有登录，让用户去登录 |
  | memberState         | 判断用户是否过期                 | 1是没过期，0是过期                                   |
  | mbEtime             | 会员结束时间                     | String字符串形式的时间，做好了格式修改               |
  | mbTimes             | 剩余免费配送次数                 |                                                      |
  | bjfMenberRightsList | 所有的会员权益                   | 把会员权益封装到List中                               |
  | bjfCommodityList    | 部分享受折扣的商品               | 随机15件商品，并把商品封装到List中                   |

- 响应数据

  ```json
  {
    "memberRecord": 1,
    "memberState": 1,
    "mbEtime": “2020-08-22”,
    "mbTimes": 3,
    "bjfMenberRightsList": [
      {
        "mrId": 1,
        "mrContent": "畅享1080p画质"
      },
      {
        "mrId": 2,
        "mrContent": "vip专享通道，化粪池任您选择吃屎快人一步"
      },
      {
        "mrId": 3,
        "mrContent": "超大碗，普通用户三倍碗，吃屎更舒心"
      },
      {
        "mrId": 4,
        "mrContent": "尊享vip化粪池，不在愁没有资源"
      }
    ],
    "bjfCommodityList": [
      {
        "cmdId": 8,
        "cmdName": "白萝卜",
        "cmdImage": "http://192.168.0.200/group1/M00/00/01/wKgAyF8gzuSAck-CAAGX3o_9J9Y500.jpg",
        "cmdPrice": 2.33,
        "cgId": 2,
        "elId": 8,
        "cmdHot": 0,
        "cmdCake": 56,
        "cmdRecommend": 0,
        "cmdScore": null,
        "cmdCountStatus": 1,
        "cmdDiscount": 0.98
      },
      {
        "cmdId": 22,
        "cmdName": "呵呵",
        "cmdImage": "http://192.168.0.200/group1/M00/00/01/wKgAyF8gzuSAck-CAAGX3o_9J9Y500.jpg",
        "cmdPrice": 8.00,
        "cgId": 1,
        "elId": 1,
        "cmdHot": 0,
        "cmdCake": 200,
        "cmdRecommend": 0,
        "cmdScore": 2,
        "cmdCountStatus": 1,
        "cmdDiscount": 0.98
      }
    ]
  }
  
  ```

### 1.4.2.会员选择框

- 请求路径：/vip/bjfMemberPay

- 请求方法：get

- 响应参数

  | 参数名   | 参数说明   | 备注 |
  | -------- | ---------- | ---- |
  | mbpId    | 会员选择ID |      |
  | mbpMonth | 月份       |      |
  | mbEtime  | 金额       |      |

- 响应数据

  ```json
  [
    {
      "mbpId": 1,
      "mbpMonth": 1,
      "mbpMoney": 0.01
    },
    {
      "mbpId": 2,
      "mbpMonth": 6,
      "mbpMoney": 0.02
    },
    {
      "mbpId": 3,
      "mbpMonth": 12,
      "mbpMoney": 0.01
    }
  ]
  ```

### 1.4.3.去支付会员

- 请求路径：/vip/vipPay

- 请求方法：get

- 请求参数

  | 参数名 | 参数说明   | 备注 |
  | ------ | ---------- | ---- |
  | mbpId  | 会员选择ID |      |
  | UId    | 用户ID     |      |

- 响应数据说明
  - 返回给前端订单编号，需要把订单编号带到下一个页面

### 1.4.4.选择支付方式页面停留时间过长或者离开页面

- 请求路径：/vip/deleteVipOrderRedis

- 请求方法：get

- 请求参数

  | 参数名  | 参数说明 | 备注 |
  | ------- | -------- | ---- |
  | OdDelid | 订单编号 |      |
  | UId     | 用户ID   |      |

- 响应数据说明
  - ​	在redis中把订单信息删掉，并返回给前端字符串“deleteSuccess”

### 1.4.5.选择支付宝方式支付

- 请求路径：/vippay/alipay/submit

- 请求方法：get

- 请求参数

  | 参数名 | 参数说明 | 备注 |
  | ------ | -------- | ---- |
  | Delid  | 订单编号 |      |
  | UId    | 用户ID   |      |

- 响应数据说明
  - 跳转到支付宝自己的网页去支付

### 1.4.6.选择微信方式支付

- 请求路径：/vippay/wx/submit

- 请求方法：get

- 请求参数

  | 参数名 | 参数说明 | 备注 |
  | ------ | -------- | ---- |
  | Delid  | 订单编号 |      |
  | UId    | 用户ID   |      |

- 响应数据说明
  - 前端接收code_url并转成微信支付二维码即可

### 1.4.7.微信支付监听接口

- 请求路径：/vippay/wx/pay/status

- 请求方法：get

- 请求参数

  | 参数名 | 参数说明 | 备注 |
  | ------ | -------- | ---- |
  | Delid  | 订单编号 |      |

- 响应参数说明

  - 每三秒监听一次这个接口，当返回0时代表还没支付完，当返回1时代表支付成功，前端处理跳转页面

  

