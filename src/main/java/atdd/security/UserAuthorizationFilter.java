package atdd.security;

import atdd.user.service.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserAuthorizationFilter extends OncePerRequestFilter {

    private static final Map<String, Object> ERROR_RESPONSE = new HashMap<>();
    static {
        ERROR_RESPONSE.put("code", HttpStatus.UNAUTHORIZED.value());
        ERROR_RESPONSE.put("message", "인증되지 않은 사용자 입니다.");
    }

    private final AuthorizationService authorizationService;
    private final ObjectMapper objectMapper;

    public UserAuthorizationFilter(AuthorizationService authorizationService, ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.objectMapper = objectMapper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final Date nowDate = new Date();
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!authorizationService.isAuthorized(authorization, nowDate)) {
            sendErrorResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());

        final PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(ERROR_RESPONSE));
        writer.flush();
    }

}
