package nextstep.subway.unit.member.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.common.domain.model.exception.EntityNotFoundException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.MemberRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService 테스트")
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
    }

    @DisplayName("요청한 ID를 가지는 Entity가 없다면 EntityNotFoundException 반환")
    @Test
    void verifyExists() {
        // Given
        when(memberRepository.existsById(anyLong()))
            .thenReturn(false);

        // When, Then
        assertThatThrownBy(() -> memberService.verifyExists(1L))
            .isInstanceOf(EntityNotFoundException.class);
    }
}
