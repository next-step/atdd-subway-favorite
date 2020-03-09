package atdd.user.web;

import atdd.path.TestConstant;
import atdd.path.application.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = JwtTokenProvider.class)
public class LoginInterceptorTest {

    private LoginInterceptor loginInterceptor;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        this.loginInterceptor = new LoginInterceptor(jwtTokenProvider);
    }

    @Test
    public void preHandle() throws Exception {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getUserEmail(any())).willReturn(TestConstant.EMAIL_BROWN);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/favorite/station");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginInterceptor.preHandle(request, response, null);

        assertThat(request.getAttribute("loginUserEmail")).isEqualTo(TestConstant.EMAIL_BROWN);
    }
}
