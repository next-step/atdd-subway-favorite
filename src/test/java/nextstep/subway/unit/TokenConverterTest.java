package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static nextstep.subway.unit.authtarget.AuthTarget.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TokenConverterTest {

  AuthenticationConverter authenticationConverter;

  @BeforeEach
  void setup() {
    authenticationConverter = new TokenConverter(new ObjectMapper());
  }

  @Test
  void convert() throws IOException {
    // when
    AuthenticationToken authenticationToken = authenticationConverter.convert(createTokenMockRequest());

    // then
    assertAll(
      () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
      () -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
    );
  }

}