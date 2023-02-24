package nextstep.config.filter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.member.domain.AuthType;
import nextstep.member.domain.AuthTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_URL = "/members/me";
    private final AuthTypes authTypes;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals(AUTH_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        AuthType authType = authTypes.findAuth(header);
        authType.validate(header);

        filterChain.doFilter(request, response);
    }
}
