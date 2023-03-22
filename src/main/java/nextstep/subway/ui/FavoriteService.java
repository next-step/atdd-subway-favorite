package nextstep.subway.ui;

import javax.lang.model.type.ErrorType;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.argumentResolver.ErrorCode;
import nextstep.argumentResolver.InvalidValueException;
import nextstep.subway.application.PathService;
import nextstep.subway.application.dto.StationResponse;
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

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        if (!pathService.isConnected(sourceStation, targetStation)) {
            throw new IllegalArgumentException("서로 연결되지 역입니다.");
        }

        Favorite favorite = createFavorite(memberId, sourceStation, targetStation);
        return FavoriteResponse.of(favorite, sourceStation, targetStation);
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
