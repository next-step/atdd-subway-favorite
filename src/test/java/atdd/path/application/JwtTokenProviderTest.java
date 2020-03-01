package atdd.path.application;

import atdd.path.security.LoginInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static atdd.path.TestConstant.USER_EMAIL_1;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JwtTokenProvider.class)
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private LoginInterceptor loginInterceptor;
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        loginInterceptor = new LoginInterceptor(jwtTokenProvider, new BearerTokenExtractor());
    }

    @Test
    public void preHandle() {
        String accessToken = jwtTokenProvider.createToken(USER_EMAIL_1);
        request.addHeader("Authorization", "Bearer " + accessToken);

        boolean result = loginInterceptor.preHandle(request, response, null);

        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("토큰 생성")
    public void createToken() {
        String token = jwtTokenProvider.createToken(USER_EMAIL_1);

        assertThat(token).isNotNull();
        assertThat(USER_EMAIL_1).isEqualTo(jwtTokenProvider.getUserEmail(token));
    }

    @Test
    @DisplayName("토큰으로 유저 이메일 가져오기")
    public void getUserEmailFromToken() {
        String token = jwtTokenProvider.createToken(USER_EMAIL_1);

        assertThat(token).isNotNull();
        assertThat(jwtTokenProvider.getUserEmail(token)).isEqualTo(USER_EMAIL_1);
    }

}
