package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.config.exception.EmailInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.member.config.message.MemberError.NOT_INPUT_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("회원 관련 테스트")
@SpringBootTest
@Transactional
class MemberServiceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @DisplayName("내 정보 조회에 성공한다.")
    @Test
    void success_findMemberOfMine() {

        memberRepository.save(createMember(EMAIL, PASSWORD, 10));

        final MemberResponse memberResponse = memberService.findMemberOfMine(EMAIL);

        assertAll(
                () -> assertThat(memberResponse.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(memberResponse.getAge()).isEqualTo(10)
        );
    }

    @DisplayName("내 정보 조회 시 이메일 정보가 달러서 실패한다.")
    @Test
    void error_findMemberOfMine() {

        memberRepository.save(createMember(EMAIL, PASSWORD, 10));

        assertThatThrownBy(() -> memberService.findMemberOfMine("different email"))
                .isInstanceOf(EmailInputException.class)
                .hasMessage(NOT_INPUT_EMAIL.getMessage());
    }

    private Member createMember(final String email, final String password, final Integer age) {
        return new Member(email, password, age);
    }
}