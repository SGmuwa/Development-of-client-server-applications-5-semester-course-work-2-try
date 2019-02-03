package ru.mirea.BalanceService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import ru.mirea.defaultFilter.DefaultFilter;

@ServletComponentScan
@SpringBootApplication
@EnableConfigurationProperties
public class BalanceApplication {
    public static void main(String[] arg){
        SpringApplication.run(BalanceApplication.class);
    }

    @Bean
    public DefaultFilter defaultFilter() {
        return new DefaultFilter();
    }
}
