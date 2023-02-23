package nextstep.auth.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtTokenFilter implements Filter {
    private static final String AUTHORIZATION = "authorization";
    private static final String BEARER = "Bearer";
    private static final String ACCESS_TOKEN = "accessToken";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authorization = httpRequest.getHeader(AUTHORIZATION);

        if(authorization.startsWith(BEARER)) {
            httpRequest.setAttribute(ACCESS_TOKEN, getAccessToken(authorization));
        }

        chain.doFilter(httpRequest, response);
    }


    private String getAccessToken(String authorization) {
        return authorization.substring((BEARER + " ").length(), authorization.length());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
