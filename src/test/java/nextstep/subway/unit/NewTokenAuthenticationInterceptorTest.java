package nextstep.subway.unit;

import static nextstep.subway.unit.TokenFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.NewTokenAuthenticationInterceptor;
import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class NewTokenAuthenticationInterceptorTest {
    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationConverter authenticationConverter;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private NewTokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @Test
    void authenticate() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        UserDetails userDetails = new LoginMember(1L, EMAIL, PASSWORD, 30);
        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(userDetails);

        // when
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
    }

    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        UserDetails userDetails = new LoginMember(1L, EMAIL, PASSWORD, 30);
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(userDetails);
        given(jwtTokenProvider.createToken(any())).willReturn(JWT_TOKEN);
        given(authenticationConverter.convert(request)).willReturn(authenticationToken);

        // when
        tokenAuthenticationInterceptor.preHandle(request, response, null);

        // then
        TokenResponse expectedTokenResponse = new TokenResponse(JWT_TOKEN);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(expectedTokenResponse));
    }

}
