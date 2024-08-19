package nextstep.member.unit;

import nextstep.member.application.GithubClient;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.TokenService;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.utils.GithubResponses.사용자1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("토큰 서비스 관련 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({"test", "databaseCleanup"})
public class TokenServiceTest {

    @Mock
    private MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private GithubClient githubClient;

    @DisplayName("토큰 생성 함수는, 깃헙에서 발급된 코드를 입력받으면 지하철 서비스의 접근 토큰을 반환한다.")
    @Test
    void createTokenTest() {
        // given
        Member 회원1 = new Member(1L, 사용자1.getEmail(), "", 사용자1.getAge());
        when(memberService.findMemberByEmail(사용자1.getEmail())).thenReturn(회원1);

        TokenService tokenService = new TokenService(memberService, jwtTokenProvider, githubClient);

        // when
        TokenResponse accessToken = tokenService.createToken(사용자1.getCode());

        // then
        assertThat(accessToken.getAccessToken()).isNotBlank();
    }
}
