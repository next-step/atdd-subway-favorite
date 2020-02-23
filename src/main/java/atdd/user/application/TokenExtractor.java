package atdd.user.application;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenExtractor {
    private static final String HEADER_AUTH = "Authorization";

    public String extract(HttpServletRequest request){
        return request.getHeader(HEADER_AUTH);
    }
}
