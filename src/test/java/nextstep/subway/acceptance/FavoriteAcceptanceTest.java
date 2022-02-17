package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteAcceptanceSteps.구간_생성_파람;
import static nextstep.subway.acceptance.FavoriteAcceptanceSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 증겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String INVALID_ACCESS_TOKEN = "invalidAccessToken";

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    public static Long 일호선;
    public static Long 이호선;
    public static Long 삼호선;
    public static Long 사호선;

    public static Long 연신내역;
    public static Long 서울역;
    public static Long 삼성역;
    public static Long 강남역;
    public static Long 영등포역;
    public static Long 신길역;

    public static String 로그인_토큰;

    /**
     *                     (10)
     *      연신내역   --- *1호선* ---   서울역
     *        |
     * (10) *2호선*
     *        |
     *      삼성역   --- *3호선* ---   강남역
     *                   (10)
     *
     *                   (10)
     *      영등포역   --- *4호선* ---   신길역
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        연신내역 = 지하철역_생성_요청("연신내역").jsonPath().getLong("id");
        서울역 = 지하철역_생성_요청("서울역").jsonPath().getLong("id");
        삼성역 = 지하철역_생성_요청("삼성역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        영등포역 = 지하철역_생성_요청("영등포역").jsonPath().getLong("id");
        신길역 = 지하철역_생성_요청("신길역").jsonPath().getLong("id");

        일호선 = 지하철_노선_생성_요청("일호선", "bg-red-100").jsonPath().getLong("id");
        이호선 = 지하철_노선_생성_요청("이호선", "bg-red-200").jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청("삼호선", "bg-red-300").jsonPath().getLong("id");
        사호선 = 지하철_노선_생성_요청("사호선", "bg-red-400").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(일호선, 구간_생성_파람(연신내역, 서울역, 10));
        지하철_노선에_지하철_구간_생성_요청(이호선, 구간_생성_파람(연신내역, 삼성역, 10));
        지하철_노선에_지하철_구간_생성_요청(삼호선, 구간_생성_파람(삼성역, 강남역, 10));
        지하철_노선에_지하철_구간_생성_요청(사호선, 구간_생성_파람(영등포역, 신길역, 10));

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        로그인_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("지하철 노선 즐겨찾기 생성")
    @Test
    void 지하철_노선_즐겨찾기_생성() {
        //when
        val 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인_토큰, 삼성역, 서울역);
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("[예외] 지하철 노선 연결되지 않은 즐겨찾기 요청 방지")
    @Test
    void 지하철_노선_연결되지_않은_즐겨찾기_요청_방지() {
        //when
        val 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인_토큰, 삼성역, 신길역);
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("잘못된 유저 정보로는 즐겨찾기를 생성할 수 없다")
    @Test
    void createFavoriteWithInvalidAccessToken() {
        // when
        final ExtractableResponse<Response> response = 즐겨찾기_생성_요청(INVALID_ACCESS_TOKEN, 삼성역, 서울역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
