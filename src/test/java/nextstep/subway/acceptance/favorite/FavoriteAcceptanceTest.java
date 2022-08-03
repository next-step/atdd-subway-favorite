package nextstep.subway.acceptance.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.member.MemberSteps.베어러_인증_내_회원_정보_삭제_요청;
import static nextstep.subway.acceptance.member.MemberSteps.베어러_인증_내_회원_정보_수정;
import static nextstep.subway.acceptance.member.MemberSteps.회원_삭제_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_정보_수정_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_정보_조회_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_정보_조회됨;
import static nextstep.subway.utils.AdministratorInfo.ADMIN_EMAIL;
import static nextstep.subway.utils.AdministratorInfo.ADMIN_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

class FavoriteAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp(){
        super.setUp();
    }

    /**
     * when 로그인 후 사용자가 즐겨찾기를 등록한다.
     * when 즐겨찾기 경로가 등록된다.
     */
    @DisplayName("즐겨찾기를 등록한다.")
    @Test
    void addFavoriteAfterLogin() {
    }

    /**
     * given 즐겨찾기를 등록한다.
     * when 로그인한 사용자가 즐겨찾기 목록을 조회하면
     * then 등록된 즐겨찾기 목록이 조회된다.
     */
    @DisplayName("즐겨찾기 목록을 조회한다.")
    void getFavoriteListAfterLogin() {
    }

    /**
     * given 즐겨찾기를 등록한다.
     * when 로그인한 사용자가 즐겨찾기 목록을 삭제하면
     * then 해당 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기 목록을 조회한다.")
    void deleteFavoriteAfterLogin() {
    }

}