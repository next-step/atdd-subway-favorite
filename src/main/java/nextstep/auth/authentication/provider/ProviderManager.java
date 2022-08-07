package nextstep.auth.authentication.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProviderManager {

    private final List<AuthenticationProvider> authenticationProviders;

    public AuthenticationProvider getAuthenticationProvider(ProviderType providerType) {
        return authenticationProviders.stream()
                .filter(provider -> provider.supports(providerType))
                .findAny()
                .orElse(null);
    }
}
