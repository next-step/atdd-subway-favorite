package nextstep.member.unit;

import nextstep.auth.domain.UserDetails;
import nextstep.member.auth.MemberUserDetails;
import nextstep.member.auth.MemberUserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberUserDetailsServiceTest {

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberUserDetailsService userDetailsService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(EMAIL, PASSWORD, 10);
    }

    @Test
    @DisplayName("유효한 사용자명으로 UserDetails를 로드한다")
    void loadUserByUsername_WithValidUsername_ReturnsUserDetails() {
        // Given
        when(memberRepository.findByEmailOrElseThrow(EMAIL)).thenReturn(member);

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(EMAIL);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(EMAIL);
        assertThat(userDetails.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    @DisplayName("존재하지 않는 사용자명으로 UserDetails를 로드하려고 하면 예외가 발생한다")
    void loadUserByUsername_WithInvalidUsername_ThrowsException() {
        // Given
        when(memberRepository.findByEmailOrElseThrow(EMAIL)).thenThrow(new MemberNotFoundException("Member not found"));

        // When & Then
        assertThrows(MemberNotFoundException.class, () -> userDetailsService.loadUserByUsername(EMAIL));
    }
}
