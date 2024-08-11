package nextstep.member.application;

import nextstep.auth.application.dto.AuthMember;
import nextstep.auth.application.dto.ProfileResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberException;
import nextstep.utils.fakeMock.FakeMemberServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.common.constant.ErrorCode.MEMBER_NOT_FOUND;
import static nextstep.utils.dtoMock.GithubResponse.사용자1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplMockTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private MemberRepository memberRepository;

    private String 존재하지_않는_멤버_EMAIL = "nonexistent@test.com";

    @BeforeEach
    public void setup() {
        userDetailsService = new UserDetailsServiceImpl(new FakeMemberServicesImpl(), memberRepository);

    }

    @DisplayName("[findAuthMemberOrOtherJob] 사용자가 조회되지 않으면, 사용자를 저장한 다음 저장한 값을 반환한다.")
    @Test
    public void memberDoesNotExist() {
        // when
        var 멤버_응답 = userDetailsService.findAuthMemberOrOtherJob(ProfileResponse.of(사용자1.getEmail(), 사용자1.getAge()));

        // then
        assertAll(
                () -> assertThat(멤버_응답.getEmail()).isEqualTo(사용자1.getEmail()),
                () -> assertThat(멤버_응답.getPassword()).isEqualTo(사용자1.getPassword())
        );
    }

    @DisplayName("[findAuthMemberOrOtherJob] 사용자를 조회한 다음, 조회된 사용자를 반환한다.")
    @Test
    public void memberExist() {
        // when
        var 멤버_응답 = userDetailsService.findAuthMemberOrOtherJob(ProfileResponse.of(사용자1.getEmail(), 사용자1.getAge()));

        // then
        assertAll(
                () -> assertThat(멤버_응답.getEmail()).isEqualTo(사용자1.getEmail()),
                () -> assertThat(멤버_응답.getPassword()).isEqualTo(사용자1.getPassword())
        );
    }

    @DisplayName("[findAuthMemberByEmail] 이메일로 인증된 멤버를 성공적으로 찾는다.")
    @Test
    void findAuthMemberByEmail_success() {
        // given
        Member member = Member.of(1L, 사용자1.getEmail(), 사용자1.getPassword(), 사용자1.getAge());

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        // when
        AuthMember actualAuthMember = userDetailsService.findAuthMemberByEmail(member.getEmail());

        // then
        assertAll(
                () -> assertThat(actualAuthMember.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(actualAuthMember.getPassword()).isEqualTo(member.getPassword())
        );
    }

    @DisplayName("[findAuthMemberByEmail] 이메일로 인증된 멤버를 찾지 못할 때 예외를 발생시킨다.")
    @Test
    void findAuthMemberByEmail_notFound() {
        // given
        when(memberRepository.findByEmail(존재하지_않는_멤버_EMAIL)).thenReturn(Optional.empty());

        // when & then
        assertAll(
                () -> assertThrows(MemberException.class, () -> userDetailsService.findAuthMemberByEmail(존재하지_않는_멤버_EMAIL))
                        .getMessage().equals(MEMBER_NOT_FOUND.getDescription())
        );
    }
}

