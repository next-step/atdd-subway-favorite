package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSourceId());
        Station target = stationService.findById(favoriteRequest.getTargetId());
        Favorite favorite = new Favorite(memberId, source, target);
        favoriteRepository.save(favorite);
        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorite(Long memberId) {
        List<Favorite> favoriteList = favoriteRepository.findAllByMemberId(memberId);
        return favoriteList.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

}
