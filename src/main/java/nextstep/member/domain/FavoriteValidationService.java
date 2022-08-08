package nextstep.member.domain;

import nextstep.member.domain.exception.CantAddFavoriteException;
import nextstep.member.domain.exception.NotMyFavoriteException;
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

    public void validateAddedFavorite(Long memberId, Long source, Long target) {
        if (source.equals(target)) {
            throw new CantAddFavoriteException("즐겨찾기의 출발점과 종점이 같을 수 없습니다.");
        }

        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);

        favorites.stream()
                .filter(it -> it.match(source, target))
                .findAny()
                .ifPresent(it -> {
                    throw new CantAddFavoriteException("이미 존재하는 즐겨찾기입니다.");
                });
    }

    public void validateOwner(Favorite favorite, Long memberId) {
        if (!favorite.belongsTo(memberId)) {
            throw new NotMyFavoriteException("내 즐겨찾기가 아닙니다.");
        }
    }
}
