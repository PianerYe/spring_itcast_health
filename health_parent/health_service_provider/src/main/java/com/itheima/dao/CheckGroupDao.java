package com.itheima.dao;

import com.itheima.pojo.CheckGroup;

import java.util.Map;

public interface CheckGroupDao {
    public void add(CheckGroup checkGroup);
    public void setCheckGroupAndCheckItem(Map map);
}
