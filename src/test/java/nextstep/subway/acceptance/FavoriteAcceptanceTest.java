package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static nextstep.subway.acceptance.FavoriteStep.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathAcceptanceTest.createSectionCreateParams;
import static nextstep.subway.acceptance.PathAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능 인수테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(adminAccessToken, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(adminAccessToken, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(adminAccessToken, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(adminAccessToken, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청(adminAccessToken, "2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청(adminAccessToken, "신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청(adminAccessToken, "3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(adminAccessToken, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
    }

    /**
     * given 역이 생성되어 있다
     * and 노선이 생성되어 있다
     * and 구간이 생성되어 있다
     * and 사용자가 로그인되어 있다
     * when 즐겨찾기를 추가한다.
     * then 201이 응답된다.
     */
    @DisplayName("즐겨찾기 생성 요청 테스트")
    @Test
    void createFavoriteTest() {
        //given
        Map<String, String> favoriteParam = Map.of("source", 교대역 + "", "target", 강남역 + "");

        //when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(memberAccessToken, favoriteParam);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * given 역이 생성되어 있다
     * and 노선이 생성되어 있다
     * and 구간이 생성되어 있다
     * and 사용자가 로그인되어 있다
     * when 즐겨찾기를 추가한다.
     * then 즐겨찾기 조회시 추가한 즐겨찾기가 조회된다.
     */
    @DisplayName("즐겨찾기 생성 인수테스트")
    @Test
    void createFavoriteAndGetFavoriteTest() {
        //given
        Map<String, String> favoriteParam = Map.of("source", 교대역 + "", "target", 강남역 + "");
        즐겨찾기_생성_요청(memberAccessToken, favoriteParam);

        //when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(memberAccessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(교대역);
        assertThat(response.jsonPath().getList("target.id", Long.class)).containsExactly(강남역);
    }
    
    /**
     * given 역이 생성되어 있다
     * and 노선이 생성되어 있다
     * and 구간이 생성되어 있다
     * and 사용자가 로그인되어 있다
     * and 즐겨찾기가 추가되어 있다.
     * when 즐겨찾기를 삭제한다.
     * then no content가 응답된다.
     */
    @DisplayName("즐겨찾기 삭제 테스트")
    @Test
    void deleteFavoriteTest() {
        //given
        Map<String, String> favoriteParam = Map.of("source", 교대역 + "", "target", 강남역 + "");
        long 즐겨찾기 = 즐겨찾기_생성_요청(memberAccessToken, favoriteParam).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(memberAccessToken, 즐겨찾기);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}
