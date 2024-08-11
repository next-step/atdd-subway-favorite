package nextstep.favorite.acceptance;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.station.dto.StationResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.util.AuthStep.로그인_후_토큰_발급;
import static nextstep.utils.step.FavoriteStep.*;
import static nextstep.subway.util.LineStep.지하철_노선_생성;
import static nextstep.subway.util.StationStep.지하철_역_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    String accessToken;
    StationResponse 강남역;
    StationResponse 역삼역;
    Long 이호선;
    FavoriteRequest 즐겨찾기_요청_강남역_역삼역;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        강남역 = 지하철_역_등록("강남역");
        역삼역 = 지하철_역_등록("역삼역");
        이호선 = 지하철_노선_생성("2호선", "green", 강남역.getId(), 역삼역.getId(), 10L).getId();

        즐겨찾기_요청_강남역_역삼역 = FavoriteRequest.of(강남역.getId(), 역삼역.getId());

        memberRepository.save(Member.of(EMAIL, PASSWORD, AGE));
        accessToken = 로그인_후_토큰_발급(EMAIL, PASSWORD);

    }

    /**
     * User Story : 사용자는 즐겨찾기를 등록할 수 있다.
     **/


    /* Given: 사용자가 로그인에 성공한 다음,
       When : 사용자가 즐겨찾기 생성을 요청하면,
       Then : 사용자의 즐겨찾기가 생성된다. */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    public void create_favorite_success() {
        // when & then
        즐겨찾기_생성(accessToken, 즐겨찾기_요청_강남역_역삼역);
    }

    /* Given: 사용자가 로그인에 성공한 다음, 즐겨찾기 생성을 요청한다.
       When: 사용자가 즐겨찾기 목록을 요청하면,
       Then: 사용자의 즐겨찾기 목록이 반환된다. */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    public void retrieve_favorite_success() {
        // given
        Long 즐겨찾기_ID = 즐겨찾기_생성(accessToken, 즐겨찾기_요청_강남역_역삼역);
        var 즐겨찾기_조회_예상_응답 = FavoriteResponse.of(즐겨찾기_ID, 강남역, 역삼역);

        // when
        var 즐겨찾기_조회_목록 = 즐겨찾기_조회(accessToken);

        // then
        assertAll(
                () -> assertThat(즐겨찾기_조회_목록).containsExactlyInAnyOrder(즐겨찾기_조회_예상_응답)
        );
    }

    /* Given: 사용자가 로그인에 성공한 다음, 즐겨찾기 생성을 요청한다.
       When : 사용자가 즐겨찾기 삭제를 요청하면,
       Then : 사용자의 즐겨찾기가 삭제된다. */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    public void delete_favorite_success() {
        // given
        var 즐겨찾기_ID = 즐겨찾기_생성(accessToken, 즐겨찾기_요청_강남역_역삼역);
        var 즐겨찾기_조회_예상_응답 = FavoriteResponse.of(즐겨찾기_ID, 강남역, 역삼역);

        // when
        즐겨찾기_삭제(accessToken, 즐겨찾기_ID);

        // then
        var 즐겨찾기_조회_목록 = 즐겨찾기_조회(accessToken);
        assertAll(
                () -> assertThat(즐겨찾기_조회_목록).doesNotContain(즐겨찾기_조회_예상_응답)
        );
    }
}

