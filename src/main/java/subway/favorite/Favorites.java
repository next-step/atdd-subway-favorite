package subway.favorite;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {
	@OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private final List<Favorite> favoriteList = new ArrayList<>();

	public List<Favorite> getFavoriteList() {
		return favoriteList;
	}

	public void addFavorite(Favorite favorite) {
		checkEqualSourceStationAndTargetStation(favorite);
		getFavoriteList().add(favorite);
	}

	private void checkEqualSourceStationAndTargetStation(Favorite favorite) {
		if (favorite.getSourceStation().equals(favorite.getTargetStation())) {
			throw new IllegalArgumentException("출발역과 도착역은 동일할 수 없습니다.");
		}
	}
}
