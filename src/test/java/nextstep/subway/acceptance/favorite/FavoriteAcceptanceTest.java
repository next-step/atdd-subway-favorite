package nextstep.subway.acceptance.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.handler.subway.StationHandler;
import nextstep.subway.application.dto.favorite.FavoriteRequest;
import nextstep.subway.application.dto.station.StationRequest;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static nextstep.auth.AuthSteps.로그인_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.favorite.FavoriteSteps.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    private int port;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private final String EMAIL = "email@email.com";
    private final String PASSWORD = "password";
    private final int AGE = 20;
    private String accessToken;

    /**
     * Given 3개의 역을 생성하고
     * Given 회원가입을 하고
     * Given 로그인을 하고
     */
    @BeforeEach
    void setUpFavoriteAcceptanceTest() {
        RestAssured.port = port;
        교대역 = 지하철_역_요청(new StationRequest("교대역"));
        강남역 = 지하철_역_요청(new StationRequest("강남역"));
        양재역 = 지하철_역_요청(new StationRequest("양재역"));
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 로그인_요청(EMAIL, PASSWORD);
    }

    /**
     * When 즐겨찾기를 생성하면
     * Then 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // when
        FavoriteRequest 즐겨찾기_요청 = new FavoriteRequest(교대역, 강남역);
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(즐겨찾기_요청);

        // then
        Assertions.assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 즐겨찾기를 조회하면
     * Then 즐겨찾기가 조회된다.
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findFavorite() {
        // given
        FavoriteRequest 즐겨찾기_요청 = new FavoriteRequest(교대역, 강남역);
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(즐겨찾기_요청);

        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(즐겨찾기_생성_응답.header("location"));

        // then
        Assertions.assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        FavoriteRequest 즐겨찾기_요청 = new FavoriteRequest(교대역, 강남역);
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(즐겨찾기_요청);

        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_삭제_요청(즐겨찾기_생성_응답.header("location"));

        // then
        Assertions.assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Long 지하철_역_요청(StationRequest request) {
        return StationHandler.지하철_역_요청(request)
                .jsonPath()
                .getLong("id");
    }
}