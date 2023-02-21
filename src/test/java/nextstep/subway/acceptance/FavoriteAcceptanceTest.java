package nextstep.subway.acceptance;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteErrorResponse;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.exception.FavoriteErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.fixtures.MemberFixtures.*;
import static nextstep.subway.fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String LOCATION_HEADER = "Location";

    @BeforeEach
    void setUpStations() {
        지하철역_생성_요청("강남");
        지하철역_생성_요청("양재");
        지하철역_생성_요청("판교");
    }

    // Given 로그인 요청하고
    // When 출발역과 도착역을 즐겨찾기에 추가하면
    // Then 즐겨찾기가 생성된다.
    @DisplayName("출발역과 도착역을 즐겨찾기에 추가한다")
    @Test
    void 출발역과_도착역을_즐겨찾기에_추가한다() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 강남, 판교);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/favorites/1")
        );
    }

    @DisplayName("즐겨찾기 생성 권한이 없는경우 Unauthorized으로 응답한다")
    @Test
    void 즐겨찾기_생성_권한이_없는경우_Unauthorized으로_응답한다() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(INVALID_ACCESS_TOKEN, 강남, 판교);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    // Given 로그인 요청하고
    // Given 출발역과 도착역을 즐겨찾기에 추가하고
    // Given 즐겨찾기를 생성하고.
    // When 즐겨찾기를 조회하면
    // Then 자신이 생성한 모든 즐겨찾기가 조회된다.
    @DisplayName("즐겨찾기를 조회하면 모든 즐겨찾기가 조회된다")
    @Test
    void 즐겨찾기를_조회하면_모든_즐겨찾기가_조회된다() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        즐겨찾기_생성_요청(accessToken, 강남, 판교);
        즐겨찾기_생성_요청(accessToken, 양재, 판교);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);
        List<FavoriteResponse> favoriteResponses = response.as(new TypeRef<>() {
        });

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(favoriteResponses).hasSize(2)
        );
    }

    @DisplayName("즐겨찾기 조회 권한이 없는경우 Unauthorized으로 응답한다")
    @Test
    void 즐겨찾기_조회_권한이_없는경우_Unauthorized으로_응답한다() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(INVALID_ACCESS_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    // Given 로그인 요청하고
    // Given 출발역과 도착역을 즐겨찾기에 추가하고
    // Given 즐겨찾기를 생성하고.
    // When 생성한 즐겨찾기를 삭제하면
    // Then 즐겨찾기는 삭제된다.
    @DisplayName("즐겨찾기 삭제에 성공한다")
    @Test
    void 즐겨찾기_삭제에_성공한다() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        String resourceLocation = 즐겨찾기_생성_요청(accessToken, 강남, 판교).header(LOCATION_HEADER);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, resourceLocation);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("즐겨찾기 삭제 권한이 없는경우 Unauthorized으로 응답한다")
    @Test
    void 즐겨찾기_삭제_권한이_없는경우_Unauthorized으로_응답한다() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        String resourceLocation = 즐겨찾기_생성_요청(accessToken, 강남, 판교).header(LOCATION_HEADER);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(INVALID_ACCESS_TOKEN, resourceLocation);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    // Given 로그인 요청하고
    // Given 출발역과 도착역을 즐겨찾기에 추가하고
    // Given 즐겨찾기를 생성하고.
    // When 다른 유저가 즐겨찾기 삭제요청을 할 경우
    // Then 삭제에 실패한다.
    @DisplayName("자신이 생성한 즐겨찾기가 아닐경우 삭제에 실패한다")
    @Test
    void 자신이_생성한_즐겨찾기가_아닐경우_삭제에_실패한다() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        String resourceLocation = 즐겨찾기_생성_요청(accessToken, 강남, 판교).header(LOCATION_HEADER);

        String differentMemberAccessToken = 베어러_인증_로그인_요청(EMAIL_2, PASSWORD_2)
                .jsonPath()
                .getString("accessToken");


        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(differentMemberAccessToken, resourceLocation);

        FavoriteErrorResponse favoriteErrorResponse = response.as(new TypeRef<>() {
        });

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(favoriteErrorResponse.getCode())
                        .isEqualTo(FavoriteErrorCode.INVALID_REMOVE_REQUEST.getMessage())
        );
    }
}
