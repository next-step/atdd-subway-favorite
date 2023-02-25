package nextstep.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.acceptance.support.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nextstep.acceptance.support.MemberSteps.AccessToken이_JWT_토큰_형식으로_반환된다;
import static nextstep.acceptance.support.MemberSteps.로그인이_성공한다;
import static nextstep.fixture.AuthFixture.베어러_인증_로그인_요청;
import static nextstep.fixture.AuthFixture.알렉스;

@DisplayName("로그인 인증 인수 테스트")
class AuthAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("Bearer 인증 로그인을 요청하면")
    class Context_with_request_bearer_authentication {

        @Test
        @DisplayName("JWT 토큰 형식의 AccessToken이 반환된다")
        void it_returns_access_token() throws Exception {
            ExtractableResponse<Response> 로그인_요청_결과 = 베어러_인증_로그인_요청(알렉스);

            로그인이_성공한다(로그인_요청_결과);
            AccessToken이_JWT_토큰_형식으로_반환된다(로그인_요청_결과);
        }
    }
}
