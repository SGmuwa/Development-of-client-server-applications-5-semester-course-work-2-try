package ru.mirea.Tokens;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author [SG]Muwa
 */
@Component
@ConfigurationProperties("tokens.configuration")
public class TokenPropertyGetter {
    public void setSecretKey(String secretKey) {
        TokenFactory.setSecretKey(secretKey);
    }
}
