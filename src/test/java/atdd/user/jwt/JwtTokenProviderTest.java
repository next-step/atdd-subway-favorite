package atdd.user.jwt;

import atdd.user.jwt.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JwtTokenProvider.class)
public class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void createToken() {
        //given
        String email = "abc@email.com";

        //when
        String token = jwtTokenProvider.createToken(email);

        //then
        Assertions.assertThat(token).isNotEmpty();
    }
}
