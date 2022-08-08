package nextstep.auth.authentication.provider;

import nextstep.auth.context.Authentication;

public interface AuthenticationProvider<T> {
    Authentication authenticate(T authenticate);
}
