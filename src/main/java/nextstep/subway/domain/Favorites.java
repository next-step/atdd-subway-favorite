package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import nextstep.subway.exception.IllegalUserException;

public class Favorites {
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

	public void validationAddable(long memberId, long source, long target) {
		if (isExistsSameFavorite(source, target)) {
			throw new IllegalArgumentException();
		}
		checkContainOtherMembers(memberId);
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
			throw new IllegalUserException();
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
