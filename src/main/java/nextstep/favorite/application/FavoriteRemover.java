package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.domain.FavoriteRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoriteRemover {
  private final FavoriteRepository favoriteRepository;

  public void removeById(Long id) {
    favoriteRepository.deleteById(id);
  }
}
