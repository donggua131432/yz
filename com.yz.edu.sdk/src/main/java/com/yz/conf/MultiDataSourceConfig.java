package com.yz.conf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.yz.datasource.Global;
import com.yz.datasource.JdbcConfig;
import com.yz.datasource.SubJdbcConfig;
import com.yz.datasource.TraceDbFilter;
import com.yz.scan.CustomerScanService;
import com.yz.spi.YzSPIFactory;

@Configuration
@AutoConfigureAfter(JdbcConfig.class)
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MultiDataSourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(MultiDataSourceConfig.class);

	@Bean(name = "jdbcConfig")
	public JdbcConfig multiJdbcConfig() {
		return new JdbcConfig();
	}

	@Bean(name = "traceDbFilter")
	public TraceDbFilter traceDbFilter() {
		return new TraceDbFilter();
	}

	/**
	 * 
	 * @param global
	 * @param config
	 * @return
	 * @throws SQLException
	 */
	@Bean(name = "yzDataSource")
	public DataSource createDatasourceBean(@Autowired JdbcConfig jdbc, @Autowired TraceDbFilter traceDbFilter) {
		Global global = jdbc.getGlobal();
		SubJdbcConfig config = jdbc.getDefaultSubJdbcConfig();
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(config.getUrl());
		datasource.setDriverClassName(global.getDriverClassName());
		datasource.setUsername(global.getUser());
		datasource.setPassword(global.getPassword());
		datasource.setInitialSize(global.getPoolSize());
		datasource.setMinIdle(global.getMinIdle());
		datasource.setMaxWait(global.getMaxWait());
		datasource.setMaxActive(global.getMaxActive());
		datasource.setMinEvictableIdleTimeMillis(global.getMinEictableIdleTimeMillis());
		List<Filter> filters = new ArrayList<>();
		filters.add(traceDbFilter);
		datasource.setProxyFilters(filters);
		return datasource;
	}

	/**
	 * 
	 * @param datasource
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "yzSqlSessionFactory")
	public SqlSessionFactory getSqlSessionFactory(@Autowired DataSource datasource) throws Exception {
		CustomerScanService service = YzSPIFactory.getInstance().getExtension(CustomerScanService.class,
				"YZ_CUSTOM_SPI");
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(datasource);
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("config/mybatis-config.xml"));
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		try {
			sqlSessionFactoryBean
					.setMapperLocations(pathMatchingResourcePatternResolver.getResources(service.getMybatisScanPath()));
		} catch (IOException e) {
			logger.error("--------------------扫描mybatis映射文件失败", e);
		}
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager txManager(@Autowired DataSource ds) { 
		return new DataSourceTransactionManager(ds);
	}

	@Bean(name = "sqlSessionTemplate")
	public SqlSessionTemplate multiSqlSessionTemplate(@Autowired SqlSessionFactory sqlSessionFactory) throws Exception {
		// 默认数据源
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
		return template;
	}

}
