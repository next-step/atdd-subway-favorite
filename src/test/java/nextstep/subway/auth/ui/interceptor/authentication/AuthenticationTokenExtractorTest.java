package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationTokenExtractorTest {
    public static final String EMAIL = "dhlee@test.com";
    public static final String PASSWORD = "PPPPPPAAAASSSSWWWWOOOORRRRDDD";

    @DisplayName("Form login 방식의 요청에서 AuthenticationToken 정보를 추출한다.")
    @Test
    void extractTest() {
        // given
        AuthenticationTokenExtractor authenticationTokenExtractor = AuthenticationTokenExtractor.of(AuthenticationTokenExtractor.Type.FORM_LOGIN);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(AuthenticationTokenExtractor.USERNAME_PARAMETER_NAME, EMAIL);
        request.setParameter(AuthenticationTokenExtractor.PASSWORD_PARAMTER_NAME, PASSWORD);

        // when
        AuthenticationToken authenticationToken = authenticationTokenExtractor.extract(request);

        // then
        assertThat(authenticationToken).isNotNull();
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}