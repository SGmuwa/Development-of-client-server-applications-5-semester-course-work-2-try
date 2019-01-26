package ru.mirea.BalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.mirea.Tokens.TokenFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Collection;
import java.util.Enumeration;

@Controller
public class BalanceController {

    @Autowired
    private BalanceDbConnection balDbCon;

    /**
     * Пополнение баланса пользователя.
     * @param token Токен пользователя, к которому надо присвоить баланс.
     * @param curName Какая валюта у пользователя?
     * @param plus Какой баланс ему необходимо присвоить.
     * @return ok, если операция прошла успешно. Иначе - not found.
     */
    @RequestMapping(value = "user/balance/{curName}/{plus}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity putBalance(@RequestHeader("token") String token, @PathVariable String curName, @PathVariable long plus) {
        int user_id = TokenFactory.decoderToken(token).getId();
        balDbCon.updateBalance(user_id, curName, plus);
        return ResponseEntity.ok().build();
    }

    /**
     * Узнаём все кошельки текущего пользователя.
     * @param token Токен, в котором хранится идентификатор пользователя.
     * @return Здесь расписаны все кошельки пользователя.
     */
    @RequestMapping (value = "user/balance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Collection<Money>> getBalance(@RequestHeader("token") String token) {
        int user_id = TokenFactory.decoderToken(token).getId();
        return ResponseEntity.ok(balDbCon.getBalance(user_id));
    }

    /**
     * Пользователь хочет купить валюту.
     * @param user_id
     * @param change_currency
     * @return
     */
    @RequestMapping (value = "user/balance/change_currency/{user_id}/{change_currency}", method = RequestMethod.PUT)
    @ResponseBody
    public String changeCurrency(@PathVariable int user_id,@PathVariable String change_currency) {
        return balDbCon.changeCurrency(user_id, change_currency);
    }

    @RequestMapping (value = "user/currency/{user_id}", method = RequestMethod.GET)
    @ResponseBody
    public String getCurrency(@PathVariable int user_id){return balDbCon.getCurrency(user_id);}

    //Вычитание баланса
    @RequestMapping (value = "admin/update/balance/{id}/{balance}", method = RequestMethod.POST)
    @ResponseBody
    public String updateBalance2(@PathVariable int id,@PathVariable double balance){return balDbCon.updateBalance2(id,balance);}

}
