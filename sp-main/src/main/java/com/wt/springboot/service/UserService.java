package com.wt.springboot.service;

public interface UserService {
    /**
     * 获取用户信息
     * @return
     * @param tel
     */
    String findUserName(String tel);
}
