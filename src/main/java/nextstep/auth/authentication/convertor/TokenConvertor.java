package nextstep.auth.authentication.convertor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;
import nextstep.subway.applicaion.exception.BusinessException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

public class TokenConvertor implements AuthenticationConverter {

    private ObjectMapper objectMapper;

    public TokenConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            TokenRequest tokenRequest = objectMapper.readValue(requestBody, TokenRequest.class);
            String principal = tokenRequest.getEmail();
            String credentials = tokenRequest.getPassword();

            return new AuthenticationToken(principal, credentials);
        } catch (Exception e) {
            throw new BusinessException("알 수 없는 서버오류", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
