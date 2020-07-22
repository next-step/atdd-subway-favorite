package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest {
    CustomUserDetailsService customUserDetailsService;
    JwtTokenProvider jwtTokenProvider;
    TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @BeforeEach
    void setUp() {
        customUserDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider);
    }

    @DisplayName("preHandle은 Response에 JwtToken을 담는다")
    @Test
    void preHandleResponsesJwtToken() throws Exception {
        // given
        String email = "email@email.com";
        String password = "password";
        String jwtToken = "jwtToken";

        when(customUserDetailsService.loadUserByUsername(email)).thenReturn(new LoginMember(1L, email, password, 20));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(jwtToken);

        MockHttpServletRequest request = getBasicAuthorizationRequest(email, password);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        tokenAuthenticationInterceptor.preHandle(request, response, new Object());

        // then
        TokenResponse tokenResponse = new ObjectMapper().readValue(response.getContentAsString(), TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isEqualTo(jwtToken);
    }

    @DisplayName("convert는 유저의 email과 password를 담은 AuthenticationToken를 리턴한다.")
    @Test
    void convertReturnsAuthenticationToken() {
        // given
        String email = "email@email.com";
        String password = "password";

        MockHttpServletRequest request = getBasicAuthorizationRequest(email, password);

        // when
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(email);
        assertThat(authenticationToken.getCredentials()).isEqualTo(password);
    }

    @DisplayName("유저가 존재하는 경우 Authentication에 유저 정보를 저장한다.")
    @Test
    void authenticateWhenUserExists() {
        // given
        String email = "email@email.com";
        String password = "password";
        AuthenticationToken authenticationToken = new AuthenticationToken(email, password);

        LoginMember userDetails = new LoginMember(1L, email, password, 20);
        when(customUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        // when
        Authentication authenticate = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat((LoginMember) authenticate.getPrincipal()).isEqualTo(userDetails);
    }

    @DisplayName("유저가 존재하지 않는 경우 RuntimeException을 던진다.")
    @Test
    void authenticateWhenUserNotExists() {
        // given
        String email = "email@email.com";
        String password = "password";
        AuthenticationToken authenticationToken = new AuthenticationToken(email, password);

        when(customUserDetailsService.loadUserByUsername(email)).thenReturn(null);

        // when
        // then
        assertThrows(RuntimeException.class, () ->
                tokenAuthenticationInterceptor.authenticate(authenticationToken));
    }

    private MockHttpServletRequest getBasicAuthorizationRequest(String email, String password) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        byte[] targetBytes = (email + ":" + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader("Authorization", "Basic " + credentials);
        return request;
    }
}
