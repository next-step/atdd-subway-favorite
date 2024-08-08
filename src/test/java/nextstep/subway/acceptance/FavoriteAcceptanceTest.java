package nextstep.subway.acceptance;

import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.auth.acceptance.AuthSteps.이메일_패스워드_로그인;
import static nextstep.auth.acceptance.AuthSteps.인증토큰을_추출한다;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.step.BaseStepAsserter.응답_상태값이_올바른지_검증한다;
import static nextstep.subway.acceptance.step.FavoriteStepExtractor.즐겨찾기_추출기.단일_응답의_id_를_추출한다;
import static nextstep.subway.acceptance.step.FavoriteSteps.*;
import static nextstep.subway.acceptance.step.LineStep.*;
import static nextstep.subway.acceptance.step.LineStepExtractor.노선_추출기;
import static nextstep.subway.acceptance.step.StationStep.*;
import static nextstep.subway.acceptance.step.StationStepExtractor.역_추출기;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends BaseTestSetup {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    private Long 시청역_id;
    private Long 서울역_id;
    private Long 을지로입구역_id;
    private Long 을지로3가역_id;
    private Long 충무로역_id;

    private String 인증토큰;

    /**
     *           *2호선*
     * 시청역 --- 을지로입구역 --- 을지로3가역
     *  |                         \
     *  | *1호선*                   \ *3호선*
     *  |                           \
     * 서울역                       충무로역
     */
    @BeforeEach
    public void setUp() {
        시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        서울역_id = 역_추출기.단일_id_를_추출한다(서울역을_생성한다());
        을지로입구역_id = 역_추출기.단일_id_를_추출한다(을지로입구역을_생성한다());
        을지로3가역_id = 역_추출기.단일_id_를_추출한다(을지로3가역을_생성한다());
        충무로역_id = 역_추출기.단일_id_를_추출한다(충무로역을_생성한다());

        일호선을_생성한다(시청역_id, 서울역_id, 10L);
        삼호선을_생성한다(을지로3가역_id, 충무로역_id, 10L);
        var 이호선_id = 노선_추출기.단일_id_를_추출한다(이호선을_생성한다(시청역_id, 을지로입구역_id, 10L));

        구간을_추가한다(이호선_id, 을지로입구역_id, 을지로3가역_id, 10L);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        인증토큰 = 인증토큰을_추출한다(이메일_패스워드_로그인(EMAIL, PASSWORD));
    }

    /**
     * Given: 지하철 노선이 등록되어 있고
     * When: 회원이 경로를 즐겨찾기로 등록하면
     * Then: 해당 경로가 즐겨찾기로 등록되고 즐겨찾기 목록에 포함된다.
     */
    @Test
    public void 즐겨찾기_등록_테스트() {
        // when
        var 즐겨찾기_등록_응답값 = 즐겨찾기_등록_요청(서울역_id, 충무로역_id, 인증토큰);

        // then
        응답_상태값이_올바른지_검증한다(즐겨찾기_등록_응답값, HttpStatus.CREATED.value());
        단일_즐겨찾기_정보_조회됨(즐겨찾기_등록_응답값, "서울역", "충무로역");

        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청(인증토큰), 단일_응답의_id_를_추출한다(즐겨찾기_등록_응답값));
    }

    /**
     * Given: 회원이 2개의 즐겨찾기를 등록했고
     * When: 본인의 즐켜찾기 목록을 조회하면
     * Then: 2개의 즐겨찾기 목록이 반환된다.
     */
    @Test
    public void 즐겨찾기_목록_조회_테스트() {
        // given
        var 서울_을지로3가_즐겨찾기_id = 단일_응답의_id_를_추출한다(즐겨찾기_등록_요청(서울역_id, 을지로3가역_id, 인증토큰));
        var 시청_충무로_즐겨찾기_id = 단일_응답의_id_를_추출한다(즐겨찾기_등록_요청(시청역_id, 충무로역_id, 인증토큰));

        // when
        var 즐겨찾기_목록_조회_응답값 = 즐겨찾기_목록_조회_요청(인증토큰);

        // then
        응답_상태값이_올바른지_검증한다(즐겨찾기_목록_조회_응답값, HttpStatus.OK.value());

        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답값, 서울_을지로3가_즐겨찾기_id, 시청_충무로_즐겨찾기_id);
    }


    /**
     * Given: 회원이 즐겨찾기를 등록했고
     * When: 본인의 즐켜찾기 목록을 삭제하면
     * Then: 해당 즐켜찾기가 삭제되고 즐겨찾기 목록에서 제외된다.
     */
    @Test
    public void 즐겨찾기_삭제_테스트() {
        // given
        var 서울_을지로3가_즐겨찾기_id = 단일_응답의_id_를_추출한다(즐겨찾기_등록_요청(서울역_id, 을지로3가역_id, 인증토큰));
        단일_응답의_id_를_추출한다(즐겨찾기_등록_요청(시청역_id, 충무로역_id, 인증토큰));

        // when
        var 즐겨찾기_삭제_응답값 = 즐겨찾기_삭제_요청(서울_을지로3가_즐겨찾기_id, 인증토큰);

        // then
        응답_상태값이_올바른지_검증한다(즐겨찾기_삭제_응답값, HttpStatus.NO_CONTENT.value());

        // then
        즐겨찾기_목록에서_제거됨(즐겨찾기_목록_조회_요청(인증토큰), 서울_을지로3가_즐겨찾기_id);
    }

}