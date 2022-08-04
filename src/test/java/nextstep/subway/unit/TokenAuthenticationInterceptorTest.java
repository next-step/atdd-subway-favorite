package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor2;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private LoginMemberService loginMemberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private TokenAuthenticationInterceptor2 authenticationInterceptor;

    @BeforeEach
    void setUp() {
        authenticationInterceptor = new TokenAuthenticationInterceptor2(loginMemberService, jwtTokenProvider);
    }

    @Test
    void convert() throws IOException {
        // when
        AuthenticationToken token = authenticationInterceptor.convert(createTokenRequest());

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() throws IOException {
        // given
        LoginMember loginMember = LoginMember.of(new Member(EMAIL, PASSWORD, 23, List.of(RoleType.ROLE_MEMBER.name())));
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

        // when
        authenticationInterceptor.authenticate(loginMember, httpServletResponse);

        // then
        TokenResponse tokenResponse = new ObjectMapper().readValue(httpServletResponse.getContentAsString(), TokenResponse.class);
        assertThat(tokenResponse).isNotNull();
    }

    private MockHttpServletRequest createTokenRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
