package nextstep.subway.auth.ui.interceptor.convert;

import java.util.Base64;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String result = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        byte[] decodedBytes = Base64.getDecoder().decode(result);
        String decodedBytesString = new String(decodedBytes);
        String[] splitContent = splitDecodedBytesString(decodedBytesString);
        String principal = splitContent[0];
        String credentials = splitContent[1];

        return new AuthenticationToken(principal, credentials);
    }

    private String[] splitDecodedBytesString(String decodedBytesString) {
        final String REGEX = ":";
        if (Objects.isNull(decodedBytesString) || !decodedBytesString.contains(REGEX)) {
            throw new RuntimeException("no principal or credentials found.");
        }
        return decodedBytesString.split(REGEX);
    }
}
