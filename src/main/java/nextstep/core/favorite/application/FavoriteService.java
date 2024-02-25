package nextstep.core.favorite.application;

import nextstep.core.favorite.application.dto.FavoriteRequest;
import nextstep.core.favorite.application.dto.FavoriteResponse;
import nextstep.core.favorite.domain.Favorite;
import nextstep.core.favorite.domain.FavoriteRepository;
import nextstep.core.member.domain.Member;
import nextstep.core.member.exception.NonMatchingMemberException;
import nextstep.core.pathFinder.application.PathFinderService;
import nextstep.core.station.application.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static nextstep.core.favorite.application.FavoriteConverter.convertToFavoriteResponses;
import static nextstep.core.pathFinder.application.converter.PathFinderConverter.convertToPathFinderRequest;

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
    public Favorite createFavorite(FavoriteRequest request, Member member) {
        if (!pathFinderService.isValidPath(convertToPathFinderRequest(request))) {
            throw new IllegalArgumentException("출발역과 도착역을 잇는 경로가 없습니다.");
        }
        return favoriteRepository.save(createFavoriteEntity(request, member));
    }

    public List<FavoriteResponse> findFavorites(Member member) {
        return convertToFavoriteResponses(favoriteRepository.findByMember(member));
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, Member member) {
        if (!member.isThisYours(findFavoriteById(favoriteId))) {
            throw new NonMatchingMemberException("다른 회원의 즐겨찾기를 삭제할 수 없습니다.");
        }
        favoriteRepository.deleteById(favoriteId);
    }

    private Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("즐겨찾기 번호에 해당하는 즐겨찾기가 없습니다."));
    }

    private Favorite createFavoriteEntity(FavoriteRequest request, Member member) {
        return new Favorite(
                stationService.findStation(request.getSource()),
                stationService.findStation(request.getTarget()),
                member);
    }
}
