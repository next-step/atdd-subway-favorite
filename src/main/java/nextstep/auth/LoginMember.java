package nextstep.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JWT 토큰의 검증과 인증된 사용자의 정보를 Member 객체로 주입하는 데 사용됩니다.
 * 컨트롤러 메서드의 파라미터에 LoginMember 어노테이션을 적용하면, 해당 파라미터에 인증된 사용자의 정보가 자동으로 주입됩니다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoginMember {
}
