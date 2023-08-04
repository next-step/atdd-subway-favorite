package nextstep.acceptance.commonStep;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.dto.FavoritePathRequest;
import org.springframework.http.MediaType;

import static nextstep.acceptance.commonStep.MemberSteps.회원_생성_요청;
import static nextstep.acceptance.commonStep.TokenStep.로그인_요청;


public class FavoritePathStep {

    public static String 회원가입_및_토큰_받기(String email,String password,int age){
        회원_생성_요청(email, password, age);

        return 로그인_요청(email, password).jsonPath().getString("accessToken");
    }
    public static ExtractableResponse<Response> 즐겨찾기_생성(String accessToken,Long source,Long target){

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(new FavoritePathRequest(source,target))
                        .when().post("/favorites")
                        .then().log().all()
                        .extract();

        return response;
    }

    public static ExtractableResponse<Response>  즐겨찾기_목록_조회(String accessToken){
        return RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .when().get("/favorites")
                .then().log().all().extract();

    }

    public static ExtractableResponse<Response>  즐겨찾기_삭제(String accessToken,String favoritePathId){
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .when().delete( favoritePathId)
                .then().log().all().extract();

    }
}
