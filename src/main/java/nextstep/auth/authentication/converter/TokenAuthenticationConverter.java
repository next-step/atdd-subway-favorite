package nextstep.auth.authentication.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;
import nextstep.exception.JsonDeserializeFailed;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Scanner;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    private ObjectMapper objectMapper;

    public TokenAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String principal, credentials;
        try {
            TokenRequest tokenRequest = objectMapper.readValue(extractPostRequestBody(request), TokenRequest.class);
            principal = tokenRequest.getEmail();
            credentials = tokenRequest.getPassword();
        } catch (IOException ioe) {
            throw new JsonDeserializeFailed(ioe.getMessage());
        }
        return new AuthenticationToken(principal, credentials);
    }

    private String extractPostRequestBody(HttpServletRequest request) {
        if (HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            Scanner s = null;
            try {
                s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s.hasNext() ? s.next() : "";
        }
        return "";
    }

}