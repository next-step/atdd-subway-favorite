package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.acceptance.MemberSteps;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteSteps {

    private static String TOKEN;



    public static void setUpToken() {
        String EMAIL = "email@naver.com";
        String PASSWORD = "password";
        int AGE = 12;

        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        Map<String, Object> param = new HashMap<>();
        param.put("email", EMAIL);
        param.put("password", PASSWORD);
        param.put("age", AGE);
        TOKEN = "bearer " + MemberSteps.토큰_생성(param);
    }


    public static long 즐겨찾기_생성한다(long source, long target) {
        setUpToken();
        Map<String, Long> param = new HashMap<>();
        param.put("source", source);
        param.put("target", target);

        Response response = getAuthorizedSpec()
                .log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();
        String location = response.getHeader("Location");
        String[] split = location.split("/");
        String id = split[split.length - 1];
        return Long.parseLong(id);
    }

    public static FavoriteResponse 즐겨찾기_조회한다(long favoriteId) {
        setUpToken();
        return getAuthorizedSpec().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites/" + favoriteId)
                .then().extract()
                .as(FavoriteResponse.class);
    }

    public static List<FavoriteResponse> 모든_즐겨찾기_조회한다() {
        return getAuthorizedSpec().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().extract()
                .as(new TypeRef<>() {});
    }

    public static void 즐겨찾기_삭제한다(long id) {
        setUpToken();
        getAuthorizedSpec().log().all()
                .when().delete("/favorites/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static void 미인증된_유저가_즐겨찾기_생성할수없다() {
        Map<String, Long> param = new HashMap<>();
        param.put("source", 1L);
        param.put("target", 2L);

        getUnAuthorizedSpec()
                .log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    private static RequestSpecification getAuthorizedSpec() {
        var requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", TOKEN)
                .build();

        return RestAssured
                .given()
                .spec(requestSpecification);
    }




    private static RequestSpecification getUnAuthorizedSpec() {
        var requestSpecification = new RequestSpecBuilder()
                .build();

        return RestAssured
                .given()
                .header("Authorization", "bearer WRONG_TOKEN")
                .spec(requestSpecification);
    }
}
