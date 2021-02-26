package com.bjf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * \* Description:
 * \* User: mortal
 * \* Date: 2020/9/24
 * \* Time: 13:40
 * \
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public String index() {
        return "mycart";
    }

    @RequestMapping("trade")
    public String trade() {
        return "trade";
    }

    @RequestMapping("payment")
    public String payment() {
        return "payment";
    }

    @RequestMapping("WePay")
    public String WePay() {
        return "WePay";
    }

    @RequestMapping("ok")
    public String ok() {
        return "ok";
    }

}
