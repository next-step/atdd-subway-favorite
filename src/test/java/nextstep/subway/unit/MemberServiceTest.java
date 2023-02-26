package nextstep.subway.unit;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("findMemberOfMine 테스트 : 정상")
    void findMemberOfMine_test_success() {
        String token = jwtTokenProvider.createToken("admin@email.com", List.of(RoleType.ROLE_ADMIN.name()));
        assertThat(memberService.findMemberOfMine(token)).extracting(MemberResponse::getEmail).isEqualTo("admin@email.com");
    }

    @Test
    @DisplayName("findMemberOfMine 테스트 : 유효하지 않은 토큰")
    void findMemberOfMine_test_invalid_token() {
        assertThatThrownBy(() -> {
            memberService.findMemberOfMine("invalid");
        }).hasMessageContaining("유효하지 않은 토큰입니다.");
    }

}
