package ru.mirea.TestClientConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties("configuration")
public class JetProjectProperties {
    private final Log log = LogFactory.getLog(getClass());

    private String jet = null;
    public String getJet() {
        log.info("get jet: " + jet);
        return jet;
    }
    public void setJet(String jet) {
        log.info("set jet: " + jet);
        this.jet = jet;
    }
}
