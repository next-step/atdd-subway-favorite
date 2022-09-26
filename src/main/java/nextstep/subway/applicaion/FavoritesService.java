package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoritesService {
    public FavoriteResponse createFavorite(final FavoriteCreateRequest request) {
        FavoriteResponse.StationDTO source = new FavoriteResponse.StationDTO(1L, "n1");
        FavoriteResponse.StationDTO target = new FavoriteResponse.StationDTO(2L, "n2");
        return new FavoriteResponse(1L, source, target);
    }
}
