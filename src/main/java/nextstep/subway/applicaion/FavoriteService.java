package nextstep.subway.applicaion;

import nextstep.member.application.dto.LoginUser;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    @Transactional
    public FavoriteResponse create(FavoriteRequest request, Long userId) {
        if (!pathService.pathValidate(request.getSource(), request.getTarget())) {
            throw new IllegalArgumentException("올바르지 않은 경로입니다.");
        }

        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Favorite favorite = new Favorite(userId, source, target);
        favorite = favoriteRepository.save(favorite);

        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> showAll(Long userId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(userId);

        return favorites.stream()
                .map(ele -> FavoriteResponse.from(ele))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }
}
