package nextstep.subway.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.TokenAuthenticate;
import nextstep.subway.auth.ui.token.TokenAuthenticationConverter;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.application.MemberAuthenticate;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private TokenAuthenticate tokenAuthenticate;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @BeforeEach
    void setUp(){
        this.tokenAuthenticate = new MemberAuthenticate(customUserDetailsService);
    }

    @Test
    void convert() throws IOException {
        // given
        MockHttpServletRequest request = createMockRequest();
        TokenAuthenticationConverter authenticationConverter = new TokenAuthenticationConverter();

        // when
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() {
        // given
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        // when
        Authentication authentication = tokenAuthenticate.authenticate(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest request = createMockRequest();
        TokenAuthenticationConverter authenticationConverter = new TokenAuthenticationConverter();
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        // when
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = tokenAuthenticate.authenticate(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @Test
    public void afterAuthentication() throws IOException{
        // given
        MockHttpServletRequest request = createMockRequest();
        TokenAuthenticationConverter authenticationConverter = new TokenAuthenticationConverter();
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        // when
        Authentication authentication = tokenAuthenticate.authenticate(authenticationToken);
        LoginMember userDetails = (LoginMember) authentication.getPrincipal();
        String tokenString = jwtTokenProvider.createToken(new ObjectMapper().writeValueAsString(userDetails));

        // then
        assertThat(tokenString).isEqualTo(JWT_TOKEN);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
