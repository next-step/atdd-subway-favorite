package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.application.dto.FavoriteRequest;
import nextstep.subway.testhelper.AcceptanceTest;
import nextstep.subway.testhelper.apicaller.FavoriteApiCaller;
import nextstep.subway.testhelper.fixture.LineFixture;
import nextstep.subway.testhelper.fixture.MemberFixture;
import nextstep.subway.testhelper.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    private StationFixture stationFixture;
    private Long 잠실역_ID;
    private Long 강남역_ID;
    private Long 삼성역_ID;
    private Long 선릉역_ID;
    private Long 교대역_ID;
    private Long 서초역_ID;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();
        stationFixture = new StationFixture();
        잠실역_ID = stationFixture.get잠실역_ID();
        강남역_ID = stationFixture.get강남역_ID();
        삼성역_ID = stationFixture.get삼성역_ID();
        선릉역_ID = stationFixture.get선릉역_ID();
        교대역_ID = stationFixture.get교대역_ID();
        서초역_ID = stationFixture.get서초역_ID();

        LineFixture lineFixture = new LineFixture(stationFixture);
        lineFixture.라인_목록_생성(stationFixture);

        MemberFixture memberFixture = new MemberFixture();
        accessToken = memberFixture.getAccessToken();
    }

    /**
     * GIVEN 지하철 노선들을 생성하고 구간을 추가 후
     * WHEN 로그인을 하지 않고 출발역과 도착역을 입력하면
     * THEN 에러 처리와 함께 '즐겨찾기 등록을 위해서 로그인이 필요합니다.' 라는 메세지가 출력된다
     */
    @DisplayName("로그인을 하지 않고 즐겨찾기 생성하면 즐겨찾기 등록을 위해서 로그인이 필요합니다.' 라는 메세지가 출력된다")
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
        String expectedBody = "즐겨찾기 등록을 위해서 로그인이 필요합니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

}
