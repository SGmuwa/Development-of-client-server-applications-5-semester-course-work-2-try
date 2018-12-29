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
    public Balance getBalance(int user_id){return balanceConnect.getBalance(user_id);}
    public String updateBalance(int user_id, double bal){return balanceConnect.updateBalance(user_id,bal);}
    public String changeCurrency(int user_id, String change_currency){return balanceConnect.changeCurrency(user_id,change_currency);}

    //запретить обращаться пользователям
    public String updateBalance2(int id, double balance){return balanceConnect.updateBalance2(id,balance);}
}
