package nextstep.member.unit;

import nextstep.member.application.MemberService;
import nextstep.member.application.TokenService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
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
    private MemberService memberService;

    @Autowired
    private TokenService tokenService;

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
}
