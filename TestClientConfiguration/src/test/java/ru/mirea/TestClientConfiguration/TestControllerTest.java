package ru.mirea.TestClientConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestControllerTest {

    public TestControllerTest() {

    }

    //@LocalServerPort
    //int randomServerPort;

    @Test
    public void getProperties() {
        /*
        assertEquals(7000, randomServerPort);
        RestTemplate abs = new RestTemplate();
        String text = new RestTemplate().getForObject("http://localhost:" + randomServerPort + "/jet", String.class);
        assertEquals("Hello World! (1)", text);*/
    }
}