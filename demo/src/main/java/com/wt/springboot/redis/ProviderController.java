package com.wt.springboot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * 〈功能概述〉<br>
 *
 * @className: ProviderController
 * @package: com.wt.springboot.redis
 * @author: wangtong
 * @date: 2021/9/11 11:39 pm
 **/

@Controller
public class ProviderController {

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @RequestMapping("/provider")
    @ResponseBody
    public Map<String, Object> provider(){
        System.out.println("welcome to provider");

        redisCacheUtil.put("testkey2","12312312");
        return null;
    }

    public static void main(String[] args){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.set("Jedis", "Hello Work!");
        System.out.println(jedis.get("Jedis"));
        jedis.close();

    }

}
