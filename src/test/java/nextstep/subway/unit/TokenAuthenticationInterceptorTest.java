package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.nonchain.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";


    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationProvider<AuthenticationToken> authenticationProvider;

    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @Test
    void convert() throws IOException {
        //given
        MockHttpServletRequest mockRequest = createMockRequest();

        //when
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(mockRequest);

        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void preHandle() throws Exception {
        //given
        when(authenticationProvider.authenticate(any()))
                .thenReturn(new Authentication(EMAIL,List.of(RoleType.ROLE_MEMBER.name())));

        boolean isChain = tokenAuthenticationInterceptor.preHandle(createMockRequest(), createMockResponse(), new Object());

        assertThat(isChain).isFalse();
    }


    @Test
    void authenticate() {
        //given
        when(authenticationProvider.authenticate(any())).thenReturn(new Authentication(EMAIL, List.of(RoleType.ROLE_MEMBER.name())));

        //when
        Authentication authentication = tokenAuthenticationInterceptor.authentication(new AuthenticationToken(EMAIL, PASSWORD));

        //then
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getAuthorities()).contains(RoleType.ROLE_MEMBER.name());
    }


    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private MockHttpServletResponse createMockResponse(){
        return new MockHttpServletResponse();
    }

}
