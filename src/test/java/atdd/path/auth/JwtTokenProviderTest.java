package atdd.path.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JwtTokenProvider.class)
public class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("입력한 email 로 token 을 얻는다")
    @Test
    public void getToken() {
        String email = "boorwonie@email.com";
        String accessToken = jwtTokenProvider.createToken(email);

        String parsedResult = jwtTokenProvider.parseToken(accessToken);

        assertThat(parsedResult).isEqualTo(email);
    }
}
