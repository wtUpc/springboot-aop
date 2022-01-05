package com.wt.springboot.aop;

import com.wt.springboot.annotation.NoRepeatSubmit;
import com.wt.springboot.lock.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 *  利用redis分布式锁解决接口幂等性校验
 *  接收请求钱先进行上锁，获得锁后继续执行，请求结束后释放锁（为防止死锁，
 *  对锁进行过期时间处理）<br>
 *
 *  接口幂等产生原因： rpc调用网络延迟（重试发送请求）
 *
 *  调用方式： 在Controller接口上添加注解
 *  参数说明： 锁失效时间 lockTime 可空  默认600秒
 *  使用样例：
 * @NoRepeatSubmit(lockTime=500)
 * public String test(HttpServletRequest request){
 *     return "test001";
 * }
 *
 * @className: RepeatSumbmitAspect
 * @package: com.wt.springboot.aop
 * @author: wangtong
 * @date: 2021/8/19 4:01 pm
 **/
@Aspect
@Component
public class RepeatSubmitAspect {

    @Autowired
    private RedisLock redisLock;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void poinCut(NoRepeatSubmit noRepeatSubmit){

    }

    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit)
            throws Throwable {
        //获取request对象
        ServletRequestAttributes ra = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ra.getRequest();

        //获取注解中的锁失效时间
        long lockSeconds = noRepeatSubmit.lockTime();
        String clientId = UUID.randomUUID().toString();
        String jsessionId = request.getSession().getId();
        String path = request.getServletPath();
        String key = jsessionId + path;

        //尝试获取锁
        boolean isSuccess = redisLock.tryLock(key,clientId,lockSeconds);
        if(isSuccess){
            Object result;
            try {
                //执行Controller的业务逻辑
                result = pjp.proceed();
            }finally {
                //释放锁
                redisLock.releaseLock(key,clientId);
            }
            return result;
        }else {
            return new Exception("不允许重复提交");
        }
    }
}
