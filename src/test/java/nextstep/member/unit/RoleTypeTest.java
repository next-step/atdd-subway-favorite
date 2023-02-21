package nextstep.member.unit;

import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;

class RoleTypeTest {

    @Test
    void convert() {
        List<RoleType> roles = RoleType.convert(List.of("ROLE_ADMIN"));

        assertThat(roles).isEqualTo(List.of(ROLE_ADMIN));
    }
}
