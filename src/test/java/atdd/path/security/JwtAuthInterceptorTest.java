package atdd.path.security;

import atdd.path.SoftAssertionTest;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import static atdd.path.fixture.UserFixture.*;
import static atdd.path.security.JwtAuthInterceptor.AUTH_USER_KEY;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtAuthInterceptorTest extends SoftAssertionTest {
    private JwtAuthInterceptor jwtAuthInterceptor;
    private TokenAuthenticationService tokenAuthenticationService;

    @MockBean
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        this.tokenAuthenticationService = new TokenAuthenticationService();
        this.jwtAuthInterceptor = new JwtAuthInterceptor(tokenAuthenticationService, userDao);
    }

    @DisplayName("사용자 로그인 시 토큰 검증을 진행하는지")
    @Test
    public void preHandle(SoftAssertions softly) throws Exception {
        //given
        when(userDao.findByEmail(KIM_EMAIL)).thenReturn(FIND_BY_EMAIL_RESPONSE_VIEW);
        MockHttpServletRequest request = jwtAuthHttpRequest(KIM_EMAIL);

        //when
        boolean isAuthorization = jwtAuthInterceptor.preHandle(request, null, null);

        //then
        softly.assertThat(isAuthorization).isTrue();
        softly.assertThat(request.getAttribute(AUTH_USER_KEY).getClass()).isEqualTo(User.class);
    }

    private MockHttpServletRequest jwtAuthHttpRequest(String email) {
        String jwt = tokenAuthenticationService.toJwtByEmail(email);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, jwt);
        return request;
    }
}
