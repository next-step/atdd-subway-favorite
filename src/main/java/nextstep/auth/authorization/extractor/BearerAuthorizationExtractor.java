package nextstep.auth.authorization.extractor;

public class BearerAuthorizationExtractor extends AuthorizationExtractor {

    @Override
    protected AuthorizationType findAuthorizationType() {
        return AuthorizationType.BEARER;
    }
}
