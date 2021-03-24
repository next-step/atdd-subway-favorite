package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteSteps {

  public static ExtractableResponse<Response> 즐겨찾기_경로추가_요청(TokenResponse tokenResponse,long sourceId, long targetId){
    FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId,targetId);
    return RestAssured.given().log().all()
        .auth().oauth2(tokenResponse.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(favoriteRequest)
        .when().post("/favorites")
        .then().log().all()
        .statusCode(HttpStatus.CREATED.value()).extract();
  }

  public static ExtractableResponse<Response> 즐겨찾기_목록_조회요청(TokenResponse tokenResponse){
    return RestAssured.given().log().all()
        .auth().oauth2(tokenResponse.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/favorites")
        .then().log().all()
        .statusCode(HttpStatus.OK.value()).extract();
  }

  public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse,long id){
    return RestAssured.given().log().all()
        .auth().oauth2(tokenResponse.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().delete("/favorites/{id}",id)
        .then().log().all()
        .statusCode(HttpStatus.OK.value()).extract();
  }

  public static void 즐겨찾기에_경로추가됨(ExtractableResponse<Response> response){
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  public static void 로그인되어있지_않음(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

}
