package atdd.user.application;

import atdd.user.configs.interceptor.LoginInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;

import static atdd.user.UserConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "security.jwt.token.secret-key=abcdabcdabcdabcd",
        "security.jwt.token.expire-length=3600000"
})
public class LogInIneterceptorTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private LoginInterceptor loginInterceptor;

    @BeforeEach
    void setup(){
        this.loginInterceptor = new LoginInterceptor(jwtTokenProvider);
    }

    @DisplayName("로그인 인터셉터 테스트")
    @Test
    public void intercepterTest(){
        String token = jwtTokenProvider.createToken(USER_EMAIL);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/me");
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.addHeader("Authorization", "Bearer " + token);
        loginInterceptor.preHandle(request, response, null);

        assertThat(request.getAttribute("loginUserEmail")).isNotNull();
    }
}
