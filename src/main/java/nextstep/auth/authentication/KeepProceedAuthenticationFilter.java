package nextstep.auth.authentication;

public abstract class KeepProceedAuthenticationFilter extends AuthenticationInterceptor {
    @Override
    protected boolean proceed() {
        return true;
    }
}
