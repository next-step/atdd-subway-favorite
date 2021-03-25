package nextstep.subway.auth.token;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.common.AuthenticationConverter;
import nextstep.subway.auth.ui.token.TokenAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.auth.token.TokenFixture.*;
import static nextstep.subway.auth.token.TokenFixture.createMockRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class TokenAuthenticationConverterTest {
    private AuthenticationConverter converter = new TokenAuthenticationConverter();

    @Test
    void convert() throws IOException {
        // given
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken convertedToken = converter.convert(request);

        // then
        assertThat(convertedToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(convertedToken.getCredentials()).isEqualTo(PASSWORD);
    }
}
