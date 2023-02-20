package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.Github의_access_token을_요청;
import static nextstep.subway.acceptance.MemberSteps.JWT_토큰으로_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.응답_코드가_일치한지_확인;
import static nextstep.subway.acceptance.MemberSteps.응답에서_access_token_일치_여부_확인;
import static nextstep.subway.acceptance.MemberSteps.응답에서_access_token_존재_여부_확인;
import static nextstep.subway.acceptance.MemberSteps.응답에서_email_정보_확인;
import static nextstep.subway.acceptance.MemberSteps.응답에서_id_정보_있는지_확인;
import static nextstep.subway.acceptance.MemberSteps.응답에서_나이_정보_확인;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.infra.github.mock.GithubFakeResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        응답_코드가_일치한지_확인(response, HttpStatus.OK);
        응답에서_access_token_존재_여부_확인(response);
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        ExtractableResponse<Response> response = JWT_토큰으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        응답_코드가_일치한지_확인(response, HttpStatus.OK);
        응답에서_id_정보_있는지_확인(response);
        응답에서_email_정보_확인(response, EMAIL);
        응답에서_나이_정보_확인(response, 20);
    }

    /**
     * Given Github Client가 정상적으로 작동한다고 가정할 때 (Fake),
     * Given Github code를 가지고,
     * When Github을 통한 로그인 요청을 하면
     * Then access token을 발급 받는다.
     */
    @DisplayName("Github Auth를 통한 로그인을 성공한다")
    @ParameterizedTest
    @EnumSource(value = GithubFakeResponses.class)
    void githubAuth(GithubFakeResponses githubResponse) {
        // Given
        String givenCode = githubResponse.getCode();
        String givenAccessToken = githubResponse.getAccessToken();

        // When
        ExtractableResponse<Response> response = Github의_access_token을_요청(givenCode);

        // Then
        응답_코드가_일치한지_확인(response, HttpStatus.OK);
        응답에서_access_token_존재_여부_확인(response);
        응답에서_access_token_일치_여부_확인(response, givenAccessToken);
    }

    /**
     * Given Github Client가 정상적으로 작동한다고 가정할 때 (Fake),
     * Given 유효하지 않은 Github code를 가지고
     * When Github을 통한 로그인 요청을 하면
     * Then 401 Unauthorized 응답을 받는다.
     */
    @DisplayName("잘못된 code를 통한 Github 로그인은 실패한다")
    @Test
    void failToGithubLogin() {
        // Given
        String givenInvalidCode = "invalid_code";

        // When
        ExtractableResponse<Response> response = Github의_access_token을_요청(givenInvalidCode);

        // Then
        응답_코드가_일치한지_확인(response, HttpStatus.UNAUTHORIZED);
    }
}
