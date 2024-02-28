package nextstep.favorite.acceptance;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.exception.ExceptionResponse;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.fixture.MemberFixture;
import nextstep.subway.domain.request.LineRequest;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.exception.ExceptionMessage.AUTHENTICATION_FAILED;
import static nextstep.exception.ExceptionMessage.NOT_CONNECTED_EXCEPTION;
import static nextstep.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.utils.LineTestUtil.지하철_노선_생성;
import static nextstep.subway.utils.SectionTestUtil.지하철_구간_추가;
import static nextstep.subway.utils.StationTestUtil.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    long 강남역, 역삼역, 선릉역, 이호선;
    String accessToken;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();

        강남역 = 지하철역_생성("강남역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성("역삼역").jsonPath().getLong("id");
        선릉역 = 지하철역_생성("선릉역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성(new LineRequest("2호선", "green", 강남역, 역삼역, 10)).jsonPath().getLong("id");
        지하철_구간_추가(이호선, 역삼역, 선릉역, 10);
    }

    @Test
    void 즐겨찾기_생성() {
        // give
        MemberFixture memberFixture = new MemberFixture();
        accessToken = memberFixture.getAccessToken();

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // then
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(favoriteResponse.getSource().getId()).isEqualTo(강남역),
                () -> assertThat(favoriteResponse.getTarget().getId()).isEqualTo(선릉역)
        );
    }

    @Test
    void 즐겨찾기_조회() {
        // give
        MemberFixture memberFixture = new MemberFixture();
        accessToken = memberFixture.getAccessToken();

        즐겨찾기_생성_요청(강남역, 선릉역, accessToken);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);

        List<FavoriteResponse> favorites = response.as(new TypeRef<>() {});

        // then


        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(favorites).hasSize(1),
                () -> assertThat(favorites.stream().anyMatch(
                        favorite -> favorite.getSource().getId().equals(강남역)
                                && favorite.getTarget().getId().equals(선릉역))).isTrue()
        );
    }

    @Test
    void 즐겨찾기_삭제() {
        // give
        MemberFixture memberFixture = new MemberFixture();
        accessToken = memberFixture.getAccessToken();

        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(강남역, 선릉역, accessToken);
        long favoriteId = response.jsonPath().getLong("id");

        // when
        즐겨찾기_삭제_요청(accessToken, favoriteId);

        // then
        List<FavoriteResponse> favorites = 즐겨찾기_조회_요청(accessToken).as(new TypeRef<>() {});

        assertAll(
                () -> assertThat(favorites).hasSize(0),
                () -> assertThat(favorites.stream().anyMatch(
                        favorite -> favorite.getSource().getId().equals(강남역)
                                && favorite.getTarget().getId().equals(선릉역))).isFalse()
        );
    }

    @DisplayName("로그인 하지않으면 즐겨찾기 생성 불가")
    @Test
    void 로그인X_즐겨찾기_생성() {
        // give

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(강남역, 선릉역, "");
        String message = response.as(ExceptionResponse.class).getMessage();
        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(message).isEqualTo(AUTHENTICATION_FAILED.getMessage())
        );
    }

    /** 이호선 : 강남역 - 역삼역 - 선릉역
     * 사호선 : 사당역 - 이수역
     */
    @Test
    void 비정상_경로_즐겨찾기_등록() {
        // give
        MemberFixture memberFixture = new MemberFixture();
        accessToken = memberFixture.getAccessToken();

        long 사당역 = 지하철역_생성("사당역").jsonPath().getLong("id");
        long 이수역 = 지하철역_생성("이수역").jsonPath().getLong("id");

        long 사호선 = 지하철_노선_생성(new LineRequest("4호선", "blue", 사당역, 이수역, 15)).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(강남역, 이수역, accessToken);
        String message = response.as(ExceptionResponse.class).getMessage();

        // then
        assertAll(
                () -> assertThat(message).isEqualTo(NOT_CONNECTED_EXCEPTION.getMessage())
        );
    }

    @Test
    void 로그인X_즐겨찾기_조회() {
        // give

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청("");
        String message = response.as(ExceptionResponse.class).getMessage();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(message).isEqualTo(AUTHENTICATION_FAILED.getMessage())
        );
    }

    @Test
    void 로그인X_즐겨찾기_삭제() {
        // give

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청("", 1L);
        String message = response.as(ExceptionResponse.class).getMessage();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(message).isEqualTo(AUTHENTICATION_FAILED.getMessage())
        );
    }
}