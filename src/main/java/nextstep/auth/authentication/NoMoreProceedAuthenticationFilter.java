package nextstep.auth.authentication;

public abstract class NoMoreProceedAuthenticationFilter extends AuthenticationInterceptor {
    @Override
    protected boolean proceed() {
        return false;
    }
}
