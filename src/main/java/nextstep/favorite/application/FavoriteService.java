package nextstep.favorite.application;

import java.util.List;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteSimpleResponse;
import nextstep.favorite.application.exception.FavoriteException.NotCreatedException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.SectionEdges;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.exception.StationException.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        MemberRepository memberRepository, StationRepository stationRepository,
        LineRepository lineRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    /**
     * TODO: LoginMember 를 추가로 받아서 FavoriteRequest 내용과 함께 Favorite 를 생성합니다.
     *
     * @param request
     * @return
     */
    @Transactional
    public FavoriteSimpleResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(
            NotCreatedException::new);
        Station source = stationRepository.findById(request.getSource())
            .orElseThrow(StationNotFoundException::new);
        Station target = stationRepository.findById(request.getTarget())
            .orElseThrow(StationNotFoundException::new);
        List<Line> lines = lineRepository.findAll();
        SectionEdges edges = new SectionEdges(lines);
        PathFinder pathFinder = new PathFinder(edges);
        pathFinder.findShortedPath(source.getId(), target.getId());
        Favorite favorite = new Favorite(member.getId(), source.getId(), target.getId());
        favoriteRepository.save(favorite);
        return new FavoriteSimpleResponse(favorite.getId());
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     *
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
