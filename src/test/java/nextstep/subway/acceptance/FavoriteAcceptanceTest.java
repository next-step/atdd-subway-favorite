package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.MemberErrorMessage;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayErrorMessage;
import nextstep.subway.utils.AccountFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 즐겨찾기 인수 테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    String userToken;
    String adminToken;
    String subscribeMemberToken;

    Station 강남역;
    Station 교대역;
    Station 서초역;
    Station 남부터미널역;

    Long 이호선_ID;
    Long 삼호선_ID;
    Long 분당선_ID;

    /* {이어진 노선}
     *  강남역 -- (이호선) ---  교대역
     *  |                     |
     * (삼호선)              (이호선)
     *  |                     |
     * 서초역 -- (삼호선) -- 남부터미널역
     *
     * {이어지지 않은 노선}
     * 수원역 -- (분당선) -- 영통역
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        userToken = MemberSteps.로그인_되어_있음(AccountFixture.USER_EMAIL, AccountFixture.USER_PASSWORD);
        adminToken = MemberSteps.로그인_되어_있음(AccountFixture.ADMIN_EMAIL, AccountFixture.ADMIN_PASSWORD);
        subscribeMemberToken = MemberSteps.로그인_되어_있음(AccountFixture.SUBSCRIPTION_EMAIL, AccountFixture.SUBSCRIPTION_PASSWORD);

        강남역 = StationSteps.지하철역_생성_요청("강남역").as(Station.class);
        교대역 = StationSteps.지하철역_생성_요청("교대역").as(Station.class);
        서초역 = StationSteps.지하철역_생성_요청("서초역").as(Station.class);
        남부터미널역 = StationSteps.지하철역_생성_요청("남부터미널역").as(Station.class);

        이호선_ID = LineSteps.지하철_노선_생성_요청("이호선", "green", adminToken).jsonPath().getLong("id");
        삼호선_ID = LineSteps.지하철_노선_생성_요청("삼호선", "orange", adminToken).jsonPath().getLong("id");
        분당선_ID = LineSteps.지하철_노선_생성_요청("분당선", "yellow", adminToken).jsonPath().getLong("id");

        LineSteps.지하철_노선에_지하철_구간_생성_요청(이호선_ID, createSectionCreateParams(강남역.getId(), 교대역.getId(), 10), adminToken);
        LineSteps.지하철_노선에_지하철_구간_생성_요청(이호선_ID, createSectionCreateParams(교대역.getId(), 남부터미널역.getId(), 5), adminToken);

        LineSteps.지하철_노선에_지하철_구간_생성_요청(삼호선_ID, createSectionCreateParams(강남역.getId(), 서초역.getId(), 3), adminToken);
        LineSteps.지하철_노선에_지하철_구간_생성_요청(삼호선_ID, createSectionCreateParams(서초역.getId(), 남부터미널역.getId(), 4), adminToken);

    }

    /*
     * when 정기 구독 멤버가 두개의 역을 즐겨찾기로 등록하면
     * then 201 상태코드와 LocationHeader 를 응답받는다.
     *
     * when LocationHeader 경로로 즐겨찾기를 조회하면
     * then 저장된 즐겨찾기 정보를 조회할 수 있다.
     */
    @Test
    void 즐겨찾기_등록_검증() {
        //when
        ExtractableResponse<Response> 즐겨찾기_등록_결과 = 즐겨찾기_등록(강남역.getId(), 남부터미널역.getId(), subscribeMemberToken);

        //then
        즐겨찾기_등록_검증(즐겨찾기_등록_결과);

        //when
        ExtractableResponse<Response> 즐겨찾기_조회_결과 = 즐겨찾기_단일_조회(즐겨찾기_등록_결과.header("Location"), subscribeMemberToken);

        //then
        즐겨찾기_조회_검증(즐겨찾기_조회_결과, 강남역.getName(), 남부터미널역.getName());
    }

    /*
     * when 정기 구독 멤버가 2개의 즐겨찾기를 등록한다
     * when 정기 구독 멤버가 즐겨찾기 목록을 조회한다.
     * then 응답 받은 리스트의 사이즈와 응답코드를 확인한다.
     */
    @Test
    void 즐겨찾기_목록_조회_검증() {
        //when
        즐겨찾기_등록(강남역.getId(), 남부터미널역.getId(), subscribeMemberToken);
        즐겨찾기_등록(서초역.getId(), 남부터미널역.getId(), subscribeMemberToken);

        //when
        ExtractableResponse<Response> 즐겨찾기_조회_결과 = 즐겨찾기_목록_조회(subscribeMemberToken);

        //then
        int 검증할_리스트_길이 = 2;

        즐겨찾기_목록_조회_검증(즐겨찾기_조회_결과, 검증할_리스트_길이);
    }

    /*
     * when 정기 구독 멤버가 즐겨찾기를 등록한다
     * then 즐겨찾기 등록 결과를 검증한다.
     * when 정기 구독 멤버가 즐겨찾기를 삭제한다.
     * and  삭제된 아이디의 즐겨찾기를 조회한다.
     * then 400 상태코드와 즐겨찾기가 존재하지 않는다는 메세지를 응답받는다.
     */
    @Test
    void 즐겨찾기_삭제_검증() {
        //when
        ExtractableResponse<Response> 즐겨찾기_등록_결과 = 즐겨찾기_등록(강남역.getId(), 남부터미널역.getId(), subscribeMemberToken);

        //then
        즐겨찾기_등록_검증(즐겨찾기_등록_결과);

        //when
        즐겨찾기_삭제(즐겨찾기_등록_결과.header("Location"), subscribeMemberToken);
        ExtractableResponse<Response> 즐겨찾기_조회_결과 = 즐겨찾기_단일_조회(즐겨찾기_등록_결과.header("Location"), subscribeMemberToken);

        //then
        즐겨찾기_삭제_검증(즐겨찾기_조회_결과);
    }


    /* given 정기 구독 멤버가 즐겨찾기를 등록하고
     * when  멤버 권한으로 즐겨찾기를 조회한다.
     * then  멤버 권한 유저는 즐겨찾기 리소스에 접근이 불가능 하므로 401 상태코드와 에러 메시지를 응답 받는다.
     */
    @Test
    void 권한이_없는_유저가_즐겨찾기_조회() {
        //when
        ExtractableResponse<Response> 즐겨찾기_등록_결과 = 즐겨찾기_등록(강남역.getId(), 남부터미널역.getId(), subscribeMemberToken);

        //then
        즐겨찾기_등록_검증(즐겨찾기_등록_결과);

        //when
        ExtractableResponse<Response> 즐겨찾기_조회_결과 = 즐겨찾기_단일_조회(즐겨찾기_등록_결과.header("Location"), userToken);

        //then
        권한이_없는_유저_즐겨찾기_응답_검증(즐겨찾기_조회_결과);
    }

    /* given 정기 구독 멤버가 즐겨찾기를 등록하고
     * when  멤버 권한으로 즐겨찾기를 삭제한다.
     * then  멤버 권한 유저는 즐겨찾기 리소스에 접근이 불가능 하므로 401 상태코드와 에러 메시지를 응답 받는다.
     */
    @Test
    void 권한이_없는_유저가_즐겨찾기_삭제() {
        //when
        ExtractableResponse<Response> 즐겨찾기_등록_결과 = 즐겨찾기_등록(강남역.getId(), 남부터미널역.getId(), subscribeMemberToken);

        //then
        즐겨찾기_등록_검증(즐겨찾기_등록_결과);

        //when
        ExtractableResponse<Response> 즐겨찾기_조회_결과 = 즐겨찾기_삭제(즐겨찾기_등록_결과.header("Location"), userToken);

        //then
        권한이_없는_유저_즐겨찾기_응답_검증(즐겨찾기_조회_결과);
    }


    /* when 멤버 권한 유저가 즐겨찾기를 등록한다.
     * then  멤버 권한 유저는 즐겨찾기 리소스에 접근이 불가능 하므로 401 상태코드와 에러 메시지를 응답 받는다.
     */
    @Test
    void 권한이_없는_유저가_즐겨찾기_등록() {
        //when
        ExtractableResponse<Response> 즐겨찾기_등록_결과 = 즐겨찾기_등록(강남역.getId(), 남부터미널역.getId(), userToken);

        //then
        권한이_없는_유저_즐겨찾기_응답_검증(즐겨찾기_등록_결과);
    }

    /*
     * when 정기 구독 멤버가 등록되지 않은 역을 즐겨찾기로 등록한다
     * then 등록되지 않은 역은 등록할 수 없기 때문에 400 상태코드와 역이 존재하지 않는다는 메세지를 응답받는다.
     */
    @Test
    void 등록되지_않은_지하철역을_즐겨찾기로_등록() {
        //when
        Long 등록되지_않은_지하철역_ID = 200L;
        ExtractableResponse<Response> 즐겨찾기_등록_결과 = 즐겨찾기_등록(등록되지_않은_지하철역_ID, 남부터미널역.getId(), subscribeMemberToken);

        //then
        등록되지_않은_역을_즐겨찾기로_등록_검증(즐겨찾기_등록_결과);
    }

    /*
     * when 정기 구독 멤버가 등록되지 않은 즐겨찾기를 삭제한다.
     * then 등록되지 않은 즐겨찾기는 삭제할 수 없다.
     * and  400 상태코드와 즐겨찾기를 찾을 수 없다는 메세지를 응답 받는다.
     */
    @Test
    void 등록되지_않은_즐겨찾기_삭제() {
        String 등록되지_않은_즐겨찾기_삭제_URI = "/favorites/99";
        ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 즐겨찾기_삭제(등록되지_않은_즐겨찾기_삭제_URI, subscribeMemberToken);

        등록되지_않은_즐겨찾기_삭제_검증(즐겨찾기_삭제_결과);
    }

    private void 등록되지_않은_즐겨찾기_삭제_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(MemberErrorMessage.NOT_FOUND_FAVORITE.getMessage());
    }

    private void 등록되지_않은_역을_즐겨찾기로_등록_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).contains(SubwayErrorMessage.NOT_FOUND_STATION.getMessage());
    }

    private void 즐겨찾기_목록_조회_검증(ExtractableResponse<Response> response, int size) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("id")).hasSize(size);
    }


    private void 즐겨찾기_조회_검증(ExtractableResponse<Response> response, String targetName, String sourceName) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("target.name")).isEqualTo(targetName);
        assertThat(response.jsonPath().getString("source.name")).isEqualTo(sourceName);
    }

    private void 즐겨찾기_등록_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }

    private void 즐겨찾기_삭제_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).hasToString(MemberErrorMessage.NOT_FOUND_FAVORITE.getMessage());
    }

    private void 권한이_없는_유저_즐겨찾기_응답_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.jsonPath().getString("message")).hasToString(MemberErrorMessage.UNAUTHORIZED.getMessage());
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, Integer distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }

}
