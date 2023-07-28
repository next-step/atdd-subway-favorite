package nextstep.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.verify;

import java.util.Optional;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    public static final String EMAIL = "eamil@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String UPDATED_EMAIL = "update@email.com";
    public static final String UPDATED_PASSWORD = "updatePassword";
    public static final int UPDATED_AGE = 10;
    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;

    @DisplayName("회원을 생성한다")
    @Test
    void createMember() {
        // given
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        Member member = mock(Member.class);
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(member.getId()).willReturn(1L);
        given(member.getEmail()).willReturn(EMAIL);
        given(member.getAge()).willReturn(AGE);

        // when
        MemberResponse createdMember = memberService.createMember(memberRequest);

        // then
        Assertions.assertAll(
                () -> assertThat(createdMember.getAge()).isEqualTo(AGE),
                () -> assertThat(createdMember.getEmail()).isEqualTo(EMAIL)
        );
    }

    @DisplayName("회원 아이디로 회원을 조회한다")
    @Test
    void findMemberSuccess() {
        // given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        // when
        MemberResponse memberResponse = memberService.findMember(1L);

        // then
        Assertions.assertAll(
                () -> assertThat(memberResponse.getAge()).isEqualTo(AGE),
                () -> assertThat(memberResponse.getEmail()).isEqualTo(EMAIL)
        );
    }

    @DisplayName("찾는 회원 아이디가 존재하지 않으면 예외를 던진다")
    @Test
    void findMemberFailedByNotExists() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.empty());

        // when,then
        assertThatThrownBy(() -> memberService.findMember(1L))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("회원 정보를 업데이트 한다")
    @Test
    void updateMember() {
        // given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        MemberRequest memberRequest = new MemberRequest(UPDATED_EMAIL, UPDATED_PASSWORD, UPDATED_AGE);

        // when
        memberService.updateMember(1L, memberRequest);

        // then
        Assertions.assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(UPDATED_EMAIL),
                () -> assertThat(member.getPassword()).isEqualTo(UPDATED_PASSWORD),
                () -> assertThat(member.getAge()).isEqualTo(UPDATED_AGE)
        );
    }

    @DisplayName("회원 정보를 업데이트 요청시 회원이 존재하지 않이면 예외를 던진다")
    @Test
    void updateMemberFailed() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.empty());
        MemberRequest memberRequest = new MemberRequest(UPDATED_EMAIL, UPDATED_PASSWORD, UPDATED_AGE);

        // when,then
        assertThatThrownBy(() -> memberService.updateMember(1L, memberRequest))
                .isInstanceOf(RuntimeException.class);

    }

    @DisplayName("회원을 삭제 요청은 memberRepository에 위임한다")
    @Test
    void deleteMember() {
        // given,when
        memberService.deleteMember(1L);

        // then
        verify(memberRepository).deleteById(1L);
    }

    @DisplayName("회원 이메일로 회원 조회한다")
    @Test
    void findMemberByEmail() {
        // given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        given(memberRepository.findByEmail(EMAIL)).willReturn(Optional.of(member));

        // when
        MemberResponse memberResponse = memberService.findMemberByEmail(EMAIL);

        // then
        Assertions.assertAll(
                () -> assertThat(memberResponse.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(memberResponse.getAge()).isEqualTo(AGE)
        );
    }

    @DisplayName("회원 이메일로 회원 조회시 회원 이메일이 존재 하지 않으면 예외를 던진다")
    @Test
    void findMemberByEmailFailedByNotExists() {
        // given
        given(memberRepository.findByEmail(EMAIL)).willReturn(Optional.empty());

        // when,then
        assertThatThrownBy(() -> memberService.findMemberByEmail(EMAIL))
                .isInstanceOf(RuntimeException.class);
    }
}
