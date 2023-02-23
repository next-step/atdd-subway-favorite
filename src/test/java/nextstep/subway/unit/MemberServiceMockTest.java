package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MemberServiceMockTest extends MockTest {

    @Mock
    private MemberRepository memberRepository;

    private MemberService memberService;

    private String email;

    private Member member;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);

        email = "email@email.com";

        member = new Member(email);
        ReflectionTestUtils.setField(member, "id", 1L);
    }

    @DisplayName("이메일로 유저를 찾고 유저를 반환한다.")
    @Test
    void findByEmail() {
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        final Member findMember = memberService.findByEmailOrCreate(email);

        assertThat(findMember.getEmail()).isEqualTo(email);
        verify(memberRepository, times(0)).save(member);
    }

    @DisplayName("이메일로 유저를 찾고 유저가 없으면 생성하여 반환한다.")
    @Test
    void findByEmailOrElseSaveMember() {
        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        final Member findMember = memberService.findByEmailOrCreate(email);

        assertThat(findMember).isEqualTo(member);
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}
