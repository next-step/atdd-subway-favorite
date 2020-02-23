package atdd.user.dto;

import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.InitializingBean;

import java.util.Base64;

public class JwtTokenInfo implements InitializingBean {

    private String secretKey;
    private Long expireLength;

    public void setSecretKey(String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public void setExpireLength(long expireLength) {
        this.expireLength = expireLength;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Long getExpireLength() {
        return expireLength;
    }

    @Override
    public String toString() {
        return "JwtTokenInfo{" +
                "secretKey='" + secretKey + '\'' +
                ", expireLength=" + expireLength +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(secretKey, "secretKey 가 없습니다.");
        Assert.notNull(expireLength, "expireLength 가 없습니다.");
    }

}
