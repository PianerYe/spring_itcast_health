package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    public void add(Setmeal setmeal, Integer[] checkgroupIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public Setmeal findById(Integer id);

    public List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    public void edit(Setmeal setmeal, Integer[] checkgroupIds);

    public void deleteById(Integer id);

    public List<Setmeal> findAll();

    public Setmeal findByIdOfDetails(Integer id);

    public List<Map<String, Object>> findSetmealCount();
}
