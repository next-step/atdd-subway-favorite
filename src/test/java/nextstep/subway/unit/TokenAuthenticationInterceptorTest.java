package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.subway.unit.TokenFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @Test
    void authenticate() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 30);
        given(customUserDetailsService.loadUserByUsername(EMAIL)).willReturn(loginMember);

        // when
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(loginMember);
    }

    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 30);

        given(customUserDetailsService.loadUserByUsername(EMAIL)).willReturn(loginMember);
        given(jwtTokenProvider.createToken(any())).willReturn(JWT_TOKEN);

        // when
        tokenAuthenticationInterceptor.preHandle(request, response, null);

        // then
        TokenResponse expectedTokenResponse = new TokenResponse(JWT_TOKEN);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(expectedTokenResponse));
    }

}
