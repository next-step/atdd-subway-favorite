package atdd.user.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtTokenProviderIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProviderIntegrationTest.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void createToken() throws Exception {
        final String email = "email@email.com";

        final String token = jwtTokenProvider.createToken(email);

        log.info("token : [{}]", token);
        assertThat(token).isNotBlank();
    }

}