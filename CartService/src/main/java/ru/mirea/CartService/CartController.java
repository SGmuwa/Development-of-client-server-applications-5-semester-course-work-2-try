package ru.mirea.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.mirea.Tokens.TokenFactory;

import java.util.Collection;

@Controller
@RequestMapping("/user")
public class CartController {

    private final CartService cs;

    @Autowired
    public CartController(CartService cs) {
        this.cs = cs;
    }

    /**
     * Изменить количество товара в корзине.
     * @param token Токен, в котором есть идентификатор пользователя (корзины).
     */
    @PutMapping
    public void putItem(@RequestHeader("token") String token, @RequestBody CartElement cartElement) {
        long user_id = TokenFactory.decoderToken(token).getId();
        cs.updateOrAdd(user_id, cartElement);
    }

    /**
     * Очистить корзину или убрать один товар из корзины.
     * @param token Токен пользователя, в котором хранится его идентификатор.
     * @param item_id Идентификатор товара, который надо очистить.
     *                Если отправить NULL, будет очищена вся корзина.
     */
    @DeleteMapping
    public void remove(@RequestHeader String token, @RequestParam(required = false) Long item_id) {
        long user_id = TokenFactory.decoderToken(token).getId();
        if(item_id == null)
            cs.clear(user_id); // Пользователь хочет очистить всю корзину
        else
            cs.clear(user_id, item_id); // Пользователь хочет удалить из коризны только один элемент.
    }

    /**
     * Получение корзины пользователя.
     * @param token Токен пользователя, в котором хранится его идентификатор.
     * @return Перечень предметов в корзине.
     */
    @GetMapping
    @ResponseBody
    public Collection<CartElement> get(@RequestHeader String token) {
        long user_id = TokenFactory.decoderToken(token).getId();
         return cs.get(user_id);
    }

    /**
     * Оплата всей корзины.
     * @param token Токен пользователя, в котором хранится его идентификатор.
     * @return True, если оплата прошла. Иначе - false.
     */
    @GetMapping(value = "/pay")
    public ResponseEntity<Boolean> pay(@RequestHeader String token) {
        long user_id = TokenFactory.decoderToken(token).getId();
        return cs.pay(user_id);
    }
}

