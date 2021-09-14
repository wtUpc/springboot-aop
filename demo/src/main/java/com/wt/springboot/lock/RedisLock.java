package com.wt.springboot.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * 〈功能概述〉<br>
 *
 * @className: RedisLock
 * @package: com.wt.springboot.lock
 * @author: wangtong
 * @date: 2021/8/19 4:30 pm
 **/

@Component
public class RedisLock {

//    @Autowired
//    JedisCluster jedisCluster;

    Jedis jedis = new Jedis();

    public RedisLock(){}

    public boolean tryLock(String lockKey, String clientId, long expire){
        String result = this.jedis.set(lockKey,clientId,"NX","PX",expire);
        return "OK".equals(result);
    }

    public boolean releaseLock(String lockKey, String clientId){
        String script ="if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        Object result  = this.jedis.eval(script, Collections.singletonList(lockKey),Collections.singletonList(clientId));
        return LockConstans.RELEASE_SUCCESS.equals(result);
    }

    public boolean existLock(String lockKey){
        return  this.jedis.exists(lockKey);
    }
}
