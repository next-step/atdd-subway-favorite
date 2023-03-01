package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역;
    private Long 양재역;
    private Long 양재시민의숲역;
    private Long 청계산입구역;

    private String accessToken;

    /**
     * GIVEN 지하철 역을 두개 생성하고
     * GIVEN 회원가입 후 로그인하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").jsonPath().getLong("id");
        청계산입구역 = 지하철역_생성_요청("청계산입구역").jsonPath().getLong("id");

        회원_생성_요청("bactoria@gmail.com", "qwe123", 20);
        var 베어러_인증_로그인_응답 = 베어러_인증_로그인_요청("bactoria@gmail.com", "qwe123");
        accessToken = Access_Token을_가져온다(베어러_인증_로그인_응답);
    }

    /**
     * WHEN 로그인한 회원이 출발역과 도착역을 즐겨찾기 하는 경우 <br>
     * THEN 즐겨찾기가 추가된다 <br>
     */
    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        // when
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(강남역, 양재역);

        // then
        assertThat(즐겨찾기_추가_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * GIVEN 로그인한 회원이 즐겨찾기를 2번 하고
     * WHEN 로그인한 회원이 즐겨찾기 조회하는 경우 <br>
     * THEN 2개의 즐겨찾기 목록이 조회된다 <br>
     */
    @DisplayName("즐겨찾기 조회")
    @Test
    void readFavorite() {
        // given
        즐겨찾기_추가_요청(강남역, 양재역);
        즐겨찾기_추가_요청(양재시민의숲역, 청계산입구역);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/favorites")
                .then().log().all()
                .extract();

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.body().jsonPath().getList("source.id", Long.class)).containsExactly(강남역, 양재시민의숲역);
            assertThat(response.body().jsonPath().getList("target.id", Long.class)).containsExactly(양재역, 청계산입구역);
        });
    }

    /**
     * GIVEN 로그인한 회원이 즐겨찾기를 하고 <br>
     * WHEN 즐겨찾기를 삭제하는 경우 <br>
     * THEN 즐겨찾기가 삭제된다 <br>
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(강남역, 양재역);
        String location = 즐겨찾기_추가_응답.header("location");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(location)
                .then().log().all()
                .extract();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_추가_요청(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");

        // when
        return RestAssured.given().log().all()
                .body(params)
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }
}
