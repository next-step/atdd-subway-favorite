package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

public class BasicAuthenticationConverter implements AuthenticationConverter {

    private static final String REGEX = ":";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String encodedCredential = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        byte[] decodeBytes = Base64.getDecoder().decode(encodedCredential);
        String decodedCredentials = new String(decodeBytes);

        String[] split = decodedCredentials.split(REGEX);

        return new AuthenticationToken(split[0], split[1]);
    }
}
