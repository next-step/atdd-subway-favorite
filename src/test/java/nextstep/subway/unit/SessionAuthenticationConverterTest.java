package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.application.SessionAuthenticationConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.unit.AuthenticationFixture.EMAIL;
import static nextstep.subway.unit.AuthenticationFixture.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SessionAuthenticationConverterTest {

    @DisplayName("세션 기반 토큰 변환")
    @Test
    void converter() throws IOException {
        //given
        AuthenticationConverter converter = new SessionAuthenticationConverter();

        //when
        AuthenticationToken token = converter.convert(createMockRequest());

        //then
        assertAll(
                () -> assertThat(token.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(token.getCredentials()).isEqualTo(PASSWORD)
        );

    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", EMAIL);
        request.setParameter("password", PASSWORD);
        return request;
    }
}
