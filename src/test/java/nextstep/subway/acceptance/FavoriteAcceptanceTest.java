package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Station;
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

    Station 강남역;
    Station 교대역;
    Station 서초역;
    Station 남부터미널역;

    Station 수원역;
    Station 영통역;

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

        강남역 = StationSteps.지하철역_생성_요청("강남역").as(Station.class);
        교대역 = StationSteps.지하철역_생성_요청("교대역").as(Station.class);
        서초역 = StationSteps.지하철역_생성_요청("서초역").as(Station.class);
        남부터미널역 = StationSteps.지하철역_생성_요청("남부터미널역").as(Station.class);

        수원역 = StationSteps.지하철역_생성_요청("수원역").as(Station.class);
        영통역 = StationSteps.지하철역_생성_요청("영통역").as(Station.class);

        이호선_ID = LineSteps.지하철_노선_생성_요청("이호선", "green", adminToken).jsonPath().getLong("id");
        삼호선_ID = LineSteps.지하철_노선_생성_요청("삼호선", "orange", adminToken).jsonPath().getLong("id");
        분당선_ID = LineSteps.지하철_노선_생성_요청("분당선", "yellow", adminToken).jsonPath().getLong("id");

        LineSteps.지하철_노선에_지하철_구간_생성_요청(이호선_ID, createSectionCreateParams(강남역.getId(), 교대역.getId(), 10), userToken);
        LineSteps.지하철_노선에_지하철_구간_생성_요청(이호선_ID, createSectionCreateParams(교대역.getId(), 남부터미널역.getId(), 5), userToken);

        LineSteps.지하철_노선에_지하철_구간_생성_요청(삼호선_ID, createSectionCreateParams(강남역.getId(), 서초역.getId(), 3), userToken);
        LineSteps.지하철_노선에_지하철_구간_생성_요청(삼호선_ID, createSectionCreateParams(서초역.getId(), 남부터미널역.getId(), 4), userToken);

        LineSteps.지하철_노선에_지하철_구간_생성_요청(분당선_ID, createSectionCreateParams(수원역.getId(), 영통역.getId(), 10), userToken);
    }

    /*
     * when 두개의 역을 즐겨찾기로 등록하면
     * then 201 상태코드와 LocationHeader 를 응답받는다.
     */
    @Test
    void 즐겨찾기_등록_검증() {
        //when 두개의 역을 즐겨찾기로 등록하면
        ExtractableResponse<Response> 즐겨찾기_등록_결과 = 즐겨찾기_등록(강남역.getId(), 남부터미널역.getId(), userToken);

        //then 201 상태코드와 LocationHeader 를 응답받는다.
        assertThat(즐겨찾기_등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(즐겨찾기_등록_결과.header("Location")).isNotNull();
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, Integer distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }

}
