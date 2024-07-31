package nextstep.subway.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.favorite.Favorite;
import nextstep.subway.domain.exception.SubwayDomainException;
import nextstep.subway.domain.exception.SubwayDomainExceptionType;
import nextstep.subway.domain.query.PathReader;
import nextstep.subway.domain.repository.FavoriteRepository;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteCommander {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final PathReader pathReader;

    public Long createFavorite(FavoriteCommand.CreateFavorite command) {
        verifyStationExist(command.getSource(), command.getTarget());
        verifyPathExist(command.getSource(), command.getTarget());
        Favorite favorite = Favorite.create(command);
        favoriteRepository.save(favorite);
        return favorite.getId();
    }

    public void deleteFavorite(FavoriteCommand.DeleteFavorite command) {
        Favorite favorite = favoriteRepository.findByIdOrThrow(command.getFavoriteId());

        if (!favorite.getMemberId().equals(command.getMemberId())) {
            throw new SubwayDomainException(SubwayDomainExceptionType.UNAUTHORIZED_FAVORITE);
        }

        favoriteRepository.deleteById(command.getFavoriteId());
    }

    private void verifyStationExist(Long upStationId, Long downStationId) {
        this.stationRepository.findByIdOrThrow(upStationId);
        this.stationRepository.findByIdOrThrow(downStationId);
    }

    private void verifyPathExist(Long source, Long target) {
        pathReader.findShortestPath(source, target);
    }
}
