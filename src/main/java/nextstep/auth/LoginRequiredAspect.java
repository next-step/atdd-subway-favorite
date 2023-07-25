package nextstep.auth;

import nextstep.auth.principal.UserPrincipal;
import nextstep.auth.token.JwtTokenProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoginRequiredAspect {
    private final JwtTokenProvider jwtTokenProvider;

    public LoginRequiredAspect(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Around("@annotation(loginRequired)")
    public Object loginCheck(ProceedingJoinPoint joinPoint, LoginRequired loginRequired) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        validateAuthorization(authorization);
        String token = authorization.split(" ")[1];

        String username = jwtTokenProvider.getPrincipal(token);
        String role = jwtTokenProvider.getRoles(token);
        UserPrincipal userPrincipal = new UserPrincipal(username, role);

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof UserPrincipal) {
                args[i] = userPrincipal;
                break;
            }
        }
        return joinPoint.proceed(args);
    }

    private static void validateAuthorization(String authorization) {
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException();
        }

        if (authorization.split(" ").length != 2) {
            throw new AuthenticationException();
        }

        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
    }
}
