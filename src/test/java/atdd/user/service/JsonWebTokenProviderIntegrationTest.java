package atdd.user.service;

import atdd.user.dto.JsonWebTokenInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JsonWebTokenProviderIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(JsonWebTokenProviderIntegrationTest.class);

    @Autowired
    private JsonWebTokenProvider jwtTokenProvider;

    private final String email = "email@email.com";
    private final Date createDate = new Date();

    @Test
    void createToken() throws Exception {
        final String token = jwtTokenProvider.createToken(email, createDate);

        log.info("token : [{}]", token);
        assertThat(token).isNotBlank();
    }

    @DisplayName("isExpired - 현재시간이 만료시간보다 작거나 같으면 false")
    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void isExpiredFalse(long delta) throws Exception {
        final String token = jwtTokenProvider.createToken(email, createDate);
        final Date expiration = jwtTokenProvider.getExpiration(token);
        final Date nowDate = new Date(expiration.getTime() - delta);

        final boolean expired = jwtTokenProvider.isExpiredToken(token, nowDate);
        assertThat(expired).isFalse();
    }

    @DisplayName("isExpired - 현재시간이 만료시간보다 크면 true")
    @Test
    void isExpiredTrue() throws Exception {
        final String token = jwtTokenProvider.createToken(email, createDate);
        final Date expiration = jwtTokenProvider.getExpiration(token);
        final Date nowDate = new Date(expiration.getTime() + 1L);

        final boolean result = jwtTokenProvider.isExpiredToken(token, nowDate);

        assertThat(result).isTrue();
    }

    @Test
    void parseEmail() throws Exception {
        final String token = jwtTokenProvider.createToken(email, createDate);

        final String parsedEmail = jwtTokenProvider.parseEmail(token);

        assertThat(parsedEmail).isEqualTo(email);
    }

}