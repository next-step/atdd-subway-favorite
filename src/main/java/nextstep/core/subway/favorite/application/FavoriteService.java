package nextstep.core.subway.favorite.application;

import nextstep.core.member.domain.Member;
import nextstep.core.member.exception.NonMatchingMemberException;
import nextstep.core.subway.favorite.application.dto.FavoriteRequest;
import nextstep.core.subway.favorite.application.dto.FavoriteResponse;
import nextstep.core.subway.favorite.domain.Favorite;
import nextstep.core.subway.favorite.domain.FavoriteRepository;
import nextstep.core.subway.pathFinder.application.PathFinderService;
import nextstep.core.subway.station.application.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static nextstep.core.subway.pathFinder.application.converter.PathFinderConverter.convertToPathFinderRequest;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    private final PathFinderService pathFinderService;

    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, PathFinderService pathFinderService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.pathFinderService = pathFinderService;
        this.stationService = stationService;
    }

    @Transactional
    public Favorite createFavorite(FavoriteRequest request, Long memberId) {
        if (!pathFinderService.isValidPath(convertToPathFinderRequest(request))) {
            throw new IllegalArgumentException("출발역과 도착역을 잇는 경로가 없습니다.");
        }
        return favoriteRepository.save(createFavoriteEntity(request, memberId));
    }

    public List<FavoriteResponse> findFavorites(Member member) {
        return FavoriteConverter.convertToFavoriteResponses(favoriteRepository.findByMember(member.getId()));
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, Member member) {
        if (!member.isSameId(findFavoriteById(favoriteId).getMemberId())) {
            throw new NonMatchingMemberException("다른 회원의 즐겨찾기를 삭제할 수 없습니다.");
        }
        favoriteRepository.deleteById(favoriteId);
    }

    private Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("즐겨찾기 번호에 해당하는 즐겨찾기가 없습니다."));
    }

    private Favorite createFavoriteEntity(FavoriteRequest request, Long memberId) {
        return new Favorite(
                stationService.findStation(request.getSource()),
                stationService.findStation(request.getTarget()),
                memberId);
    }
}
