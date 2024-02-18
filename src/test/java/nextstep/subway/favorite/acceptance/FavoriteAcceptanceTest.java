package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.application.dto.FavoriteRequest;
import nextstep.subway.favorite.application.dto.FavoriteResponse;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.testhelper.AcceptanceTest;
import nextstep.subway.testhelper.JsonPathHelper;
import nextstep.subway.testhelper.apicaller.FavoriteApiCaller;
import nextstep.subway.testhelper.fixture.LineFixture;
import nextstep.subway.testhelper.fixture.MemberFixture;
import nextstep.subway.testhelper.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationFixture stationFixture;
    private Long 잠실역_ID;
    private Long 강남역_ID;
    private Long 서초역_ID;
    private Long 오이도역_ID;
    private String accessToken;
    private String anotherMemberAccessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();
        stationFixture = new StationFixture();
        잠실역_ID = stationFixture.get잠실역_ID();
        강남역_ID = stationFixture.get강남역_ID();
        서초역_ID = stationFixture.get서초역_ID();
        오이도역_ID = stationFixture.get오이도역_ID();

        LineFixture lineFixture = new LineFixture(stationFixture);
        lineFixture.라인_목록_생성(stationFixture);

        MemberFixture memberFixture = new MemberFixture();
        accessToken = memberFixture.getAccessToken();
        anotherMemberAccessToken = memberFixture.getAnotherMemberAccessToken();
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 후
     * WHEN 로그인을 하지 않고 출발역과 도착역을 입력하면
     * THEN 에러 처리와 함께 '로그인이 필요합니다.' 라는 메세지가 출력된다
     */
    @DisplayName("로그인을 하지 않고 즐겨찾기 생성하면 '로그인이 필요합니다.' 라는 메세지가 출력된다")
    @Test
    void createFavorite1() {
        // given
        // when
        FavoriteRequest request = new FavoriteRequest(잠실역_ID, 강남역_ID);
        ExtractableResponse<Response> response = given().log().all()
                .body(FavoriteApiCaller.createParams(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();

        // then
        int actual = response.statusCode();
        int expected = HttpStatus.UNAUTHORIZED.value();
        assertThat(actual).isEqualTo(expected);

        String actualBody = response.asString();
        String expectedBody = "로그인이 필요합니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 후
     * THEN 출발역과 도착역을 입력하면 즐겨찾기가 등록된다
     * THEN 즐겨찾기 목록 조회 시 생성한 즐겨찾기를 찾을 수 있다
     */
    @DisplayName("지하철 노선들을 생성하고 구간을 추가 후 출발역과 도착역을 입력하면 즐겨찾기가 등록된다")
    @Test
    void createFavorite2() {
        // given
        // then
        FavoriteRequest request = new FavoriteRequest(잠실역_ID, 강남역_ID);
        FavoriteApiCaller.즐겨찾기_생성(request, accessToken);

        // then
        List<FavoriteResponse> responses = JsonPathHelper.getAll(FavoriteApiCaller.즐겨찾기_조회(accessToken), "", FavoriteResponse.class);

        StationResponse actualSource = responses.get(0).getSource();
        StationResponse expectedSource = new StationResponse(잠실역_ID, StationFixture.잠실역.getName());
        assertThat(actualSource).isEqualTo(expectedSource);

        StationResponse actualTarget = responses.get(0).getTarget();
        StationResponse expectedTarget = new StationResponse(강남역_ID, StationFixture.강남역.getName());
        assertThat(actualTarget).isEqualTo(expectedTarget);
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 후
     * WHEN 출발역과 도착역을 같게 입력하면
     * THEN 에러 처리와 함께 '출발역과 도착역은 같을 수 없습니다.' 라는 메세지가 출력된다
     */
    @DisplayName("출발역과 도착역을 같게 입력하면 '출발역과 도착역은 같을 수 없습니다.' 라는 메세지가 출력된다")
    @Test
    void createFavorite3() {
        // given
        // then
        FavoriteRequest request = new FavoriteRequest(잠실역_ID, 잠실역_ID);
        ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .body(FavoriteApiCaller.createParams(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();

        // then
        int actual = response.statusCode();
        int expected = HttpStatus.BAD_REQUEST.value();
        assertThat(actual).isEqualTo(expected);

        String actualBody = response.asString();
        String expectedBody = "출발역과 도착역은 같을 수 없습니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 후
     * WHEN 출발역과 도착역이 연결이 되지 않게 입력하면
     * THEN 에러 처리와 함께 '출발역과 도착역은 연결되어 있어야 합니다.' 라는 메세지가 출력된다
     */
    @DisplayName("출발역과 도착역이 연결이 되지 않게 입력하면 '출발역과 도착역은 연결되어 있어야 합니다.' 라는 메세지가 출력된다")
    @Test
    void createFavorite4() {
        // given
        // then
        FavoriteRequest request = new FavoriteRequest(강남역_ID, 서초역_ID);
        ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .body(FavoriteApiCaller.createParams(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();

        // then
        int actual = response.statusCode();
        int expected = HttpStatus.BAD_REQUEST.value();
        assertThat(actual).isEqualTo(expected);

        String actualBody = response.asString();
        String expectedBody = "출발역과 도착역은 연결되어 있어야 합니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 후
     * WHEN 존재 하지 않는 역을 입력하면
     * THEN 에러 처리와 함께 '입력한 역을 찾을 수 없습니다.' 라는 메세지가 출력된다
     */
    @DisplayName("존재 하지 않는 역을 입력하면 '입력한 역을 찾을 수 없습니다.' 라는 메세지가 출력된다")
    @Test
    void createFavorite5() {
        // given
        // then
        FavoriteRequest request = new FavoriteRequest(강남역_ID, 오이도역_ID);
        ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .body(FavoriteApiCaller.createParams(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();

        // then
        int actual = response.statusCode();
        int expected = HttpStatus.BAD_REQUEST.value();
        assertThat(actual).isEqualTo(expected);

        String actualBody = response.asString();
        String expectedBody = "입력한 역을 찾을 수 없습니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 그리고 즐겨찾기를 등록 후
     * WHEN 로그인을 하지 않고 즐겨 찾기 조회를 하면</br>
     * THEN 에러 처리와 함께 '로그인이 필요합니다.' 라는 메세지가 출력된다
     */
    @DisplayName("로그인을 하지 않고 즐겨 찾기 조회를 하면 '로그인이 필요합니다.' 라는 메세지가 출력된다")
    @Test
    void getFavorites1() {
        // given
        FavoriteRequest request = new FavoriteRequest(잠실역_ID, 강남역_ID);
        FavoriteApiCaller.즐겨찾기_생성(request, accessToken);

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();

        // then
        int actual = response.statusCode();
        int expected = HttpStatus.UNAUTHORIZED.value();
        assertThat(actual).isEqualTo(expected);

        String actualBody = response.asString();
        String expectedBody = "로그인이 필요합니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 그리고 즐겨찾기를 등록 후
     * WHEN 즐겨 찾기 조회를 하면
     * THEN 등록한 즐겨찾기 목록을 찾을 수 있다
     */
    @DisplayName("즐겨찾기를 등록 후 즐겨 찾기 조회를 하면 등록한 즐겨찾기 목록을 찾을 수 있다")
    @Test
    void getFavorites2() {
        // given
        FavoriteRequest request = new FavoriteRequest(잠실역_ID, 강남역_ID);
        FavoriteApiCaller.즐겨찾기_생성(request, accessToken);

        // when
        List<FavoriteResponse> responses = JsonPathHelper.getAll(FavoriteApiCaller.즐겨찾기_조회(accessToken), "", FavoriteResponse.class);

        // then
        StationResponse actualSource = responses.get(0).getSource();
        StationResponse expectedSource = new StationResponse(잠실역_ID, StationFixture.잠실역.getName());
        assertThat(actualSource).isEqualTo(expectedSource);

        StationResponse actualTarget = responses.get(0).getTarget();
        StationResponse expectedTarget = new StationResponse(강남역_ID, StationFixture.강남역.getName());
        assertThat(actualTarget).isEqualTo(expectedTarget);
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 그리고 즐겨찾기를 등록 후
     * WHEN 로그인을 하지 않고 즐겨 찾기 삭제를 하면
     * THEN 에러 처리와 함께 '로그인이 필요합니다.' 라는 메세지가 출력된다
     */
    @DisplayName("로그인을 하지 않고 즐겨 찾기 삭제를 하면 '로그인이 필요합니다.' 라는 메세지가 출력된다")
    @Test
    void deleteFavorite1() {
        // given
        FavoriteRequest request = new FavoriteRequest(잠실역_ID, 강남역_ID);
        String location = FavoriteApiCaller.즐겨찾기_생성(request, accessToken).header("location");

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .extract();

        // then
        int actual = response.statusCode();
        int expected = HttpStatus.UNAUTHORIZED.value();
        assertThat(actual).isEqualTo(expected);

        String actualBody = response.asString();
        String expectedBody = "로그인이 필요합니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 그리고 즐겨찾기를 등록 후
     * WHEN 즐겨 찾기 삭제를 하면
     * THEN 삭제된 즐겨찾기 목록이 조회된다
     */
    @DisplayName("즐겨 찾기 삭제를 하면 삭제된 즐겨찾기 목록이 조회된다")
    @Test
    void deleteFavorite2() {
        // given
        FavoriteRequest request = new FavoriteRequest(잠실역_ID, 강남역_ID);
        String location = FavoriteApiCaller.즐겨찾기_생성(request, accessToken).header("location");

        // when
        FavoriteApiCaller.즐겨찾기_삭제(location, accessToken);


        // then
        List<FavoriteResponse> actual = JsonPathHelper.getAll(FavoriteApiCaller.즐겨찾기_조회(accessToken), "",
                FavoriteResponse.class);
        List<FavoriteResponse> expected = Collections.emptyList();
        assertThat(actual).isEqualTo(expected);
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 그리고 즐겨찾기를 등록 후
     * WHEN 즐겨 찾기 삭제를 하면
     * THEN 삭제된 즐겨찾기 목록이 조회된다
     */
    @DisplayName("등록 되지 않은 즐겨 찾기 삭제를 하면 '즐겨찾기를 찾을 수 없습니다. 라는 메세지가 출력된다")
    @Test
    void deleteFavorite3() {
        // given
        FavoriteRequest request = new FavoriteRequest(잠실역_ID, 강남역_ID);
        FavoriteApiCaller.즐겨찾기_생성(request, accessToken).header("location");
        String anotherMemberLocation = FavoriteApiCaller.즐겨찾기_생성(request, anotherMemberAccessToken).header(
                "location");

        // when
        ExtractableResponse<Response> response = given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(anotherMemberLocation)
                .then().log().all()
                .extract();


        // then
        int actual = response.statusCode();
        int expected = HttpStatus.BAD_REQUEST.value();
        assertThat(actual).isEqualTo(expected);

        String actualBody = response.asString();
        String expectedBody = "즐겨찾기를 찾을 수 없습니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

}
