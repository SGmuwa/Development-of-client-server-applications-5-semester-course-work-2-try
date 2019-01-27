package ru.mirea.BalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceForBalance {
    private BalanceDbConnection balanceConnect;
    @Autowired
    public ServiceForBalance(BalanceDbConnection balanceConnect){
        this.balanceConnect = balanceConnect;
    }
}
