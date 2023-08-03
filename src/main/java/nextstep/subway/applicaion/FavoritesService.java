package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoritesRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final StationService stationService;
    private final PathService pathService;

    public FavoritesService(FavoritesRepository favoritesRepository,
        StationService stationService, PathService pathService) {

        this.favoritesRepository = favoritesRepository;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    @Transactional
    public Long create(FavoriteRequest request, Long memberId) {


        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        pathService.findPath(source.getId(), target.getId());

        return favoritesRepository.save(Favorite.of(source, target, memberId)).getId();
    }

    public List<FavoritesResponse> findAllByUser(Long memberId) {

        return favoritesRepository.findAllByMemberId(memberId)
            .orElse(List.of())
            .stream()
            .map(FavoritesResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(long favoritesId, Long memberId) {

        Favorite favorite = favoritesRepository.findById(favoritesId)
            .orElseThrow(IllegalArgumentException::new);

        favorite.validDelete(memberId);

        favoritesRepository.delete(favorite);
    }
}
