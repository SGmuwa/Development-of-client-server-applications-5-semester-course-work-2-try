package ru.mirea.BalanceService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RunTest {

    @Autowired
    ServiceForBalance serviceForBalance;

    @Test
    public void startTest() {
        serviceForBalance.getBalance(0);
    }
}