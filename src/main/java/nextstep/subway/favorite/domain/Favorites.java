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

  public void add(Favorite favorite) {
    favorites.add(favorite);
  }

  public List<Favorite> getFavorites(){
    return favorites;
  }
}
