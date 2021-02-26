$(function () {
    // 点击登录按钮判断账号密码(手机号验证码)是否为空
    $("#check").click(function () {
        // 当前活动tab默认tab1为账号密码页
        if ($(".active:eq(1)").attr("id") === "tab1") {
            //账号是否有值
            if ($("#exampleInputEmail").val()) {
                //账号错误提示
                $("#emailError").text("");
                //密码是否存在
                if (!($("#exampleInputPassword").val())) {
                    //密码为空发生提示信息
                    $("#passwordError").text("密码不能为空").css("color", "red");
                } else {
                    //密码不为空消除提示
                    $("#passwordError").text("");
                }
            } else {
                // 账号为空提示信息
                $("#emailError").text("账号不能为空").css("color", "red");
            }
            // 当前活动tab默认tab2为手机号验证码页（结构与上同步）
        } else {
            if ($("#exampleInputPhone").val()) {
                $("#phoneError").text("");
                if (!($("#exampleInputCheck").val())) {
                    $("#checkError").text("验证码不能为空").css("color", "red");
                } else {
                    $("#checkError").text("");
                }
            } else {
                $("#phoneError").text("手机号码不能为空").css("color", "red");

            }
        }
    });
});

//滑动验证传的值
var sessionID;
//标签页
var tabId = 1;

// 验证码计时器
function throttle(fn, delay) {
    let canUse = true;
    return function () {
        if (canUse) {
            fn.apply(this, arguments);
            canUse = false;
            setTimeout(() => canUse = true, delay);
        }
    };
}


$(function () {

    var nc_token = ["FFFF0N00000000009889", (new Date()).getTime(), Math.random()].join(":");
    var NC_Opt =
        {
            renderTo: "#your-dom-id",
            appkey: "FFFF0N00000000009889",
            scene: "nc_login",
            token: nc_token,
            customWidth: 300,
            trans: {"key1": "code0"},
            elementID: ["usernameID"],
            is_Opt: 0,
            language: "cn",
            isEnabled: true,
            timeout: 3000,
            times: 5,
            apimap: {},

            callback: function (data) {
                /*window.console && console.log(nc_token)
                window.console && console.log(data.csessionid)
                window.console && console.log(data.sig)*/
                var sessionId = data.csessionid + "," + data.sig + "," + nc_token + ",nc_activity";
                $("#sessionId").val(sessionId);
                sessionID = sessionId;
            }
        };
    var nc = new noCaptcha(NC_Opt);
    nc.upLang("cn", {
        _startTEXT: "请按住滑块，拖动到最右边",
        _yesTEXT: "验证通过",
        _error300: "哎呀，出错了，点击<a href=\"javascript:__nc.reset()\">刷新</a>再来一次",
        _errorNetwork: "网络不给力，请<a href=\"javascript:__nc.reset()\">点击刷新</a>",

    });

    //form转换成对象方法，获取一个表单的整个数据结构，用户名密码滑动解锁等等
    $.fn.parseForm = function () {
        var serializeObj = {};
        var array = this.serializeArray();
        var str = this.serialize();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        return serializeObj;
    };

    $(document).ready(function () {
        //验证码发送ajax
        let yzm = () => {
            if ($("#exampleInputPhone").val()) {
                console.log("执行了");
                $.ajax({
                    type: "POST",   //提交的方法
                    url: "/sendCode", //验证码接口
                    data: {number: $("#exampleInputPhone").val(), type: "phone"},
                    success: function (data) {  //成功
                        if (data == 1) {
                            Swal.fire("验证码发送成功");
                        }

                    },
                    error: function (data) {  //失败的话
                        if (data == 0) {
                            Swal.fire("验证码发送失败");
                            //window.location.reload();
                        }

                    }
                });
            } else {
                Swal.fire("请输入手机号");
            }
        };
        const throttled = throttle(yzm, 5000);

        $("#sendCheck").click(function (event) {
            getCode(event);
            throttled();
            event.preventDefault(); //阻止冒泡
        });
        $("#check").click(function () {
            md();
            if ($(".active:eq(1)").attr("id") === "tab1") {
                tabId = 1;
            } else {
                tabId = 2;
            }
            // 账号密码ajax提交
            if (tabId === 1) {
                if ($("#exampleInputEmail").val() && $("#exampleInputPassword").val()) {
                    // fm1表单数据的获取
                    let obj = $(`#fm${tabId}`).parseForm();
                    //判断账号的状态是否是手机号 决定choose的值 0为账号，1为手机
                    let phone = $("#exampleInputEmail").val();
                    let phoneRegExp = /0?(13|14|15|18|17)[0-9]{9}/;
                    let ok = phoneRegExp.test(phone);
                    if (ok) {
                        obj.choose = 1;
                    } else {
                        obj.choose = 0;
                    }
                    //滑动解锁的值
                    obj.sessionId = `${sessionID}`;
                    obj.loginType = "UserRealm";
                    obj.rememberMe = true;
                    console.log(obj);
                    $.ajax({
                        type: "POST",   //提交的方法
                        url: "/user/namelogin", //提交的地址
                        data: obj,
                        success: function (userId) {  //成功
                            if (userId === "") {
                                Swal.fire({
                                    title: '账号或密码错误',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '确定',
                                }).then(function () {
                                    window.location.reload();
                                });

                            } else {
                                $.cookie("userId", userId, {expires: 7});
                                console.log(userId);
                                Swal.fire({
                                    title: '登录成功',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '确定',
                                }).then(function () {
                                    window.location.href = "index.html"
                                });




                            }//就将返回的数据显示出来

                        },
                        error: function (request) {  //失败的话
                            Swal.fire("登录失败");
                            window.location.reload();
                        }
                    });
                }
                //手机验证码ajax提交
            } else {
                if ($("#exampleInputPhone").val() && $("#exampleInputCheck").val()) {
                    // fm2表单的数据的获取
                    let obj = $(`#fm${tabId}`).parseForm();
                    obj.choose = 1;
                    obj.loginType = "UserRealm";
                    obj.rememberMe = true;
                    console.log(obj);
                    $.ajax({
                        type: "POST",   //提交的方法
                        url: "/user/phoneLogin", //提交的地址
                        data: obj,
                        success: function (userId) {  //成功
                            if (userId === "") {
                                Swal.fire({
                                    title: '手机号或验证码错误',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '确定',
                                }).then(function () {
                                    window.location.reload();
                                });
                            } else {
                                Swal.fire("登录成功"); //就将返回的数据显示出来
                                $.cookie("userId", userId, {expires: 7});
                                console.log(userId);
                                window.location.href = "index.html";
                            }
                        },
                        error: function (request) {  //失败的话
                            Swal.fire("登录失败");
                            window.location.reload();
                        }
                    });
                }
            }

        });
    });
});


/*获取验证码*/
var isPhone = 1;

function getCode(event) {
    if (isPhone) {
        resetCode(event); //倒计时
    } else {
        $("#phone").focus();
    }
}

// 验证码计时器的时间
function resetCode(event) {
    $("#sendCheck").hide();
    $("#J_second").html("60");
    $("#J_resetCode").show();
    var second = 60;
    var timer = null;
    timer = setInterval(function () {
        second -= 1;
        if (second > 0) {
            $("#J_second").html(second);
        } else {
            clearInterval(timer);
            $("#sendCheck").show();
            $("#J_resetCode").hide();
        }
    }, 1000);
}