package ru.mirea.BalanceService;

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
import ru.mirea.Tokens.PayloadToken;
import ru.mirea.Tokens.Role;
import ru.mirea.Tokens.TokenFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.mirea.Tokens.Role.ADMIN;
import static ru.mirea.Tokens.Role.USER;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BalanceControllerTest {

    private final Log log = LogFactory.getLog(BalanceControllerTest.class);

    @Autowired
    BalanceController bc;

    String admin = TokenFactory.generateToken(
            new PayloadToken(
                    -1,
                    "admin",
                    ADMIN
            )
    );

    String user = TokenFactory.generateToken(
            new PayloadToken(
                    -2,
                    "test",
                    USER
            )
    );

    @Before
    public void forTest() {
        log.info("Проверка, что в БД нет данных.");
        assertEquals(0, bc.getBalance().getBody().size());
    }

    @Test
    public void getBalance() {
        log.info("Тестирование того, что возможно ");
        User user = new User(1, Arrays.asList(
                new Money(1, "rub"),
                new Money(1, "usd"))
        );
        bc.updateBalance(user);
        ResponseEntity<Collection<User>> re = bc.getBalance();
        assertNotNull(re);
        assertEquals(HttpStatus.OK, re.getStatusCode());
        assertNotNull(re.getBody());
        assertEquals(1, re.getBody().size());
    }

    @Test
    public void getBalance1() {
    }

    @Test
    public void buyCurrency() {
    }

    @Test
    public void updateBalance() {
    }

    @Test
    public void updateBalance1() {
    }
}