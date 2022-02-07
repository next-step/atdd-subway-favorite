package nextstep.subway.favorite.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.repository.FavoriteRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationNotFoundException;
import nextstep.subway.station.repository.StationRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public Long create(long memberId, FavoriteRequest request) {
        Station source = findStationById(request.getSource());
        Station target = findStationById(request.getTarget());

        Favorite favorite = favoriteRepository.save(Favorite.of(memberId, source, target));
        return favorite.getId();
    }

    private Station findStationById(long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }
}
