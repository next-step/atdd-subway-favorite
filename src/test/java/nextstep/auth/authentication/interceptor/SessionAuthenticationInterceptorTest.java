package nextstep.auth.authentication.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.domain.member.domain.LoginMemberImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Mock
    private UserDetailsService userDetailsService;

    private AuthenticationInterceptor authenticationInterceptor;

    @BeforeEach
    void setUp() {
        authenticationInterceptor = new SessionAuthenticationInterceptor(userDetailsService);
    }

    @DisplayName("로그인 정보 추출 테스트")
    @Test
    void convert() throws IOException {
        //given
        MockHttpServletRequest request = createMockRequest();
        request.setParameter("username", EMAIL);
        request.setParameter("password", PASSWORD);

        //when
        AuthenticationToken token = authenticationInterceptor.convert(request);

        //then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }


    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}