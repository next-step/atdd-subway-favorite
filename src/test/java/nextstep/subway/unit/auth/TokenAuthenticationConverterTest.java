package nextstep.subway.unit.auth;

import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.unit.auth.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class TokenAuthenticationConverterTest {
    AuthenticationConverter converter = new TokenAuthenticationConverter();

    @Test
    void convert() throws IOException {
        // given
        MockHttpServletRequest request = createTokenMockRequest();

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }
}