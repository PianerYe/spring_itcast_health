package com.itheima.service;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SpringSecurityUserService implements UserDetailsService {
    //根据用户名查询用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("用户输入的用户名为："+ username);
        return null;
    }
}
