package nextstep.subway.favorite.domain;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {

  @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Favorite> favorites = new ArrayList<>();

  public Favorite add(Favorite favorite) {
    favorites.add(favorite);
    return favorite;
  }

  public List<Favorite> getAllFavorite(){
    return favorites;
  }

  public void remove(Favorite favorite){
    favorites.remove(favorite);
  }
}
