package com.bjf.controller;


import com.bjf.pojo.BjfCategory;
import com.bjf.pojo.BjfCommodity;
import com.bjf.pojo.BjfSlideshow;
import com.bjf.service.BjfCategoryService;
import com.bjf.service.BjfCommodityService;
import com.bjf.service.BjfMerchantService;
import com.bjf.service.BjfSlideshowService;
import com.bjf.util.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/5
 * \* Time: 15:13
 * \* Description:首页展示&商品详情页展示
 * \
 */
@RestController
@RequestMapping("/homePage")
@CrossOrigin
public class HomePageController {
    @Resource
    BjfMerchantService bjfMerchantService;

    @Resource
    BjfCategoryService bjfCategoryService;

    @Resource
    BjfSlideshowService bjfSlideshowService;

    @Resource
    BjfCommodityService bjfCommodityService;


    /**
     * 获取商家logo展示在首页
     * @return
     */
    @RequestMapping("/logo")
    public String logo(){
        String logo=bjfMerchantService.selectLogo();
        return logo;
    }

    /**
     * 首页类目
     * @return
     */
    @RequestMapping("getTree")
    public List<BjfCategory> getBjfCategoryTree(){
        List<BjfCategory> list=bjfCategoryService.getBjfCategoryTree();
        return list;
    }

    /**
     * 附加一个递归的获取树节点
     */
    public List<BjfCategory> getTree(){
//        int i = 0;
        List<BjfCategory> list = bjfCategoryService.getBjfCategoryTree();
//        首次循环获取母节点
        for(BjfCategory bjfCategory: list){
            System.out.println("start"+bjfCategory.getCgName());
            getList(bjfCategory.getNext());
        }

        return list;
    }
    public void getList(List<BjfCategory> list){

        BjfCategory bc = new BjfCategory();
        if(list != null){
            if(list.size()>0){
                for(BjfCategory bjfCategory : list){
//                    i++;
                    System.out.println(bjfCategory.getCgName());
                    bc.setCgId(bjfCategory.getCgId());
                    bc.setCgName(bjfCategory.getCgName());

                    //递归获取下一个子节点
                    getList(bjfCategory.getNext());
                }
            } else{
//                i = 0;
//                System.out.println("枝干结束"+i);
            }
        } else{
//            i = 0;
        }
    }

    /**
     * 轮播图的展示
     * @return
     */
    @RequestMapping("/slideShow")
    public List<BjfSlideshow> slideshow(){
        List<BjfSlideshow>  slideshow= bjfSlideshowService.slideshow();
        return slideshow;
    }

    /**
     * 推荐图
     *
     * @return
     */
    @RequestMapping("/recommendImage")
    private List<BjfCommodity> recommend() {
        List<BjfCommodity> recommend = bjfCommodityService.recommend();
        return recommend;
    }

    /**
     * 热销图
     *
     * @return
     */
    @RequestMapping("/hotImage")
    private List<BjfCommodity> hot() {
        List<BjfCommodity> hot = bjfCommodityService.hot();
        return hot;
    }

    /**
     * 首页商品展示
     *
     * @return
     */
    @RequestMapping("/shouzhan")
    private List<BjfCommodity> shouzhan() {
        List<BjfCommodity> shouzhan = bjfCommodityService.shouzhan();
        return shouzhan;
    }

    /**
     * 点击类目（一级或二级），展示分类的商品，并进行分页
     * @param   pageNum & pageSize    当前页码&每页数量
     * @param cgId  类目id
     * @return 返回查询的商品
     */
    @RequestMapping("findPageGetTree")
    public Object findPage(Integer pageNum, Integer cgId){
        if(pageNum==null){
            pageNum=1;
        }
        PageRequest pageQuery=new PageRequest();
        pageQuery.setPageNum(pageNum);
        //这里的pageSize到后期需要每页展示几个商品再改
        pageQuery.setPageSize(15);
        //System.out.println(pageQuery);
        //System.out.println(pageNum+"======="+cgId);
        Object object=bjfCommodityService.findPage(pageQuery,cgId);
        //model.addAttribute("fenlei",object);
        return bjfCommodityService.findPage(pageQuery,cgId);
    }

    /**
     * 商品详情页展示
     * @param cmdId
     * @return
     */
    @RequestMapping("/commodityDetails")
    @ResponseBody
    public BjfCommodity commodityDetails(Integer cmdId){
        BjfCommodity bjfCommodity = bjfCommodityService.selectCommodityDetails(cmdId);
        System.out.println(bjfCommodity);
        return bjfCommodity;
    }

}
