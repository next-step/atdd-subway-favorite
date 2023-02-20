package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.exception.BadCredentialException;
import nextstep.member.domain.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    private Long id;

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";
    private static final String WRONG_EMAIL = "wrong@wrong.com";
    private static final String WRONG_PASSWORD = "wrong";

    @BeforeEach
    void setUp() {
        id = memberService.createMember(new MemberRequest(EMAIL, PASSWORD, 10)).getId();
    }

    @DisplayName("올바른 이메일과 비밀번호를 입력하면 로그인한다.")
    @Test
    void test() {
        final Member member = memberService.authenticate(new TokenRequest(EMAIL, PASSWORD));

        assertThat(member.getId()).isEqualTo(id);
    }

    @DisplayName("잘못된 이메일과 올바른 비밀번호를 입력하면 오류가 발생한다.")
    @Test
    void wrongEmailAndCorrectPassword() {
        assertThatThrownBy(() -> memberService.authenticate(new TokenRequest(WRONG_EMAIL, PASSWORD)))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("올바른 이메일과 잘못된 비밀번호를 입력하면 오류가 발생한다.")
    @Test
    void correctEmailAndWrongPassword() {
        assertThatThrownBy(() -> memberService.authenticate(new TokenRequest(EMAIL, WRONG_PASSWORD)))
                .isInstanceOf(BadCredentialException.class);
    }
}
