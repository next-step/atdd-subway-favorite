package nextstep.member.application;

import nextstep.common.exception.EntityNotFoundException;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSourceId());
        Station target = stationService.findById(favoriteRequest.getTargetId());

        return FavoriteResponse.of(favoriteRepository.save(new Favorite(source, target)));
    }

    public List<FavoriteResponse> findFavorites() {
        return favoriteRepository.findAll().stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    private FavoriteResponse findFavoriteById(Long id) {
        return FavoriteResponse.of(favoriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Favorite")));
    }

    @Transactional
    public void deleteFavorite(Long id) {
        FavoriteResponse favoriteResponse = findFavoriteById(id);
        favoriteRepository.deleteById(favoriteResponse.getId());
    }
}
