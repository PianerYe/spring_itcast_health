package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup,Integer[] checkitemIds);
    public PageResult pageQuery(QueryPageBean queryPageBean);
}
