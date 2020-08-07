package nextstep.subway.auth.ui.interceptor.converter;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

public class TokenAuthenticationConverter implements AuthenticationConverter{
    private static final String BASIC_AUTH_REGEX = ":";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        byte[] decodedBytes= Base64.getDecoder().decode(credentials.getBytes());
        String decodedCredentials = new String(decodedBytes);

        String[] split = decodedCredentials.split(BASIC_AUTH_REGEX);

        String principle = split[0];
        String credential = split[1];

        return new AuthenticationToken(principle, credential);
    }
}
