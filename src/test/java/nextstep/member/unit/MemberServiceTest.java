package nextstep.member.unit;

import nextstep.member.application.MemberNotFoundException;
import nextstep.member.application.MemberService;
import nextstep.member.application.WrongPasswordException;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class MemberServiceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private MemberService memberService;

    @Test
    void authenticate() {
        // When
        Member member = memberService.authenticate(EMAIL, PASSWORD);

        // Then
        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(member.getPassword()).isEqualTo(PASSWORD)
        );
    }

    @Test
    void wrongPasswordAuthenticate() {
        // When & Then
        assertThatThrownBy(() -> memberService.authenticate(EMAIL, "틀린 암호"))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    void notExistEmailAuthenticate() {
        // When & Then
        assertThatThrownBy(() -> memberService.authenticate("존재하지 않는 이메일", PASSWORD))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
