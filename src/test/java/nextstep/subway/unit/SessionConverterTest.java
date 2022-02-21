package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SessionConverter;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static nextstep.subway.unit.AuthTarget.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class SessionConverterTest {

  AuthenticationConverter authenticationConverter;

  @BeforeEach
  void setup() {
    this.authenticationConverter = new SessionConverter();
  }

  @Test
  void convert() throws IOException {
    // when
    AuthenticationToken authenticationToken = authenticationConverter.convert(createSessionMockRequest());

    // then
    assertAll(
      () -> AssertionsForClassTypes.assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
      () -> AssertionsForClassTypes.assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
    );
  }
}