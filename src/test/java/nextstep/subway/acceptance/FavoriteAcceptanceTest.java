package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.AcceptanceTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.accessToken;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.givens;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.강남역_ID;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.권한이_존재하지_않음;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.역삼역_ID;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_목록_조회_요청;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_목록_조회됨;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_삭제됨;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_생성됨;
import static org.apache.http.HttpHeaders.LOCATION;

class FavoriteAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setFixtures() {
        givens();
    }

    @Test
    void 즐겨찾기를_관리() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역_ID, 역삼역_ID);
        즐겨찾기_생성됨(createResponse);
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);
        즐겨찾기_목록_조회됨(findResponse);
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createResponse.header(LOCATION));
        즐겨찾기_삭제됨(deleteResponse);
    }

    @Test
    void 권한_없는_상태에서_즐겨찾기를_관리() {
        String wrongAccessToken = StringUtils.EMPTY;
        ExtractableResponse<Response> createFailResponse = 즐겨찾기_생성_요청(wrongAccessToken, 강남역_ID, 역삼역_ID);
        권한이_존재하지_않음(createFailResponse);
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(wrongAccessToken);
        권한이_존재하지_않음(findResponse);
        ExtractableResponse<Response> createSuccessResponse = 즐겨찾기_생성_요청(accessToken, 강남역_ID, 역삼역_ID);
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(wrongAccessToken, createSuccessResponse.header(LOCATION));
        권한이_존재하지_않음(deleteResponse);
    }
}
