package nextstep.common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class LineUtil {
    public static Long 교대역Id;
    public static Long 남부터미널역Id;
    public static Long 양재역Id;
    public static Long 강남역Id;
    public static Long 반포역Id;
    public static Long 학동역Id;

    public static int 교대역_남부터미널역_거리 = 3;
    public static int 남부터미널역_양재역_거리 = 4;
    public static int 교대역_강남역_거리 = 1;
    public static int 양재역_강남역_거리 = 2;
    public static int 반포역_학동역_거리 = 5;

    /**
     * 교대역    --- *2호선* (1) ---   강남역           반포역  --- *7호선* (5) ---  학동역
     * |                              |
     * *3호선*(3)                    *신분당선*(2)
     * |                              |
     * 남부터미널역  --- *3호선*(4) ---  양재역
     *
     */

    public static void setStation() {
        교대역Id = extractResponseId(StationRestAssuredCRUD.createStation("교대역"));
        남부터미널역Id = extractResponseId(StationRestAssuredCRUD.createStation("남부터미널역"));
        양재역Id = extractResponseId(StationRestAssuredCRUD.createStation("양재역"));
        강남역Id = extractResponseId(StationRestAssuredCRUD.createStation("강남역"));
        반포역Id = extractResponseId(StationRestAssuredCRUD.createStation("반포역"));
        학동역Id = extractResponseId(StationRestAssuredCRUD.createStation("학동역"));

        setOrangeLine();
        setGreenLine();
        setRedLine();
        setDarkGreenLine();
    }

    public static void setOrangeLine() {
        ExtractableResponse<Response> lineResponse = LineRestAssuredCRUD.createLine("3호선", "bg-orange-600");
        Long 삼호선Id = lineResponse.jsonPath().getLong("id");

        SectionRestAssuredCRUD.addSection(교대역Id, 남부터미널역Id, 교대역_남부터미널역_거리, 삼호선Id);
        SectionRestAssuredCRUD.addSection(남부터미널역Id, 양재역Id, 남부터미널역_양재역_거리, 삼호선Id);
    }

    static void setGreenLine() {
        ExtractableResponse<Response> lineResponse = LineRestAssuredCRUD.createLine("2호선", "bg-green-600");
        Long 이호선Id = lineResponse.jsonPath().getLong("id");

        SectionRestAssuredCRUD.addSection(교대역Id, 강남역Id, 교대역_강남역_거리, 이호선Id);
    }

    static void setRedLine() {
        ExtractableResponse<Response> lineResponse = LineRestAssuredCRUD.createLine("신분당선", "bg-red-600");
        Long 신분당선Id = lineResponse.jsonPath().getLong("id");

        SectionRestAssuredCRUD.addSection(강남역Id, 양재역Id, 양재역_강남역_거리, 신분당선Id);
    }

    static void setDarkGreenLine() {
        ExtractableResponse<Response> lineResponse = LineRestAssuredCRUD.createLine("7호선", "bg-darkgreen-600");
        Long 칠호선Id = lineResponse.jsonPath().getLong("id");

        SectionRestAssuredCRUD.addSection(반포역Id, 학동역Id, 반포역_학동역_거리, 칠호선Id);
    }

    static Long extractResponseId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }
}
