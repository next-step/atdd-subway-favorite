package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthMember;
import nextstep.auth.authentication.AuthMemberLoader;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
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
        interceptor = new TokenAuthenticationInterceptor(authMemberLoader, jwtTokenProvider, objectMapper);
        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void convert() throws IOException {
        // given
        TokenRequest tokenRequest = interceptor.convert(request);

        // then
        assertAll(() -> {
            assertThat(tokenRequest.getEmail()).isEqualTo(EMAIL);
            assertThat(tokenRequest.getPassword()).isEqualTo(PASSWORD);
        });
    }

    @Test
    void authenticate() {
        // given
        given(authMemberLoader.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name())));
        AuthMember authMember = interceptor.authenticate(new TokenRequest(EMAIL, PASSWORD));

        // then
        assertAll(() -> {
            assertThat(authMember.getEmail()).isEqualTo(EMAIL);
            assertThat(authMember.checkPassword(PASSWORD)).isTrue();
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
