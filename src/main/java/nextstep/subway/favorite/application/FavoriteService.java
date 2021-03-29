package nextstep.subway.favorite.application;

import nextstep.subway.common.exception.ApplicationException;
import nextstep.subway.common.exception.ApplicationType;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private StationService stationService;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(FavoriteRequest favoriteRequest, Long memberId) {
        validateStationId(favoriteRequest);

        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());

        Favorite favorite = favoriteRepository.save(new Favorite(memberId, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findByMemberId(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId).stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public void removeFavorite(Long id, Long memberId) {
        favoriteRepository.deleteByIdAndMemberId(id, memberId);
    }

    public void validateStationId(FavoriteRequest request) {
        if (request.getSource() == request.getTarget()) {
            throw new ApplicationException(ApplicationType.CANNOT_SAME_WITH_SOURCE_AND_TARGET_ID);
        }
    }
}
