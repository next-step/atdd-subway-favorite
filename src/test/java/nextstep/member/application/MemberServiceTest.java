package nextstep.member.application;

import nextstep.common.ErrorMessage;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.NoMemberExistException;
import org.assertj.core.api.Assertions;
import org.hibernate.QueryTimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final String OAUTH_DEFAULT_PASSWORD = "defaultPassword";
    private final String email = "test@test.com";
    private final int age = 20;

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
    }

    @DisplayName("기존에 가입한 회원인 경우 정상적으로 회원 정보를 응답합니다.")
    @Test
    void exist() {
        // given
        Member savedMember = new Member(email, OAUTH_DEFAULT_PASSWORD, age);
        Mockito.doReturn(Optional.of(savedMember)).when(memberRepository).findByEmail(email);

        // when
        Member member = memberService.findMemberByUserResource(email, age);
        // then
        Assertions.assertThat(member.getEmail()).isEqualTo(email);
        Assertions.assertThat(member.getPassword()).isEqualTo(OAUTH_DEFAULT_PASSWORD);
        Assertions.assertThat(member.getAge()).isEqualTo(age);
    }

    @DisplayName("이미 가입된 회원이 아닌 경우 회원을 새로 가입시키고 가입 정보를 응답합니다.")
    @Test
    void notExist() {
        // given
        Member savedMember = new Member(email, OAUTH_DEFAULT_PASSWORD, age);

        Mockito.doThrow(NoMemberExistException.class)
                .doReturn(Optional.of(savedMember))
                .when(memberRepository).findByEmail(email);
        Mockito.doReturn(savedMember).when(memberRepository).save(Mockito.any(Member.class));

        // when
        Member resultMember = memberService.findMemberByUserResource(email, age);
        // then
        Assertions.assertThat(resultMember.getEmail()).isEqualTo(email);
        Assertions.assertThat(resultMember.getPassword()).isEqualTo(OAUTH_DEFAULT_PASSWORD);
        Assertions.assertThat(resultMember.getAge()).isEqualTo(age);
    }

    @DisplayName("이미 가입된 사용자지만 중간에 네트워크 에러가 발생하는 경우 서버 에러를 발생합니다.")
    @Test
    void serverError() {
        // given
        Mockito.doThrow(QueryTimeoutException.class)
                .when(memberRepository).findByEmail(email);
        // when
        // then
        Assertions.assertThatThrownBy(() -> memberService.findMemberByUserResource(email, age))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(ErrorMessage.SERVER_ERROR.getMessage());
    }

}