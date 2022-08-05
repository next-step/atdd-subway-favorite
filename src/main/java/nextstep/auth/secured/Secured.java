package nextstep.auth.secured;

import nextstep.member.domain.RoleType;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Secured {
    RoleType[] value();
}
