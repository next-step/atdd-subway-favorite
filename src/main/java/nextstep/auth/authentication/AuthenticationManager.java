package nextstep.auth.authentication;

import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.context.Authentication;

public interface AuthenticationManager {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
