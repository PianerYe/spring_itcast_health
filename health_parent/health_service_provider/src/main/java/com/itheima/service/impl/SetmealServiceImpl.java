package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.SetmealDao;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 套餐服务
 * */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    //新增套餐信息，同时需要关联检查组
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //新增套餐，操作t_setmeal表
        setmealDao.add(setmeal);
        //设置套餐和检查组的多对多的关联关系，操作t_setmeal_checkgroup表
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
    }

    //建立套餐和检查组多对多关系(抽取方法)
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds){
        if (checkgroupIds!=null && checkgroupIds.length > 0){
            for (Integer checkGroupId : checkgroupIds){
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkGroupId",checkGroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }
}
