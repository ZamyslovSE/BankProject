package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
@ComponentScan("web.config")
@Component
public class SpringJdbcConfig {

    @Bean
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC")
                .username("root")
                .password("root").build();
//        return DataSourceBuilder.create()
//                .driverClassName("com.mysql.jdbc.Driver")
//                .url("jdbc:mysql://ec2-18-223-2-140.us-east-2.compute.amazonaws.com:3306/BankDBTest?serverTimezone=UTC")
//                .username("bank_server")
//                .password("interiorlikesuicidewristred").build();
    }
}