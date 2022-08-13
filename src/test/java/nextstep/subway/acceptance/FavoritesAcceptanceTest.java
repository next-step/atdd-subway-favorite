package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.관리자로_지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoritesAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "member@email.com";
    private static final String PASSWORD = "password2";
    private static final String OTHER_EMAIL = "other@email.com";
    private static final String OTHER_PASSWORD = "password123";


    private Long 오호선;

    private Long 마곡역;
    private Long 발산역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        마곡역 = 관리자로_지하철역_생성_요청(관리자, "마곡역").jsonPath().getLong("id");
        발산역 = 관리자로_지하철역_생성_요청(관리자, "발산역").jsonPath().getLong("id");
        오호선 = 지하철_노선_생성_요청("5호선", "purple", 마곡역, 발산역, 2);

    }


    // 정상 시나리오

    /**
     * Given : 사용자 토큰이 주어진 후
     * When : 출발역과 도착역을 입력하면
     * Then : 해당 정보 기반으로 즐겨찾기 데이터가 생성된다
     */
    @DisplayName("즐겨찾기 등록")
    @Test
    public void registFavorite() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        //When
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, 마곡역, 발산역);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    /**
     * Given : 사용자 토큰을 통해 출발역과 도착역을 입력하여 즐겨찾기를 등록한후
     * When : 사용자 토큰을 통해 사용자의 즐겨찾기를 조회하면
     * Then : 해당 사용자가 등록한 토큰이 모두 나타난다.
     */

    @DisplayName("즐겨찾기 조회")
    @Test
    public void getFavorites() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        즐겨찾기_등록_요청(accessToken, 마곡역, 발산역);

        //When
        ExtractableResponse<Response> response = 즐겨찾기_모두_조회_요청(accessToken);

        //Then
        assertThat(response.jsonPath().getList("source.name")).contains("마곡역");
        assertThat(response.jsonPath().getList("target.name")).contains("발산역");
    }


    /**
     * Given : 사용자 토큰을 통해 출발역과 도착역을 입력하여 즐겨찾기를 등록한후
     * When : 사용자 토근을 통해 해당 즐겨찾기를 삭제하면
     * Then : 즐겨찾기가 삭제된다
     */

    @DisplayName("즐겨찾기 삭제")
    @Test
    public void deleteFavorite() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        즐겨찾기_등록_요청(accessToken, 마곡역, 발산역);
        ExtractableResponse<Response> favorite = 즐겨찾기_조회_요청(accessToken, 마곡역, 발산역);
        Long id = favorite.jsonPath().getLong("id");

        //When
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, id);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    // 비정상 시나리오 ( 예외처리 )

    /**
     * Given : 로그인이 되어 있지 않은 상태에서
     * When : 출발역과 도착역을 입력하면
     * Then : 즐겨찾기가 등록이 되지 않는다.
     */
    @DisplayName("로그인 없이 즐겨찾기 등록")
    @Test
    public void canNotRegistFavoriteWithoutToken() {
        //When
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(마곡역, 발산역);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    /**
     * Given : 로그인이 되어 있지 않은 상태에서
     * When : 즐겨찾기를 조회하면
     * Then : 즐겨찾기를 조회할 수 없다.
     */
    @DisplayName("로그인 없이 즐겨찾기 조회")
    @Test
    public void canNotGetFavoriteWithoutToken() {
        //When
        ExtractableResponse<Response> response = 즐겨찾기_모두_조회_요청();

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    /**
     *  Given : 로그인이 되어 있지 않은 상태에서
     *  When : 즐겨찾기를 삭제하면
     *  Then : 즐겨찾기를 삭제할 수 없다
     */
    @DisplayName("로그인 없이 즐겨찾기 삭제")
    @Test
    public void canNotDeleteFavoriteWithoutToken() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        즐겨찾기_등록_요청(accessToken, 마곡역, 발산역);
        ExtractableResponse<Response> favorite = 즐겨찾기_조회_요청(accessToken, 마곡역, 발산역);
        Long id = favorite.jsonPath().getLong("id");

        //When
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(id);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }


    /**
     *  Given : 잘못된 토근정보가 주어진 후
     *  When : 출발역과 도착역을 입력하면
     *  Then : 잘못된 토큰 정보 이므로 즐겨찾기가 등록이 되지 않는다.
     */
    @DisplayName("잘못된 토큰으로 즐겨찾기 등록")
    @Test
    public void canNotRegistFavoriteWithInvalidToken() {
        //Given
        String accessToken = "잘못된 토큰";

        //When
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, 마곡역, 발산역);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     *  Given : 잘못된 토근정보가 주어진 후
     *  When : 즐겨찾기를 조회하면
     *  Then : 잘못된 토큰 정보 이므로 즐겨찾기를 조회할 수 없다.
     */
    @DisplayName("잘못된 토큰으로 즐겨찾기 조회")
    @Test
    public void canNotGetFavoriteWithInvalidToken() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        즐겨찾기_등록_요청(accessToken, 마곡역, 발산역);
        accessToken = "잘못된 토큰";

        //When
        ExtractableResponse<Response> response = 즐겨찾기_모두_조회_요청(accessToken);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    /**
     * Given : 잘못된 토근정보가 주어진 후
     * When : 즐겨찾기를 삭제하면
     * Then : 잘못된 토큰 정보 이므로 즐겨찾기를 삭제할 수 없다
     */
    @DisplayName("잘못된 토큰으로 즐겨찾기 삭제")
    @Test
    public void canNotDeleteFavoriteWithInvalidToken() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        즐겨찾기_등록_요청(accessToken, 마곡역, 발산역);
        ExtractableResponse<Response> favorite = 즐겨찾기_조회_요청(accessToken, 마곡역, 발산역);
        Long id = favorite.jsonPath().getLong("id");
        accessToken = "잘못된 토큰";

        //When
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, id);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    /**
     * Given : 잘못된 토근정보가 주어진 후
     * When : 즐겨찾기를 삭제하면
     * Then : 잘못된 토큰 정보 이므로 즐겨찾기를 삭제할 수 없다
     */
    @DisplayName("잘못된 토큰으로 즐겨찾기 삭제")
    @Test
    public void canNotDeleteFavoriteWithOtherToken() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        즐겨찾기_등록_요청(accessToken, 마곡역, 발산역);
        ExtractableResponse<Response> favorite = 즐겨찾기_조회_요청(accessToken, 마곡역, 발산역);
        Long id = favorite.jsonPath().getLong("id");
        String OtherAccessToken = 로그인_되어_있음(OTHER_EMAIL, OTHER_PASSWORD);

        //When
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(OtherAccessToken, id);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }


    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");

        return LineSteps.관리자로_지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

}
