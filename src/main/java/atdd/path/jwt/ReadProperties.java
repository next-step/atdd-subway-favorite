package atdd.path.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token")
public class ReadProperties {
    private String secretKey;
    private int expireLength;

    public String getSecretKey() {
        return secretKey;
    }

    public int getExpireLength() {
        return expireLength;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpireLength(int expireLength) {
        this.expireLength = expireLength;
    }
}
