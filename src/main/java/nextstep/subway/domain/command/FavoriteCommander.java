package nextstep.subway.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.favorite.Favorite;
import nextstep.subway.domain.query.PathFinder;
import nextstep.subway.domain.query.PathReader;
import nextstep.subway.domain.repository.FavoriteRepository;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteCommander {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathReader pathReader;

    public Long createFavorite(FavoriteCommand.CreateFavorite command) {
        verifyStationExist(command.getSource(), command.getTarget());
        verifyPathExist(command.getSource(), command.getTarget());
        Favorite favorite = Favorite.create(command);
        favoriteRepository.save(favorite);
        return favorite.getId();
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private void verifyStationExist(Long upStationId, Long downStationId) {
        this.stationRepository.findByIdOrThrow(upStationId);
        this.stationRepository.findByIdOrThrow(downStationId);
    }

    private void verifyPathExist(Long source, Long target) {
        pathReader.findShortestPath(source, target);
    }
}
