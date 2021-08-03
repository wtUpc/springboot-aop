package com.wt.springboot.service.impl;

import com.wt.springboot.annotation.OperationLogDetail;
import com.wt.springboot.enums.OperationType;
import com.wt.springboot.enums.OperationUnit;
import com.wt.springboot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @OperationLogDetail(detail = "通过手机号[{{tel}}]获取用户名",level = 3,operationUnit = OperationUnit.USER,operationType = OperationType.SELECT)
    @Override
    public String findUserName(String tel) {
        System.out.println("tel:" + tel);
        return "zhangsan";
    }
}
