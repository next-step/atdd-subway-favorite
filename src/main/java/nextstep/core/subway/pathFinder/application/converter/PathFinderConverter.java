package nextstep.core.subway.pathFinder.application.converter;


import nextstep.core.subway.favorite.application.dto.FavoriteRequest;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.pathFinder.application.dto.PathFinderResponse;
import nextstep.core.subway.pathFinder.domain.dto.PathFinderResult;

public class PathFinderConverter {

    public static PathFinderResponse convertToResponse(PathFinderResult pathFinderResult) {
        return new PathFinderResponse(pathFinderResult.getStations(), pathFinderResult.getDistance());
    }

    public static PathFinderRequest convertToRequest(FavoriteRequest request) {
        return new PathFinderRequest(request.getSource(), request.getTarget());
    }
}
