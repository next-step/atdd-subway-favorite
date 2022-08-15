package nextstep.auth.authentication.provider;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;

public interface AuthenticationManager {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
