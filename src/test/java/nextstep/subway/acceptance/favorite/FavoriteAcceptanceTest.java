package nextstep.subway.acceptance.favorite;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.line.LineSteps;
import nextstep.subway.acceptance.member.MemberSteps;
import nextstep.subway.acceptance.station.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    /**
     * Given 로그인이 되어있고
     * And   출발, 도착 지하철 역이 등록 되어있고
     * And   지하철 노선이 등록되어 있고
     * And   지하철 노선에 지하철 역이 등록 되어있을때
     * When  즐겨찾기를 추가 하면
     * Then  즐겨찾기가 추가 된다.
     * */
    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        // given
        String accessToken = MemberSteps.회원_생성_하고_로그인_됨("username@username.com", "password1234", 10);
        Long 교대역 = StationSteps.지하철역_생성_요청_하고_ID_반환("교대역");
        Long 양재역 = StationSteps.지하철역_생성_요청_하고_ID_반환("양재역");
        LineSteps.지하철_노선_생성_요청(LineSteps.createLineCreateParams(교대역, 양재역));

        // when
        FavoriteRequest request = new FavoriteRequest(교대역, 양재역);
        ExtractableResponse<Response> response = 즐겨찾기_추가_요청(accessToken, request);

        // then
        즐겨찾기_추가_요청_됨(response);
    }

    private ExtractableResponse<Response> 즐겨찾기_추가_요청(String accessToken, FavoriteRequest request) {
        return RestAssured.given().log().all()
                          .auth().oauth2(accessToken)
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/favorites")
                          .then().log().all()
                          .extract();
    }

    private void 즐겨찾기_추가_요청_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("location")).isNotBlank();
    }
}
