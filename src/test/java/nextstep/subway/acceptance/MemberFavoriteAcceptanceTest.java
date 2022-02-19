package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.MemberFavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberFavoriteAcceptanceTest extends MemberAcceptanceTest {

    @DisplayName("나의 즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // given
        Long 강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        Long 양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        Map<String, String> lineCreateParams = LineSectionAcceptanceTest.createLineCreateParams(강남역, 양재역);
        지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> 내_즐겨찾기_생성_요청_응답 = 내_즐겨찾기_생성_요청(accessToken, 강남역, 양재역);

        // then
        즐겨찾기_정보_생성됨(내_즐겨찾기_생성_요청_응답);

        // when
        ExtractableResponse<Response> 내_즐겨찾기_정보_조회_응답 = 내_즐겨찾기_정보_조회_요청(accessToken);

        // then
        즐겨찾기_정보_조회됨(내_즐겨찾기_정보_조회_응답, 강남역, 양재역);

        //given
        String location = 내_즐겨찾기_생성_요청_응답.header("Location");

        // when
        ExtractableResponse<Response> 내_즐겨찾기_삭제_요청_응답 = 내_즐겨찾기_삭제_요청(accessToken, location);

        // then
        즐겨찾기_정보_삭제됨(내_즐겨찾기_삭제_요청_응답);
    }
}
