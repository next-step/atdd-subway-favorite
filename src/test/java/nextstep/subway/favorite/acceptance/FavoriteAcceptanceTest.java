package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.라인_요청_PARAM;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 30;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        Map<String, String> 신분당선_요청_PARAM = 라인_요청_PARAM("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(신분당선_요청_PARAM).as(LineResponse.class);
    }


    @DisplayName("즐겨찾기 조회")
    @Test
    public void inquiryFavorite() {
        //given
        회원정보_등록();
        로그인();
        즐겨찾기_등록(강남역, 광교역);

        //when
        ExtractableResponse<Response> response = 즐겨찾기_조회();

        //then
        즐겨찾기_조회됨(response);
        즐겨찾기_조회_결과_1건(response);
    }


    @DisplayName("즐겨찾기 조회시 권한없을 때 401 오류")
    @Test
    public void inquiryFavoriteUnAuthorized() {
        //when
        ExtractableResponse<Response> response = 즐겨찾기_조회();

        //then
        권한없음_401_오류(response);
    }

    @DisplayName("즐겨찾기 등록")
    @Test
    public void createFavorite() {
        //given
        회원정보_등록();
        로그인();

        //when
        ExtractableResponse<Response> response = 즐겨찾기_등록(강남역, 광교역);

        //then
        즐겨찾기_등록됨(response);
    }

    @DisplayName("즐겨찾기 등록시 권한없을 때 401 오류")
    @Test
    public void createFavoriteUnAuthorized() {
        //when
        ExtractableResponse<Response> response = 즐겨찾기_등록(강남역, 광교역);

        //then
        권한없음_401_오류(response);
    }

    @DisplayName("같은 source/target으로 즐겨찾기 등록시 실패")
    @Test
    public void createFavoriteWithSameSourceTarget() {
        //when
        ExtractableResponse<Response> response = 즐겨찾기_등록(강남역, 강남역);

        //then
        즐겨찾기_등록_실패(response);
    }

    private void 회원정보_등록() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

    private void 로그인() {
        로그인_되어_있음(EMAIL, PASSWORD);
    }

    private ExtractableResponse<Response> 즐겨찾기_등록(StationResponse source, StationResponse target) {
        Map<String, Long> requestParam = new HashMap<>();
        requestParam.put("source", source.getId());
        requestParam.put("target", target.getId());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParam)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회() {
        return RestAssured
                .given().log().all()
                .when().get("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/favorites/{id}", id)
                .then().log().all().extract();
    }

    private void 즐겨찾기_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_등록_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_조회_결과_1건(ExtractableResponse<Response> response) {
        List<FavoriteResponse> favoriteResponses = response.as(new ArrayList<FavoriteResponse>().getClass());
        assertThat(favoriteResponses.size()).isEqualTo(1);
    }

    private void 권한없음_401_오류(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
