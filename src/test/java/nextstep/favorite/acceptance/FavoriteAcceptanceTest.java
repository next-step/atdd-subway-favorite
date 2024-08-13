package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.MemberRepository;
import nextstep.subway.presentation.LineRequest;
import nextstep.subway.presentation.SectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static nextstep.favorite.acceptance.FavoriteSteps.*;
import static nextstep.auth.acceptance.AuthSteps.로그인_토큰_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성;
import static nextstep.subway.acceptance.SectionSteps.지하철_구간_생성;
import static nextstep.subway.acceptance.StationSteps.지하철_역_생성;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("즐겨찾기 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FavoriteAcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;
    private Long 강남역_ID;
    private Long 신사역_ID;
    private Long 양재역_ID;
    private Long 교대역_ID;
    private Long 신분당선_ID;

    private String accessToken;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        강남역_ID = 지하철_역_생성("강남역").body().jsonPath().getLong("id");
        신사역_ID = 지하철_역_생성("신사역").body().jsonPath().getLong("id");
        양재역_ID = 지하철_역_생성("양재역").body().jsonPath().getLong("id");
        교대역_ID = 지하철_역_생성("교대역").body().jsonPath().getLong("id");

        LineRequest 신분당선_request = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 양재역_ID, 10);
        신분당선_ID = 지하철_노선_생성(신분당선_request).body().jsonPath().getLong("id");

        SectionRequest 신분당성_구간_request = new SectionRequest(강남역_ID, 신사역_ID, 5);
        지하철_구간_생성(신분당선_ID, 신분당성_구간_request);

        accessToken = 사용자_설정_및_로그인(EMAIL, PASSWORD);
    }

    /**
     * Given 지하철 역이 등록되어 있고,
     * And 회원 가입한 사용자가 있고,
     * And 해당 사용자가 로그인을 한 상태일 떄,
     * When 해당 사용자가 시작 역과 종착역을 즐겨찾기로 등록하면
     * Then 자신의 즐겨찾기가 등록된다.
     */
    @Test
    @DisplayName("사용자는 시작 역과 종착 역을 가지고 즐겨찾기를 등록할 수 있다.")
    void createFavorite() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 강남역_ID, 신사역_ID);

        // then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    /**
     * Given 지하철 역이 등록되어 있고,
     * And 회원 가입한 사용자가 있고,
     * And 해당 사용자가 로그인을 하지 않은 상태일 떄,
     * When 해당 사용자가 시작 역과 종착역을 즐겨찾기로 등록하면
     * Then 등록에 실패하고 인증 오류가 발생한다.
     */
    @Test
    @DisplayName("로그인 하지 않은 사용자가 즐겨찾기 등록을 시도하면 인증 오류가 발생한다.")
    void invalidUserCreateFavorite() {
        // given
        var invalidAccessToken = "invalid token";

        // when
        var 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(invalidAccessToken, 강남역_ID, 신사역_ID);

        // then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 지하철 역이 등록되어 있지 않고,
     * And 회원 가입한 사용자가 있고,
     * And 해당 사용자가 로그인을 한 상태일 떄,
     * When 해당 사용자가 시작 역과 종착역을 즐겨찾기로 등록하면
     * Then 등록에 오류가 발생한다.
     */
    @Test
    @DisplayName("존재하지 않는 역으로 즐겨찾기를 등록하면 오류가 발생한다.")
    void createFavoriteWithNonExistentStation() {
        // given
        var 존재하지_않는_역_ID = 999999L;

        // when
        var 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 강남역_ID, 존재하지_않는_역_ID);

        // then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 회원 가입한 사용자가 있고,
     * And 해당 사용자가 로그인을 한 상태이고,
     * And 해당 사용자가 즐겨찾기를 등록한 상태일 때
     * When 해당 사용자의 즐겨찾기를 조회 하면
     * Then 해당 사용자가 등록한 즐겨찾기가 조회된다.
     */
    @Test
    @DisplayName("사용자는 자신이 등록한 즐겨찾기를 조회할 수 있다.")
    void getFavorites() {
        // given
        즐겨찾기_생성_요청(accessToken, 강남역_ID, 신사역_ID);

        // when
        var 즐겨찾기_조회_응답 = 즐겨찾기_조회(accessToken);

        // then
        assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Map<String, Object>> favorites = 즐겨찾기_조회_응답.jsonPath().getList(".");
        assertThat(favorites).hasSize(1);

        var favorite = favorites.get(0);
        assertThat(favorite).containsKeys("id", "source", "target");

        var source = (Map<String, Object>) favorite.get("source");
        var target = (Map<String, Object>) favorite.get("target");

        assertThat(source.get("id")).isEqualTo(강남역_ID.intValue());
        assertThat(source.get("name")).isEqualTo("강남역");
        assertThat(target.get("id")).isEqualTo(신사역_ID.intValue());
        assertThat(target.get("name")).isEqualTo("신사역");
    }

    /**
     * Given 회원 가입한 사용자가 있고,
     * And 해당 사용자가 로그인을 한 상태이고,
     * And 해당 사용자가 즐겨찾기를 등록한 상태일 때
     * When 해당 사용자가 즐겨찾기를 삭제 하면
     * Then 헤딩 사용자가 등록한 즐겨찾기가 삭제된다.
     */
    @Test
    @DisplayName("사용자는 자신이 등록한 즐겨찾기를 삭제할 수 있다.")
    void deleteFavorite() {
        // given
        var favoriteId = 즐겨찾기_생성_및_ID_추출(accessToken, 강남역_ID, 신사역_ID);

        // when
        var 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(accessToken, favoriteId);

        // then
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        var 즐겨찾기_조회_응답 = 즐겨찾기_조회(accessToken);
        var favorites = 즐겨찾기_조회_응답.jsonPath().getList(".");
        assertThat(favorites).isEmpty();
    }

    /**
     * Given 회원 가입한 사용자가 있고,
     * And 헤딩 사용자가 로그인을 한 상태이고,
     * And 해당 사용자가 즐겨찾기를 등록한 상태일 때
     * When 다른 사용자가 해당 사용자의 즐겨찾기를 삭제 하면
     * Then 즐겨찾기가 삭제가 실패하고 권한 오류가 발생한다.
     */
    @Test
    @DisplayName("다른 사용자의 즐겨찾기를 삭제하려고 하면 인증 오류가 발생한다.")
    void deleteOtherUsersFavorite() {
        // given
        var favoriteId = 즐겨찾기_생성_및_ID_추출(accessToken, 강남역_ID, 신사역_ID);
        var otherAccessToken = 사용자_설정_및_로그인("other@email.com", "otherpassword");

        // when
        var 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(otherAccessToken, favoriteId);

        // then
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private String 사용자_설정_및_로그인(String email, String password) {
        memberRepository.save(new Member(email, password, AGE));
        var 로그인_토큰_응답 = 로그인_토큰_요청(email, password);
        return 로그인_토큰_응답.jsonPath().getString("accessToken");
    }

    private Long 즐겨찾기_생성_및_ID_추출(String accessToken, Long sourceStationId, Long targetStationId) {
        var 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, sourceStationId, targetStationId);
        var location = 즐겨찾기_생성_응답.header("Location");
        return extractIdFromLocation(location);
    }

    private Long extractIdFromLocation(String location) {
        var splitLocation = location.split("/");
        return Long.parseLong(splitLocation[splitLocation.length - 1]);
    }
}
