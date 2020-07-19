package nextstep.subway.auth.ui.interceptor.token;

import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private CustomUserDetailsService customUserDetailsService;
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        loginMember = new LoginMember(1L, EMAIL, PASSWORD, AGE);
    }


    @Test
    void testPreHandle() throws Exception {

    }
}
