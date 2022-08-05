package nextstep.auth.filters.provider;

import nextstep.auth.user.UserDetails;

@FunctionalInterface
public interface AuthenticationProvider<T> {
    UserDetails provide(T token);
}
