package nextstep.subway.auth.application.converter;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.util.ConvertUtils;
import org.springframework.data.util.Pair;

import javax.servlet.http.HttpServletRequest;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        final String payload = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        final String decodedPayload = ConvertUtils.b64Decode(payload);
        final Pair<String, String> emailAndPasswordPair = getEmailAndPasswordPairFrom(decodedPayload);
        return new AuthenticationToken(emailAndPasswordPair.getFirst(), emailAndPasswordPair.getSecond());
    }

    private Pair<String, String> getEmailAndPasswordPairFrom(String basicAuthString) {
        final String[] split = basicAuthString.split(":");
        return Pair.of(split[0], split[1]);
    }
}
