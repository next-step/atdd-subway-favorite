package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.token.TokenRequest;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        // TODO: request에서 AuthenticationToken 객체 생성하기
        try {
            String requestData = request.getReader()
                .lines()
                .collect(Collectors.joining());

            TokenRequest tokenRequest = new ObjectMapper().readValue(requestData,
                TokenRequest.class);

            return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
        } catch (IOException e) {
            return new AuthenticationToken();
        }
    }
}
