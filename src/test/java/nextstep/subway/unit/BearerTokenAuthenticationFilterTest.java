package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.interceptor.AuthenticationChainHandler;
import nextstep.auth.interceptor.BearerTokenAuthenticationFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.UserDetails;
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
public class BearerTokenAuthenticationFilterTest {

    private String EMAIL = "nextstep";
    private String PASSWORD = "password";
    private Integer AGE = 10;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationChainHandler authenticationChainHandler;
    private MockHttpServletRequest mockHttpServletRequest;

    @BeforeEach
    void setUp() {
        mockHttpServletRequest = new MockHttpServletRequest();
        authenticationChainHandler = new BearerTokenAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    @Test
    void extractCredentialsTest() {
        mockHttpServletRequest.addHeader("Authorization", AuthorizationType.BEARER + " test");

        String extract = authenticationChainHandler.extractCredentials(mockHttpServletRequest);

        assertThat(extract).isEqualTo("test");
    }

    @Test
    void getUserDetailsTest() {
        when(jwtTokenProvider.createToken(any(), anyList()))
            .thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXh0c3RlcCIsImlhdCI6MTY1OTg5NDI3MywiZXhwIjoxNjU5ODk3ODczLCJyb2xlcyI6WyJST0xFX01FTUJFUiJdfQ.bqwC7GsrkYO6jkX8om-Tg9Tk1-VrwvkWCs4CVWuaMyo");
        when(jwtTokenProvider.getPrincipal(any())).thenReturn(EMAIL);

        when(memberRepository.findByEmail(EMAIL)).thenReturn(
            Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(
            LoginMember.of(new Member(EMAIL, PASSWORD, AGE)));

        LoginMember 회원 = 회원_찾기(EMAIL);

        LoginMember loginMember = (LoginMember) authenticationChainHandler.getUserDetails(jwtTokenProvider.createToken(EMAIL,
            List.of(RoleType.ROLE_MEMBER.name())));

        assertThat(loginMember).isEqualTo(회원);
    }

    @Test
    void createAuthenticationTest() {
        when(memberRepository.findByEmail(EMAIL)).thenReturn(
            Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        UserDetails userDetails = 회원_찾기(EMAIL);

        Authentication authentication = authenticationChainHandler.createAuthentication(userDetails);

        assertThat(authentication.getPrincipal()).isEqualTo(userDetails.getEmail());
        assertThat(authentication.getAuthorities()).isEqualTo(userDetails.getAuthorities());
    }

    private void 동일한지_찾기() {

    }

    private LoginMember 회원_찾기(String email) {
        return LoginMember.of(memberRepository.findByEmail(email).get());
    }

}
