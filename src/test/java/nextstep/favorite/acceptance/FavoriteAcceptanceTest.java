package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        RestAssured.given().log().all()
                .contentType("application/json")
                .body(new HashMap<String, String>(){{
                    put("source", "1");
                    put("target", "2");
                }})
                .when().post("/favorites")
                .then().log().all()
                .statusCode(201);
    }

}
