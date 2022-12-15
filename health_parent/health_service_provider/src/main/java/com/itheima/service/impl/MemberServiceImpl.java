package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 会员服务
 * */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //保存会员信息
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null){
            //使用md5将明文密码加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    //根据月份查询会员数量
    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) throws Exception {
        List<Integer> memberCount = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy.MM");
        for (String month : months){
            String date = month;
            Date parse = dateFormat1.parse(month);
            Calendar time = Calendar.getInstance();
            time.setTime(parse);
            time.add(Calendar.MONTH,1);
            time.add(Calendar.DAY_OF_YEAR,-1);
            Date parse1 = time.getTime();
            String date1 = dateFormat.format(parse1);
            Integer count = memberDao.findMemberCountBeforeDate(date1);
            memberCount.add(count);
        }
        return memberCount;
    }


}
