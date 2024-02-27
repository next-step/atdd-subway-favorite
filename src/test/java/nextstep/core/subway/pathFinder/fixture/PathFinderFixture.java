package nextstep.core.subway.pathFinder.fixture;

import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;

public class PathFinderFixture {

    public static PathFinderRequest 지하철_경로(Long 출발역, Long 도착역) {
        return new PathFinderRequest(출발역, 도착역);
    }
}
