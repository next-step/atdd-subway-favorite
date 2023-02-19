package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.exception.BadCredentialException;
import nextstep.exception.MemberNotFoundException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;

@SpringBootTest
@Transactional
class MemberServiceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_EMAIL = "admin999@email.com";
    private static final String WRONG_PASSWORD = "password999";

    @Autowired
    private MemberService memberService;

    @DisplayName("이메일 주소와 비밀번호로 로그인한다.")
    @Test
    void authenticate() {
        // when
        Member member = memberService.authenticate(EMAIL, PASSWORD);

        // then
        assertAll(
            () -> assertThat(member.getEmail()).isEqualTo(EMAIL),
            () -> assertThat(member.getPassword()).isEqualTo(PASSWORD)
        );
    }

    @DisplayName("잘못된 이메일 주소로 로그인 시, 해당 사용자를 찾을 수 없다.")
    @Test
    void memberNotFound() {
        // when & then
        assertThatThrownBy(() -> memberService.authenticate(WRONG_EMAIL, PASSWORD))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("잘못된 비밀번호로 로그인 시, 예외가 발생한다.")
    @Test
    void badCredential() {
        // when & then
        assertThatThrownBy(() -> memberService.authenticate(EMAIL, WRONG_PASSWORD))
            .isInstanceOf(BadCredentialException.class);
    }

    @DisplayName("이메일 주소로 회원을 조회했을 때, 회원이 존재하지 않으면 해당 이메일 주소로 회원을 생성한다.")
    @Test
    void findOrCreateMember() {
        Member member = memberService.findOrCreateMember(WRONG_EMAIL);

        assertAll(
            () -> assertThat(member.getEmail()).isEqualTo(WRONG_EMAIL),
            () -> assertThat(member.getPassword()).isNull(),
            () -> assertThat(member.getAge()).isNull(),
            () -> assertThat(member.getRoles()).containsExactly(RoleType.ROLE_MEMBER.name())
        );
    }
}
