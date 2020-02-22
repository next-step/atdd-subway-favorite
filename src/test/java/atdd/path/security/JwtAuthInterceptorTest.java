package atdd.path.security;

import atdd.path.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import static atdd.path.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class JwtAuthInterceptorTest {
    private JwtAuthInterceptor jwtAuthInterceptor;
    private TokenAuthenticationService tokenAuthenticationService;

    @MockBean
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        this.jwtAuthInterceptor = new JwtAuthInterceptor(tokenAuthenticationService, userDao);
        this.tokenAuthenticationService = new TokenAuthenticationService();
    }

    @DisplayName("사용자 로그인 시 토큰 검증을 진행하는지")
    @Test
    public void preHandle() throws Exception {
        //given
        when(userDao.findByEmail(KIM_EMAIL)).thenReturn(FIND_BY_EMAIL_RESPONSE_VIEW);
        MockHttpServletRequest request = jwtAuthHttpRequest(KIM_EMAIL);

        //when
        boolean isAuthorization = jwtAuthInterceptor.preHandle(request, null, null);

        //then
        assertThat(isAuthorization).isTrue();
    }

    private MockHttpServletRequest jwtAuthHttpRequest(String email) {
        String jwt = tokenAuthenticationService.toJwtByEmail(email);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, jwt);
        return request;
    }
}
