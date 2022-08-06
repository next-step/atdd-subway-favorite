package nextstep.auth.authorization.extractor;

public class BasicAuthorizationExtractor extends AuthorizationExtractor {

    @Override
    protected AuthorizationType findAuthorizationType() {
        return AuthorizationType.BASIC;
    }
}
