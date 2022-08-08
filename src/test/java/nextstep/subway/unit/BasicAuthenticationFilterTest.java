package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.auth.context.Authentication;
import nextstep.auth.interceptor.AuthenticationChainHandler;
import nextstep.auth.interceptor.BasicAuthenticationFilter;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.login.LoginMember;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class BasicAuthenticationFilterTest {

    private String EMAIL = "nextstep";
    private String PASSWORD = "password";
    private Integer AGE = 10;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private MemberRepository memberRepository;

    private AuthenticationChainHandler authenticationChainHandler;

    private MockHttpServletRequest mockHttpServletRequest;

    @BeforeEach
    void setUp() {
        mockHttpServletRequest = new MockHttpServletRequest();
        authenticationChainHandler = new BasicAuthenticationFilter(userDetailsService);
    }

    @Test
    void extractCredentialsTest() {
        String 암호화_값 = 베이직_암호화값을_반환합니다(EMAIL, PASSWORD);
        헤더값을_추가한다(암호화_값);

        String extract = authenticationChainHandler.extractCredentials(mockHttpServletRequest);

        assertThat(extract).isEqualTo(암호화_값);
    }

    @Test
    void getUserDetailsTest() {
        when(memberRepository.findByEmail(EMAIL)).thenReturn(
            Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(
            LoginMember.of(new Member(EMAIL, PASSWORD, AGE)));

        String 암호화_값 = 베이직_암호화값을_반환합니다(EMAIL, PASSWORD);

        LoginMember loginMember = (LoginMember) authenticationChainHandler.getUserDetails(암호화_값);
        LoginMember login = 회원_조회(EMAIL);

        assertThat(loginMember).isEqualTo(login);
    }

    @Test
    void createAuthenticationTest() {
        when(memberRepository.findByEmail(EMAIL)).thenReturn(
            Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        UserDetails userDetails = 회원_조회(EMAIL);

        Authentication authentication = authenticationChainHandler.createAuthentication(userDetails);

        assertThat(authentication.getPrincipal()).isEqualTo(userDetails.getEmail());
        assertThat(authentication.getAuthorities()).isEqualTo(userDetails.getAuthorities());
    }

    private String 베이직_암호화값을_반환합니다(String email, String password) {
        return Base64.encodeBase64String(
            String.join(":", email, password).getBytes());
    }

    private void 헤더값을_추가한다(String basicAuthentication) {
        mockHttpServletRequest.addHeader("Authorization", "Basic " + basicAuthentication);
    }

    private LoginMember 회원_조회(String email) {
        return LoginMember.of(memberRepository.findByEmail(email).get());
    }

}
