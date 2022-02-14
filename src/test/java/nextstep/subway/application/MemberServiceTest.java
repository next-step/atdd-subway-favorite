package nextstep.subway.application;

import nextstep.subway.application.dto.member.MemberResponse;
import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.application.DtoFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {
    private static final String EMAIL = "login@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 26;

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void init() {
        member = new Member(EMAIL, PASSWORD, AGE);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("내 정보 수정을 처리한다.")
    void updateMember() {
        // when
        MemberResponse changedMemberResponse = memberService.updateMember(member.getId(), createMemberRequest());

        // then
        assertThat(changedMemberResponse.getEmail()).isEqualTo(CHANGED_EMAIL);
        assertThat(changedMemberResponse.getAge()).isEqualTo(CHANGED_AGE);
    }

    @Test
    @DisplayName("내 정보 삭제를 처리한다.")
    void deleteMember() {
        // when
        memberService.deleteMember(member.getId());
    }
}