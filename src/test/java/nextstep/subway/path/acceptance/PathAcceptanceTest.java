package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineSteps.노선_생성_요청_후_id_반환;
import static nextstep.subway.path.acceptance.PathSteps.지하철_경로_조회_요청;
import static nextstep.subway.section.acceptance.SectionSteps.구간_생성_요청;
import static nextstep.subway.station.acceptance.StationSteps.역_생성_요청_후_id_반환;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.presentation.request.LineCreateRequest;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("경로 찾기 인수테스트")
public class PathAcceptanceTest extends AcceptanceTest {

    private Long 수서역;
    private Long 선릉역;
    private Long 역삼역;
    private Long 강남역;
    private Long 분당선;
    private Long 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        수서역 = 역_생성_요청_후_id_반환("수서역");
        선릉역 = 역_생성_요청_후_id_반환("선릉역");
        역삼역 = 역_생성_요청_후_id_반환("역삼역");
        강남역 = 역_생성_요청_후_id_반환("강남역");
        분당선 = 노선_생성_요청_후_id_반환(new LineCreateRequest("분당선", "yellow", 수서역, 선릉역, 10));
        이호선 = 노선_생성_요청_후_id_반환(new LineCreateRequest("이호선", "green", 선릉역, 역삼역, 7));

        구간_생성_요청(이호선, 역삼역, 강남역, 2);
    }

    /**
     *  Given : 출발역과 도착역을 다르게 설정하고
     *  When : 경로를 검색하면
     *  Then : 경로에 포함된 역들과 거리를 반환한다
     */
    @Test
    void 출발역과_도착역을_지정하면_경로의_역을_보여준다() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(수서역, 강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly(
                "수서역",
                "선릉역",
                "역삼역",
                "강남역"
        );
    }

    /**
     *  Given : 출발역과 도착역을 같게 설정하고
     *  When : 경로를 검색하면
     *  Then : 예외를 반환한다
     */
    @Test
    void 출발역과_도착역을_같게_지정하면_예외를_반환한다() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(수서역, 수서역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("출발역과 종착역이 같습니다.");
    }

    /**
     *  Given : 출발역과 도착역이 연결되어 있지 않게 설정하고
     *  When : 경로를 검색하면
     *  Then : 예외를 반환한다
     */
    @Test
    void 출발역과_도착역이_연결되지_않으면_예외를_반환한다() {
        // given
        Long 해운대역 = 역_생성_요청_후_id_반환("해운대역");

        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(수서역, 해운대역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("출발역과 종착역이 연결되어 있지 않습니다.");
    }

    /**
     *  When : 출발역이나 도착역이 존재하지 않으면
     *  Then : 예외를 반환한다
     */
    @Test
    void 출발역이나_도착역이_존재하지_않으면_예외를_반환한다() {
        // given
        Long 독도역 = 10L;

        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(수서역, 독도역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
