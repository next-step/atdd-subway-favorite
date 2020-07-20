package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.subway.auth.ui.interceptor.authentication.SessionAuthenticationInterceptor.PASSWORD_FIELD;
import static nextstep.subway.auth.ui.interceptor.authentication.SessionAuthenticationInterceptor.USERNAME_FIELD;
import static org.assertj.core.api.Assertions.assertThat;

class FormAuthenticationConverterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Form Login 정보를 request에서 AuthenticationToken으로 변환한다")
    @Test
    void convert() {
        //given
        FormAuthenticationConverter converter = new FormAuthenticationConverter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(USERNAME_FIELD, EMAIL);
        request.setParameter(PASSWORD_FIELD, PASSWORD);

        //when
        AuthenticationToken token = converter.convert(request);

        //then
        assertThat(token).isNotNull()
                .isEqualToComparingFieldByField(new AuthenticationToken(EMAIL, PASSWORD));
    }
}
