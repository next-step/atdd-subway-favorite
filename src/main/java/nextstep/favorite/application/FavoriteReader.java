package nextstep.favorite.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.FavoriteNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoriteReader {
  private final FavoriteRepository favoriteRepository;

  public List<Favorite> readAllByMemberId(Long memberId) {
    return favoriteRepository.findAllByMemberId(memberId);
  }

  public Favorite readById(Long id) {
    return favoriteRepository.findById(id).orElseThrow(() -> new FavoriteNotFoundException(id));
  }
}
