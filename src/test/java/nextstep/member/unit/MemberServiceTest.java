package nextstep.member.unit;


import nextstep.member.application.MemberService;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void init() {
        memberRepository.deleteAll();
    }

    @DisplayName("회원이 등록되어 있지 않을 때, 회원이 등록된다.")
    @Test
    void findMemberOrCreate_unRegisterMember() {
        final String email = GithubResponses.사용자1.email();
        final Integer age = 31;
        assertThatThrownBy(() -> {memberService.findMemberByEmail(email);})
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록된 회원이 아닙니다.");

        memberService.findMemberOrCreate(new GithubProfileResponse(email, age));

        MemberResponse result = memberService.findMemberByEmail(email);
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @DisplayName("회원이 등록되어 있을 때")
    @Test
    void findMemberOrCreate_RegisterMember() {
        final String email = GithubResponses.사용자1.email();
        final Integer age = 31;
        final Member member = new Member(email, "", age);
        memberRepository.save(member);

        memberService.findMemberOrCreate(new GithubProfileResponse(email, age));

        MemberResponse result = memberService.findMemberByEmail(email);
        assertThat(result.getEmail()).isEqualTo(email);
    }
}
