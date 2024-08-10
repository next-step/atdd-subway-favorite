package nextstep.utils.step;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FavoriteStep {

    public static Long 즐겨찾기_생성(String accessToken, FavoriteRequest favoriteRequest) {
        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value()).extract();

        String locationHeader = response2.header("Location");
        URI locationUri = URI.create(locationHeader);

        String favoriteId = locationUri.getPath().split("/")[2];
        return Long.parseLong(favoriteId);
    }

    public static List<FavoriteResponse> 즐겨찾기_조회(String accessToken) {
        ObjectMapper objectMapper = new ObjectMapper();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        String responseBody = response2.body().asString();
        List<FavoriteResponse> favorites = new ArrayList<>();

        try {
            favorites = objectMapper.readValue(responseBody, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return favorites;
    }

    public static void 즐겨찾기_삭제(String accessToken, Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().delete("/favorites/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value()).extract();
    }
}

