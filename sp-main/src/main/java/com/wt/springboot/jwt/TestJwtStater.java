package com.wt.springboot.jwt;

import com.wttest.starter.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试 Springboot-Starter-JWT
 *
 * @ className: TestJwtStater
 * @ package: com.wt.springboot.jwt
 * @ author: wangtong
 * @ date: 2021/11/29 3:19 pm
 **/

public class TestJwtStater {

    @Autowired
    JwtService jwtService;

    /**
     * @Author wangtong
     * @Date 3:24 pm 2021/11/29
     * @Param
     * @return
     **/
    @RequestMapping(value = "/getAllSysUser", method = RequestMethod.GET)
    public String getAllSysUser() {
        String reuslt = "";
        Map map = new HashMap();
        map.put("user", "赵刚");
//        System.out.println(jwtService.createPersonToken(map, "1402753117", 200));
        return reuslt;
    }

    public static void main(String[] args){
        TestJwtStater stater = new TestJwtStater();
        stater.getAllSysUser();
    }
}
