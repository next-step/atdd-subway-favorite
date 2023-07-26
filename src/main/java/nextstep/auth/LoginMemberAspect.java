package nextstep.auth;

import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
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
public class LoginMemberAspect {
    public static final String AUTHORIZATION_BEARER = "bearer";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public LoginMemberAspect(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Around("@annotation(loginMember)")
    public Object validateTokenAndInjectMember(ProceedingJoinPoint joinPoint, LoginMember loginMember) throws Throwable {
        String username = validateTokenAndReturnUsername();
        Object[] args = injectMember(joinPoint, username);

        return joinPoint.proceed(args);
    }

    private String validateTokenAndReturnUsername() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        validateAuthorization(authorization);

        String token = authorization.split(" ")[1];
        String username = jwtTokenProvider.getPrincipal(token);

        return username;
    }

    private static void validateAuthorization(String authorization) {
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException();
        }

        if (authorization.split(" ").length != 2) {
            throw new AuthenticationException();
        }

        if (!AUTHORIZATION_BEARER.equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
    }

    private Object[] injectMember(ProceedingJoinPoint joinPoint, String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new AuthenticationException());

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Member) {
                args[i] = member;
                break;
            }
        }
        return args;
    }
}
