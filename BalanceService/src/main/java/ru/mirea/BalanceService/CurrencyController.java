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

@Controller
@RequestMapping("user/currency")
public class CurrencyController {

    private final Log log = LogFactory.getLog(CurrencyController.class);

    private final CurrencyService cs;

    @Autowired
    public CurrencyController(CurrencyService cs) {
        this.cs = cs;
    }

    /**
     * Получает все записи из БД по ценам валют.
     */
    @GetMapping
    public ResponseEntity<Collection<CurrencyConvert>> show() {
        return ResponseEntity.ok(
                cs.getAll()
        );
    }

    /**
     * Пользователь хочет купить валюту.
     * @param token Токен пользователя, который хочет купить валюту.
     * @param from Валюта, которую он готов потратить.
     * @param to Валюта, которую пользователь хочет купить.
     * @param count Количество минимальных единиц* валюты, которую пользователь хочет купить.
     *              * - копейки, центы и другие.
     * @return True, если транзакция выполнена. Иначе - False.
     */
    @RequestMapping (value = "/buy_currency", method = GET)
    @ResponseBody
    public ResponseEntity<Boolean> buyCurrency(
            @RequestHeader("token") String token,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam long count
    ) {
        log.info("User buy currency: " + from + to + count);
        long user_id = TokenFactory.decoderToken(token).getId();
        return cs.buyCurrency(user_id, from, new Money(count, to)) ?
                ResponseEntity.ok(true)
                : ResponseEntity.ok(false);
    }
}
