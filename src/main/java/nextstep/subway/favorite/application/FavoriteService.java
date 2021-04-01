package nextstep.subway.favorite.application;

import nextstep.subway.exception.NotExistsFavoriteException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(loginMember.getId(), source.getId(), target.getId()));
        return new FavoriteResponse(favorite.getId(), StationResponse.of(source), StationResponse.of(target));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        favorites.stream()
                .forEach(favorite -> {
                    Station source = stationService.findById(favorite.getSourceId());
                    Station target = stationService.findById(favorite.getTargetId());
                    if(source != null && target != null){
                        favoriteResponses.add(convertResponse(favorite));
                    }
                });
        return favoriteResponses;
    }

    private FavoriteResponse convertResponse(Favorite favorite) {
        Station source = stationService.findStationById(favorite.getSourceId());
        Station target = stationService.findStationById(favorite.getTargetId());
        return new FavoriteResponse(favorite.getId(), StationResponse.of(source), StationResponse.of(target));
    }

    public void deleteFavoriteOfMine(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByMemberIdAndId(loginMember.getId(), favoriteId).orElseThrow(NotExistsFavoriteException::new);
        favoriteRepository.delete(favorite);
    }

}
