package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;

public interface AuthenticationManager {
    Authentication load(String principal, String credentials);
}
