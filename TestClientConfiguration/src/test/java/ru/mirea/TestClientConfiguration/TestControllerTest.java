package ru.mirea.TestClientConfiguration;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerTest {

    @LocalServerPort
    int randomServerPort;

    @org.junit.jupiter.api.Test
    void getProperties() {
        /*
        assertEquals(7000, randomServerPort);
        RestTemplate abs = new RestTemplate();
        String text = new RestTemplate().getForObject("http://localhost:" + randomServerPort + "/jet", String.class);
        assertEquals("Hello World! (1)", text);*/
    }
}