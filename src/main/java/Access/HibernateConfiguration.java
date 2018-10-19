package Access;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

    @Value("${DB_URL}")
    private String db_url;
    @Value("${DB_Username}")
    private String db_username;
    @Value("${DB_Password}")
    private String db_password;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));
        sessionFactoryBean.setPackagesToScan("Model");
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", db_url);
        properties.setProperty("hibernate.connection.username", db_username);
        properties.setProperty("hibernate.connection.password", db_password);
        sessionFactoryBean.setHibernateProperties(properties);

        return sessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
