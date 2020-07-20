package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.utils.AuthorizationTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class BasicAuthenticationConverterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Basic Login 정보를 request Header에서 받아와 AuthenticationToken으로 변환한다")
    @Test
    void convert() {
        //given
        BasicAuthenticationConverter converter = new BasicAuthenticationConverter();
        MockHttpServletRequest request = AuthorizationTestUtils.addBasicAuthorizationHeader(new MockHttpServletRequest(), EMAIL, PASSWORD);
        //when
        AuthenticationToken token = converter.convert(request);

        //then
        assertThat(token).isNotNull()
                .isEqualToComparingFieldByField(new AuthenticationToken(EMAIL, PASSWORD));
    }


}
