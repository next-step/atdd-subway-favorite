package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class Favorites {
	private final List<Favorite> values;

	public Favorites(List<Favorite> values) {
		if (CollectionUtils.isEmpty(values)) {
			this.values = new ArrayList<>();
			return;
		}
		this.values = values;
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
		return values;
	}

	public boolean isExistsSameFavorite(long source, long target) {
		return this.values
			.stream()
			.anyMatch(value -> value.isSameFavorite(source, target));
	}

	public void checkContainOtherMembers(long memberId) {
		if (this.values
			.stream()
			.anyMatch(value -> value.isNotSameMember(memberId))) {
			throw new IllegalArgumentException();
		}
	}
}
