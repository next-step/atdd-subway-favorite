package nextstep.core.pathFinder.application.converter;


import nextstep.core.favorite.application.dto.FavoriteRequest;
import nextstep.core.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.pathFinder.application.dto.PathFinderResponse;
import nextstep.core.pathFinder.domain.dto.PathFinderResult;

public class PathFinderConverter {

    public static PathFinderResponse convertToPathFinderResponse(PathFinderResult pathFinderResult) {
        return new PathFinderResponse(pathFinderResult.getStations(), pathFinderResult.getDistance());
    }

    public static PathFinderRequest convertToPathFinderRequest(FavoriteRequest request) {
        return new PathFinderRequest(request.getSource(), request.getTarget());
    }
}
