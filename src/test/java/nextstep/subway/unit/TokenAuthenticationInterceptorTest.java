package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.interceptor.AuthenticationChainHandler;
import nextstep.auth.interceptor.AuthenticationNonChainHandler;
import nextstep.auth.interceptor.BearerTokenAuthenticationFilter;
import nextstep.auth.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.UserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.member.login.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class TokenAuthenticationInterceptorTest {

    private String EMAIL = "nextstep";
    private String PASSWORD = "password";
    private Integer AGE = 10;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private MemberRepository memberRepository;

    private ObjectMapper objectMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationNonChainHandler authenticationNonChainHandler;
    private MockHttpServletRequest mockHttpServletRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockHttpServletRequest = new MockHttpServletRequest();
        authenticationNonChainHandler = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, objectMapper);
    }

    @Test
    void getAuthenticationTokenTest() {
        mockHttpServletRequest.setContent("{\"email\":\"nextstep@naver.com\",\"password\":\"1234\"}".getBytes(StandardCharsets.UTF_8));

        AuthenticationToken authenticationToken = authenticationNonChainHandler.getAuthenticationToken(mockHttpServletRequest);

        assertThat(authenticationToken).isEqualTo(new AuthenticationToken("nextstep@naver.com", "1234"));
    }

    @Test
    void getUserDetailsTest() {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(
            LoginMember.of(new Member(EMAIL, PASSWORD, AGE)));

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        LoginMember loginMember = (LoginMember) authenticationNonChainHandler.getUserDetails(authenticationToken);

        assertThat(loginMember).isEqualTo(new LoginMember(EMAIL, PASSWORD, List.of(RoleType.ROLE_MEMBER.name())));
    }

}
