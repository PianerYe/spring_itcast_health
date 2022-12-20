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


}
