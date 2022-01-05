package com.wt.springboot.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.apache.shardingsphere.driver.jdbc.adapter.AbstractDataSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;


/**
 * 动态数据源配置
 * * 使用{@link com.baomidou.dynamic.datasource.annotation.DS}注解，切换数据源
 * *
 * * <code>@DS(DataSourceConfiguration.SHARDING_DATA_SOURCE_NAME)</code>
 *
 * @className: DataSourceConfiguration
 * @package: com.wt.springboot.config
 * @author: wangtong
 * @date: 2022/1/4 5:02 pm
 **/
@Configuration
@AutoConfigureBefore({DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class})
public class DataSourceConfiguration {

    /**
     * 分片数据源名称
     *
     * */
    private static final  String SHARDING_DATA_SOURCE_NAME = "gits_sharding";

    /**
     *  动态数据源配置项
     **/
    @Autowired
    private DynamicDataSourceProperties properties;


    /**
     * shardingjdbc有四种数据源，需要根据业务注入不同的数据源
     *
     * <p>1. 未使用分片, 脱敏的名称(默认): shardingDataSource;
     * <p>2. 主从数据源: masterSlaveDataSource;
     * <p>3. 脱敏数据源：encryptDataSource;
     * <p>4. 影子数据源：shadowDataSource
     *
     */
    @Lazy
    @Resource(name = "shardingDataSource")
    AbstractDataSourceAdapter shardingDataSource;


    public DynamicDataSourceProvider dynamicDataSourceProvider(){
        Map<String , DataSourceProperty> dataSourceMap = properties.getDatasource();

        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourcePropertyMap = createDataSourceMap(dataSourceMap);
                // 将 shardingjdbc 管理的数据源也交给动态数据源管理
                dataSourcePropertyMap.put(SHARDING_DATA_SOURCE_NAME, shardingDataSource);
                return dataSourcePropertyMap;
            }
        };
    }


    /**
     * 将动态数据源设置为首选的
     * 当spring存在多个数据源时, 自动注入的是首选的对象
     * 设置为主要的数据源之后，就可以支持shardingjdbc原生的配置方式了
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider){
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setStrict(properties.getStrict());

        return dataSource;
    }
}
