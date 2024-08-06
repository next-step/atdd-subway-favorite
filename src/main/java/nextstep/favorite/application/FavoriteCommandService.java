package nextstep.favorite.application;

import nextstep.exceptions.ErrorMessage;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exceptions.FavoriteAlreadyExistsException;
import nextstep.favorite.exceptions.FavoriteNotFoundException;
import nextstep.favorite.payload.FavoriteRequest;
import nextstep.member.AuthorizationException;
import nextstep.path.exceptions.PathNotFoundException;
import nextstep.path.repository.PathResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FavoriteCommandService {
    private final FavoriteRepository favoriteRepository;

    private final PathResolver pathResolver;


    public FavoriteCommandService(final FavoriteRepository favoriteRepository, final PathResolver pathResolver) {
        this.favoriteRepository = favoriteRepository;
        this.pathResolver = pathResolver;
    }

    public Long createFavorite(final Long memberId, final FavoriteRequest request) {
        Long source = request.getSource();
        Long target = request.getTarget();

        assertDuplicateFavorite(memberId, source, target);

        pathResolver.get(source, target)
                .orElseThrow(() -> new PathNotFoundException(ErrorMessage.PATH_NOT_FOUND));

        Favorite favorite = new Favorite(memberId, source, target);
        Favorite saved = favoriteRepository.save(favorite);
        return saved.getId();
    }

    private void assertDuplicateFavorite(final Long memberId, final Long source, final Long target) {
        favoriteRepository.findByMemberIdAndSourceStationIdAndTargetStationId(memberId, source, target)
                .ifPresent(it -> {
                    throw new FavoriteAlreadyExistsException(ErrorMessage.FAVORITE_ALREADY_EXISTS);
                });
    }

    public void deleteFavorite(final Long memberId, final Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new FavoriteNotFoundException(ErrorMessage.FAVORITE_NOT_FOUND));

        if (!favorite.isAuthority(memberId)) {
            throw new AuthorizationException();
        }
        favoriteRepository.delete(favorite);
    }
}
