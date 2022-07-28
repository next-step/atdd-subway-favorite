package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthMember;
import nextstep.auth.authentication.AuthMemberLoader;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.converter.TokenAuthenticationConverter;
import nextstep.auth.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private TokenAuthenticationInterceptor interceptor;
    @Mock
    private AuthMemberLoader authMemberLoader;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;


    @BeforeEach
    void setUp() throws IOException {
        interceptor = new TokenAuthenticationInterceptor(new TokenAuthenticationConverter(objectMapper), authMemberLoader, jwtTokenProvider);
        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void convert() throws IOException {
        // given
        AuthenticationToken token = interceptor.convert(request);

        // then
        assertAll(() -> {
            assertThat(token.getPrincipal()).isEqualTo(EMAIL);
            assertThat(token.getCredentials()).isEqualTo(PASSWORD);
        });
    }

    @Test
    void authenticate() {
        // given
        given(authMemberLoader.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name())));
        Authentication authenticate = interceptor.authenticate(new AuthenticationToken(EMAIL, PASSWORD));

        // then
        assertAll(() -> {
            assertThat(authenticate.getPrincipal()).isEqualTo(EMAIL);
            assertThat(authenticate.getAuthorities()).containsExactly(ROLE_MEMBER.name());
        });
    }

    @Test
    void preHandle() throws Exception {
        // given
        given(authMemberLoader.loadUserByUsername(EMAIL))
                .willReturn(authMember());
        boolean result = interceptor.preHandle(request, response, null);

        // then
        assertThat(result).isFalse();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    public AuthMember authMember() {
        return new AuthMember() {
            @Override
            public String getEmail() {
                return EMAIL;
            }

            @Override
            public List<String> getAuthorities() {
                return List.of(ROLE_MEMBER.name());
            }

            @Override
            public boolean checkPassword(String password) {
                return true;
            }
        };
    }
}
