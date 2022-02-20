package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

public class Favorites {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "favorite_id")
	private List<Favorite> favorites = new ArrayList<>();

	public void addFavorite(Favorite favorite) {
		favorites.add(favorite);
	}

	public List<Favorite> getFavorites() {
		return favorites;
	}
}
