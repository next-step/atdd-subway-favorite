package nextstep.auth.secured;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.RoleType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class SecuredAnnotationChecker {

    public static final String ROLE_AUTHENTICATION_EX = "권한이 없습니다.";

    @Before("@annotation(nextstep.auth.secured.Secured)")
    public void checkAuthorities(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Secured secured = method.getAnnotation(Secured.class);
        List<RoleType> values = Arrays.stream(secured.value()).collect(Collectors.toList());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RoleAuthenticationException(ROLE_AUTHENTICATION_EX);
        }

        authentication.getAuthorities().stream()
                .filter(auth -> values.stream().anyMatch(value -> value.name().equals(auth)))
                .findFirst()
                .orElseThrow(() -> new RoleAuthenticationException(ROLE_AUTHENTICATION_EX));
    }
}
