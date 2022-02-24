package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_반환;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성;
import static nextstep.subway.acceptance.MemberAcceptanceTest.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @Autowired
    private StationRepository stationRepository;

    /**
     * Given 회원을 생성하고 로그인을 한 상태에서
     * When 즐겨찾기를 생성하면
     * Then 즐겨찾기 생성이 성공한다.
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavoriteTest() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        String 로그인_회원_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));

        ExtractableResponse<Response> response = 즐겨찾기_생성(강남역.getId(), 역삼역.getId(), 로그인_회원_토큰);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 회원을 생성하고 로그인을 하고 즐겨찾기를 생상한 뒤
     * When 즐겨찾기를 반환하면
     * Then 즐겨찾기가 반환된다.
     */
    @DisplayName("즐겨찾기 전부 반환")
    @Test
    void getFavoritesTest() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        String 로그인_회원_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));

        즐겨찾기_생성(강남역.getId(), 역삼역.getId(), 로그인_회원_토큰);

        ExtractableResponse<Response> response = 즐겨찾기_반환(로그인_회원_토큰);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("유효하지않은 토큰으로 즐겨찾기 반환 요청시 UNAUTHORIZED 반환")
    @Test
    void test() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2("test")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
