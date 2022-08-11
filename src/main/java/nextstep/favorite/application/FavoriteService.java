package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Long registerFavorite(FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        Favorite favorite = Favorite.register(source, target);
        return favoriteRepository.save(favorite).getId();
    }

    public List<FavoriteResponse> getFavorites() {
        return favoriteRepository.findAll().stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    public boolean deleteFavorite(Long id) {
        Favorite favorite = findById(id);
        favoriteRepository.delete(favorite);
        return true;
    }

    @Transactional(readOnly = true)
    public Favorite findById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

}
