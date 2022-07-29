package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐服务
 * */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    //新增套餐信息，同时需要关联检查组
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //新增套餐，操作t_setmeal表
        setmealDao.add(setmeal);
        //设置套餐和检查组的多对多的关联关系，操作t_setmeal_checkgroup表
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
        //将图片名称保存到Redis集合中
        savePic2Redis(setmeal.getImg());
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_checkgroup limit 0,10
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();
        return new PageResult(total,rows);
    }

    //根据ID查询套餐
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    //编辑套餐
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //编辑套餐，操作t_setmeal表
        setmealDao.edit(setmeal);
        Integer setmealId = setmeal.getId();
        //清除关系表
        setmealDao.deleteAssoication(setmealId);
        //设置套餐和检查组的多对多的关联关系，操作t_setmeal_checkgroup表
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
    }

    //根据套餐ID，删除套餐
    @Override
    public void deleteById(Integer id) {
        //判断当前检查组是否已经关联到套餐
        Long count = setmealDao.findCountBySetmealId(id);
        if (count>0){
            //当前检查组已经关联到套餐，不允许删除
            throw new RuntimeException(MessageConstant.DELETE_CHECKGROUP_FAIL);
        }else {
            setmealDao.deleteById(id);
        }

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

    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }
}
