package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;

import java.util.List;

public class ProviderManager {
    private final List<AuthenticationProvider> providers;

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        Authentication authentication = null;
        AuthenticationException exception = null;

        for (AuthenticationProvider provider : providers) {
            try {
                if (provider.supports(authenticationToken.getClass())) {
                    authentication = provider.authenticate(authenticationToken);
                }
            } catch (AuthenticationException e) {
                exception = e;
            }

            if (authentication != null) {
                return authentication;
            }
        }

        if (exception != null) {
            throw exception;
        }

        throw new AuthenticationException();
    }


}
