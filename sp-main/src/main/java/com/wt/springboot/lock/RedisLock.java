package com.wt.springboot.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * redis 分布式锁 <br>
 *
 * @className: RedisLock
 * @package: com.wt.springboot.lock
 * @author: wangtong
 * @date: 2021/8/19 4:30 pm
 **/

@Component
public class RedisLock {

    @Autowired
    JedisCluster jedisCluster;


    public RedisLock(){}

    public boolean tryLock(String lockKey, String clientId, long expire){
//        String result = this.jedisCluster.set(lockKey,clientId,"NX","PX",expire);
        //版本升级 换种写法，待验证
        SetParams setParams = new SetParams();
        setParams.px(expire);
        String result = this.jedisCluster.set(lockKey, clientId, setParams);

        return "OK".equals(result);
    }

    public boolean releaseLock(String lockKey, String clientId){
        String script ="if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        Object result  = this.jedisCluster.eval(script, Collections.singletonList(lockKey),Collections.singletonList(clientId));
        return LockConstans.RELEASE_SUCCESS.equals(result);
    }

    public boolean existLock(String lockKey){
        return  this.jedisCluster.exists(lockKey);
    }
}
