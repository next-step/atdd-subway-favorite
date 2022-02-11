package nextstep.auth.unit.service;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.AuthenticationConverterFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.Map;

import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.createMockSessionRequest;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.createMockTokenRequest;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthenticationConverterTest {

    @Autowired
    private Map<String, AuthenticationConverter> converters;

    private AuthenticationConverter converter;

    @Test
    void sessionAuthenticationConvert() throws IOException {
        converter = AuthenticationConverterFactory.ofSession(converters);
        MockHttpServletRequest request = createMockSessionRequest();
        AuthenticationToken authenticationToken = converter.convert(request);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void tokenAuthenticationConvert() throws IOException {
        converter = AuthenticationConverterFactory.ofToken(converters);
        MockHttpServletRequest request = createMockTokenRequest();
        AuthenticationToken authenticationToken = converter.convert(request);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}
