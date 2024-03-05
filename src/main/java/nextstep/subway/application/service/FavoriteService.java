package nextstep.subway.application.service;

import nextstep.exception.AuthenticationException;
import nextstep.subway.application.dto.FavoriteRequest;
import nextstep.subway.application.dto.FavoriteResponse;
import nextstep.subway.domain.entity.Favorite;
import nextstep.subway.domain.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest request) {
        validFavoriteRequest(request);
        Favorite favorite = new Favorite(memberId, request.getSource(), request.getTarget());
        return createFavoriteResponse(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> findFavoritesByMemberId(Long memberId) {
        return favoriteRepository.findByMemberId(memberId).stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id, Long memberId) {
        Favorite favorite = favoriteRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("즐겨찾기가 존재하지 않습니다."));

        if(favorite.notMemberEquals(memberId)) {
            throw new AuthenticationException("삭제할 권한이 없습니다.");
        }

        favoriteRepository.deleteById(id);
    }

    private void validFavoriteRequest(FavoriteRequest request) {
        stationService.findStationById(request.getSource());
        stationService.findStationById(request.getTarget());

        pathService.getPath(request.getSource(), request.getTarget());
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId()
                , stationService.findStationById(favorite.getSource())
                , stationService.findStationById(favorite.getTarget())
        );
    }
}
