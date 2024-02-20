package nextstep.favorite.application;

import nextstep.exception.ApplicationException;
import nextstep.favorite.application.dto.FavoriteCreateRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.auth.ui.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.favorite.application.dto.FavoriteResponse.listOf;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final PathFinder pathFinder;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository, PathFinder pathFinder) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    @Transactional
    public long createFavorite(LoginMember loginMember, FavoriteCreateRequest request) {
        Member member = memberRepository.getBy(loginMember.getEmail());

        Station source = stationRepository.getBy(request.getSourceId());
        Station target = stationRepository.getBy(request.getTargetId());

        Path path = pathFinder.findPath();
        path.findShortestPath(source, target);

        favoriteRepository.existBy(source, target, member);

        Favorite savedFavorite = favoriteRepository.save(new Favorite(source, target, member));
        return savedFavorite.id();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.getBy(loginMember.getEmail());

        List<Favorite> favorites = favoriteRepository.findByMember(member);
        validateSize(favorites);

        return listOf(favorites);
    }

    private void validateSize(List<Favorite> favorites) {
        if(favorites.isEmpty()) {
            throw new ApplicationException("즐겨찾기가 없습니다.");
        }
    }

    @Transactional
    public void deleteFavorite(Long id) {
        favoriteRepository.getBy(id);
        favoriteRepository.deleteById(id);
    }
}
