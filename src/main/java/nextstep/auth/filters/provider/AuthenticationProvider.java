package nextstep.auth.filters.provider;

import nextstep.auth.user.UserDetails;

public interface AuthenticationProvider<T> {
    UserDetails provide(T token);
}
