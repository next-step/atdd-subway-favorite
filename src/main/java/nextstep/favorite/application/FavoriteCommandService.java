package nextstep.favorite.application;

import nextstep.exceptions.ErrorMessage;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exceptions.FavoriteNotFoundException;
import nextstep.favorite.payload.FavoriteRequest;
import nextstep.member.AuthorizationException;
import nextstep.path.application.PathQueryService;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FavoriteCommandService {
    private final FavoriteRepository favoriteRepository;

    private final PathQueryService pathQueryService;

    public FavoriteCommandService(final FavoriteRepository favoriteRepository, final PathQueryService pathQueryService) {
        this.favoriteRepository = favoriteRepository;
        this.pathQueryService = pathQueryService;
    }

    public Long createFavorite(final Long memberId, final FavoriteRequest request) {
        Long source = request.getSource();
        Long target = request.getTarget();
        pathQueryService.findShortestPath(source, target);
        Favorite favorite = new Favorite(memberId, source, target);
        Favorite saved = favoriteRepository.save(favorite);
        return saved.getId();
    }

    public void deleteFavorite(final Long memberId, final Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new FavoriteNotFoundException(ErrorMessage.FAVORITE_NOT_FOUND));

        if (!favorite.hasAuthority(memberId)) {
            throw new AuthorizationException();
        }
        favoriteRepository.delete(favorite);
    }
}
