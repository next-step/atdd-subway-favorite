package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Station upStation = stationService.findById(request.getSource());
        Station downStation = stationService.findById(request.getTarget());

        Favorite createFavorite = favoriteRepository.save(new Favorite(memberId, upStation, downStation));

        return FavoriteResponse.of(createFavorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void removeFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
