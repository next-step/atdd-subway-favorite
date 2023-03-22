package nextstep.subway.ui;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.argumentResolver.ErrorCode;
import nextstep.exception.InvalidValueException;
import nextstep.subway.application.PathService;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.application.dto.FavoriteRequest;
import nextstep.subway.application.dto.FavoriteResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final PathService pathService;

    @Transactional(readOnly = true)
    public FavoriteResponse getFavorite(Long memberId, long favoriteId) {

        Favorite favorite = getOwnFavorite(memberId, favoriteId);
        return FavoriteResponse.of(favorite);
    }

    @Transactional
    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        if (!pathService.isConnected(sourceStation, targetStation)) {
            throw new IllegalArgumentException("서로 연결되지 역입니다.");
        }

        Favorite favorite = createFavorite(memberId, sourceStation, targetStation);
        return FavoriteResponse.of(favorite);
    }

    @Transactional
    public void deleteFavorite(Long memberId, long favoriteId) {
        Favorite favorite = getOwnFavorite(memberId, favoriteId);
        favoriteRepository.delete(favorite);
    }

    private Favorite getOwnFavorite(Long memberId, long favoriteId) {
        return favoriteRepository.findByIdAndMemberId(favoriteId, memberId)
            .orElseThrow(() -> new InvalidValueException("자신이 등록한 즐겨찾기가 아닙니다."));
    }

    private Favorite createFavorite(Long memberId, Station sourceStation, Station targetStation) {
        Favorite favorite = Favorite.of(memberId, sourceStation, targetStation);
        return favoriteRepository.save(favorite);
    }

    private Station getStation(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new InvalidValueException(ErrorCode.UNREGISTERED_STATION));
    }

}
