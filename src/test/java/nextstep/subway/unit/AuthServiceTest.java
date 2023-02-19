package nextstep.subway.unit;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthService authService;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("test@test.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
    }

    @Test
    void login() {
        // given
        final TokenRequest tokenRequest = new TokenRequest(member.getEmail(), member.getPassword());

        // when
        final TokenResponse tokenResponse = authService.login(tokenRequest);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }
}
