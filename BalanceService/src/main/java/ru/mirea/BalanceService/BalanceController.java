package ru.mirea.BalanceService;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.mirea.Tokens.TokenFactory;

import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class BalanceController {

    private final BalanceService bs;

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    public BalanceController(BalanceService bs) {
        log.info("init. BalanceController.");
        this.bs = bs;
    }

    /**
     * Узнаём все кошельки текущего пользователя.
     * @param token Токен, в котором хранится идентификатор пользователя.
     * @return Здесь расписаны все кошельки пользователя.
     */
    @RequestMapping (value = "user", method = GET)
    @ResponseBody
    public ResponseEntity<Collection<Money>> getBalance(@RequestHeader("token") String token) {
        log.info("get user about");
        int user_id = TokenFactory.decoderToken(token).getId();
        User user = bs.getUserInfo(user_id);
        if(user == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(user.getBalance());
    }

    /**
     * Узнаём все кошельки всех пользователей.
     * @return Здесь расписаны все кошельки пользователя.
     */
    @RequestMapping (value = "admin", method = GET)
    @ResponseBody
    public ResponseEntity<Collection<User>> getBalance() {
        log.info("get users about");
        return ResponseEntity.ok(bs.getUserInfo());
    }

    /**
     * Пользователь хочет купить валюту.
     * @param token Токен пользователя, который хочет купить валюту.
     * @param currencyFrom Валюта, которую он готов потратить.
     * @param currencyTo Валюта, которую пользователь хочет купить.
     * @param count Количество минимальных единиц* валюты, которую пользователь хочет купить.
     *              * - копейки, центы и другие.
     * @return True, если транзакция выполнена. Иначе - False.
     */
    @RequestMapping (value = "user/buy_currency/{currencyFrom}/{currencyTo}/{count}", method = GET)
    @ResponseBody
    public ResponseEntity<Boolean> buyCurrency(
            @RequestHeader("token") String token,
            @PathVariable String currencyFrom,
            @PathVariable String currencyTo,
            @PathVariable long count
    ) {
        log.info("User buy currency: " + currencyFrom + currencyTo + count);
        int user_id = TokenFactory.decoderToken(token).getId();
        return bs.buyCurrency(user_id, currencyFrom, new Money(count, currencyTo)) ?
                ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    /**
     * Изменение баланса
     * @param user Пользователь, у которого надо обновить баланс.
     * @return ok
     */
    @RequestMapping (value = "admin/user", method = POST)
    @ResponseBody
    public ResponseEntity updateBalance(@RequestBody User user) {
        log.info("admin/user");
        bs.updateOrAddUser(user);
        return ResponseEntity.ok().build();
    }

    /**
     * Проверка баланса.
     * @param user_id Пользователь, у которого надо проверить баланс.
     * @return ok
     */
    @RequestMapping (value = "admin/{user_id}", method = GET)
    @ResponseBody
    public ResponseEntity<Collection<Money>> getBalance(@PathVariable long user_id) {
        log.info("admin/" + user_id);
        User user = bs.getUserInfo(user_id);
        if(user == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(user.getBalance());
    }
}
