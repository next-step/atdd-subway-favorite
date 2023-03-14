package nextstep.favorites;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.favorites.application.dto.FavoriteResponse;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static nextstep.favorites.FavoritesSteps.*;
import static nextstep.member.LoginSteps.베어러_인증_로그인_요청;
import static nextstep.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoritesAcceptanceTest extends AcceptanceTest {

    Long 강남역;
    Long 양재역;

    String EMAIL = "email@email.com";
    String PASSWORD = "password";
    int AGE = 20;

    String 인증정보;




    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        회원_생성_요청(EMAIL, PASSWORD, AGE);

         인증정보 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    /**
     * given 유저의 인증 정보를 가지고
     * when 강남역을 시작지점으로해서 양재역을 도착지점으로 즐겨찾기를 추가하면
     * then 즐겨찾기를 추가 할 수 있다.
     */
    @DisplayName("즐겨찾기를 추가 할 수 있다.")
    @Test
    void favoriteAdd() {
        ExtractableResponse<Response> response = 즐겨찾기_추가(인증정보, 강남역, 양재역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();

    }

    /**
     * given 유저의 인증 정보를 가지고 강남역과 양재역을 즐겨찾기를 추가하고
     * when 유저에 대한 즐겨찾기를 조회하면
     * then 강남역과 양재역에 관한 경로를 조회할 수 있다.
     */
    @DisplayName("즐겨찾기를 조회할 수 있다.")
    @Test
    void favoritesFind() {

        즐겨찾기_추가(인증정보, 강남역, 양재역);
        ExtractableResponse<Response> response = 즐겨찾기_조회(인증정보);
        List<Long> sourceList = response.jsonPath().getList("source.id", Long.class);
        List<Long> targetList = response.jsonPath().getList("target.id", Long.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(sourceList.get(0)).isEqualTo(강남역);
        assertThat(targetList.get(0)).isEqualTo(양재역);
    }

    /**
     * given 유저의 인증 정보를 가지고 강남역과 양재역을 즐겨찾기를 추가하고
     * when 해당 즐겨찾기를 삭제하면
     * then 해당 즐겨찾기를 조회할 수 없다.
     */
    @DisplayName("즐겨찾기를 삭제할 수 있다.")
    @Test
    void favoritesDelete() {

        즐겨찾기_추가(인증정보, 강남역, 양재역);
        Long id = 즐겨찾기_조회(인증정보).jsonPath().getList("id", Long.class).get(0);

        ExtractableResponse<Response> response = 즐겨찾기_삭제(인증정보, id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List list = 즐겨찾기_조회(인증정보).as(List.class);
        assertThat(list).hasSize(0);

    }

    /**
     * given 유저 정보 없이
     * when 즐겨찾기 기능을 사용할 시
     * then 401 에러 메세지를 응답 받는다.
     *
     */
    @DisplayName("즐겨찾기 권한 테스트")
    @Test
    void favoritesAuthorizedTest() {
        즐겨찾기_추가(인증정보, 강남역, 양재역);
        String 가짜_인증_정보 = "";

        final ExtractableResponse<Response> createResponse = 즐겨찾기_추가(가짜_인증_정보, 강남역, 양재역);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        final ExtractableResponse<Response> findResponse = 즐겨찾기_조회(가짜_인증_정보);
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


}
