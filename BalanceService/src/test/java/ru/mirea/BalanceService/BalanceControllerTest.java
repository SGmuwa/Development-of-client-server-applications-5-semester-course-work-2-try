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
import ru.mirea.Tokens.PayloadToken;
import ru.mirea.Tokens.TokenFactory;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.mirea.Tokens.Role.ADMIN;
import static ru.mirea.Tokens.Role.USER;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BalanceControllerTest {

    private final Log log = LogFactory.getLog(BalanceControllerTest.class);

    private ObjectMapper objectMapper = new ObjectMapper();

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
        bc.clear();
        log.info("Проверка, что в БД нет данных.");
        assertEquals(0, bc.getBalance().getBody().size());
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
        ResponseEntity<Collection<User>> re = bc.getBalance();
        assertNotNull(re);
        assertEquals(HttpStatus.OK, re.getStatusCode());
        assertNotNull(re.getBody());
        assertEquals(1, re.getBody().size());
        log.info(objectMapper.writeValueAsString(re.getBody()));
        assertEquals("[{\"user_id\":1,\"iterator\":[{\"countPenny\":15000,\"currency\":\"rub\"},{\"countPenny\":1,\"currency\":\"usd\"}]}]",
                objectMapper.writeValueAsString(re.getBody()));
    }
}