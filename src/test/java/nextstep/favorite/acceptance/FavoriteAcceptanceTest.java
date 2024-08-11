package nextstep.favorite.acceptance;



import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.favorite.acceptance.FavoriteSteps.*;
import static nextstep.line.acceptance.LineSteps.*;
import static nextstep.member.acceptance.MemberSteps.회원_로그인_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.station.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {



    private Long 신사역;
    private Long 교대역;
    private Long 양재역;

    private Long 삼호선;

    private String 회원1;
    private String 회원2;


    @BeforeEach
    public void setUp() {
        super.setUp();

        신사역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 양재역, 2L).jsonPath().getLong("id");

        회원_생성_요청("user1@email.com","1234",20);
        회원_생성_요청("user2@email.com","1234",20);
        회원1 = 회원_로그인_요청("user1@email.com","1234").jsonPath().getString("accessToken");
        회원2 = 회원_로그인_요청("user2@email.com","1234").jsonPath().getString("accessToken");


    }

    /**
     * When 지하철 즐겨찾기를 생성하면
     * Then 지하철 즐겨찾기 목록 조회 시 생성한 즐겨찾기를 찾을 수 있다
     */
    @DisplayName("지하철 즐겨찾기 생성")
    @Test
    void createFavorite() {
        // when
        var response = 지하철_즐겨찾기_생성_요청(회원1, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        var listResponse = 지하철_즐겨찾기_조회_요청(회원1);

        assertThat(listResponse.jsonPath().getList("source.name")).contains("교대역");
        assertThat(listResponse.jsonPath().getList("target.name")).contains("양재역");

    }

    /**
     * When 지하철 즐겨찾기를 생성한 사용자가 비로그인 상태이면
     * Then 예외를 발생한다.
     */
    @DisplayName("지하철 즐겨찾기 생성한 사용자가 비로그인 상태일 경우 예외가 발생한다.")
    @Test
    void createFavoriteNotLogin() {
        // when
        var 비로그인 = "Invalid Token";
        var response = 지하철_즐겨찾기_생성_요청(비로그인, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    /**
     * When 지하철 즐겨찾기를 생성한 경로가 비정상 경로일경우
     * Then 예외를 발생한다.
     */
    @DisplayName("지하철 즐겨찾기 생성한 경로가 비정상 경로일경우 예외가 발생한다.")
    @Test
    void createFavoriteNotPath() {
        // when
        var response = 지하철_즐겨찾기_생성_요청(회원1, 신사역, 양재역);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.jsonPath().getString("code")).isEqualTo("P001");
            assertThat(response.jsonPath().getString("message")).isEqualTo(" 경로를 찾을 수 없습니다.");
        });
    }

    /**
     * Given 지하철 즐겨찾기를 생성하고
     * When 지하철 즐겨찾기 목록 조회하면
     * Then 생성한 즐겨찾기를 찾을 수 있다
     */
    @DisplayName("지하철 즐겨찾기 조회")
    @Test
    void getFavorite() {
        // given
        지하철_즐겨찾기_생성_요청(회원1, 교대역, 양재역);

        //when
        var listResponse = 지하철_즐겨찾기_조회_요청(회원1);

        // then
        assertThat(listResponse.jsonPath().getList("source.name")).contains("교대역");
        assertThat(listResponse.jsonPath().getList("target.name")).contains("양재역");

    }

    /**
     * When 지하철 즐겨찾기를 조회한 사용자가 비로그인 상태이면
     * Then 예외를 발생한다.
     */
    @DisplayName("지하철 즐겨찾기 조회한 사용자가 비로그인 상태일 경우 예외가 발생한다.")
    @Test
    void getFavoriteNotLogin() {
        // when
        String 비로그인 = "Invalid Token";
        var response = 지하철_즐겨찾기_조회_요청(비로그인);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());


    }


    /**
     * Given 지하철 즐겨찾기를 생성하고
     * When 생성한 지하철 즐겨찾기를 삭제하면
     * Then 해당 즐겨찾기는 삭제된다.
     */
    @DisplayName("지하철 즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        var response = 지하철_즐겨찾기_생성_요청(회원1, 교대역, 양재역);

        //when
        var deleteResponse = 지하철_즐겨찾기_삭제_요청(회원1, response);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());


    }

    /**
     * When 지하철 즐겨찾기를 삭제시 자신이 등록한 즐겨찾기가 아닐 경우
     * Then 예외가 발생한다.
     */
    @DisplayName("지하철 즐겨찾기를 삭제시 자신이 등록한 즐겨찾기가 아닐경우 예외가 발생한다.")
    @Test
    void deleteFavoriteNotMine() {
        // when
        var response = 지하철_즐겨찾기_생성_요청(회원1, 교대역, 양재역);
        var deleteResponse = 지하철_즐겨찾기_삭제_요청(회원2, response);

        // then
        assertAll(() -> {
            assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(deleteResponse.jsonPath().getString("code")).isEqualTo("A002");
            assertThat(deleteResponse.jsonPath().getString("message")).isEqualTo(" 해당 즐겨찾기를 삭제할 권한이 없습니다.");
        });
    }

}