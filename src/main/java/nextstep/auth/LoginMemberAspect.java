package nextstep.auth;

import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.IntStream;

@Aspect
@Component
public class LoginMemberAspect {
    private static final String AUTHORIZATION_BEARER = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public LoginMemberAspect(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Around("@annotation(loginMember)")
    public Object validateTokenAndInjectMember(ProceedingJoinPoint joinPoint, LoginMember loginMember) throws Throwable {
        validateToken();
        String username = extractUsername();

        Object[] args = injectMember(joinPoint, username);

        return joinPoint.proceed(args);
    }

    private void validateToken() {
        String authorization = extractAuthorizationHeader();
        validateAuthorization(authorization);
    }

    private String extractAuthorizationHeader() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private void validateAuthorization(String authorization) {
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException();
        }

        if (!authorization.startsWith(AUTHORIZATION_BEARER)) {
            throw new AuthenticationException();
        }
    }

    private String extractUsername() {
        String authorization = extractAuthorizationHeader();
        String token = authorization.replace(AUTHORIZATION_BEARER, "");
        return jwtTokenProvider.getPrincipal(token);
    }

    private Object[] injectMember(ProceedingJoinPoint joinPoint, String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new AuthenticationException());

        Object[] args = joinPoint.getArgs();
        IntStream.range(0, args.length)
                .filter(i -> args[i] instanceof Member)
                .findFirst()
                .ifPresent(i -> args[i] = member);

        return args;
    }
}
