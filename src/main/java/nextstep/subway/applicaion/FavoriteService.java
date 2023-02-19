package nextstep.subway.applicaion;

import nextstep.auth.domain.LoginUserInfo;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.config.exception.NotFoundException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.config.message.SubwayError.*;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(final LoginUserInfo loginUser, final FavoriteRequest request) {
        final Station source = stationService.findById(request.getSource());
        final Station target = stationService.findById(request.getTarget());
        final Favorite saveFavorite = favoriteRepository.save(Favorite.of(loginUser.getMemberId(), source.getId(), target.getId()));
        return FavoriteResponse.of(saveFavorite, StationResponse.from(source), StationResponse.from(target));
    }

    public FavoriteResponse showFavorite(final LoginUserInfo loginUser, final Long id) {
        final Favorite favorite = favoriteRepository.findByIdAndMemberId(id, loginUser.getMemberId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        final Station source = stationService.findById(favorite.getSourceStationId());
        final Station target = stationService.findById(favorite.getTargetStationId());
        return FavoriteResponse.of(favorite, StationResponse.from(source), StationResponse.from(target));
    }

    @Transactional
    public void removeFavorite(final LoginUserInfo loginUser, final Long id) {
        favoriteRepository.deleteByIdAndMemberId(id, loginUser.getMemberId());
    }
}
