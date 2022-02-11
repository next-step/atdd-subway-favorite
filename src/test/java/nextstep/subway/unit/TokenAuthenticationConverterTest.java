package nextstep.subway.unit;

import nextstep.auth.application.TokenAuthenticationConverter;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static nextstep.subway.unit.AuthenticationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TokenAuthenticationConverterTest {

    @DisplayName("토큰 기반 토큰 변환")
    @Test
    void converter() throws IOException {
        //given
        AuthenticationConverter converter = new TokenAuthenticationConverter(FIXTURE_OBJECT_MAPPER);

        //when
        AuthenticationToken token = converter.convert(createMockRequest());

        //then
        assertAll(
                () -> assertThat(token.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(token.getCredentials()).isEqualTo(PASSWORD)
        );

    }

}
