package com.qa.data.visualization.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryThird",
        transactionManagerRef = "transactionManagerThird",
        basePackages = {"com.qa.data.visualization.repositories.callcenter"})
//设置Repository所在位置
public class ThirdConfig {

    @Autowired
    @Qualifier("thirdDataSource")
    private DataSource thirdDataSource;
    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "entityManagerThird")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryThird(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryThird")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryThird(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(thirdDataSource)
                .properties(getVendorProperties(thirdDataSource))
                .packages("com.qa.data.visualization.entities.callcenter") //设置实体类所在位置
                .persistenceUnit("thirdPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerThird")
    PlatformTransactionManager transactionManagerThird(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryThird(builder).getObject());
    }

}