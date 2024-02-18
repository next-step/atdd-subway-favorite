package nextstep.member.unit;


import nextstep.member.application.MemberService;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @MockBean
    private MemberRepository memberRepository;

    @DisplayName("회원이 등록되어 있지 않을 때, 회원이 등록된다.")
    @Test
    void findMemberOrCreate_unRegisterMember() {
        final String email = GithubResponses.사용자1.email();
        final Integer age = 31;
        final Member member = new Member(email, "", age);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(memberRepository.save(member)).thenReturn(member);

        memberService.findMemberOrCreate(new GithubProfileResponse(email, age));

        verify(memberRepository).save(member);
    }

    @DisplayName("회원이 등록되어 있을 때")
    @Test
    void findMemberOrCreate_RegisterMember() {
        final String email = GithubResponses.사용자1.email();
        final Integer age = 31;
        final Member member = new Member(email, "", age);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.ofNullable(member));

        memberService.findMemberOrCreate(new GithubProfileResponse(email, age));

        verify(memberRepository).save(member);
    }
}
