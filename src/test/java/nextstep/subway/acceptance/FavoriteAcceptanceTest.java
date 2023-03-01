package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역;
    private Long 양재역;
    private Long 양재시민의숲역;
    private Long 청계산입구역;

    private String myAccessToken;
    private String otherAccessToken;

    /**
     * GIVEN 지하철 역을 두개 생성하고
     * GIVEN 회원가입 후 로그인하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").jsonPath().getLong("id");
        청계산입구역 = 지하철역_생성_요청("청계산입구역").jsonPath().getLong("id");

        회원_생성_요청("bactoria@gmail.com", "qwe123", 20);
        var 나의_베어러_인증_로그인_응답 = 베어러_인증_로그인_요청("bactoria@gmail.com", "qwe123");
        myAccessToken = Access_Token을_가져온다(나의_베어러_인증_로그인_응답);

        회원_생성_요청("vivi@gmail.com", "qwe123", 20);
        var 다른_사람의_베어러_인증_로그인_응답 = 베어러_인증_로그인_요청("vivi@gmail.com", "qwe123");
        otherAccessToken = Access_Token을_가져온다(다른_사람의_베어러_인증_로그인_응답);
    }

    /**
     * WHEN 로그인한 회원이 출발역과 도착역을 즐겨찾기 하는 경우 <br>
     * THEN 즐겨찾기가 추가된다 <br>
     */
    @DisplayName("로그인한 유저가 즐겨찾기 추가")
    @Test
    void addFavoriteLoginUser() {
        // when
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(myAccessToken, 강남역, 양재역);

        // then
        즐겨찾기_추가됨(즐겨찾기_추가_응답);
    }

    /**
     * WHEN 로그인하지 않거나, 유효하지 않은 회원 인증정보로 출발역과 도착역을 즐겨찾기 하는 경우 <br>
     * THEN 예외가 발생한다 <br>
     */
    @DisplayName("로그인 하지 않은 유저가 즐겨찾기 추가")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Bearer abcd"})
    void addFavoriteNotLoginUser(String illegalAccessToken) {
        // when
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(illegalAccessToken, 강남역, 양재역);

        // then
        인증_예외_발생함(즐겨찾기_추가_응답);
    }

    /**
     * GIVEN 로그인한 회원이 즐겨찾기를 2번 하고 <br>
     * WHEN 로그인한 회원이 즐겨찾기 조회하는 경우 <br>
     * THEN 2개의 즐겨찾기 목록이 조회된다 <br>
     */
    @DisplayName("로그인한 유저가 즐겨찾기 조회")
    @Test
    void readFavoriteLoginUser() {
        // given
        즐겨찾기_추가_요청(myAccessToken, 강남역, 양재역);
        즐겨찾기_추가_요청(myAccessToken, 양재시민의숲역, 청계산입구역);

        // when
        var 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(myAccessToken);

        // then
        즐겨찾기_조회됨(즐겨찾기_조회_응답, List.of(강남역, 양재시민의숲역), List.of(양재역, 청계산입구역));
    }

    /**
     * WHEN 로그인하지 않거나, 유효하지 않은 회원 인증정보로 즐겨찾기 조회하는 경우 <br>
     * THEN 예외가 발생한다 <br>
     */
    @DisplayName("로그인하지 않은 유저가 즐겨찾기 조회")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Bearer abcd"})
    void readFavoriteNotLoginUser(String illegalAccessToken) {
        // when
        var 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(illegalAccessToken);

        // then
        assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * GIVEN 로그인한 회원이 즐겨찾기를 하고 <br>
     * WHEN 즐겨찾기를 삭제하는 경우 <br>
     * THEN 즐겨찾기가 삭제된다 <br>
     */
    @DisplayName("로그인한 유저가 즐겨찾기 삭제")
    @Test
    void deleteFavoriteLoginUser() {
        // given
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(myAccessToken, 강남역, 양재역);
        String location = 즐겨찾기_추가_응답.header("location");

        // when
        var 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(myAccessToken, location);

        // then
        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }

    /**
     * WHEN 로그인하지 않거나, 유효하지 않은 회원 인증정보로 즐겨찾기 삭제하는 경우 <br>
     * THEN 예외가 발생한다 <br>
     */
    @DisplayName("로그인하지 않은 유저가 즐겨찾기 삭제")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Bearer abcd"})
    void deleteFavoriteNotLoginUser(String illegalAccessToken) {
        // given
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(myAccessToken, 강남역, 양재역);
        String location = 즐겨찾기_추가_응답.header("location");

        // when
        var 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(illegalAccessToken, location);

        // then
        인증_예외_발생함(즐겨찾기_삭제_응답);
    }

    /**
     * WHEN 로그인한 유저가 존재하지 않는 즐겨찾기를 삭제하는 경우 <br>
     * THEN 예외가 발생한다 <br>
     */
    @DisplayName("로그인한 유저가 존재하지 않는 즐겨찾기 삭제")
    @Test
    void deleteNotExistsFavorite() {
        // given
        Long 존재하지_않는_즐겨찾기_아이디 = Long.MAX_VALUE;
        String location = "/favorites/" + 존재하지_않는_즐겨찾기_아이디;

        // when
        var 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(myAccessToken, location);

        // then
        찾을_수_없는_예외_발생함(즐겨찾기_삭제_응답);
    }

    /**
     * WHEN 로그인한 유저가 다른 유저의 즐겨찾기를 삭제하는 경우 <br>
     * THEN 예외가 발생한다 <br>
     */
    @DisplayName("로그인한 유저가 다른 사람의 즐겨찾기 삭제")
    @Test
    void deleteOtherUserFavorite() {
        // given
        var 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(otherAccessToken, 강남역, 양재역);
        String location = 즐겨찾기_추가_응답.header("location");

        // when
        var 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(myAccessToken, location);

        // then
        권한_예외_발생함(즐겨찾기_삭제_응답);
    }
}
