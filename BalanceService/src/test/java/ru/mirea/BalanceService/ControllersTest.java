package ru.mirea.BalanceService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mirea.Money.Money;
import ru.mirea.Tokens.TokenFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.Assert.*;
import static ru.mirea.Tokens.Role.USER;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ControllersTest {

    private final Log log = LogFactory.getLog(ControllersTest.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    BalanceController bc;

    @Autowired
    CurrencyController cc;

    @Autowired
    CurrencyControllerAdmin ccAdmin;

    @Before
    public void forTest() {
        bc.clear();
        ccAdmin.clear();
        log.info("Проверка, что в БД нет данных.");
        assertEquals(0, bc.getBalance((Long)null).getBody().size());
    }

    @Test
    public void getBalance() throws JsonProcessingException {
        log.info("Тестирование того, что возможно добавить пользователя.");
        log.info("Test json sample: " + objectMapper.writeValueAsString(new Money(100, "usb")));
        User user = new User(1, Arrays.asList(
                new Money(15000, "rub"),
                new Money(1, "usd"))
        );
        log.info("Test json user sample: " + objectMapper.writeValueAsString(user));
        bc.updateBalance(user);
        ResponseEntity<Collection<?>> re = bc.getBalance((Long)null);
        assertNotNull(re);
        assertEquals(HttpStatus.OK, re.getStatusCode());
        assertNotNull(re.getBody());
        assertEquals(1, re.getBody().size());
        log.info(objectMapper.writeValueAsString(re.getBody()));
        assertEquals("[{\"user_id\":1,\"balance\":[{\"penny\":15000,\"currency\":\"rub\"},{\"penny\":1,\"currency\":\"usd\"}]}]",
                objectMapper.writeValueAsString(re.getBody()));
    }

    @Test
    public void currency_buy() {
        ccAdmin.update(Arrays.asList(
                new CurrencyConvert("rub", "usd", 70235234), // 70 рублей
                new CurrencyConvert("usd", "rub", 21341) // 2 цента
        ));
        User user = new User(
                2, Arrays.asList(
                        new Money(7024, "rub"), // 70 рублей
                new Money(2, "usd") // 2 цента
        ));
        bc.updateBalance(user);

        log.info("Сейчас пользователь пройдёт процедуру покупки валюты.");
        ResponseEntity<Boolean> responseEntity1 = cc.buyCurrency(TokenFactory.generateToken(user.getUser_id(), "test", USER), "rub", "usd", 100);
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertNotNull(responseEntity1.getBody());
        assertTrue(responseEntity1.getBody());
        log.info(bc.getBalance(user.getUser_id()).getBody());
        ResponseEntity<Collection<?>> responseEntity2 = bc.getBalance(user.getUser_id());
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        Collection<?> cash = responseEntity2.getBody();
        assertNotNull(cash);
        assertFalse(cash.isEmpty());
        assertTrue(cash.containsAll(
                Arrays.asList(
                        new Money(0, "rub"),
                        new Money(102, "usd")
                )
        ));
        cc.buyCurrency(TokenFactory.generateToken(user.getUser_id(), "test", USER), "usd", "rub", 100);
        log.info(bc.getBalance(user.getUser_id()).getBody());
        assertTrue(Objects.requireNonNull(bc.getBalance(user.getUser_id()).getBody()).containsAll(
                Arrays.asList(
                        new Money(100, "rub"),
                        new Money(100, "usd")
                )
        ));

    }
}