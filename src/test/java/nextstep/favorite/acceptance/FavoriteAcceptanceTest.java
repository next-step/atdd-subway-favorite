package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.annotation.AcceptanceTest;
import nextstep.member.acceptance.MemberSteps;
import nextstep.member.acceptance.TokenSteps;
import nextstep.member.application.dto.TokenResponse;
import nextstep.subway.acceptance.fixture.LineFixture;
import nextstep.subway.acceptance.fixture.StationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.util.RestAssuredUtil.생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest{

    private ExtractableResponse<Response> 교대역;
    private ExtractableResponse<Response> 강남역;
    private ExtractableResponse<Response> 이호선;

    private String email = "ksyk1205@naver.com";
    private String password = "12341234aa";

    /**
     * given 지하철 경로를 등록, 토큰을 생성한다.
     * when 즐겨찾기가 등록하면
     * then 등록된다.
     */
    @Test
    @DisplayName("즐겨찾기를 등록한다.")
    void 즐겨찾기등록() {
        //given
        교대역 = 생성_요청(
                StationFixture.createStationParams("교대역"),
                "/stations");
        강남역 = 생성_요청(
                StationFixture.createStationParams("강남역"),
                "/stations");
        이호선 = 생성_요청(
                LineFixture.createLineParams("2호선", "GREEN", 교대역.jsonPath().getLong("id"), 강남역.jsonPath().getLong("id"), 10L),
                "/lines");

        MemberSteps.회원_생성_요청(email, password, 1);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();

        //when
        ExtractableResponse<Response> 즐겨찾기_생성요청 = FavoriteSteps.즐겨찾기_생성요청(교대역.jsonPath().getLong("id"), 강남역.jsonPath().getLong("id"), accessToken);


        //then
        assertThat(즐겨찾기_생성요청.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }
}