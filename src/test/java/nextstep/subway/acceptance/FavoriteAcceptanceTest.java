package nextstep.subway.acceptance;

import static nextstep.auth.token.acceptance.GithubResponses.사용자1;
import static nextstep.subway.acceptance.FavoriteSteps.상태코드_400_응답;
import static nextstep.subway.acceptance.FavoriteSteps.상태코드_401_응답;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록에_즐겨찾기가_존재하지_않는다;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록에_즐겨찾기가_존재한다;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_추가;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_추가_실패;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 기능 인수테스트")
public class FavoriteAcceptanceTest {

    /*
    Given 즐겨찾기를 추가하면
    When 즐겨찾기 목록을 조회했을 때
    Then 즐겨찾기 목록에 추가한 즐겨찾기가 조회된다
     */
    @DisplayName("즐겨찾기를 추가하고 조회한다")
    @Test
    void favoriteAddAndFind() {
        즐겨찾기_추가(사용자1.getAccessToken(), "강남역", "신논현역");

        var 즐겨찾기 = 즐겨찾기_조회(사용자1.getAccessToken());

        즐겨찾기_목록에_즐겨찾기가_존재한다(즐겨찾기, "강남역", "신논현역");
    }

    /*
    Given 권한이 없는 유저가
    When 즐겨찾기를 추가하면
    Then 401 Unauthorized를 응답받는다
     */
    @DisplayName("권한이 없는 유저가 즐겨찾기를 추가한다")
    @Test
    void favoriteAddWithoutAuth() {
        int 즐겨찾기_추가_상태코드 = 즐겨찾기_추가_실패("unknownToken", "강남역", "신논현역");

        상태코드_401_응답(즐겨찾기_추가_상태코드);
    }

    /*
    Given 권한이 없는 유저가
    When 즐겨찾기를 조회하면
    Then 401 Unauthorized를 응답받는다
    * */
    @DisplayName("권한이 없는 유저가 즐겨찾기를 조회한다")
    @Test
    void favoriteFindWithoutAuth() {
        int 즐겨찾기_조회_상태코드 = 즐겨찾기_조회_요청("unknownToken").statusCode();

        상태코드_401_응답(즐겨찾기_조회_상태코드);
    }

    /*
    Given 즐겨찾기 목록이 있고
    When 즐겨찾기를 삭제하면
    Then 즐겨찾기 목록에서 삭제한 즐겨찾기가 조회되지 않는다
     */
    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void favoriteDelete() {
        var 즐겨찾기_id = 즐겨찾기_추가(사용자1.getAccessToken(), "강남역", "신논현역");

        즐겨찾기_삭제(사용자1.getAccessToken(), 즐겨찾기_id);

        var 즐겨찾기 = 즐겨찾기_조회(사용자1.getAccessToken());

        즐겨찾기_목록에_즐겨찾기가_존재하지_않는다(즐겨찾기, 즐겨찾기_id);
    }

    /*
    Given 즐겨찾기를 추가한 후
    And 권한이 없는 유저가
    When 즐겨찾기를 삭제하면
    Then 401 Unauthorized를 응답받는다
    * */
    @DisplayName("권한이 없는 유저가 즐겨찾기를 삭제한다")
    @Test
    void favoriteDeleteWithoutAuth() {
        var 즐겨찾기_id = 즐겨찾기_추가(사용자1.getAccessToken(), "강남역", "신논현역");

        int 즐겨찾기_삭제_상태코드 = 즐겨찾기_삭제_요청("unknownToken", 즐겨찾기_id).statusCode();

        상태코드_401_응답(즐겨찾기_삭제_상태코드);
    }

    /*
    When 없는 즐겨찾기 id에 대해 즐겨찾기를 삭제 요청하면
    Then 400 Bad Request를 응답받는다
    * */
    @DisplayName("없는 즐겨찾기 id에 대해 즐겨찾기를 삭제한다")
    @Test
    void favoriteDeleteWithInvalidId() {
        int 즐겨찾기_삭제_상태코드 = 즐겨찾기_삭제_요청(사용자1.getAccessToken(), 999L).statusCode();

        상태코드_400_응답(즐겨찾기_삭제_상태코드);
    }
}
