package nextstep.auth.authentication.provider;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;

public interface AuthenticationProvider {

    boolean supports(ProviderType providerType);
    Authentication authenticate(AuthenticationToken token);
}
