package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

public class Favorites {
	@OneToMany
	@JoinColumn(name = "favorite_id")
	private List<Favorite> favorites = new ArrayList<>();

	public void addFavorite(Favorite favorite) {
		favorites.add(favorite);
	}
}
