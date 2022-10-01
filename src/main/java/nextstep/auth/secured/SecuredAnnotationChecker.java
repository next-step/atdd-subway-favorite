package nextstep.auth.secured;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.common.exception.AuthException;
import nextstep.common.exception.code.AuthCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
public class SecuredAnnotationChecker {
    @Before("@annotation(nextstep.auth.secured.Secured)")
    public void checkAuthorities(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Secured secured = method.getAnnotation(Secured.class);
        List<String> values = Arrays.stream(secured.value()).map(Enum::name).collect(Collectors.toList());

        Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                                                .orElseThrow(() -> new AuthException(AuthCode.AUTH_INVALID));

        authentication.getAuthorities().stream()
                      .filter(values::contains)
                      .findFirst()
                      .orElseThrow(() -> new AuthException(AuthCode.AUTH_INVALID));
    }
}
