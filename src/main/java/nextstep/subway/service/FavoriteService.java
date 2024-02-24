package nextstep.subway.service;

import nextstep.subway.dto.FavoriteRequest;
import nextstep.subway.dto.FavoriteResponse;
import nextstep.subway.domain.entity.Favorite;
import nextstep.subway.dto.StationResponse;
import nextstep.subway.repository.FavoriteRepository;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    public FavoriteResponse saveFavorite(FavoriteRequest request) {
        validFavoriteRequest(request);

        Favorite favorite = new Favorite(request.getSource(), request.getTarget());
        return createFavoriteResponse(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> findFavorites() {
        return favoriteRepository.findAll().stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("즐겨찾기가 존재하지 않습니다."));

        favoriteRepository.deleteById(id);
    }

    private void validFavoriteRequest(FavoriteRequest request) {
        stationService.findStationById(request.getSource());
        stationService.findStationById(request.getTarget());

        pathService.getPath(request.getSource(), request.getTarget());
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId()
                , stationService.findStationById(favorite.getSource())
                , stationService.findStationById(favorite.getTarget())
        );
    }
}
