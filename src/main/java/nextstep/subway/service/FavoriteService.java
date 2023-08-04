package nextstep.subway.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.Favorite;
import nextstep.subway.domain.entity.FavoriteRepository;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.entity.StationRepository;
import nextstep.subway.dto.FavoriteRequest;
import nextstep.subway.dto.FavoriteResponse;
import nextstep.subway.exception.StationNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MessageSource messageSource;

    @Transactional
    public Long createFavorite(FavoriteRequest request) {
        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        Favorite favorite = new Favorite(sourceStation, targetStation);
        favoriteRepository.save(favorite);

        return favorite.getId();
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(messageSource.getMessage("section.not.found", null, Locale.KOREA)));
    }

    public List<FavoriteResponse> getFavorites() {
        List<Favorite> favoriteList = favoriteRepository.findAll();
        return favoriteList.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(long id) {
        favoriteRepository.deleteById(id);
    }
}
