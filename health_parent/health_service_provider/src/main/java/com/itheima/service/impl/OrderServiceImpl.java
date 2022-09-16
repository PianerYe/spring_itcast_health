package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 *体检预约服务
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    //体检预约
    @Override
    public Result order(Map map) throws Exception {
        //1.检查用户所选的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String)map.get("OrderDate");//预约日期
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null){
            //指定日期没有进行预约设置，无法完成预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2.检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        //3.检查用户是否重复预约（同一个用户同一天预约了同一个套餐），如果重复预约则无法完成预约
        //4.检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        //5.预约成功，更新当日的已预约人数
        return null;
    }
}
