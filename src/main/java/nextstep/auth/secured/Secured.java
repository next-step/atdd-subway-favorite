package nextstep.auth.secured;

import java.lang.annotation.*;
import nextstep.member.domain.RoleType;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Secured {
    RoleType[] value();
}
