package atdd.user.dto;

import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.InitializingBean;

import java.util.Base64;

public class JsonWebTokenInfo implements InitializingBean {

    private static final int MILLI_OF_SECOND = 1000;
    private String secretKey;
    private Long expireLength;

    public void setSecretKey(String secretKey) {
        checkSecretKey(secretKey);
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private void checkSecretKey(String secretKey) {
        Assert.hasText(secretKey, "secretKey 가 없습니다.");
    }

    public void setExpireLength(Long expireLength) {
        checkExpireLength(expireLength);
        this.expireLength = expireLength;
    }

    private void checkExpireLength(Long expireLength) {
        Assert.notNull(expireLength, "expireLength 가 없습니다.");
        Assert.isTrue(expireLength >= MILLI_OF_SECOND, "expireLength 는 1초(1000ms) 이상이어야 합니다.");
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
        checkSecretKey(secretKey);
        checkExpireLength(expireLength);
    }

}
