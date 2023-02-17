package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("회원 관련 Mock 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceMockTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    @DisplayName("내 정보 조회에 성공한다.")
    @Test
    void success_findMemberOfMine() {

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(createMember(EMAIL, PASSWORD, 10)));

        final MemberResponse memberResponse = memberService.findMemberOfMine(EMAIL);

        assertAll(
                () -> assertThat(memberResponse.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(memberResponse.getAge()).isEqualTo(10)
        );
    }

    private Member createMember(final String email, final String password, final Integer age) {
        return new Member(email, password, age);
    }
}
