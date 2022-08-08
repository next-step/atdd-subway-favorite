package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.interceptor.AuthenticationNonChainHandler;
import nextstep.auth.interceptor.UsernamePasswordAuthenticationFilter;
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
public class UsernamePasswordAuthenticationFilterTest {

    private String EMAIL = "nextstep";
    private String PASSWORD = "password";
    private Integer AGE = 10;

    @Mock
    private UserDetailsService userDetailsService;

    private ObjectMapper objectMapper;

    private AuthenticationNonChainHandler authenticationNonChainHandler;
    private MockHttpServletRequest mockHttpServletRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockHttpServletRequest = new MockHttpServletRequest();
        authenticationNonChainHandler = new UsernamePasswordAuthenticationFilter(userDetailsService);
    }

    @Test
    void getAuthenticationTokenTest() {
        파라미터값을_추가(EMAIL, PASSWORD);

        AuthenticationToken authenticationToken = authenticationNonChainHandler.getAuthenticationToken(mockHttpServletRequest);

        AuthenticationToken 비교값 = new AuthenticationToken(EMAIL, PASSWORD);

        assertThat(authenticationToken).isEqualTo(비교값);
    }

    @Test
    void getUserDetailsTest() {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(
            LoginMember.of(new Member(EMAIL, PASSWORD, AGE)));

        LoginMember loginMember = (LoginMember) authenticationNonChainHandler.getUserDetails(new AuthenticationToken(EMAIL, PASSWORD));

        assertThat(loginMember).isEqualTo(new LoginMember(EMAIL, PASSWORD, List.of(
            RoleType.ROLE_MEMBER.name())));
    }

    private void 파라미터값을_추가(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        mockHttpServletRequest.addParameters(params);
    }

}
