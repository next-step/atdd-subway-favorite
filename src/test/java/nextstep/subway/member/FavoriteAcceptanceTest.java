package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.member.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationSteps.*;
import static nextstep.subway.member.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 역삼역;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private TokenResponse tokenResponse;
    private FavoriteRequest favoriteRequest;

    @BeforeEach
    void 초기세팅(){
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);

        Map<String, String> lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "2호선");
        lineCreateParams.put("color", "bg-red-600");
        lineCreateParams.put("upStationId", 강남역.getId() + "");
        lineCreateParams.put("downStationId", 교대역.getId() + "");
        lineCreateParams.put("distance", 10 + "");

        LineResponse 이호선 = 지하철_노선_생성_요청(lineCreateParams).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 역삼역, 6);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        favoriteRequest = new FavoriteRequest(강남역.getId(), 역삼역.getId());
    }

    @DisplayName("즐겨찾기 경로 추가")
    @Test
    void 즐겨찾기_경로_추가(){
        //when
        ExtractableResponse<Response> response = 즐겨찾기_만들기(tokenResponse, favoriteRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    @DisplayName("즐겨찾기 경로 확인")
    @Test
    void 즐겨찾기_경로_확인(){
        //given
        즐겨찾기_만들기(tokenResponse, favoriteRequest);

        //when
        ExtractableResponse<Response> response = 즐겨찾기_확인(tokenResponse);

        //then
        assertThat(response.jsonPath().getList(".", FavoriteResponse.class)).hasSize(1);

    }

    @DisplayName("즐겨찾기 경로 삭제")
    @Test
    void 즐겨찾기_경로_삭제(){
        //given
        ExtractableResponse<Response> createResponse = 즐겨찾기_만들기(tokenResponse, favoriteRequest);

        //when
        ExtractableResponse<Response> response = 즐겨찾기_삭제(tokenResponse, createResponse);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("즐겨찾기 통합 테스트")
    @Test
    void 즐겨찾기_통합_테스트(){
        //when
        ExtractableResponse<Response> createResponse = 즐겨찾기_만들기(tokenResponse, favoriteRequest);
        //then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> searchResponse = 즐겨찾기_확인(tokenResponse);
        //then
        assertThat(searchResponse.jsonPath().getList(".", FavoriteResponse.class)).hasSize(1);
        assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제(tokenResponse, createResponse);
        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}
