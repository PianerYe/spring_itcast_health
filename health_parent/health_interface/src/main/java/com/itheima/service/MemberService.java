package com.itheima.service;

import com.itheima.pojo.Member;

public interface MemberService {
    //根据手机号查询会员
    public Member findByTelephone(String telephone);

    public void add(Member member);
}
