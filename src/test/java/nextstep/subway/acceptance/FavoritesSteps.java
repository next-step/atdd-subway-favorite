package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.authGiven;
import static nextstep.subway.acceptance.MemberSteps.관리자Bearer토큰;

public final class FavoritesSteps {

    private FavoritesSteps() {
        throw new IllegalStateException();
    }

    public static ExtractableResponse<Response> 즐겨찾기추가(final Long source, final Long target) {
        return authGiven(관리자Bearer토큰())
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("source", source, "target", target))
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기조회(final Long id) {
        return authGiven(관리자Bearer토큰())
                .given().log().all()
                .when().post("/favorites/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기삭제(final Long id) {
        return authGiven(관리자Bearer토큰())
                .given().log().all()
                .when().delete("/favorites/{id}", id)
                .then().log().all().extract();
    }

}
