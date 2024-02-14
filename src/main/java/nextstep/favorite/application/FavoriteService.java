package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteCreateRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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

    /**
     * TODO: LoginMember 를 추가로 받아서 FavoriteRequest 내용과 함께 Favorite 를 생성합니다.
     */
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

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return new ArrayList<>();
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
