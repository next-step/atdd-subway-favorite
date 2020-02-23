package atdd.user.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token")
public class ReadProperties {
    private String secretKey;
    private Long expireLength;

    public String getSecretKey() {
        return secretKey;
    }

    public Long getExpireLength() {
        return expireLength;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpireLength(Long expireLength) {
        this.expireLength = expireLength;
    }
}
