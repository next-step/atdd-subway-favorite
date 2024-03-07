package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.CommonAcceptanceTest;
import nextstep.utils.GithubResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.member.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthAcceptanceTest extends CommonAcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    /** given 회원 정보를 생성하여 토큰을 발급받고
     *  when 발급된 토큰으로 회원 정보를 요청하면
     *  then 회원 정보를 응답 받는다
     */
    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        //회원 정보 생성
        createMember(EMAIL, PASSWORD, AGE);

        //회원 정보(이메일)로 토큰 발급 (회원 정보가 없으면 오류 발생)
        ExtractableResponse<Response> 회원_로그인_응답 = 회원_로그인_요청(EMAIL, PASSWORD);

        String accessToken = extractAccessToken(회원_로그인_응답);
        assertThat(accessToken).isNotBlank();

        //발급된 토큰으로 회원 정보 찾기
        ExtractableResponse<Response> 회원_정보_응답 = 토큰으로_회원_정보_조회_요청(accessToken);

        //토큰 발급 시 사용한 이메일과 응답 받은 회원 정보가 같은지 검증
        assertThat(extractEmail(회원_정보_응답)).isEqualTo(EMAIL);
    }

    /**
     * given 회원 정보를 생성하고
     * when 코드와 함께 깃헙 로그인 요청을 하면
     * then 액세스 토큰을 발급 받을 수 있다. (로그인 성공)
     */
    @Test
    void 깃허브_로그인_토큰발급() {
        //given
        createMember(GithubResponse.회원.getEmail(), PASSWORD, GithubResponse.회원.getAge());

        //when
        ExtractableResponse<Response> 깃헙_로그인_응답 = 깃헙_로그인_요청(GithubResponse.회원.getCode());

        //then
        assertThat(깃헙_로그인_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        String accessToken = extractAccessToken(깃헙_로그인_응답);
        ExtractableResponse<Response> 회원_정보_응답 = 토큰으로_회원_정보_조회_요청(accessToken);

        assertThat(extractEmail(회원_정보_응답)).isEqualTo(GithubResponse.회원.getEmail());
    }

    /**
     * when 깃허브 토큰이 없는 회원이 로그인 시도를 하면
     * then 인증오류가 발생한다.
     */
    @Test
    void 깃허브_토큰_없는_경우_로그인_인증오류() {
        //when
        ExtractableResponse<Response> 깃헙_로그인_응답 = 깃헙_로그인_요청(GithubResponse.토큰_없는_회원.getCode());

        //then
        assertThat(깃헙_로그인_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * when 비회원이 깃헙 로그인 요청을 하면
     * then 회원 가입 진행 후 토큰을 발급 받는다.
     */
    @Test
    void 비회원_로그인시_회원가입_후_토큰발급() {
        //when
        ExtractableResponse<Response> 깃헙_로그인_응답 = 깃헙_로그인_요청(GithubResponse.비회원.getCode());

        //then
        String accessToken = extractAccessToken(깃헙_로그인_응답);
        ExtractableResponse<Response> 회원_정보_응답 = 토큰으로_회원_정보_조회_요청(accessToken);
        assertThat(extractEmail(회원_정보_응답)).isEqualTo(GithubResponse.비회원.getEmail());
    }

    void createMember(String email, String password, int age) {
        memberRepository.save(new Member(email, password, age));
    }

    String extractEmail(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("email");
    }

    String extractAccessToken(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }
}