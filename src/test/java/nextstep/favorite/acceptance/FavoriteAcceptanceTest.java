package nextstep.favorite.acceptance;

import static nextstep.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.subway.application.dto.LineRequest;
import nextstep.utils.AcceptanceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberService memberService;

    private String token;
    private Long 교대역;
    private Long 양재역;

    @BeforeEach
    void setUpBeforeEach() {
        super.setUp();

        MemberRequest memberRequest = new MemberRequest("email@example.com", "password", 20);
        memberService.createMember(memberRequest);
        token = jwtTokenProvider.createToken(memberRequest.getEmail());

        교대역 = createStationAndGetId("교대역");
        양재역 = createStationAndGetId("양재역");

        createLine(new LineRequest("2호선", "green", 교대역, 양재역, 10));
    }

    @Test
    @DisplayName("즐겨찾기를 생성할 때 유효한 요청이면 즐겨찾기가 생성된다")
    void createFavorite() {
        // given

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, 교대역, 양재역);

        // then
        즐겨찾기_생성됨(response);
    }

    @Test
    @DisplayName("즐겨찾기를 조회할 때 즐겨찾기가 존재하면 즐겨찾기 목록을 반환한다")
    void getFavorites() {
        // given
        즐겨찾기_생성_요청(token, 교대역, 양재역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(token);

        // then
        즐겨찾기_조회됨(response, "교대역", "양재역");
    }

    @Test
    @DisplayName("즐겨찾기를 삭제할 때 유효한 요청이면 즐겨찾기가 삭제된다")
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(token, 교대역, 양재역);
        String location = createResponse.header("Location");
        Long favoriteId = Long.parseLong(location.split("/")[2]);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(token, favoriteId);

        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    @Test
    @DisplayName("즐겨찾기를 생성할 때 비정상 경로이면 예외가 발생한다")
    void invalidFavoriteCreation() {
        // given
        Long invalidSourceId = 999L;
        Long invalidTargetId = 1000L;

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, invalidSourceId, invalidTargetId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
