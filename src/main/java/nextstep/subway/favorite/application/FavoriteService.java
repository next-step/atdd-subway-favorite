package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse saveFavorites(LoginMember loginMember, FavoriteRequest request) {
        Station source = stationRepository.findById(request.getSource()).orElseThrow(RuntimeException::new);
        Station target = stationRepository.findById(request.getTarget()).orElseThrow(RuntimeException::new);
        Favorite favorite = favoriteRepository.save(new Favorite(loginMember.getId(), source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());
        return toFavoriteResponses(favorites);
    }

    private List<FavoriteResponse> toFavoriteResponses(List<Favorite> favorites) {
        return favorites.stream()
                .map(f -> FavoriteResponse.of(f))
                .collect(Collectors.toList());
    }

    public void deleteFavoritesById(Long id) {
        favoriteRepository.deleteById(id);
    }
}
