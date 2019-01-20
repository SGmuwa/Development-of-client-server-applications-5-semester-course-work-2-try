package ru.mirea.TestClientConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("configuration")
public class JetProjectProperties {
    private final Log log = LogFactory.getLog(getClass());

    private String jet;
    public String getJet() {
        return jet;
    }
    public void setJet(String jet) {
        this.jet = jet;
    }
}
