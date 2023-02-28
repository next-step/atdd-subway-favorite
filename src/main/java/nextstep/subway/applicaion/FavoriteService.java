package nextstep.subway.applicaion;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.FavoriteRepository;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;

	public Long createFavorite(String email, FavoriteRequest request) {

		return 1L;
	}

	public List<FavoriteResponse> showFavorite(String email) {
		List<FavoriteResponse> favoriteResponses = new ArrayList<>();

		return favoriteResponses;
	}

	public void deleteFavorite(String email, Long id) {

	}
}
