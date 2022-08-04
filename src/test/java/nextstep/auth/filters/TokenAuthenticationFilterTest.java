package nextstep.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.DataLoader;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.user.UserDetails;
import nextstep.member.application.LoginMemberService;
import nextstep.auth.user.User;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static nextstep.DataLoader.MEMBER_EMAIL;
import static nextstep.DataLoader.PASSWORD;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class TokenAuthenticationFilterTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;
    @Autowired
    private LoginMemberService loginMemberService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationRespondingFilter authenticationInterceptor;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        dataLoader.loadData();
        authenticationInterceptor = new TokenAuthenticationFilter(loginMemberService, jwtTokenProvider);
    }

    @Test
    void convert() throws IOException {
        // when
        AuthenticationToken token = authenticationInterceptor.convert(createTokenRequest());

        // then
        assertThat(token.getPrincipal()).isEqualTo(MEMBER_EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void validate() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(MEMBER_EMAIL, PASSWORD);

        // when + then
        assertThatNoException().isThrownBy(() -> authenticationInterceptor.validate(authenticationToken));
    }

    @Test
    void validate_No_User() {
        // given
        String invalidEmail = "invalid@email.com";
        AuthenticationToken authenticationToken = new AuthenticationToken(invalidEmail, PASSWORD);

        // when + then
        assertThatThrownBy(() -> authenticationInterceptor.validate(authenticationToken))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void validate_Wrong_Password() {
        // given
        String invalidPassword = "asdfasdf";
        AuthenticationToken authenticationToken = new AuthenticationToken(MEMBER_EMAIL, invalidPassword);

        // when + then
        assertThatThrownBy(() -> authenticationInterceptor.validate(authenticationToken))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void authenticate() throws IOException {
        // given
        UserDetails userDetails = User.of(new Member(MEMBER_EMAIL, PASSWORD, 23, List.of(RoleType.ROLE_MEMBER.name())));
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

        // when
        authenticationInterceptor.authenticate(userDetails, httpServletResponse);

        // then
        TokenResponse tokenResponse = new ObjectMapper().readValue(httpServletResponse.getContentAsString(), TokenResponse.class);
        assertThat(tokenResponse).isNotNull();
    }

    private MockHttpServletRequest createTokenRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(MEMBER_EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
