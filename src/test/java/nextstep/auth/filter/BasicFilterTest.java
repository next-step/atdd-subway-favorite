package nextstep.auth.filter;

import nextstep.auth.context.Authentication;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicFilterTest {

    HttpServletRequest request;
    BasicFilter basicFilter;
    LoginService loginService;

    private static final String PRINCIPAL = "user1";
    private static final String CREDENTIALS = "password1";
    private static final List<String> AUTHORITIES = List.of(RoleType.ROLE_ADMIN.name());
    private static final String COLON = ":";
    private static final String TOKEN = PRINCIPAL + COLON + CREDENTIALS;

    @BeforeEach
    void setUp() throws IOException {
        request = createMockRequest();
        loginService = mock(LoginService.class);
        basicFilter = new BasicFilter(loginService);
    }


    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Base64 encoder = new Base64(0, new byte[0]);
        String encodedUserPass = encoder.encodeToString(BasicFilterTest.TOKEN.getBytes());
        request.addHeader("Authorization", "BASIC " + encodedUserPass);
        return request;
    }

    @Test
    @DisplayName("request에서 토큰을 가져옵니다.")
    void getToken() {
        // when & then
        String requestToken = assertDoesNotThrow(
            () -> basicFilter.getToken(request)
        );
        assertThat(requestToken).isEqualTo(TOKEN);
    }

    @Test
    @DisplayName("토큰이 유효한지 검사합니다.")
    void validToken() {
        // when & then
        boolean result = basicFilter.validToken(TOKEN);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰이 유효한지 검사합니다.")
    void validTokenValidation() {
        // given
        String invalidToken = PRINCIPAL + CREDENTIALS;

        // when & then
        boolean result = basicFilter.validToken(invalidToken);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인증 정보를 가져옵니다.")
    void getAuthentication1() {

        // when & then
        Authentication authentication = assertDoesNotThrow(
            () -> basicFilter.getAuthentication(TOKEN)
        );
        assertThat(authentication.getPrincipal()).isEqualTo(PRINCIPAL);
        assertThat(authentication.getCredentials()).isEqualTo(CREDENTIALS);
    }

    @Test
    @DisplayName("사용자를 검증합니다.")
    void validUser() {
        // given
        Authentication authentication = new Authentication(PRINCIPAL, CREDENTIALS);

        // when
        when(loginService.loadUserByUsername(PRINCIPAL)).thenReturn(new LoginMember(PRINCIPAL, CREDENTIALS, null));

        // then
        boolean result = basicFilter.validUser(authentication);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자의 정보가 존재하지 않는다면 false를 반환합니다.")
    void validUserValidation1() {
        // given
        Authentication authentication = new Authentication(PRINCIPAL, CREDENTIALS);

        // when
        when(loginService.loadUserByUsername(PRINCIPAL)).thenReturn(null);

        // then
        boolean result = basicFilter.validUser(authentication);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("비밀번호가 틀리면 false를 반환합니다.")
    void validUserValidation2() {
        // given
        String otherPassword = "otherPassword";
        Authentication authentication = new Authentication(PRINCIPAL, CREDENTIALS);

        // when
        when(loginService.loadUserByUsername(PRINCIPAL)).thenReturn(new LoginMember(PRINCIPAL, otherPassword, null));

        // then
        boolean result = basicFilter.validUser(authentication);
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("인증 정보를 통해 권할을 가져옵니다.")
    void getAuthentication2() {
        // given
        Authentication user = new Authentication(PRINCIPAL, CREDENTIALS);

        // when
        when(loginService.loadUserByUsername(PRINCIPAL)).thenReturn(new LoginMember(PRINCIPAL, CREDENTIALS, AUTHORITIES));

        // then
        Authentication authentication = basicFilter.getAuthentication(user);

        assertAll(
            () -> assertThat(authentication.getPrincipal()).isEqualTo(PRINCIPAL),
            () -> assertThat(authentication.getAuthorities()).containsExactly(RoleType.ROLE_ADMIN.name())
        );
    }
}
