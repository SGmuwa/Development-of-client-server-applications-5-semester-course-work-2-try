package ru.mirea.Identity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class IdentityController {

    @Autowired
    private IdentityService identityService;


    @RequestMapping(value = "auth", method = RequestMethod.POST)
    @ResponseBody
    public Token getToken(@RequestBody User user) throws Exception {
        return identityService.getToken(user.getLogin(), user.getPassword());
    }

    @RequestMapping(value = "sign/up", method = RequestMethod.POST)
    @ResponseBody
    public Token addUser(@RequestBody User user) throws Exception {
        return identityService.addUser(user.getLogin(), user.getPassword());
    }
}
