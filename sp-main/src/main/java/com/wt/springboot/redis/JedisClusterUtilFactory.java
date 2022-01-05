package com.wt.springboot.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JedisCLuster 初始化 <br>
 *
 * @className: JedisClusterUtilFactory
 * @package: com.wt.springboot.redis
 * @author: wangtong
 * @date: 2021/11/30 3:19 pm
 **/
@Component
public class JedisClusterUtilFactory implements FactoryBean<JedisCluster>, InitializingBean {

    @Autowired
    private RedisProperties properties;

    private Resource resource;
    private JedisCluster jedisCluster;
//    private GenericObjectPoolConfig poolConfig;

    @Override
    public JedisCluster getObject() throws Exception{
        return jedisCluster;
    }

    @Override
    public Class<? extends JedisCluster> getObjectType(){
        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
    }

    @Override
    public boolean isSingleton(){
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort();
//        poolConfig = new JedisPoolConfig();
//        jedisCluster = new JedisCluster(haps,
//                (new Long(properties.getTimeout().getSeconds()).intValue())*1000,
//                300000,
//                ((int)properties.getCluster().getMaxRedirects()),
//                properties.getPassword(),
//                poolConfig
//        );
        GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
        jedisCluster = new JedisCluster(haps,
                (new Long(properties.getTimeout().getSeconds()).intValue())*1000,
                300000,
                6,
                 properties.getPassword(),
                "",
                poolConfig
        );

    }

    /**
     * @Author wangtong
     * @Description  reids 集群地址配置
     * @Date 3:27 pm 2021/11/30
     * @Param
     * @return  Set<HostAndPort>
     **/
    private Set<HostAndPort> parseHostAndPort(){
        List<String> serverArray = properties.getCluster().getNodes();
        Set<HostAndPort> nodes = new HashSet<>();

        for(String ipPort : serverArray){
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.parseInt(ipPortPair[1].trim())));
        }
        return nodes;
    }
}
