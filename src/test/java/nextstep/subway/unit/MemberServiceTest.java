package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.subway.ApplicationContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberServiceTest extends ApplicationContextTest {
    public static final String EMAIL = "email@email.com";
    public static final String GITHUB_CODE = "a8sd97f9sdfiukl";
    @Autowired
    private MemberService memberService;

    @DisplayName("email 주소로 회원 조회시 존재하지 않으면 생성")
    @Test
    void findOrCreateMember() {
        // when
        var response = memberService.findOrCreateMember(EMAIL, GITHUB_CODE);

        // then
        assertAll(
                () -> assertThat(response.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(response.getAge()).isNull()
        );
    }
}
