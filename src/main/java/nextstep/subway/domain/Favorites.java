package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import nextstep.subway.exception.IllegalUserException;

@Embeddable
public class Favorites {
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	@JoinColumn(name = "member_id")
	private List<Favorite> values = new ArrayList<>();

	public Favorites(List<Favorite> values) {
		if (CollectionUtils.isEmpty(values)) {
			this.values = new ArrayList<>();
			return;
		}
		this.values = values;
	}

	public Favorites() {

	}

	public void addFavorite(long memberId, long source, long target) {
		if (isExistsSameFavorite(source, target)) {
			throw new IllegalArgumentException();
		}
		checkContainOtherMembers(memberId);

		values.add(new Favorite(memberId, source, target));
	}

	public void removeFavorite(long source, long target) {
		if (values.size() == 0) {
			throw new IllegalArgumentException();
		}
		if (!isExistsSameFavorite(source, target)) {
			throw new IllegalArgumentException();
		}
		this.values
			.removeIf(favorite -> favorite.isSameFavorite(source, target));
	}

	public List<Favorite> getValues() {
		return Collections.unmodifiableList(this.values);
	}

	private boolean isExistsSameFavorite(long source, long target) {
		return this.values
			.stream()
			.anyMatch(value -> value.isSameFavorite(source, target));
	}

	private void checkContainOtherMembers(long memberId) {
		if (this.values
			.stream()
			.anyMatch(value -> value.isNotSameMember(memberId))) {
			throw new IllegalArgumentException();
		}
	}

	public Favorite getFavoriteById(long favoriteId) {
		return this.values
			.stream()
			.filter(favorite -> favorite.isSameFavorite(favoriteId))
			.findFirst()
			.orElseThrow(IllegalUserException::new);
	}
}
