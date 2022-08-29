package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);

    public void setSetmealAndCheckGroup(Map<String, Integer> map);

    public Page<Setmeal> selectByCondition(String queryString);

    public Setmeal findById(Integer id);

    public List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    public void edit(Setmeal setmeal);

    public void deleteAssoication(Integer setmealId);
    public void deleteById(Integer id);

    public Long findCountBySetmealId(Integer id);

    public List<Setmeal> findAll();

    public Setmeal findByIdOfDetails(Integer id);

}
