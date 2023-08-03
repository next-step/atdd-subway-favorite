package nextstep.favorite.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.CreateFavoriteRequest;
import nextstep.study.AuthSteps;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.favorite.ui.FavoriteSteps.requestDto;
import static nextstep.favorite.ui.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.member.acceptance.MemberSteps.getAccessToken;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public String accessToken;

    private Long 강남역;
    private Long 양재역;

    @BeforeEach
    void before() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        var 로그인_응답 = AuthSteps.로그인요청(EMAIL, PASSWORD);
        accessToken = getAccessToken(로그인_응답);
        역_노선_구간_생성();
    }

    /**
     * <pre>
     *     Feature: 즐겨찾기 생성
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *      And : 회원을 생성한다.
     *      And : 로그인을 한다.
     *     When : 토큰과 함께 즐겨찾기를 추가를 요청한다.
     *     Then : 즐겨찾기가 추가된다.
     * </pre>
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // given : 선행조건 기술
        CreateFavoriteRequest createFavoriteRequest = requestDto(강남역, 양재역);

        // when : 기능 수행
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청(accessToken, createFavoriteRequest);

        // then : 결과 확인
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    /**
     * <pre>
     *     Feature: 즐겨찾기 조회
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *      And : 회원을 생성한다.
     *      And : 로그인을 한다.
     *      And : 토큰과 함께 즐겨찾기를 추가를 요청한다.
     *     When : 토큰과 함게 즐겨찾기를 조회한다.
     *     Then : 즐겨찾기가 조회된다.
     * </pre>
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findFavorite() {
        // given : 선행조건 기술
        즐겨찾기_추가();

        // when : 기능 수행
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(accessToken);

        // then : 결과 확인
        즐겨찾기_조회_응답_강남역과_양재역이_응답된다(즐겨찾기_조회_응답);
    }

    /**
     * <pre>
     *     Feature: 즐겨찾기 삭제
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *      And : 회원을 생성한다.
     *      And : 로그인을 한다.
     *      And : 토큰과 함께 즐겨찾기를 추가를 요청한다.
     *     When : 토큰과 함게 즐겨찾기를 삭제한다.
     *     Then : 즐겨찾기가 삭제된다.
     * </pre>
     */
    @Test
    void deleteFavorite() {
        // given : 선행조건 기술

        // when : 기능 수행

        // then : 결과 확인
    }

    /**
     * <pre>
     *     Feature: 401 Unauthorized 에러 발생
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *     When : 즐겨찾기를 추가를 요청한다.
     *     Then : 401 Unauthorized 에러가 발생한다.
     * </pre>
     */
    @Test
    void unauthorized() {
        // given : 선행조건 기술

        // when : 기능 수행

        // then : 결과 확인
    }

    /**
     * <pre>
     *     Feature: 비정상 경로를 즐겨찾기로 등록하는 경우
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간에 대한 거리가 추가되어있다.
     *      And : 회원을 생성한다.
     *      And : 로그인을 한다.
     *     When : 토큰과 함께 구간에 없는 역을 즐겨찾기를 추가를 요청한다.
     *     Then : 예외를 응답한다.
     * </pre>
     */
    @DisplayName("비정상 경로를 즐겨찾기로 등록하는 경우")
    @Test
    void createFavoriteReturnException() {
        // given : 선행조건 기술

        // when : 기능 수행

        // then : 결과 확인
    }

    private void 역_노선_구간_생성() {
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
        지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createLineCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "신분당선");
        lineCreateParams.put("color", "bg-red-600");
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", 10 + "");
        return lineCreateParams;
    }

    private void 즐겨찾기_추가() {
        CreateFavoriteRequest createFavoriteRequest = requestDto(강남역, 양재역);
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청(accessToken, createFavoriteRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_조회_응답_강남역과_양재역이_응답된다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("id")).hasSize(1)
                .containsExactly(1);
        assertThat(response.jsonPath().getList("source")).hasSize(1)
                .extracting("name")
                .containsExactly("강남역");
        assertThat(response.jsonPath().getList("target")).hasSize(1)
                .extracting("name")
                .containsExactly("양재역");
    }
}
