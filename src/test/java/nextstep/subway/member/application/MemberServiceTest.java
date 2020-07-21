package nextstep.subway.member.application;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("멤버 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    @Mock
    private MemberRepository memberRepository;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        memberService = new MemberService(memberRepository);
    }

    @Test
    @DisplayName("멤버를 생성하여 저장한다.")
    void createMember() {
        //given
        MemberRequest request = new MemberRequest(EMAIL, PASSWORD, AGE);
        Member member = reflectionMember(1L);

        given(memberRepository.save(any())).willReturn(member);

        //when
        MemberResponse memberResponse = memberService.createMember(request);

        //then
        assertThat(memberResponse).isNotNull()
                .isEqualToComparingFieldByField(new MemberResponse(1L, EMAIL, AGE));
    }

    @Test
    @DisplayName("멤버를 id로 찾아 응답한다.")
    void findMember() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(reflectionMember(1L)));

        //when
        MemberResponse memberResponse = memberService.findMember(1L);

        //then
        assertThat(memberResponse).isNotNull()
                .isEqualToComparingFieldByField(new MemberResponse(1L, EMAIL, AGE));
    }

    @Test
    @DisplayName("멤버를 email로 찾아 응답한다.")
    void findMemberByEmail() {
        //given
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(reflectionMember(1L)));

        //when
        MemberResponse memberResponse = memberService.findMemberByEmail(EMAIL);

        //then
        assertThat(memberResponse).isNotNull()
                .isEqualToComparingFieldByField(new MemberResponse(1L, EMAIL, AGE));
    }

    @Test
    @DisplayName("멤버의 필드값은 업데이트 한다.")
    void updateMember() {
        //given
        setUpSecurityContext();
        Member member = mock(Member.class);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        //when
        memberService.updateMember(1L, new MemberRequest(EMAIL, "123", AGE));

        //then
        verify(member).update(any());
    }

    @Test
    @DisplayName("현재 로그인된 멤버와 다른 멤버는 수정 요청 시 권한 에러가 발생한다.")
    void updateMemberNoPermission() {
        //given
        setUpSecurityContext();

        assertThatThrownBy(() ->
                //when
                memberService.updateMember(4L, new MemberRequest(EMAIL, "123", AGE)))
                //then
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("멤버의 id로 멤버를 삭제한다.")
    @Test
    void deleteMember() {
        //given
        setUpSecurityContext();

        //when
        memberService.deleteMember(1L);

        //then
        verify(memberRepository).deleteById(1L);
    }

    @DisplayName("현재 로그인된 멤버와 다른 멤버는 수정 요청 시 권한 에러가 발생한다.")
    @Test
    void deleteMemberNoPermission() {
        //given
        setUpSecurityContext();

        assertThatThrownBy(() ->
                //when
                memberService.deleteMember(3L))
                //then
                .isInstanceOf(AuthorizationException.class);
    }

    private Member reflectionMember(long id) {
        Member member = new Member(EMAIL, PASSWORD, AGE);
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    private void setUpSecurityContext() {
        SecurityContextHolder.setContext(new SecurityContext(new Authentication(new LoginMember(1L, EMAIL, PASSWORD, AGE))));
    }
}