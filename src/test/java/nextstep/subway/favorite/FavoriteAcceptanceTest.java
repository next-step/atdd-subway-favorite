package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationSteps.*;

public class FavoriteAcceptanceTest {

    StationResponse 교대역;
    StationResponse 강남역;
    StationResponse 역삼역;

    @BeforeEach
    void setUp(){
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

    }

    @DisplayName("즐겨찾기 경로 추가")
    @Test
    void 즐겨찾기_경로_추가(){
        //given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 역삼역.getId());
        //when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all().extract();

    }


}
