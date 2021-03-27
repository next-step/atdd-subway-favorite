package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.auth.exception.NotFoundAuthenticationException;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private StationService stationService;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse save(long memberId, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());

        Favorite favorite = favoriteRepository.save(Favorite.of(memberId, sourceStation, targetStation));
        return FavoriteResponse.of(favorite);
    }


    public List<FavoriteResponse> getAll(long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);

        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void delete(long favoriteId, LoginMember loginMember) {
        if (loginMember == null) {
            throw new NotFoundAuthenticationException();
        }

        favoriteRepository.deleteById(favoriteId);
    }
}
