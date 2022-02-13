package nextstep.auth.unit.service;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.createMockSessionRequest;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.createMockTokenRequest;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthenticationConverterTest {

    @Autowired
    private AuthenticationConverter sessionAuthenticationConverter;

    @Autowired
    private AuthenticationConverter tokenAuthenticationConverter;

    @Test
    void sessionAuthenticationConvert() throws IOException {
        MockHttpServletRequest request = createMockSessionRequest();
        AuthenticationToken authenticationToken = sessionAuthenticationConverter.convert(request);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void tokenAuthenticationConvert() throws IOException {
        MockHttpServletRequest request = createMockTokenRequest();
        AuthenticationToken authenticationToken = tokenAuthenticationConverter.convert(request);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}
