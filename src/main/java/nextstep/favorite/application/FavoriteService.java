package nextstep.favorite.application;

import nextstep.common.exception.CanNotDeleteFavoriteException;
import nextstep.common.exception.ErrorCode;
import nextstep.common.exception.NotFoundException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final LineService lineService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, LineService lineService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional
    public Long saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());

        validateStationPath(sourceStation, targetStation);

        return favoriteRepository.save(new Favorite(memberId, sourceStation, targetStation)).getId();
    }

    private void validateStationPath(Station sourceStation, Station targetStation) {
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        subwayMap.findPath(sourceStation, targetStation);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, Long memberId) {
        Favorite favorite = findById(favoriteId);
        if (!favorite.isCreatedBy(memberId)) {
            throw new CanNotDeleteFavoriteException();
        }
        favoriteRepository.delete(favorite);
    }

    private Favorite findById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_FAVORITE));
    }
}
