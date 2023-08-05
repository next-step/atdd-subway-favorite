package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.dto.request.FavoriteRequest;
import nextstep.favorite.dto.response.FavoriteResponse;
import nextstep.subway.line.dto.request.SaveLineRequest;
import nextstep.subway.line.dto.request.SaveLineSectionRequest;
import nextstep.subway.station.dto.response.StationResponse;
import nextstep.support.AcceptanceTest;
import nextstep.support.AssertUtils;
import nextstep.support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static nextstep.auth.acceptance.step.AuthStep.일반_로그인_요청;
import static nextstep.favorite.acceptance.step.FavoriteStep.*;
import static nextstep.member.acceptance.step.MemberStep.회원_생성_요청;
import static nextstep.member.fixture.MemberFixture.회원_정보_DTO;
import static nextstep.subway.acceptance.step.LineAcceptanceStep.지하철_노선_생성을_요청한다;
import static nextstep.subway.acceptance.step.LineSectionAcceptanceStep.지하철_구간_생성을_요청한다;
import static nextstep.subway.acceptance.step.StationAcceptanceStep.지하철역_생성을_요청한다;
import static nextstep.subway.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AcceptanceTest
public class FavoriteAcceptanceTest {

    @LocalServerPort
    private int port;

    private static final String STATION_ID_KEY = "id";

    private static final String LINE_ID_KEY = "id";

    private static final String SOURCE_KEY = "source";

    private static final String TARGET_KEY = "target";

    private Long 교대역_아이디,
                 강남역_아이디,
                 양재역_아이디,
                 남부터미널역_아이디;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    /**
     * <pre>
     * 교대역    --- *2호선(10)* ---   강남역
     * |                              |
     * *3호선(2)*                      *신분당선(10)*
     * |                              |
     * 남부터미널역  --- *3호선(3)* --- 양재역
     * </pre>
     */
    @BeforeEach
    void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleanup.execute();

        this.교대역_아이디 = 지하철역_생성을_요청한다(교대역).jsonPath().getLong(STATION_ID_KEY);
        this.강남역_아이디 = 지하철역_생성을_요청한다(강남역).jsonPath().getLong(STATION_ID_KEY);
        this.양재역_아이디 = 지하철역_생성을_요청한다(양재역).jsonPath().getLong(STATION_ID_KEY);
        this.남부터미널역_아이디 = 지하철역_생성을_요청한다(남부터미널역).jsonPath().getLong(STATION_ID_KEY);

        SaveLineRequest 생성할_이호선 = SaveLineRequest.builder()
                .name("2호선")
                .color("#52c41a")
                .upStationId(교대역_아이디)
                .downStationId(강남역_아이디)
                .distance(10)
                .build();
        SaveLineRequest 생성할_신분당선 = SaveLineRequest.builder()
                .name("신분당선")
                .color("#f5222d")
                .upStationId(강남역_아이디)
                .downStationId(양재역_아이디)
                .distance(10)
                .build();
        SaveLineRequest 생성할_삼호선 = SaveLineRequest.builder()
                .name("3호선")
                .color("#fa8c16")
                .upStationId(교대역_아이디)
                .downStationId(남부터미널역_아이디)
                .distance(2)
                .build();
        지하철_노선_생성을_요청한다(생성할_이호선)
                .jsonPath()
                .getLong(LINE_ID_KEY);
        지하철_노선_생성을_요청한다(생성할_신분당선)
                .jsonPath()
                .getLong(LINE_ID_KEY);
        Long 삼호선_아이디 = 지하철_노선_생성을_요청한다(생성할_삼호선)
                .jsonPath()
                .getLong(LINE_ID_KEY);

        SaveLineSectionRequest 삼호선에_생성할_구간 = SaveLineSectionRequest.builder()
                .upStationId(남부터미널역_아이디)
                .downStationId(양재역_아이디)
                .distance(3)
                .build();
        지하철_구간_생성을_요청한다(삼호선에_생성할_구간, 삼호선_아이디);
    }

    /**
     * <pre>
     * Given 로그인을 하고
     * When 경로를 즐겨찾기에 등록하면
     * Then 즐겨찾기 목록 조회 시 등록한 경로를 찾을 수 있다
     * </pre>
     */
    @DisplayName("경로를 즐겨찾기에 등록한다.")
    @Test
    void createFavoritePath() {
        // given
        회원_생성_요청(회원_정보_DTO);
        var 로그인_응답 = 일반_로그인_요청(회원_정보_DTO.getEmail(), 회원_정보_DTO.getPassword());

        // when
        FavoriteRequest 즐겨찾기_추가_요청_DTO = FavoriteRequest.builder()
                .source(강남역_아이디)
                .target(남부터미널역_아이디)
                .build();
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(즐겨찾기_추가_요청_DTO, 로그인_응답);

        // then
        var 즐겨찾기_목록_응답_DTO = 즐겨찾기_목록_조회_요청(로그인_응답);
        List<Long> 출발역_아이디_목록 = 출발역_아이디_목록을_가져온다(즐겨찾기_목록_응답_DTO);
        List<Long> 도착역_아이디_목록 = 도착역_아이디_목록을_가져온다(즐겨찾기_목록_응답_DTO);

        assertAll(
                () -> AssertUtils.assertThatStatusCode(즐겨찾기_추가_응답, HttpStatus.CREATED),
                () -> assertThat(출발역_아이디_목록).contains(강남역_아이디),
                () -> assertThat(도착역_아이디_목록).contains(남부터미널역_아이디)
        );
    }

    /**
     * <pre>
     * When 로그인을 하지 않고 경로를 즐겨찾기에 등록하려하면
     * Then 즐겨찾기 등록에 실패한다.
     * </pre>
     */
    @DisplayName("비로그인 상태로 경로를 즐겨찾기에 등록하면 실패한다.")
    @Test
    void createFavoritePathWhenNonLoggedIn() {
        // when
        FavoriteRequest 즐겨찾기_추가_요청_DTO = FavoriteRequest.builder()
                .source(교대역_아이디)
                .target(양재역_아이디)
                .build();
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(즐겨찾기_추가_요청_DTO, null);

        // then
        AssertUtils.assertThatNonLoggedIn(즐겨찾기_추가_응답);
    }

    /**
     * <pre>
     * Given 로그인을 하고
     * Given 2개의 경로를 즐겨찾기에 등록하고
     * When 즐겨찾기 목록을 조회하면
     * Then 즐겨 찾기 목록 조회 시 2개의 경로를 조회할 수 있다.
     * </pre>
     */
    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void readFavorites() {
        // given
        회원_생성_요청(회원_정보_DTO);
        var 로그인_응답 = 일반_로그인_요청(회원_정보_DTO.getEmail(), 회원_정보_DTO.getPassword());

        FavoriteRequest 강남역_남부터미널역_추가_요청_DTO = FavoriteRequest.builder()
                .source(강남역_아이디)
                .target(남부터미널역_아이디)
                .build();
        FavoriteRequest 교대역_양재역_추가_요청_DTO = FavoriteRequest.builder()
                .source(교대역_아이디)
                .target(양재역_아이디)
                .build();
        Stream.of(강남역_남부터미널역_추가_요청_DTO, 교대역_양재역_추가_요청_DTO)
                .forEach(dto -> 즐겨찾기_추가_요청(dto, 로그인_응답));

        // when
        var 즐겨찾기_목록_응답_DTO = 즐겨찾기_목록_조회_요청(로그인_응답);
        List<Long> 출발역_아이디_목록 = 출발역_아이디_목록을_가져온다(즐겨찾기_목록_응답_DTO);
        List<Long> 도착역_아이디_목록 = 도착역_아이디_목록을_가져온다(즐겨찾기_목록_응답_DTO);

        // then
        assertAll(
                () -> assertThat(출발역_아이디_목록).containsExactly(강남역_아이디, 교대역_아이디),
                () -> assertThat(도착역_아이디_목록).containsExactly(남부터미널역_아이디, 양재역_아이디)
        );
    }

    /**
     * <pre>
     * Given 2개의 경로를 즐겨찾기에 등록하고
     * When 로그인을 하지 않고 즐겨찾기 목록을 조회하면
     * Then 즐겨 찾기 목록을 조회할 수 없다.
     * </pre>
     */
    @DisplayName("비로그인 상태로 즐겨찾기 목록을 조회할 수 없다.")
    @Test
    void readFavoritesWhenNonLoggedIn() {
        // given
        회원_생성_요청(회원_정보_DTO);
        var 로그인_응답 = 일반_로그인_요청(회원_정보_DTO.getEmail(), 회원_정보_DTO.getPassword());

        FavoriteRequest 강남역_남부터미널역_추가_요청_DTO = FavoriteRequest.builder()
                .source(강남역_아이디)
                .target(남부터미널역_아이디)
                .build();
        FavoriteRequest 교대역_양재역_추가_요청_DTO = FavoriteRequest.builder()
                .source(교대역_아이디)
                .target(양재역_아이디)
                .build();
        Stream.of(강남역_남부터미널역_추가_요청_DTO, 교대역_양재역_추가_요청_DTO)
                .forEach(dto -> 즐겨찾기_추가_요청(dto, 로그인_응답));

        // when즐겨찾기_목록_조회_요청
        var 즐겨찾기_목록_응답_DTO = 즐겨찾기_목록_조회_요청(null);

        // then
        AssertUtils.assertThatNonLoggedIn(즐겨찾기_목록_응답_DTO);
    }

    /**
     * <pre>
     * Given 로그인을 하고
     * Given 경로를 즐겨찾기에 등록하고
     * When 즐겨찾기에 등록한 경로를 즐겨찾기 목록에서 삭제하면
     * Then 해당 경로는 즐겨찾기 목록에서 찾을 수 없다.
     * </pre>
     */
    @DisplayName("즐겨찾기 항목을 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        회원_생성_요청(회원_정보_DTO);
        var 로그인_응답 = 일반_로그인_요청(회원_정보_DTO.getEmail(), 회원_정보_DTO.getPassword());

        FavoriteRequest 즐겨찾기_추가_요청_DTO = FavoriteRequest.builder()
                .source(강남역_아이디)
                .target(남부터미널역_아이디)
                .build();
        Long 추가된_즐겨찾기_아이디 = 즐겨찾기_추가_요청(즐겨찾기_추가_요청_DTO, 로그인_응답)
                 .as(FavoriteResponse.class)
                 .getId();

        // when
        var 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(추가된_즐겨찾기_아이디, 로그인_응답);

        // then
        var 즐겨찾기_목록_응답_DTO = 즐겨찾기_목록_조회_요청(로그인_응답);
        List<Long> 출발역_아이디_목록 = 출발역_아이디_목록을_가져온다(즐겨찾기_목록_응답_DTO);
        List<Long> 도착역_아이디_목록 = 도착역_아이디_목록을_가져온다(즐겨찾기_목록_응답_DTO);

        assertAll(
                () -> AssertUtils.assertThatStatusCode(즐겨찾기_삭제_응답, HttpStatus.NO_CONTENT),
                () -> assertThat(출발역_아이디_목록).doesNotContain(강남역_아이디),
                () -> assertThat(도착역_아이디_목록).doesNotContain(남부터미널역_아이디)
        );
    }

    /**
     * <pre>
     * Given 경로를 즐겨찾기에 등록하고
     * When 로그인을 하지 않고 즐겨찾기에 등록한 경로를 즐겨찾기 목록에서 삭제하면
     * Then 즐겨찾기 항목 삭제에 실패한다.
     * </pre>
     */
    @DisplayName("비로그인 상태로 즐겨찾기 항목을 삭제할 수 없다.")
    @Test
    void deleteFavoriteWhenNonLoggedIn() {
        // given
        회원_생성_요청(회원_정보_DTO);
        var 로그인_응답 = 일반_로그인_요청(회원_정보_DTO.getEmail(), 회원_정보_DTO.getPassword());

        FavoriteRequest 즐겨찾기_추가_요청_DTO = FavoriteRequest.builder()
                .source(교대역_아이디)
                .target(양재역_아이디)
                .build();
        Long 추가된_즐겨찾기_아이디 = 즐겨찾기_추가_요청(즐겨찾기_추가_요청_DTO, 로그인_응답)
                .as(FavoriteResponse.class)
                .getId();

        // when
        var 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(추가된_즐겨찾기_아이디, null);

        // then
        AssertUtils.assertThatNonLoggedIn(즐겨찾기_삭제_응답);
    }

    /**
     * <pre>
     * 즐겨찾기 목록 조회 시
     * 출발역 아이디로 등록된 역들의 목록을 추출하는 역할을 한다.
     * </pre>
     * @param response RestAssured를 통해 즐겨찾기 목록 조회 API를 호출해서 응답 받은 응답 객체
     * @return 출발역 아이디 목록
     */
    private List<Long> 출발역_아이디_목록을_가져온다(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList(SOURCE_KEY, StationResponse.class)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }

    /**
     * <pre>
     * 즐겨찾기 목록 조회 시
     * 도착역 아이디로 등록된 역들의 목록을 추출하는 역할을 한다.
     * </pre>
     * @param response RestAssured를 통해 즐겨찾기 목록 조회 API를 호출해서 응답 받은 응답 객체
     * @return 도착역 아이디 목록
     */
    private List<Long> 도착역_아이디_목록을_가져온다(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList(TARGET_KEY, StationResponse.class)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
