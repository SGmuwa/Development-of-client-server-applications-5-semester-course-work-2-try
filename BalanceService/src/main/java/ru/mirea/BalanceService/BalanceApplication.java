package ru.mirea.BalanceService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
@EnableConfigurationProperties
public class BalanceApplication {
    public static void main(String[] arg){
        SpringApplication.run(BalanceApplication.class);
    }
}
