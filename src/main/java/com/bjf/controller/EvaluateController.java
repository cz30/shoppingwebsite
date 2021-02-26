package com.bjf.controller;


import com.bjf.pojo.BjfEvaluate;
import com.bjf.pojo.vo.BjfEvaluateVo;
import com.bjf.service.BjfEvaluateService;
import com.bjf.service.BjfOrderItemService;
import com.bjf.util.ImgFileUploadUtill;
import com.bjf.util.SensitiveFilter;
import com.bjf.util.TimeChangeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/9/9
 * \* Time: 10:01
 * \* Description:评论模块
 * \
 */
@Controller
@CrossOrigin
@RequestMapping("/evaluate")
public class EvaluateController {

    @Resource
    private TimeChangeUtil timeChangeUtil;

    @Resource
    private BjfEvaluateService bjfEvaluateService;

    @Resource
    private BjfOrderItemService bjfOrderItemServicep;

    @Resource
    private SensitiveFilter sensitiveFilter;

    @Resource
    private ImgFileUploadUtill imgFileUploadUtill;

    /**
     * 用户初次进行评论，需要前端传商品id，用户id，订单编号

     */
    @RequestMapping("/toBeEvaluated")
    @ResponseBody
    public String toBeEvaluated(Integer cmdId, Integer uId,Integer oiId,String eContent,
            MultipartFile[] eImage ,Integer eGrepStatus) throws IOException {
        if(!eContent.equals("")){
            //对评价内容进行判断，判断是否有敏感字符
                String text=sensitiveFilter.filter(eContent);
                boolean status=text.contains("*#￥*#￥");
                if(status){//如果包含特殊字符，则向前端返还一个判断
                    System.out.println("包含敏感字符，请注意文明");
                    return "civilization";
                }
            }
            String str="";
       /* System.out.println(eImage.length);
        System.out.println(Arrays.toString(eImage));*/
        //!eImage[0].isEmpty()
            if(eImage.length !=0){
                if(eImage.length>1){//判断是否是多张图片，如果是多张图片就进行字符串拼接，如果不是则直接存进数据库
                    for(int i=0;i<eImage.length;i++){
                        str=str+"#"+ imgFileUploadUtill.imgUpload(eImage[i]);
                    }
                    str=str.substring(1);
                }
                else{
                    str=str+imgFileUploadUtill.imgUpload(eImage[0]);
                }
            }
            System.out.println("str=="+str);
            //获取当前系统时间
            Date date=new Date();
            BjfEvaluate bjfEvaluate=new BjfEvaluate();
            if(str==""){
                bjfEvaluate.setEContent(eContent).setEGrepStatus(eGrepStatus)
                        .setCmdId(cmdId).setUId(uId).setEUtime(date).setEId(oiId);
            }
            else{
                bjfEvaluate.setEContent(eContent).setEImage(str).setEGrepStatus(eGrepStatus)
                        .setCmdId(cmdId).setUId(uId).setEUtime(date).setEId(oiId);
            }
            bjfEvaluateService.insertSelective(bjfEvaluate);

            bjfOrderItemServicep.updateSupportByDelidAndCmdId(4,oiId);
            return "success";

    }


    /**
     * 点击去追加评论
     * @return
     */
    @RequestMapping("/additionalComments")
    @ResponseBody
    public BjfEvaluate additionalComments(Integer oiId){

        BjfEvaluate bjfEvaluate=bjfEvaluateService.selectInitialCommentsByCmdIdAndUId(oiId);
        System.out.println(bjfEvaluate);
        return bjfEvaluate;
    }

    /**
     * 追加评论提交

     * @param eUback
     * @param eBackImages
     * @return
     * @throws IOException
     */
    @RequestMapping("/additionalCommentsSubmit")
    @ResponseBody
    public String additionalCommentsSubmit(Integer oiId, String eUback,MultipartFile[] eBackImages) throws IOException {
        //System.out.println(cmdId+"==="+uId+"==="+eUback+"==="+Arrays.toString(eBackImages));
        if(!eUback.equals("")) {
            //对评价内容进行判断，判断是否有敏感字符
            String text = sensitiveFilter.filter(eUback);
            boolean status = text.contains("*#￥*#￥");
            if (status) {//如果包含特殊字符，则向前端返还一个判断
                System.out.println("包含敏感字符，请注意文明");
                return "civilization";
            }
        }

            String str = "";
        //!eBackImages[0].isEmpty()
        if(eBackImages.length !=0) {
            if (eBackImages.length > 1) {//判断是否是多张图片，如果是多张图片就进行字符串拼接，如果不是则直接存进数据库
                for (int i = 0; i < eBackImages.length; i++) {
                    str = str + "#" + imgFileUploadUtill.imgUpload(eBackImages[i]);
                }
                str = str.substring(1);
            } else {
                str = str + imgFileUploadUtill.imgUpload(eBackImages[0]);
            }
        }
            System.out.println("str==" + str);
            Date date = new Date();
            BjfEvaluate bjfEvaluate = new BjfEvaluate();
            bjfEvaluate.setEId(oiId).setEUback(eUback).setEUbackTime(date).setEBackImages(str);
            bjfEvaluateService.updateAdditionalComments(bjfEvaluate);
            bjfOrderItemServicep.updateSupportByDelidAndCmdId(5,oiId);
            return "success";

    }

    @RequestMapping("/showComments")
    @ResponseBody
    public List<BjfEvaluateVo> showComments(Integer cmdId){
        List<BjfEvaluateVo> list=bjfEvaluateService.selectEvaluateBycmdId(cmdId);
       for (int i=0;i<list.size();i++){
           BjfEvaluateVo bjfEvaluate=list.get(i);
           bjfEvaluate.setUtime(timeChangeUtil.changeTime(bjfEvaluate.getEUtime(),"yyyy-MM-dd hh:mm:ss"));
           bjfEvaluate.setMtime(timeChangeUtil.changeTime(bjfEvaluate.getEMtime(),"yyyy-MM-dd hh:mm:ss"));
           bjfEvaluate.setUbackTime(timeChangeUtil.changeTime(bjfEvaluate.getEUbackTime(),"yyyy-MM-dd hh:mm:ss"));
       }
        return list;
    }
}
