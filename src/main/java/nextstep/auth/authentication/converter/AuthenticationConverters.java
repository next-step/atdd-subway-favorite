package nextstep.auth.authentication.converter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;

@RequiredArgsConstructor
@Component
public class AuthenticationConverters {
    private final List<AuthenticationConverter> converters;

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        return authenticationConverter(request.getRequestURI()).convert(request);
    }

    public AuthenticationConverter authenticationConverter(String requestUri) {
        return converters.stream()
                         .filter(eachConverter -> eachConverter.matchRequestUri(requestUri))
                         .findFirst()
                         .orElseThrow(IllegalAccessError::new);
    }
}
