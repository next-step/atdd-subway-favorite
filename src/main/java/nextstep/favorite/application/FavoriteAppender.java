package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoriteAppender {
  private final FavoriteRepository favoriteRepository;

  public Favorite append(Favorite favorite) {
    return favoriteRepository.save(favorite);
  }
}
