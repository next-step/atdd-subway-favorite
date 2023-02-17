package nextstep.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value = MockitoExtension.class)
@DisplayName("회원 관련 기능")
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private MemberService memberService;

    private Member member;

    @BeforeEach
    void setUp() {
        this.member = new Member(
                "email.google.com", "1234", 26,
                List.of(RoleType.ROLE_MEMBER.name(), RoleType.ROLE_ADMIN.name())
        );
    }

    @DisplayName("토큰으로 로그인한다.")
    @Test
    void loginByToken() {
        String expected = "token";
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(jwtTokenProvider.createToken(anyString(), anyList())).thenReturn(expected);

        TokenResponse tokenResponse = memberService.loginBy(new TokenRequest(member.getEmail(), member.getPassword()));

        assertThat(tokenResponse.getAccessToken()).isEqualTo(expected);
    }
}
