package com.alamin.emi.config;

import java.util.Properties;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alamin.emi.interceptors.MyCustomInterceptor;
import com.alamin.emi.utils.EncAndDecAlgorithm;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.*")
@EnableJpaRepositories(basePackages= "com.alamin.emi.repository", entityManagerFactoryRef="priEntityManagerFactory")
public class BeanConfiguration implements WebMvcConfigurer {
	@Autowired
	Environment env;
	@Autowired
	EncAndDecAlgorithm crypto;

	@Value("${hikari.isolation.level:TRANSACTION_REPEATABLE_READ}")
	private String isolationLevel;
	@Autowired
	private MyCustomInterceptor myCustomInterceptor;


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(myCustomInterceptor).addPathPatterns("/**")
		.excludePathPatterns(
				
				//swagger related
				"/error/**","/swagger-ui.html","/v2/api-docs","/swagger-resources/**","/webjars/**",
				"/swagger.json","/webjars/springfox-swagger-ui/**","/swagger-resources/configuration/ui",
				"/swagger-resources/configuration/security","/swagger-resources","/csrf",
				
				//emi-api's
				"/add-product","/list-product","/edit-product/*","/delete-product/*","/view-product/*","/transaction");
	}

	@Bean(name = "dataSource")
	@Primary
	public DataSource getHikariConnectionPool() throws Exception {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(env.getProperty("db.url"));
		config.setUsername(env.getProperty("db.username"));
		config.setPassword(crypto.decrypt(env.getProperty("db.password").trim()));
		config.setDriverClassName(env.getProperty("db.driver"));

		config.setMinimumIdle(Integer.parseInt(env.getProperty("hikari.minimumIdel").trim()));
		config.setMaximumPoolSize(Integer.parseInt(env.getProperty("hikari.maxPoolSize").trim()));
		config.setConnectionTimeout(Integer.parseInt(env.getProperty("hikari.connectionTimeout").trim()));
		config.setIdleTimeout(Integer.parseInt(env.getProperty("hikari.idleTimeOut").trim()));
		config.setMaxLifetime(Integer.parseInt(env.getProperty("hikari.maxLifetime").trim()));
		config.setKeepaliveTime(Integer.parseInt(env.getProperty("hikari.keepaliveTime").trim()));
		config.setTransactionIsolation(isolationLevel);

		config.setConnectionTestQuery("SELECT 1");
		config.setPoolName("emiConnectionPool");

		return new HikariDataSource(config);

	}

	@Bean(name = "priEntityManagerFactory")
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setDataSource(dataSource);

		// Classpath scanning of @Component, @Service, etc annotated class
		entityManagerFactory.setPackagesToScan("com.alamin.emi.entities");

		// Vendor adapter
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

		// Hibernate properties
		Properties additionalProperties = new Properties();
		additionalProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		additionalProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		additionalProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		additionalProperties.put("connection.autoReconnectForPools", true);
		additionalProperties.put("connection.autoReconnect", true);
		additionalProperties.put("hibernate.enable_lazy_load_no_trans", true);
		entityManagerFactory.setPersistenceUnitName("primary");
		entityManagerFactory.setJpaProperties(additionalProperties);
		return entityManagerFactory;
	}

	/**
	 * Declare the transaction manager.
	 */
	@Bean(name = "transactionManager")
	@Primary
	public PlatformTransactionManager transactionManager(
			@Qualifier("priEntityManagerFactory") LocalContainerEntityManagerFactoryBean priEntityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(priEntityManagerFactory.getObject());
		return transactionManager;
	}

	/**
	 * PersistenceExceptionTranslationPostProcessor is a bean post processor which
	 * adds an advisor to any bean annotated with Repository so that any
	 * platform-specific exceptions are caught and then rethrown as one Spring's
	 * unchecked data access exceptions (i.e. a subclass of DataAccessException).
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	@Bean
	public Validator validator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator;
	}

}
