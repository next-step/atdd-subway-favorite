package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteDto;
import nextstep.favorite.application.response.ShowAllFavoriteResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.common.Constant.*;
import static nextstep.favorite.acceptance.FavoriteAcceptanceStep.*;
import static nextstep.member.acceptance.AuthAcceptanceStep.로그인_성공;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.line.LineAcceptanceStep.지하철_노선_생성됨;
import static nextstep.subway.acceptance.station.StationAcceptanceStep.지하철_역_생성됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMPTY = "";

    private Long 강남역_ID;
    private Long 양재역_ID;
    private Long 강남구청역_ID;
    private Long 압구정로데오역_ID;
    private Long 신분당선_ID;
    private Long 수인분당선_ID;

    @BeforeEach
    protected void beforeEach() {
        회원_생성_요청(홍길동_이메일, 홍길동_비밀번호, 홍길동_나이);
        회원_생성_요청(임꺽정_이메일, 임꺽정_비밀번호, 임꺽정_나이);

        강남역_ID = 지하철_역_생성됨(강남역);
        양재역_ID = 지하철_역_생성됨(양재역);
        신분당선_ID = 지하철_노선_생성됨(신분당선, 빨간색, 강남역_ID, 양재역_ID, 역_간격_10);

        압구정로데오역_ID = 지하철_역_생성됨(압구정로데오역);
        강남구청역_ID = 지하철_역_생성됨(강남구청역);
        수인분당선_ID = 지하철_노선_생성됨(수인분당선, 노란색, 압구정로데오역_ID, 강남구청역_ID, 역_간격_10);
    }

    /**
     * 강남역    --- *신분당선* (10) ---   양재역
     *
     * 압구정로데오 --- *수인분당선*(10) --- 강남구청역
     */

    /**
     * Given 로그인을 하고
     * When 구간을 즐겨찾기에 추가하면
     * Then 추가한 구간이 조회된다.
     */
    @DisplayName("로그인하고 구간을 즐겨찾기에 추가한다.")
    @Test
    void 로그인하고_구간을_즐겨찾기에_추가() {
        // given
        String 토큰 = 로그인_성공(홍길동_이메일, 홍길동_비밀번호);

        // when
        즐겨찾기_추가됨(강남역_ID, 양재역_ID, 토큰);

        // then
        ShowAllFavoriteResponse 즐겨찾기_조회_응답 = 즐겨찾기_조회됨(토큰);
        List<FavoriteDto> 즐겨찾기 = 즐겨찾기_조회_응답.getFavorites();
        즐겨찾기_추가_검증(즐겨찾기, 1, 강남역_ID, 양재역_ID);
    }

    /**
     * Given 로그인을 하지않고
     * When 구간을 즐겨찾기에 추가하면
     * Then 즐겨찾기에 추가되지 않는다.
     */
    @DisplayName("로그인을 하지 않고 구간을 즐겨찾기를 추가하면 추가되지 않는다.")
    @Test
    void 로그인을_하지않고_구간을_즐겨찾기에_추가() {
        // when
        즐겨찾기_추가됨(강남역_ID, 양재역_ID, EMPTY);

        // then
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회(EMPTY);
        즐겨찾기_추가_예외발생_검증(즐겨찾기_조회_응답, HttpStatus.UNAUTHORIZED);
    }

    /**
     * When 경로가 없는 구간을 즐겨찾기에 추가하면
     * Then 즐겨찾기에 추가되지 않는다.
     */
    @DisplayName("경로가 없는 구간을 즐겨찾기로 추가하면 추가되지 않는다.")
    @Test
    void 경로가_없는_구간을_즐겨찾기에_추가() {
        // given
        String 토큰 = 로그인_성공(홍길동_이메일, 홍길동_비밀번호);

        // when & then
        ExtractableResponse<Response> 즐겨찾기_추가_응답 = 즐겨찾기_추가됨(강남구청역_ID, 양재역_ID, 토큰);
        즐겨찾기_추가_예외발생_검증(즐겨찾기_추가_응답, HttpStatus.BAD_REQUEST);
    }

    /**
     * given 즐겨찾기를 추가하고
     * When 즐겨찾기를 조회하면
     * Then 자신의 즐겨찾기만 조회된다.
     */
    @DisplayName("즐겨찾기를 조회하면 자신의 즐겨찾기만 조회된다.")
    @Test
    void 즐겨찾기를_조회() {
        // given
        String 홍길동_토큰 = 로그인_성공(홍길동_이메일, 홍길동_비밀번호);
        즐겨찾기_추가됨(강남역_ID, 양재역_ID, 홍길동_토큰);
        String 임꺽정_토큰 = 로그인_성공(임꺽정_이메일, 임꺽정_비밀번호);
        즐겨찾기_추가됨(강남구청역_ID, 압구정로데오역_ID, 임꺽정_토큰);

        // when & then
        ShowAllFavoriteResponse 홍길동_즐겨찾기_조회_응답 = 즐겨찾기_조회됨(홍길동_토큰);
        List<FavoriteDto> 홍길동_즐겨찾기 = 홍길동_즐겨찾기_조회_응답.getFavorites();

        // then
        즐겨찾기_추가_검증(홍길동_즐겨찾기, 1, 강남역_ID, 양재역_ID);
        즐겨찾기_추가_안됨_검증(홍길동_즐겨찾기, 강남구청역_ID, 압구정로데오역_ID);
    }

    void 즐겨찾기_추가_검증(List<FavoriteDto> 즐겨찾기, int 즐겨찾기_수, Long 시작역, Long 종료역) {
        assertThat(즐겨찾기).hasSize(즐겨찾기_수);
        assertTrue(즐겨찾기.stream()
                .anyMatch(favoriteDto ->
                        favoriteDto.getStartStation().getStationId().equals(시작역)
                                && favoriteDto.getEndStation().getStationId().equals(종료역)
                ));
    }

    void 즐겨찾기_추가_안됨_검증(List<FavoriteDto> 즐겨찾기, Long 시작역, Long 종료역) {
        assertTrue(즐겨찾기.stream()
                .noneMatch(favoriteDto ->
                        favoriteDto.getStartStation().getStationId().equals(시작역)
                                && favoriteDto.getEndStation().getStationId().equals(종료역)
                ));
    }

    void 즐겨찾기_추가_예외발생_검증(ExtractableResponse<Response> extractableResponse, HttpStatus status) {
        assertThat(extractableResponse.statusCode()).isEqualTo(status.value());
    }

}
