package nextstep.core.pathFinder.application.converter;


import nextstep.core.pathFinder.application.dto.PathFinderResponse;
import nextstep.core.pathFinder.domain.dto.PathFinderResult;

public class PathFinderConverter {

    public static PathFinderResponse convertToPathResponse(PathFinderResult pathFinderResult) {
        return new PathFinderResponse(pathFinderResult.getStations(), pathFinderResult.getDistance());
    }
}
