package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.NotFoundStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest) {
        Station sourceStation  = stationRepository.findById(favoriteRequest.getSource())
                .orElseThrow(NotFoundStationException::new);
        Station targetStation  = stationRepository.findById(favoriteRequest.getTarget())
                .orElseThrow(NotFoundStationException::new);
        System.out.println("######## : " + LocalDateTime.now());
        Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, LocalDateTime.now()));
        System.out.println(favorite.toString());
        return FavoriteResponse.of(favorite);
    }

}
