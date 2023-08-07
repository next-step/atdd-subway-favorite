package nextstep.subway.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.ShortestPathFinder;
import nextstep.subway.domain.entity.*;
import nextstep.subway.dto.FavoriteRequest;
import nextstep.subway.dto.FavoriteResponse;
import nextstep.subway.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {

    private final LineRepository lineRepository;
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    @Transactional
    public Long createFavorite(FavoriteRequest request, Long memberId) {
        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        ShortestPathFinder.checkReachable(lineRepository.findAll(), sourceStation, targetStation);

        Favorite favorite = new Favorite(memberId, sourceStation, targetStation);
        favoriteRepository.save(favorite);

        return favorite.getId();
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {

        List<Favorite> favoriteList = favoriteRepository.findAllByMemberId(memberId);
        return favoriteList.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(long id, Long memberId) {
        favoriteRepository.deleteFavoriteByIdAndMemberId(id, memberId);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("section.not.found"));
    }
}
