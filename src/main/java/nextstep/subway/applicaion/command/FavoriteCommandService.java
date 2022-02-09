package nextstep.subway.applicaion.command;

import nextstep.exception.StationNotFoundException;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.query.FavoriteQueryService;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class FavoriteCommandService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final FavoriteQueryService favoriteQueryService;

    public FavoriteCommandService(FavoriteRepository favoriteRepository,
                                  StationRepository stationRepository,
                                  FavoriteQueryService favoriteQueryService) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.favoriteQueryService = favoriteQueryService;
    }

    public void createFavorite(long memberId, FavoriteRequest request) {
        Station sourceStation = findStationById(request.getSource());
        Station targetStation = findStationById(request.getTarget());

        Favorite favorite = Favorite.of(memberId, sourceStation, targetStation);
        favoriteRepository.save(favorite);
    }

    public void deleteFavorite(long favoriteId) {
        Favorite favorite = favoriteQueryService.findFavorite(favoriteId);
        favoriteRepository.delete(favorite);
    }

    private Station findStationById(long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }

}
