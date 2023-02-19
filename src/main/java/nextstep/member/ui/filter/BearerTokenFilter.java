import org.springframework.util.StringUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtTokenFilter implements Filter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authorizationHeader = httpRequest.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());
            httpRequest.setAttribute("accessToken", accessToken);
        }

        chain.doFilter(request, response);
    }
}
