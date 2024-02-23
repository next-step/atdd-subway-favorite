package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
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

import java.util.List;

import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
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

    @Test
    void 즐겨찾기_생성() {
        // give

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // then
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(favoriteResponse.getSource().getId()).isEqualTo(강남역),
                () -> assertThat(favoriteResponse.getTarget().getId()).isEqualTo(선릉역)
        );
    }

    @Test
    void 즐겨찾기_조회() {
        // give
        즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);

        List<FavoriteResponse> favorites = response.as(new TypeRef<>() {});

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(favorites).hasSize(1),
                () -> assertThat(favorites.stream().anyMatch(
                        favorite -> favorite.getSource().getId().equals(강남역)
                        && favorite.getTarget().getId().equals(선릉역))).isTrue()
        );
    }


}