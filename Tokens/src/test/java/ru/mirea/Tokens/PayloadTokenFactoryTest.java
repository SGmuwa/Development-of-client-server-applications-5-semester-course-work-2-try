package ru.mirea.Tokens;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class PayloadTokenFactoryTest {
    @Test
    public void SaveAndLoadPayload() {
        PayloadToken payloadToken = new PayloadToken(2, "pup", Collections.singletonList(Role.ADMIN));
        assertEquals("{\"id\":2,\"login\":\"pup\",\"role\":[\"ADMIN\"]}", payloadToken.generateJSON());
        System.out.println(new PayloadToken(3, "SidorenkoMikhail", Collections.singletonList(Role.USER)));
        System.out.println(TokenFactory.generateToken(new PayloadToken(3, "SidorenkoMikhail", Collections.singletonList(Role.USER))));
    }
}