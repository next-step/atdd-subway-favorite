package nextstep.favorite.acceptance;

import static nextstep.favorite.acceptance.FavoriteAcceptanceSteps.*;
import static nextstep.favorite.acceptance.FavoriteAcceptanceSteps.즐겨찾기_등록_파라미터생성;
import static nextstep.member.acceptance.AuthAcceptanceTest.AGE;
import static nextstep.member.acceptance.AuthAcceptanceTest.EMAIL;
import static nextstep.member.acceptance.AuthAcceptanceTest.PASSWORD;
import static nextstep.subway.acceptance.LineSteps.createSectionCreateParams;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private String accessToken;
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
        accessToken = getAccessToken();
    }

    /**
     * given : 노선, 역, 구간을 생성하고 로그인 후
     * when : 경로 즐겨찾기 등록시
     * then : 경로 즐겨찾기에 성공한다.
     */
    @Test
    @DisplayName("즐겨찾기 생성 성공 테스트")
    void favoriteCreateAcceptanceTest() {
        Map<String, Long> params = 즐겨찾기_등록_파라미터생성(교대역, 양재역);

        즐겨찾기_등록_요청(accessToken, params);
    }

    /**
     * given : 즐겨찾기 등록 후
     * when : 즐겨찾기 조회 시
     * then : 생성된 즐겨찾기 목록이 조회된다
     */
    @Test
    @DisplayName("즐겨찾기 조회 성공 테스트")
    void favoriteGetAcceptanceTest() {
        //given
        즐겨찾기_등록_요청(accessToken, 즐겨찾기_등록_파라미터생성(교대역, 양재역));

        //when
        var response = 즐겨찾기_조회_요청(accessToken);

        //then
        long id = response.jsonPath().getLong("id[0]");
        assertTrue(favoriteRepository.findById(id).isPresent());
    }

    /**
     * given : 즐겨찾기 등록 후
     * when : 즐겨찾기 삭제 후 조회시
     * then : 즐겨찾기가 삭제된다
     */
    @Test
    @DisplayName("즐겨찾기 삭제 성공 테스트")
    void favoriteDeleteAcceptanceTest() {
        //given
        즐겨찾기_등록_요청(accessToken, 즐겨찾기_등록_파라미터생성(교대역, 양재역));
        long id = 즐겨찾기_조회_요청(accessToken).jsonPath().getLong("id[0]");

        //when
        var response = 즐겨찾기_삭제_요청(id, accessToken);

        //then
        assertTrue(favoriteRepository.findById(id).isEmpty());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    public String getAccessToken() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .body(params)
                                                            .when().post("/login/token")
                                                            .then().log().all()
                                                            .statusCode(HttpStatus.OK.value()).extract();

        return response.jsonPath().getString("accessToken");
    }
}