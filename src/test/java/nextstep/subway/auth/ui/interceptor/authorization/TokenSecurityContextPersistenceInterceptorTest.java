package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenSecurityContextPersistenceInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private static final String REGEX = ":";
    private static final String JWT = "accessToken";
    private static final long ID = 1L;

    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private TokenSecurityContextPersistenceInterceptor interceptor;
    private ObjectMapper objectMapper;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();

        request.addHeader("Authorization","Bearer NEXTSTEP");
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
    }

    @Test
    void preHandler() throws Exception {

    }
}
