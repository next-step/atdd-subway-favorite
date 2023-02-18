package nextstep.subway.unit;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.RoleType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TokenTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Test
    void makeToken() {
        String memberId = "memberId";
        List<String> roles = new ArrayList<>();
        roles.add("Member");
        String token = jwtTokenProvider.createToken(memberId, roles);
        Assertions.assertThat(token).isNotNull();
    }
}
