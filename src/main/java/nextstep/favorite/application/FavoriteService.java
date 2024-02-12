package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.station.application.StationProvider;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationProvider stationProvider;

    public FavoriteService(final FavoriteRepository favoriteRepository, final StationProvider stationProvider) {
        this.favoriteRepository = favoriteRepository;
        this.stationProvider = stationProvider;
    }

    public FavoriteResponse createFavorite(final LoginMember loginMember, final FavoriteRequest request) {
        final Station sourceStation = stationProvider.findById(request.getSource());
        final Station targetStation = stationProvider.findById(request.getTarget());
        final Favorite favorite = new Favorite(loginMember.getId(), sourceStation, targetStation);
        final Favorite saved = favoriteRepository.save(favorite);
        return FavoriteResponse.from(saved);
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        final List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     *
     * @param id
     */
    public void deleteFavorite(final Long id) {
        favoriteRepository.deleteById(id);
    }
}
