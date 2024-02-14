package nextstep.favorite.acceptance;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import nextstep.exception.ExceptionResponse;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.subway.controller.dto.LineCreateRequest;
import nextstep.subway.controller.dto.LineResponse;
import nextstep.subway.controller.dto.StationCreateRequest;
import nextstep.subway.controller.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_요청을_구성한다;
import static nextstep.subway.fixture.LineFixture.SHINBUNDANG_LINE;
import static nextstep.subway.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 강남역_ID;
    private Long 선릉역_ID;
    private Long 양재역_ID;
    private Long 역삼역_ID;
    private Long 신대방역_ID;
    private Long 신림역_ID;
    private Long 봉천역_ID;

    private Long 신분당선_ID;
    private Long 분당선_ID;
    private Long 일호선_ID;
    private Long 이호선_ID;
    private Long 삼호선_ID;

    /**
     * GIVEN 지하철 역을 생성하고
     * GIVEN 노선을 생성한다
     *
     * 역삼역    --- *1호선*(10) ---   양재역
     * |                        |
     * *2호선*(10)                   *분당선*(10)
     * |                        |
     * 강남역    --- *신분당호선*(10) ---    선릉역
     * <p>
     * 강남역    --- *3호선*(10) ---    선릉역
     */
    @BeforeEach
    void setFixture() {
        강남역_ID = 지하철역_생성_요청(GANGNAM_STATION.toCreateRequest());
        선릉역_ID = 지하철역_생성_요청(SEOLLEUNG_STATION.toCreateRequest());
        양재역_ID = 지하철역_생성_요청(YANGJAE_STATION.toCreateRequest());
        역삼역_ID = 지하철역_생성_요청(YEOKSAM_STATION.toCreateRequest());
        신대방역_ID = 지하철역_생성_요청(YEOKSAM_STATION.toCreateRequest());
        신림역_ID = 지하철역_생성_요청(YEOKSAM_STATION.toCreateRequest());
        봉천역_ID = 지하철역_생성_요청(YEOKSAM_STATION.toCreateRequest());

        신분당선_ID = 노선_생성_요청(SHINBUNDANG_LINE.toCreateRequest(강남역_ID, 선릉역_ID));
        분당선_ID = 노선_생성_요청(SHINBUNDANG_LINE.toCreateRequest(선릉역_ID, 양재역_ID));
        일호선_ID = 노선_생성_요청(SHINBUNDANG_LINE.toCreateRequest(양재역_ID, 역삼역_ID));
        이호선_ID = 노선_생성_요청(SHINBUNDANG_LINE.toCreateRequest(역삼역_ID, 강남역_ID));
        삼호선_ID = 노선_생성_요청(SHINBUNDANG_LINE.toCreateRequest(신대방역_ID, 신림역_ID));
    }

    private Long 지하철역_생성_요청(StationCreateRequest stationCreateRequest) {
        return 지하철역_생성_요청(stationCreateRequest, CREATED.value())
                .as(StationResponse.class).getId();
    }

    private Long 노선_생성_요청(LineCreateRequest lineCreateRequest) {
        return 노선_생성_요청(lineCreateRequest, CREATED.value())
                .as(LineResponse.class).getId();
    }

    /**
     * WHEN 즐겨찾기 생성시 로그인을 하지 않았을 경우
     * Then 즐겨찾기를 생성할 수 없다
     */
    @Test
    void 실패_즐겨찾기_생성시_로그인_하지_않았을_경우_즐겨찾기를_생성할_수_없다() {
        String message = 즐겨찾기_요청을_구성한다()
                .Response_HTTP_상태_코드(UNAUTHORIZED.value())
                .즐겨찾기_생성_정보를_설정한다(1L, 2L)
                .즐겨찾기_생성_요청을_보낸다()
                .as(ExceptionResponse.class).getMessage();

        assertThat(message).isEqualTo("인증에 실패했습니다.");
    }

    /**
     * GIVEN 로그인을 한 다음
     * WHEN 즐겨찾기 생성시 경로가 존재하지 않을 경우
     * Then 즐겨찾기를 생성할 수 없다
     */
    @Test
    void 실패_즐겨찾기_생성시_경로가_존재하지_않을_경우_즐겨찾기를_생성할_수_없다() {
        String message = 즐겨찾기_생성_요청(1L, 2L)
                .as(ExceptionResponse.class).getMessage();

        assertThat(message).isEqualTo("경로가 존재하지 않습니다.");
    }

    /**
     * GIVEN 로그인을 한 다음
     * WHEN 즐겨찾기 생성시 경로가 연결되어 있지 않을 경우
     * Then 즐겨찾기를 생성할 수 없다
     */
    @Test
    void 실패_즐겨찾기_생성시_경로가_연결되어_있지_않을_경우_즐겨찾기를_생성할_수_없다() {
        String message = 즐겨찾기_생성_요청(1L, 2L)
                .as(ExceptionResponse.class).getMessage();

        assertThat(message).isEqualTo("경로가 연결되어 있지 않습니다.");
    }

    /**
     * GIVEN 로그인을 한 다음
     * WHEN 즐겨찾기 생성시 이미 즐겨찾기한 경로일 경우
     * Then 즐겨찾기를 생성할 수 없다
     */
    @Test
    void 실패_즐겨찾기_생성시_이미_즐겨찾기한_경로일_경우_즐겨찾기를_생성할_수_없다() {
        String message = 즐겨찾기_생성_요청(1L, 2L)
                .as(ExceptionResponse.class).getMessage();

        assertThat(message).isEqualTo("등록되어 있는 경로입니다.");
    }

    /**
     * GIVEN 로그인을 한 다음
     * WHEN 즐겨찾기 생성시
     * Then 즐겨찾기를 생성할 수 있다
     */
    @Test
    void 성공_즐겨찾기를_생성할_수_있다() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(1L, 2L);

        List<FavoriteResponse> findResponse = 즐겨찾기_조회_요청().as(new TypeRef<>() {
        });

        String locationHeader = createResponse.header("Location");
        assertAll(
                () -> assertThat(locationHeader).isEqualTo("/favorites/1"),
                () -> assertThat(findResponse).hasSize(1)
                        .extracting("id", "source.id", "source.name", "target.id", "target.name")
                        .containsExactly(
                                tuple(1L, 1L, "교대역", 3L, "양재역")
                        )
        );
    }

    /**
     * WHEN 즐겨찾기 조회시 로그인을 하지 않았을 경우
     * Then 즐겨찾기를 조회할 수 없다
     */
    @Test
    void 실패_즐겨찾기_조회시_로그인_하지_않았을_경우_즐겨찾기를_조회할_수_없다() {
        String message = 즐겨찾기_요청을_구성한다()
                .즐겨찾기_조회_요청을_보낸다()
                .as(ExceptionResponse.class).getMessage();

        assertThat(message).isEqualTo("인증에 실패했습니다.");
    }

    /**
     * GIVEN 로그인을 한 다음
     * WHEN 즐겨찾기 조회시 즐겨찾기한 경로가 없을경우
     * Then 즐겨찾기를 조회할 수 없다
     */
    @Test
    void 실패_즐겨찾기_조회시_즐겨찾기한_경로가_없을_경우_즐겨찾기를_조회할_수_없다() {
        String message = 즐겨찾기_조회_요청()
                .as(ExceptionResponse.class).getMessage();

        assertThat(message).isEqualTo("즐겨찾기한 경로가 없습니다.");
    }

    /**
     * WHEN 즐겨찾기 삭제시 로그인을 하지 않았을 경우
     * Then 즐겨찾기를 삭제할 수 없다
     */
    @Test
    void 실패_즐겨찾기_삭제시_로그인_하지_않았을_경우_즐겨찾기를_삭제할_수_없다() {
        String message = 즐겨찾기_요청을_구성한다()
                .즐겨찾기_삭제_요청을_보낸다("/favorites/1")
                .as(ExceptionResponse.class).getMessage();

        assertThat(message).isEqualTo("인증에 실패했습니다.");
    }

    /**
     * GIVEN 로그인을 한 다음
     * WHEN 즐겨찾기 삭제시 즐겨찾기가 존재하지 않을 경우
     * Then 즐겨찾기를 삭제할 수 없다
     */
    @Test
    void 실패_즐겨찾기_삭제시_즐겨찾기가_존재하지_않을_경우_즐겨찾기를_삭제할_수_없다() {
        String message = 즐겨찾기_요청을_구성한다()
                .로그인을_한다(accessToken)
                .즐겨찾기_삭제_요청을_보낸다("/favorites/1")
                .as(ExceptionResponse.class).getMessage();

        assertThat(message).isEqualTo("즐겨찾기가 존재하지 않습니다.");
    }

    /**
     * GIVEN 로그인을 한 다음
     * GIVEN 즐겨찾기를 생성한 다음
     * WHEN 즐겨찾기 삭제시
     * Then 즐겨찾기를 삭제할 수 있다
     */
    @Test
    void 성공_즐겨찾기_삭제에_성공한다() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(1L, 2L);
        String uri = createResponse.header("Location");
        즐겨찾기_삭제_요청(uri);

        String message = 즐겨찾기_조회_요청()
                .as(ExceptionResponse.class).getMessage();

        assertThat(message).isEqualTo("즐겨찾기한 경로가 없습니다.");
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(long source, long target) {
        return 즐겨찾기_요청을_구성한다()
                .로그인을_한다(accessToken)
                .Response_HTTP_상태_코드(CREATED.value())
                .즐겨찾기_생성_정보를_설정한다(source, target)
                .즐겨찾기_생성_요청을_보낸다();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청() {
        return 즐겨찾기_요청을_구성한다()
                .로그인을_한다(accessToken)
                .즐겨찾기_조회_요청을_보낸다();
    }

    private void 즐겨찾기_삭제_요청(String uri) {
        즐겨찾기_요청을_구성한다()
                .Response_HTTP_상태_코드(NO_CONTENT.value())
                .로그인을_한다(accessToken)
                .즐겨찾기_삭제_요청을_보낸다(uri);
    }

}
