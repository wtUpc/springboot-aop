package com.wt.springboot.lock;

/**
 * 〈功能概述〉<br>
 *
 * @className: LockConstans
 * @package: com.wt.springboot.lock
 * @author: wangtong
 * @date: 2021/8/19 4:43 pm
 **/

public class LockConstans {

    public static final String LOCK_SUCCESS = "OK";
    public static final Long RELEASE_SUCCESS = 1L;
    public static final String SET_IF_NOT_EXIST = "NX";
    public static final String SET_WITH_EXPIRE_TIME = "PX";

    public LockConstans(){}
}
