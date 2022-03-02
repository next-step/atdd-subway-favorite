package nextstep.auth.authentication.converter;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import org.json.JSONObject;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String requestInfo = request.getReader()
                .lines()
                .findFirst()
                .orElseThrow(AuthenticationException::new);

        JSONObject jsonObject = new JSONObject(requestInfo);

        String principal = jsonObject.getString("email");
        String credentials = jsonObject.getString("password");

        return new AuthenticationToken(principal, credentials);
    }
}
