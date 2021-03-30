package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name="favorite_id")
  private List<Favorite> favorites = new ArrayList<>();

  public Favorite add(Favorite favorite) {
    favorites.add(favorite);
    return favorite;
  }

  public List<Favorite> getAllFavorite(){
    return favorites;
  }

  public void remove(long favoriteId){
    Favorite targetFavorite = favorites.stream()
        .filter(favorite -> favorite.getId().equals(favoriteId))
        .findFirst()
        .orElseThrow(RuntimeException::new);
    favorites.remove(targetFavorite);
  }
}
