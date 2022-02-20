package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AuthAcceptanceTest.AGE;
import static nextstep.subway.acceptance.AuthAcceptanceTest.PASSWORD;
import static nextstep.subway.acceptance.FavoriteStep.즐겨찾기_생성;
import static nextstep.subway.acceptance.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.utils.LineStepUtil.*;
import static nextstep.subway.utils.StationStepUtil.지하철_역_아이디_키;
import static nextstep.subway.utils.StationStepUtil.지하철역생성;

@DisplayName("즐겨찾기를 관리한다.")
public class FavoriteAcceptanceTest extends AcceptanceTest{

    private Long 교대역;
    private Long 강남역;
    private Long 이호선;
    private String 액세스_토큰;

    /** Background
     * Given 지하철역 등록되어 있음
     * And   지하철 노선 등록되어 있음
     * And   지하철 노선에 지하철 등록되어 있음
     * And   회원 등록되어 있음
     * And   로그인 되어있음
     */
    @BeforeEach
    void init(){
        //given
        교대역 = 지하철역생성("교대역").jsonPath().getLong(지하철_역_아이디_키);
        강남역 = 지하철역생성("강남역").jsonPath().getLong(지하철_역_아이디_키);

        //And
        이호선 = 노선생성(노선파라미터생성("2호선", "green", 교대역, 강남역, 100)).jsonPath().getLong(노선_아이디_키);

        //And
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        //And
        액세스_토큰 = 로그인_되어_있음(EMAIL,PASSWORD);
    }

    /**
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨     */
    @DisplayName("즐겨찾기를 관리")
    @Test
    void 즐겨찾기_관리(){
        //when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성(액세스_토큰,교대역,강남역);

        //then
        상태_값_검사(즐겨찾기_생성_응답, HttpStatus.CREATED);

    }


}
