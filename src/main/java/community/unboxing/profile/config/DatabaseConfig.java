package community.unboxing.profile.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dataSourceUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    // @Bean
    // public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
    //     LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    //     entityManagerFactoryBean.setDataSource(dataSource);

    //     Properties jpaProperties = new Properties();
    //     jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    //     jpaProperties.put("show_sql", true);
    //     entityManagerFactoryBean.setJpaProperties(jpaProperties);

    //     entityManagerFactoryBean.afterPropertiesSet();
    //     return entityManagerFactoryBean.getObject();
    // }
}

