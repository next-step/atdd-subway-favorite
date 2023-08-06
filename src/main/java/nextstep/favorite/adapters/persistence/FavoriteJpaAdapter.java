package nextstep.favorite.adapters.persistence;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.entity.Favorite;
import nextstep.favorite.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteJpaAdapter {

    private final FavoriteRepository favoriteRepository;

    @Transactional
    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> findByMemberId(Long memberId) {
        return favoriteRepository.findByMemberId(memberId);
    }

    public void deleteByIdAndMemberId(Long id, Long memberId) {
        favoriteRepository.deleteByIdAndMemberId(id, memberId);
    }

}
