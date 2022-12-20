package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表操作
 * */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;

    //会员数量折线图数据
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() throws Exception {
        Map<String,Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();//获得日历对象。模拟时间就是当前时间
        //计算过去一年12个月
        calendar.add(Calendar.MONDAY,-12);//获得当前时间往前推12个月
        for (int i=0;i<12;i++){
            calendar.add(Calendar.MONDAY,1);//获得当前时间往后推一个月
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months",months);

        List<Integer> memberCount = memberService.findMemberCountByMonths(months);
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    //套餐预约饼形图
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        //使用模拟数据测试使用什么样的java对象转为饼形图所需的json数据格式
        Map<String,Object> data = new HashMap<>();
//        List<String> setmealNames = new ArrayList<>();
//        setmealNames.add("体检套餐");
//        setmealNames.add("孕前检查套餐");
//        data.put("setmealNames",setmealNames);
        try {
            List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
            data.put("setmealCount",setmealCount);
            /*List<String> setmealNames = new ArrayList<>();
            for (Map<String,Object> map: setmealCount) {
                String name = (String) map.get("name");//套餐名称
                setmealNames.add(name);
            }
            data.put("setmealNames",setmealNames);*/
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,data);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }

    /**
     * 获取运营数据统计
     * */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            /**
             *                     reportDate:null,
             *                     todayNewMember :0,
             *                     totalMember :0,
             *                     thisWeekNewMember :0,
             *                     thisMonthNewMember :0,
             *                     todayOrderNumber :0,
             *                     todayVisitsNumber :0,
             *                     thisWeekOrderNumber :0,
             *                     thisWeekVisitsNumber :0,
             *                     thisMonthOrderNumber :0,
             *                     thisMonthVisitsNumber :0,
             *                     hotSetmeal :[
             *                         {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
             *                         {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
             *                     ]
             *
             * */
            //使用模拟数据测试流程是否可以正常展示
            Map<String,Object> data = new HashMap<>();
            data.put("reportDate",100);
            data.put("todayNewMember",20000);
            data.put("totalMember",300);
            data.put("thisWeekNewMember",500);
            data.put("todayOrderNumber",150);
            data.put("todayVisitsNumber",100);
            data.put("thisWeekOrderNumber",300);
            data.put("thisWeekVisitsNumber",280);
            data.put("thisMonthOrderNumber",600);
            List<Map<String,Object>> hotSetmeal = new ArrayList<>();
            Map<String,Object> map1 = new HashMap<>();
            map1.put("name","阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐");
            map1.put("setmeal_count",300);
            map1.put("proportion",0.5);

            Map<String,Object> map2 = new HashMap<>();
            map2.put("name","阳光爸妈升级肿瘤12项筛查体检套餐");
            map2.put("setmeal_count",100);
            map2.put("proportion",0.2);

            hotSetmeal.add(map1);
            hotSetmeal.add(map2);

            data.put("hotSetmeal",hotSetmeal);

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,data);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


}
