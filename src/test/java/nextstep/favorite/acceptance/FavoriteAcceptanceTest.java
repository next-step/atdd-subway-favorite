package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static nextstep.favorite.fixture.MemberFixture.joinMember;
import static nextstep.favorite.fixture.MemberFixture.loginMember;
import static nextstep.subway.acceptance.fixture.LineFixture.newLineAndGetId;
import static nextstep.subway.acceptance.fixture.StationFixture.newStationAndGetId;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철역이 등록되어 있다.
     * And 지하철 노선이 등록되어 있다.
     * And 사용자가 회원가입 되어있다.
     * And 사용자가 로그인을 한다.
     * When 지하철역을 사용자의 즐겨찾기에 등록한다.
     * Then 지하철역이 사용자의 즐겨찾기 목록에 추가되었다.
     */
    @DisplayName("즐겨찾기에 지하철역을 등록한다.")
    @Test
    void addFavorite() {
        // given
        Long 강남역 = newStationAndGetId("강남역");
        Long 양재역 = newStationAndGetId("양재역");
        Long 신분당선 = newLineAndGetId("신분당선", "green", 강남역, 양재역, 100);

        joinMember();
        String token = loginMember();
        FavoriteRequest request = new FavoriteRequest(강남역, 양재역);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .statusCode(201)
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(201);
    }
}