package nextstep.utils.subway;

public class PathFixture {
	public static final Long 도곡역 = StationSteps.역_생성_요청("도곡역").jsonPath().getLong("id");
	public static final Long 학여울역 = StationSteps.역_생성_요청("학여울역").jsonPath().getLong("id");
	public static final Long 수서역 = StationSteps.역_생성_요청("수서역").jsonPath().getLong("id");
	public static final Long 개포동역 = StationSteps.역_생성_요청("개포동역").jsonPath().getLong("id");
	public static final Long 판교역 = StationSteps.역_생성_요청("판교역").jsonPath().getLong("id");
	public static final Long 양재역 = StationSteps.역_생성_요청("양재역").jsonPath().getLong("id");
	public static final Long 수원역 = StationSteps.역_생성_요청("수원역").jsonPath().getLong("id");
	public static final Long 분당선 = LineSteps.노선_생성_요청("분당선", "노랑", 도곡역, 학여울역, 6).jsonPath().getLong("id");
	public static final Long 삼호선 = LineSteps.노선_생성_요청("3호선", "주황", 도곡역, 개포동역, 4).jsonPath().getLong("id");
}
