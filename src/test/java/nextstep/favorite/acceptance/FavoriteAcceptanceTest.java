package nextstep.favorite.acceptance;

import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.member.acceptance.AuthSteps.로그인_토큰밝급_요청_후_토큰_반환;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.line.acceptance.LineSteps.노선_생성_요청;
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

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 선릉역;
    private Long 제주도역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 역_생성_요청_후_id_반환("강남역");
        선릉역 = 역_생성_요청_후_id_반환("선릉역");
        제주도역 = 역_생성_요청_후_id_반환("제주도역");
        회원_생성_요청("abc@gmail.com", "1234", 20);
        accessToken = 로그인_토큰밝급_요청_후_토큰_반환("abc@gmail.com", "1234");
        노선_생성_요청(new LineCreateRequest("2호선", "blue", 강남역, 선릉역, 10));
    }

    /**
     *  Given : 2개 이상의 지하철 역이 존재하고 로그인한 회원이
     *  When : 서로 다른 지하철 역으로 즐겨찾기를 추가한면
     *  Then : 즐겨찾기 생성이 완료된다.
     */
    @Test
    void 서로_다른_지하철_역으로_즐겨찾기를_생성한다() {
        // when
        ExtractableResponse<Response> extractableResponse = 즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(extractableResponse.header("Location")).startsWith("/favorites/");
    }

    /**
     *  Given : 1개 이상의 지하철 역이 존재하고 로그인한 회원이
     *  When : 같은 지하철 역으로 즐겨찾기를 추가하면
     *  Then : 에러를 반환한다.
     */
    @Test
    void 같은_지하철_역으로_즐겨찾기를_생성하면_에러를_반환한다() {
        // when
        ExtractableResponse<Response> extractableResponse = 즐겨찾기_생성_요청(강남역, 강남역, accessToken);

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(extractableResponse.header("Location")).startsWith("/favorites/");
    }

    /**
     *  Given : 2개 이상의 지하철 역이 존재하고 로그인한 회원이
     *  When : 서로 다른 지하철 역을 해당 회원의 유효한 토큰이 아닌 토큰으로 즐겨찾기를 추가하면
     *  Then : 에러를 반환한다.
     */
    @Test
    void 유효하지_않은_토큰으로_즐겨찾기를_생성하면_에러를_반환한다() {
        // when
        ExtractableResponse<Response> extractableResponse = 즐겨찾기_생성_요청(강남역, 선릉역, "wrongToken");

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     *  Given : 로그인한 회원의 즐겨찾기가 1개 이상 등록되어 있을 때
     *  When : 즐기찾기 목록을 조회하면
     *  Then : 등록된 즐겨찾기를 가져온다
     */
    @Test
    void 즐겨찾기_목록을_가져온다() {
        // given
        즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // when
        ExtractableResponse<Response> extractableResponse = 즐겨찾기_조회_요청(accessToken);

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     *  Given : 로그인한 회원의 즐겨찾기가 등록되어 있지 않을 때
     *  When : 즐기찾기 목록을 조회하면
     *  Then : 즐겨찾기 목록이 비어있다.
     */
    @Test
    void 즐겨찾기가_등록되어있지_않다면_빈_즐겨찾기_목록을_가져온다() {
        // when
        ExtractableResponse<Response> extractableResponse = 즐겨찾기_조회_요청(accessToken);

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extractableResponse.body().asString()).isEmpty();

    }

    /**
     *  Given : 특정 회원의 즐겨찾기가 등록되어 있을 때
     *  When : 해당 회원의 유효한 토큰이 아닌 토큰으로 즐겨찾기 목록을 조회하면
     *  Then : 에러를 반환한다
     */
    @Test
    void 유효하지_않은_토큰으로_즐겨찾기_목록을_가져오면_에러를_반환한다() {
        // given
        즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // when
        ExtractableResponse<Response> extractableResponse = 즐겨찾기_조회_요청("wrongToken");

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     *  Given : 특정 회원의 즐겨찾기가 등록되어 있고
     *  When : 해당 회원의 특정 즐겨찾기를 삭제하면
     *  Then : 즐겨찾기에서 제거된다
     */
    @Test
    void 즐겨찾기를_삭제한다() {
        // given
        즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // when
        ExtractableResponse<Response> extractableResponse = 즐겨찾기_삭제_요청(1L, accessToken);

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     *  Given : 특정 회원이 존재할 때
     *  When : 해당 회원의 존재하지 않는 즐겨찾기를 삭제하면
     *  Then : 에러를 반환한다
     */
    @Test
    void 존재하지않는_즐겨찾기를_삭제하면_에러를_반환한다() {
        // given
        즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // when
        ExtractableResponse<Response> extractableResponse = 즐겨찾기_삭제_요청(1000L, accessToken);

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     *  Given : 특정 회원의 즐겨찾기가 등록되어 있을 때
     *  When : 해당 회원의 유효한 토큰이 아닌 토큰으로 즐겨찾기를 삭제하면
     *  Then : 에러를 반환한다
     */
    @Test
    void 유효하지_않은_토큰으로_즐겨찾기를_삭제하면_에러를_반환한다() {
        // given
        즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // when
        ExtractableResponse<Response> extractableResponse = 즐겨찾기_삭제_요청(1L, "wrongToken");

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}