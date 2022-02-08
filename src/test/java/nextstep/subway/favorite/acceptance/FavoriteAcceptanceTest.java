package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.commons.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionTestRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.favorite.acceptance.FavoriteUtils.*;
import static nextstep.subway.line.acceptance.LineUtils.getLineId;
import static nextstep.subway.line.acceptance.LineUtils.지하철노선_생성_요청;
import static nextstep.subway.line.acceptance.SectionUtils.지하철노선_구간생성_요청;
import static nextstep.subway.member.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.station.acceptance.StationUtils.getStationId;
import static nextstep.subway.station.acceptance.StationUtils.지하철역_생성요청;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    long 교대역;
    long 강남역;
    long 역삼역;
    long 선릉역;
    long 이호선;
    String accessToken;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        교대역 = getStationId(지하철역_생성요청("교대역"));
        강남역 = getStationId(지하철역_생성요청("강남역"));
        역삼역 = getStationId(지하철역_생성요청("역삼역"));
        선릉역 = getStationId(지하철역_생성요청("선릉역"));

        LineRequest 이호선_요청 = LineRequest.builder()
                .name("2호선")
                .color("green")
                .upStationId(교대역)
                .downStationId(강남역)
                .distance(5)
                .build();
        이호선 = getLineId(지하철노선_생성_요청(이호선_요청));

        SectionTestRequest 이호선_구간생성_요청1 = SectionTestRequest.builder()
                .lineId(이호선)
                .upStationId(강남역)
                .downStationId(역삼역)
                .distance(3)
                .build();
        지하철노선_구간생성_요청(이호선_구간생성_요청1);

        SectionTestRequest 이호선_구간생성_요청2 = SectionTestRequest.builder()
                .lineId(이호선)
                .upStationId(역삼역)
                .downStationId(선릉역)
                .distance(4)
                .build();
        지하철노선_구간생성_요청(이호선_구간생성_요청2);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("즐겨찾기를 관리")
    void manageFavorites() {
        // when
        ExtractableResponse<Response> 생성_응답 = 즐겨찾기_생성_요청(accessToken, 교대역, 역삼역);

        // then
        즐겨찾기_생성_성공(생성_응답);

        // given
        즐겨찾기_생성_요청(accessToken, 강남역, 선릉역);

        // when
        ExtractableResponse<Response> 조회_응답 = 즐겨찾기_조회_요청(accessToken);

        // then
        즐겨찾기_조회_성공(조회_응답);
    }

}
