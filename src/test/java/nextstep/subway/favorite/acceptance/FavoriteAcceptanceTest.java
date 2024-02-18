package nextstep.subway.favorite.acceptance;

import nextstep.subway.testhelper.*;
import nextstep.subway.testhelper.apicaller.MemberApiCaller;
import nextstep.subway.testhelper.fixture.LineFixture;
import nextstep.subway.testhelper.fixture.MemberFixture;
import nextstep.subway.testhelper.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
     *
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {

    }
}
