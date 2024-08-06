package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_조회;
import static nextstep.member.acceptance.AuthSteps.로그인_토큰_요청;
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

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        강남역_ID = 지하철_역_생성("강남역").body().jsonPath().getLong("id");
        신사역_ID = 지하철_역_생성("신사역").body().jsonPath().getLong("id");
    }

    /**
     * Given 지하철 역이 등록되어 있고,
     * And 회원 가입한 사용자가 있고,
     * And 회원 가입한 사용자가 로그인을 한 상태일 떄,
     * When 시작 역과 종착역을 즐겨찾기로 등록하면
     * Then 자신의 즐겨찾기가 등록된다.
     */
    @Test
    @DisplayName("사용자는 시작 역과 종착 역을 가지고 즐겨찾기를 등록할 수 있다.")
    void createFavorite() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        ExtractableResponse<Response> 로그인_토큰_응답 = 로그인_토큰_요청(EMAIL, PASSWORD);
        String accessToken = 로그인_토큰_응답.jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 강남역_ID, 신사역_ID);

        // then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 역이 등록되어 있고,
     * And 회원 가입한 사용자가 있고,
     * And 회원 가입한 사용자가 로그인을 하지 않은 상태일 떄,
     * When 시작 역과 종착역을 즐겨찾기로 등록하면
     * Then 등록에 실패하고 인증 오류가 발생한다.
     */
    @Test
    @DisplayName("로그인 하지 않은 사용자가 즐겨찾기 등록을 시도하면 인증 오류가 발생한다.")
    void invalidUserCreateFavorite() {
        // given
        String accessToken = "invalid token";

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 강남역_ID, 신사역_ID);

        // then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 지하철 역이 등록되어 있지 않고,
     * And 회원 가입한 사용자가 있고,
     * And 회원 가입한 사용자가 로그인을 한 상태일 떄,
     * When 시작 역과 종착역을 즐겨찾기로 등록하면
     * Then 등록에 오류가 발생한다.
     */
    @Test
    @DisplayName("존재하지 않는 역으로 즐겨찾기를 등록하면 오류가 발생한다.")
    void createFavoriteWithNonExistentStation() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        ExtractableResponse<Response> 로그인_토큰_응답 = 로그인_토큰_요청(EMAIL, PASSWORD);
        String accessToken = 로그인_토큰_응답.jsonPath().getString("accessToken");
        Long 존재하지_않는_역_ID = 999999L;

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 강남역_ID, 존재하지_않는_역_ID);

        // then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 회원 가입한 사용자가 있고,
     * And 회원 가입한 사용자가 로그인을 한 상태이고,
     * And 해당 회원이 즐겨찾기를 등록한 상태일 때
     * When 즐겨찾기를 조회 하면
     * Then 회원이 등록한 즐겨찾기가 조회된다.
     */
    @Test
    @DisplayName("사용자는 자신이 등록한 즐겨찾기를 조회할 수 있다.")
    void getFavorites() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        ExtractableResponse<Response> 로그인_토큰_응답 = 로그인_토큰_요청(EMAIL, PASSWORD);
        String accessToken = 로그인_토큰_응답.jsonPath().getString("accessToken");

        // 즐겨찾기 생성
        즐겨찾기_생성_요청(accessToken, 강남역_ID, 신사역_ID);

        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회(accessToken);

        // then
        assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Map<String, Object>> favorites = 즐겨찾기_조회_응답.jsonPath().getList(".");
        assertThat(favorites).hasSize(1);

        Map<String, Object> favorite = favorites.get(0);
        assertThat(favorite).containsKeys("id", "source", "target");

        Map<String, Object> source = (Map<String, Object>) favorite.get("source");
        Map<String, Object> target = (Map<String, Object>) favorite.get("target");

        assertThat(source.get("id")).isEqualTo(강남역_ID.intValue());
        assertThat(source.get("name")).isEqualTo("강남역");
        assertThat(target.get("id")).isEqualTo(신사역_ID.intValue());
        assertThat(target.get("name")).isEqualTo("신사역");
    }
}
