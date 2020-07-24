package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationTokenExtractorTest {
    public static final String EMAIL = "dhlee@test.com";
    public static final String PASSWORD = "PPPPPPAAAASSSSWWWWOOOORRRRDDD";
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }

    @DisplayName("Form login 방식의 요청에서 AuthenticationToken 정보를 추출한다.")
    @Test
    void extractFromFormLoginTest() {
        // given
        AuthenticationTokenExtractor authenticationTokenExtractor = AuthenticationTokenExtractor.of(AuthenticationTokenExtractor.Type.FORM_LOGIN);
        request.setParameter(AuthenticationTokenExtractor.USERNAME_FIELD, EMAIL);
        request.setParameter(AuthenticationTokenExtractor.PASSWORD_FIELD, PASSWORD);

        // when
        AuthenticationToken authenticationToken = authenticationTokenExtractor.extract(request);

        // then
        assertThat(authenticationToken).isNotNull();
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("Basic 방식의 요청에서 AuthenticationToken 정보를 추출한다.")
    @Test
    void extractFromBasicAuthTest() {
        AuthenticationTokenExtractor authenticationTokenExtractor = AuthenticationTokenExtractor.of(AuthenticationTokenExtractor.Type.BASIC);
        byte[] authorizationToken = Base64.encodeBase64((EMAIL + ":" + PASSWORD).getBytes());
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + new String(authorizationToken));

        // when
        AuthenticationToken authenticationToken = authenticationTokenExtractor.extract(request);

        // then
        assertThat(authenticationToken).isNotNull();
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}