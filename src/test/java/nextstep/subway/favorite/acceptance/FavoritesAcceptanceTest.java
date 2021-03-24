package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.acceptance.LineSteps;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.favorite.acceptance.FavoritesSteps.*;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;

@DisplayName("즐겨찾기를 관리 인수 테스트")
public class FavoritesAcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 30;

    private StationResponse 강남역;
    private StationResponse 삼성역;
    private StationResponse 잠실역;
    private StationResponse 강남구청역;

    private LineResponse 이호선;

    private TokenResponse memberToken;

    @BeforeEach
    public void setUp() {
        강남역 = 지하철역_생성_요청("강남역");
        삼성역 = 지하철역_생성_요청("삼성역");
        잠실역 = 지하철역_생성_요청("잠실역");
        강남구청역 = 지하철역_생성_요청("강남구청역");

        LineRequest request2line = new LineRequest("이호선", "green", 강남구청역.getId(), 잠실역.getId(),40);

        이호선 = 노선_생성_요청(request2line);


        지하철노선에_지하철역_등록_요청(이호선, 잠실역, 삼성역, 5);
        지하철노선에_지하철역_등록_요청(이호선, 삼성역, 강남역, 5);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        memberToken = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMyFavorite() {

        //when
        ExtractableResponse<Response> favoriteCreateResponse = 즐겨찾기_생성을_요청(memberToken, 잠실역.getId(), 강남역.getId());

        //then
        즐겨찾기_생성됨(favoriteCreateResponse);

        //when
        ExtractableResponse<Response> readFavoriteResponse = 즐겨찾기_목록_조회를_요청(memberToken);

        //then
        즐겨찾기_목록_조회됨(readFavoriteResponse);
        FavoritesSteps.즐겨찾기_목록_내용_일치함(readFavoriteResponse, 잠실역, 강남역);

        //when
        ExtractableResponse<Response> readFavoriteResponse2 = 즐겨찾기_목록_조회를_요청(new TokenResponse(""));

        //then
        FavoritesSteps.즐겨찾기_목록_조회_실패됨(readFavoriteResponse2);

        //when
        ExtractableResponse<Response> deleteFavoriteResponse = 즐겨찾기_삭제_요청(memberToken, favoriteCreateResponse);

        //then
        즐겨찾기_삭제됨(deleteFavoriteResponse);
    }

    private LineResponse 지하철노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return LineSectionAcceptanceTest.노선에_지하철역_등록_요청(line, upStation.getId(), downStation.getId(), distance).body().as(LineResponse.class);
    }

    private StationResponse 지하철역_생성_요청(String name) {
        return StationAcceptanceTest.지하철역_생성_요청(name).body().as(StationResponse.class);
    }

    private LineResponse 노선_생성_요청(LineRequest lineRequest) {
        return LineSteps.노선_생성_요청(lineRequest).body().as(LineResponse.class);
    }

}
