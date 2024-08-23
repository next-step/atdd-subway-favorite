package nextstep.member.unit;

import nextstep.member.application.MemberService;
import nextstep.member.application.TokenService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.utils.GithubResponses.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("토큰 서비스 관련 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class TokenServiceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute(this);
    }

    @DisplayName("토큰 생성 함수는, 깃헙에서 발급된 코드를 입력받으면 지하철 서비스의 접근 토큰을 반환한다.")
    @Test
    void createTokenTest() {
        // given
        memberService.createMember(new MemberRequest(사용자1.getEmail(), 사용자1.getPassword(), 사용자1.getAge()));

        // when
        TokenResponse accessToken = tokenService.createToken(사용자1.getCode());

        // then
        assertThat(accessToken.getAccessToken()).isNotBlank();
    }

    @DisplayName("토큰 생성 함수는, 회원가입 되어 있지 않은 사용자의 코드를 입력받으면 회원가입 진행 후 토큰을 반환한다.")
    @Test
    void createTokenOfNotMemberTest() {
        // when
        TokenResponse accessToken = tokenService.createToken(사용자1.getCode());

        // then
        assertThat(accessToken.getAccessToken()).isNotBlank();
    }
}
