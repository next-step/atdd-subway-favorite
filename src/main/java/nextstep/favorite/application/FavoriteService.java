package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.FavoriteSaveException;
import nextstep.member.domain.LoginMember;
import nextstep.path.application.PathService;
import nextstep.path.application.dto.PathSearchRequest;
import nextstep.station.application.StationProvider;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationProvider stationProvider;
    private final PathService pathService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final StationProvider stationProvider, final PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.stationProvider = stationProvider;
        this.pathService = pathService;
    }

    @Transactional
    public FavoriteResponse createFavorite(final LoginMember loginMember, final FavoriteRequest request) {
        validateFavoriteCreation(loginMember, request);

        final Station sourceStation = stationProvider.findById(request.getSource());
        final Station targetStation = stationProvider.findById(request.getTarget());

        final Favorite favorite = new Favorite(loginMember.getId(), sourceStation, targetStation);
        final Favorite saved = favoriteRepository.save(favorite);

        return FavoriteResponse.from(saved);
    }

    private void validateFavoriteCreation(final LoginMember loginMember, final FavoriteRequest request) {
        request.validate();
        if(pathService.isInvalidPath(new PathSearchRequest(request.getSource(), request.getTarget()))) {
            throw new FavoriteSaveException("존재하지 않는 경로는 즐겨찾기에 추가할 수 없습니다.");
        }

        if(favoriteRepository.existsByStations(loginMember.getId(), request.getSource(), request.getTarget())) {
            throw new FavoriteSaveException("이미 등록된 즐겨찾기 경로입니다.");
        }
    }

    public List<FavoriteResponse> findFavorites(final LoginMember loginMember) {
        return favoriteRepository.findAllWithStationsByMember(loginMember.getId())
                .stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
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
