package nextstep.favorite.application;

import nextstep.exceptions.ErrorMessage;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exceptions.FavoriteNotFoundException;
import nextstep.favorite.payload.FavoriteRequest;
import nextstep.member.AuthorizationException;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FavoriteCommandService {
    private final FavoriteRepository favoriteRepository;

    private final StationRepository stationRepository;

    public FavoriteCommandService(final FavoriteRepository favoriteRepository, final StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public Long createFavorite(final Long memberId, final FavoriteRequest request) {
        Favorite favorite = new Favorite(memberId, request.getSource(), request.getTarget());
        Favorite saved = favoriteRepository.save(favorite);
        return saved.getId();
    }


    public void deleteFavorite(final Long memberId, final Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new FavoriteNotFoundException(ErrorMessage.FAVORITE_NOT_FOUND));

        if(!favorite.hasAuthority(memberId)) {
            throw new AuthorizationException();
        }
        favoriteRepository.delete(favorite);
    }
}
