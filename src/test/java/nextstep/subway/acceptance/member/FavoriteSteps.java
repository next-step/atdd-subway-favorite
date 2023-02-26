package nextstep.subway.acceptance.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.subway.acceptance.AbstractSteps;

public class FavoriteSteps extends AbstractSteps {

    private static final String BEARER = "Bearer ";

    public static ExtractableResponse<Response> 즐겨찾기_등록(String accessToken, Long source, Long target) {
        FavoriteRequest request = new FavoriteRequest(source, target);

        return oauth2(accessToken)
            .body(request)
            .when().post("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return oauth2(accessToken)
            .when().get("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String accessToken, Long id) {
        return oauth2(accessToken)
            .pathParam("id", id)
            .when().delete("/favorites/{id}")
            .then().log().all().extract();
    }
}
