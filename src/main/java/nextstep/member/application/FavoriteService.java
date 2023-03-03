package nextstep.member.application;

import nextstep.common.exception.EntityNotFoundException;
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
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSourceId());
        Station target = stationService.findById(favoriteRequest.getTargetId());

        return FavoriteResponse.of(favoriteRepository.save(new Favorite(memberId, source, target)));
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Favorite"));
        favorite.validateMember(memberId);

        favoriteRepository.deleteById(id);
    }
}
