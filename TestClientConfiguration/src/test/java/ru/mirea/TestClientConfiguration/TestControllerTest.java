package ru.mirea.TestClientConfiguration;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)//, loader = AnnotationConfigContextLoader.class)
public class TestControllerTest {

    private short port = -1;

    public TestControllerTest() {
        log.info("Construct.");
    }

    @PostConstruct
    public void abs() {
        log.info("postConstruct.");
        String portStr = environment.getProperty("local.server.port");
        log.info(portStr);
        if(portStr != null)
            port = Short.parseShort(portStr);
        else
            log.error("Can't read port!");
    }

    @Autowired
    private Environment environment;

    private Log log = LogFactory.getLog(TestControllerTest.class);

    //@LocalServerPort
    //int randomServerPort;

    @Test
    public void getProperties() {
        assertEquals("property local.server.port and server.port not equal.", environment.getProperty("local.server.port"), environment.getProperty("server.port"));
        assertEquals("Server port property not set.", "7000", environment.getProperty("server.port"));

        assertEquals("PostConstruct do not work.", 7000, port);
        String text = new RestTemplate().getForObject("http://localhost:" + port + "/jet", String.class);
        assertEquals("Hello World! (1)", text);
    }
}