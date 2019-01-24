package ru.mirea.TestClientConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class TestController {

    private JetProjectProperties jetProjectProperties;

    @Autowired
    public TestController(JetProjectProperties jetProjectProperties) {
        this.jetProjectProperties = jetProjectProperties;
    }

    @RequestMapping(value = "/jet", method = GET)
    @ResponseBody
    public String getProperties() {
        return jetProjectProperties.getJet();
    }
}
