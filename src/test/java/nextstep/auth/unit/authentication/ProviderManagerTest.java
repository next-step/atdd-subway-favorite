package nextstep.auth.unit.authentication;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.ProviderManager;
import nextstep.auth.context.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderManagerTest {
    private ProviderManager providerManager;

    @BeforeEach
    void setUp() {
        providerManager = new ProviderManager(Collections.singletonList(new TestAuthenticationProvider()));
    }

    @Test
    void authenticate() {
        String principal = "TEST_PRINCIPAL";
        String credentials = "TEST_CREDENTIALS";
        AuthenticationToken authenticationToken = new TestAuthenticationToken(principal, credentials);

        Authentication authentication = providerManager.authenticate(authenticationToken);

        assertThat(authentication.getPrincipal()).isEqualTo(principal);
    }
}