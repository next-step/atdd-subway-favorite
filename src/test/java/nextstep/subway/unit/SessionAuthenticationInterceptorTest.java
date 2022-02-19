package nextstep.subway.unit;

import static nextstep.subway.unit.TokenFixture.EMAIL;
import static nextstep.subway.unit.TokenFixture.PASSWORD;
import static nextstep.subway.unit.TokenFixture.createSessionMockRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class SessionAuthenticationInterceptorTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationConverter authenticationConverter;

    @InjectMocks
    private SessionAuthenticationInterceptor sessionAuthenticationInterceptor;

    @Test
    void authenticate() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        UserDetails userDetails = new LoginMember(1L, EMAIL, PASSWORD, 30);
        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(userDetails);

        // when
        Authentication authentication = sessionAuthenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
    }

    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest request = createSessionMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        UserDetails userDetails = new LoginMember(1L, EMAIL, PASSWORD, 30);
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(userDetails);
        given(authenticationConverter.convert(request)).willReturn(authenticationToken);

        // when
        sessionAuthenticationInterceptor.preHandle(request, response, null);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);

    }

}
