package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(final FavoriteRepository favoriteRepository, final StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse saveFavorite(final Long memberId, final FavoriteRequest request) {
        final Station source = stationRepository.findById(request.getSource()).orElseThrow(IllegalArgumentException::new);
        final Station target = stationRepository.findById(request.getTarget()).orElseThrow(IllegalArgumentException::new);

        return FavoriteResponse.of(favoriteRepository.save(new Favorite(memberId, source, target)));
    }

    public List<FavoriteResponse> findAllFavorites(final Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavoriteById(Long memberId, final Long id) {
        final Favorite favorite = favoriteRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        favorite.validateMember(memberId);

        favoriteRepository.deleteById(id);
    }
}
