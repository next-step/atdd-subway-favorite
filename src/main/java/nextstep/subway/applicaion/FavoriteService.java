package nextstep.subway.applicaion;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.applicaion.dto.FavoriteResponse;

@Service
public class FavoriteService {

	public List<FavoriteResponse> registerFavorite(long source, long target) {
		return Arrays.asList(new FavoriteResponse());
	}

	public void deleteFavorite(long favoriteId) {

	}

	public List<FavoriteResponse> getFavorites() {
		return Arrays.asList(new FavoriteResponse());
	}
}
