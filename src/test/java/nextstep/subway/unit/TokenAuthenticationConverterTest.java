package nextstep.subway.unit;

import static nextstep.subway.unit.TokenFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationConverter;
import org.junit.jupiter.api.Test;

public class TokenAuthenticationConverterTest {
    private TokenAuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter(new ObjectMapper());

    @Test
    void convert() throws IOException {
        // when
        AuthenticationToken authenticationToken = tokenAuthenticationConverter.convert(createMockRequest());

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

}
