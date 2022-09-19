package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);
    public void editNumberByOrderDate(OrderSetting orderSetting);
    public void editReservationsByOrderDate(OrderSetting orderSetting);
    public long findCountByOrderDate(Date orderDate);
    public List<OrderSetting> getOrderSettingByMonth(String date);
    public OrderSetting findByOrderDate(Date orderDate);
}
