package nextstep.auth.authentication.filter.processing;

import nextstep.auth.authentication.filter.AuthenticationFilter;

public abstract class AuthenticationProcessingFilter extends AuthenticationFilter {
    @Override
    protected boolean proceed() {
        return false;
    }
}
