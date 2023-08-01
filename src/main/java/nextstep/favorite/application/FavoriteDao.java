package nextstep.favorite.application;

import java.util.List;
import nextstep.favorite.domain.FavoriteResponse;
import nextstep.favorite.domain.FavoriteResponseRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FavoriteDao {

    private final FavoriteResponseRepository favoriteResponseRepository;

    public FavoriteDao(FavoriteResponseRepository favoriteResponseRepository) {
        this.favoriteResponseRepository = favoriteResponseRepository;
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getAll(String email) {
        return favoriteResponseRepository.findAllByEmail(email);
    }
}
