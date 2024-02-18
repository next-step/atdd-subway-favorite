package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.testhelper.*;
import nextstep.subway.testhelper.apicaller.LineApiCaller;
import nextstep.subway.testhelper.apicaller.MemberApiCaller;
import nextstep.subway.testhelper.fixture.LineFixture;
import nextstep.subway.testhelper.fixture.SectionFixture;
import nextstep.subway.testhelper.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

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
        LineApiCaller.지하철_노선_생성(lineFixture.get일호선_잠실역_부터_강남역_params());
        LineApiCaller.지하철_노선_생성(lineFixture.get이호선_강남역_부터_삼성역_params());
        Long 삼호선_강남역_부터_선릉역_ID = JsonPathHelper.getObject(LineApiCaller.지하철_노선_생성(lineFixture.get삼호선_잠실역_부터_선릉역_params()), "id", Long.class);
        LineApiCaller.지하철_노선_생성(lineFixture.get사호선_교대역_부터_서초역_params());

        SectionFixture sectionFixture = new SectionFixture(stationFixture);
        LineApiCaller.지하철_노선에_구간_추가(sectionFixture.get선릉역_부터_삼성역_구간_params(), "/lines/" + 삼호선_강남역_부터_선릉역_ID.toString());

        String email = "email@email.com";
        String password = "password";
        int age = 20;
        MemberApiCaller.회원_생성_요청(email, password, age).header("location");

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        accessToken = MemberApiCaller.회원_로그인_요청(params).jsonPath().getString("accessToken");
    }

    /**
     *
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {

    }
}
