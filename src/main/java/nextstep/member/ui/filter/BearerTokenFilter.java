package nextstep.member.ui.filter;

import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BearerTokenFilter implements Filter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String ACCESS_TOKEN = "accessToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authorization = httpRequest.getHeader(AUTHORIZATION);

        if(containsBearerToken(authorization)) {
            httpRequest.setAttribute(ACCESS_TOKEN, getAccessToken(authorization));
        }

        chain.doFilter(httpRequest, response);
    }

    private boolean containsBearerToken(String authorization) {
        return StringUtils.hasText(authorization) && authorization.startsWith(BEARER);
    }

    private String getAccessToken(String authorization) {
        return authorization.replace(BEARER, "");
    }
}
