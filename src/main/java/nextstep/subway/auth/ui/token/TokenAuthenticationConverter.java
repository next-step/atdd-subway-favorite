package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TokenAuthenticationConverter implements AuthenticationConverter {
    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        final Map<String, String> jwtPayloadMap = readPostRequestBody(request);
        String principal = jwtPayloadMap.get("email");
        String credentials = jwtPayloadMap.get("password");
        return new AuthenticationToken(principal, credentials);
    }

    private Map<String, String> readPostRequestBody(HttpServletRequest request){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
