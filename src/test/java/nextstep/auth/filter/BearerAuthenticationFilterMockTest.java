package nextstep.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BearerAuthenticationFilterMockTest {
    HttpServletRequest request;
    BearerFilter bearerFilter;
    LoginService loginService;
    JwtTokenProvider provider;
    AuthorizationFilter authorizationFilter;
    String TOKEN;
    private static final String PRINCIPAL = "email@email.com";
    private static final String CREDENTIALS = "password";
    private static final List<String> AUTHORITIES = List.of(RoleType.ROLE_ADMIN.name());


    @BeforeEach
    void setUp() throws IOException, IllegalAccessException {
        provider = getProvider();
        loginService = mock(LoginService.class);
        TOKEN = provider.createToken(PRINCIPAL, AUTHORITIES);
        request = createMockRequest();
        bearerFilter = new BearerFilter(provider, loginService);
        authorizationFilter = new AuthorizationFilter(bearerFilter);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(PRINCIPAL, CREDENTIALS);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());

        request.addHeader("Authorization", "BEARER " + provider.createToken(PRINCIPAL, AUTHORITIES));
        return request;
    }

    @Test
    @DisplayName("request에서 토큰을 가져옵니다.")
    void getToken() {
        // when & then
        String requestToken = assertDoesNotThrow(
            () -> bearerFilter.getToken(request)
        );
        assertThat(requestToken).isEqualTo(TOKEN);
    }

    @Test
    @DisplayName("토큰이 유효한지 검사합니다.")
    void validToken() {
        // when & then
        boolean result = bearerFilter.validToken(TOKEN);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰이 유효한지 검사합니다.")
    void validTokenValidation() {
        // given
        String invalidToken = PRINCIPAL + CREDENTIALS;

        // when & then
        boolean result = bearerFilter.validToken(invalidToken);
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("인증 정보를 가져옵니다.")
    void getAuthentication1() {

        // when & then
        Authentication authentication = assertDoesNotThrow(
            () -> bearerFilter.getAuthentication(TOKEN)
        );
        assertThat(authentication.getPrincipal()).isEqualTo(PRINCIPAL);
    }

    @Test
    @DisplayName("사용자를 검증합니다.")
    void validUser() {
        // given
        Authentication authentication = new Authentication(PRINCIPAL, CREDENTIALS);

        // when
        when(loginService.isUserExist(PRINCIPAL)).thenReturn(true);

        // then
        boolean result = bearerFilter.validUser(authentication);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자의 정보가 존재하지 않는다면 false를 반환합니다.")
    void validUserValidation1() {
        // given
        Authentication authentication = new Authentication(PRINCIPAL, CREDENTIALS);

        // when
        when(loginService.isUserExist(PRINCIPAL)).thenReturn(false);

        // then
        boolean result = bearerFilter.validUser(authentication);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인증 정보를 통해 권할을 가져옵니다.")
    void getAuthentication2() {
        // given
        Authentication user = new Authentication(PRINCIPAL, AUTHORITIES);
        // then
        Authentication authentication = bearerFilter.getAuthentication(user);

        assertAll(
            () -> assertThat(authentication.getPrincipal()).isEqualTo(PRINCIPAL),
            () -> assertThat(authentication.getAuthorities()).containsExactly(RoleType.ROLE_ADMIN.name())
        );
    }

    private JwtTokenProvider getProvider() throws IllegalAccessException {
        JwtTokenProvider provider = new JwtTokenProvider();
        // secret-key 설정
        FieldUtils.writeField(provider, "secretKey", "atdd-secret-key", true);
        FieldUtils.writeField(provider, "validityInMilliseconds", 3600000, true);

        return provider;
    }

    @Test
    @DisplayName("prehadle을 실행합니다.")
    void preHandleTest() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = mock(Object.class);

        when(loginService.isUserExist(PRINCIPAL)).thenReturn(true);

        authorizationFilter.preHandle(request, response, handler);
    }
}
