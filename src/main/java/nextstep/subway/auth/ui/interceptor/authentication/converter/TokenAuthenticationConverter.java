package nextstep.subway.auth.ui.interceptor.authentication.converter;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    private static final String AUTHORIZATION_DELIMITER = ":";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String authorization = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);

        byte[] decodedBytes = Base64.getDecoder().decode(authorization.getBytes());
        String decodedAuthorization = new String(decodedBytes);
        String[] split = decodedAuthorization.split(AUTHORIZATION_DELIMITER);

        String principal = split[0];
        String credential = split[1];

        return new AuthenticationToken(principal, credential);
    }
}
