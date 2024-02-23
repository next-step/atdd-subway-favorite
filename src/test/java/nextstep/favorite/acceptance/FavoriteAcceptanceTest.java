package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.fixture.MemberFixture;
import nextstep.subway.domain.request.LineRequest;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.LineTestUtil.지하철_노선_생성;
import static nextstep.subway.utils.SectionTestUtil.지하철_구간_추가;
import static nextstep.subway.utils.StationTestUtil.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    long 강남역, 역삼역, 선릉역, 이호선;
    String accessToken;

    @BeforeEach
    public void setUp() {
        강남역 = 지하철역_생성("강남역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성("역삼역").jsonPath().getLong("id");
        선릉역 = 지하철역_생성("선릉역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성(new LineRequest("2호선", "green", 강남역, 역삼역, 10)).jsonPath().getLong("id");
        지하철_구간_추가(이호선, 역삼역, 선릉역, 10);

        MemberFixture memberFixture = new MemberFixture();
        accessToken = memberFixture.getAccessToken();
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void 즐겨찾기_생성() {
        // give

        // when
        Map<String, Object> request = new HashMap<>();
        request.put("source", 강남역);
        request.put("target", 선릉역);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();

        // then
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(favoriteResponse.getSource().getId()).isEqualTo(강남역),
                () -> assertThat(favoriteResponse.getTarget().getId()).isEqualTo(선릉역)
        );
    }

}