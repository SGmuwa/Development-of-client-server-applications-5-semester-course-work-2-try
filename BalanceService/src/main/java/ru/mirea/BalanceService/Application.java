package ru.mirea.BalanceService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

@ServletComponentScan
@SpringBootApplication
public class Application {
    public static void main(String[] arg){
        SpringApplication.run(Application.class);
    }
}
