package ru.mirea.Tokens;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class TokenFactoryTest {

    @Test
    public void parseTokenTest() throws Exception {
        PayloadToken user = new PayloadToken(12, "pupu", Collections.singletonList(Role.ADMIN));
        String tokenStr  = TokenFactory.generateToken(user);
        System.out.println(tokenStr);
        PayloadToken result = TokenFactory.decoderToken(tokenStr);
        System.out.println(result);
        assertEquals(user, result);
    }
}