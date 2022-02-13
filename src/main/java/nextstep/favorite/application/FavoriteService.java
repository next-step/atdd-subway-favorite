package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public void addFavorite(FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSourceId());
        Station target = stationService.findById(favoriteRequest.getTargetId());
        Favorite favorite = new Favorite(favoriteRequest.getMemberId(), source, target);
        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorite(FavoriteRequest favoriteRequest) {
        List<Favorite> favoriteList = favoriteRepository.findAllByMemberId(favoriteRequest.getMemberId());
        return favoriteList.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

}
