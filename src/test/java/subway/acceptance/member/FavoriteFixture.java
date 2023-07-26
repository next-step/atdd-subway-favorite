package subway.acceptance.member;

import subway.acceptance.station.StationFixture;

import java.util.HashMap;
import java.util.Map;

public class FavoriteFixture {
    public static Map<String, String> 즐겨찾기_등록_요청_만들기(long source, long target) {
        Map<String, String> request = new HashMap<>();
        request.put("source", String.valueOf(source));
        request.put("target", String.valueOf(target));
        return request;
    }

    public static String 강남역_남부터미널역_즐겨찾기_등록(String accessToken) {
        Long 강남역_ID = StationFixture.getStationId("강남역");
        Long 남부터미널역_ID = StationFixture.getStationId("남부터미널역");
        Map<String, String> request = new HashMap<>();
        request.put("source", String.valueOf(강남역_ID));
        request.put("target", String.valueOf(남부터미널역_ID));
        var response = FavoriteSteps.즐겨찾기_생성_API(accessToken, request);
        return response.header("Location");
    }
}
