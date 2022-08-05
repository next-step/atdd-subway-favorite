package nextstep.subway.domain;

import nextstep.auth.secured.RoleAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteValidationService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteValidationService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public void validateDuplicate(Long memberId, Long source, Long target) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);

        favorites.stream()
                .filter(it -> it.match(source, target))
                .findAny()
                .ifPresent(it -> {
                    throw new IllegalArgumentException();
                });
    }

    public void validateOwner(Favorite favorite, Long memberId) {
        if (!favorite.belongsTo(memberId)) {
            throw new RoleAuthenticationException();
        }
    }
}
