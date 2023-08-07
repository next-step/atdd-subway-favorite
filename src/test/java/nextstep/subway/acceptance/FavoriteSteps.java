package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static Long 즐겨찾기_추가(String accessToken, Long sourceId, Long targetId) {
        ExtractableResponse<Response> response = 즐겨찾기_추가_요청(accessToken, sourceId, targetId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return Long.valueOf(response.header("Location").split("/")[2]);
    }

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(
        String accessToken,
        Long sourceId,
        Long targetId
    ) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(favoriteRequest)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static List<FavoriteResponse> 즐겨찾기_조회(String accessToken) {
        ExtractableResponse<Response> responses = 즐겨찾기_조회_요청(accessToken);

        assertThat(responses.statusCode()).isEqualTo(HttpStatus.OK.value());

        return responses.jsonPath().getList(".", FavoriteResponse.class);
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/favorites/{id}", favoriteId)
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_삭제(String accessToken, Long favoriteId) {
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, favoriteId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_목록에_즐겨찾기가_존재한다(
        List<FavoriteResponse> favoriteResponses,
        Long sourceId,
        Long targetId
    ) {
        FavoriteResponse favoriteResponse = favoriteResponses.get(0);

        assertThat(favoriteResponse.getSource().getId()).isEqualTo(sourceId);
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(targetId);
    }

    public static void 즐겨찾기_목록에_즐겨찾기가_존재하지_않는다(
        List<FavoriteResponse> favoriteResponses,
        Long favoriteId
    ) {
        assertThat(
            favoriteResponses.stream()
                .anyMatch(it -> it.getId().equals(favoriteId))
        )
            .isFalse();
    }

    public static void 상태코드_401_응답(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 상태코드_400_응답(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
