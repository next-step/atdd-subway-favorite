package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void preHandle() throws UnsupportedEncodingException, JsonProcessingException {
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

    private MockHttpServletRequest getBasicAuthorizationRequest(String email, String password) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        byte[] targetBytes = (email + ":" + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader("Authorization", "Basic " + credentials);
        return request;
    }
}
