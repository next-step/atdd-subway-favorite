package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSteps.*;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;
    private TokenResponse tokenResponse;

    @BeforeEach
    @DisplayName("Background")
    void setup() {
        super.setUp();

        // Given 지하철역 등록되어 있음
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        // And 지하철 노선 등록되어 있음
        이호선 = 지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 2);

        // And 지하철 노선에 지하철역 등록되어 있음
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3);

        // And 회원 등록되어 있음
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // And 로그인 되어있음
        tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void 즐겨찾기_관리_테스트() {

        // When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(
            tokenResponse,
            new FavoriteRequest(강남역.getId(), 남부터미널역.getId())
        );

        // Then 즐겨찾기 생성됨
        즐겨찾기_생성됨(createResponse);

        // When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> viewResponse = 즐겨찾기_목록_조회_요청(tokenResponse);

        // Then 즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회됨(viewResponse);

        // When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(
            tokenResponse,
            createResponse
        );

        // Then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(deleteResponse);

    }

    @DisplayName("인증 정보가 없을 경우 401 반환")
    @Test
    void 즐겨찾기_인증_실패_테스트() {

        // When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(
            tokenResponse,
            new FavoriteRequest(강남역.getId(), 남부터미널역.getId())
        );

        // when: 회원 정보 조회 요청
        ExtractableResponse<Response> createResponse = 비회원_즐겨찾기_생성_요청(
            new FavoriteRequest(강남역.getId(), 남부터미널역.getId())
        );

        // then
        인증_실패(createResponse);

        // when: 회원 정보 수정 요청
        ExtractableResponse<Response> viewResponse = 비회원_즐겨찾기_목록_조회_요청();

        // then
        인증_실패(viewResponse);

        // when: 회원 정보 삭제 요
        ExtractableResponse<Response> deleteResponse = 비회원_즐겨찾기_삭제_요청(response);

        // then
        인증_실패(deleteResponse);

    }

    public void 즐겨찾기_생성됨 (ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.CREATED.value());
    }

    public void 즐겨찾기_목록_조회됨 (ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.OK.value());
    }

    public void 즐겨찾기_삭제됨 (ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
