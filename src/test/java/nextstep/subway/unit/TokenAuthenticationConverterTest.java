package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationConverter;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.unit.AuthenticationFixture.EMAIL;
import static nextstep.subway.unit.AuthenticationFixture.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TokenAuthenticationConverterTest {

    @DisplayName("토큰 기반 토큰 변환")
    @Test
    void converter() throws IOException {
        //given
        AuthenticationConverter converter = new TokenAuthenticationConverter();

        //when
        AuthenticationToken token = converter.convert(createMockRequest());

        //then
        assertAll(
                () -> assertThat(token.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(token.getCredentials()).isEqualTo(PASSWORD)
        );

    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
