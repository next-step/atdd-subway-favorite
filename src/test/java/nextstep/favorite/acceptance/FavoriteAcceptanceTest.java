package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.line.LineSteps;
import nextstep.member.domain.MemberRepository;
import nextstep.section.SectionSteps;
import nextstep.station.StationSteps;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        StationSteps.역_생성_요창("교대역");    // 1L
        StationSteps.역_생성_요창("강남역");    // 2L
        StationSteps.역_생성_요창("양재역");    // 3L
        StationSteps.역_생성_요창("남부터미널역");// 4L

        LineSteps.노선_생성_요청("2호선", 1L, 2L, 10);  // 1L
        LineSteps.노선_생성_요청("신분당선", 2L, 3L, 10);// 2L
        LineSteps.노선_생성_요청("3호선", 1L, 4L, 2);   // 3L
        SectionSteps.구간_생성_요청(3L, 4L, 3L, 3);
    }

    @DisplayName("즐겨찾기 생성 성공")
    @Test
    void favoriteCreationSuccess() {
        // given
        Long 교대역Id = 1L;
        Long 양재역Id = 3L;

        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청(교대역Id, 양재역Id, ACCESS_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("즐겨찾기 조회 성공")
    @Test
    void favoriteFindSuccess() {
        // given
        Long 교대역Id = 1L;
        Long 양재역Id = 3L;

        FavoriteSteps.즐겨찾기_생성_요청(교대역Id, 양재역Id, ACCESS_TOKEN);

        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기목록_조회_요청(ACCESS_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favoriteResponses.get(0).getSource().getId()).isEqualTo(교대역Id);
        assertThat(favoriteResponses.get(0).getTarget().getId()).isEqualTo(양재역Id);
    }

    @DisplayName("즐겨찾기 삭제 성공")
    @Test
    void favoriteDeletionSuccess() {
        // given
        Long 교대역Id = 1L;
        Long 양재역Id = 3L;

        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청(교대역Id, 양재역Id, ACCESS_TOKEN);

        // when
        response = FavoriteSteps.즐겨찾기_삭제_요청(response, ACCESS_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("비로그인 시 즐겨찾기 생성 실패")
    @Test
    void favoriteCreationFail() {
        // given
        Long 교대역Id = 1L;
        Long 양재역Id = 3L;

        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청(교대역Id, 양재역Id, "");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}