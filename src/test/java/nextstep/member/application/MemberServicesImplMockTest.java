package nextstep.member.application;

import nextstep.auth.exception.UnAuthorizedException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MemberServicesImplMockTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServicesImpl memberService;

    private Member 멤버;
    private MemberRequest 멤버_생성_요청;
    private MemberRequest 멤버_업데이트_요청;
    private Long 멤버_ID = 1L;
    private String 존재하지_않는_멤버_EMAIL = "nonexistent@test.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        멤버 = Member.of(멤버_ID, "test@test.com", "password", 20);
        멤버_생성_요청 = MemberRequest.of(멤버.getEmail(), 멤버.getPassword(), 멤버.getAge());
        멤버_업데이트_요청 = MemberRequest.of("updated@test.com", "newpassword", 30);

    }

    @DisplayName("[createMember] 멤버를 생성한다.")
    @Test
    void createMember_success() {
        // given
        when(memberRepository.save(any(Member.class))).thenReturn(멤버);

        // when
        var 멤버_생성_응답 = memberService.createMember(멤버_생성_요청);

        // then
        verify(memberRepository, times(1)).save(any(Member.class));

        assertAll(
                () -> assertThat(멤버_생성_응답).isNotNull(),
                () -> assertThat(멤버_생성_응답.getEmail()).isEqualTo(멤버_생성_요청.getEmail()),
                () -> assertThat(멤버_생성_응답.getAge()).isEqualTo(멤버_생성_요청.getAge())
        );
    }

    @DisplayName("[findMember] 멤버를 ID로 찾는다.")
    @Test
    void findMember_success() {
        // given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(멤버));

        // when
        MemberResponse response = memberService.findMember(멤버_ID);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(멤버.getEmail());
        assertThat(response.getAge()).isEqualTo(멤버.getAge());
    }

    @DisplayName("[findMember] ID로 멤버를 찾지 못했을 때 예외를 발생시킨다.")
    @Test
    void findMember_notFound() {
        // given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> memberService.findMember(1L));
    }

    @DisplayName("[updateMember] 멤버 정보를 성공적으로 업데이트한다.")
    @Test
    void updateMember_success() {
        // given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(멤버));

        // when
        memberService.updateMember(멤버_ID, 멤버_업데이트_요청);

        // then
        verify(memberRepository, times(1)).findById(멤버_ID);
        assertAll(
                () -> assertThat(멤버.getEmail()).isEqualTo(멤버_업데이트_요청.getEmail()),
                () -> assertThat(멤버.getPassword()).isEqualTo(멤버_업데이트_요청.getPassword()),
                () -> assertThat(멤버.getAge()).isEqualTo(멤버_업데이트_요청.getAge())
        );
    }

    @DisplayName("[updateMember] 멤버를 찾지 못했을 때 예외를 발생시킨다.")
    @Test
    void updateMember_notFound() {
        // given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> memberService.updateMember(멤버_ID, 멤버_업데이트_요청));
    }

    @DisplayName("[deleteMember] 멤버를 성공적으로 삭제한다.")
    @Test
    void deleteMember_success() {
        // when
        memberService.deleteMember(멤버_ID);

        // then
        verify(memberRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("[findMemberByEmail] 주어진 이메일로 멤버를 성공적으로 찾는다.")
    @Test
    void findMemberByEmail_success() {
        // given
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(멤버));

        // when
        Member foundMember = memberService.findMemberByEmail(멤버.getEmail());

        // then
        assertAll(
                () -> assertThat(foundMember).isNotNull(),
                () -> assertThat(foundMember.getEmail()).isEqualTo(멤버.getEmail())
        );
    }

    @DisplayName("[findMemberByEmail] 이메일로 멤버를 찾지 못했을 때 예외를 발생시킨다.")
    @Test
    void findMemberByEmail_notFound() {
        // given
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberException.class, () -> memberService.findMemberByEmail(존재하지_않는_멤버_EMAIL));
    }

    @DisplayName("[findMemberOptionalByEmail] 이메일로 멤버를 찾는다.")
    @Test
    void findMemberOptionalByEmail_success() {
        // given
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(멤버));

        // when
        Optional<Member> foundMember = memberService.findMemberOptionalByEmail(멤버.getEmail());

        // then
        assertAll(
                () -> assertThat(foundMember).isNotEmpty(),
                () -> assertThat(foundMember.get().getEmail()).isEqualTo(멤버.getEmail())
        );
    }

    @DisplayName("[findMemberOptionalByEmail] 이메일로 멤버를 찾지 못했을 때 빈 Optional을 반환한다.")
    @Test
    void findMemberOptionalByEmail_notFound() {
        // given
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        Optional<Member> foundMember = memberService.findMemberOptionalByEmail(존재하지_않는_멤버_EMAIL);

        // then
        assertThat(foundMember).isEmpty();
    }

    @DisplayName("[findMe] 로그인 멤버의 이메일로 멤버를 성공적으로 찾는다.")
    @Test
    void findMe_success() {
        // given
        LoginMember loginMember = new LoginMember(멤버.getEmail());
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(멤버));

        // when
        MemberResponse response = memberService.findMe(loginMember);

        // then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getEmail()).isEqualTo(멤버.getEmail())
        );
    }

    @DisplayName("[findMe] 로그인 멤버의 이메일로 멤버를 찾지 못했을 때 예외를 발생시킨다.")
    @Test
    void findMe_notFound() {
        // given
        LoginMember loginMember = new LoginMember(존재하지_않는_멤버_EMAIL);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> memberService.findMe(loginMember));
    }
}

