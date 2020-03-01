package atdd.user.jwt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(ReadProperties.class)
public class Properties {
    private String secretKey;
    private Long expireLength;

    public Properties(ReadProperties readProperties) {
        this.secretKey = readProperties.getSecretKey();
        this.expireLength = readProperties.getExpireLength();
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Long getExpireLength() {
        return expireLength;
    }
}
