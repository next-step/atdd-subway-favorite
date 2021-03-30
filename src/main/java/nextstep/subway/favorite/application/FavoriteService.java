package nextstep.subway.favorite.application;

import com.google.common.collect.Lists;
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

import java.util.List;
import java.util.stream.Collectors;

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
        Station source = stationService.findById(favoriteRequest.getSourceId());
        Station target = stationService.findById(favoriteRequest.getTargetId());
        Favorite favorite = favoriteRepository.save(new Favorite(loginMember.getId(), source.getId(), target.getId()));
        return new FavoriteResponse(favorite.getId(), StationResponse.of(source), StationResponse.of(target));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        checkFavoritesOfMine(favorites, loginMember.getId());
        return favorites.stream()
                .map(favorite -> convertResponse(favorite))
                .collect(Collectors.toList());
    }

    private FavoriteResponse convertResponse(Favorite favorite) {
        Station source = stationService.findById(favorite.getSourceId());
        Station target = stationService.findById(favorite.getTargetId());
        return new FavoriteResponse(favorite.getId(), StationResponse.of(source), StationResponse.of(target));
    }

    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(RuntimeException::new);
        checkFavoriteOfMine(favorite.getMemberId(), loginMember.getId());
        favoriteRepository.delete(favorite);
    }

    public void checkFavoritesOfMine(List<Favorite> favorites, Long myId) {
        favorites.stream()
                .forEach(favorite -> {
                    checkFavoriteOfMine(favorite.getId(), myId);
                });
    }

    public void checkFavoriteOfMine(Long memberId, Long myId) {
        if(memberId != myId){
            throw new IllegalArgumentException();
        }
    }
}
