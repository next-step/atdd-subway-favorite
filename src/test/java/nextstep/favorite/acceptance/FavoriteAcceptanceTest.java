package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.acceptance.MemberSteps;
import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.acceptance.StationSteps;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.StationResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.favorite.acceptance.FavoriteSteps.*;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.LineSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String 강남역 = "강남역";
    public static final String 역삼역 = "역삼역";
    public static final String 선릉역 = "선릉역";
    public static final String 삼성역 = "삼성역";
    public static final String EMAIL = "apvmffkdls@gmail.com";
    private Long 강남역Id;
    private Long 역삼역Id;
    private Long 선릉역Id;
    private Long 삼성역Id;

    @BeforeEach
    void init() {
        강남역Id = 지하철역_생성_요청(강남역);
        역삼역Id = 지하철역_생성_요청(역삼역);
        선릉역Id = 지하철역_생성_요청(선릉역);
        삼성역Id = 지하철역_생성_요청(삼성역);
    }
    /**
     * Given 지하철역이 등록되어 있다.
     * And 노선이 등록되어 있다.
     * And 사용자가 등록되어 있다.
     * When 즐겨찾기를 등록한다.
     * Then HTTP 코드 201을 리턴한다.
     */
    @DisplayName("즐겨찾기를 등록한다.")
    @Test
    void createFavorite() {
        노선이_생성되어_있다("이호선", "red", 강남역Id, 역삼역Id, 10);
        회원_생성_요청(EMAIL, "1234", 30);

        final ExtractableResponse<Response> response = 토큰을_포함하여_즐겨찾기를_등록한다(EMAIL, 강남역Id, 역삼역Id);

        HTTP코드를_검증한다(response, HttpStatus.CREATED);
    }

    /**
     * Given 지하철역이 등록되어 있다.
     * And 노선이 등록되어 있다.
     * When JWT 토큰 없이, 즐겨찾기를 등록한다.
     * Then 권한이 없으므로, HTTP 코드 401을 리턴한다.
     */
    @DisplayName("즐겨찾기를 등록 할 때, 인증되지 않은 사용자의 요청은 401코드를 리턴한다.")
    @Test
    void createFavorite_invalid_jwt() {
        노선이_생성되어_있다("이호선", "red", 강남역Id, 역삼역Id, 10);

        final ExtractableResponse<Response> response = 즐겨찾기를_등록한다(강남역Id, 역삼역Id);

        HTTP코드를_검증한다(response, HttpStatus.UNAUTHORIZED);
    }


    /**
     * Given 지하철역이 등록되어 있다.
     * And 사용자가 등록되어 있다.
     * When 비정상 경로를 즐겨찾기로 등록한다.
     * Then 잘못된 요청이므로, HTTP 코드 400 리턴한다.
     */
    @DisplayName("즐겨찾기를 등록 할 때, 존재하지 않는 경로인 경우 400코드를 리턴한다.")
    @Test
    void createFavorite_invalid_path() {
        회원_생성_요청(EMAIL, "1234", 30);

        final ExtractableResponse<Response> response = 토큰을_포함하여_즐겨찾기를_등록한다(EMAIL, 강남역Id, 역삼역Id);

        HTTP코드를_검증한다(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Given 지하철역이 등록되어 있다.
     * And 노선이 등록되어 있다.
     * And 사용자가 등록되어 있다.
     * And 즐겨찾기가 등록되어 있다.
     * When 즐겨찾기로 조회한다.
     * Then 강남역-역삼역의 즐겨찾기가 조회된다.
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void getFavorites() {
        노선이_생성되어_있다("이호선", "red", 강남역Id, 역삼역Id, 10);
        회원_생성_요청(EMAIL, "1234", 30);
        즐겨찾기가_등록되어_있다(EMAIL, 강남역Id, 역삼역Id);

        final ExtractableResponse<Response> response = 즐겨찾기를_조회한다(EMAIL);

        즐겨찾기한_지하철역을_비교한다(response, Arrays.asList(강남역, 역삼역));
    }

    private void 즐겨찾기한_지하철역을_비교한다(ExtractableResponse<Response> response, List<String> stations) {
        final List<String> stationNames = response.jsonPath().getList("[0].stations.name");
        assertThat(stationNames).containsExactlyElementsOf(stations);
    }

    public static Long 즐겨찾기가_등록되어_있다(final String email,final Long source, final Long target) {
        return FavoriteSteps.토큰을_포함하여_즐겨찾기를_등록한다(email, source, target)
                .as(FavoriteResponse.class).getId();
    }

    private static Long 지하철역_생성_요청(final String name) {
        return StationSteps.지하철역_생성_요청(name).as(StationResponse.class).getId();
    }

    private static void HTTP코드를_검증한다(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}