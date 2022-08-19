package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 预约设置服务
 * */

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    //批量导入预约设置数据
    @Override
    public void add(List<OrderSetting> list) {
        if (list != null && list.size() > 0){
            for (OrderSetting orderSetting : list){
                //判断当前日期是否已经进行了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            //    System.out.println(countByOrderDate);
                if (countByOrderDate > 0 ){
                    //已经进行了预约设置，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //没有进行预约设置，执行插入操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据月份查询对应的预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) throws Exception{//yyyy-MM
        //SELECT * FROM t_ordersetting WHERE DATE_FORMAT(orderDate,'%Y-%m')= '2023-02'
        //日期格式转换
       /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date temp = sdf.parse(date);
        String date1 = sdf.format(temp);*/
            //调用Dao,根据日期查询预约设置数据
            List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(date);
            List<Map> result = new ArrayList<>();
            if (list!= null && list.size()>0){
                for (OrderSetting orderSetting : list){
                    Map<String,Object> m = new HashMap<>();
                    m.put("date",orderSetting.getOrderDate().getDate());//获取日期数字(几号)
                    m.put("number",orderSetting.getNumber());
                    m.put("reservations",orderSetting.getReservations());
                    result.add(m);
                }
            }
            return result;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        //根据日期查询是否已经进行了预约设置
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        if (count>0){
            //当前日期已经进行了预约设置，需要执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //没有进行预约设置，执行插入操作
            orderSettingDao.add(orderSetting);
        }
    }
}
