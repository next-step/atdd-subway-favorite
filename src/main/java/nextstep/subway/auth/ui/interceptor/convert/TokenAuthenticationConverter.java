package nextstep.subway.auth.ui.interceptor.convert;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Objects;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter {
    private static final String REGEX = ":";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String result = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);

        byte[] decodedBytes = Base64.getDecoder().decode(result);
        String enCodeString = new String(decodedBytes);
        String principal = "";
        String credentials = "";
        if (Objects.nonNull(enCodeString)) {
            String[] split = enCodeString.split(REGEX);
            principal = split[0];
            credentials = split[1];
        }

        return new AuthenticationToken(principal, credentials);
    }
}
