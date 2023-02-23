package nextstep.member.unit;

import nextstep.member.application.MemberNotFoundException;
import nextstep.member.application.MemberService;
import nextstep.member.application.WrongPasswordException;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.utils.DataLoader.ADMIN_EMAIL;
import static nextstep.subway.utils.DataLoader.ADMIN_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void authenticate() {
        // When
        Member member = memberService.authenticate(ADMIN_EMAIL, ADMIN_PASSWORD);

        // Then
        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(ADMIN_EMAIL),
                () -> assertThat(member.getPassword()).isEqualTo(ADMIN_PASSWORD)
        );
    }

    @Test
    void wrongPasswordAuthenticate() {
        // When & Then
        assertThatThrownBy(() -> memberService.authenticate(ADMIN_EMAIL, "틀린 암호"))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    void notExistEmailAuthenticate() {
        // When & Then
        assertThatThrownBy(() -> memberService.authenticate("존재하지 않는 이메일", ADMIN_PASSWORD))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
