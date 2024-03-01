package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.LineRestAssuredCRUD;
import nextstep.common.SectionRestAssuredCRUD;
import nextstep.common.StationRestAssuredCRUD;
import nextstep.member.acceptance.MemberRestAssuredCRUD;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.CommonAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends CommonAcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 교대역Id;
    private Long 남부터미널역Id;
    private Long 양재역Id;
    private Long 강남역Id;
    private Long 반포역Id;
    private Long 학동역Id;

    private static int 교대역_남부터미널역_거리 = 3;
    private static int 남부터미널역_양재역_거리 = 4;
    private static int 교대역_강남역_거리 = 1;
    private static int 양재역_강남역_거리 = 2;
    private static int 반포역_학동역_거리 = 5;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 교대역    --- *2호선* (1) ---   강남역           반포역  --- *7호선* (5) ---  학동역
     * |                              |
     * *3호선*(3)                    *신분당선*(2)
     * |                              |
     * 남부터미널역  --- *3호선*(4) ---  양재역
     *
     */

    @BeforeEach
    void setStation() {
        교대역Id = extractResponseId(StationRestAssuredCRUD.createStation("교대역"));
        남부터미널역Id = extractResponseId(StationRestAssuredCRUD.createStation("남부터미널역"));
        양재역Id = extractResponseId(StationRestAssuredCRUD.createStation("양재역"));
        강남역Id = extractResponseId(StationRestAssuredCRUD.createStation("강남역"));
        반포역Id = extractResponseId(StationRestAssuredCRUD.createStation("반포역"));
        학동역Id = extractResponseId(StationRestAssuredCRUD.createStation("학동역"));

        setOrangeLine();
        setGreenLine();
        setRedLine();
        setDarkGreenLine();
    }

    void setOrangeLine() {
        ExtractableResponse<Response> lineResponse = LineRestAssuredCRUD.createLine("3호선", "bg-orange-600");
        Long 삼호선Id = lineResponse.jsonPath().getLong("id");

        SectionRestAssuredCRUD.addSection(교대역Id, 남부터미널역Id, 교대역_남부터미널역_거리, 삼호선Id);
        SectionRestAssuredCRUD.addSection(남부터미널역Id, 양재역Id, 남부터미널역_양재역_거리, 삼호선Id);
    }

    void setGreenLine() {
        ExtractableResponse<Response> lineResponse = LineRestAssuredCRUD.createLine("2호선", "bg-green-600");
        Long 이호선Id = lineResponse.jsonPath().getLong("id");

        SectionRestAssuredCRUD.addSection(교대역Id, 강남역Id, 교대역_강남역_거리, 이호선Id);
    }

    void setRedLine() {
        ExtractableResponse<Response> lineResponse = LineRestAssuredCRUD.createLine("신분당선", "bg-red-600");
        Long 신분당선Id = lineResponse.jsonPath().getLong("id");

        SectionRestAssuredCRUD.addSection(강남역Id, 양재역Id, 양재역_강남역_거리, 신분당선Id);
    }

    void setDarkGreenLine() {
        ExtractableResponse<Response> lineResponse = LineRestAssuredCRUD.createLine("7호선", "bg-darkgreen-600");
        Long 칠호선Id = lineResponse.jsonPath().getLong("id");

        SectionRestAssuredCRUD.addSection(반포역Id, 학동역Id, 반포역_학동역_거리, 칠호선Id);
    }


    /**
     * given 인증된 회원의 즐겨찾기를 생성하고
     * when 즐겨찾기 목록을 조회하면
     * then 생성한 회원의 즐겨찾기를 조회할 수 있다.
     */
    @Test
    @DisplayName("인증된 회원의 즐겨찾기를 생성하고 조회한다.")
    void 즐겨찾기_생성_조회() {
        //given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        ExtractableResponse<Response> 회원_인증_응답 = MemberRestAssuredCRUD.getLoginToken(EMAIL, PASSWORD);

        String accessToken = 회원_인증_응답.jsonPath().getString("accessToken");

        ExtractableResponse<Response> 즐겨찾기_생성됨 = FavoriteRestAssuredCRUD.createFavorite(강남역Id, 양재역Id, accessToken);
        assertThat(즐겨찾기_생성됨.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        ExtractableResponse<Response> 즐겨찾기_조회됨 = FavoriteRestAssuredCRUD.showFavorite(accessToken);

        List<Long> 즐겨찾기_출발역_리스트 = 즐겨찾기_조회됨.jsonPath().getList("source.id", Long.class);
        List<Long> 즐겨찾기_도착역_리스트 = 즐겨찾기_조회됨.jsonPath().getList("target.id", Long.class);

        assertAll(
            () -> assertThat(즐겨찾기_조회됨.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(즐겨찾기_출발역_리스트).contains(강남역Id),
            () -> assertThat(즐겨찾기_도착역_리스트).contains(양재역Id)
        );
    }

    /**
     * when 인증되지 않은 회원의 즐겨찾기를 생성하면
     * then 인증 오류가 발생한다.
     */
    @Test
    @DisplayName("인증되지 않은 회원이 즐겨찾기를 등록하면 인증 오류가 발생한다.")
    void 즐겨찾기_생성_인증오류() {
        //when
        ExtractableResponse<Response> 빈_토큰값으로_즐겨찾기_생성 = FavoriteRestAssuredCRUD.createFavorite(강남역Id, 양재역Id, "");
        ExtractableResponse<Response> 잘못된_토큰값으로_즐겨찾기_생성 = FavoriteRestAssuredCRUD.createFavorite(강남역Id, 양재역Id, "token");
        ExtractableResponse<Response> null_토큰값으로_즐겨찾기_생성 = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/favorites")
                .then().log().all()
                .extract();

        //then
        assertAll(
                () -> assertThat(빈_토큰값으로_즐겨찾기_생성.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(잘못된_토큰값으로_즐겨찾기_생성.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(null_토큰값으로_즐겨찾기_생성.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        );
    }

    /**
     * when 존재하지 않은 경로를 인증된 회원의 즐겨찾기로 생성하면
     * then 400 에러가 발생한다.
     */
    @Test
    @DisplayName("존재하지 않는 경로를 즐겨찾기로 등록하면 405에러가 발생한다.")
    void 존재하지_않는_경로_즐겨찾기_등록_오류() {
        //given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        ExtractableResponse<Response> 회원_인증_응답 = MemberRestAssuredCRUD.getLoginToken(EMAIL, PASSWORD);

        String accessToken = 회원_인증_응답.jsonPath().getString("accessToken");

        ExtractableResponse<Response> 즐겨찾기_생성됨 = FavoriteRestAssuredCRUD.createFavorite(강남역Id, 반포역Id, accessToken);

        assertThat(즐겨찾기_생성됨.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * when 인증되지 않은 회원이 즐겨찾기를 조회하면
     * then 인증 오류가 발생한다.
     */
    @Test
    @DisplayName("인증되지 않은 회원이 즐겨찾기를 조회하면 인증 오류가 발생한다.")
    void 즐겨찾기_조회_인증오류() {
        //when
        ExtractableResponse<Response> 빈_토큰값으로_즐겨찾기_조회 = FavoriteRestAssuredCRUD.showFavorite("");
        ExtractableResponse<Response> 잘못된_토큰값으로_즐겨찾기_조회 = FavoriteRestAssuredCRUD.showFavorite("token");
        ExtractableResponse<Response> null_토큰값으로_즐겨찾기_조회 = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/favorites")
                .then().log().all()
                .extract();

        //then
        assertAll(
                () -> assertThat(빈_토큰값으로_즐겨찾기_조회.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(잘못된_토큰값으로_즐겨찾기_조회.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(null_토큰값으로_즐겨찾기_조회.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        );
    }

    /**
     * given 인증된 회원의 즐겨찾기를 등록하고
     * when 즐겨찾기를 삭제하면
     * then 즐겨찾기 조회 시 해당 즐겨찾기를 조회할 수 없다.
     */
    @Test
    @DisplayName("인증된 회원의 즐겨찾기를 삭제한다.")
    void 즐겨찾기_삭제() {

    }

    /**
     * when 인증되지 않은 회원이 즐겨찾기를 삭제하면
     * then 인증 오류가 발생한다.
     */
    @Test
    @DisplayName("인증되지 않은 회원이 즐겨찾기를 삭제하면 인증 오류가 발생한다.")
    void 즐겨찾기_삭제_인증오류() {

    }

    Long extractResponseId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }
}