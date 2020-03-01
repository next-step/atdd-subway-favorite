package atdd.path.interceptor;

import atdd.user.application.JwtUtils;
import atdd.user.interceptor.AuthenticationInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static atdd.path.TestConstant.USER_EMAIL2;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {AuthenticationInterceptor.class, JwtUtils.class})
public class AuthenticationInterceptorTest {
    @Autowired
    private JwtUtils jwtUtils;

    private AuthenticationInterceptor interceptor;

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        interceptor = new AuthenticationInterceptor(jwtUtils);
    }

    @Test
    public void preHandle() throws Exception {
        String accessToken = jwtUtils.createToken(USER_EMAIL2);

        request.addHeader("Authorization", accessToken);

        boolean result = interceptor.preHandle(request, response, null);

        assertThat(result).isEqualTo(true);
    }
}
