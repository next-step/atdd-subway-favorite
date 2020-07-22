package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationTokenExtractorTest {
    public static final String EMAIL = "dhlee@test.com";
    public static final String PASSWORD = "PPPPPPAAAASSSSWWWWOOOORRRRDDD";
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.setParameter(AuthenticationTokenExtractor.USERNAME_FIELD, EMAIL);
        request.setParameter(AuthenticationTokenExtractor.PASSWORD_FIELD, PASSWORD);
    }

    @DisplayName("Form login 방식의 요청에서 AuthenticationToken 정보를 추출한다.")
    @Test
    void extractFromFormLoginTest() {
        AuthenticationTokenExtractor authenticationTokenExtractor = AuthenticationTokenExtractor.of(AuthenticationTokenExtractor.Type.FORM_LOGIN);

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

        // when
        AuthenticationToken authenticationToken = authenticationTokenExtractor.extract(request);

        // then
        assertThat(authenticationToken).isNotNull();
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}