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

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.LineSteps.노선_생성_파라미터;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private String accessToken;
    private String email = "email@email.com";
    private String password = "password";
    private int age = 30;

    /**
     *  Background ( BeforeEach : setUp )
     *
     *  Given 지하철역 등록되어 있음
     *      And 지하철 노선 등록되어 있음
     *      And 지하철 노선에 지하철역 등록되어 있음
     *      And 회원 등록되어 있음
     *      And 로그인 되어있음
     */
    @BeforeEach
    void setUp() {
        super.setUp();
        //given
        String 교대역 = 지하철역_생성_요청("교대역").jsonPath().getString("id");
        String 양재역 = 지하철역_생성_요청("양재역").jsonPath().getString("id");

        //and
        Map<String, String> 노선_생성_파라미터 = 노선_생성_파라미터("_2호선", "green", 교대역, 양재역);
        지하철_노선_생성_요청(노선_생성_파라미터);


        회원_생성_요청(email, password, age);
        accessToken = 로그인_되어_있음(email, password);
    }


    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        //given
        Map<String, String> params = 즐겨찾기_파라미터_생성("1", "3");

        //when
        ExtractableResponse<Response> 즐겨찾기_생성 = 즐겨찾기_생성_요청(params, accessToken);

        //then
        즐겨찾기_생성_검증(즐겨찾기_생성);

        //when
        ExtractableResponse<Response> 조회_즐겨찾기 = 즐겨찾기_조회_요청(accessToken);

        //then
        즐겨찾기_조회_검증하기(조회_즐겨찾기);

        //given
        String location = 즐겨찾기_생성.header("Location");

        //when
        ExtractableResponse<Response> 즐겨찾기_삭제 = 즐겨찾기_삭제_요청(accessToken, location);

        //then
        즐겨찾기_삭제_검증(즐겨찾기_삭제);
    }

    @DisplayName("즐겨찾기 생성 검증")
    @Test
    void createFavorite() {
        //given
        Map<String, String> params = 즐겨찾기_파라미터_생성("1", "3");

        //when
        ExtractableResponse<Response> 즐겨찾기_생성 = 즐겨찾기_생성_요청(params, accessToken);
    }
}
